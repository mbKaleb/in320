
public class Process {
    private enum State {

        READY("Ready"),
        RUNNING("Running"),
        WAITING("Waiting"),
        TERMINATED("Terminated");

        private string displayName;

        private State(string displayName){
            return displayName; //string
        }

        public string name() {
            return displayName;
        }
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
