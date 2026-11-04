package org.firstinspires.ftc.teamcode.Opmode.Calculate;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

public class lime_light {

    Limelight3A limelight;
    double camera_theta = 38.57;
    double camera_height = 9.45; // Inch in cm is 24
    double offset_robot_to_camera = 8.5;


    public void init (HardwareMap hardwareMap){
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }
    public double get_x (){
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            Pose3D botpose = result.getBotpose();
            if (botpose != null){
            return botpose.getPosition().x;}
        }
        return 0.0;
    }
    public double get_y (double dis){
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            return calculate_height(dis,result.getTy());
//            return result.getTy();
        }
        return 0.0;
    }
    public double calculate_height (double distance , double Ty){
        double real_dis = distance;
        double height = camera_height + (Math.tan(Math.toRadians(camera_theta + Ty)) * real_dis);
        return height;
    }
}
