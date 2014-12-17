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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                                       *
 *                                                                                                                                                           *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                                        *
 *                                                                                                                                                           *
 * And the corresponding file at:                                                                                                                            *
 *                                                                                                                                                           *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/HomeFragment.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;

import git.egatuts.nxtremotecontroller.ControllerActivity;
import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.device.PairedDeviceAdapter;
import git.egatuts.nxtremotecontroller.device.PairedDeviceItemClickListener;

public class HomeFragment extends BaseFragment {

  private View view;
  private RecyclerView recycler_view;
  private LinearLayoutManager linear_layout_manager;
  private PairedDeviceAdapter paired_devices_adapter;
  private boolean connecting = false;
  private PairedDevice connecting_device;
  private RippleView connecting_device_view;
  private RippleView.AnimationFinishListener listener;

  public HomeFragment () {
    listener = new RippleView.AnimationFinishListener() {
      @Override
      public void onFinish () {
        HomeFragment.this.startControllerActivity(connecting_device);
        connecting_device_view.setAnimationFinishListener(null);
      }
    };
  }

  private String getStr (int resId) {
    return getResources().getString(resId);
  }

  /*
   * We create the recycler view and it's adapter and inflate it with the bonded devices.
   */
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent_container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.home_fragment, parent_container, false);
    paired_devices_adapter = new PairedDeviceAdapter(bluetooth_utils.getDevices());
    paired_devices_adapter.setContext(getActivity());
    recycler_view = (RecyclerView) view.findViewById(R.id.paired_devices);

    linear_layout_manager = new LinearLayoutManager(parent_container.getContext());
    linear_layout_manager.setOrientation(LinearLayoutManager.VERTICAL);
    linear_layout_manager.scrollToPosition(0);

    recycler_view.setAdapter(paired_devices_adapter);
    recycler_view.setLayoutManager(linear_layout_manager);
    recycler_view.setItemAnimator(new DefaultItemAnimator());
    recycler_view.addOnItemTouchListener(new PairedDeviceItemClickListener(getActivity(), new PairedDeviceItemClickListener.OnItemClickListener() {
      @Override
      public void onItemClick (View view, int position) {
        connecting_device = paired_devices_adapter.get(position);
        connecting_device_view = (RippleView) view;
        if (!connecting_device.isMajorToy()) {
          new AlertDialog.Builder(getActivity())
                  .setTitle(R.string.device_type_toy_title)
                  .setMessage(String.format(getStr(R.string.device_type_toy_message), connecting_device.getName()))
                  .setPositiveButton(android.R.string.yes, null)
                  .show();
          return;
        }
        if (!connecting_device.isToyRobot()) {
          new AlertDialog.Builder(getActivity())
                  .setTitle(R.string.device_type_robot_title)
                  .setMessage(String.format(getStr(R.string.device_type_robot_message), connecting_device.getName()))
                  .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int id) {
                      if (!connecting_device_view.isRunning()) {
                        HomeFragment.this.startControllerActivity(connecting_device);
                      } else {
                        connecting_device_view.setAnimationFinishListener(listener);
                      }
                    }
                  })
                  .setNegativeButton(android.R.string.no, null)
                  .show();
          return;
        }
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run () {
            if (!connecting_device_view.isRunning()) {
              HomeFragment.this.startControllerActivity(connecting_device);
            } else {
              connecting_device_view.setAnimationFinishListener(listener);
            }
          }
        }, 0);
      }
    }));
    return view;
  }

  public void startControllerActivity (PairedDevice device) {
    Intent intent = new Intent(getActivity(), ControllerActivity.class);
    intent.putExtra("device", device);
    getActivity().startActivity(intent);
    getActivity().overridePendingTransition(R.anim.controller_transition_in, R.anim.controller_transition_out);
  }

}