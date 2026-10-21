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
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Closer_nectar;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Closer_pollen;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Intake;

import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Shooter_nectar;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Shooter_pollen;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Turret;
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
    FSM_Shooter_pollen FSM_shooter_pollen;
    FSM_Shooter_nectar FSM_shooter_nectar;
    FSM_Turret FSM_turret;
    FSM_Intake FSM_intake;
    FSM_Closer_pollen FSM_closer_pollen;
    FSM_Closer_nectar FSM_closer_nectar;
    ElapsedTime time;

    public static int debug_key = 0;

    double RR;
    double RL;
    double FR;
    double FL;
    boolean check_cross = true;
    boolean check_intake_in = false;
    boolean check_circle = true;
    boolean check_intake_out = false;
    boolean check_triggerR = true;
    boolean check_closer_pollen = false;
    boolean check_triggerR2 = true;
    boolean check_closer_nectar = false;
    boolean check_bumperR = true;
    boolean check_shoot_pollen = false;
    boolean check_bumperR2 = true;
    boolean check_shoot_nectar = false;
    boolean check_dpadL = true;
    boolean check_dpadR = true;
    int state_turret_manual = 2;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void init() {
        time = new ElapsedTime();
        packet = new TelemetryPacket();
        telemetryX = new TelemetryX();
        FSM_shooter_pollen = new FSM_Shooter_pollen();
        FSM_shooter_nectar = new FSM_Shooter_nectar();
        FSM_intake = new FSM_Intake();
        FSM_closer_pollen = new FSM_Closer_pollen();
        FSM_closer_nectar = new FSM_Closer_nectar();
        FSM_turret = new FSM_Turret();

        motorRR = hardwareMap.get(DcMotorEx.class, "Back_R");
        motorRL = hardwareMap.get(DcMotorEx.class, "Back_L");
        motorFR = hardwareMap.get(DcMotorEx.class, "Front_R");
        motorFL = hardwareMap.get(DcMotorEx.class, "Front_L");


        motorRL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);

        time.reset();
        time.startTime();
        FSM_shooter_pollen.init(hardwareMap,time);
        FSM_shooter_nectar.init(hardwareMap,time);
        FSM_intake.init(hardwareMap);
        FSM_closer_pollen.init(hardwareMap);
        FSM_closer_nectar.init(hardwareMap);
        FSM_turret.init(hardwareMap,time);
        telemetryX.init(telemetry);
        telemetryX.clear();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void start() {
        FSM_shooter_pollen.command(FSM_Shooter_pollen.Current_State.Stop);
        FSM_shooter_nectar.command(FSM_Shooter_nectar.Current_State.Stop);
        FSM_intake.command(FSM_Intake.Current_State.Stop);
    }
    @Override
    public void loop() {
        FL = -gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x;
        FR = -gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
        RL = -gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x;
        RR = -gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;
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
            FSM_intake.command(FSM_Intake.Current_State.In);}
        // OUT
        if (gamepad1.circleWasPressed() && check_circle) {
            check_intake_out = !check_intake_out;
            check_circle = false;
        } else if (!gamepad1.circleWasPressed()) {
            check_circle = true;}
        if (check_intake_out) {
            FSM_intake.command(FSM_Intake.Current_State.Out);}
        // STOP
        if (!check_intake_in && !check_intake_out){
            FSM_intake.command(FSM_Intake.Current_State.Stop);}

        // CLOSER
        // POLLEN
        if (gamepad1.right_trigger > 0 && check_triggerR) {
            check_closer_pollen = !check_closer_pollen;
            check_triggerR = false;
        } else if (gamepad1.right_trigger <= 0) {
            check_triggerR = true;}
        if (check_closer_pollen){
            FSM_closer_pollen.command(FSM_Closer_pollen.Current_State.Open);
            FSM_closer_nectar.command(FSM_Closer_nectar.Current_State.Open);
            FSM_intake.command(FSM_Intake.Current_State.shoot);
        }else {
            FSM_closer_pollen.command(FSM_Closer_pollen.Current_State.Close);
            FSM_closer_nectar.command(FSM_Closer_nectar.Current_State.Close);}

        // PID_SHOOTER
        // POLLEN
        if (gamepad1.rightBumperWasPressed() && check_bumperR){
            check_shoot_pollen = !check_shoot_pollen;
            check_bumperR = false;
        } else if (!gamepad1.rightBumperWasPressed()) {
            check_bumperR = true;}
        if (check_shoot_pollen){
            FSM_shooter_pollen.command(FSM_Shooter_pollen.Current_State.Mid);}
        else {
            FSM_shooter_pollen.command(FSM_Shooter_pollen.Current_State.Stop);
        }
        // NECTAR
        if (gamepad2.rightBumperWasPressed() && check_bumperR2){
            check_shoot_nectar = !check_shoot_nectar;
            check_bumperR2 = false;
        } else if (!gamepad2.rightBumperWasPressed()) {
            check_bumperR2 = true;}
        if (check_shoot_nectar){
            FSM_shooter_nectar.command(FSM_Shooter_nectar.Current_State.Mid);}
        else {
            FSM_shooter_nectar.command(FSM_Shooter_nectar.Current_State.Stop);
        }

        // Turret
        if (gamepad1.dpad_left && check_dpadL){
            if (state_turret_manual > 0) {
                state_turret_manual -= 1;}
            else {state_turret_manual = 0;}
            check_dpadL = false;
        } else if (!gamepad1.dpad_left){
            check_dpadL = true;}
        if (gamepad1.dpad_right && check_dpadR){
            if (state_turret_manual < 4){
                state_turret_manual += 1;}
            else {state_turret_manual = 4;}
            check_dpadR = false;
        } else if (!gamepad1.dpad_right){
            check_dpadR = true;}
        if (state_turret_manual == 0){FSM_turret.command(FSM_Turret.Current_State.L2);}
        if (state_turret_manual == 1){FSM_turret.command(FSM_Turret.Current_State.L1);}
        if (state_turret_manual == 2){FSM_turret.command(FSM_Turret.Current_State.M0);}
        if (state_turret_manual == 3){FSM_turret.command(FSM_Turret.Current_State.R1);}
        if (state_turret_manual == 4){FSM_turret.command(FSM_Turret.Current_State.R2);}


        debugging();
        FSM_shooter_pollen.update_state();
        FSM_shooter_nectar.update_state();
        FSM_intake.update_state();
        FSM_closer_pollen.update_state();
        FSM_closer_nectar.update_state();
        FSM_turret.update_state();
        telemetryX.update();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////

    public void debugging (){
        switch (debug_key){
            case 0 :
                telemetryX.addData("PID_Shooter",true, TelemetryX.Target.BOTH);
                telemetryX.addData("Velocity_pollen",FSM_shooter_pollen.get_info_shooter()[0], TelemetryX.Target.BOTH);
                telemetryX.addData("Velocity_nectar",FSM_shooter_nectar.get_info_shooter()[0], TelemetryX.Target.BOTH);
                telemetryX.addData("Target_Pollen",FSM_shooter_pollen.get_info_shooter()[1], TelemetryX.Target.BOTH);
                telemetryX.addData("Target_Nectar",FSM_shooter_nectar.get_info_shooter()[1], TelemetryX.Target.BOTH);
                telemetryX.addData("Current_State_Shooter_pollen",FSM_shooter_pollen.get_state(),TelemetryX.Target.BOTH);
                telemetryX.addData("Current_State_Shooter_nectar",FSM_shooter_pollen.get_state(),TelemetryX.Target.BOTH);
                telemetryX.addData("Current_State_Intake",FSM_intake.get_state(),TelemetryX.Target.BOTH);
                telemetryX.addData("Current_State_Closer_pollen",FSM_closer_pollen.get_state(),TelemetryX.Target.BOTH);
                telemetryX.addData("Current_State_Closer_nectar",FSM_closer_nectar.get_state(),TelemetryX.Target.BOTH);
                telemetryX.addData("test",Config_Tuning.FSM_Closer.close_pollen,TelemetryX.Target.BOTH);

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