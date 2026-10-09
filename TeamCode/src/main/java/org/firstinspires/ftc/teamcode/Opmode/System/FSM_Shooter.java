package org.firstinspires.ftc.teamcode.Opmode.System;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;


public class FSM_Shooter {
    public enum Current_State{Far,Mid,Stop}

    PID_Shooter PID;
    Current_State current_state;
    DcMotorEx shooter1;
    DcMotorEx shooter2;
    ElapsedTime time;

    double far = Config_Tuning.FSM_Shooter.far; // target1
    double mid = Config_Tuning.FSM_Shooter.mid; // target2
    double power; // power_shooter

    public void init (HardwareMap hardwareMap, ElapsedTime Time){
        shooter1 = hardwareMap.get(DcMotorEx.class, "Shooter1");
        shooter2 = hardwareMap.get(DcMotorEx.class,"Shooter2");

        shooter1.setDirection(DcMotorSimple.Direction.REVERSE);

        time = Time;
        PID = new PID_Shooter();
        PID.init(time);
        current_state = Current_State.Stop;
    }
    public void command(Current_State change_state){
        current_state = change_state;
    }

    public void update_state (){
        switch (current_state){
            case Far:
                power = PID.PIDF(far , shooter1.getVelocity());
                shooter1.setPower(power);
                shooter2.setPower(power);
                break;
            case Mid:
               power = PID.PIDF(mid , shooter1.getVelocity());
                shooter1.setPower(power);
                shooter2.setPower(power);
                break;
            case Stop:
                shooter1.setPower(0);
                shooter2.setPower(0);
                break;
        }
    }
}
