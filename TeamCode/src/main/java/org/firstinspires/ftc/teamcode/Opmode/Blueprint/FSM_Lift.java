package org.firstinspires.ftc.teamcode.Opmode.Blueprint;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class FSM_Lift {
    public enum Current_State{
        Idle_state,
        Up_max,
        Down_max
    }

    ElapsedTime time;
    DcMotorEx motor1;
    DcMotorEx motor2;
    PID_Lift PID;
    public Current_State current_state = Current_State.Idle_state ;
    public double current_posi, power;
    public double up_max = 2000;
    public double down_max = 10;
    public double idle = 0.1;


    public void init (HardwareMap hardwareMap , ElapsedTime elapsedTime){
//        motor1 = hardwareMap.get(DcMotorEx.class, "Motor1");
//        motor2 = hardwareMap.get(DcMotorEx.class, "Motor2");
        time = elapsedTime;

        PID = new PID_Lift();
        PID.init(time);
    }

    public void Lift_command (Current_State Lift_state){
        current_state = Lift_state;
        //
    }

    public void update_state (){
        current_posi = (double) (motor1.getCurrentPosition() + motor2.getCurrentPosition())/2;
        switch (current_state){
            case Idle_state:
                motor1.setPower(idle);
                motor2.setPower(idle);
                break;
            case Up_max:
                power = PID.PIDF(up_max,current_posi);
                motor1.setPower(power);
                motor2.setPower(power);
                break;
            case Down_max:
                power = PID.PIDF(down_max,current_posi);
                motor1.setPower(power);
                motor2.setPower(power);
                break;


        }
    }
}
