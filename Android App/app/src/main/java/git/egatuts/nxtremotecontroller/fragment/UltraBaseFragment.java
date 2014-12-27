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
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE    *
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER         *
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  *
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN      *
 *  THE SOFTWARE.                                                                  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                            *
 *                                                                                                                                                                 *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                             *
 *                                                                                                                                                                 *
 *  And the corresponding file at:                                                                                                                                 *
 *                                                                                                                                                                 *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/UltraBaseFragment.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import git.egatuts.nxtremotecontroller.GlobalUtils;
import git.egatuts.nxtremotecontroller.activity.MainActivity;
import git.egatuts.nxtremotecontroller.activity.UltraBaseActivity;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import git.egatuts.nxtremotecontroller.preference.PreferencesUtils;

public abstract class UltraBaseFragment extends Fragment {

  protected UltraBaseFragment newFragment;

  /*
   *  Getters for UltraBaseActivity methods.
   */
  public UltraBaseActivity getBaseActivity () {
    return (UltraBaseActivity) this.getActivity();
  }
  public BluetoothUtils getBluetoothUtils () {
    return this.getBaseActivity().getBluetoothUtils();
  }

  public FragmentManager getSupportFragmentManager () {
    return this.getBaseActivity().getFragmentmanager();
  }

  public GlobalUtils getGlobalUtils () {
    return this.getBaseActivity().getGlobalUtils();
  }

  public PreferencesUtils getPreferencesUtils () {
    return this.getBaseActivity().getPreferencesUtils();
  }

  public PreferencesUtils.Editor getPreferencesEditor () {
    return this.getPreferencesUtils().getEditor();
  }

  /*
   *  Replaces the active fragment.
   */
  public void replaceFragmentWith (UltraBaseFragment fragment, FragmentPendingTransition transitionInterface) {
    ((MainActivity) this.getActivity()).replaceFragmentWith(fragment, transitionInterface);
  }

}
