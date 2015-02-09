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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE   *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER        *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN     *
 * THE SOFTWARE.                                                                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                                           *
 *                                                                                                                                                               *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                                            *
 *                                                                                                                                                               *
 * And the corresponding file at:                                                                                                                                *
 *                                                                                                                                                               *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/bluetooth/BluetoothUtils.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.exception.SocketCreationException;

/*
 *  Global utility-belt class used to access properties from the bluetooth adapter and do some
 *  dirty work like pairing a device using Java Reflection.
 */
public class BluetoothUtils {

  private BluetoothAdapter bluetooth_adapter;

  /*
   *  Constructor.
   */
  private void initAdapter () {
    bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();
  }

  public BluetoothUtils () {
    initAdapter();
  }

  /*
   *  Returns the adapter if exists or creates a new instance.
   */
  public BluetoothAdapter getAdapter () {
    if (bluetooth_adapter == null) initAdapter();
    return bluetooth_adapter;
  }

  /*
   *  State checkers for enabled and discovering.
   */
  public boolean isEnabled () {
    return getAdapter().isEnabled();
  }

  public boolean isDiscovering () {
    return getAdapter().isDiscovering();
  }

  /*
   *  Starts and cancels bluetooth discovery.
   */
  public boolean startDiscovery () {
    return this.getAdapter().startDiscovery();
  }

  public boolean cancelDiscovery () {
    return this.getAdapter().startDiscovery();
  }

  /*
   *  Enables and disables bluetooth adapter.
   */
  public boolean enable () {
    return this.getAdapter().enable();
  }

  public boolean disable () {
    return this.getAdapter().disable();
  }

  /*
   *  Returns all bonded devices as an ArrayList of PairedDevice instances.
   */
  public ArrayList<PairedDevice> getDevices () {
    Set<BluetoothDevice> devices = bluetooth_adapter.getBondedDevices();
    ArrayList<PairedDevice> devices_set = new ArrayList<PairedDevice>();
    PairedDevice new_device;
    for (BluetoothDevice old_device : devices) {
      new_device = PairedDevice.from(old_device);
      devices_set.add(new_device);
    }
    return devices_set;
  }

  /*
   *  Pairs a bluetooth device using KitKat API if available.
   */
  public boolean pair (PairedDevice paired_device) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      return paired_device.getBluetoothDevice().createBond();
    } else {
      try {
        BluetoothDevice device = paired_device.getBluetoothDevice();
        Method method = device.getClass().getMethod("createBond", (Class[]) null);
        return (boolean) method.invoke(device, (Object[]) null);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
      return false;
    }
  }

  /*
   *  Gets the socket of a device.
   */
  public BluetoothSocket getSocketFrom (PairedDevice device) throws SocketCreationException {
    return this.getSocketFrom(device.getBluetoothDevice());
  }

  public BluetoothSocket getSocketFrom (BluetoothDevice device) throws SocketCreationException {
    BluetoothSocket socket = null;
    try {
      socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
    } catch (IOException e) {
      //e.printStackTrace();
      try {
        Method method = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
        socket = (BluetoothSocket) method.invoke(device, Integer.valueOf(1));
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e2) {
        //e2.printStackTrace();
      } catch (Exception e3) {
        //e3.printStackTrace();
        throw new SocketCreationException();
      }
    }
    return socket;
  }

}