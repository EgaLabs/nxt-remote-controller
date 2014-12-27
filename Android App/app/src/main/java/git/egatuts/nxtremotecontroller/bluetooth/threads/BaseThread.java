package git.egatuts.nxtremotecontroller.bluetooth.threads;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;

import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import git.egatuts.nxtremotecontroller.bluetooth.NXTConnector;
import git.egatuts.nxtremotecontroller.bluetooth.exception.ConnectionFailedException;
import git.egatuts.nxtremotecontroller.device.PairedDevice;

public abstract class BaseThread extends Thread {

  protected BluetoothUtils bluetooth_utils;
  protected final BluetoothSocket socket;
  protected BluetoothDevice device;
  protected NXTConnector connector;

  /*
   * Constructors.
   */
  public BaseThread (PairedDevice device, NXTConnector connector) throws IOException, ConnectionFailedException {
    this.connector = connector;
    this.device = device.getBluetoothDevice();
    this.bluetooth_utils = new BluetoothUtils();
    this.socket = this.bluetooth_utils.getSocketFrom(device);
  }

  public BaseThread (BluetoothDevice device, NXTConnector connector) throws IOException, ConnectionFailedException {
    this.connector = connector;
    this.device = device;
    this.bluetooth_utils = new BluetoothUtils();
    this.socket = this.bluetooth_utils.getSocketFrom(device);
  }

  public BaseThread (BluetoothSocket socket, NXTConnector connector) {
    this.connector = connector;
    this.device = socket.getRemoteDevice();
    this.socket = socket;
    this.bluetooth_utils = new BluetoothUtils();
  }

  /*
   * Getters for BluetoothSocket and BluetoothDevice.
   */
  public BluetoothSocket getSocket () {
    return this.socket;
  }

  public BluetoothDevice getDevice () {
    return this.device;
  }

  /*
   * Getters and setters for connector.
   */
  public void setConnector (NXTConnector connector) {
    this.connector = connector;
  }

  public NXTConnector getConnector () {
    return this.connector;
  }

  public void cancel () {
    try {
      this.socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
