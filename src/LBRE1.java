import com.kuka.common.ITaggable;
import com.kuka.roboticsAPI.controllerModel.sunrise.ISafetyState;
import com.kuka.roboticsAPI.deviceModel.IOperationModeProvider;
import com.kuka.roboticsAPI.deviceModel.JointEnum;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Workpiece;
import com.kuka.roboticsAPI.sensorModel.*;

public interface LBRE1 extends ITaggable, IOperationModeProvider {
    SensorForMeasuredTorque getSensorForMeasuredTorque();

    TorqueSensorData getMeasuredTorque();

    SensorForExternalTorque getSensorForExternalTorque();

    TorqueSensorData getExternalTorque();

    SensorForExternalForce getSensorForExternalForce();

    ForceSensorData getExternalForceTorque(AbstractFrame measureFrame);

    ForceSensorData getExternalForceTorque(AbstractFrame measureFrame, AbstractFrame orientationFrame);

    ISafetyState getSafetyState();

    double[] getTorqueSensorLimits();

    boolean checkTorqueSensor(JointEnum joint);

    void setESMState(String var1);

    void setSafetyWorkpiece(Workpiece var1);

    double getAlphaAngle(JointPosition joints);

    JointPosition getAlphaInverseKinematicFromReference(AbstractFrame frame, JointPosition correspondingJoints, double alphaAngle);
}
