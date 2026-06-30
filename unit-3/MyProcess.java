import java.util.Optional;

public class MyProcess {

    public enum pState {

        READY("Ready"),
        RUNNING("Running"),
        WAITING("Waiting"),
        TERMINATED("Terminated");

        private String displayName;

        pState(String displayName){
            this.displayName = displayName; //string
        }

        public String getName() {
            return displayName;
        }
    }

    private pState state;
    // private String desc;
    private int id;


    MyProcess(int id) {
        this.id = id;
        this.state = pState.READY;
    }

    // Private getter functions
    private void isAlive() {
        if (this.state == pState.TERMINATED){
            throw new IllegalStateException("Error: Process terminated");
        }
        else return;
    }

    private void pTerminate() {
        this.isAlive();
        this.state = pState.TERMINATED;
        System.err.println("Process:" +id +" terminated");
    }

    private void pReady(){
        this.isAlive();
        this.state = pState.READY;
        System.err.println("Process: " + id + " ready");
    }

    private void pWait(){
        this.isAlive();
        this.state = pState.WAITING;
        System.err.println("Process: " + id + " waiting");

    }
    // public void pStart(){}
    private void pRun(){
        this.isAlive();
        this.state = pState.RUNNING;
        System.err.println("Process: " + id + " running");

    }

    // public setter + getter functions
    public void transitionState(pState newState){
        switch (newState) {
            case READY:
                this.pReady();
                break;
            case RUNNING:
                this.pRun();
                break;
            case WAITING:
                this.pWait();
                break;
            case TERMINATED:
                this.pTerminate();
                break;
            default:
                throw new IllegalArgumentException("Unknown state: " + newState);
        }

    }

    public pState getState() {
        return state;
    }

    public int getID() {
        return id;
    }

    public void printStatus() {
        System.out.println("    p" + id + ": " + state.getName());
    }

}
