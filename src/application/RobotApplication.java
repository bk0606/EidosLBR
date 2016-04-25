package application;

import com.eidos.simulatableAPI.*;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;

public class RobotApplication extends SimulatableRoboticsApp {
	private Controller kuka_Sunrise_Cabinet_1;
	private SimulatableRobot robot;

	@Override
	public void initialize() {
		kuka_Sunrise_Cabinet_1 = getController("KUKA_Sunrise_Cabinet_1");
		robot = new LBRRobot((LBR) getDevice(kuka_Sunrise_Cabinet_1, "LBR_iiwa_14_R820_1"));
	}

	@Override
	public void initializeSimulation() {
		robot = new LBRSimulator("localhost", 6001); // TODO: via app configs
	}

	@Override
	public void run() {
		try {
			robot.move(ptp(2.0D));/*
			robot.moveAsync(ptp(0.0, 1.5D));
			robot.moveAsync(ptp(-1.3, 0.0, 1.4D));*/
		} catch (Exception e) {
			System.err.println("Error while `run` method execution: `" + e.getMessage() + "`");
		}
	}

	public RobotApplication(RoboticsAppMode mode) {
		super(mode);
	}

	public static void main(String[] args) {
		RobotApplication app = new RobotApplication(RoboticsAppMode.Simulation);
		app.runApplication();
	}
}
