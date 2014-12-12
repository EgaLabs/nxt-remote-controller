/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (c) 2014 EgaTuts & Esaú García - All Rights Reserved                *
 *                                                                               *
 * Open-source code licensed under the MIT License (the "License").              *
 *                                                                               *
 * Permission is hereby granted, free of charge, to any person obtaining a copy  *
 * of this software and associated documentation files (the "Software"), to deal *
 * in the Software without restriction, including without limitation the rights  *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell     *
 * copies of the Software, and to permit persons to whom the Software is         *
 * furnished to do so, subject to the following conditions:                      *
 *                                                                               *
 * The above copyright notice and this permission notice shall be included in    *
 * all copies or substantial portions of the Software.                           *
 *                                                                               *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR    *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,      *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE   *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER        *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN     *
 * THE SOFTWARE.                                                                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                                     *
 *                                                                                                                                                         *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                                      *
 *                                                                                                                                                         *
 * And the corresponding file at:                                                                                                                          *
 *                                                                                                                                                         *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/device/PairedDevice.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.device;

import android.bluetooth.BluetoothDevice;

public class PairedDevice {

  private BluetoothDevice _bluetooth_device;
  private String _name;
  private String _mac_address;
  private byte _signal;

  /*
   * Constructor.
   */
  public PairedDevice (String name, String mac_address, byte signal, BluetoothDevice device) {
    this._name = name;
    this._mac_address = mac_address;
    this._signal = signal;
    this._bluetooth_device = device;
  }

  /*
   * Getter and setter for name.
   */
  public String getName () {
    return this._name;
  }

  public void setName (String name) {
    this._name = name;
  }

  /*
   * Getter and setter for address.
   */
  public String getAddress () {
    return this._mac_address;
  }

  public void setAddress (String address) {
    this._mac_address = address;
  }

  /*
   * Getter and setter for signal.
   */
  public byte getSignal () {
    return this._signal;
  }

  public void setSignal (byte signal) {
    this._signal = signal;
  }

  /*
   * Getter and setter for connectivity.
   */
  public int getConnectivity () {
    return (int) ((float) (this._signal & 0xff) / 0xff * 100);
  }

  public void setConnectivity (int connectivity) {
    int sanitized = Math.min(100, Math.max(0, connectivity));
    byte binary = (byte) ((float) sanitized / 100 * 0xff);
    this.setSignal(binary);
  }

  /*
   * Getter and setter for bluetooth device.
   */
  public BluetoothDevice getBluetoothDevice () {
    return _bluetooth_device;
  }

  public void setBluetoothDevice (BluetoothDevice device) {
    _bluetooth_device = device;
  }

  /*
   * Exports a BluetoothDevice as PairedDevice.
   */
  public static PairedDevice from (BluetoothDevice device) {
    return new PairedDevice(device.getName(), device.getAddress(), (byte) 0, device);
  }

}