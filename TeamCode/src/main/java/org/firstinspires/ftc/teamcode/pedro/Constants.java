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
            c.frontLeftName.set("Front_L");
            c.frontRightName.set("Front_R");
            c.backLeftName.set("Back_L");
            c.backRightName.set("Back_R");
            c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
            c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
            c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
            c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
        });
        public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
            c.name.set("odo");
            c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);
            c.xPodOffset.set(2.317064540592704);
            c.yPodOffset.set(-4.304050836037463);
            c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
            c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
            c.globalDistanceUnit.set(DistanceUnit.INCH);
            c.offsetUnits.set(DistanceUnit.INCH);
        });
        public static ForesightConfig foresightConfig = new ForesightConfig(
                c -> {
                    Controller primaryTranslationalForward = Controller.proportional(0.36483449402363144);
                    Controller secondaryTranslationalForward = Controller.proportional(0.13479659698450974);
                    Controller primaryTranslationalLateral = Controller.proportional(0.5257278650606433);
                    Controller secondaryTranslationalLateral = Controller.proportional(0.19424239843263297);

                    c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                    c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                    c.coast.set(Controller.proportionalFeedforward(0.012698358357840455));
                    c.brake.set(Controller.proportionalFeedforward(0.010793604604164387));

                    c.headingFeedback.set(Controller.proportional(5.397982387467192));
                    c.headingBrakeCoefficients.set(Vector2D.cartesian(0.0596638586727062, 0.005481024170487944));

                    c.linearBrakeCoefficients.set(Matrix.diag(0.13589836162049196, 0.07326582931024077));
                    c.quadraticBrakeCoefficients.set(Matrix.diag(6.37036837122074E-4, 0.0014683500370562886));

                    c.maxAchievableForwardVelocity.set(49.45617871233827);
                    c.maxAchievableStrafeVelocity.set(43.276584412385134);
                    c.naturalForwardDeceleration.set(35.873808724842924);
                    c.naturalStrafeDeceleration.set(69.05150542710044);
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