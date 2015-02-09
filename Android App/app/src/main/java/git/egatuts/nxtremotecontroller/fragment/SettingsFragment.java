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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                            *
 *                                                                                                                                                                 *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                             *
 *                                                                                                                                                                 *
 *  And the corresponding file at:                                                                                                                                 *
 *                                                                                                                                                                 *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/SettingsFragment.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.preference.PreferenceFragment;

import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.BaseActivity;
import git.egatuts.nxtremotecontroller.activity.MainActivity;
import git.egatuts.nxtremotecontroller.activity.SettingsActivity;

/*
 *  Fragment used in the SettingsActivity due to deprecated version of PreferenceActivity.
 *  Using library PreferenceFragment to support older devices.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

  /*
   *  When the preferences has changed and the preference key
   *  equals the preference theme we kill the previous activity (MainActivity)
   *  in order to not let it to use the cached theme.
   */
  @Override
  public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key) {
    BaseActivity context = (BaseActivity) this.getActivity();
    if (key.equals(BaseActivity.PREFERENCE_THEME_NAME)) {
      Intent intent = new Intent(context, context.getClass());
      intent.putExtra(SettingsActivity.EXTRA_KILL_APP, true);
      context.startActivity(intent);
      context.finish();
    }
  }

  /*
   *  When the activity is created if the intent has the extra SettingsActivity.EXTRA_KILL_APP
   *  set as true we send a new Intent to the main activity in order to kill it.
   */
  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    BaseActivity activity = (BaseActivity) this.getActivity();
    super.addPreferencesFromResource(R.layout.preference_fragment);
    if (activity.getIntent().getBooleanExtra(SettingsActivity.EXTRA_KILL_APP, false)) {
      Intent intent = new Intent();
      intent.setAction(MainActivity.ACTION_RESTART_APP);
      activity.sendBroadcast(intent);
    }
  }

  /*
   *  When the app is resumed we listen for preferences changes.
   */
  @Override
  public void onResume () {
    super.onResume();
    this.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  /*
   *  When the app is paused we stop listening for preference changes.
   */
  @Override
  public void onPause () {
    super.onPause();
    this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
  }

}
