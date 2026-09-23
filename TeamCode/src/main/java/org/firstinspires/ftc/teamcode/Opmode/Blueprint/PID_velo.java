package org.firstinspires.ftc.teamcode.Opmode.Blueprint;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;

public class PID_velo {
    double kp = Config_Tuning.Velo.kp;
    double ki = Config_Tuning.Velo.ki ;
    double kd = Config_Tuning.Velo.kd ;
    double kf = Config_Tuning.Velo.kf ;// start power

    private double previous_error, error, delta_error, integral, previous_time, delta_time, power_motor;
    private DcMotorEx motor1;
    private ElapsedTime time;

    public void init (HardwareMap hardwareMap , ElapsedTime Time){
        motor1 = hardwareMap.get(DcMotorEx.class, "Motor1");
        time = Time;
        time.startTime();
        previous_time = time.seconds();
        previous_error = 0;
    }
    public double PIDF (double target_velo){
        delta_time = time.seconds() - previous_time;
        error = target_velo - motor1.getVelocity();
        delta_error = error - previous_error;
        integral += error*delta_time;

        power_motor = (kp*error) + (ki*integral) + (kd*(delta_error/delta_time)) + (kf * Math.signum(error));
        previous_time = time.seconds();
        previous_error = error;
        return power_motor;
    }
}