package git.egatuts.nxtremotecontroller.bluetooth.exception;

public class ConnectionLostException extends Exception {
  public ConnectionLostException () {
    super();
  }
  public ConnectionLostException (String msg) {
    super(msg);
  }
}