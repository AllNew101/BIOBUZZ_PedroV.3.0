package org.firstinspires.ftc.teamcode.Opmode.Teleop;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.pedro.Constants;

import org.firstinspires.ftc.teamcode.Opmode.Blueprint.Distance;
import org.firstinspires.ftc.teamcode.Opmode.Blueprint.FSM_Lift;
import org.firstinspires.ftc.teamcode.Opmode.Blueprint.FSM_Turret;
import org.firstinspires.ftc.teamcode.Opmode.Blueprint.Test_sensor;
import org.firstinspires.ftc.teamcode.Opmode.System.TelemetryX;


import java.util.function.Supplier;
@Config
@TeleOp
public class Drive extends OpMode {

    Follower follower;
//    Test_sensor checkball;
    Drawing drawing;
    TelemetryX telemetryX;
    TelemetryPacket packet;
    DcMotorEx motor1;
    public static int debug_key = 0;
    public static double test = 1;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void init() {

        packet = new TelemetryPacket();
        telemetryX = new TelemetryX();
//        checkball = new Test_sensor();
//        motor1 = hardwareMap.get(DcMotorEx.class, "Back_R");

    //    test_shooter = hardwareMap.get(DcMotorEx.class, "motor1");

        telemetryX.init(telemetry);
//        checkball.init(hardwareMap,telemetryX);

        follower = Constants.create(hardwareMap);
        follower.setPose(new Pose(72,-72,0));
        follower.update();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void start() {
    }
    @Override
    public void loop() {


        drawing.draw(packet.fieldOverlay(),follower.pose());
        packet = new TelemetryPacket(false);
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
        follower.update();
        telemetryX.update();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////
}




