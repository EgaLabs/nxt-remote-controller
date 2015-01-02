/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  Copyright (c) 2014 EgaTuts & Esaú García - All Rights Reserved                 *
 *                                                                                 *
 *  Open-source code licensed under the MIT License (the "License").               *
 *                                                                                 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy   *
 *  of this software and associated documentation files (the "Software"), to deal  *
 *  in the Software without restriction, including without limitation the rights   *
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell      *
 *  copies of the Software, and to permit persons to whom the Software is          *
 *  furnished to do so, subject to the following conditions:                       *
 *                                                                                 *
 *  The above copyright notice and this permission notice shall be included in     *
 *  all copies or substantial portions of the Software.                            *
 *                                                                                 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR     *
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,       *
 *  FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE    *
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER         *
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  *
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN      *
 *  THE SOFTWARE.                                                                  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                        *
 *                                                                                                                                                             *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                         *
 *                                                                                                                                                             *
 *  And the corresponding file at:                                                                                                                             *
 *                                                                                                                                                             *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/bluetooth/NXTConnector.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import git.egatuts.nxtremotecontroller.activity.ControllerActivity;
import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.exception.SocketCreationException;
import git.egatuts.nxtremotecontroller.thread.ConnectThread;
import git.egatuts.nxtremotecontroller.thread.ConnectedThread;

/*
 *  Wrapper used in the BaseThread class used to store all the variables like
 *  the context and activity, resources, bluetooth utils, global utils, etc.
 */
public class NXTConnector {

  public static final int EMPTY_FIELD = 0;

  public static final int WHAT_CHANGE_STATE      = 1;
  public static final int WHAT_ERROR_ENCOUNTERED = 2;

  public static final int STATE_CHANGE_PREPARING_CONNECTION = 3;
  public static final int STATE_CHANGE_CREATING_SOCKET      = 4;
  public static final int STATE_CHANGE_CONNECTING           = 5;
  public static final int STATE_CHANGE_CONNECTED            = 6;

  public static final int ERROR_UNEXPECTED         = 7;
  public static final int ERROR_REQUEST_FAILED     = 8;
  public static final int ERROR_SOCKET_CREATE      = 9;
  public static final int ERROR_CONNECTION_CLOSED  = 10;
  public static final int ERROR_CONNECTION_LOST    = 11;

  private ControllerActivity activity;
  private Handler handler;
  private BluetoothDevice device;
  private BluetoothSocket socket;

  private ConnectThread connectThread;
  private ConnectedThread connectedThread;

  private int state;
  private int error;

  /*
   *  Constructor.
   */
  public NXTConnector () {}

  public NXTConnector (ControllerActivity activity, PairedDevice device, Handler handler) {
    this.activity = activity;
    this.device = device.getBluetoothDevice();
    this.handler = handler;
  }

  /*
   *  Getter and setter for the Handler.
   */
  public void setHandler (Handler handler) {
    this.handler = handler;
  }

  public Handler getHandler () {
    return this.handler;
  }

  /*
   *  Getter and setter for the BluetoothDevice.
   */
  public void setDevice (BluetoothDevice device) {
    this.device = device;
  }

  public BluetoothDevice getDevice () {
    return this.device;
  }

  /*
   *  Getter and setter for BluetoothSocket.
   */
  public void setSocket (BluetoothSocket socket) {
    this.socket = socket;
  }

  public BluetoothSocket getSocket () {
    return this.socket;
  }

  /*
   *  Getter and setter for the BaseActivity.
   */
  public void setActivity (ControllerActivity activity) {
    this.activity = activity;
  }

  public ControllerActivity getActivity () {
    return this.activity;
  }

  /*
   *  Sends a message through the handler.
   */
  public void sendMessage (int category, int state, int state2) {
    if (this.handler != null) {
      this.handler.obtainMessage(category, state, state2).sendToTarget();
    }
  }

  /*
   *  Changes the state and sends it through the handler.
   */
  public synchronized void setState (int state) {
    this.state = state;
    this.sendMessage(NXTConnector.WHAT_CHANGE_STATE, state, this.error);
  }

  /*
   *  Changes the error and sends it through the handler.
   */
  public synchronized void setError (int error) {
    this.error = error;
    this.sendMessage(NXTConnector.WHAT_ERROR_ENCOUNTERED, error, this.state);
  }

  /*
   *  Getters for state and error.
   */
  public synchronized int getState () {
    return this.state;
  }

  public synchronized int getError () {
    return this.error;
  }

  /*
   *  Getters for the state and the error that removes them.
   */
  public synchronized int popState () {
    int state = this.state;
    this.state = NXTConnector.EMPTY_FIELD;
    return state;
  }

  public synchronized int popError () {
    int error = this.error;
    this.error = NXTConnector.EMPTY_FIELD;
    return error;
  }

  /*
   *  Getters for the Threads.
   */
  public ConnectThread getConnectThread () {
    return this.connectThread;
  }

  public ConnectedThread getConnectedThread () {
    return this.connectedThread;
  }

  /*
   *  Closes all the connections and it's threads.
   */
  public synchronized void closeConnectThread () {
    if (this.connectThread != null) {
      this.connectThread.stopThread();
      this.connectThread = null;
    }
  }

  public synchronized void closeConnectedThread () {
    if (this.connectedThread != null) {
      this.connectedThread.stopThread();
      this.connectedThread = null;
    }
  }

  public synchronized void closeAllThreads () {
    this.closeConnectThread();
    this.closeConnectedThread();
  }

  /*
   *  State checkers for states.
   */
  public static boolean isDisconnected (int state) {
    return state == NXTConnector.EMPTY_FIELD;
  }

  public static boolean isPreparingConnection (int state) {
    return state == NXTConnector.STATE_CHANGE_PREPARING_CONNECTION;
  }

  public static boolean isCreatingSocket (int state) {
    return state == NXTConnector.STATE_CHANGE_CREATING_SOCKET;
  }

  public static boolean isConnecting (int state) {
    return state == NXTConnector.STATE_CHANGE_CONNECTING;
  }

  public static boolean isConnected (int state) {
    return state == NXTConnector.STATE_CHANGE_CONNECTED;
  }

  /*
   *  State checkers for errors.
   */
  public static boolean connectionFailed (int state, int error) {
    return state == NXTConnector.EMPTY_FIELD && error != NXTConnector.EMPTY_FIELD;
  }

  public static boolean connectionUnexpectedFailed (int state, int error) {
    return state == NXTConnector.EMPTY_FIELD && error == NXTConnector.ERROR_UNEXPECTED;
  }

  public static boolean connectionSocketFailed (int state, int error) {
    return state == NXTConnector.EMPTY_FIELD && error == NXTConnector.ERROR_SOCKET_CREATE;
  }

  public static boolean connectionRequestFailed (int state, int error) {
    return state == NXTConnector.EMPTY_FIELD && error == NXTConnector.ERROR_REQUEST_FAILED;
  }

  public static boolean connectionClosed (int state, int error) {
    return state == NXTConnector.EMPTY_FIELD && error == NXTConnector.ERROR_CONNECTION_CLOSED;
  }

  public static boolean connectionLost (int state, int error) {
    return state == NXTConnector.EMPTY_FIELD && error == NXTConnector.ERROR_CONNECTION_LOST;
  }

  /*
   *  Methods used to define the states.
   */
  public synchronized void setStatePreparing () {
    this.error = NXTConnector.EMPTY_FIELD;
    this.setState(NXTConnector.STATE_CHANGE_PREPARING_CONNECTION);
  }

  public synchronized void setStateCreatingSocket () {
    this.error = NXTConnector.EMPTY_FIELD;
    this.setState(NXTConnector.STATE_CHANGE_CREATING_SOCKET);
  }

  public synchronized void setStateConnecting () {
    this.error = NXTConnector.EMPTY_FIELD;
    this.setState(NXTConnector.STATE_CHANGE_CONNECTING);
  }

  public synchronized void setStateConnected () {
    this.error = NXTConnector.EMPTY_FIELD;
    this.setState(NXTConnector.STATE_CHANGE_CONNECTED);
  }

  /*
   *  Methods used to define errors.
   */
  public synchronized void setConnectionUnexpectedError () {
    this.state = NXTConnector.EMPTY_FIELD;
    this.setError(NXTConnector.ERROR_UNEXPECTED);
  }

  public synchronized void setConnectionSocketFailed () {
    this.state = NXTConnector.EMPTY_FIELD;
    this.setError(NXTConnector.ERROR_SOCKET_CREATE);
  }

  public synchronized void setConnectionRequestFailed () {
    this.state = NXTConnector.EMPTY_FIELD;
    this.setError(NXTConnector.ERROR_SOCKET_CREATE);
  }

  public synchronized void setConnectionClosed () {
    this.state = NXTConnector.EMPTY_FIELD;
    this.setError(NXTConnector.ERROR_CONNECTION_CLOSED);
  }

  public synchronized void setConnectionLost () {
    this.state = NXTConnector.EMPTY_FIELD;
    this.setError(NXTConnector.ERROR_CONNECTION_LOST);
  }

  /*
   *  Returns the from the activity BluetoothUtils
   */
  public BluetoothUtils getBluetoothUtils () {
    return this.activity.getBluetoothUtils();
  }

  /*
   *  Creates a socket from the stored device.
   */
  public void createSocket () throws SocketCreationException {
    this.socket = this.getBluetoothUtils().getSocketFrom(this.device);
  }

  /*
   *  These methods configure the device or the socket closing all the connections.
   */
  public synchronized void configureSocket (BluetoothSocket socket) {
    this.socket = socket;
    this.device = socket.getRemoteDevice();
  }

  public synchronized void configureDevice (BluetoothDevice device) {
    this.device = device;
    try {
      this.createSocket();
    } catch (SocketCreationException e) {
      this.setError(NXTConnector.ERROR_SOCKET_CREATE);
    }
  }

  /*
   *  Threads starters and stoppers.
   */
  public synchronized void startConnectThread () {
    if (this.connectThread != null && this.connectThread.isRunning()) {
      this.connectThread.stopThread();
    }
    this.connectThread = new ConnectThread(this);
    this.connectThread.startThread();
  }

  public synchronized void stopConnectThread () {
    if (this.connectThread != null) {
      this.connectThread.stopThread();
    }
  }

  public synchronized void startConnectedThread () {
    if (this.connectedThread != null && this.connectedThread.isRunning()) {
      this.connectedThread.stopThread();
    }
    this.connectedThread = new ConnectedThread(this);
    this.connectedThread.startThread();
  }

  public synchronized void stopConnectedThread () {
    if (this.connectedThread != null) {
      this.connectedThread.stopThread();
    }
  }

  /*
   *  Controls motors.
   */
  public void motorA (double power, boolean speedReg, boolean syncMotors) {
    this.connectedThread.write(BluetoothConstants.motorA(power, speedReg, syncMotors));
  }

  public void motorB (double power, boolean speedReg, boolean syncMotors) {
    this.connectedThread.write(BluetoothConstants.motorB(power, speedReg, syncMotors));
  }

  public void motorC (double power, boolean speedReg, boolean syncMotors) {
    this.connectedThread.write(BluetoothConstants.motorC(power, speedReg, syncMotors));
  }

  public void motorBC (double powerLeft, double powerRight, boolean speedReg, boolean syncMotors) {
    this.connectedThread.write(BluetoothConstants.motorBC(powerLeft, powerRight, speedReg, syncMotors));
  }

}