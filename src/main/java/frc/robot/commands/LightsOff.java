package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.LEDLights;
import frc.robot.subsystems.LEDLights.AnimationTypes;
import frc.robot.subsystems.LEDLights.LEDColor;

/** */
public class LightsOff extends InstantCommand {

  private final LEDLights m_lights;

  public LightsOff(RobotContainer container) {
    m_lights = container.m_lights;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_lights.setOnboardLights(LEDColor.kOff);
    m_lights.changeAnimation(AnimationTypes.RgbFade, new LEDColor(0, 0, 0, 1.0, 0.1));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}
}
