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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                           *
 *                                                                                                                               *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                            *
 *                                                                                                                               *
 * And the corresponding file at:                                                                                                *
 *                                                                                                                               *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/App/src/git/egatuts/nxtremotecontroller/BluetoothFragment.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller;

import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import git.egatuts.nxtremotecontroller.bluetooth.receiver.BluetoothEnableReceiver;
import git.egatuts.nxtremotecontroller.bluetooth.listener.BluetoothEnableListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class BluetoothFragment extends Fragment {

  private BluetoothUtils _bluetooth_utils;
  private BluetoothEnableReceiver bluetooth_enable_receiver;
  private Fragment _last_fragment;
  private IndeterminateProgressDialog progress_dialog;

  /*
   * Constructor.
   */
  public BluetoothFragment (Fragment last_fragment, BluetoothUtils bluetooth_utils) {
    this._last_fragment = last_fragment;
    this._bluetooth_utils = bluetooth_utils;
  }

  /*
   * We register the receiver to detect bluetooth changes.
   */
  @Override
  public void onAttach (final Activity activity) {
    super.onAttach(activity);
    bluetooth_enable_receiver = new BluetoothEnableReceiver(getActivity());
    bluetooth_enable_receiver.setListener(new BluetoothEnableListener () {
      @Override
      public void onStateChange (Context context, Intent intent) {
        final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
        if (state == BluetoothAdapter.STATE_ON) {
          progress_dialog.dismiss();
          FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
          transaction.setCustomAnimations(R.anim.transaction_in, R.anim.transaction_out);
          transaction.replace(R.id.main_container, _last_fragment);
          transaction.commit();
        }
      }
    });
    bluetooth_enable_receiver.registerReceiver();
  }
  
  @Override
  public void onDetach () {
    super.onDetach();
    bluetooth_enable_receiver.unregisterReceiver();
  }

  public View onCreateView (LayoutInflater inflater, final ViewGroup parent, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.bluetooth_enable, parent, false);
    Button button = (Button) view.findViewById(R.id.enable_bluetooth);
    button.setOnClickListener(new View.OnClickListener () {
      @Override
      public void onClick (View v) {
         _bluetooth_utils.enable();
         progress_dialog = new IndeterminateProgressDialog(parent.getContext());
         progress_dialog.show();
         progress_dialog.setText(R.string.bluetooth_enabling);
      }
    });
    return view;
  }

}