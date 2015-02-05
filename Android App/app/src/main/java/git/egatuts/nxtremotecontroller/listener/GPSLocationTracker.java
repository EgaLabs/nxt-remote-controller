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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                              *
 *                                                                                                                                                                   *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                               *
 *                                                                                                                                                                   *
 *  And the corresponding file at:                                                                                                                                   *
 *                                                                                                                                                                   *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/listener/GPSLocationTracker.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.listener;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/*
 *  Extended version of LocationListener that listens for GPS location updates (only 1 update) and executes it's internal listener.
 */
public class GPSLocationTracker implements LocationListener {

  public OnChangeLocation locationListener;
  public Context context;
  public LocationManager locationManager;
  public boolean isLocable;

  /*
   *  Represents a location change.
   */
  public interface OnChangeLocation {
    public void onChange (Location location);
  }

  /*
   *  Executes in all the constructors.
   */
  private void init () {
    this.locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
  }

  /*
   *  Constructors.
   */
  public GPSLocationTracker (Context context) {
    this.context = context;
    this.init();
  }

  public GPSLocationTracker (Context context, OnChangeLocation locationListener) {
    this.context = context;
    this.locationListener = locationListener;
    this.init();
  }

  /*
   *  Getter and setter for location change listener.
   */
  public void setOnChangeLocation (OnChangeLocation locationListener) {
    this.locationListener = locationListener;
  }

  public OnChangeLocation getOnChangeLocation () {
    return this.locationListener;
  }

  /*
   *  Returns true if GPS enabled. Returns false otherwise.
   */
  public boolean isGPSEnabled () {
    return this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  /*
   *  Returns true if Network enabled. Returns false otherwise.
   */
  public boolean isNetworkEnabled () {
    return this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
  }

  /*
   *  Returns true if it can be geolocalized.
   */
  public boolean isLocationAvailable () {
    return this.isLocable;
  }

  /*
   *  Stops the location updates.
   */
  public void stopLocationService () {
    this.locationManager.removeUpdates(this);
  }

  /*
   *  Starts the location updates.
   */
  public void startLocationService () {
    boolean gps = this.isGPSEnabled();
    boolean net = this.isNetworkEnabled();
    if (!gps && !net) return;
    this.isLocable = true;
    if (net) {
      this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    } else if (gps) {
      this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
  }

  /*
   *  Executes the internal listener when a new location is found.
   */
  @Override
  public void onLocationChanged (Location location) {
    if (this.locationListener != null) this.locationListener.onChange(location);
  }

  /*
   *  Pointless methods (in our app).
   */
  @Override public void onStatusChanged (String s, int i, Bundle bundle) {}
  @Override public void onProviderEnabled (String s) {}
  @Override public void onProviderDisabled (String s) {}

}