package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Cannon;

public class ControlCannon extends Command {

  private final RobotContainer m_container;
  private final Cannon m_cannon;
  private final XboxController m_controller;
  private boolean m_isFired = false;

  public ControlCannon(RobotContainer container) {

    m_container = container;
    m_cannon = container.m_cannon;
    m_controller = container.getXboxController();
    addRequirements(m_cannon);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("ControlCannon-init");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    int pov = m_controller.getPOV();
    if (pov == 0) {
      m_cannon.raiseCannon(-0.4);
      System.out.println("raiseCannon");
    } else if (pov == 90) {
      m_cannon.rotateCannon(-0.2);
      System.out.println("rotateCannon right");
    } else if (pov == 180) {
      m_cannon.raiseCannon(0.4);
      System.out.println("lowerCannon");
    } else if (pov == 270) {
      m_cannon.rotateCannon(0.2);
      System.out.println("rotateCannon left");
    } else {
      m_cannon.raiseCannon(0);
      m_cannon.rotateCannon(0);
    }
    if (m_controller.getLeftTriggerAxis() > 0.5) {
      if (!m_isFired) {
        m_isFired = true;
        CommandScheduler.getInstance()
            .schedule(
                new SequentialCommandGroup(
                    new ShootCannon(m_container), new ActivateLights(m_container)));
        System.out.println("fireCannon");
      }
    } else {
      m_isFired = false;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

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
