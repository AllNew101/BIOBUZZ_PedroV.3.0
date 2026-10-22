package org.firstinspires.ftc.teamcode.Opmode;

import com.acmerobotics.dashboard.config.Config;
@Config
public class Config_Tuning {
    @Config
    public static class Lift {
        public static double kp = 0;
        public static double ki = 0;
        public static double kd = 0;
        public static double kg = 0;
        public static double kf = 0;
    }
    @Config
    public static class Posi {
        public static double kp = 0;
        public static double ki = 0;
        public static double kd = 0;
        public static double kf = 0;
    }
    @Config
    public static class Turret {
        public static double MaxTheta = 50;
        public static double MinTheta = -50;
        public static double kp = 0.02;
        public static double ki = 0;
        public static double kd = 0;
        public static double kf = 0.17;
        public static double kpSec = 0.000001;
        public static double kiSec = 0;
        public static double kdSec = 0;
        public static double kfSec = 0.1;
    }
    @Config
    public static class Shooter_Pollen { // Yellow_ball
        public static double kd = 1e-8;
        public static double kf = 0.5;
        public static double ki = 0;
        public static double kp = 0.005;
    }
    @Config
    public static class Shooter_Nectar { // Color_ball
        public static double kd = 0.0000012;
        public static double kf = 0.15;
        public static double ki = 0;
        public static double kp = 0.03;
    }
    @Config
    public static class FSM_Turret {
        public static double Idle_target = 0;
    }
    @Config
    public static class FSM_Shooter {
        public static double far = 800;
        public static double mid_pollen = 1140;
        public static double mid_nectar = 1160;
        public static double power = 0.5;
    }
    @Config
    public static class Intake {
        public static double Intake_In = 0.5;
        public static double Intake_Shoot = 0.5;
        public static double Intake_Out = -0.8;
    }
    @Config
    public static class FSM_Closer {
        public static double close_nectar = 0.25;
        public static double close_pollen = 0.82;
        public static double open_nectar = 0.45;
        public static double open_pollen = 0.7;
    }
}