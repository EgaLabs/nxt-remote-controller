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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                     *
 *                                                                                                                                         *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                      *
 *                                                                                                                                         *
 * And the corresponding file at:                                                                                                          *
 *                                                                                                                                         *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/bluetooth/BluetoothReceiver.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothCallback.OnDiscoveryListener;

public class BluetoothReceiver extends BroadcastReceiver {

  private BluetoothCallback.OnDiscoveryListener onDiscoveryListener;

  /*
   * Returns intent filter to listen for new devices and discovery ending.
   */
  public static IntentFilter getIntentFilter () {
    IntentFilter filter = new IntentFilter();
    filter.addAction(BluetoothDevice.ACTION_FOUND);
    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
    return filter;
  }

  /*
   * Getter and setter for OnDiscoveryListener.
   */
  public void setOnDiscoveryListener (OnDiscoveryListener listener) {
    onDiscoveryListener = listener;
  }

  public OnDiscoveryListener getOnDiscoveryListener () {
    return onDiscoveryListener;
  }

  /*
   * Fired when one of the filtered intent gets fired by the Android system.
   */
  public void onReceive (Context context, Intent intent) {
    String action = intent.getAction();
    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
      BluetoothDevice bluetooth_device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
      if (bluetooth_device != null) onDiscoveryListener.onDiscover(bluetooth_device, intent);
    } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
      onDiscoveryListener.onFinish();
    } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
      onDiscoveryListener.onStart();
    }
  }

}