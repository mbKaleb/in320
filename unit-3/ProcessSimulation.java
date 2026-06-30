import java.util.ArrayList;
import java.util.List;

public class ProcessSimulation {
    private List<MyProcess> processes;

    public ProcessSimulation() {
        processes = new ArrayList<>();
        processes.add(new MyProcess(1));
        processes.add(new MyProcess(2));
        processes.add(new MyProcess(3));
    }

    public void printAllProcessStates() {
        System.out.println("\n--- Current Process States ---");
        for (MyProcess p : processes){
            p.printStatus();
        }
        System.out.println();
    }

    public void simulateProcessLifecycle() {
        System.out.println("========================================");
        System.out.println("  PROCESS STATE SIMULATION");
        System.out.println("========================================\n");

        // Step 1: Initial state - all processes Ready
        System.out.println("STEP 1: Initial State - All Processes Created (Ready State)");
        printAllProcessStates();
        sleep(1000);

        // Step 2: P1 Ready → Running
        System.out.println("STEP 2: Scheduler Selects P1 to Run");
        processes.get(0).transitionState(MyProcess.pState.RUNNING);
        printAllProcessStates();
        sleep(1000);

        // Step 3: P1 Running → Waiting
        System.out.println("STEP 3: P1 Makes I/O Request (Disk Read)");
        processes.get(0).transitionState(MyProcess.pState.WAITING);
        printAllProcessStates();
        sleep(1000);

        // Step 4: P2 Ready → Running
        System.out.println("STEP 4: Scheduler Selects P2 to Run");
        processes.get(1).transitionState(MyProcess.pState.RUNNING);
        printAllProcessStates();
        sleep(1000);

        // Step 5: P1 Waiting → Ready
        System.out.println("STEP 5: I/O Completion for P1 (Disk Read Finished)");
        processes.get(0).transitionState(MyProcess.pState.READY);
        printAllProcessStates();
        sleep(1000);

        // Step 6: P2 Running → Terminated
        System.out.println("STEP 6: P2 Completes Execution");
        processes.get(1).transitionState(MyProcess.pState.TERMINATED);
        printAllProcessStates();
        sleep(1000);

        // Step 7: P1 Ready → Running
        System.out.println("STEP 7: Scheduler Selects P1 to Run Again");
        processes.get(0).transitionState(MyProcess.pState.RUNNING);
        printAllProcessStates();
        sleep(1000);

        // Step 8: P1 Running → Terminated
        System.out.println("STEP 8: P1 Completes Execution");
        processes.get(0).transitionState(MyProcess.pState.TERMINATED);
        printAllProcessStates();
        sleep(1000);

        // Step 9: P3 Ready → Running
        System.out.println("STEP 9: Scheduler Selects P3 to Run");
        processes.get(2).transitionState(MyProcess.pState.RUNNING);
        printAllProcessStates();
        sleep(1000);

        // Step 10: P3 Running → Terminated
        System.out.println("STEP 10: P3 Completes Execution");
        processes.get(2).transitionState(MyProcess.pState.TERMINATED);
        printAllProcessStates();

        printFinalSummary();
    }

    public void printFinalSummary() {
        System.out.println("========================================");
        System.out.println("  SIMULATION COMPLETE - FINAL STATE");
        System.out.println("========================================");
        System.out.println("\nFinal Status of All Processes:");
        for (MyProcess p : processes) {
            System.out.println("  P" + p.getID() + " [" + p.getState().getName() + "]");
        }
        System.out.println("\nAll processes have completed execution.\n");
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ProcessSimulation sim = new ProcessSimulation();
        sim.simulateProcessLifecycle();
    }
}