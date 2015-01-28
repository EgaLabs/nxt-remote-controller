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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                                      *
 *                                                                                                                                                                           *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                                       *
 *                                                                                                                                                                           *
 *  And the corresponding file at:                                                                                                                                           *
 *                                                                                                                                                                           *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/receiver/BluetoothDiscoveryReceiver.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.listener.BaseListener;

/*
 *  BluetoothDiscoveryReceiver class used to register a BroadcastReceiver using an BluetoothDiscoveryListener.
 */
public class BluetoothDiscoveryReceiver extends BaseReceiver {

  /*
   *  Constructors.
   */
  private void init () {
    this.BROADCAST_CALLBACKS_STATES.put("ON_DISCOVER_FINISH", true);
    this.BROADCAST_CALLBACKS_STATES.put("ON_DISCOVER_START", true);
    this.BROADCAST_CALLBACKS_STATES.put("ON_DEVICE_FOUND", true);
  }

  public BluetoothDiscoveryReceiver (Context context) {
    super(context);
    this.init();
  }

  public BluetoothDiscoveryReceiver (Context context, BaseListener listener) {
    super(context, listener);
    this.init();
  }

  /*
   *  Returns a new PairedDevice instance from the BluetoothDevice.
   */
  @Override
  public Object getIntentData (Intent intent) {
    return PairedDevice.from((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
  }

  /*
   *  Returns the IntentFilter with BluetoothAdapter#ACTION_DISCOVERY_STARTED,
   *  BluetoothAdapter#ACTION_DISCOVERY_FINISHED and BluetoothDevice#ACTION_FOUND.
   */
  @Override
  public IntentFilter getIntentFilter () {
    IntentFilter intent = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
    intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    intent.addAction(BluetoothDevice.ACTION_FOUND);
    return intent;
  }
}
