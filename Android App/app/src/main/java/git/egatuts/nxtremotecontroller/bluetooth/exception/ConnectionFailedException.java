package git.egatuts.nxtremotecontroller.bluetooth.exception;

public class ConnectionFailedException extends Exception {
  public ConnectionFailedException () {
    super();
  }
  public ConnectionFailedException (String msg) {
    super(msg);
  }
}