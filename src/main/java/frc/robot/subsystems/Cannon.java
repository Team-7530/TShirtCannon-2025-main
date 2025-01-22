package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.spark.*;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Compressor;
// import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
// import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/** */
public class Cannon extends SubsystemBase {
  private WPI_VictorSPX cannonElevator;
  private SparkMax magazineRotator;
  // private DigitalInput leftAlignment;
  // private DigitalInput rightAlignment;
  private Compressor firingMechanism;

  private Boolean _armed = false;

  /** */
  public Cannon() {

    cannonElevator = new WPI_VictorSPX(Constants.kID_CannonElevator);
    magazineRotator = new SparkMax(Constants.kID_CannonRotator, MotorType.kBrushless);
    // magazineRotator = new WPI_TalonFX(Constants.kID_CannonRotator);

    // leftAlignment = new DigitalInput(0);
    // addChild("LeftAlignment", leftAlignment);

    // rightAlignment = new DigitalInput(1);
    // addChild("RightAlignment", rightAlignment);

    firingMechanism = new Compressor(0, PneumaticsModuleType.CTREPCM);
    addChild("FiringMechanism", firingMechanism);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // if (!_isNowRotating && (_rotateLeft || _rotateRight)) {
    //   _isNowRotating = !leftAlignment.get() && !rightAlignment.get();
    // }
    // if (_isNowRotating) {
    //   if (_rotateLeft && leftAlignment.get()) {
    //     magazineRotator.set(0.05);
    //   }
    //   if (_rotateRight && rightAlignment.get()) {
    //     magazineRotator.set(-0.05);
    //   }
    //   if (leftAlignment.get() && rightAlignment.get()) {
    //     magazineRotator.stopMotor();
    //     _isNowRotating = _rotateLeft = _rotateRight = false;
    //     if (_armed) {
    //       lightsBlink();
    //     } else {
    //       lightsOn();
    //     }
    //   } else {
    //     lightsOff();
    //   }
    // }
    // if (_blinkLights >= 0) {
    //   _blinkLights += 20;

    //   lights.set(_blinkLights >= 500);

    //   if (_blinkLights >= 1000) {
    //     _blinkLights = _blinkLights % 1000;
    //   }
    // }
    if (!_armed) {
      firingMechanism.disable();
    }
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run when in simulation

  }

  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public void rotateCannon(double speed) {
    magazineRotator.set(speed);
  }

  public void raiseCannon(double speed) {
    cannonElevator.set(speed);
  }

  public void arm(boolean armed) {
    _armed = armed;
  }

  public void fire() {
    if (_armed && !isMoving()) {
      firingMechanism.enableDigital();
      _armed = false;
    }
  }

  public Boolean isMoving() {
    return false;//(magazineRotator.getStatorCurrent() > 0.001) || (cannonElevator.get() > 0.001);
  } 
}
