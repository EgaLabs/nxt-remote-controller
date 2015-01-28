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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                                    *
 *                                                                                                                                                                         *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                                     *
 *                                                                                                                                                                         *
 *  And the corresponding file at:                                                                                                                                         *
 *                                                                                                                                                                         *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/fragment/OnlineControllerFragment.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.fragment;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.activity.ControllerActivity;
import git.egatuts.nxtremotecontroller.client.Client;
import git.egatuts.nxtremotecontroller.client.ClientAdapter;
import git.egatuts.nxtremotecontroller.views.BaseProgressDialog;

/*
 *  Fragment used in the ControllerActivity to control the robot remotely from a computer or other phone connected to the same NET
 *  as the phone that is holding the NodeJS server and the application to connect to the robot.
 *  This fragment will send audio and video record to the remote controller and will have access to the
 *  robot sensors and the phone flash (if possible).
 */
public class OnlineControllerFragment extends BaseFragment {

  private RecyclerView recyclerView;
  private ClientAdapter clientsAdapter;
  private BaseProgressDialog progressDialog;
  private long showingTime;
  private int networkType;

  public void refreshFragment () {
    ((ControllerActivity) this.getBaseActivity()).setTab(0);
    ((ControllerActivity) this.getBaseActivity()).setTab(1);
  }

  @Override
  public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    final OnlineControllerFragment self = this;
    final WifiManager wifiManager = this.getWifiManager();
    this.progressDialog = this.getShortProgressDialog();
    View view;
    Button button;
    if (!wifiManager.isWifiEnabled()) {
      view = inflater.inflate(R.layout.online_error_layout, parent, false);
      button = (Button) view.findViewById(R.id.redo_action);
      button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v) {
          self.refreshFragment();
        }
      });
      return view;
    }

    view = inflater.inflate(R.layout.online_layout, parent, false);
    this.recyclerView = (RecyclerView) view.findViewById(R.id.clients);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseActivity());
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    linearLayoutManager.scrollToPosition(0);

    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    this.clientsAdapter = new ClientAdapter(this, new ArrayList<Client>());
    recyclerView.setAdapter(this.clientsAdapter);
    Client ejemplo = new Client();
    ejemplo.setName("EgaTuts");
    ejemplo.setEmail("egatuts@gmail.com");
    ejemplo.setLatitude(41.2133024d);
    ejemplo.setLongitude(1.5255549d);
    ejemplo.setShortLocation("El Vendrell, Cataluña");
    ejemplo.setLongLocation("El Vendrell, Tarragona, Cataluña, España");
    this.clientsAdapter.add(ejemplo);
    return view;
  }

}
