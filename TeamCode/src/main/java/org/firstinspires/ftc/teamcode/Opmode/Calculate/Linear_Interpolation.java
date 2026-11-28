package org.firstinspires.ftc.teamcode.Opmode.Calculate;


import org.firstinspires.ftc.teamcode.Opmode.System.TelemetryX;

public class Linear_Interpolation { // ใช้สมการเส้นตรงระหว่าง 2 จุด
    double[] Target_Velo = {1030,1035,1040}; // y
    double[] Dis_Pose = {47,50,53}; // x

    double target_velo;
    double slope;
    int Point;

    public double calculate (double Dis_target , double offset_shoot){
        int last = Dis_Pose.length - 1;
        if (Dis_target <= Dis_Pose[0]) {return Target_Velo[0];}
        if (Dis_target >= Dis_Pose[last]) {return Target_Velo[last];}

        while (Point < last){
            Point += 1;
            if (Dis_target <= Dis_Pose[Point]){break;}}

        slope = (Target_Velo[Point] - Target_Velo[Point - 1]) / (Dis_Pose[Point] - Dis_Pose[Point -1]);
        target_velo = Target_Velo[Point - 1] + (slope * (Dis_target - Dis_Pose[Point - 1]));
        return Math.round(target_velo);
    }

}
