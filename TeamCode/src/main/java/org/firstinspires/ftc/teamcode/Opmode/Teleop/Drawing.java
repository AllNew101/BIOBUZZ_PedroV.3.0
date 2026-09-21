package org.firstinspires.ftc.teamcode.Opmode.Teleop;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.pedropathing.math.Pose;

public class Drawing {

    public static void draw(Canvas canvas, Pose pose) {
        if (pose == null) return;

        canvas.drawImage("/images/biobuzz-field.webp", 0, 0, 144, 144);

        double x = pose.x() - 72;
        double y = pose.y() + 72;

        // 2. Draw robot circle
        canvas.setStroke("red");
        canvas.fillCircle(x, y, 9);

        // 3. Draw heading line
        double headX = x + Math.cos(pose.heading()) *10;
        double headY = y + Math.sin(pose.heading()) *10;

        canvas.setStroke("white");
        canvas.strokeLine(x, y, headX, headY);
    }
}