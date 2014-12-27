/*package git.egatuts.nxtremotecontroller;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import git.egatuts.nxtremotecontroller.application.BaseApplication;
import git.egatuts.nxtremotecontroller.bluetooth.NXTConnector;
import git.egatuts.nxtremotecontroller.device.PairedDevice;
import git.egatuts.nxtremotecontroller.fragment.OldOnlineControllerFragment;
import git.egatuts.nxtremotecontroller.fragment.OldPadControllerFragment;
import git.egatuts.nxtremotecontroller.views.IndeterminateProgressDialog;

public class ControllerActivity extends BaseActivity {

  Intent intent;
  PairedDevice device;
  FragmentTabHost tab_host;
  StateListDrawable background_list_drawable;
  private int background_color;
  private LinearGradient linear_gradient;
  int light_background_color;
  private PaintDrawable tab_background;
  private PaintDrawable tab_background_all;
  ColorStateList text_color;
  NXTConnector nxt_connector;
  private IndeterminateProgressDialog progress_dialog;
  private Handler connection_handler;

  private void prepareConnection () {
    BaseApplication app = (BaseApplication) this.getApplication();
    this.nxt_connector = app.getNXTConnector();
    this.device = app.getDevice();
  }

  public NXTConnector getConnector () {
    return this.nxt_connector;
  }

  public PairedDevice getDevice () {
    return this.device;
  }

  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.controller_layout);

    this.prepareConnection();

    this.setTitle(getResources().getString(R.string.controller_window_title) + device.getName());
    toolbar = (Toolbar) super.findViewById(R.id.toolbar_element);
    this.setSupportToolbar();
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick (View v) {
        ControllerActivity.this.startActivity(new Intent(ControllerActivity.this, MainActivity.class));
        ControllerActivity.this.onBackPressed();
      }
    });

    final int underline_color = (int) this.getThemeProperty(R.attr.toolbar_color);
    final int background_color = (int) this.getThemeProperty(R.attr.toolbar_background);
    final int light_background_color = 0x55FFFFFF;

    ShapeDrawable.ShaderFactory shader_factory_selected = new ShapeDrawable.ShaderFactory() {
      @Override
      public Shader resize (int width, int height) {
        linear_gradient = new LinearGradient(
                0, 0,
                0, height,
                new int[]{background_color, background_color, underline_color, underline_color},
                new float[]{0, 10f / 11f, 10f / 11f, 1},
                Shader.TileMode.REPEAT);
        return linear_gradient;
      }
    };
    tab_background = new PaintDrawable();
    tab_background.setShape(new RectShape());
    tab_background.setShaderFactory(shader_factory_selected);
    ShapeDrawable.ShaderFactory shader_factory_all = new ShapeDrawable.ShaderFactory() {
      @Override
      public Shader resize (int width, int height) {
        linear_gradient = new LinearGradient(
                0, 0,
                0, height,
                new int[]{mixColors(background_color, light_background_color, 0.5f), mixColors(background_color, light_background_color, 0.5f), underline_color, underline_color},
                new float[]{0, 10f / 11f, 10f / 11f, 1},
                Shader.TileMode.REPEAT);
        return linear_gradient;
      }
    };
    tab_background_all = new PaintDrawable();
    tab_background_all.setShape(new RectShape());
    tab_background_all.setShaderFactory(shader_factory_all);

    tab_host = (FragmentTabHost) this.findViewById(R.id.tabhost);
    tab_host.setup(this, super.getSupportFragmentManager(), R.id.tabcontent);
    tab_host.getTabWidget().setDividerDrawable(null);
    this.addTab(tab_host, "local", "Local", OldPadControllerFragment.class);
    this.addTab(tab_host, "online", "Online", OldOnlineControllerFragment.class);
    tab_host.setCurrentTab(0);
  }

  public static int mixColors (int color1, int color2, float amount) {
    final byte ALPHA_CHANNEL = 24;
    final byte RED_CHANNEL = 16;
    final byte GREEN_CHANNEL = 8;
    final byte BLUE_CHANNEL = 0;

    final float inverseAmount = 1.0f - amount;

    int a = ((int) (((float) (color1 >> ALPHA_CHANNEL & 0xff) * amount) +
            ((float) (color2 >> ALPHA_CHANNEL & 0xff) * inverseAmount))) & 0xff;
    int r = ((int) (((float) (color1 >> RED_CHANNEL & 0xff) * amount) +
            ((float) (color2 >> RED_CHANNEL & 0xff) * inverseAmount))) & 0xff;
    int g = ((int) (((float) (color1 >> GREEN_CHANNEL & 0xff) * amount) +
            ((float) (color2 >> GREEN_CHANNEL & 0xff) * inverseAmount))) & 0xff;
    int b = ((int) (((float) (color1 & 0xff) * amount) +
            ((float) (color2 & 0xff) * inverseAmount))) & 0xff;

    return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b << BLUE_CHANNEL;
  }

  private Object getThemeProperty (int resId) {
    TypedValue typed_value = new TypedValue();
    getTheme().resolveAttribute(resId, typed_value, true);
    return typed_value.data;
  }

  private void addTab (FragmentTabHost tab_host, int spec, int title, Class<?> clas) {
    this.addTab(tab_host, getResources().getString(spec), getResources().getString(title), clas);
  }

  private void addTab (FragmentTabHost tab_host, int spec, String title, Class<?> clas) {
    this.addTab(tab_host, getResources().getString(spec), title, clas);
  }

  private void addTab (FragmentTabHost tab_host, String spec, int title, Class<?> clas) {
    this.addTab(tab_host, spec, getResources().getString(title), clas);
  }

  private void addTab (FragmentTabHost tab_host, String spec, String title, Class<?> clas) {
    View tab_view = this.createTabView(tab_host.getContext(), R.layout.controller_tab, title);
    TabHost.TabSpec tab_spec = tab_host.newTabSpec(spec).setIndicator(tab_view);
    tab_host.addTab(tab_spec, clas, null);
  }

  private View createTabView (Context context, int resId, int text) {
    return this.createTabView(context, resId, getResources().getString(text));
  }

  private View createTabView (Context context, int resId, String text) {
    View view = LayoutInflater.from(context).inflate(resId, null);
    TextView text_view = (TextView) view.findViewById(R.id.tab_title);
    text_view.setText(text);

    light_background_color = 0x55FFFFFF;

    background_list_drawable = new StateListDrawable();
    background_list_drawable.addState(new int[]{-android.R.attr.state_selected, android.R.attr.state_pressed}, new ColorDrawable(light_background_color));
    background_list_drawable.addState(new int[]{-android.R.attr.state_selected, android.R.attr.state_pressed, android.R.attr.state_focused}, new ColorDrawable(light_background_color));
    background_list_drawable.addState(new int[]{android.R.attr.state_selected, -android.R.attr.state_pressed}, tab_background);
    background_list_drawable.addState(new int[]{android.R.attr.state_selected, -android.R.attr.state_pressed, -android.R.attr.state_focused}, tab_background);
    background_list_drawable.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_pressed}, tab_background_all);
    background_list_drawable.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_pressed, android.R.attr.state_focused}, tab_background_all);
    background_list_drawable.addState(new int[]{}, new ColorDrawable(background_color));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      text_view.setBackground(background_list_drawable);
    } else {
      text_view.setBackgroundDrawable(background_list_drawable);
    }

    int dark_color = (int) this.getThemeProperty(R.attr.toolbar_color);
    int light_color = Color.argb(0xBB, Color.red(dark_color), Color.green(dark_color), Color.blue(dark_color));
    int[][] states = new int[][]{
            new int[]{-android.R.attr.state_selected, android.R.attr.state_pressed},
            new int[]{-android.R.attr.state_selected, android.R.attr.state_pressed, android.R.attr.state_focused},
            new int[]{android.R.attr.state_selected, -android.R.attr.state_pressed},
            new int[]{android.R.attr.state_selected, -android.R.attr.state_pressed, -android.R.attr.state_focused},
            new int[]{android.R.attr.state_selected, android.R.attr.state_pressed},
            new int[]{android.R.attr.state_selected, android.R.attr.state_pressed, android.R.attr.state_focused},
            new int[]{}
    };
    int[] colors = new int[]{
            light_color,
            light_color,
            dark_color,
            dark_color,
            dark_color,
            dark_color,
            light_color,
            light_color
    };
    text_color = new ColorStateList(states, colors);
    text_view.setTextColor(text_color);
    return view;
  }

  public void setSupportToolbar () {
    super.setSupportActionBar(toolbar);
    super.getSupportActionBar().setHomeButtonEnabled(true);
    super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    super.getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  public String getActiveTheme () {
    return preferencesEditor.getString("preference_theme", getResources().getString(R.string.preference_value_theme));
  }

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
  public void onBackPressed () {
    super.onBackPressed();
    this.nxt_connector.closeConnections();
  }

  @Override
  public void finish () {
    super.finish();
    super.overridePendingTransition(R.anim.controller_transition_back_in, R.anim.controller_transition_back_out);
  }

}*/