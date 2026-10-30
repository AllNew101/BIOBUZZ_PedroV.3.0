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

public class FSM_Intake {
    public enum Current_State{In,Out,shoot,Boost,Stop}

    Current_State current_state;
    DcMotorEx intake1;
    DcMotorEx intake2;

    double stop = 0;


    public void init (HardwareMap hardwareMap){
        intake1 = hardwareMap.get(DcMotorEx.class, "intake1");
        intake2 = hardwareMap.get(DcMotorEx.class,"intake2");
    }

    public Current_State get_state(){return current_state;}

    public void command(Current_State change_state){
        current_state = change_state;
    }

    public void update_state (){
        double power_intake_in = Config_Tuning.Intake.Intake_In;
        double power_intake_out = Config_Tuning.Intake.Intake_Out;
        double power_intake_shoot = Config_Tuning.Intake.Intake_Shoot;
        double power_intake_boost = Config_Tuning.Intake.Intake_Boost;
        switch (current_state){
            case In:
                intake1.setPower(power_intake_in);
                intake2.setPower(power_intake_in);
                break;
            case shoot:
                intake1.setPower(power_intake_shoot);
                intake2.setPower(power_intake_shoot);
                break;
            case Out:
                intake1.setPower(power_intake_out);
                intake2.setPower(power_intake_out);
                break;
            case Boost:
                intake1.setPower(power_intake_boost);
                intake2.setPower(power_intake_boost);
                break;
            case Stop:
                intake1.setPower(stop);
                intake2.setPower(stop);
                break;
        }
    }
}
