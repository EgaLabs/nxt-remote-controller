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
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/BluetoothFragment.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import git.egatuts.nxtremotecontroller.R;

/*
 *  BluetoothFragment that shows an error when the bluetooth is not enabled.
 *  Inaccessible by the user unless he/she disables the bluetooth.
 *  Automatically replaced when the user starts again the bluetooth.
 */
public class BluetoothFragment extends ActivityBaseFragment {

  /*
   *  If the application was paused and the user enabled the bluetooth
   *  we wait a little bit and do the fragment transaction animation to not make it smoother.
   */
  @Override
  public void onResume () {
    super.onResume();
    final BluetoothFragment self = this;
    if (this.getBluetoothUtils().isEnabled()) {
      new Handler().postDelayed(new Runnable () {
        @Override
        public void run () {
          ActivityBaseFragment lastFragment = (ActivityBaseFragment) self.getLastFragment();
          self.replaceFragmentWith(lastFragment, lastFragment);
        }
      }, 100);
    }
  }

  /*
   *  Inflates the view and sets the click listener to the button.
   */
  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.bluetooth_enable, parent, false);
    Button button = (Button) view.findViewById(R.id.enable_bluetooth);
    button.setOnClickListener(new View.OnClickListener () {
      @Override
      public void onClick (View arg0) {
        BluetoothFragment.this.getBluetoothUtils().enable();
      }
    });
    return view;
  }

}
