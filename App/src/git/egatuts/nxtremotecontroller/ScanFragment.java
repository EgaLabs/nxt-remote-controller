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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothCallback;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothReceiver;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.device.PairedDeviceAdapter;

public class ScanFragment extends Fragment
{
  
  private BluetoothUtils bluetooth_utils;
  private BluetoothReceiver bluetooth_receiver;
  private View view;
  private PairedDeviceAdapter paired_devices_adapter;
  private LinearLayoutManager linear_layout_manager;
  private RecyclerView recycler_view;
  
  public ScanFragment () {}
  
  @Override
  public void onDetach ()
  {
    super.onDetach();
    getActivity().unregisterReceiver(bluetooth_receiver);
    if (bluetooth_utils.isDiscovering()) bluetooth_utils.cancelDiscovery();
  }
  
  @Override
  public void onAttach (Activity activity)
  {
    super.onAttach(activity);
    bluetooth_utils = new BluetoothUtils();
    
    if (bluetooth_utils.isEnabled() == false) {
      getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new BluetoothFragment(this, bluetooth_utils)).commit();
    }
    
    if (bluetooth_utils.isDiscovering()) {
      bluetooth_utils.cancelDiscovery();
    }
    bluetooth_utils.startDiscovery();
    bluetooth_receiver = new BluetoothReceiver();
    bluetooth_receiver.setOnDiscoveryListener(new BluetoothCallback.OnDiscoveryListener () {
      @Override
      public void onDiscover(PairedDevice discovered_device, BluetoothDevice bluetooth_device, Intent intent) {
        int raw_signal = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
        paired_devices_adapter.add(discovered_device);
        //Toast.makeText(getActivity(), "Strength: " + , Toast.LENGTH_SHORT).show();
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