package frc.robot.Intake;



import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.LinearSystem;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Intake {

    SingleJointedArmSim arm;
    FlywheelSim flywheel;

    private PIDController pController;
    private BangBangController bbController;

    LinearSystem<N1, N1, N1> system = LinearSystemId.createFlywheelSystem(DCMotor.getKrakenX60(1), 0.127, 2);

    public Intake () {

        pController = new PIDController(1, 0, 0.1);
        bbController = new BangBangController(0.5);


        arm = new SingleJointedArmSim(
            LinearSystemId.createDCMotorSystem(
                DCMotor.getKrakenX60(1), 0.001, 2),
                DCMotor.getKrakenX60(1),
                2, 1, 0, 10000, true, 0
        
        );
        flywheel = new FlywheelSim(system, DCMotor.getKrakenX60(1), 1);

    }



    public void setSpeedAndPosition(double position, double speed) {

        SmartDashboard.putNumber("set intake position", position);
        SmartDashboard.putNumber("set intake speed", speed);

        SmartDashboard.putNumber("current intake position", arm.getAngleRads());
        SmartDashboard.putNumber("current intake speed", flywheel.getAngularVelocityRPM()/60);

        arm.setInputVoltage(pController.calculate(
            arm.getAngleRads(), position) * 1000
        );


        flywheel.setInputVoltage(
            bbController.calculate(
                flywheel.getAngularVelocityRPM() / 60, speed
            ) * 12
        );

        arm.update(0.02);
        flywheel.update(0.02);


    }

    
}
