package org.firstinspires.ftc.teamcode.Opmode.Blueprint;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import com.acmerobotics.dashboard.config.Config;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Config_Biobuzz
;

@Config
public class PID_posi {
    double kp = Config_Biobuzz.Posi.kp;
    double ki = Config_Biobuzz.Posi.ki ;
    double kd = Config_Biobuzz.Posi.kd ;
    double kf = Config_Biobuzz.Posi.kf ;

    private double previous_error, error, delta_error, integral, previous_time, delta_time, power_motor;
    private DcMotorEx motor1;
    private ElapsedTime time;

    public void init (HardwareMap hardwareMap){
        motor1 = hardwareMap.get(DcMotorEx.class, "Motor1");
        time = new ElapsedTime();
        time.startTime();
        previous_time = time.seconds();
        previous_error = 0;
    }
    public void PIDF (double target_posi){
        delta_time = time.seconds() - previous_time;
        error = target_posi - motor1.getCurrentPosition();
        delta_error = error - previous_error;
        integral += error*delta_time;

        power_motor = (kp*error) + (ki*integral) + (kd*(delta_error/delta_time)) + (kf * Math.signum(error));
        previous_time = time.seconds();
        previous_error = error;
        set_posi(power_motor);
    }
    public void set_posi (double power){
        motor1.setPower(power);
    }
}