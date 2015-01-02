package git.egatuts.nxtremotecontroller.bluetooth;

import git.egatuts.nxtremotecontroller.GlobalUtils;

public class BluetoothConstants {

  /* Bytes used to send telegrams to the robot */
  public static final byte COMMAND_DIRECT_RESPONSE    = 0x00;
  public static final byte COMMAND_SYSTEM_RESPONSE    = 0x01;
  public static final byte COMMAND_DIRECT_NO_RESPONSE = (byte) 0x80;
  public static final byte COMMAND_SYSTEM_NO_RESPONSE = (byte) 0x81;

  public static final byte OUTPUT_STATE_MOTOR = 0x04;

  public static final byte MOTOR_A   = 0x00;
  public static final byte MOTOR_B   = 0x01;
  public static final byte MOTOR_C   = 0x02;
  public static final byte MOTOR_ALL = (byte) 0xff;

  public static final byte MOTOR_MAX_POWER = 0x64;

  /* Bytes used to receive telegrams from the robot */
  public static final byte COMMAND_REPLY = 0x02;

  /*
   *  Returns the byte array to control the left motor. Can be personalized.
   */
  public static byte[] motorC (double power, boolean regulateSpeed, boolean syncMotors) {
    byte roundedPower = (byte) (power * BluetoothConstants.MOTOR_MAX_POWER);
    byte[] command = {
            0x0C, 0x00, /* Command length (LSB) */
            BluetoothConstants.COMMAND_DIRECT_NO_RESPONSE,
            BluetoothConstants.OUTPUT_STATE_MOTOR,
            BluetoothConstants.MOTOR_C,
            roundedPower,
            0x07,
            0x00,
            0x00,
            0x20,
            0x00, 0x00, 0x00, 0x00
    };
    if (regulateSpeed) command[7] |= 0x01;
    if (syncMotors) command[7] |= 0x02;
    return command;
  }

  /*
   *  Returns the byte array to control the right motor. Can be personalized.
   */
  public static byte[] motorB (double power, boolean regulateSpeed, boolean syncMotors) {
    byte roundedPower = (byte) (power * BluetoothConstants.MOTOR_MAX_POWER);
    byte[] command = {
            0x0C, 0x00, /* Command length (LSB) */
            BluetoothConstants.COMMAND_DIRECT_NO_RESPONSE,
            BluetoothConstants.OUTPUT_STATE_MOTOR,
            BluetoothConstants.MOTOR_B,
            roundedPower,
            0x07,
            0x00,
            0x00,
            0x20,
            0x00, 0x00, 0x00, 0x00
    };
    if (regulateSpeed) command[7] |= 0x01;
    if (syncMotors) command[7] |= 0x02;
    return command;
  }

  /*
   *  Returns the byte array to control the arm motor. Can be personalized.
   */
  public static byte[] motorA (double power, boolean regulateSpeed, boolean syncMotors) {
    byte roundedPower = (byte) (power * BluetoothConstants.MOTOR_MAX_POWER);
    if (power < 0) {
      roundedPower = (byte) (0xff - roundedPower);
    }
    byte[] command = {
            0x0C, 0x00, /* Command length (LSB) */
            BluetoothConstants.COMMAND_DIRECT_NO_RESPONSE,
            BluetoothConstants.OUTPUT_STATE_MOTOR,
            BluetoothConstants.MOTOR_A,
            roundedPower,
            0x07,
            0x00,
            0x00,
            0x20,
            0x00, 0x00, 0x00, 0x00
    };
    if (regulateSpeed) command[7] |= 0x01;
    if (syncMotors) command[7] |= 0x02;
    return command;
  }

  /*
   *  Moves the two motors that let the robot move.
   */
  public static byte[] motorBC (double powerLeft, double powerRight, boolean regulateSpeed, boolean syncMotors) {
    byte[] motorLeft  = BluetoothConstants.motorC(powerLeft, regulateSpeed, syncMotors);
    byte[] motorRight = BluetoothConstants.motorB(powerRight, regulateSpeed, syncMotors);
    return GlobalUtils.concatBytes(motorLeft, motorRight);
  }

}
