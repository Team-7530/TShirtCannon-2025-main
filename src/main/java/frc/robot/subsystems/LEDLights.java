// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.led.*;
import com.ctre.phoenix.led.CANdle.LEDStripType;
import com.ctre.phoenix.led.CANdle.VBatOutputMode;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import com.ctre.phoenix.led.TwinkleAnimation.TwinklePercent;
import com.ctre.phoenix.led.TwinkleOffAnimation.TwinkleOffPercent;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;

import java.util.Map;

public class LEDLights extends SubsystemBase {
  public static int LEDCount = 128;

  public static class LEDColor {
    public int red = 255;
    public int green = 255;
    public int blue = 255;
    public int white = 255;
    public int startIndex = 0;
    public int count = LEDCount;
    public double brightness = 0.5;
    public double speed = 0.5;
    public static LEDColor kOff = new LEDColor(0, 0, 0);
    public static LEDColor kWhite = new LEDColor(255, 255, 255);
    public static LEDColor kRed = new LEDColor(255, 0, 0);
    public static LEDColor kGreen = new LEDColor(0, 255, 0);
    public static LEDColor kBlue = new LEDColor(0, 0, 255);
    public static LEDColor kPurple = new LEDColor(255, 0, 255);
    public static LEDColor kOrange = new LEDColor(0, 255, 255);
    public static LEDColor kYellow = new LEDColor(255, 255, 0);

    public LEDColor(int r, int g, int b, int w, int start, int cnt) {
      red = r;
      green = g;
      blue = b;
      white = w;
      startIndex = start;
      count = cnt;
    }

    public LEDColor(int r, int g, int b) {
      this(r, g, b, 0, 0, LEDCount);
    }

    public LEDColor(int r, int g, int b, double br, double spd) {
      this(r, g, b);
      brightness = br;
      speed = spd;
    }

    public boolean equals(LEDColor color) {
      return (color.red == red) && (color.green == green) && (color.blue == blue);
    }

    public String rgbToHexcode() {
      int val = ((red & 0xFF) << 16) + ((green & 0xFF) << 8) + (blue & 0xFF);
      return String.format("#%06x", val);
    }
  }

  public final CANdle m_candle;

  public enum AnimationTypes {
    ColorFlow,
    Fire,
    Larson,
    Rainbow,
    RgbFade,
    SingleFade,
    Strobe,
    Twinkle,
    TwinkleOff,
    SetAll
  }

  private AnimationTypes m_currentAnimation = AnimationTypes.SetAll;
  private Animation m_toAnimate = null;

  private SimpleWidget colorWidget;
  private GenericEntry colorWidgetEntry;
  private LEDColor currentColor = LEDColor.kOff;

  public LEDLights(boolean sim) {
    m_candle = sim ? null : new CANdle(Constants.kID_CANdle);

    if (m_candle != null) {
      changeAnimation(AnimationTypes.SetAll, LEDColor.kWhite);

      CANdleConfiguration configAll = new CANdleConfiguration();
      configAll.statusLedOffWhenActive = true;
      configAll.disableWhenLOS = false;
      configAll.stripType = LEDStripType.GRB;
      configAll.brightnessScalar = 0.5;
      configAll.vBatOutputMode = VBatOutputMode.Modulated;
      m_candle.configAllSettings(configAll, 100);
    }

    colorWidget =
        Shuffleboard.getTab("SmartDashboard")
            .add("LEDColor", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(0, 4)
            .withProperties(Map.of("colorWhenFalse", LEDColor.kOff.rgbToHexcode()))
            .withProperties(Map.of("colorWhenTrue", LEDColor.kRed.rgbToHexcode()));
    colorWidgetEntry = colorWidget.getEntry();
  }

  public LEDLights() {
    this(Robot.isSimulation() || Robot.isReal());
  }

  public void incrementAnimation() {
    switch (m_currentAnimation) {
      case ColorFlow:
        changeAnimation(AnimationTypes.Fire, new LEDColor(0, 0, 0, 0.5, 0.7));
        break;
      case Fire:
        changeAnimation(AnimationTypes.Larson, new LEDColor(0, 255, 46, 0.5, 1.0));
        break;
      case Larson:
        changeAnimation(AnimationTypes.Rainbow, new LEDColor(0, 0, 0, 1.0, 0.1));
        break;
      case Rainbow:
        changeAnimation(AnimationTypes.RgbFade, new LEDColor(0, 0, 0, 0.7, 0.4));
        break;
      case RgbFade:
        changeAnimation(AnimationTypes.SingleFade, new LEDColor(50, 2, 200, 0.5, 0.5));
        break;
      case SingleFade:
        changeAnimation(AnimationTypes.Strobe, new LEDColor(240, 10, 180, 0.5, 98.0 / 256.0));
        break;
      case Strobe:
        changeAnimation(AnimationTypes.Twinkle, new LEDColor(30, 70, 60, 0.5, 0.4));
        break;
      case Twinkle:
        changeAnimation(AnimationTypes.TwinkleOff, new LEDColor(70, 90, 175, 0.5, 0.8));
        break;
      case TwinkleOff:
        changeAnimation(AnimationTypes.ColorFlow, new LEDColor(128, 20, 70, 0.0, 0.7));
        break;
      case SetAll:
        changeAnimation(AnimationTypes.ColorFlow, new LEDColor(128, 20, 70, 0.0, 0.7));
        break;
    }
  }

  public void decrementAnimation() {
    switch (m_currentAnimation) {
      case ColorFlow:
        changeAnimation(AnimationTypes.TwinkleOff, new LEDColor(70, 90, 175, 0.5, 0.8));
        break;
      case Fire:
        changeAnimation(AnimationTypes.ColorFlow, new LEDColor(128, 20, 70, 0.0, 0.7));
        break;
      case Larson:
        changeAnimation(AnimationTypes.Fire, new LEDColor(0, 0, 0, 0.5, 0.7));
        break;
      case Rainbow:
        changeAnimation(AnimationTypes.Larson, new LEDColor(0, 255, 46, 0.5, 1.0));
        break;
      case RgbFade:
        changeAnimation(AnimationTypes.Rainbow, new LEDColor(0, 0, 0, 1.0, 0.1));
        break;
      case SingleFade:
        changeAnimation(AnimationTypes.RgbFade, new LEDColor(0, 0, 0, 0.7, 0.4));
        break;
      case Strobe:
        changeAnimation(AnimationTypes.SingleFade, new LEDColor(50, 2, 200, 0.5, 0.5));
        break;
      case Twinkle:
        changeAnimation(AnimationTypes.Strobe, new LEDColor(240, 10, 180, 0.5, 98.0 / 256.0));
        break;
      case TwinkleOff:
        changeAnimation(AnimationTypes.Twinkle, new LEDColor(30, 70, 60, 0.5, 0.4));
        break;
      case SetAll:
        changeAnimation(AnimationTypes.ColorFlow, new LEDColor(128, 20, 70, 0.0, 0.7));
        break;
    }
  }

  public void setColors() {
    changeAnimation(AnimationTypes.SetAll, LEDColor.kWhite);
  }

  /* Wrappers so we can access the CANdle from the subsystem */
  public double getVbat() {
    return m_candle != null ? m_candle.getBusVoltage() : 0;
  }

  public double get5V() {
    return m_candle != null ? m_candle.get5VRailVoltage() : 0;
  }

  public double getCurrent() {
    return m_candle != null ? m_candle.getCurrent() : 0;
  }

  public double getTemperature() {
    return m_candle != null ? m_candle.getTemperature() : 0;
  }

  public void configBrightness(double percent) {
    if (m_candle != null) m_candle.configBrightnessScalar(percent, 0);
  }

  public void configLos(boolean disableWhenLos) {
    if (m_candle != null) m_candle.configLOSBehavior(disableWhenLos, 0);
  }

  public void configLedType(LEDStripType type) {
    if (m_candle != null) m_candle.configLEDType(type, 0);
  }

  public void configStatusLedBehavior(boolean offWhenActive) {
    if (m_candle != null) m_candle.configStatusLedState(offWhenActive, 0);
  }

  public void changeAnimation(AnimationTypes toChange, LEDColor color) {
    m_currentAnimation = toChange;

    switch (toChange) {
      case ColorFlow:
        m_toAnimate =
            new ColorFlowAnimation(
                color.red,
                color.green,
                color.blue,
                color.white,
                color.speed,
                color.count,
                Direction.Forward);
        break;
      case Fire:
        m_toAnimate = new FireAnimation(color.brightness, color.speed, color.count, 0.7, 0.5);
        break;
      case Larson:
        m_toAnimate =
            new LarsonAnimation(
                color.red,
                color.green,
                color.blue,
                color.white,
                color.speed,
                color.count,
                BounceMode.Back,
                6);
        break;
      case Rainbow:
        m_toAnimate = new RainbowAnimation(color.brightness, color.speed, color.count);
        break;
      case RgbFade:
        m_toAnimate = new RgbFadeAnimation(color.brightness, color.speed, color.count);
        break;
      case SingleFade:
        m_toAnimate =
            new SingleFadeAnimation(
                color.red, color.green, color.blue, color.white, color.speed, color.count);
        break;
      case Strobe:
        m_toAnimate =
            new StrobeAnimation(
                color.red, color.green, color.blue, color.white, color.speed, color.count);
        break;
      case Twinkle:
        m_toAnimate =
            new TwinkleAnimation(
                color.red,
                color.green,
                color.blue,
                color.white,
                color.speed,
                color.count,
                TwinklePercent.Percent6);
        break;
      case TwinkleOff:
        m_toAnimate =
            new TwinkleOffAnimation(
                color.red,
                color.green,
                color.blue,
                color.white,
                color.speed,
                color.count,
                TwinkleOffPercent.Percent100);
        break;
      case SetAll:
        m_toAnimate = null;
        break;
    }
    Shuffleboard.addEventMarker(
        "LEDAnimation changed", m_currentAnimation.toString(), EventImportance.kHigh);
    // System.out.println("Changed to " + m_currentAnimation.toString());
  }

  @Override
  public void periodic() {
    // Put code here to be run every loop
    if (isAnimating()) {
      if (m_candle != null) m_candle.animate(m_toAnimate);
    }
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  public void logPeriodic() {
    // Choose "true" color based on color of wheel required for Position Control
    if (!currentColor.equals(LEDColor.kOff)) {
      colorWidget.withProperties(Map.of("colorWhenTrue", currentColor.rgbToHexcode()));
      colorWidgetEntry.setBoolean(true);
    } else colorWidgetEntry.setBoolean(false);

    SmartDashboard.putString("LEDAnimation", m_currentAnimation.toString());
  }

  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public void runLights(int red, int green, int blue, int white, int start, int count) {
    if (m_candle != null) m_candle.setLEDs(red, green, blue, white, start, count);
    currentColor = new LEDColor(red, green, blue);
  }

  public void runLights(int red, int green, int blue) {
    if (m_candle != null) m_candle.setLEDs(red, green, blue);
    currentColor = new LEDColor(red, green, blue);
  }

  public void runLights(LEDColor color) {
    if (m_candle != null)
      m_candle.setLEDs(
          color.red, color.green, color.blue, color.white, color.startIndex, color.count);
    currentColor = color;
  }

  public void setOnboardLights(LEDColor color) {
    if (m_candle != null) {
      int start = Math.min(color.startIndex, 7);
      int count = Math.min(8 - start, color.count);
      m_candle.setLEDs(color.red, color.green, color.blue, color.white, start, count);
    }
    currentColor = color;
  }

  public void setStripLights(LEDColor color) {
    if (m_candle != null) {
      int start = Math.max(8 + color.startIndex, 8);
      int count = Math.min(LEDCount - 8, color.count - 8);
      m_candle.setLEDs(color.red, color.green, color.blue, color.white, start, count);
    }
    currentColor = color;
  }

  public boolean isAnimating() {
    return m_toAnimate != null;
  }

  public Command lightsOffCommand() {
    return runOnce(() -> { 
      runLights(LEDColor.kOff); 
      changeAnimation(AnimationTypes.RgbFade, new LEDColor(0, 0, 0, 1.0, 0.1)); });
  }
  public Command lightsArmedCommand() {
    return runOnce(() -> { 
      runLights(LEDColor.kRed); 
      changeAnimation(AnimationTypes.ColorFlow, new LEDColor(128, 20, 70, 0.0, 0.7)); });
  }
  public Command lightsFireCommand() {
    return runOnce(() -> { 
      runLights(LEDColor.kWhite); 
      changeAnimation(AnimationTypes.Strobe, new LEDColor(255, 0, 0, 1.0, 0.5)); });
  }
}
