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
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/HomeFragment.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andexert.library.RippleView;

import java.util.ArrayList;

import git.egatuts.nxtremotecontroller.utils.GlobalUtils;
import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.MainActivity;
import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.device.PairedDeviceAdapter;
import git.egatuts.nxtremotecontroller.device.PairedDeviceItemClickListener;

/*
 *  Default Activity fragment that shows all bonded devices.
 */
public class HomeFragment extends ActivityBaseFragment {

  private ArrayList<PairedDevice> pairedDevices;
  private PairedDeviceAdapter pairedDeviceAdapter;
  private RecyclerView recyclerView;
  private LinearLayoutManager linearLayoutManager;

  /*
   *  Starts the controller activity.
   */
  public void controlDevice (RippleView view, PairedDevice device) {
    ((MainActivity) this.getBaseActivity()).controlDevice(view, device);
  }

  /*
   *  Creates the view.
   */
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    final HomeFragment self = this;
    View view = inflater.inflate(R.layout.home_fragment, parent, false);

    /*
     *  Creates the adapter, the recycler view and the linear layout.
     */
    this.pairedDeviceAdapter = new PairedDeviceAdapter(this, this.getBluetoothUtils().getDevices());
    this.recyclerView = (RecyclerView) view.findViewById(R.id.paired_devices);

    this.linearLayoutManager = new LinearLayoutManager(this.getActivity());
    this.linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    this.linearLayoutManager.scrollToPosition(0);

    this.recyclerView.setAdapter(this.pairedDeviceAdapter);
    this.recyclerView.setLayoutManager(this.linearLayoutManager);
    this.recyclerView.setItemAnimator(new DefaultItemAnimator());

    /*
     *  When the item of the recycler view is clicked we do some checks.
     */
    this.recyclerView.addOnItemTouchListener(new PairedDeviceItemClickListener(this.getBaseActivity(), new PairedDeviceItemClickListener.OnItemClickListener() {
      @Override
      public void onItemClick (final View view, int position) {
        final PairedDevice device = self.pairedDeviceAdapter.get(position);
        GlobalUtils utils = self.getGlobalUtils();

        /*
         *  If the device is not a Toy we are no supposed to control it.
         */
        if (!device.isMajorToy()) {
          utils.createAlertDialog(
                  utils.getStringResource(R.string.device_type_toy_title),
                  utils.format(R.string.device_type_toy_message, device.getName()))
                  .show();
          return;
        }

        /*
         *  If the device is a Toy but not a robot maybe it will work
         *  or maybe is the robot but it's bad configured (so strange...).
         */
        if (!device.isToyRobot()) {
          AlertDialog.Builder dialog = utils.createAlertDialog(
                  utils.getStringResource(R.string.device_type_robot_title),
                  utils.format(R.string.device_type_robot_message, device.getName())
          );
          dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int which) {
              self.controlDevice((RippleView) view, device);
            }
          });
          dialog.setNegativeButton(android.R.string.no, null);
          dialog.show();
          return;
        }

        /*
         *  If everything is OK we start the controller activity calling it's activity method.
         *  We pass the view because we don't want the ripple to be ripped off.
         */
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run () {
            self.controlDevice((RippleView) view, device);
          }
        }, 0);
      }

      @Override
      public void onItemLongClick (View view, int position) {
        HomeFragment.this.getGlobalUtils().createAlertDialog("MAL", "VAMOS").show();
      }
    }));

    return view;
  }

}