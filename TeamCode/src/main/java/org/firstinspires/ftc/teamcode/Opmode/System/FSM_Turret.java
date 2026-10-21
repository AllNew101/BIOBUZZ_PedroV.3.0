package org.firstinspires.ftc.teamcode.Opmode.System;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Calculate.Distance;
import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;

public class FSM_Turret {

    double Idle_target = Config_Tuning.FSM_Turret.Idle_target;

    public enum Current_State{Idle_state, Lock , L2,L1,M0,R1,R2}
    Servo turret1,turret2;
    ElapsedTime time;
    Current_State current_state;
    PID_Turret PID;
    Distance distance;

    public void init (HardwareMap hardwareMap, ElapsedTime elapsedTime){
        PID = new PID_Turret();
        time = elapsedTime;
        turret1 = hardwareMap.get(Servo.class, "Turret1");
        turret2 = hardwareMap.get(Servo.class,"Turret2");
        PID.init(time);
        current_state = Current_State.M0;

    }

    public void command(Current_State state){
        current_state = state;
    }

    public void update_state(){ // if use pid need to have target_theta
        switch (current_state){
//            case Idle_state:
//                break;
//            case Lock :
//                break;
            case L2:
                turret1.setPosition(0);
                turret2.setPosition(0);
                break;
            case L1:
                turret1.setPosition(0.25);
                turret2.setPosition(0.25);
                break;
            case M0:
                turret1.setPosition(0.5);
                turret2.setPosition(0.5);
                break;
            case R1:
                turret1.setPosition(0.75);
                turret2.setPosition(0.75);
                break;
            case R2:
                turret1.setPosition(1);
                turret2.setPosition(1);
                break;
        }

    }
}
