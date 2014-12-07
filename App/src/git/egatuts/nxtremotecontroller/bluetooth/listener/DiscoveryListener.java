package git.egatuts.nxtremotecontroller.bluetooth.listener;

import android.content.Context;
import android.content.Intent;

public abstract class DiscoveryListener extends BaseListener {

  @Override public void onConnectionChange  (Context context, Intent intent) {}
  //@Override public void onDiscoveryFinish (Context context, Intent intent) {}
  //@Override public void onDiscoveryStart  (Context context, Intent intent) {}
  @Override public void onLocalNameChange   (Context context, Intent intent) {}
  @Override public void onScanModeChange    (Context context, Intent intent) {}
  @Override public void onStateChange       (Context context, Intent intent) {}
  @Override public void onLowLevelConnect           (Context context, Intent intent) {}
  @Override public void onLowLevelDisconnect        (Context context, Intent intent) {}
  @Override public void onLowLevelDisconnectRequest (Context context, Intent intent) {}
  @Override public void onBondStateChange           (Context context, Intent intent) {}
  @Override public void onDeviceClassChange         (Context context, Intent intent) {}
  //@Override public void onDeviceFound             (Context context, Intent intent) {}
  @Override public void onRemoteNameChange          (Context context, Intent intent) {}

}