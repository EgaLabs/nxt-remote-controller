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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                                *
 *                                                                                                                                                                     *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                                 *
 *                                                                                                                                                                     *
 *  And the corresponding file at:                                                                                                                                     *
 *                                                                                                                                                                     *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/device/PairedDeviceViewHolder.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.device;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.andexert.library.RippleView;

import git.egatuts.nxtremotecontroller.R;

/*
 *  Extended class of RecyclerView.ViewHolder used to access properties from each holded item in the RecyclerView.Adapter.
 */
public class PairedDeviceViewHolder extends RecyclerView.ViewHolder {

  /*
   *  All the views.
   */
  public TextView connection;
  public TextView name;
  public TextView address;
  public RippleView rippleView;

  /*
   *  Constructor.
   */
  public PairedDeviceViewHolder (View itemView) {
    super(itemView);

    /*
     *  We save all the views in public properties to be accessible.
     */
    this.rippleView = (RippleView) itemView.findViewById(R.id.ripple_view);
    this.connection = (TextView)   itemView.findViewById(R.id.paired_device_connection);
    this.name       = (TextView)   itemView.findViewById(R.id.paired_device_name);
    this.address    = (TextView)   itemView.findViewById(R.id.paired_device_address);
  }

}