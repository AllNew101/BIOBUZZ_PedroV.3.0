package org.firstinspires.ftc.teamcode.Opmode.AUTO;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.api.Paths.path;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import android.bluetooth.le.ScanSettings;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.CommandBuilder;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.Calculate.Distance;
import org.firstinspires.ftc.teamcode.Opmode.Calculate.lime_light;
import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Closer_nectar;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Intake;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Shooter_nectar;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Turret;
import org.firstinspires.ftc.teamcode.Opmode.System.TelemetryX;
import org.firstinspires.ftc.teamcode.Opmode.Teleop.Drawing;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Shooter_pollen;
import org.firstinspires.ftc.teamcode.Opmode.System.FSM_Closer_pollen;

@Autonomous
public class Blueprint_Auto extends OpMode {
    Follower follower;
    PoseFactory poseFactory = PoseFactory.degrees();

    Distance distance;
    TelemetryX telemetryX;
    FSM_Shooter_pollen FSM_shooter_pollen;
    FSM_Shooter_nectar FSM_shooter_nectar;
    FSM_Turret FSM_turret;
    FSM_Intake FSM_intake;
    FSM_Closer_pollen FSM_closer_pollen;
    FSM_Closer_nectar FSM_closer_nectar;
    ElapsedTime time;
    Servo flower2;

    boolean check_Turret_Red = true;
    double offset = 0;
    double[] dis;

    // Poses
    private final Pose startPose = poseFactory.of(57, -135, 90);
    private final Pose scorePose = poseFactory.of(57, -128, 90);
    private final Pose GardenPose = poseFactory.of(14, -134, 180);
    private final Pose GardenPose_ControlPoint = poseFactory.of(54, -140, 0);
    private final Pose scorePose2 = poseFactory.of(50, -19, 285);
    private final Pose scorePose2_ControlPoint = poseFactory.of(28, -109, 0);
    private final Pose scorePose2_ControlPoint2 = poseFactory.of(17, -44, 0);
    private final Pose Flower1_Turn = poseFactory.of(50, -20, 90);
    private final Pose Flower1_Keep = poseFactory.of(50, -14, 90);
    private final Pose scorePose3_Backward = poseFactory.of(50, -24, 90);
    private final Pose scorePose3 = poseFactory.of(50, -19, 285);
    private final Pose Flower2_Turn = poseFactory.of(26, -94, 180);
    private final Pose Flower2_Turn_ControlPoint = poseFactory.of(17, -44, 0);
    private final Pose Flower2_Turn_ControlPoint2 = poseFactory.of(50, -98, 180);
    private final Pose Flower2_Keep = poseFactory.of(19, -94, 180);
    private final Pose scorePose4 = poseFactory.of(50, -126, 75);

    // Path methods
    private Path startToScore() {return line(startPose, scorePose).linear(startPose, scorePose);}
    private Path Garden(){return Paths.curve(scorePose, GardenPose_ControlPoint, GardenPose).constant(GardenPose);}
    private Path GardenToScore(){return Paths.curve(GardenPose, scorePose2_ControlPoint, scorePose2_ControlPoint2, scorePose2).linear(GardenPose,scorePose2);}
    private Path ScoreToFlower_Turn() {return Paths.line(scorePose2, Flower1_Turn).constant(Flower1_Turn);}
    private Path ScoreToFlower_Keep() {return Paths.line(Flower1_Turn, Flower1_Keep).linear(Flower1_Turn, Flower1_Keep);}
    private Path FlowerToScore_Backward() {return Paths.line(Flower1_Keep, scorePose3_Backward).linear(Flower1_Keep,scorePose3_Backward);}
    private Path FlowerToScore() {return Paths.line(scorePose3_Backward,scorePose3).linear(scorePose3_Backward,scorePose3);}
    private Path ScoreToFlower2_Turn(){return Paths.curve(scorePose3, Flower2_Turn_ControlPoint, Flower2_Turn_ControlPoint2, Flower2_Turn).linear(scorePose3,Flower2_Turn);}
    private Path ScoreToFlower2_Keep() {return Paths.line(Flower2_Turn, Flower2_Keep).linear(Flower2_Turn, Flower2_Keep);}
    private Path Flower2ToScore() {return Paths.line(Flower2_Keep,scorePose4).linear(Flower2_Keep,scorePose4);}


    Command Shooter_Pollen_ON = instant(() -> FSM_shooter_pollen.command(FSM_Shooter_pollen.Current_State.Mid));
    Command Intake_In = instant(() -> FSM_intake.command(FSM_Intake.Current_State.In)); // 0.8
    Command Intake_Shoot = instant(() -> FSM_intake.command(FSM_Intake.Current_State.shoot)); // 0.5
    Command Intake_Boost = instant(() -> FSM_intake.command(FSM_Intake.Current_State.Boost)); // 0.5
    Command Shoot_pollen = instant(() -> FSM_closer_pollen.command(FSM_Closer_pollen.Current_State.Open));
    Command Close_pollen = instant(() -> FSM_closer_pollen.command(FSM_Closer_pollen.Current_State.Close));
//    Command Shoot_nectar = instant(() -> FSM_closer_nectar.command(FSM_Closer_nectar.Current_State.Open));
    Command Turret_ON = instant(() -> FSM_turret.command(FSM_Turret.Current_State.Lock));
    Command Turret_OFF = instant(() -> FSM_turret.command(FSM_Turret.Current_State.M0));
    Command Flower_ON = instant(() -> flower2.setPosition(1));
    Command Flower_OFF = instant(() -> flower2.setPosition(0.25));
    Command Power_shoot_1 = instant(() -> Config_Tuning.FSM_Shooter.mid_pollen = 1120);
    Command Power_shoot_2 = instant(() -> Config_Tuning.FSM_Shooter.mid_pollen = 1110);


    private Command AUTO() {
        return sequential(
                parallel(Turret_OFF,Power_shoot_1,Shooter_Pollen_ON,Close_pollen),
                follow(follower, startToScore()),
                waitMs(600).with(Intake_Boost),
                Shoot_pollen,
                waitMs(500),
                parallel(Close_pollen,Intake_In,follow(follower, Garden())),
                waitMs(500),
                follow(follower, GardenToScore()),
                waitMs(200).with(Intake_Boost),
                Shoot_pollen,
                waitMs(600),
                follow(follower, ScoreToFlower_Turn()).with(Close_pollen).with(Flower_ON),
                waitMs(500),
                race( follow(follower, ScoreToFlower_Keep()),waitMs(1000)),
                waitMs(1000),
                follow(follower, FlowerToScore_Backward()),
                waitMs(600),
                follow(follower, FlowerToScore()),
                waitMs(200).with(Intake_Boost).with(Flower_OFF),
                Shoot_pollen,
                waitMs(1000),
                follow(follower, ScoreToFlower2_Turn()).with(Close_pollen),
                Flower_ON,
                waitMs(400),
                race( follow(follower, ScoreToFlower2_Keep()),waitMs(1000)),
                waitMs(1000),
                follow(follower, Flower2ToScore()).with(Power_shoot_2),
                waitMs(200).with(Intake_Boost).with(Flower_OFF),
                Shoot_pollen,
                waitMs(600)
                );
    }

    @Override
    public void init() {
        time = new ElapsedTime();
        telemetryX = new TelemetryX();
        FSM_shooter_pollen = new FSM_Shooter_pollen();
//        FSM_shooter_nectar = new FSM_Shooter_nectar();
        FSM_intake = new FSM_Intake();
        FSM_closer_pollen = new FSM_Closer_pollen();
//        FSM_closer_nectar = new FSM_Closer_nectar();
        FSM_turret = new FSM_Turret();
        distance = new Distance();

        flower2 = hardwareMap.get(Servo.class,"Flower2");
        flower2.setDirection(Servo.Direction.REVERSE);

        time.reset();
        time.startTime();
        FSM_shooter_pollen.init(hardwareMap,time);
//        FSM_shooter_nectar.init(hardwareMap,time);
        FSM_intake.init(hardwareMap);
        FSM_closer_pollen.init(hardwareMap);
//        FSM_closer_nectar.init(hardwareMap);
        FSM_turret.init(hardwareMap,time);
        distance.init(time);
        telemetryX.init(telemetry);
        telemetryX.clear();

        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
    }

    @Override
    public void start() {
        schedule(AUTO());
        FSM_intake.command(FSM_Intake.Current_State.Stop);
    }

    @Override
    public void loop() {
        dis = distance.Pythagoras(follower.pose().x(),follower.pose().y(),follower.pose().heading(), Distance.Goal.Red_Garden_Blue_Human,check_Turret_Red,offset,offset);

        FSM_shooter_pollen.update_state();
//        FSM_shooter_nectar.update_state();
        FSM_intake.update_state(0);
        FSM_closer_pollen.update_state();
//        FSM_closer_nectar.update_state();
        telemetryX.update();
        FSM_turret.update_state(0,dis);
        follower.update();

        Scheduler.execute();

        FSM_intake.update_state(0);
        telemetryX.addData("X", follower.pose().x());
        telemetryX.addData("Y", follower.pose().y());
        telemetryX.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetryX.addData("Follower Mode", follower.mode());
        telemetryX.update();

    }

}
