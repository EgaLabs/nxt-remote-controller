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
 *  FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE   *
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER         *
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  *
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN      *
 *  THE SOFTWARE.                                                                  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                                  *
 *                                                                                                                                                                       *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                                   *
 *                                                                                                                                                                       *
 *  And the corresponding file at:                                                                                                                                       *
 *                                                                                                                                                                       *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/LocalControllerFragment.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.views.JoystickView;

/*
 *  Fragment used to control the robot locally with no server intervention.
 *  It's the first fragment shown on the ControllerActivity.
 */
public class LocalControllerFragment extends ControllerBaseFragment {

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    final LocalControllerFragment self = this;
    View view = inflater.inflate(R.layout.pad_layout, parent, false);
    final JoystickView stick = (JoystickView) view.findViewById(R.id.joystick);
    stick.setOnChangePositionListener(new JoystickView.OnChangePosition() {
      @Override
      public void onChange (float x, float y) {
        double powerLeft = 0.0d;
        double powerRight = 0.0d;
        double angle = stick.getAngle();
        double module = stick.getModule();
        double maxModule = stick.getMaximumModule();
        if (x >= 0 && y >= 0) {
          powerLeft  = module / maxModule * Math.signum(y);
          powerRight = Math.abs(Math.sin(angle)) * y;
        } else if (x < 0 && y >= 0) {
          powerLeft  = Math.sin(angle) * -y;
          powerRight = module / maxModule * Math.signum(y);
        } else if (x < 0 && y < 0) {
          powerLeft  = Math.sin(angle) * y;
          powerRight = module / maxModule * Math.signum(y);
        } else if (x >= 0 && y < 0) {
          powerLeft  = module / maxModule * Math.signum(y);
          powerRight = Math.sin(angle) * y;
        }
        self.getConnector().motorBC(powerLeft, powerRight, false, false);
      }
    });
    return view;
  }

}