package com.eidos.simulatableAPI;

import com.kuka.roboticsAPI.controllerModel.sunrise.SunriseLBR;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.math.ITransformation;
import com.kuka.roboticsAPI.motionModel.IMotion;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.IMotionContainerListener;
import com.sun.istack.internal.NotNull;

// TODO: Rename LBRRobot
public class LBRRobot implements SimulatableRobot {
    protected LBR delegatingLBR;

    // TODO: Research: Mb delegatingLBR.getInverseKinematic()

    @Override
    public IMotionContainer moveAsync(IMotion motion) {
        return delegatingLBR.moveAsync(motion);
    }

    @Override
    public IMotionContainer moveAsync(IMotion motion, IMotionContainerListener containerListener) {
        return delegatingLBR.moveAsync(motion, containerListener);
    }

    @Override
    public IMotionContainer move(IMotion motion) {
        return delegatingLBR.move(motion);
    }

    @Override
    public IMotionContainer move(IMotion motion, IMotionContainerListener containerListener) {
        return delegatingLBR.move(motion, containerListener);
    }

    @Override
    public JointPosition getCurrentJointPosition() {
        return delegatingLBR.getCurrentJointPosition();
    }

    @Override
    public Frame getCurrentCartesianPosition(ObjectFrame frameOnFlange) {
        return delegatingLBR.getCurrentCartesianPosition(frameOnFlange);
    }

    @Override
    public Frame getCurrentCartesianPosition(ObjectFrame frameOnFlange, AbstractFrame reference) {
        return delegatingLBR.getCurrentCartesianPosition(frameOnFlange, reference);
    }

    @Override
    public boolean isReadyToMove() {
        return delegatingLBR.isReadyToMove();
    }

    @Override
    public void changeFramePosition(@NotNull ObjectFrame frame, @NotNull ITransformation transformation) {
        delegatingLBR.changeFramePosition(frame, transformation);
    }

    @Override
    public ObjectFrame getFrame(String path) {
        return delegatingLBR.getFrame(path);
    }

    public LBRRobot(LBR robot) {
        System.out.println("robot: " + robot);
        delegatingLBR = robot;
    }
}
