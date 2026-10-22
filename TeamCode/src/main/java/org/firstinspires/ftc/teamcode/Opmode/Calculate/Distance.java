package org.firstinspires.ftc.teamcode.Opmode.Calculate;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;

@Config
public class Distance {
    ElapsedTime time;
    Follower follower;

    public double[] Red_Goal_1 = {58 , -84 , 0}; // x,y,theta
    public double[] Red_Goal_2 = {60, -60 , 0}; // x,y,theta
    public double[] Blue_Goal_1 = {123, -18 , 0}; // x,y,theta
    public double[] Blue_Goal_2 = {123, -18 , 0}; // x,y,theta
    double Max_theta = Config_Tuning.Turret.MaxTheta;
    double Min_theta = Config_Tuning.Turret.MinTheta;
    double Gear_ratio = 3; // 3:1
    double maximum_servo = 355;

    public double distance,deltaX,deltaY,Target_Theta,Target_Theta_Servo,Theta,robot;
    public void init (ElapsedTime elapsedTime){
        time = elapsedTime;}

    public double[] Pythagoras (double X,double Y,double robot_theta,int Goal,boolean Check_red,double offset_garden,double offset_human){
        if (Goal == 1 && Check_red) { // Red_Garden
            deltaX = Red_Goal_1[0] - X;// X_robot
            deltaY = Red_Goal_1[1] - Y;// Y_robot
            distance = Math.hypot(deltaX, deltaY);// c^2 = a^2 + b^2
        }else if (Goal == 2 && Check_red) { // Red_Human
            deltaX = Red_Goal_2[0] - X;
            deltaY = Red_Goal_2[1] - Y;
            distance = Math.hypot(deltaX,deltaY);
        }else if (Goal == 1 && !Check_red) { // Blue_Garden
            deltaX = Blue_Goal_1[0] - X;
            deltaY = Blue_Goal_1[1] - Y;
            distance = Math.hypot(deltaX, deltaY);
        }else if (Goal == 2 && !Check_red) { // Blue_Human
            deltaX = Blue_Goal_2[0] - X;
            deltaY = Blue_Goal_2[1] - Y;
            distance = Math.hypot(deltaX,deltaY);}
        Theta = Math.round(Math.toDegrees(Math.atan2(deltaY,deltaX))); // Radian to Degree
        robot =  Math.round(Math.toDegrees(robot_theta)); // Radian to Degree ** .round = ปัดเศษ
        Target_Theta = AngleUnit.normalizeDegrees(Theta - robot);

        if (Target_Theta > Max_theta){Target_Theta = Max_theta;}
        else if (Target_Theta < Min_theta) {Target_Theta = Min_theta;}
        if (Y <= -72){Target_Theta_Servo = degree_to_Servo(Target_Theta - offset_garden);}
        else {Target_Theta_Servo = degree_to_Servo(Target_Theta - offset_human);}


        double[] pythagoras = {distance,Target_Theta,Target_Theta_Servo};
        return pythagoras;
    }

    public double degree_to_Servo (double degree){
        double target_theta_servo = ((degree * Gear_ratio) / maximum_servo) + 0.5 ;
        return target_theta_servo;
    }

}