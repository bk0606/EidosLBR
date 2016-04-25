package com.eidos.simulatableAPI;

import com.kuka.roboticsAPI.commandModel.ICommandRuntimeData;
import com.kuka.roboticsAPI.commandModel.IControllerCommand;
import com.kuka.roboticsAPI.conditionModel.ICondition;
import com.kuka.roboticsAPI.executionModel.ExecutionState;
import com.kuka.roboticsAPI.executionModel.IFiredConditionInfo;
import com.kuka.roboticsAPI.motionModel.IMotion;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// TODO:
public class SimulatorMotion implements IMotionContainer {
    IMotion currentMtion;

    @Override
    public IMotion getCurrentMotion() {
        return currentMtion;
    }

    @Override
    public IMotion getLastExecutedMotion() {
        return null;
    }

    @Override
    public void append(IMotion iMotion) {

    }

    @Override
    public void cancel() {

    }

    @Override
    public void await() {

    }

    @Override
    public void await(long l, TimeUnit timeUnit) throws TimeoutException {

    }

    @Override
    public void validate() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean hasError() {
        return false;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public boolean hasFired(ICondition iCondition) {
        return false;
    }

    @Override
    public IFiredConditionInfo getFiredBreakConditionInfo() {
        return null;
    }

    @Override
    public ICommandRuntimeData getRuntimeData() {
        return null;
    }

    @Override
    public IControllerCommand getCommand() {
        return null;
    }

    @Override
    public ExecutionState getState() {
        return null;
    }
}
