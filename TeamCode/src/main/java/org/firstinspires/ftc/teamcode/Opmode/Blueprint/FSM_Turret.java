package org.firstinspires.ftc.teamcode.Opmode.Blueprint;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Config_Biobuzz;

@Config
public class FSM_Turret {

    double Idle_target = Config_Biobuzz.FSM_Turret.Idle_target;

    public enum Current_State{
        Idle_state,
        Lock
    }
    DcMotorEx motor1;
    ElapsedTime time;
    Current_State current_state;
    PID_Turret PID;
    Distance distance;

    public void init (HardwareMap hardwareMap, ElapsedTime elapsedTime){
        PID = new PID_Turret();
        time = elapsedTime;
        motor1 = hardwareMap.get(DcMotorEx.class, "Turret");
        PID.init(time);
        current_state = Current_State.Idle_state;

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor1.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void command(Current_State state){
        current_state = state;
    }

    public void update_state(double target_theta){
        switch (current_state){
            case Idle_state:
                motor1.setPower(PID.PIDF(Idle_target, motor1.getCurrentPosition()));
                break;
            case Lock :
                motor1.setPower(PID.PIDF(target_theta, motor1.getCurrentPosition()));
                break;

        }


    }
}
