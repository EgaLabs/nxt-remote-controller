package git.egatuts.nxtremotecontroller.utils;

public class TokenRequester extends Thread {

  private OnFinishListener onFinishListener;
  private OnExceptionListener onExceptionListener;
  private Runnable onRun;

  public interface OnFinishListener {
    public void onFinish (String data);
  }

  public interface OnExceptionListener {
    public void onError (Exception e);
  }

  public void setOnFinishListener (OnFinishListener listener) {
    this.onFinishListener = listener;
  }

  public OnFinishListener getOnFinishListener () {
    return this.onFinishListener;
  }

  public void setOnExceptionListener (OnExceptionListener listener) {
    this.onExceptionListener = listener;
  }

  public OnExceptionListener getOnExceptionListener () {
    return this.onExceptionListener;
  }

  public void setRunnable (Runnable runnable) {
    this.onRun = runnable;
  }

  public Runnable getRunnable () {
    return this.onRun;
  }

  public void finishRequest (String data) {
    if (this.onFinishListener != null) this.onFinishListener.onFinish(data);
  }

  public void cancelRequest (Exception e) {
    if (this.onExceptionListener != null) this.onExceptionListener.onError(e);
  }

  @Override
  public void run () {
    this.onRun.run();
  }

}
