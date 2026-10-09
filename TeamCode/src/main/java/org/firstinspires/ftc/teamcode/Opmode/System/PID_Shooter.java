package org.firstinspires.ftc.teamcode.Opmode.System;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;

public class PID_Shooter {
    double kp = Config_Tuning.Shooter.kp;
    double ki = Config_Tuning.Shooter.ki ;
    double kd = Config_Tuning.Shooter.kd ;
    double kf = Config_Tuning.Shooter.kf ;// start power

    private double previous_error, error, delta_error, integral, previous_time, delta_time, power_motor;
    ElapsedTime time;

    public void init (ElapsedTime Time){
        time = Time;
        previous_time = time.seconds();
        previous_error = 0;
    }
    public double PIDF (double target_velo , double current_velo){
        delta_time = time.seconds() - previous_time;
        error = target_velo - current_velo;
        delta_error = error - previous_error;
        integral += error*delta_time;

        power_motor = (kp*error) + (ki*integral) + (kd*(delta_error/delta_time)) + (kf * target_velo);
        previous_time = time.seconds();
        previous_error = error;
        return Range.clip(power_motor,-1.0,1.0);
    }
}