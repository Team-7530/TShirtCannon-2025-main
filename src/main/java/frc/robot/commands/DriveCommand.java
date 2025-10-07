package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.DriveTrain.DriveMode;

/** */
public class DriveCommand extends Command {

  private final DriveTrain m_driveTrain;
  private final XboxController m_controller;

  public DriveCommand(RobotContainer container) {

    m_driveTrain = container.m_driveTrain;
    m_controller = container.getXboxController();
    addRequirements(m_driveTrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_driveTrain.setDriveMode(DriveMode.ARCADE);
    m_driveTrain.setUseSquares(true);
    m_driveTrain.setUseDriveScaling(true);
    m_driveTrain.setDriveScaling(0.1);
    m_driveTrain.enableBrakes(false);
    m_driveTrain.enableDriveTrain(true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_driveTrain.drive(m_controller);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveTrain.tankDriveVolts(0, 0);
    m_driveTrain.setUseSquares(true);
    m_driveTrain.enableBrakes(true);
    m_driveTrain.setUseDriveScaling(false);
    m_driveTrain.setDriveScaling(1.0);
    m_driveTrain.setDriveMode(DriveMode.ARCADE);
    m_driveTrain.enableDriveTrain(false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public boolean runsWhenDisabled() {
    return false;
  }
}
