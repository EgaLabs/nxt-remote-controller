package git.egatuts.nxtremotecontroller.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import git.egatuts.nxtremotecontroller.R;

public class IndeterminateProgressDialog extends ProgressDialog {
  
  private Context _context;
  
  public IndeterminateProgressDialog (Context context) {
    super(context);
    this.setCancelable(false);
    _context = context;
  }
  
  @Override
  public void show () {
    super.show();
    this.setContentView(R.layout.progress_dialog);
  }

  public void setDoFirstAnimation (boolean value) {
      ((ProgressBarCircularIndeterminate) this.findViewById(R.id.progress_bar)).setDoFirstAnimation(value);
  }

  public boolean getDoFirstAnimation () {
    return ((ProgressBarCircularIndeterminate) this.findViewById(R.id.progress_bar)).getDoFirstAnimation();
  }
  
  public void setText (int resId) {
    this.setText(_context.getResources().getString(resId));
  }
  
  public void setText (String text) {
    ((TextView) findViewById(R.id.text_view)).setText(text);
  }
  
}