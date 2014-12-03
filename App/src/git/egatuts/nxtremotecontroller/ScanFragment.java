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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                       *
 *                                                                                                                           *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                        *
 *                                                                                                                           *
 * And the corresponding file at:                                                                                            *
 *                                                                                                                           *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/ScanFragment.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothCallback;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothReceiver;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.device.PairedDeviceAdapter;
import git.egatuts.nxtremotecontroller.preference.PreferencesUtils;

public class ScanFragment extends Fragment
{
  
  private BluetoothUtils bluetooth_utils;
  private BluetoothReceiver bluetooth_receiver;
  private PreferencesUtils preference_utils;
  private PreferencesUtils.Editor preference_editor;
  private View view;
  private PairedDeviceAdapter paired_devices_adapter;
  private PairedDevice discovered_device;
  private LinearLayoutManager linear_layout_manager;
  private RecyclerView recycler_view;
  private boolean first_time;
  private ArrayList<PairedDevice> discovered_devices;
  private ArrayList<PairedDevice> lost_devices;
  final private String MAXIMUM_SIGNAL = "preference_maximum_signal";
  final private String MINIMUM_SIGNAL = "preference_minimum_signal";
  
  public ScanFragment () {}
  
  @Override
  public void onDetach ()
  {
    if (bluetooth_utils.isDiscovering()) bluetooth_utils.cancelDiscovery();
    getActivity().unregisterReceiver(bluetooth_receiver);
    first_time = false;
    super.onDetach();
  }
  
  @Override
  public void onAttach (Activity activity)
  {
    super.onAttach(activity);
    bluetooth_utils = new BluetoothUtils();
    preference_utils = new PreferencesUtils(activity);
    preference_utils.privateMode();
    preference_editor = preference_utils.getEditor();
    preference_editor.edit();
    discovered_devices = new ArrayList<PairedDevice>();
    lost_devices = new ArrayList<PairedDevice>();
    first_time = true;
    
    if (bluetooth_utils.isEnabled() == false) {
      getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new BluetoothFragment(this, bluetooth_utils)).commit();
    }
    
    if (bluetooth_utils.isDiscovering()) bluetooth_utils.cancelDiscovery();
    bluetooth_utils.startDiscovery();
    bluetooth_receiver = new BluetoothReceiver();
    bluetooth_receiver.setOnDiscoveryListener(new BluetoothCallback.OnDiscoveryListener () {
      
      @Override
      public void onFinish ()
      {
        if (first_time == true) first_time = false;
        lost_devices = paired_devices_adapter.diff(discovered_devices);
        int index;
        for (PairedDevice discovered_device : lost_devices) {
          index = paired_devices_adapter.exists(discovered_device);
          if (index != -1) {
            paired_devices_adapter.remove(index);
          }
        }
        for (PairedDevice discovered_device : discovered_devices) {
          index = paired_devices_adapter.exists(discovered_device);
          if (index != -1) {
            paired_devices_adapter.update(index);
          } else {
            paired_devices_adapter.add(discovered_device);
          }
        }
        discovered_devices.clear();
        lost_devices.clear();
        bluetooth_utils.startDiscovery();
      }
      
      @Override
      public void onDiscover(BluetoothDevice bluetooth_device, Intent intent) {
        int raw_signal = (int) intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
        int minimum_value = Integer.parseInt(preference_editor.getString(MINIMUM_SIGNAL, "0"));
        int maximum_value = Integer.parseInt(preference_editor.getString(MAXIMUM_SIGNAL, "-100"));
        if (raw_signal > maximum_value) {
          maximum_value = raw_signal;
          preference_editor.saveString(MAXIMUM_SIGNAL, Integer.toString(maximum_value));
        }
        if (raw_signal < minimum_value) {
          minimum_value = raw_signal;
          preference_editor.saveString(MINIMUM_SIGNAL, Integer.toString(minimum_value));
        }
        int total = maximum_value - minimum_value;
        int portion = raw_signal - minimum_value;
        float percentage = ((float) portion / (float) total) * 100;
        int position = paired_devices_adapter.exists(bluetooth_device);
        if (first_time == false) {
          if (position != -1) {
            discovered_device = paired_devices_adapter.get(position);
          } else {
            discovered_device = PairedDevice.from(bluetooth_device);
          }
          discovered_device.setConnectivity((int) percentage);
          discovered_devices.add(discovered_device);
          return;
        }
        if (position != -1) {
          discovered_device = paired_devices_adapter.get(position);
          discovered_device.setConnectivity((int) percentage);
          paired_devices_adapter.update(position);
        } else {
          discovered_device = PairedDevice.from(bluetooth_device);
          discovered_device.setConnectivity((int) percentage);
          paired_devices_adapter.add(discovered_device);
        }
      }
      
    });
    getActivity().registerReceiver(bluetooth_receiver, BluetoothReceiver.getIntentFilter());
  }
  
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent_container, Bundle savedInstanceState)
  {
    view = inflater.inflate(R.layout.home_fragment, parent_container, false);
    paired_devices_adapter = new PairedDeviceAdapter(new ArrayList<PairedDevice>(0));
    recycler_view = (RecyclerView) view.findViewById(R.id.paired_devices);
    
    linear_layout_manager = new LinearLayoutManager(parent_container.getContext());
    linear_layout_manager.setOrientation(LinearLayoutManager.VERTICAL);
    linear_layout_manager.scrollToPosition(0);
    
    recycler_view.setAdapter(paired_devices_adapter);
    recycler_view.setLayoutManager(linear_layout_manager);
    recycler_view.setItemAnimator(new DefaultItemAnimator());
    return view;
  }

}