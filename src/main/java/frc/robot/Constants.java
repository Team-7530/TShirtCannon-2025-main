package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public class Constants {
  /**
   * public static final class DriveConstants { public static final int kLeftMotor1Port = 0; public
   * static final int kLeftMotor2Port = 1; public static final int kRightMotor1Port = 2; public
   * static final int kRightMotor2Port = 3; }
   */
  public static final int kID_LMasterDrive = 1; // SparkMAX
  public static final int kID_LFollowDrive = 2; // SparkMAX
  public static final int kID_RMasterDrive = 3; // SparkMAX
  public static final int kID_RFollowDrive = 4; // SparkMAX
  public static final int kID_CANdle = 20;

  public static final int kID_CannonElevator = 6;
  public static final int kID_CannonRotator = 5;

  public static final double kMaxSpeedFactor = 0.3;
}
