package org.firstinspires.ftc.teamcode.Opmode.System;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;

public class FSM_Closer_pollen {

    public enum Current_State{Close,Open}
    Servo closer_pollen;
    Current_State current_state;

    public void init (HardwareMap hardwareMap){
        closer_pollen = hardwareMap.get(Servo.class, "Closer_pollen");
    }

    public void command(Current_State state){
        current_state = state;
    }
    public Current_State get_state(){return current_state;}

    public void update_state(){
        double close_pollen = Config_Tuning.FSM_Closer.close_pollen;
        double open_pollen = Config_Tuning.FSM_Closer.open_pollen;
        switch (current_state){
            case Close:
                closer_pollen.setPosition(close_pollen);
                break;
            case Open:
                closer_pollen.setPosition(open_pollen);
                break;
        }

    }
}
