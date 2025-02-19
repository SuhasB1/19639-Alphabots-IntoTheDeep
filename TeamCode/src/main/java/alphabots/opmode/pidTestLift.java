/* Copyright (c) 2021 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package alphabots.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

@Config
@TeleOp(name="PID_Test_Lift", group="z")
public class pidTestLift extends OpMode {
    public DcMotor rightLift, leftLift;
    public static boolean manual = false;
    public static int posLeft, posRight;
    public PIDController liftPID;
    public static int target;
    public static double manualLeftPower = 0, manualRightPower = 0;
    public static double maxRetractPowerPositiveVal = 0.8;
    public static double p = 0.012, i = 0, d = 0.0001;
    public static double f = 0.15;
    private Gamepad currentGamepad2 = new Gamepad(), previousGamepad2 = new Gamepad();

    @Override
    public void init() {
        posLeft = 0;
        posRight = 0;
        rightLift = hardwareMap.get(DcMotor.class, "bottomLift");
        leftLift = hardwareMap.get(DcMotor.class, "topLift");

        rightLift.setDirection(DcMotor.Direction.FORWARD);
        leftLift.setDirection(DcMotor.Direction.REVERSE);
        leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        liftPID = new PIDController(p, i, d);
        telemetry.setMsTransmissionInterval(50);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }

    @Override
    public void loop() {

        updatePIDF();

        telemetry.addData("manual", manual);
        telemetry.addData("left lift pos", getLeftPos());
        telemetry.addData("right lift pos", getRightPos());
        telemetry.addData("target", target);
        telemetry.addData("P:", p);
        telemetry.addData("I:", i);
        telemetry.addData("D:", d);
        telemetry.addData("F:", f);
        telemetry.update();
    }

    public void updatePIDF(){
        if (!manual) {
            liftPID.setPID(p,i,d);
            rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            double pid = liftPID.calculate(getLeftPos(), target);
            double ticks_in_degrees = 537.7 / 360.0;
            double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;
            double power = pid + ff;
            power = Math.max(-maxRetractPowerPositiveVal,power);

            rightLift.setPower(power);
            leftLift.setPower(power);

            telemetry.addData("left lift pos", getLeftPos());
            telemetry.addData("right lift pos", getRightPos());
            telemetry.addData("lift target", target);
        }
        else {
            leftLift.setPower(manualLeftPower);
            rightLift.setPower(manualRightPower);
        }
    }

    //Util

    public void setTarget(int b) {
        target = b;
    }

    public void addToTarget(int b) {
        target += b;
    }

    public int getRightPos() {
        posRight = rightLift.getCurrentPosition();
        return rightLift.getCurrentPosition();
    }
    public int getLeftPos() {
        posLeft = leftLift.getCurrentPosition();
        return leftLift.getCurrentPosition();
    }
}
