package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LEDLights;
import frc.robot.subsystems.LEDLights.AnimationTypes;
import frc.robot.subsystems.LEDLights.LEDColor;

/** */
public class LightsArmed extends InstantCommand {

  private final LEDLights m_lights;

  public LightsArmed(RobotContainer container) {
    m_lights = container.m_lights;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_lights.setOnboardLights(LEDColor.kRed);
    m_lights.changeAnimation(AnimationTypes.ColorFlow, new LEDColor(128, 20, 70, 0.0, 0.7));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}
}
