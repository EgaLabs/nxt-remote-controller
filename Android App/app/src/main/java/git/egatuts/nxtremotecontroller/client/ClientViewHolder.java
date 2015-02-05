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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE   *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER        *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN     *
 * THE SOFTWARE.                                                                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                                         *
 *                                                                                                                                                             *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                                          *
 *                                                                                                                                                             *
 * And the corresponding file at:                                                                                                                              *
 *                                                                                                                                                             *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/client/ClientViewHolder.java *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.client;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;

import git.egatuts.nxtremotecontroller.R;

/*
 *  RecyclerViewHolder that stores the views of the client interface.
 */
public class ClientViewHolder extends RecyclerView.ViewHolder {

  public TextView name;
  public TextView email;
  public TextView coords;
  public TextView location;
  public ImageView profileImage;
  public RippleView nameRipple;
  public RippleView coordsRipple;
  public RippleView imageRipple;

  /*
   *  Construcor.
   */
  public ClientViewHolder (View item) {
    super(item);
    this.name         = (TextView)   item.findViewById(R.id.name);
    this.email        = (TextView)   item.findViewById(R.id.email);
    this.location     = (TextView)   item.findViewById(R.id.location);
    this.coords       = (TextView)   item.findViewById(R.id.coords);
    this.profileImage = (ImageView)  item.findViewById(R.id.profile_image);
    this.nameRipple   = (RippleView) item.findViewById(R.id.name_ripple) ;
    this.coordsRipple = (RippleView) item.findViewById(R.id.coords_ripple) ;
    this.imageRipple  = (RippleView) item.findViewById(R.id.image_ripple) ;
  }

}
