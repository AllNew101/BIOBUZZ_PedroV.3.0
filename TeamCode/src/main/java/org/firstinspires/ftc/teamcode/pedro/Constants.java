    package org.firstinspires.ftc.teamcode.pedro;

    import com.pedropathing.algorithm.Foresight;
    import com.pedropathing.algorithm.ForesightConfig;
    import com.pedropathing.controllers.Controller;
    import com.pedropathing.follower.Follower;
    import com.pedropathing.math.Matrix;
    import com.pedropathing.math.Vector2D;
    import com.pedropathing.revhub.drivetrains.Mecanum;
    import com.pedropathing.revhub.drivetrains.MecanumConfig;
    import com.pedropathing.revhub.localizers.PinpointConfig;
    import com.pedropathing.revhub.localizers.PinpointLocalizer;
    import com.pedropathing.tuning.autotune.Procedure;
    import com.pedropathing.tuning.autotune.Tuner;
    import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
    import com.qualcomm.robotcore.hardware.DcMotorSimple;
    import com.qualcomm.robotcore.hardware.HardwareMap;

    import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
    import org.firstinspires.ftc.teamcode.pedro.procedures.ForesightTuner;

    public class Constants {

        public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
            c.frontLeftName.set("leftFront");
            c.frontRightName.set("rightFront");
            c.backLeftName.set("leftRear");
            c.backRightName.set("rightRear");
            c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
            c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
            c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
            c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
        });

        public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
            c.name.set("odo");
            c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
            c.xPodOffset.set(2.198078125480592);
            c.yPodOffset.set(-5.929951705331878);
            c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
            c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
            c.globalDistanceUnit.set(DistanceUnit.INCH);
            c.offsetUnits.set(DistanceUnit.INCH);
        });

        public static ForesightConfig foresightConfig = new ForesightConfig(
                c -> {
                    Controller primaryTranslationalForward = Controller.proportional(0.3060392184220425);
                    Controller secondaryTranslationalForward = Controller.proportional(0.11307331368842097);
                    Controller primaryTranslationalLateral = Controller.proportional(0.5511878586002942);
                    Controller secondaryTranslationalLateral = Controller.proportional(0.20364918574197718);

                    c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                    c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                    c.coast.set(Controller.proportionalFeedforward(0.016498874319945605));
                    c.brake.set(Controller.proportionalFeedforward(0.014024043171953764));

                    c.headingFeedback.set(Controller.proportional(4.840345474659974));
                    c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04933142963333411, 0.005262357109277144));

                    c.linearBrakeCoefficients.set(Matrix.diag(0.11775969635751184, 0.01887631387797331));
                    c.quadraticBrakeCoefficients.set(Matrix.diag(-4.3114632450165346E-5, 0.002584684585933522));

                    c.maxAchievableForwardVelocity.set(47.2218997369381);
                    c.maxAchievableStrafeVelocity.set(39.5097540497669);
                    c.naturalForwardDeceleration.set(37.04412874221226);
                        c.naturalStrafeDeceleration.set(74.50382751773564);
                }
        );
        public static Follower create(HardwareMap h) {
            return new Follower(
                    new PinpointLocalizer(h, localizerConfig),
                    new Mecanum(h, drivetrainConfig),
                    new Foresight(foresightConfig)
            );
        }
    }