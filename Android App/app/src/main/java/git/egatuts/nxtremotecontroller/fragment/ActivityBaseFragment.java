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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                                *
 *                                                                                                                                                                     *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                                 *
 *                                                                                                                                                                     *
 *  And the corresponding file at:                                                                                                                                     *
 *                                                                                                                                                                     *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/ActivityBaseFragment.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.fragment;

import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.MainActivity;
import git.egatuts.nxtremotecontroller.views.BaseIndeterminateProgressDialog;

/*
 *  Default abstract class applied to the fragments of the MainActivity class.
 *  Defines the default transaction animations.
 *  Prevents the fragment to be resumed if Bluetooth is not enabled.
 */
public abstract class ActivityBaseFragment extends BaseFragment implements FragmentPendingTransition {

  /*
   *  Getter and setter for the lastFragment parent's activity property.
   */
  public void setLastFragment (ActivityBaseFragment fragment) {
    ((MainActivity) this.getActivity()).setLastFragment(fragment);
  }

  public BaseFragment getLastFragment () {
    return ((MainActivity) this.getActivity()).getLastFragment();
  }

  /*
   *  Getter and setter for parent's progress dialog.
   */
  public BaseIndeterminateProgressDialog getShortProgressDialog () {
    return ((MainActivity) this.getActivity()).getShortProgressDialog();
  }

  public BaseIndeterminateProgressDialog getLongProgressDialog () {
    return ((MainActivity) this.getActivity()).getLongProgressDialog();
  }

  /*
   *  Transitions.
   */
  @Override
  public int[] onForward (BaseFragment fragment) {
    return new int[] { R.anim.transaction_in, R.anim.transaction_out };
  }

  @Override
  public int[] onBackward () {
    return new int[]{};
  }

  /*
   *  When the fragment is attached.
   */
  @Override
  public void onResume () {
    if (!this.getBluetoothUtils().isEnabled() && !(this instanceof BluetoothFragment)) {
      FragmentPendingTransition transaction = this.getLastFragment() != null ? this : null;
      this.setLastFragment(this);
      this.replaceFragmentWith(new BluetoothFragment(), null);
    }
    super.onResume();
  }

  /*
   *  Overridden default method to replace fragment.
   */
  @Override
  public void replaceFragmentWith (BaseFragment fragment, FragmentPendingTransition transitionInterface) {
    if (!(fragment instanceof BluetoothFragment)) {
      this.setLastFragment((ActivityBaseFragment) fragment);
    }
    super.replaceFragmentWith(fragment, transitionInterface);
  }

}
