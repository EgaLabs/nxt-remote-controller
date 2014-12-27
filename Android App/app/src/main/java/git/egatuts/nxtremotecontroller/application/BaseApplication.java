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
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE    *
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER         *
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  *
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN      *
 *  THE SOFTWARE.                                                                  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                              *
 *                                                                                                                                                                   *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                               *
 *                                                                                                                                                                   *
 *  And the corresponding file at:                                                                                                                                   *
 *                                                                                                                                                                   *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/application/BaseApplication.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.application;

import android.app.Application;
import android.bluetooth.BluetoothSocket;

import git.egatuts.nxtremotecontroller.bluetooth.NXTConnector;
import git.egatuts.nxtremotecontroller.device.PairedDevice;

/*
 *  Extended class of android.app.Application with 3 getters and setters to save instances of:
 *    PairedDevice
 *    NXTConnector
 *    BluetoothSocket
 *  The main reason is to access those objects through different activities when it's
 *  not possible to parse them as Seriazable or Parceable, like the BluetoothSocket class.
 */
public class BaseApplication extends Application {

  private PairedDevice device;
  private NXTConnector connector;
  private BluetoothSocket socket;

  /*
   *  Getter and setter for the PairedDevice object.
   */
  public void setDevice (PairedDevice device) {
    this.device = device;
  }

  public PairedDevice getDevice () {
    return this.device;
  }

  /*
   *  Getter and setter for the NXTConnector object.
   */
  public void setNXTConnector (NXTConnector connector) {
    this.connector = connector;
  }

  public NXTConnector getNXTConnector () {
    return this.connector;
  }

  /*
   *  Getter and setter for the BluetoothSocket object.
   */
  public void setSocket (BluetoothSocket socket) {
    this.socket = socket;
  }

  public BluetoothSocket getSocket () {
    return this.socket;
  }

}
