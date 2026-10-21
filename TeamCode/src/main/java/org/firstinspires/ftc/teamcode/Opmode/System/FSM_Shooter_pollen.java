package org.firstinspires.ftc.teamcode.Opmode.System;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;

public class FSM_Shooter_pollen {
    public enum Current_State{Far,Mid,Stop}

    PID_Shooter PID;
    Current_State current_state;
    DcMotorEx shooter2;
    ElapsedTime time;

    double stop = 0; // target3
    double power_pid_pollen , power_pid_nectar; // power_shooter
    double target_velo_nectar , target_velo_pollen;

    public void init (HardwareMap hardwareMap, ElapsedTime elapsedTime){
        shooter2 = hardwareMap.get(DcMotorEx.class,"Shooter2"); // pollen
        shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        time = elapsedTime;
        PID = new PID_Shooter();
        PID.init(time);
    }

    public double[] get_info_shooter(){double[] info = {shooter2.getVelocity(),target_velo_pollen}; return info;}
    public Current_State get_state(){return current_state;}

    public void command(Current_State change_state){
        current_state = change_state;
    }

    public void update_state (){
        double test_power = Config_Tuning.FSM_Shooter.power;
        double mid_pollen = Config_Tuning.FSM_Shooter.mid_pollen; //target1
        double mid_nectar = Config_Tuning.FSM_Shooter.mid_nectar; // target2
        switch (current_state){
            case Far:
//                target_velo = far;
//                power_pid = PID.PIDF(far , shooter1.getVelocity());
//                shooter1.setPower(power_pid);
//                shooter2.setPower(power_pid);
                break;
            case Mid:
                target_velo_pollen = mid_pollen;
                //// POLLEN ////
                power_pid_pollen = PID.PIDF_Pollen(mid_pollen , shooter2.getVelocity());
                shooter2.setPower(power_pid_pollen);

                break;
            case Stop:
                target_velo_pollen = stop;
                shooter2.setPower(stop);
                break;
        }
    }
}
