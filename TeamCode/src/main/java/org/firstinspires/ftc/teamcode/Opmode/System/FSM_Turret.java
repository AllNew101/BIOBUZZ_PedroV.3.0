package org.firstinspires.ftc.teamcode.Opmode.System;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
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

    public enum Current_State{Idle_state, Lock_red_human, Lock_red_garden, Lock_blue_human, Lock_blue_garden, L2,L1,M0,R1,R2}
    Servo turret1,turret2;
    ElapsedTime time;
    Current_State current_state;
    PID_Turret PID;
    Distance distance;
    double offset_manual = 0.5;

    public void init (HardwareMap hardwareMap, ElapsedTime elapsedTime){
        PID = new PID_Turret();
        distance = new Distance();

        time = elapsedTime;
        turret1 = hardwareMap.get(Servo.class, "Turret1");
        turret2 = hardwareMap.get(Servo.class,"Turret2");
        PID.init(time);
        distance.init(time);
        turret1.setDirection(Servo.Direction.REVERSE);
        turret2.setDirection(Servo.Direction.REVERSE);
        current_state = Current_State.M0;
    }

    public void command(Current_State state){
        current_state = state;
    }
    public void check_goal (){}

    public void update_state(Follower follower,double gamepad,double offset_garden,double offset_human){// if use pid need to have target_theta
        switch (current_state){
//            case Idle_state:
//                break;
            case Lock_red_garden:
                turret1.setPosition(distance.Pythagoras(follower.pose().x(),follower.pose().y(),follower.pose().heading(),1,true,offset_garden,offset_human)[2]);
                turret2.setPosition(distance.Pythagoras(follower.pose().x(),follower.pose().y(),follower.pose().heading(),1,true,offset_garden,offset_human)[2]);
                break;
            case Lock_red_human:
                turret1.setPosition(distance.Pythagoras(follower.pose().x(),follower.pose().y(),follower.pose().heading(),2,true,offset_garden,offset_human)[2]);
                turret2.setPosition(distance.Pythagoras(follower.pose().x(),follower.pose().y(),follower.pose().heading(),2,true,offset_garden,offset_human)[2]);
                break;
            case L2:
                turret1.setPosition(0);
                turret2.setPosition(0);
                break;
            case L1:
                turret1.setPosition(0.25);
                turret2.setPosition(0.25);
                break;
            case M0:
                offset_manual += -gamepad * 0.02;
                turret1.setPosition(offset_manual);
                turret2.setPosition(offset_manual);
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
