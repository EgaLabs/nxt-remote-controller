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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                         *
 *                                                                                                                                             *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                          *
 *                                                                                                                                             *
 * And the corresponding file at:                                                                                                              *
 *                                                                                                                                             *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/bluetooth/listener/BaseListener.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.bluetooth.listener;

import android.content.Context;
import android.content.Intent;

public abstract class BaseListener {

  /*
   * Defines which methods are defined. By default none.
   */
  public boolean ON_CONNECTION                   = false;
  public boolean ON_DISCOVER_FINISH              = false;
  public boolean ON_DISCOVER_START               = false;
  public boolean ON_LOCAL_NAME_CHANGE            = false;
  public boolean ON_SCAN_MODE_CHANGE             = false;
  public boolean ON_STATE_CHANGE                 = false;
  public boolean ON_LOW_LEVEL_CONNECT            = false;
  public boolean ON_LOW_LEVEL_DISCONNECT         = false;
  public boolean ON_LOW_LEVEL_DISCONNECT_REQUEST = false;
  public boolean ON_BOND_STATE_CHANGE            = false;
  public boolean ON_DEVICE_CLASS_CHANGE          = false;
  public boolean ON_DEVICE_FOUND                 = false;
  public boolean ON_REMOTE_NAME_CHANGE           = false;

  /*
   * Methods for Broadcast Actions from BluetoothAdapter.
   */
  public abstract void onConnectionChange (Context context, Intent intent);
  public abstract void onDiscoveryFinish  (Context context, Intent intent);
  public abstract void onDiscoveryStart   (Context context, Intent intent);
  public abstract void onLocalNameChange  (Context context, Intent intent);
  public abstract void onScanModeChange   (Context context, Intent intent);
  public abstract void onStateChange      (Context context, Intent intent);

  /*
   * Methods for Broadcast Actions from BluetoothDevice.
   */
  public abstract void onLowLevelConnect           (Context context, Intent intent);
  public abstract void onLowLevelDisconnect        (Context context, Intent intent);
  public abstract void onLowLevelDisconnectRequest (Context context, Intent intent);
  public abstract void onBondStateChange           (Context context, Intent intent);
  public abstract void onDeviceClassChange         (Context context, Intent intent);
  public abstract void onDeviceFound               (Context context, Intent intent);
  public abstract void onRemoteNameChange          (Context context, Intent intent);

}