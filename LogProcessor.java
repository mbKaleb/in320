import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogProcessor {

    private static final int PASSES = 6000;

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Worker thread: counts errors and fatal errors in a single log file.
    static class LogWorker extends Thread {
        private final String fileName;
        int errorCount = 0;   // read by main thread after join
        int fatalCount = 0;

        LogWorker(String fileName) {
            this.fileName = fileName;
            setName("Thread-" + fileName.replace(".txt", ""));
        }

        @Override
        public void run() {
            System.out.printf("[%s] Started processing %s at %s%n",
                    getName(), fileName, LocalTime.now().format(TIME));

            try {
                for (int pass = 0; pass < PASSES; pass++) {
                    int errors = 0;
                    int fatals = 0;
                    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.contains("ERROR")) {
                                errors++;
                            } else if (line.contains("FATAL")) {
                                fatals++;
                            }
                        }
                    }
                    errorCount = errors;   // same result each pass
                    fatalCount = fatals;
                }
            } catch (IOException e) {
                System.err.printf("[%s] Could not read %s: %s%n",
                        getName(), fileName, e.getMessage());
                return;
            }

            System.out.printf("[%s] Found %d ERROR entries and %d FATAL entries%n",
                    getName(), errorCount, fatalCount);
            System.out.printf("[%s] Finished processing %s at %s%n",
                    getName(), fileName, LocalTime.now().format(TIME));
        }
    }

      //Reads major (hard) page faults for this process from /proc/self/stat.
      //Returns -1 on non-Linux
    private static long getHardPageFaults() {
        try {
            String stat = new String(Files.readAllBytes(Paths.get("/proc/self/stat")));
            // field 12 (1-based) = majflt; index 11 in the space-split array
            return Long.parseLong(stat.trim().split("\\s+")[11]);
        } catch (Exception e) {
            return -1;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long pid         = ProcessHandle.current().pid();
        long startMs     = System.currentTimeMillis();
        Runtime rt       = Runtime.getRuntime();

        long initialMemoryMB = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);

        // Track peaks in arrays so the lambda can write to them
        long[]   peakMemoryMB = {initialMemoryMB};
        double[] peakCpuPct   = {0.0};

        // Cast to the com.sun extension interface that exposes per-process CPU load
        com.sun.management.OperatingSystemMXBean osBean =
                (com.sun.management.OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();

        // Background sampler -- daemon so it never prevents JVM exit
        Thread monitor = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                long usedMB = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
                if (usedMB > peakMemoryMB[0]) peakMemoryMB[0] = usedMB;

                double cpuPct = osBean.getProcessCpuLoad() * 100.0;
                if (cpuPct > peakCpuPct[0]) peakCpuPct[0] = cpuPct;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "Monitor");
        monitor.setDaemon(true);
        monitor.start();

        String[] files = {"LogFile1.txt", "LogFile2.txt", "LogFile3.txt"};
        LogWorker[] workers = new LogWorker[files.length];

        // start one thread per file
        for (int i = 0; i < files.length; i++) {
            workers[i] = new LogWorker(files[i]);
            workers[i].start();
        }

        // wait for each thread, then add its results to the running totals
        int totalErrors = 0;
        int totalFatals = 0;
        for (LogWorker worker : workers) {
            worker.join();
            totalErrors += worker.errorCount;
            totalFatals += worker.fatalCount;
        }

        monitor.interrupt();
        monitor.join();

        long durationSec = (System.currentTimeMillis() - startMs) / 1000;
        long hardFaults  = getHardPageFaults();

        System.out.println();
        System.out.println("[Main Thread] Aggregated Results:");
        System.out.println("Total ERROR entries: " + totalErrors);
        System.out.println("Total FATAL entries: " + totalFatals);

        System.out.println();
        System.out.printf("[INFO] Java process started: PID %d%n", pid);
        System.out.printf("[INFO] Initial Memory Usage: %d MB%n", initialMemoryMB);
        System.out.printf("[INFO] Memory Allocation Peak: %d MB%n", peakMemoryMB[0]);
        System.out.printf("[INFO] CPU Peak: %.0f%%%n", peakCpuPct[0]);
        System.out.printf("[INFO] Program Duration: %d seconds%n", durationSec);
        if (hardFaults >= 0) {
            System.out.printf("[INFO] Hard Page Faults Observed: %d%n", hardFaults);
        } else {
            System.out.println("[INFO] Hard Page Faults Observed: N/A (Linux only)");
        }
    }
}
