package com.kuka.roboticsAPI

import java.io.File
import java.net.URL

import com.kuka.common.ParameterSet
import com.kuka.roboticsAPI.controllerModel.sunrise.AbstractSunriseController
import com.kuka.roboticsAPI.controllerModel.sunrise.api.SPR
import com.kuka.roboticsAPI.controllerModel.{Controller, ExecutionService, RequestService}
import com.kuka.roboticsAPI.deviceModel.LBR
import com.kuka.roboticsAPI.geometricModel.{ITransformationProvider, Workpiece}
import com.kuka.roboticsAPI.motionModel.PTP

class SimulatableRobot(name: String)
  extends LBR(
    new FakeController(
      RoboticsAPIContext.createFromFile(new File("src\\RoboticsAPI.config.xml")),
      name),
    name) {

  def move: PTP => Unit = ptp => {
    println("ptp.getDestination.get(0): " + ptp.getDestination.get(0))
  }

  protected def initGmsSensorLimits() {
  }

  def setESMState(s: String) {
  }

  def setSafetyWorkpiece(workpiece: Workpiece) {
  }

  protected def createBaseFlangeTransformationProvider(): ITransformationProvider = {
    null
  }

}

class FakeController(val context: RoboticsAPIContext, val name: String) extends AbstractSunriseController(context, name) {
  def getApplicationStateSPR: SPR = {
    null
  }

  protected def createRequestServiceInstance: RequestService = {
    null
  }

  protected def createExecutionServiceInstance: ExecutionService = {
    null
  }
}

class FakeContext extends RoboticsAPIContext(classOf[RoboticsAPIContext], new URL("src\\RoboticsAPI.config.xml")) {
  def createController: ParameterSet => Controller = p => {
    new FakeController(
      RoboticsAPIContext.createFromFile(new File("C:\\Users\\a.bikeev\\SunriseWorkspace\\Sandbox\\src\\RoboticsAPI.config.xml")),
      "Fake context")
  }
}