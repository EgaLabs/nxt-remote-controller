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
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/BaseFragment.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.fragment;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import git.egatuts.nxtremotecontroller.utils.GlobalUtils;
import git.egatuts.nxtremotecontroller.activity.BaseActivity;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import git.egatuts.nxtremotecontroller.preference.PreferencesUtils;
import git.egatuts.nxtremotecontroller.views.BaseIndeterminateProgressDialog;

/*
 *  Base class that is recommended to be used in all fragments.
 */
public abstract class BaseFragment extends Fragment {

  protected BaseFragment newFragment;

  /*
   *  Returns the base activity (no need to cast).
   */
  public BaseActivity getBaseActivity () {
    return (BaseActivity) this.getActivity();
  }

  /*
   *  Returns the BluetoothUtils instance.
   */
  public BluetoothUtils getBluetoothUtils () {
    return this.getBaseActivity().getBluetoothUtils();
  }

  /*
   *  Returns the fragment manager.
   */
  public FragmentManager getBaseFragmentManager () {
    return this.getBaseActivity().getBaseFragmentManager();
  }

  /*
   *  Returns the global utils intance.
   */
  public GlobalUtils getGlobalUtils () {
    return this.getBaseActivity().getGlobalUtils();
  }

  /*
   *  Returns the preferences utils.
   */
  public PreferencesUtils getPreferencesUtils () {
    return this.getBaseActivity().getPreferencesUtils();
  }

  /*
   *  Returns the preferences editor.
   */
  public PreferencesUtils.Editor getPreferencesEditor () {
    return this.getPreferencesUtils().getEditor();
  }

  /*
   *  Returns the short progress dialog (uncancellable BaseProgressDialog).
   */
  public BaseIndeterminateProgressDialog getShortProgressDialog () {
    return this.getBaseActivity().getShortProgressDialog();
  }

  /*
   *  Returns the long progress dialog (cancellable BaseProgressDialog)
   */
  public BaseIndeterminateProgressDialog getLongProgressDialog () {
    return this.getBaseActivity().getLongProgressDialog();
  }

  /*
   *  Returns the wifi service manager.
   */
  public WifiManager getWifiManager () {
    return (WifiManager) this.getActivity().getSystemService(Context.WIFI_SERVICE);
  }

}
