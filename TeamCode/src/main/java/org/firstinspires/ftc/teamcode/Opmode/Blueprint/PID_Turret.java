package org.firstinspires.ftc.teamcode.Opmode.Blueprint;

import com.acmerobotics.dashboard.config.Config;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Config_Biobuzz;

@Config
public class PID_Turret {

    double Max_angle = Config_Biobuzz.Turret.MaxAngle;
    double Min_angle = Config_Biobuzz.Turret.MinAngle;
    double kd = Config_Biobuzz.Turret.kd;
    double kd_sec = Config_Biobuzz.Turret.kdSec;
    double kf = Config_Biobuzz.Turret.kf;
    double kf_sec = Config_Biobuzz.Turret.kfSec;
    double ki = Config_Biobuzz.Turret.ki;
    double ki_sec = Config_Biobuzz.Turret.kiSec;
    double kp = Config_Biobuzz.Turret.kp;
    double kp_sec = Config_Biobuzz.Turret.kpSec;

    private double previous_error, error, delta_error, integral, previous_time, delta_time, power_motor;
    private boolean Check_limit;
    private double Per_round = 537.7;
    private double gear_motor = 39;
    private double gear_turret = 89;
    private ElapsedTime time;

    public void init (ElapsedTime elapsedTime){
        time = elapsedTime;
        previous_time = time.seconds();
        previous_error = 0;
        Check_limit = false;
    }

    public double convert_current_to_degree(double motor_posi){
        return (motor_posi / Per_round) * 360 * gear_motor / gear_turret;
    }

    public double PIDF (double target_posi , double current_posi){
        delta_time = time.seconds() - previous_time;
        if (convert_current_to_degree(current_posi) > Max_angle){error = Max_angle - convert_current_to_degree(current_posi);}
        else if (convert_current_to_degree(current_posi) < Min_angle){error = Min_angle - convert_current_to_degree(current_posi);}
        else {error = target_posi - convert_current_to_degree(current_posi);}
        delta_error = error - previous_error;
        integral += error*delta_time;

        if (Math.abs(error) > 2) {
            if (Math.abs(error) > 8) {
                power_motor = (kp * error) + (ki * integral) + (kd * (delta_error / delta_time)) + (kf * Math.signum(error));
            } else {
                power_motor = (kp_sec * error) + (ki_sec * integral) + (kd_sec * (delta_error / delta_time)) + (kf_sec * Math.signum(error));
            }
        }

        previous_time = time.seconds();
        previous_error = error;
        return power_motor;
    }
}