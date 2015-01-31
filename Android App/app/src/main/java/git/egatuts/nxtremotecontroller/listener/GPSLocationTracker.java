package git.egatuts.nxtremotecontroller.listener;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSLocationTracker implements LocationListener {

  public OnChangeLocation locationListener;
  public Context context;
  public LocationManager locationManager;
  public boolean isLocable;

  public interface OnChangeLocation {
    public void onChange (Location location);
  }

  private void init () {
    this.locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
  }

  public GPSLocationTracker (Context context) {
    this.context = context;
    this.init();
  }

  public GPSLocationTracker (Context context, OnChangeLocation locationListener) {
    this.context = context;
    this.locationListener = locationListener;
    this.init();
  }

  public void setOnChangeLocation (OnChangeLocation locationListener) {
    this.locationListener = locationListener;
  }

  public OnChangeLocation getOnChangeLocation () {
    return this.locationListener;
  }

  public boolean isGPSEnabled () {
    return this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  public boolean isNetworkEnabled () {
    return this.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
  }

  public boolean isLocationAvailable () {
    return this.isLocable;
  }

  public void stopLocationService () {
    this.locationManager.removeUpdates(this);
  }

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

  @Override
  public void onLocationChanged (Location location) {
    if (this.locationListener != null) this.locationListener.onChange(location);
  }

  @Override public void onStatusChanged (String s, int i, Bundle bundle) {}
  @Override public void onProviderEnabled (String s) {}
  @Override public void onProviderDisabled (String s) {}

}