/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (c) 2014 EgaTuts & Esaú García - All Rights Reserved                *
 *                                                                               *
 * Open-source code licensed under the MIT License (the "License").              *
 *                                                                               *
 * Permission is hereby granted, free of charge, to any person obtaining a copy  *
 * of this software and associated documentation files (the "Software"), to deal *
 * in the Software without restriction, including without limitation the rights  *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell     *
 * copies of the Software, and to permit persons to whom the Software is         *
 * furnished to do so, subject to the following conditions:                      *
 *                                                                               *
 * The above copyright notice and this permission notice shall be included in    *
 * all copies or substantial portions of the Software.                           *
 *                                                                               *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR    *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,      *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE   *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER        *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN     *
 * THE SOFTWARE.                                                                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                                                   *
 *                                                                                                                                                                       *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                                                    *
 *                                                                                                                                                                       *
 * And the corresponding file at:                                                                                                                                        *
 *                                                                                                                                                                       *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/views/IndeterminateProgressDialog.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
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

  public void setColor (int color) {
    this.findViewById(R.id.progress_bar).setBackgroundColor(color);
  }

}