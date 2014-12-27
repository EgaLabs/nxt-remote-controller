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
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/activity/SettingsActivity.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import git.egatuts.nxtremotecontroller.R;

/*
 *  Main settings activity which only purpose is to inflate the view with the PreferenceFragment.
 */
public class SettingsActivity extends UltraBaseActivity implements ActivityPendingTransition {

  /*
   *  Constant used as Intent extra data to know when to send a
   *  broadcast intent to the AppKillerReceiver to kill the MainActivity.
   */
  public static final String EXTRA_KILL_APP = "restart";

  /**
   * @see ActivityPendingTransition#onForward(android.content.Intent)
   */
  @Override
  public int[] onForward (Intent intent) {
    return new int[] {};
  }

  /**
   * @see ActivityPendingTransition#onBackward()
   */
  @Override
  public int[] onBackward () {
    return new int[] { R.anim.settings_transition_back_in, R.anim.settings_transition_back_out };
  }

  /*
   *  Goes to the previous activity finishing the actual one.
   */
  private void goToPreviousActivity () {
    super.startActivity(this, MainActivity.class);
    super.finish(this);
  }

  /*
   *  Sets the theme to the one selected on the preferences.
   *  Sets the View#OnclickListener() to the navigation toolbar button to simulate back button press.
   */
  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setActiveTheme(super.getPreferenceTheme());
    super.setContentView(R.layout.preference_layout);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    super.setSupportToolbar();

    /*
     *  When the toolbar home button we go to the previous activity killing
     *  the actual one to avoid going back through the history stack and
     *  avoid some themes bugs.
     */
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v) {
        SettingsActivity.this.goToPreviousActivity();
      }
    });
  }

  /*
   *  When the back button is pressed, it does the same as pressing the home/up toolbar button.
   */
  @Override
  public void onBackPressed () {
    this.goToPreviousActivity();
    super.onBackPressed();
  }
}
