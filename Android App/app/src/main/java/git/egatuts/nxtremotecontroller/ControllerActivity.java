package git.egatuts.nxtremotecontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.View;

import git.egatuts.nxtremotecontroller.activity.BaseActivity;
import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.fragment.OnlineControllerFragment;
import git.egatuts.nxtremotecontroller.fragment.PadControllerFragment;

public class ControllerActivity extends BaseActivity {

  private Intent intent;
  private PairedDevice device;
  private FragmentTabHost tab_host;

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.controller_layout);
    intent = getIntent();
    device = intent.getParcelableExtra("device");
    this.setTitle(getResources().getString(R.string.controller_window_title) + device.getName());
    toolbar = (Toolbar) super.findViewById(R.id.toolbar_element);
    this.setSupportToolbar();
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v) {
        ControllerActivity.this.onBackPressed();
      }
    });
    tab_host = (FragmentTabHost) this.findViewById(R.id.tabhost);
    tab_host.setup(this, super.getSupportFragmentManager(), R.id.tabcontent);
    tab_host.addTab(tab_host.newTabSpec("local").setIndicator("Local"), PadControllerFragment.class, null);
    tab_host.addTab(tab_host.newTabSpec("online").setIndicator("Online"), OnlineControllerFragment.class, null);
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

  @Override
  public void finish () {
    super.finish();
    super.overridePendingTransition(R.anim.controller_transition_back_in, R.anim.controller_transition_back_out);
  }

}
