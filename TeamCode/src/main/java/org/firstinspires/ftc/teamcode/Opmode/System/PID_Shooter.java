package org.firstinspires.ftc.teamcode.Opmode.System;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;

public class PID_Shooter {

    private double previous_error, error, delta_error, integral_pollen,integral_nectar, previous_time, delta_time, power_motor;
    ElapsedTime time;

    public void init (ElapsedTime Time){
        time = Time;
        previous_time = time.seconds();
        previous_error = 0;
    }
    public double PIDF_Pollen (double target_velo , double current_velo){
        double kp = Config_Tuning.Shooter_Pollen.kp;
        double ki = Config_Tuning.Shooter_Pollen.ki ;
        double kd = Config_Tuning.Shooter_Pollen.kd ;
        double kf = Config_Tuning.Shooter_Pollen.kf ;// start power
        ////////////////////////////////////////////////////
        delta_time = time.seconds() - previous_time;
        error = target_velo - current_velo;
        delta_error = error - previous_error;
        integral_pollen += error*delta_time;

        power_motor = (kp*error) + (ki*integral_pollen) + (kd*(delta_error/delta_time)) + (kf * target_velo);
        previous_time = time.seconds();
        previous_error = error;
        return power_motor;
    }
    public double PIDF_Nectar (double target_velo , double current_velo){
        double kp = Config_Tuning.Shooter_Nectar.kp;
        double ki = Config_Tuning.Shooter_Nectar.ki ;
        double kd = Config_Tuning.Shooter_Nectar.kd ;
        double kf = Config_Tuning.Shooter_Nectar.kf ;// start power
        ////////////////////////////////////////////////////
        delta_time = time.seconds() - previous_time;
        error = target_velo - current_velo;
        delta_error = error - previous_error;
        integral_nectar += error*delta_time;

        power_motor = (kp*error) + (ki*integral_nectar) + (kd*(delta_error/delta_time)) + (kf * target_velo);
        previous_time = time.seconds();
        previous_error = error;
        return power_motor;
    }
}