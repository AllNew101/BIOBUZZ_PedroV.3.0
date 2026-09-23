package org.firstinspires.ftc.teamcode.Opmode.Blueprint;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;


public class FSM_velo {
    public enum Current_State{Fast, Mid,Stop}

    PID_velo PID;
    Current_State current_state;
    DcMotorEx motor1;
    ElapsedTime time;

    double fast = Config_Tuning.FSM_Velo.fast;
    double mid = Config_Tuning.FSM_Velo.mid;
    double stop = Config_Tuning.FSM_Velo.stop;

    public void init (HardwareMap hardwareMap, ElapsedTime elapsedTime){
        motor1 = hardwareMap.get(DcMotorEx.class, "motor1");
        time = elapsedTime;
        PID = new PID_velo();
        PID.init(hardwareMap,time);
    }
    public void command(Current_State change_state){
        current_state = change_state;
    }

    public void update_state (){
        switch (current_state){
            case Fast:
                motor1.setPower(PID.PIDF(fast));
                break;
            case Mid:
                motor1.setPower(PID.PIDF(mid));
                break;
            case Stop:
                motor1.setPower(PID.PIDF(stop));
                break;
        }
    }
}
