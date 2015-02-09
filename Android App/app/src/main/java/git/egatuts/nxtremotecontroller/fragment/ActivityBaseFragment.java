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

import android.os.Handler;

import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.MainActivity;

/*
 *  Default abstract class applied to the fragments of the MainActivity class.
 *  Defines the default transaction animations.
 *  Prevents the fragment to be resumed if Bluetooth is not enabled.
 */
public abstract class ActivityBaseFragment extends BaseFragment implements FragmentPendingTransition {

  protected boolean isGoingToChange;

  /*
   *  Getter and setter for the lastFragment parent's activity property.
   */
  public void setLastFragment (ActivityBaseFragment fragment) {
    ((MainActivity) this.getBaseActivity()).setLastFragment(fragment);
  }

  public ActivityBaseFragment getLastFragment () {
    return ((MainActivity) this.getBaseActivity()).getLastFragment();
  }

  /*
   *  Getter and setter for the activeFragment parent's activity property.
   */
  public void setActiveFragment (ActivityBaseFragment fragment) {
    ((MainActivity) this.getBaseActivity()).setActiveFragment(fragment);
  }

  public ActivityBaseFragment getActiveFragment () {
    return ((MainActivity) this.getBaseActivity()).getActiveFragment();
  }

  /*
   *  Getter and setter for the isGoingToChange flag.
   */
  public void isGoingToChange (boolean flag) {
    this.isGoingToChange = flag;
  }

  public boolean isGoingToChange () {
    return this.isGoingToChange;
  }

  /*
   *  Overridden default method to replace fragment.
   */
  public void replaceFragmentWith (ActivityBaseFragment fragment, FragmentPendingTransition transitionInterface) {
    if (fragment instanceof BluetoothFragment) {
      this.setLastFragment(this);
    } else {
      this.setLastFragment(fragment);
    }
    ((MainActivity) this.getBaseActivity()).replaceFragmentWith(fragment, transitionInterface);
  }

  public void replaceFragmentWith (ActivityBaseFragment fragment) {
    this.replaceFragmentWith(fragment, this);
  }

  /*
   *  Transitions.
   */
  @Override
  public int[] onForward (BaseFragment fragment) {
    return new int[]{R.anim.transaction_in, R.anim.transaction_out};
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
    super.onResume();
    final ActivityBaseFragment self = this;
    boolean isBluetoothFragment = this instanceof BluetoothFragment;
    boolean isBluetoothEnabled = this.getBluetoothUtils().isEnabled();
    ActivityBaseFragment fragment = null;
    ActivityBaseFragment last = null;

    if (!isBluetoothEnabled && !isBluetoothFragment) {
      last = this;
      fragment = new BluetoothFragment();
    } else if (isBluetoothEnabled && isBluetoothFragment) {
      fragment = this.getLastFragment();
    }
    if (fragment != null) {
      this.isGoingToChange(true);
      final ActivityBaseFragment tmpFragment = fragment;
      final ActivityBaseFragment tmpLast = last;
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run () {
          if (tmpLast != null) {
            self.replaceFragmentWith(tmpFragment, self.getLastFragment());
          } else {
            self.replaceFragmentWith(tmpFragment);
          }
        }
      }, 100);
    }
  }

}
