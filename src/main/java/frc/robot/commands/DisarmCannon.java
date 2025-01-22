package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Cannon;

/** */
public class DisarmCannon extends InstantCommand {

  private final Cannon m_cannon;

  public DisarmCannon(RobotContainer container) {
    m_cannon = container.m_cannon;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_cannon.arm(false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}
}
