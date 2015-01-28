package git.egatuts.nxtremotecontroller.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

  public static final int NETWORK_TYPE_UNKNOWN       = 0;
  public static final int NETWORK_TYPE_WIFI          = 1;
  public static final int NETWORK_TYPE_MOBILE        = 2;
  public static final int NETWORK_TYPE_NOT_CONNECTED = 3;

  public static int getConnectionType (NetworkInfo info) {
    if (info != null) {
      int type = info.getType();
      if (type == ConnectivityManager.TYPE_WIFI) {
        return NetworkUtils.NETWORK_TYPE_WIFI;
      } else if (type == ConnectivityManager.TYPE_MOBILE) {
        return NetworkUtils.NETWORK_TYPE_MOBILE;
      }
      return NetworkUtils.NETWORK_TYPE_UNKNOWN;
    }
    return NetworkUtils.NETWORK_TYPE_NOT_CONNECTED;
  }

  public static int getConnectionType (Context context) {
    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    return NetworkUtils.getConnectionType(manager.getActiveNetworkInfo());
  }

  public static int getConnectionType (Intent intent) {
    return NetworkUtils.getConnectionType((NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO));
  }

}