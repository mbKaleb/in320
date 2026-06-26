import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LogGenerator {

    public static void main(String[] args) throws IOException {
        // total lines, ERROR count, FATAL count  (totals: 156 ERROR / 66 FATAL)
        generate("LogFile1.txt", 600, 52, 23);
        generate("LogFile2.txt", 600, 54, 21);
        generate("LogFile3.txt", 600, 50, 22);
        System.out.println("Done. Three log files written to: " + System.getProperty("user.dir"));
    }

    private static void generate(String fileName, int total, int errors, int fatals)
            throws IOException {
        List<String> lines = new ArrayList<>(total);

        for (int i = 0; i < errors; i++) {
            lines.add("ERROR: Connection timeout on worker node " + i);
        }
        for (int i = 0; i < fatals; i++) {
            lines.add("FATAL: Unrecoverable kernel panic, code 0x" + Integer.toHexString(i));
        }
        int remaining = total - errors - fatals;
        for (int i = 0; i < remaining; i++) {
            lines.add(i % 2 == 0 ? "INFO: Task completed successfully"
                                 : "WARNING: Low disk space");
        }

        // shuffle
        Collections.shuffle(lines, new Random(42));

        Random clock = new Random(7);
        try (BufferedWriter w = new BufferedWriter(new FileWriter(fileName))) {
            for (String body : lines) {
                String ts = String.format("2025-06-24 %02d:%02d:%02d",
                        clock.nextInt(24), clock.nextInt(60), clock.nextInt(60));
                w.write("[" + ts + "] " + body);
                w.newLine();
            }
        }
        System.out.printf("%s: %d lines written (%d ERROR, %d FATAL)%n",
                fileName, total, errors, fatals);
    }
}
