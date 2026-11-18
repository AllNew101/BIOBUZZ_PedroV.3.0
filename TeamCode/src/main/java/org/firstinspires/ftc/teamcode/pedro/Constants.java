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
            c.xPodOffset.set(2.521897864154005);
            c.yPodOffset.set(-5.59385104442206);
            c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
            c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
            c.globalDistanceUnit.set(DistanceUnit.INCH);
            c.offsetUnits.set(DistanceUnit.INCH);
        });
        public static ForesightConfig foresightConfig = new ForesightConfig(
                c -> {
                    Controller primaryTranslationalForward = Controller.proportional(0.38397119566263893);
                    Controller secondaryTranslationalForward = Controller.proportional(0.14186709689803773);
                    Controller primaryTranslationalLateral = Controller.proportional(0.5134311031677143);
                    Controller secondaryTranslationalLateral = Controller.proportional(0.18969907348872492);

                    c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                    c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                    c.coast.set(Controller.proportionalFeedforward(0.012164850892814721));
                    c.brake.set(Controller.proportionalFeedforward(0.010340123258892513));

                    c.headingFeedback.set(Controller.proportional(6.2005440995297585));
                    c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05985747855724296, 0.005765026692409443));

                    c.linearBrakeCoefficients.set(Matrix.diag(0.09539871471785051, 0.06777206268225516));
                    c.quadraticBrakeCoefficients.set(Matrix.diag(0.0016520056779870968, 0.001721135536301411));

                    c.maxAchievableForwardVelocity.set(51.17363993977509);
                    c.maxAchievableStrafeVelocity.set(45.45570069620577);
                    c.naturalForwardDeceleration.set(32.013420197683516);
                    c.naturalStrafeDeceleration.set(68.56519861945542);
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