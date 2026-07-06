
========================================
  PROCESS STATE SIMULATION
========================================

STEP 1: Initial State - All Processes Created (Ready State)

--- Current Process States ---
    p1: Ready
    p2: Ready
    p3: Ready

STEP 2: Scheduler Selects P1 to Run
Process: 1 running

--- Current Process States ---
    p1: Running
    p2: Ready
    p3: Ready

STEP 3: P1 Makes I/O Request (Disk Read)
Process: 1 waiting

--- Current Process States ---
    p1: Waiting
    p2: Ready
    p3: Ready

STEP 4: Scheduler Selects P2 to Run
Process: 2 running

--- Current Process States ---
    p1: Waiting
    p2: Running
    p3: Ready

STEP 5: I/O Completion for P1 (Disk Read Finished)
Process: 1 ready

--- Current Process States ---
    p1: Ready
    p2: Running
    p3: Ready

STEP 6: P2 Completes Execution
Process:2 terminated

--- Current Process States ---
    p1: Ready
    p2: Terminated
    p3: Ready

STEP 7: Scheduler Selects P1 to Run Again
Process: 1 running

--- Current Process States ---
    p1: Running
    p2: Terminated
    p3: Ready

STEP 8: P1 Completes Execution
Process:1 terminated

--- Current Process States ---
    p1: Terminated
    p2: Terminated
    p3: Ready

STEP 9: Scheduler Selects P3 to Run
Process: 3 running

--- Current Process States ---
    p1: Terminated
    p2: Terminated
    p3: Running

STEP 10: P3 Completes Execution
Process:3 terminated

--- Current Process States ---
    p1: Terminated
    p2: Terminated
    p3: Terminated

========================================
  SIMULATION COMPLETE - FINAL STATE
========================================

Final Status of All Processes:
  P1 [Terminated]
  P2 [Terminated]
  P3 [Terminated]

All processes have completed execution.