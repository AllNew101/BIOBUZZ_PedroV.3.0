package org.firstinspires.ftc.teamcode.Opmode.System;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;

public class FSM_Shooter {
    public enum Current_State{Far,Mid,Stop}

    PID_Shooter PID;
    Current_State current_state;
    DcMotorEx shooter1;
    DcMotorEx shooter2;
    ElapsedTime time;

    double stop = 0; // target3
    double power_pid; // power_shooter
    double target_velo;

    public void init (HardwareMap hardwareMap, ElapsedTime elapsedTime){
        shooter1 = hardwareMap.get(DcMotorEx.class, "Shooter1");
        shooter2 = hardwareMap.get(DcMotorEx.class,"Shooter2");
        shooter1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter2.setDirection(DcMotorSimple.Direction.REVERSE);

        time = elapsedTime;
        PID = new PID_Shooter();
        PID.init(time);
    }

    public double[] get_info_shooter(){double[] info = {shooter1.getVelocity(),target_velo}; return info;}
    public Current_State get_state(){return current_state;}

    public void command(Current_State change_state){
        current_state = change_state;
    }

    public void update_state (){
        double test_power = Config_Tuning.FSM_Shooter.power;
        double far = Config_Tuning.FSM_Shooter.far; // target1
        double mid = Config_Tuning.FSM_Shooter.mid; //target2
        switch (current_state){
            case Far:
                target_velo = far;
//                power_pid = PID.PIDF(far , shooter1.getVelocity());
//                shooter1.setPower(power_pid);
//                shooter2.setPower(power_pid);
                break;
            case Mid:
                target_velo = mid;
//                shooter1.setPower(test_power);
//                shooter2.setPower(test_power);
                power_pid = PID.PIDF(mid , shooter1.getVelocity());
                shooter1.setPower(power_pid);
                shooter2.setPower(power_pid);
                break;
            case Stop:
                target_velo = stop;
                shooter1.setPower(stop);
                shooter2.setPower(stop);
                break;
        }
    }
}
