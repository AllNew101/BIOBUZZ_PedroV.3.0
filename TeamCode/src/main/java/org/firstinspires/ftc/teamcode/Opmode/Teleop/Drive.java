package org.firstinspires.ftc.teamcode.Opmode.Teleop;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Calculate.Distance;
import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Closer_nectar;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Closer_pollen;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Intake;

import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Shooter_nectar;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Shooter_pollen;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Turret;
import org.firstinspires.ftc.teamcode.Opmode.System.TelemetryX;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Config
@TeleOp
public class Drive extends OpMode {
    Follower follower;
    Drawing drawing;
    Distance distance;
    TelemetryX telemetryX;
    TelemetryPacket packet;
    FSM_Shooter_pollen FSM_shooter_pollen;
    FSM_Shooter_nectar FSM_shooter_nectar;
    FSM_Turret FSM_turret;
    FSM_Intake FSM_intake;
    FSM_Closer_pollen FSM_closer_pollen;
    FSM_Closer_nectar FSM_closer_nectar;
    ElapsedTime time;
    DcMotorEx motorRR;
    DcMotorEx motorRL;
    DcMotorEx motorFR;
    DcMotorEx motorFL;
    Servo flower2;
    Servo RGB;



    public static int debug_key = 0;
    public static double offset_garden = 0;
    public static double offset_human = 0;


    boolean check_cross = true;
    boolean check_intake_in = false;
    boolean check_circle = true;
    boolean check_intake_out = false;
    boolean check_intake_shoot = false;
    boolean check_triggerR = true;
    boolean check_closer_pollen = false;
    boolean check_bumperR = true;
    boolean check_shoot_pollen = false;
    boolean check_triangle2 = true;
    boolean check_shoot_nectar = false;
    boolean check_dpadL2 = true;
    boolean check_dpadR2 = true;
    int state_turret_manual = 2;
    boolean check_square = true;
    boolean check_turret = false;
    boolean check_bumperR2 = true;
    boolean check_bumperL2 = true;
    boolean check_triggerR2 = true;
    boolean check_triggerL2 = true;
    boolean check_DpadDOWN = true;
    boolean check_flower = false;
    boolean check_intake_boost = false;
    boolean check_Turret_Red = true;
    boolean check_ps2 = true;


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
        distance = new Distance();

        motorRR = hardwareMap.get(DcMotorEx.class, "Back_R");
        motorRL = hardwareMap.get(DcMotorEx.class, "Back_L");
        motorFR = hardwareMap.get(DcMotorEx.class, "Front_R");
        motorFL = hardwareMap.get(DcMotorEx.class, "Front_L");
        motorRL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        flower2 = hardwareMap.get(Servo.class,"Flower2");
        flower2.setDirection(Servo.Direction.REVERSE);
        RGB = hardwareMap.get(Servo.class,"RGB");

        time.reset();
        time.startTime();
        FSM_shooter_pollen.init(hardwareMap,time);
        FSM_shooter_nectar.init(hardwareMap,time);
        FSM_intake.init(hardwareMap);
        FSM_closer_pollen.init(hardwareMap);
        FSM_closer_nectar.init(hardwareMap);
        FSM_turret.init(hardwareMap,time);
        distance.init(time);
        telemetryX.init(telemetry);
        telemetryX.clear();

        follower = Constants.create(hardwareMap);
//        follower.setPose(new Pose(57,-135,Math.toRadians(90)));
        follower.setPose(new Pose(84,-84,Math.toRadians(90)));
        follower.update();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void start() {
        FSM_shooter_pollen.command(FSM_Shooter_pollen.Current_State.Stop);
        FSM_shooter_nectar.command(FSM_Shooter_nectar.Current_State.Stop);}
    @Override
    public void loop() {

        // DRIVE
        motorFL.setPower(-gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x);
        motorFR.setPower(-gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x);
        motorRL.setPower(-gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x);
        motorRR.setPower(-gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x);
//        double forward = -gamepad1.left_stick_y;
//        double lateral = -gamepad1.left_stick_x;
//        double turn = -gamepad1.right_stick_x;
//        follower.manual(forward, lateral, turn);

        // CLOSER
            // POLLEN/NECTAR
        if (gamepad1.right_trigger > 0 && check_triggerR) {
            check_closer_pollen = !check_closer_pollen;
            check_triggerR = false;
        } else if (gamepad1.right_trigger <= 0) {
            check_triggerR = true;}
        if (check_closer_pollen){
            FSM_closer_pollen.command(FSM_Closer_pollen.Current_State.Open);
            FSM_closer_nectar.command(FSM_Closer_nectar.Current_State.Open);
            FSM_intake.command(FSM_Intake.Current_State.shoot);// Intake
            RGB.setPosition(0.3);
            check_intake_shoot = true;
        }else {
            FSM_closer_pollen.command(FSM_Closer_pollen.Current_State.Close);
            FSM_closer_nectar.command(FSM_Closer_nectar.Current_State.Close);
            RGB.setPosition(0.5);
            check_intake_shoot = false;}

        // INTAKE
        // IN
        if (gamepad1.crossWasPressed() && check_cross) {
            check_intake_in = !check_intake_in;
            check_cross = false;
        } else if (!gamepad1.crossWasPressed()) {
            check_cross = true;}
        // OUT
        if (gamepad1.circleWasPressed() && check_circle) {
            check_intake_out = !check_intake_out;
            check_circle = false;
        } else if (!gamepad1.circleWasPressed()) {
            check_circle = true;}
        // BOOST
        if (gamepad2.cross){
            check_intake_boost = true;
        }else{
            check_intake_boost = false;}
        // FUNCTION
        if (check_intake_boost){FSM_intake.command(FSM_Intake.Current_State.Boost);}
        else if (check_intake_out){FSM_intake.command(FSM_Intake.Current_State.Out);}
        else if (check_intake_shoot){FSM_intake.command(FSM_Intake.Current_State.shoot);}
        else if (check_intake_in){FSM_intake.command(FSM_Intake.Current_State.In);}
        else {FSM_intake.command(FSM_Intake.Current_State.Stop);}

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
        if (gamepad2.triangleWasPressed() && check_triangle2){
            check_shoot_nectar = !check_shoot_nectar;
            check_triangle2 = false;
        } else if (!gamepad2.triangleWasPressed()) {
            check_triangle2 = true;}
        if (check_shoot_nectar){
            FSM_shooter_nectar.command(FSM_Shooter_nectar.Current_State.Mid);}
        else {
            FSM_shooter_nectar.command(FSM_Shooter_nectar.Current_State.Stop);}

        // SPEED_SHOOT
        if (gamepad2.right_bumper && check_bumperR2){
            Config_Tuning.FSM_Shooter.mid_pollen += 10;
            check_bumperR2 = false;}
        else if(!gamepad2.right_bumper) {
            check_bumperR2=true;}
        if (gamepad2.left_bumper&&check_bumperL2){
            Config_Tuning.FSM_Shooter.mid_nectar += 10;
            check_bumperL2 = false;}
        else if(!gamepad2.left_bumper)  {
            check_bumperL2=true;}
        if (gamepad2.right_trigger > 0 && check_triggerR2) {
            Config_Tuning.FSM_Shooter.mid_pollen -= 10;
            check_triggerR2 = false;}
        else if(gamepad2.right_trigger <= 0 ) {
            check_triggerR2=true;}
        if (gamepad2.left_trigger > 0 && check_triggerL2) {
            Config_Tuning.FSM_Shooter.mid_nectar -= 10;
            check_triggerL2 = false;}
        else if(gamepad2.left_trigger <= 0) {
            check_triggerL2=true;}

        //Flower
        if (gamepad2.dpadDownWasPressed() && check_DpadDOWN) {
            check_DpadDOWN = false;
            check_flower = !check_flower;}
        else if (!gamepad2.dpadDownWasPressed()) {
            check_DpadDOWN = true;}
        if (check_flower){
            flower2.setPosition(1);
        } else {
            flower2.setPosition(0.25);
        }

        // Turret // Pedro Uses
        if (gamepad1.square && check_square){
            check_turret = !check_turret;
            check_square = false;}
        else if (!gamepad1.square) {
            check_square = true;}
        if (check_turret && follower.pose().y() <=-90){
            FSM_turret.command(FSM_Turret.Current_State.Lock_garden);}
        else if (check_turret && follower.pose().y() >= -54){
            FSM_turret.command(FSM_Turret.Current_State.Lock_human);}
        else {
            FSM_turret.command(FSM_Turret.Current_State.M0);}
        if (gamepad2.dpad_right && check_dpadR2) {
            if (follower.pose().y() <= -72)
                offset_garden += 2;
            else {offset_human += 2;}
            check_dpadR2 = false;
        } else if (!gamepad2.dpad_right) {
            check_dpadR2 = true;}
        if (gamepad2.dpad_left && check_dpadL2) {
            if (follower.pose().y() <= -72)
                offset_garden -= 2;
            else {offset_human -= 2;}
            check_dpadL2 = false;
        } else if (!gamepad2.dpad_left) {
            check_dpadL2 = true;}
        if (gamepad2.ps && check_ps2){
            check_Turret_Red = !check_Turret_Red;
            check_ps2 = false;
        } else if (!gamepad2.ps){check_ps2 = true;}

        debugging();
        FSM_shooter_pollen.update_state();
        FSM_shooter_nectar.update_state();
        FSM_intake.update_state();
        FSM_closer_pollen.update_state();
        FSM_closer_nectar.update_state();
        telemetryX.update();
        FSM_turret.update_state(follower,gamepad2.left_stick_x,offset_garden,offset_human,check_Turret_Red);
        follower.update();
            drawing.draw(packet.fieldOverlay(),follower.pose());
            packet = new TelemetryPacket(false);
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
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
                telemetryX.addData("Current_State_Closer",FSM_closer_pollen.get_state(),TelemetryX.Target.BOTH);
                telemetryX.addData("theta_target",(distance.Pythagoras(follower.pose().x(),follower.pose().y(),follower.pose().heading(),2,true,offset_garden,offset_human)[1]),TelemetryX.Target.BOTH);
                telemetryX.addData("X",follower.pose().x(),TelemetryX.Target.BOTH);
                telemetryX.addData("Y",follower.pose().y(),TelemetryX.Target.BOTH);
                telemetryX.addData("theta_robot",Math.toDegrees(follower.pose().heading()),TelemetryX.Target.BOTH);
                telemetryX.addData("offset_human",offset_human,TelemetryX.Target.BOTH);
                telemetryX.addData("offset_garden",offset_garden,TelemetryX.Target.BOTH);


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

//            if (gamepad2.dpad_left && check_dpadL2) {
//                if (state_turret_manual > 0) {
//                    state_turret_manual -= 1;
//                } else {
//                    state_turret_manual = 0;}
//                check_dpadL2 = false;
//            } else if (!gamepad2.dpad_left) {
//                check_dpadL2 = true;}
//            if (gamepad2.dpad_right && check_dpadR2) {
//                if (state_turret_manual < 4) {
//                    state_turret_manual += 1;
//                } else {
//                    state_turret_manual = 4;}
//                check_dpadR2 = false;
//            } else if (!gamepad2.dpad_right) {
//                check_dpadR2 = true;}
//            if (state_turret_manual == 0) {FSM_turret.command(FSM_Turret.Current_State.L2);}
//            if (state_turret_manual == 1) {FSM_turret.command(FSM_Turret.Current_State.L1);}
//            if (state_turret_manual == 2) {FSM_turret.command(FSM_Turret.Current_State.M0);}
//            if (state_turret_manual == 3) {FSM_turret.command(FSM_Turret.Current_State.R1);}
//            if (state_turret_manual == 4) {FSM_turret.command(FSM_Turret.Current_State.R2);}