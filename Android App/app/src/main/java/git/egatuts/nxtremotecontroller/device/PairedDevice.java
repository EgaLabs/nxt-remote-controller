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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  You can find the entire project at:                                                                                                                      *
 *                                                                                                                                                           *
 *    https://github.com/Egatuts/nxt-remote-controller                                                                                                       *
 *                                                                                                                                                           *
 *  And the corresponding file at:                                                                                                                           *
 *                                                                                                                                                           *
 *    https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/device/PairedDevice.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.device;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import git.egatuts.nxtremotecontroller.fragment.ScanFragment;
import git.egatuts.nxtremotecontroller.preference.PreferencesUtils;

/*
 *  Paired device that saves a BluetoothDevice and some of it's properties for easy/fast access.
 */
public class PairedDevice implements Parcelable {

  /*
   *  The original BluetoothDevice.
   *  The name that the device uses to identify itself.
   *  The supposedly unique MAC address used to identify each device.
   *  The signal stored as a byte to save memory. An int type uses 32 bits of space.
   */
  private BluetoothDevice bluetoothDevice;
  private String name;
  private String address;
  private byte signal;

  /*
   *  Totally useless, but must be implemented.
   */
  @Override
  public int describeContents () {
    return 0;
  }

  /*
   *  This function is called by the android system when it's passed through an intent.
   *  It passes a Parcel where the class must be saved using only primitive values (int, byte, short, long)
   *  or any object/class that also implements the classes Parcelable or Serializable.
   */
  @Override
  public void writeToParcel (Parcel out, int flags) {
    out.writeStringArray(new String[]{this.name, this.address});
    out.writeByte(this.signal);
    out.writeParcelable(this.bluetoothDevice, flags);
  }

  /*
   *  Field used by the android system used to recreate our objects, individually or as arrays.
   *  you can always create a new constructor which accepts as argument a Parcel.
   *
   *  public YourClassConstructor (Parcel in) {
   *    //Some calculations, etc...
   *  }
   *
   *  And in your CREATOR field.
   *
   *  @Override
   *  public YourClass createFromParcel (Parcel in) {
   *    return new YourClass(in);
   *  }
   */
  public static final Parcelable.Creator<PairedDevice> CREATOR = new Parcelable.Creator<PairedDevice>() {
    @Override
    public PairedDevice createFromParcel (Parcel in) {

      /*
       *  We read each value from the Parcel.
       */
      String[] stringData = new String[2];
      in.readStringArray(stringData);
      byte signal = in.readByte();
      BluetoothDevice device = in.readParcelable(BluetoothDevice.class.getClassLoader());

      /*
       *  We return a new device with those values.
       */
      return new PairedDevice(stringData[0], stringData[1], signal, device);
    }

    /*
     *  Very simple. Probably useless.
     */
    @Override
    public PairedDevice[] newArray (int size) {
      return new PairedDevice[size];
    }
  };

  /*
   *  Constructor.
   */
  public PairedDevice (String name, String address, byte signal, BluetoothDevice device) {
    this.name = name;
    this.address = address;
    this.signal = signal;
    this.bluetoothDevice = device;
  }

  public PairedDevice (String name, String address, int connectivity, BluetoothDevice device) {
    this.name = name;
    this.address = address;
    this.setConnectivity(connectivity);
    this.bluetoothDevice = device;
  }

  /*
   *  Getter and setter for device name.
   */
  public String getName () {
    return this.name;
  }

  public void setName (String name) {
    this.name = name;
  }

  /*
   *  Getter and setter for mac address.
   */
  public String getAddress () {
    return this.address;
  }

  public void setAddress (String address) {
    this.address = address;
  }

  /*
   *  Getter and setter for signal strength.
   */
  public byte getSignal () {
    return this.signal;
  }

  public void setSignal (byte signal) {
    this.signal = signal;
  }

  /*
   *  Getter and setter for connectivity (signal strength on 0 to 100 scale).
   */
  public int getConnectivity () {
    return (int) ((float) (this.signal & 0xff) / 0xff * 100);
  }

  public void setConnectivity (int connectivity) {
    int sanitized = Math.min(100, Math.max(0, connectivity));
    byte binary = (byte) ((float) sanitized / 100 * 0xff);
    this.setSignal(binary);
  }

  /*
   *  Getter for device type.
   */
  public int getIntDeviceType () {
    return this.bluetoothDevice.getBluetoothClass().getDeviceClass();
  }

  /*
   *  Getters for toy devices.
   */
  public boolean isToyRobot () {
    return this.getIntDeviceType() == BluetoothClass.Device.TOY_ROBOT;
  }

  public boolean isToyGame () {
    return this.getIntDeviceType() == BluetoothClass.Device.TOY_GAME;
  }

  public boolean isToyActionFigure () {
    return this.getIntDeviceType() == BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE;
  }

  public boolean isToyController () {
    return this.getIntDeviceType() == BluetoothClass.Device.TOY_CONTROLLER;
  }

  public boolean isToyVehicle () {
    return this.getIntDeviceType() == BluetoothClass.Device.TOY_VEHICLE;
  }

  public boolean isToyUncategorized () {
    return this.getIntDeviceType() == BluetoothClass.Device.TOY_UNCATEGORIZED;
  }

  /*
   *  Getters for wearable devices.
   */
  public boolean isWearableGlasses () {
    return this.getIntDeviceType() == BluetoothClass.Device.WEARABLE_GLASSES;
  }

  public boolean isWearableHelmet () {
    return this.getIntDeviceType() == BluetoothClass.Device.WEARABLE_HELMET;
  }

  public boolean isWearableJacket () {
    return this.getIntDeviceType() == BluetoothClass.Device.WEARABLE_JACKET;
  }

  public boolean isWearablePager () {
    return this.getIntDeviceType() == BluetoothClass.Device.WEARABLE_PAGER;
  }

  public boolean isWearableWatch () {
    return this.getIntDeviceType() == BluetoothClass.Device.WEARABLE_WRIST_WATCH;
  }

  public boolean isWearableUncategorized () {
    return this.getIntDeviceType() == BluetoothClass.Device.WEARABLE_UNCATEGORIZED;
  }

  /*
   *  Getters for health devices.
   */
  public boolean isHealthBloodPressureDevice () {
    return this.getIntDeviceType() == BluetoothClass.Device.HEALTH_BLOOD_PRESSURE;
  }

  public boolean isHealthDataDisplayDevice () {
    return this.getIntDeviceType() == BluetoothClass.Device.HEALTH_DATA_DISPLAY;
  }

  public boolean isHealthGlucoseDevice () {
    return this.getIntDeviceType() == BluetoothClass.Device.HEALTH_GLUCOSE;
  }

  public boolean isHealthPulseOximeterDevice () {
    return this.getIntDeviceType() == BluetoothClass.Device.HEALTH_PULSE_OXIMETER;
  }

  public boolean isHealthPulseRateDevice () {
    return this.getIntDeviceType() == BluetoothClass.Device.HEALTH_PULSE_RATE;
  }

  public boolean isHealthThermometerDevice () {
    return this.getIntDeviceType() == BluetoothClass.Device.HEALTH_THERMOMETER;
  }

  public boolean isHealthWeighingDevice () {
    return this.getIntDeviceType() == BluetoothClass.Device.HEALTH_WEIGHING;
  }

  public boolean isHealthUncategorized () {
    return this.getIntDeviceType() == BluetoothClass.Device.HEALTH_UNCATEGORIZED;
  }

  /*
   *  Getters for phone devices.
   */
  public boolean isPhoneCellular () {
    return this.getIntDeviceType() == BluetoothClass.Device.PHONE_CELLULAR;
  }

  public boolean isPhoneCordless () {
    return this.getIntDeviceType() == BluetoothClass.Device.PHONE_CORDLESS;
  }

  public boolean isPhoneISDN () {
    return this.getIntDeviceType() == BluetoothClass.Device.PHONE_ISDN;
  }

  public boolean isPhoneModemOrGateway () {
    return this.getIntDeviceType() == BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY;
  }

  public boolean isPhoneSmart () {
    return this.getIntDeviceType() == BluetoothClass.Device.PHONE_SMART;
  }

  public boolean isPhoneUncategorized () {
    return this.getIntDeviceType() == BluetoothClass.Device.PHONE_UNCATEGORIZED;
  }

  /*
   *  Getters for computer devices.
   */
  public boolean isComputerDesktop () {
    return this.getIntDeviceType() == BluetoothClass.Device.COMPUTER_DESKTOP;
  }

  public boolean isComputerHandheldPDA () {
    return this.getIntDeviceType() == BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA;
  }

  public boolean isComputerLaptop () {
    return this.getIntDeviceType() == BluetoothClass.Device.COMPUTER_LAPTOP;
  }

  public boolean isComputerPalmSizePDA () {
    return this.getIntDeviceType() == BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA;
  }

  public boolean isComputerServer () {
    return this.getIntDeviceType() == BluetoothClass.Device.COMPUTER_SERVER;
  }

  public boolean isComputerWearable () {
    return this.getIntDeviceType() == BluetoothClass.Device.COMPUTER_WEARABLE;
  }

  public boolean isComputerUncategorized () {
    return this.getIntDeviceType() == BluetoothClass.Device.COMPUTER_UNCATEGORIZED;
  }

  /*
   *  Getters for audio/video devices.
   */
  public boolean isCamcorder () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER;
  }

  public boolean isCar () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO;
  }

  public boolean isHandsfree () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE;
  }

  public boolean isHeadphones () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES;
  }

  public boolean isAudioVideoHifi () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO;
  }

  public boolean isSpeakers () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER;
  }

  public boolean isMicrophone () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE;
  }

  public boolean isPortableAudio () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO;
  }

  public boolean isAudioVideoSetTopBox () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX;
  }

  public boolean isVideoCasetteRecorder () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_VCR;
  }

  public boolean isVideoCamera () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA;
  }

  public boolean isVideoConferencing () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING;
  }

  public boolean isVideoDisplayAndCamcorder () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER;
  }

  public boolean isVideoGamingToy () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY;
  }

  public boolean isVideoMonitor () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR;
  }

  public boolean isWearableHeadset () {
    return this.getIntDeviceType() == BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET;
  }

  /*
   *  Getter for major types.
   */
  public int getIntMajorType () {
    return this.bluetoothDevice.getBluetoothClass().getMajorDeviceClass();
  }

  public boolean isMajorPhone () {
    return this.getIntMajorType() == BluetoothClass.Device.Major.PHONE;
  }

  public boolean isMajorComputer () {
    return this.getIntMajorType() == BluetoothClass.Device.Major.COMPUTER;
  }

  public boolean isMajorHealth () {
    return this.getIntMajorType() == BluetoothClass.Device.Major.HEALTH;
  }

  public boolean isMajorImaging () {
    return this.getIntMajorType() == BluetoothClass.Device.Major.IMAGING;
  }

  public boolean isMajorMisc () {
    return this.getIntMajorType() == BluetoothClass.Device.Major.MISC;
  }

  public boolean isMajorNetworking () {
    return this.getIntMajorType() == BluetoothClass.Device.Major.NETWORKING;
  }

  public boolean isMajorPeripheral () {
    return this.getIntMajorType() == BluetoothClass.Device.Major.PERIPHERAL;
  }

  public boolean isMajorWearable () {
    return this.getIntMajorType() == BluetoothClass.Device.Major.WEARABLE;
  }

  public boolean isMajorToy () {
    return this.getIntMajorType() == BluetoothClass.Device.Major.TOY;
  }

  public boolean isMajorUncategorized () {
    return this.getIntMajorType() == BluetoothClass.Device.Major.UNCATEGORIZED;
  }

  /*
   *  Getter and setter for bluetooth device.
   */
  public BluetoothDevice getBluetoothDevice () {
    return bluetoothDevice;
  }

  public void setBluetoothDevice (BluetoothDevice device) {
    bluetoothDevice = device;
  }

  /*
   *  Calculates the connectivity percentage.
   */
  public static int calculateConnectivity (int min, int max, int con, PreferencesUtils.Editor editor) {
    int realmax = max;
    int realmin = min;
    if (con > max) {
      editor.saveString(ScanFragment.PREFERENCE_MAX_SIGNAL_KEY, "" + con);
      realmax = con;
    }
    if (con < min) {
      editor.saveString(ScanFragment.PREFERENCE_MIN_SIGNAL_KEY, "" + con);
      realmin = con;
    }
    int total = max - min;
    int portion = con - min;
    return (int) (((float) portion / (float) total) * 100);
  }

  public static int calculateConnectivity (int min, int max, byte sig, PreferencesUtils.Editor editor) {
    return PairedDevice.calculateConnectivity(min, max, (int) ((float) (sig & 0xff) / 0xff * 100), editor);
  }

  public static int calculateConnectivity (int min, int max, Intent intent, PreferencesUtils.Editor editor) {
    return PairedDevice.calculateConnectivity(min, max, (int) intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE), editor);
  }

  /*
   *  Exports a BluetoothDevice as PairedDevice.
   */
  public static PairedDevice from (BluetoothDevice device, int connectivity) {
    return new PairedDevice(device.getName(), device.getAddress(), connectivity, device);
  }

  public static PairedDevice from (BluetoothDevice device, byte signal) {
    return new PairedDevice(device.getName(), device.getAddress(), signal, device);
  }

  public static PairedDevice from (BluetoothDevice device, Intent intent, int min, int max, PreferencesUtils.Editor editor) {
    int connectivity = PairedDevice.calculateConnectivity(min, max, intent, editor);
    return new PairedDevice(device.getName(), device.getAddress(), connectivity, device);
  }

  public static PairedDevice from (Intent intent, int min, int max, PreferencesUtils.Editor editor) {
    return PairedDevice.from((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE), intent, min, max, editor);
  }

  public static PairedDevice from (BluetoothDevice device) {
    return new PairedDevice(device.getName(), device.getAddress(), (byte) 0, device);
  }

}