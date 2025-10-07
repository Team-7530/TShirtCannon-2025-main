package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private static RobotContainer instance;

  // The robot's subsystems
  public final Cannon m_cannon = new Cannon();
  public final DriveTrain m_driveTrain = new DriveTrain();
  public final LEDLights m_lights = new LEDLights(RobotBase.isSimulation());

  /* Path follower */
//   private SendableChooser<Command> autoChooser;
  private Command autonomousCommand;
  
  // Joysticks
  private final XboxController xboxController = new XboxController(0);

  // Driver Controller
  public final JoystickButton a_Button_Driver =
      new JoystickButton(xboxController, XboxController.Button.kA.value);
  public final JoystickButton b_Button_Driver =
      new JoystickButton(xboxController, XboxController.Button.kB.value);
  public final JoystickButton x_Button_Driver =
      new JoystickButton(xboxController, XboxController.Button.kX.value);
  public final JoystickButton y_Button_Driver =
      new JoystickButton(xboxController, XboxController.Button.kY.value);
  public final JoystickButton left_Bumper_Driver =
      new JoystickButton(xboxController, XboxController.Button.kLeftBumper.value);
  public final JoystickButton right_Bumper_Driver =
      new JoystickButton(xboxController, XboxController.Button.kRightBumper.value);
  public final JoystickButton back_Button_Driver =
      new JoystickButton(xboxController, XboxController.Button.kBack.value);
  public final JoystickButton start_Button_Driver =
      new JoystickButton(xboxController, XboxController.Button.kStart.value);
  public final JoystickButton left_Stick_Driver =
      new JoystickButton(xboxController, XboxController.Button.kLeftStick.value);
  public final JoystickButton right_Stick_Driver =
      new JoystickButton(xboxController, XboxController.Button.kRightStick.value);
  public final Trigger left_Trigger_Driver = 
      new Trigger(() -> xboxController.getLeftTriggerAxis() >= 0.5);
  public final Trigger right_Trigger_Driver = 
      new Trigger(() -> xboxController.getRightTriggerAxis() >= 0.5);


  public static RobotContainer GetInstance() {
    return instance;
  }
    
      /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    instance = this;

    // disable all telemetry in the LiveWindow to reduce the processing during each iteration
    LiveWindow.disableAllTelemetry();

    m_driveTrain.setMaxOutput(Constants.kMaxSpeedFactor);
    configureAutoPaths();
    configureAutoCommands();
    configureTelemetry();

    // Configure the button bindings
    configureButtonBindings();
    configureDefaultCommands();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    a_Button_Driver
        .onTrue(Commands.sequence(
                    Commands.runOnce(() -> m_cannon.arm(true)),
                    m_lights.lightsArmedCommand()))
        .onFalse(Commands.sequence(
                    Commands.runOnce(() -> m_cannon.arm(false)),
                    m_lights.lightsOffCommand()));

    left_Bumper_Driver.onTrue(Commands.runOnce(() -> m_driveTrain.setDriveScaling(Math.max(m_driveTrain.getDriveScaling() - 0.1, 0.1))));
    right_Bumper_Driver.onTrue(Commands.runOnce(() -> m_driveTrain.setDriveScaling(Math.max(m_driveTrain.getDriveScaling() + 0.1, 1.0))));

    start_Button_Driver
        .onTrue(Commands.sequence(
            Commands.runOnce(() -> m_cannon.fire()),
            m_lights.lightsFireCommand()));
    left_Stick_Driver
        .onTrue(Commands.sequence(
            Commands.runOnce(() -> m_cannon.fire()),
            m_lights.lightsFireCommand()));
  }

  public XboxController getXboxController() {
    return xboxController;
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // The selected command will be run in autonomous
    // return m_chooser.getSelected();
    return null;
  }

  /** Use this method to define your commands for autonomous mode. */
  private void configureAutoCommands() {
    // Add commands to Autonomous Sendable Chooser
    // autoChooser = AutoBuilder.buildAutoChooser("Forward");
    // PathfindingCommand.warmupCommand().schedule();
  }
    
  private void configureDefaultCommands() {
    // Configure default commands
    m_driveTrain.setDefaultCommand(new DriveCommand(this));

    m_cannon.setDefaultCommand(Commands.run(() -> m_cannon.teleop(xboxController.getPOV()), m_cannon));
  }
    
  private void configureAutoPaths() {
    // NamedCommands.registerCommand("Intake", intake.intakeCommand());
  }

  private void configureTelemetry() {
    // m_driveTrain.registerTelemetry(logger::telemeterize);
  }

  public void robotPeriodic() {
  }

  public void simulationInit() {}

  public void simulationPeriodic() {
  }
 
  public void autonomousInit() {
    autonomousCommand = this.getAutonomousCommand();
    if (autonomousCommand != null) {
      autonomousCommand.schedule();
    }
  }

  public void autonomousPeriodic() {}

  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
  }

  public void teleopPeriodic() {}

  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  public void testPeriodic() {}

  public void testExit() {}

  public void disabledInit() {}

  public void disabledPeriodic() {}
}
