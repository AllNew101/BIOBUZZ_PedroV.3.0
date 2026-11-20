package org.firstinspires.ftc.teamcode.Opmode.AUTO;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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

@Autonomous
public class Auto_Garden_Blue_Allies extends OpMode {
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

    boolean check_Turret_Red = false;
    double offset = 0;
    Distance.Goal Goal;
    double[] dis;

    // Poses
    private final Pose startPose = poseFactory.of(81, -135, 90);
    private final Pose scorePose = poseFactory.of(88, -128, 90);

    private final Pose Flower1_Turn = poseFactory.of(96, -116, 270);
    private final Pose Flower1_Keep = poseFactory.of(96, -130, 270);

    private final Pose scorePose2_Backward = poseFactory.of(96, -118, 270);
    private final Pose scorePose2 = poseFactory.of(96, -128, 90);
    private final Pose Park = poseFactory.of(134, -120, 90);

    // Path methods
    private Path startToScore() {return line(startPose, scorePose).linear(startPose, scorePose);}
    private Path ScoreToFlower_Turn() {return Paths.line(scorePose, Flower1_Turn).constant(Flower1_Turn);}
    private Path ScoreToFlower_Keep() {return Paths.line(Flower1_Turn, Flower1_Keep).linear(Flower1_Turn, Flower1_Keep);}
    private Path FlowerToScore_Backward() {return Paths.line(Flower1_Keep, scorePose2_Backward).linear(Flower1_Keep,scorePose2_Backward);}
    private Path FlowerToScore() {return Paths.line(scorePose2_Backward,scorePose2).linear(scorePose2_Backward,scorePose2);}
    private Path ScoreTo_Park(){return Paths.line(scorePose2,Park).linear(scorePose2,Park);}


    Command Shooter_Pollen_ON = instant(() -> FSM_shooter_pollen.command(FSM_Shooter_pollen.Current_State.Mid));
    Command Intake_In = instant(() -> FSM_intake.command(FSM_Intake.Current_State.In)); // 0.8
    Command Intake_Shoot = instant(() -> FSM_intake.command(FSM_Intake.Current_State.shoot)); // 0.5
    Command Intake_Boost = instant(() -> FSM_intake.command(FSM_Intake.Current_State.Boost)); // 0.8
    Command Shoot_pollen = instant(() -> FSM_closer_pollen.command(FSM_Closer_pollen.Current_State.Open));
    Command Close_pollen = instant(() -> FSM_closer_pollen.command(FSM_Closer_pollen.Current_State.Close));
    //    Command Shoot_nectar = instant(() -> FSM_closer_nectar.command(FSM_Closer_nectar.Current_State.Open));
    Command Turret_ON = instant(() -> FSM_turret.command(FSM_Turret.Current_State.Lock));
    Command Turret_OFF = instant(() -> FSM_turret.command(FSM_Turret.Current_State.M0));
    Command Flower_ON = instant(() -> flower2.setPosition(1));
    Command Flower_OFF = instant(() -> flower2.setPosition(0.25));
    Command Power_shoot_1 = instant(() -> Config_Tuning.FSM_Shooter.mid_pollen = 1045);
    Command Power_shoot_2 = instant(() -> Config_Tuning.FSM_Shooter.mid_pollen = 1030);
    Command Power_shoot_3 = instant(() -> Config_Tuning.FSM_Shooter.mid_pollen = 1040);
    Command Offset_1 = instant(() -> offset = 4);
    Command Offset_2 = instant(() -> offset = 6);
    Command Garden_Goal = instant(() -> Goal = Distance.Goal.Red_Human_Blue_Garden); // Blue
    Command Human_Goal = instant(() -> Goal = Distance.Goal.Red_Garden_Blue_Human); // Blue


    private Command AUTO() {
        return sequential(
                parallel(Turret_ON,Power_shoot_1,Shooter_Pollen_ON,Close_pollen,Human_Goal),
                follow(follower, startToScore()).with(Intake_Shoot),
                waitMs(6000), // Delay_Wait_Allies
                Shoot_pollen,
                waitMs(800),
                parallel(Close_pollen,Intake_In),
                follow(follower, ScoreToFlower_Turn()).with(Close_pollen),
                Flower_ON,
                waitMs(1200),
                race(follow(follower, ScoreToFlower_Keep()),waitMs(1000)),
                waitMs(1000),
                follow(follower, FlowerToScore_Backward()),
                waitMs(600),
                follow(follower, FlowerToScore()),
                waitMs(200).with(Intake_Shoot).with(Flower_OFF),
                Shoot_pollen,
                waitMs(1000),
                follow(follower, ScoreTo_Park()).with(Close_pollen)
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
        dis = distance.Pythagoras(follower.pose().x(),follower.pose().y(),follower.pose().heading(),Goal,check_Turret_Red,offset,offset);

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
