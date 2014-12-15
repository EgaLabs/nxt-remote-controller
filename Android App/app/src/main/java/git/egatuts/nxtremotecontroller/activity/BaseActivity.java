package git.egatuts.nxtremotecontroller.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Toast;

import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import git.egatuts.nxtremotecontroller.preference.PreferencesUtils;

public class BaseActivity extends ActionBarActivity {

  protected Toolbar toolbar;
  protected BluetoothUtils bluetooth_utils;
  protected FragmentManager fragment_manager;
  protected PreferencesUtils preference_utils;
  protected PreferencesUtils.Editor preference_editor;

  /*
   * Inits all utils, enables notification bar coloring (Android Lollipop) and sets the active theme based on the preference value.
   */
  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    bluetooth_utils = new BluetoothUtils();
    fragment_manager = super.getSupportFragmentManager();
    preference_utils = new PreferencesUtils(this);
    preference_utils.privateMode();
    preference_editor = preference_utils.getEditor();
    preference_editor.edit();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    this.setActiveTheme(this.getActiveTheme());
  }

  /*
   * Sets, shows and enables "up" the home button.
   */
  public void setSupportToolbar () {
    super.setSupportActionBar(toolbar);
    super.getSupportActionBar().setHomeButtonEnabled(true);
    super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    super.getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  /*
   * Returns the active theme name (e.g. (String) AppTheme.Indigo ).
   */
  public String getActiveTheme () {
    return preference_editor.getString("preference_theme", getResources().getString(R.string.preference_value_theme));
  }

  /*
   * Sets the active theme using the given name.
   */
  public boolean setActiveTheme (String theme) {
    try {
      super.setTheme(getResources().getIdentifier(theme, "style", getPackageName()));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
