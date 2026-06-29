
public class Process {
    private enum State {
        READY,RUNNING,WAITING,TERMINATED;
    }

    State state;



    public Process() {
        this.state = State.READY;
    }
    
    public void Pause() {
        this.state = State.WAITING;
    }


    

    // public changeState(this) {

    // }
}
