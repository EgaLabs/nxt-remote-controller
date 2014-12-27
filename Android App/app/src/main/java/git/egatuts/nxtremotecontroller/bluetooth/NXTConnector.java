package git.egatuts.nxtremotecontroller.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;

import git.egatuts.nxtremotecontroller.activity.BaseActivity;
import git.egatuts.nxtremotecontroller.bluetooth.exception.ConnectionFailedException;
import git.egatuts.nxtremotecontroller.bluetooth.threads.ConnectThread;
import git.egatuts.nxtremotecontroller.bluetooth.threads.ConnectedThread;
import git.egatuts.nxtremotecontroller.device.PairedDevice;

public class NXTConnector {

  private int state;
  private int error_code;
  private Handler handler;
  private BluetoothUtils bluetooth_utils;
  private BluetoothSocket socket;
  private BaseActivity activity;

  private ConnectThread connect_thread;
  private ConnectedThread connected_thread;

  public NXTConnector (Handler handler) {
    this.handler = handler;
    this.bluetooth_utils = new BluetoothUtils();
  }

  public Handler getHandler () {
    return this.handler;
  }

  public synchronized void setHandler (Handler handler) {
    this.handler = handler;
  }

  public BluetoothSocket getSocket () {
    return this.socket;
  }

  public synchronized void setSocket (BluetoothSocket socket) {
    this.socket = socket;
  }

  public synchronized void sendMessage (int category, int state) {
    if (this.handler != null) {
      this.handler.obtainMessage(category, state).sendToTarget();
    }
  }

  public synchronized void setState (int state) {
    this.state = state;
    this.sendMessage(BluetoothConstants.ACTION_STATE_CHANGED, this.state);
  }

  public synchronized void setError (int error_code) {
    this.error_code = error_code;
    this.sendMessage(BluetoothConstants.ACTION_ERROR_ENCOUNTERED, this.error_code);
  }

  public synchronized void setConnectionAsFailed () {
    this.setState(BluetoothConstants.STATE_DISCONNECTED);
  }

  public synchronized int getState () {
    return this.state;
  }

  public synchronized int getError () {
    return this.error_code;
  }

  public synchronized void resetError () {
    this.error_code = BluetoothConstants.EMPTY_VALUE;
  }

  public synchronized boolean isErrorEmpty () {
    return this.error_code == BluetoothConstants.EMPTY_VALUE;
  }

  public synchronized ConnectThread getConnectThread () {
    return this.connect_thread;
  }

  public synchronized ConnectedThread getConnectedThread () {
    return this.connected_thread;
  }

  public synchronized void stopConnectThread () {
    if (this.connect_thread != null) {
      this.connect_thread.cancel();
      this.connect_thread = null;
    }
  }

  public synchronized void stopConnectedThread () {
    if (this.connected_thread != null) {
      this.connected_thread.cancel();
      this.connected_thread = null;
    }
  }

  public boolean isDisonnected () {
    return this.state == BluetoothConstants.STATE_DISCONNECTED;
  }

  public boolean isConnected () {
    return this.state == BluetoothConstants.STATE_CONNECTED;
  }

  public boolean isConnecting () {
    return this.state == BluetoothConstants.STATE_CONNECTING;
  }

  /*
   * Closes all connections and stops all threads.
   */
  public synchronized void closeConnections () {
    this.stopConnectThread();
    this.stopConnectedThread();
    this.setState(BluetoothConstants.STATE_DISCONNECTED);
  }

  /*
   * Opens a new connection. When connection is successful a new ConnectionThread is opened.
   */
  public synchronized void openConnection (PairedDevice device) throws IOException {
    this.openConnection(device.getBluetoothDevice());
  }

  public synchronized void openConnection (BluetoothDevice device) throws IOException {
    this.stopConnectThread();
    this.stopConnectedThread();
    try {
      this.connect_thread = new ConnectThread(device, this);
    } catch (ConnectionFailedException e) {
      e.printStackTrace();
      this.setError(BluetoothConstants.ERROR_CREATING_SOCKET);
      return;
    }
    this.connect_thread.start();
  }

  public synchronized void establishConnection (BluetoothSocket socket, PairedDevice device) {
    this.establishConnection(socket, device.getBluetoothDevice());
  }

  public synchronized void establishConnection (BluetoothSocket socket, BluetoothDevice device) {
    this.connect_thread = null;
    this.stopConnectedThread();
    this.connected_thread = new ConnectedThread(socket, this);
    this.connected_thread.start();
  }



  public void motors(byte l, byte r, boolean speedReg, boolean motorSync) {
    byte[] data = { 0x0c, 0x00, (byte) 0x80, 0x04, 0x02, 0x32, 0x07, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00,
            0x0c, 0x00, (byte) 0x80, 0x04, 0x01, 0x32, 0x07, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00 };

    //Log.i("NXT", "motors: " + Byte.toString(l) + ", " + Byte.toString(r));

    data[5] = l;
    data[19] = r;
    if (speedReg) {
      data[7] |= 0x01;
      data[21] |= 0x01;
    }
    if (motorSync) {
      data[7] |= 0x02;
      data[21] |= 0x02;
    }
    write(data);
  }

  public void motor(int motor, byte power, boolean speedReg, boolean motorSync) {
    byte[] data = { 0x0c, 0x00, (byte) 0x80, 0x04, 0x02, 0x32, 0x07, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00 };

    //Log.i("NXT", "motor: " + Integer.toString(motor) + ", " + Byte.toString(power));

    if (motor == 0) {
      data[4] = 0x02;
    } else {
      data[4] = 0x01;
    }
    data[5] = power;
    if (speedReg) {
      data[7] |= 0x01;
    }
    if (motorSync) {
      data[7] |= 0x02;
    }
    write(data);
  }

  public void motors3(byte l, byte r, byte action, boolean speedReg, boolean motorSync) {
    byte[] data = { 0x0c, 0x00, (byte) 0x80, 0x04, 0x02, 0x32, 0x07, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00,
            0x0c, 0x00, (byte) 0x80, 0x04, 0x01, 0x32, 0x07, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00,
            0x0c, 0x00, (byte) 0x80, 0x04, 0x00, 0x32, 0x07, 0x00, 0x00, 0x20, 0x00, 0x00, 0x00, 0x00 };

    //Log.i("NXT", "motors3: " + Byte.toString(l) + ", " + Byte.toString(r) + ", " + Byte.toString(action));

    data[5] = l;
    data[19] = r;
    data[33] = action;
    if (speedReg) {
      data[7] |= 0x01;
      data[21] |= 0x01;
    }
    if (motorSync) {
      data[7] |= 0x02;
      data[21] |= 0x02;
    }
    write(data);
  }

  private void write(byte[] out) {
    ConnectedThread r;
    synchronized (this) {
      if (!this.isConnected()) {
        return;
      }
      r = this.connected_thread;
    }
    r.write(out);
  }

}
