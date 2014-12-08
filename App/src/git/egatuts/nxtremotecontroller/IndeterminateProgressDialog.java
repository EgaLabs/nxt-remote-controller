package git.egatuts.nxtremotecontroller;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

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
  
  public void setText (int resId) {
    this.setText(_context.getResources().getString(resId));
  }
  
  public void setText (String text) {
    ((TextView) findViewById(R.id.text_view)).setText(text);
  }
  
}