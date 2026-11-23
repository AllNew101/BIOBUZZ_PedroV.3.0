package org.firstinspires.ftc.teamcode.Opmode.AUTO;

import com.pedropathing.math.Pose;

public class Auto_End_Pose {

    public enum Start_pose {End_Red, End_Red_Allies, End_Blue, End_Blue_Allies, Default}

    public static Pose End_Auto_Red = new Pose(0, 0, 0);
    public static Pose End_Auto_Red_Allies = new Pose(0, 0, 0);
    public static Pose End_Auto_Blue = new Pose(0, 0, 0);
    public static Pose End_Auto_Blue_Allies = new Pose(0, 0, 0);
    public static Pose Default_Start = new Pose(57,-135,Math.toRadians(90));
    public static Start_pose Start_Pose = Start_pose.Default;
}
//        follower.setPose(new Pose(63,-9,Math.toRadians(90)));

