package org.firstinspires.ftc.teamcode.Opmode.System;


import org.firstinspires.ftc.robotcore.external.Telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

public class  TelemetryX {
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashboardTelemetry = dashboard.getTelemetry();
    Telemetry Driver_tele;

    public enum Target {
        DRIVER,
        DASHBOARD,
        BOTH
    }

    public void init(Telemetry t){
        Driver_tele = t;
    }

    public void addData(String head, Object child, Target key){
        switch (key) {

            case DRIVER:
                Driver_tele.addData(head, child);
                break;

            case DASHBOARD:
                dashboardTelemetry.addData(head,child);
                break;

            case BOTH:
                Driver_tele.addData(head, child);
                dashboardTelemetry.addData(head,child);
                break;

        }
    }

    // public void

    public void update(){
        Driver_tele.update();
        dashboardTelemetry.update();
    }
    public void clear(){
        Driver_tele.clear();
    }
}


