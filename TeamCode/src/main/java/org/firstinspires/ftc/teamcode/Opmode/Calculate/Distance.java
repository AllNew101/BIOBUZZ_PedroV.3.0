package org.firstinspires.ftc.teamcode.Opmode.Calculate;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Opmode.Config_Tuning;

public class Distance {
    ElapsedTime time;
    Follower follower;

    public double[] Red_Goal = {128.80923076923077 , -140.4923076923077 , 33.0}; // x,y,theta
    public double[] Blue_Goal = {123, -18 , 50.0}; // x,y,theta
    double Max_theta = Config_Tuning.Turret.MaxTheta;
    double Min_theta = Config_Tuning.Turret.MinTheta;

    public double distance,deltaX,deltaY,Target_Theta,Target_Theta_Servo,Theta,robot;
    public void init (ElapsedTime elapsedTime){
        time = elapsedTime;
    }

    public double[] Pythagoras (double X,double Y,double robot_theta,boolean Goal_red){
        if (Goal_red) {
            deltaX = Red_Goal[0] - X;// X_robot
            deltaY = Red_Goal[1] - Y;// Y_robot
            distance = Math.hypot(deltaX,deltaY);// c^2 = a^2 + b^2
        }
        else if (!Goal_red) {
            deltaX = Blue_Goal[0] - X;
            deltaY = Blue_Goal[1] - Y;
            distance = Math.hypot(deltaX,deltaY);
        }
        Theta = Math.round(Math.toDegrees(Math.atan2(deltaY,deltaX))); // Radian to Degree
        robot =  Math.round(Math.toDegrees(robot_theta)); // Radian to Degree ** .round = ปัดเศษ
        Target_Theta = AngleUnit.normalizeDegrees(Theta - robot);

        if (Target_Theta > Max_theta){Target_Theta = Max_theta;}
        else if (Target_Theta < Min_theta) {Target_Theta = Min_theta;}


        double[] pythagoras = {distance,Target_Theta,Target_Theta_Servo};
        return pythagoras;
    }

}