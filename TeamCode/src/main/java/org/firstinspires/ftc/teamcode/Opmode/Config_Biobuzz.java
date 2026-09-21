package org.firstinspires.ftc.teamcode.Opmode;

import com.acmerobotics.dashboard.config.Config;

@Config
public class Config_Biobuzz {

    public static class Lift {
        public static double kp = 0;
        public static double ki = 0;
        public static double kd = 0;
        public static double kg = 0;
        public static double kf = 0;
    }

    public static class Posi {
        public static double kp = 0;
        public static double ki = 0;
        public static double kd = 0;
        public static double kf = 0;
    }

    public static class Turret {
        public static double MaxAngle = 120;
        public static double MinAngle = -120;
        public static double kp = 0.02;
        public static double ki = 0;
        public static double kd = 0;
        public static double kf = 0.17;
        public static double kpSec = 0.000001;
        public static double kiSec = 0;
        public static double kdSec = 0;
        public static double kfSec = 0.1;
    }

    public static class Velo {
        public static double kp = 0;
        public static double ki = 0;
        public static double kd = 0;
        public static double kf = 0;
    }

    public static class FSM_Turret {
        public static double Idle_target = 0;
    }

    public static class FSM_Velo {
        public static double fast = 1000;
        public static double mid = 500;
        public static double stop = 0;
    }

}