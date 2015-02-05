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

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * You can find the entire project at:                                                                                                                               *
 *                                                                                                                                                                   *
 *   https://github.com/Egatuts/nxt-remote-controller                                                                                                                *
 *                                                                                                                                                                   *
 * And the corresponding file at:                                                                                                                                    *
 *                                                                                                                                                                   *
 *   https://github.com/Egatuts/nxt-remote-controller/blob/master/Android%20App/app/src/main/java/git/egatuts/nxtremotecontroller/bluetooth/BluetoothConstants.java  *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package git.egatuts.nxtremotecontroller.bluetooth;

import git.egatuts.nxtremotecontroller.utils.GlobalUtils;

/*
 * Bluetooth constants containing commands sent to the robot and some static methods.
 */
public class BluetoothConstants {

  /* Bytes used to send telegrams to the robot */
  public static final byte[] LENGTH_OUTPUT_STATE = { 0x0C, 0x00 };

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

  public static final byte MOTOR_MODE_COAST                      = 0x00;
  public static final byte MOTOR_MODE_ON                         = 0x01;
  public static final byte MOTOR_MODE_BREAK                      = 0x02;
  public static final byte MOTOR_MODE_ON_AND_BREAK               = 0x03;
  public static final byte MOTOR_MODE_REGULATED                  = 0x04;
  public static final byte MOTOR_MODE_ON_AND_REGULATED           = 0x05;
  public static final byte MOTOR_MODE_ON_AND_BREAK_AND_REGULATED = 0x07;

  public static final byte REGULATION_MODE_NONE   = 0x00;
  public static final byte REGULATION_MODE_SPEED  = 0x01;
  public static final byte REGULATION_MODE_MOTORS = 0x02;

  public static final byte TURN_RATIO_NONE = 0x00;
  public static final byte TURN_RATIO_MAX = 0x64;

  public static final byte RUN_STATE_NONE      = 0x00;
  public static final byte RUN_STATE_RAMP_UP   = 0x10;
  public static final byte RUN_STATE_RUNNING   = 0x20;
  public static final byte RUN_STATE_RAMP_DOWN = 0x40;

  public static final byte[] TACHO_LIMIT_NONE = { 0x00, 0x00, 0x00, 0x00 };

  /* Bytes used to receive telegrams from the robot */
  public static final byte COMMAND_REPLY = 0x02;

  /*
   *  Returns the byte array to control the left motor. Can be personalized.
   */
  public static byte[] motorC (double power, boolean regulateSpeed, boolean syncMotors) {
    byte roundedPower = (byte) (power * BluetoothConstants.MOTOR_MAX_POWER);
    byte[] rawCommand = {
            BluetoothConstants.COMMAND_DIRECT_NO_RESPONSE,
            BluetoothConstants.OUTPUT_STATE_MOTOR,
            BluetoothConstants.MOTOR_C,
            roundedPower,
            BluetoothConstants.MOTOR_MODE_ON_AND_BREAK_AND_REGULATED,
            BluetoothConstants.REGULATION_MODE_NONE,
            BluetoothConstants.TURN_RATIO_NONE,
            BluetoothConstants.RUN_STATE_RUNNING
    };
    byte[] command = GlobalUtils.concatBytes(BluetoothConstants.LENGTH_OUTPUT_STATE, rawCommand);
    if (regulateSpeed) command[7] |= 0x01;
    if (syncMotors) command[7] |= 0x02;
    return GlobalUtils.concatBytes(command, BluetoothConstants.TACHO_LIMIT_NONE);
  }

  /*
   *  Returns the byte array to control the right motor. Can be personalized.
   */
  public static byte[] motorB (double power, boolean regulateSpeed, boolean syncMotors) {
    byte roundedPower = (byte) (power * BluetoothConstants.MOTOR_MAX_POWER);
    byte[] rawCommand = {
      BluetoothConstants.COMMAND_DIRECT_NO_RESPONSE,
      BluetoothConstants.OUTPUT_STATE_MOTOR,
      BluetoothConstants.MOTOR_B,
      roundedPower,
      BluetoothConstants.MOTOR_MODE_ON_AND_BREAK_AND_REGULATED,
      BluetoothConstants.REGULATION_MODE_NONE,
      BluetoothConstants.TURN_RATIO_NONE,
      BluetoothConstants.RUN_STATE_RUNNING
    };
    byte[] command = GlobalUtils.concatBytes(BluetoothConstants.LENGTH_OUTPUT_STATE, rawCommand);
    if (regulateSpeed) command[7] |= 0x01;
    if (syncMotors) command[7] |= 0x02;
    return GlobalUtils.concatBytes(command, BluetoothConstants.TACHO_LIMIT_NONE);
  }

  /*
   *  Returns the byte array to control the arm motor. Can be personalized.
   */
  public static byte[] motorA (double power, boolean regulateSpeed, boolean syncMotors) {
    byte roundedPower = (byte) (power * BluetoothConstants.MOTOR_MAX_POWER);
    byte[] rawCommand = {
      BluetoothConstants.COMMAND_DIRECT_NO_RESPONSE,
      BluetoothConstants.OUTPUT_STATE_MOTOR,
      BluetoothConstants.MOTOR_A,
      roundedPower,
      BluetoothConstants.MOTOR_MODE_ON_AND_BREAK_AND_REGULATED,
      BluetoothConstants.REGULATION_MODE_NONE,
      BluetoothConstants.TURN_RATIO_NONE,
      BluetoothConstants.RUN_STATE_RUNNING
    };
    byte[] command = GlobalUtils.concatBytes(BluetoothConstants.LENGTH_OUTPUT_STATE, rawCommand);
    if (regulateSpeed) command[7] |= 0x01;
    if (syncMotors) command[7] |= 0x02;
    return GlobalUtils.concatBytes(command, BluetoothConstants.TACHO_LIMIT_NONE);
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
