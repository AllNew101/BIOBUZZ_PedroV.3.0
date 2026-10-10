package org.firstinspires.ftc.teamcode.Opmode.Teleop;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Shooter;

import org.firstinspires.ftc.teamcode.Opmode.System.TelemetryX;

@Config
@TeleOp
public class Drive extends OpMode {

    Drawing drawing;
    TelemetryX telemetryX;
    TelemetryPacket packet;
    DcMotorEx motorRR;
    DcMotorEx motorRL;
    DcMotorEx motorFR;
    DcMotorEx motorFL;
    DcMotorEx intake1;
    DcMotorEx intake2;
    FSM_Shooter FSM_shooter;
    Servo closer;
    Servo slide;
    ElapsedTime time;

    public static int debug_key = 0;

    double power_intake_in = Config_Tuning.Intake.Intake_In;
    double power_intake_out = Config_Tuning.Intake.Intake_Out;
    double RR;
    double RL;
    double FR;
    double FL;
    boolean check_cross = true;
    boolean check_intake_in = false;
    boolean check_circle = true;
    boolean check_intake_out = false;
    boolean check_triggerR = true;
    boolean check_closer = true;
    boolean check_bumperR = true;
    boolean check_shoot = false;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void init() {
        time = new ElapsedTime();
        packet = new TelemetryPacket();
        telemetryX = new TelemetryX();
        FSM_shooter = new FSM_Shooter();

        motorRR = hardwareMap.get(DcMotorEx.class, "Back_R");
        motorRL = hardwareMap.get(DcMotorEx.class, "Back_L");
        motorFR = hardwareMap.get(DcMotorEx.class, "Front_R");
        motorFL = hardwareMap.get(DcMotorEx.class, "Front_L");
        intake1 = hardwareMap.get(DcMotorEx.class, "intake1");
        intake2 = hardwareMap.get(DcMotorEx.class,"intake2");
        slide = hardwareMap.get(Servo.class,"Slide");
        closer = hardwareMap.get(Servo.class, "KAN");

        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorRR.setDirection(DcMotorSimple.Direction.REVERSE);
        intake2.setDirection(DcMotorSimple.Direction.REVERSE);

        time.reset();
        time.startTime();
        FSM_shooter.init(hardwareMap,time);
        telemetryX.init(telemetry);
        telemetryX.clear();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void start() {
        FSM_shooter.command(FSM_Shooter.Current_State.Stop);
    }
    @Override
    public void loop() {
        FL = gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
        FR = gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x;
        RL = gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;
        RR = gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x;
        motorFL.setPower(FL);
        motorFR.setPower(FR);
        motorRL.setPower(RL);
        motorRR.setPower(RR);

        // INTAKE
        // IN
        if (gamepad1.crossWasPressed() && check_cross) {
            check_intake_in = !check_intake_in;
            check_cross = false;
        } else if (!gamepad1.crossWasPressed()) {
            check_cross = true;}
        if (check_intake_in) {
            intake1.setPower(power_intake_in); intake2.setPower(power_intake_in);}
        // OUT
        if (gamepad1.circleWasPressed() && check_circle) {
            check_intake_out = !check_intake_out;
            check_circle = false;
        } else if (!gamepad1.circleWasPressed()) {
            check_circle = true;}
        if (check_intake_out) {
            intake1.setPower(power_intake_out); intake2.setPower(power_intake_out);}
        // STOP
        if (!check_intake_in && !check_intake_out){
            intake1.setPower(0); intake2.setPower(0);}

        // CLOSER
        if (gamepad1.right_trigger > 0 && check_triggerR) {
            check_closer = !check_closer;
            check_triggerR = false;
        } else if (gamepad1.right_trigger <= 0) {
            check_triggerR = true;}
        if (check_closer){
            closer.setPosition(0.93);
        }else {
            closer.setPosition(0.8);}

        // PID_SHOOTER
        if (gamepad1.rightBumperWasPressed() && check_bumperR){
            check_shoot = !check_shoot;
            check_bumperR = false;
        } else if (!gamepad1.rightBumperWasPressed()) {
            check_bumperR = true;}
        if (check_shoot){
            FSM_shooter.command(FSM_Shooter.Current_State.Mid);}
        else {
            FSM_shooter.command(FSM_Shooter.Current_State.Stop);
        }

        debugging();
        FSM_shooter.update_state();
        telemetryX.update();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
    public void debugging (){
        switch (debug_key){
            case 0 :
                telemetryX.addData("PID_Shooter",true, TelemetryX.Target.BOTH);
                telemetryX.addData("Kp",Config_Tuning.Shooter.kp, TelemetryX.Target.BOTH);
                telemetryX.addData("Kd",Config_Tuning.Shooter.kd, TelemetryX.Target.BOTH);
                telemetryX.addData("Kf",Config_Tuning.Shooter.kf, TelemetryX.Target.BOTH);
                telemetryX.addData("Velocity",FSM_shooter.get_info_shooter()[0], TelemetryX.Target.BOTH);
                telemetryX.addData("Target",FSM_shooter.get_info_shooter()[1], TelemetryX.Target.BOTH);
                telemetryX.addData("Current_State",FSM_shooter.get_state(),TelemetryX.Target.BOTH);
        }
    }
}

//    Follower follower;

//        follower = Constants.create(hardwareMap);
//        follower.setPose(new Pose(72,-72,0));
//        follower.update();

//        drawing.draw(packet.fieldOverlay(),follower.pose());
//        packet = new TelemetryPacket(false);
//        FtcDashboard.getInstance().sendTelemetryPacket(packet);
//        follower.update();
//        telemetryX.update();