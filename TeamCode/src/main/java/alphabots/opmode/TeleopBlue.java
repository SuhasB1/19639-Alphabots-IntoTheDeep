package alphabots.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import alphabots.config.runmodes.Teleop;
import alphabots.config.util.VariableStorage;

@TeleOp(name="TeleBlue", group="A")
public class TeleopBlue extends OpMode {

    private Teleop teleop;
    @Override
    public void init() {
        VariableStorage.currentTeam = VariableStorage.Team.BLUE;
        teleop = new Teleop(hardwareMap, telemetry, false, gamepad1, gamepad2);
        teleop.init();
    }

    @Override
    public void start(){
        teleop.start();
    }

    @Override
    public void loop() {
        teleop.update();
        telemetry.update();

    }


}