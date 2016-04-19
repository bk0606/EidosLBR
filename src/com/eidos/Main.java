package com.eidos;

import com.kuka.roboticsAPI.RoboticsAPIContext;
import com.kuka.roboticsAPI.RoboticsAPILazyDeviceContext;
import com.kuka.roboticsAPI.applicationModel.IApplicationData;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import com.kuka.roboticsAPI.commandModel.IControllerCommand;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.controllerModel.mock.MockController;
import com.kuka.roboticsAPI.controllerModel.sunrise.ISPRHook;
import com.kuka.roboticsAPI.controllerModel.sunrise.SunriseController;
import com.kuka.roboticsAPI.controllerModel.sunrise.SunriseExecutionService;
import com.kuka.roboticsAPI.controllerModel.sunrise.SunriseLBR;
import com.kuka.roboticsAPI.controllerModel.sunrise.api.Assignment;
import com.kuka.roboticsAPI.controllerModel.sunrise.api.SPR;
import com.kuka.roboticsAPI.controllerModel.sunrise.predefinedCompounds.AxisValidatorModule;
import com.kuka.roboticsAPI.deviceModel.Device;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.ITransformationProvider;
import com.kuka.roboticsAPI.geometricModel.Workpiece;
import com.kuka.roboticsAPI.geometricModel.math.Transformation;
import com.kuka.roboticsAPI.motionModel.CartesianPTP;
import com.kuka.roboticsAPI.motionModel.PTP;
import com.kuka.roboticsAPI.motionModel.PTPHome;
import java.io.*;
import java.net.*;
import com.kuka.roboticsAPI.SimulatableRobot;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;


public class Main {

    public static void main(String[] args) {
        System.out.println("com.eidos.Main main");
        /*File f = new File("D:\\Dropbox\\Code\\Java\\LBR\\src\\com\\eidos\\RoboticsAPI.config.xml");
        try {
            System.out.print(f.length());
            System.out.print(Files.readAllLines(Paths.get("D:\\Dropbox\\Code\\Java\\LBR\\src\\com\\eidos\\RoboticsAPI.config.xml"), Charset.forName("ISO-8859-1")));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        RoboticsApp m = new RoboticsApp(SimulatableRoboticsAPIMode.Simulation);
    }
}

class RoboticsApp extends SimulatableRoboticsAPIApplication {
    private Controller kuka_Sunrise_Cabinet_1;
    private SunriseExecutionService serv;
    // private LBR iiwa7R8001;
    private SimulatableLBR iiwa7R8001;
    private LBR robot;

    @Override
    // This will call before robot execution
    public void initialize() {
        kuka_Sunrise_Cabinet_1 = getController("KUKA_Sunrise_Cabinet_1");
        robot = (LBR) getDevice(kuka_Sunrise_Cabinet_1, "LBR_iiwa_7_R800_1");
        /*serv = (SunriseExecutionService)kuka_Sunrise_Cabinet_1.getExecutionService();
        AxisValidatorModule axV = new AxisValidatorModule("");*/
    }

    @Override
    // This will call before simulator execution
    public void initializeSimulation() {
        robot = new SimulatableRobot("KUKA_Sunrise_Cabinet_1");
    }

    @Override
    public void run() {
        robot.move(ptp(10.0D));
    }

    public RoboticsApp(SimulatableRoboticsAPIMode mode) {
        super(mode);
    }
}

class Connector {
    private Socket socket;
    private PrintWriter outWriter;
    private BufferedReader inReader;

    public void sendPTPMove(Transformation worldTransform) {
        // TODO: Implement (inverse kinematics)
        outWriter.write("<?xml version = '1.0'?><methodCall><methodName>UI.Log</methodName><params><param><value><string>Got it!</string></value></param></params></methodCall>");
        outWriter.flush();
    }

    public void sendPTPMove(JointPosition joints) {
        outWriter.write("<?xml version = '1.0'?><methodCall><methodName>UI.Log</methodName><params><param><value><string>" +
                joints.get(0) + "</string></value></param></params></methodCall>");
        outWriter.flush();
    }

    public void dispose() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connector(String hostName, int portNumber) {
        try {
            socket = new Socket(hostName, portNumber);
            outWriter = new PrintWriter(socket.getOutputStream(), true);
            inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

class SimulatableLBR {
    private AbstractFrame homePosition;
    private int jointsCount = -1;
    private Connector connector = null;

    public void move(PTPHome motion) {
        // TODO: Parse motion params from `motion`
        connector.sendPTPMove(homePosition.transformationFromWorld());
    }

    public void move(PTP motion) {
        connector.sendPTPMove(motion.getDestination());
    }

    public void move(CartesianPTP motion) {
        connector.sendPTPMove(motion.getDestination().transformationFromWorld());
    }

    public int getJointsCount()
    {
        return jointsCount;
    }

    public void setHomePosition(AbstractFrame homePosition) {
        this.homePosition = homePosition;
    }

    public void dispose() {
        connector.dispose();
    }

    public SimulatableLBR(int jointsCount) {
        this.jointsCount = jointsCount;
        this.connector = new Connector("localhost", 6001);
    }
}

enum SimulatableRoboticsAPIMode {
    Simulation,
    DeviceExecution
}

abstract class SimulatableRoboticsAPIApplication {
    private Optional<RoboticsAPIApplication> app;

    public abstract void run();

    public void initialize() {
    }

    public void initializeSimulation() {

    }

    public void dispose() {
    }

    // TODO: Think about
    public void runApplication() throws IllegalAccessError {
        if (app.isPresent()) app.get().runApplication();
        else {
            this.initializeSimulation();
            this.run();
        }
    }

    public IApplicationData getApplicationData() throws IllegalAccessError {
        if (app.isPresent()) return app.get().getApplicationData();
        else throw new IllegalAccessError(getAccessErrorMessage("getApplicationData"));
    }

    public Controller getController(String name) throws IllegalAccessError {
        if (app.isPresent()) return app.get().getController(name);
        else throw new IllegalAccessError(getAccessErrorMessage("getController"));
    }
    public Device getDevice(Controller controller, String name) throws IllegalAccessError {
        if (app.isPresent()) return app.get().getDevice(controller, name);
        else throw new IllegalAccessError(getAccessErrorMessage("getDevice"));
    }

    public SimulatableRoboticsAPIApplication(SimulatableRoboticsAPIMode mode) {
        SimulatableRoboticsAPIApplication self = this;
        switch (mode) {
            case DeviceExecution:
                app = Optional.of(new RoboticsAPIApplication() {
                    @Override
                    public void run() {
                        self.run();
                    }
                    @Override
                    public void initialize() {
                        self.initialize();
                    }
                    @Override
                    public void dispose() {
                        self.dispose();
                    }
                });
                break;
            case Simulation:
                app = Optional.empty();
                runApplication();
                break;
        }
    }

    public final String getAccessErrorMessage(String methodName) {
        return "Cannot access " + methodName + " `RoboticsAPIApplication` method inReader simulating mode";
    }

    public SimulatableRoboticsAPIApplication() {
    }
}
