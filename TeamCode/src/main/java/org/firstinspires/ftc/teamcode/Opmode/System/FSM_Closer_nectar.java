package org.firstinspires.ftc.teamcode.Opmode.System;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;

public class FSM_Closer_nectar {

    public enum Current_State{Close,Open}
    Servo closer_nectar;
    Current_State current_state;

    public void init (HardwareMap hardwareMap){
        closer_nectar = hardwareMap.get(Servo.class,"Closer_nectar");
    }

    public void command(Current_State state){
        current_state = state;
    }
    public Current_State get_state(){return current_state;}

    public void update_state(){
        double close_nectar = Config_Tuning.FSM_Closer.close_nectar;
        double open_nectar = Config_Tuning.FSM_Closer.open_nectar;
        switch (current_state){
            case Close:
                closer_nectar.setPosition(close_nectar);
                break;
            case Open:
                closer_nectar.setPosition(open_nectar);
                break;
        }

    }
}
