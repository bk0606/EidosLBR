package com.eidos.simulatableAPI;

import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.math.ITransformation;
import com.kuka.roboticsAPI.motionModel.IMotion;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.motionModel.IMotionContainerListener;
import com.sun.istack.internal.NotNull;

public interface SimulatableRobot {
    IMotionContainer        moveAsync(IMotion motion);
    IMotionContainer        moveAsync(IMotion motion, IMotionContainerListener containerListener);

    IMotionContainer        move(IMotion motion) throws Exception;
    IMotionContainer        move(IMotion motion, IMotionContainerListener containerListener) throws Exception;

    JointPosition           getCurrentJointPosition() throws Exception;
    Frame                   getCurrentCartesianPosition(ObjectFrame frameOnFlange) throws Exception;
    Frame                   getCurrentCartesianPosition(ObjectFrame frameOnFlange, AbstractFrame reference) throws Exception;

    boolean                 isReadyToMove();

    void                    changeFramePosition(@NotNull ObjectFrame frame, @NotNull ITransformation transformation);
    ObjectFrame             getFrame(String path);

    // TODO: changeFrameOrientation - may be via getFrame -> transformFrame -> setFrame
}
