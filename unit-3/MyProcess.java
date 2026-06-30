import java.util.Optional;

public class MyProcess {

    private enum State {

        READY("Ready"),
        RUNNING("Running"),
        WAITING("Waiting"),
        TERMINATED("Terminated");

        private String displayName;

        State(String displayName){
            this.displayName = displayName; //string
        }

        public String getName() {
            return displayName;
        }
    }

    private State state;
    // private String desc;
    private int id;


    MyProcess(int id) {
        this.id = id;
        this.state = State.READY;
    }

    // Private getter functions
    private void isAlive() {
        if (this.state == State.TERMINATED){
            throw new IllegalStateException("Error: Process terminated");
        }
        else return;
    }

    private void pTerminate() {
        this.isAlive();
        this.state = State.TERMINATED;
        System.err.println("Process:" +id +" terminated");
    }

    private void pReady(){
        this.isAlive();
        this.state = State.READY;
        System.err.println("Process: " + id + " ready");
    }

    private void pWait(){
        this.isAlive();
        this.state = State.WAITING;
        System.err.println("Process: " + id + " waiting");

    }
    // public void pStart(){}
    private void pRun(){
        this.isAlive();
        this.state = State.RUNNING;
        System.err.println("Process: " + id + " running");

    }

    // public setter + getter functions
    public void transitionState(State newState){
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

    public State getState() {
        return state;
    }

    public int getID() {
        return id;
    }

    public void printStatus() {
        System.out.println("    p" + id + ": " + state.getName());
    }

}
