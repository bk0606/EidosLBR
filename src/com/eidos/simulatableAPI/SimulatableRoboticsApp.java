package com.eidos.simulatableAPI;

import com.kuka.roboticsAPI.applicationModel.IApplicationData;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.Device;
import java.util.Optional;

// TODO: Remove optional with null
public abstract class SimulatableRoboticsApp {
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
            try {
                this.initializeSimulation();
                this.run();
            } catch (Exception ex) {
                System.err.println("Error occurs while robot simulation: `" + ex.getMessage() + "`");
            } finally {
                this.dispose();
            }
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

    public SimulatableRoboticsApp(RoboticsAppMode mode) {
        SimulatableRoboticsApp self = this;
        switch (mode) {
            case DeviceExecution:
                app = Optional.of(new RoboticsAPIApplication() {
                    @Override
                    public void run() {
                        self.run();
                    }
                    @Override
                    public void initialize() {
                        System.out.println("Initizlise : " + self);
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
                break;
        }
    }

    public final String getAccessErrorMessage(String methodName) {
        return "Cannot access " + methodName + " `RoboticsAPIApplication` method inReader simulating mode";
    }

    // TODO: Rm
    public SimulatableRoboticsApp() {
    }
}