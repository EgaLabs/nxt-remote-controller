package git.egatuts.nxtremotecontroller.fragment;

import android.app.Activity;
import android.os.Handler;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.lang.Runnable;

import git.egatuts.nxtremotecontroller.R;
import git.egatuts.nxtremotecontroller.bluetooth.BluetoothUtils;
import git.egatuts.nxtremotecontroller.bluetooth.listener.BluetoothEnableListener;
import git.egatuts.nxtremotecontroller.bluetooth.receiver.BluetoothEnableReceiver;
import git.egatuts.nxtremotecontroller.views.IndeterminateProgressDialog;

public abstract class BaseFragment extends Fragment {

  protected BluetoothEnableReceiver bluetooth_enable_receiver;
  protected BluetoothEnableListener bluetooth_enable_listener;
  protected IndeterminateProgressDialog progress_dialog;
  protected DialogInterface.OnDismissListener progress_dialog_on_dismiss;
  protected BaseFragment last_fragment;
  protected BaseFragment new_fragment;
  protected BluetoothUtils bluetooth_utils;
  private long show_date;

  /*
   * Changes the fragment using animations.
   */
  public void changeFragmentTo (BaseFragment fragment, boolean transition) {
    BaseFragment.changeFragmentTo(getActivity().getSupportFragmentManager(), this, fragment, transition);
  }

  public static void changeFragmentTo (FragmentManager manager, BaseFragment object, BaseFragment fragment, boolean transition) {
    fragment.setLastFragment(object);
    FragmentTransaction transaction = manager.beginTransaction();
    if (transition) transaction.setCustomAnimations(R.anim.transaction_in, R.anim.transaction_out);
    transaction.replace(R.id.main_container, fragment);
    transaction.commit();
  }

  /*
   * Getter and setter for BluetoothEnableReceiver.
   */
  public BluetoothEnableReceiver getBluetoothEnableReceiver () {
    return bluetooth_enable_receiver;
  }

  public void setBluetoothEnableReceiver (BluetoothEnableReceiver receiver) {
    bluetooth_enable_receiver = receiver;
  }

  /*
   * Getter and setter for BluetoothEnableListener.
   */
  public BluetoothEnableListener getBluetoothEnableListener () {
    return bluetooth_enable_listener;
  }

  public void setBluetoothEnableListener (BluetoothEnableListener listener) {
    bluetooth_enable_listener = listener;
  }

  /*
   * Getter and setter to remember the previous fragment.
   */
  public void setLastFragment (BaseFragment fragment) {
    last_fragment = fragment;
  }

  public BaseFragment getLastFragment () {
    return last_fragment;
  }

  /*
   * Getter and setter for BluetoothUtils.
   */
  public void setBluetoothUtils (BluetoothUtils utils) {
    bluetooth_utils = utils;
  }

  public BluetoothUtils getBluetoothUtils () {
    return bluetooth_utils;
  }

  /*
   * Creates the receiver and the listener if are null.
   */
  public void initListenersAndReceivers () {
    final BaseFragment self = this;
    if (progress_dialog == null) progress_dialog = new IndeterminateProgressDialog(getActivity());
    if (progress_dialog_on_dismiss == null) progress_dialog_on_dismiss = new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss (DialogInterface dialog) {
        progress_dialog.setOnDismissListener(null);
        new Handler().postDelayed(new Runnable () {
          @Override
          public void run () {
            changeFragmentTo(new_fragment, true);
          }
        }, 200);
      }
    };
    if (bluetooth_enable_receiver == null) bluetooth_enable_receiver = new BluetoothEnableReceiver(getActivity());
    if (bluetooth_enable_listener == null) bluetooth_enable_listener = new BluetoothEnableListener() {
      @Override
      public void onStateChange (Context context, Intent intent) {
        int state = BluetoothEnableReceiver.getIntentExtraData(intent);
        boolean hasToShow = false;
        int hasToChange = 0;
        Integer resource_text = null;
        switch (state) {
          case BluetoothAdapter.STATE_OFF:
            hasToChange = 2;
          break;
          case BluetoothAdapter.STATE_ON:
            hasToChange = 1;
          break;
          case BluetoothAdapter.STATE_TURNING_OFF:
            hasToShow = true;
            resource_text = R.string.bluetooth_disabling;
          break;
          case BluetoothAdapter.STATE_TURNING_ON:
            hasToShow = true;
            resource_text = R.string.bluetooth_enabling;
          break;
        }
        if (hasToShow == true && resource_text != null) {
          show_date = System.currentTimeMillis();
          if (!progress_dialog.isShowing()) progress_dialog.show();
          progress_dialog.setDoFirstAnimation(false);
          progress_dialog.setText(resource_text);
        }
        if (hasToChange != 0) {
          if (hasToChange == 2) new_fragment = new BluetoothFragment(bluetooth_utils);
          if (hasToChange == 1) new_fragment = last_fragment;
          if (!hasToShow && progress_dialog.isShowing()) {
            progress_dialog.setOnDismissListener(progress_dialog_on_dismiss);
            if (System.currentTimeMillis() - show_date < 750) {
              new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  progress_dialog.dismiss();
                }
              }, 750);
            } else {
              progress_dialog.dismiss();
            }
          }
        }
      }
    };
  }

  public void listenForBluetoothChanges () {
    bluetooth_enable_receiver.setListener(bluetooth_enable_listener);
    bluetooth_enable_receiver.registerReceiver();
  }

  public void unlistenForBluetoothChanges () {
    bluetooth_enable_receiver.unregisterReceiver();
  }
}
