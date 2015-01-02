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
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/activity/BaseActivity.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import git.egatuts.nxtremotecontroller.GlobalUtils;
import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import git.egatuts.nxtremotecontroller.preference.PreferencesUtils;
import git.egatuts.nxtremotecontroller.views.BaseIndeterminateProgressDialog;
import git.egatuts.nxtremotecontroller.views.LongIndeterminateProgressDialog;
import git.egatuts.nxtremotecontroller.views.ShortIndeterminateProgressDialog;

/*
 *  Most basic class to extend Activity (ActionBarActivity) classes.
 *  It can override the default theme to the one selected in the SharedPreferences.
 *  It can enable the Toolbar home button as "up".
 *  It can instantiates most used classes:
 *    BluetoothUtils
 *    FragmentManager
 *    GlobalUtils
 *    PreferencesUtils
 *      PreferencesUtils#Editor
 *  And creates the variable Toolbar (not instantiated).
 */
public abstract class BaseActivity extends ActionBarActivity {

  /*
   * Constants used, normally resources IDs and key names.
   */
  public static final String PREFERENCE_THEME_NAME = "preference_theme";
  public static final int PREFERENCE_THEME_VALUE = R.string.preference_value_theme;

  protected BluetoothUtils bluetoothUtils;
  protected FragmentManager fragmentManager;
  protected GlobalUtils globalUtils;
  protected PreferencesUtils preferencesUtils;
  protected PreferencesUtils.Editor preferencesEditor;
  protected Toolbar toolbar;
  protected BaseIndeterminateProgressDialog progressDialog;

  /*
   *  Some getters for the properties.
   */
  public BluetoothUtils getBluetoothUtils () {
    return this.bluetoothUtils;
  }

  public FragmentManager getBaseFragmentManager () {
    return this.fragmentManager;
  }

  public GlobalUtils getGlobalUtils () {
    return this.globalUtils;
  }

  public PreferencesUtils getPreferencesUtils () {
    return this.preferencesUtils;
  }

  public PreferencesUtils.Editor getPreferencesEditor () {
    return this.preferencesEditor;
  }

  public BaseIndeterminateProgressDialog getProgressDialog () {
    return this.progressDialog;
  }

  /*
   *  Sets, shows and enables "up" the home button on the Toolbar (ActionBar/AppBar).
   */
  protected void setSupportToolbar () {
    super.setSupportActionBar(toolbar);
    super.getSupportActionBar().setHomeButtonEnabled(true);
    super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    super.getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  /*
   *  Getter and setter for the active Theme.
   */
  protected Theme getActiveTheme () {
    return globalUtils.getActiveTheme();
  }

  protected void setActiveTheme (String name) {
    globalUtils.setActiveTheme(name);
  }

  /*
   *  Getter for the Theme saved in the default SharedPreferences.
   */
  protected String getPreferenceTheme () {
    String defaultValue = globalUtils.getStringResource(BaseActivity.PREFERENCE_THEME_VALUE);
    return preferencesEditor.getString(BaseActivity.PREFERENCE_THEME_NAME, defaultValue);
  }

  /*
   *  Instantiates the objects defined above.
   */
  @Override
  public void onCreate (Bundle savedInstanceState) {

    /*
     *  Super method. MUST BE CALLED.
     */
    super.onCreate(savedInstanceState);

    /*
     *  Variables instantiation.
     */
    this.bluetoothUtils = new BluetoothUtils();
    this.fragmentManager = super.getSupportFragmentManager();
    this.globalUtils = new GlobalUtils(this);
    this.preferencesUtils = new PreferencesUtils(this);
    this.preferencesUtils.privateMode();
    this.preferencesEditor = preferencesUtils.getEditor();
    this.preferencesEditor.edit();

    /*
     *  If version is equal or greater than Android Lollipop (API 21) enable FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
     *  to change the navigation and notification bar colors.
     */
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    }
  }

  /*
   *  Overridden method Activity#startActivity() and variants to use ActivityPendingTransition interface.
   */
  private void startNewActivity (Intent intent, ActivityPendingTransition transitionInterface) {
    ActivityPendingTransition transition = transitionInterface != null ? transitionInterface : new DefaultActivityPendingTransition();
    int[] transitions = transition.onForward(intent);
    super.startActivity(intent);
    if (transitions.length == 2)
      super.overridePendingTransition(transitions[0], transitions[1]);
  }

  public void startActivity (Context context, Class<?> defClass, ActivityPendingTransition transitionInterface) {
    this.startNewActivity(new Intent(context, defClass), transitionInterface);
  }

  public void startActivity (Context context, Class<?> defClass) {
    this.startActivity(context, defClass, null);
  }

  public void startActivity (Intent intent, ActivityPendingTransition interfaceTransition) {
    this.startNewActivity(intent, interfaceTransition);
  }

  /*
   *  Returns a short progress dialog (doFirstAnimation = false and setCancelable = false)
   */
  public BaseIndeterminateProgressDialog getShortProgressDialog () {
    if ( !(this.progressDialog instanceof ShortIndeterminateProgressDialog) ) {
      this.progressDialog = new ShortIndeterminateProgressDialog(this);
    }
    return this.progressDialog;
  }

  /*
   *  Returns a long progress dialog (doFirstAnimation = true and setCancelable = true)
   */
  public BaseIndeterminateProgressDialog getLongProgressDialog () {
    if ( !(this.progressDialog instanceof LongIndeterminateProgressDialog) ) {
      this.progressDialog = new LongIndeterminateProgressDialog(this);
    }
    return this.progressDialog;
  }

  @Override
  public void startActivity (Intent intent) {
    this.startNewActivity(intent, null);
  }

  /*
   *  Overridden method Activity#finish() and variants to use ActivityPendingTransition interface.
   */
  private void finishActivity (ActivityPendingTransition transitionInterface) {
    ActivityPendingTransition transition = transitionInterface != null ? transitionInterface : new DefaultActivityPendingTransition();
    int[] transitions = transition.onBackward();
    super.finish();
    if (transitions.length == 2)
      super.overridePendingTransition(transitions[0], transitions[1]);
  }

  public void finish (ActivityPendingTransition transitionInterface) {
    this.finishActivity(transitionInterface);
  }

  @Override
  public void finish () {
    this.finishActivity(null);
  }

}
