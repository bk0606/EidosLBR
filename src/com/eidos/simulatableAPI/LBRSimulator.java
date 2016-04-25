package com.eidos.simulatableAPI;

import com.eidos.simulatableAPI.utils.Mathf;
import com.kuka.roboticsAPI.commandModel.ICommandRuntimeData;
import com.kuka.roboticsAPI.commandModel.IControllerCommand;
import com.kuka.roboticsAPI.conditionModel.ICondition;
import com.kuka.roboticsAPI.controllerModel.sunrise.SunriseMotionContainer;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.PoseInformation;
import com.kuka.roboticsAPI.executionModel.ExecutionState;
import com.kuka.roboticsAPI.executionModel.ICommandContainer;
import com.kuka.roboticsAPI.executionModel.IFiredConditionInfo;
import com.kuka.roboticsAPI.geometricModel.*;
import com.kuka.roboticsAPI.geometricModel.math.ITransformation;
import com.kuka.roboticsAPI.motionModel.*;
import com.sun.istack.internal.NotNull;
import java.nio.channels.InterruptedByTimeoutException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LBRSimulator implements SimulatableRobot {
    private Simulator simulator;
    private Long timeoutMs = 1000L; // milliseconds
    private ObjectFrame hardcodedFrame = new ObjectFrame("", null, new SceneGraphObject() {
        @Override
        protected void changeParent(ObjectFrame objectFrame) {

        }
    }, new ITransformationProvider() {
        @Override
        public boolean isStatic() {
            return true;
        }

        @Override
        public PoseInformation getPoseInformation() {
            return null;
        }

        @Override
        public ITransformation getTransformation() {
            return null;
        }

        @Override
        public String getUniqueName() {
            return null;
        }
    });

    @Override
    public IMotionContainer moveAsync(IMotion motion) {
        if (motion instanceof PTP) {
            double jointValues[] = ((PTP) motion).getDestination().getInternalArray();
            float fltDegValues[] = new float[jointValues.length];
            for (int i = 0, len = jointValues.length; i < len; i++) {
                fltDegValues[i] = ((float) jointValues[i]) * Mathf.RAD_TO_DEG;
            }
            simulator.moveJoints(fltDegValues);
        }
        else if (motion instanceof LIN) {
            // TODO: Lin motion
            throw new UnsupportedOperationException("Lin motion is not supported in simulator yet.");
        }
        else if (motion instanceof CIRC) {
            // TODO: CIRC motion
            throw new UnsupportedOperationException("CIRC motion is not supported in simulator yet.");
        }
        else if (motion instanceof PTPHome) {
            // TODO: Acquire home frame
            throw new UnsupportedOperationException("PTPHome is not supported in simulator yet.");
        }
        return null;
    }

    @Override
    public IMotionContainer moveAsync(IMotion motion, IMotionContainerListener containerListener) {
        System.out.println("moveAsync no support IMotionContainerListener arg for now in simulator");
        return moveAsync(motion);
    }

    @Override
    public IMotionContainer move(IMotion motion) throws Exception {
        IMotionContainer container = moveAsync(motion);
        Long pastTime = 0L;
        Long sleepTime = 50L;
        while (!simulator.isCommandHandled()) {
            Thread.sleep(sleepTime);
            pastTime += sleepTime;
            if (pastTime > timeoutMs) throw new InterruptedByTimeoutException();
        }
        return container;
    }

    @Override
    public IMotionContainer move(IMotion motion, IMotionContainerListener containerListener) throws Exception {
        System.out.println("move no support IMotionContainerListener arg for now in simulator");
        return move(motion);
    }

    @Override
    public JointPosition getCurrentJointPosition() throws Exception {
        return new JointPosition(simulator.getJointsValues());
    }

    @Override
    public Frame getCurrentCartesianPosition(ObjectFrame frameOnFlange) throws Exception {
        return simulator.getFlangePosition();
    }

    @Override
    public Frame getCurrentCartesianPosition(ObjectFrame frameOnFlange, AbstractFrame reference) throws Exception {
        Frame f = simulator.getFlangePosition();
        f.transformationTo(reference);
        return f;
    }

    @Override
    public boolean isReadyToMove() {
        return simulator.isReady();
    }

    @Override
    public void changeFramePosition(@NotNull ObjectFrame frame, @NotNull ITransformation transformation) {
        // TODO: Find out wtf is this :)
        throw new UnsupportedOperationException("changeFramePosition is not supported in simulator yet.");
    }

    @Override
    public ObjectFrame getFrame(String path) {
        return hardcodedFrame;
    }

    public LBRSimulator(String hostName, int port) {
        simulator = new Simulator(hostName, port);
    }
}
