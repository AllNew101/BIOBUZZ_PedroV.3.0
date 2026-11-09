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
            c.xPodOffset.set(3.4165612168199435);
            c.yPodOffset.set(-5.8339931458000125);
            c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
            c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
            c.globalDistanceUnit.set(DistanceUnit.INCH);
            c.offsetUnits.set(DistanceUnit.INCH);
        });
        public static ForesightConfig foresightConfig = new ForesightConfig(
                c -> {
                    Controller primaryTranslationalForward = Controller.proportional(0.3952252228443207);
                    Controller secondaryTranslationalForward = Controller.proportional(0.14602515922852452);
                    Controller primaryTranslationalLateral = Controller.proportional(0.6538867354772194);
                    Controller secondaryTranslationalLateral = Controller.proportional(0.241593676583471);

                    c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                    c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                    c.coast.set(Controller.proportionalFeedforward(0.012827858383443103));
                    c.brake.set(Controller.proportionalFeedforward(0.010903679625926636));

                    c.headingFeedback.set(Controller.proportional(6.399521629519623));
                    c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06308490401934722, 0.00558854156295802));

                    c.linearBrakeCoefficients.set(Matrix.diag(0.10677919479531495, 0.032888434927740455));
                    c.quadraticBrakeCoefficients.set(Matrix.diag(0.0012331762817217875, 0.0023390601363863185));

                    c.maxAchievableForwardVelocity.set(66.32102151631628);
                    c.maxAchievableStrafeVelocity.set(52.970461971744406);
                    c.naturalForwardDeceleration.set(33.67809895967148);
                    c.naturalStrafeDeceleration.set(72.5212005539232);
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