package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;

/** */
public class DisableDriveScaling extends InstantCommand {

  private final DriveTrain m_driveTrain;

  public DisableDriveScaling(RobotContainer container) {
    m_driveTrain = container.m_driveTrain;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_driveTrain.setUseDriveScaling(false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}
}
