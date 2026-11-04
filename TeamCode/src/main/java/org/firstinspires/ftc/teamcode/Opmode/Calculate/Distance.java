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

    public double[] Red_Goal_1 = {58 , -84 , 0}; // x,y,theta ** Garden_red
    public double[] Red_Goal_2 = {58, -60 , 0}; // x,y,theta ** Human_red
    public double[] Blue_Goal_1 = {86, -84 , 0}; // x,y,theta ** Human_blue
    public double[] Blue_Goal_2 = {86, -60 , 0}; // x,y,theta ** Garden_blue
    double Max_theta = Config_Tuning.Turret.MaxTheta;
    double Min_theta = Config_Tuning.Turret.MinTheta;
    double Gear_ratio = 3; // 3:1
    double maximum_servo = 355;

    public double distance,deltaX,deltaY,Target_Theta,Target_Theta_Servo,Theta,robot;
    public void init (ElapsedTime elapsedTime){
        time = elapsedTime;}

    public double[] IDLE (){
        double[] idle = {0,0,0};
        return idle;
    }

    public enum Goal {Red_Garden_Blue_Human,Red_Human_Blue_Garden}

    public double[] Pythagoras (double X,double Y,double robot_theta,Goal goal,boolean Check_red,double offset_garden,double offset_human){
        if (goal == Goal.Red_Garden_Blue_Human) {
            if (Check_red) { // Red_Garden
                deltaX = Red_Goal_1[0] - X;
                deltaY = Red_Goal_1[1] - Y;
            }else {  // Blue_Garden
                deltaX = Blue_Goal_2[0] - X;
                deltaY = Blue_Goal_2[1] - Y;}
            distance = Math.hypot(deltaX, deltaY);// c^2 = a^2 + b^2
        }else if (goal == Goal.Red_Human_Blue_Garden) {
            if (Check_red) { // Red_Human
                deltaX = Red_Goal_2[0] - X;
                deltaY = Red_Goal_2[1] - Y;
            } else {  // Blue_Human
                deltaX = Blue_Goal_1[0] - X;
                deltaY = Blue_Goal_1[1] - Y;}
            distance = Math.hypot(deltaX,deltaY);}// c^2 = a^2 + b^2

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

    public double pythagoras_limelight (double X,double Y,double robot_theta){
        double limelight_X = X + (8 * (Math.cos(robot_theta)));
        double limelight_Y = Y + (8 * (Math.sin(robot_theta)));

        deltaX = Red_Goal_1[0] - limelight_X;// X_robot
        deltaY = Red_Goal_1[1] - limelight_Y;// Y_robot
        distance = (deltaX * Math.cos(robot_theta)) + (deltaY * Math.sin(robot_theta));
        return distance;
    }

    public double degree_to_Servo (double degree){
        double target_theta_servo = ((degree * Gear_ratio) / maximum_servo) + 0.5 ;
        return target_theta_servo;
    }

}