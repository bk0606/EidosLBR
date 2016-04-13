package com.eidos.roboticsAPI;

import com.kuka.roboticsAPI.RoboticsAPIContext;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.controllerModel.ExecutionService;
import com.kuka.roboticsAPI.controllerModel.RequestService;
import com.kuka.roboticsAPI.controllerModel.sunrise.ISafetyState;
import com.kuka.roboticsAPI.deviceModel.JointEnum;
import com.kuka.roboticsAPI.deviceModel.JointPosition;
import com.kuka.roboticsAPI.deviceModel.Robot;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Workpiece;
import com.kuka.roboticsAPI.requestModel.GetAlphaAngleRequest;
import com.kuka.roboticsAPI.requestModel.GetAlphaInverseKinematicsFromReferenceRequest;
import com.kuka.roboticsAPI.requestModel.GetTorqueStateRequest;
import com.kuka.roboticsAPI.sensorModel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.kuka.roboticsAPI.controlle;

/*public class Controller extends com.kuka.roboticsAPI.controllerModel.Controller {

	protected Controller(RoboticsAPIContext context, String name) {
		super(context, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ExecutionService createExecutionServiceInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RequestService createRequestServiceInstance() {
		// TODO Auto-generated method stub
		return null;
	}

}*/
