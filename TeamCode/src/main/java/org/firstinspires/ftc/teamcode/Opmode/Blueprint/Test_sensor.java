package org.firstinspires.ftc.teamcode.Opmode.Blueprint;


import android.graphics.Color;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Opmode.System.TelemetryX;
import org.firstinspires.ftc.teamcode.R;

public class Test_sensor {

    NormalizedColorSensor color_sen;
    NormalizedRGBA RGBA; // RED GREEN BLUE ALPHA
    TelemetryX telemetryX;

    float [] HSV = new float[3]; // use float because the calculation force to use it
    float hue;
    float saturation;
    float values;
    float alpha;
    boolean is_yellow = false;

    public void init (HardwareMap hardwareMap , TelemetryX telemetry){
        color_sen = hardwareMap.get(NormalizedColorSensor.class,"Sen");
        telemetryX = telemetry;
        color_sen.setGain(2.0f);
    }

    public void RGBA_To_HSV (){ // CONVERT RGBA in to HSV for stable detecting
        RGBA = color_sen.getNormalizedColors(); // Get red green blue and alpha from sensor
        Color.colorToHSV(RGBA.toColor() , HSV); // calculate in to HSV by Change RGBA value in to 34bits for 'colortoHsv' calculation
        // keep the value in HSV variable

    }

    public boolean check_ball (){ // need to be in main loop too
        RGBA_To_HSV();
        hue = HSV[0];
        saturation = HSV[1];
        values = HSV[2];
        alpha = RGBA.alpha;

        if  ( alpha >= 0.015f && (hue >= 45.0f && hue <= 80.0f) && (saturation >= 0.45f) && (values >= 0.20f)){
            is_yellow = true;
        }else {
            is_yellow = false;
        }

        return is_yellow;
    }

    public void telemetry_checkball(){
        telemetryX.addData("Is_Ball_Yellow",is_yellow, TelemetryX.Target.BOTH);
    }

    public void telemetry_HSV_value (){
        telemetryX.addData("Hue",hue, TelemetryX.Target.BOTH);
        telemetryX.addData("Saturation",saturation, TelemetryX.Target.BOTH);
        telemetryX.addData("Value",values, TelemetryX.Target.BOTH);
        telemetryX.addData("Alpha",alpha, TelemetryX.Target.BOTH);
    }

}
