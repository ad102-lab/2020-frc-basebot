package org.team1619.behavior;

import org.uacr.models.behavior.Behavior;
import org.uacr.shared.abstractions.InputValues;
import org.uacr.shared.abstractions.OutputValues;
import org.uacr.shared.abstractions.RobotConfiguration;
import org.uacr.utilities.Config;
import org.uacr.utilities.Timer;
import org.uacr.utilities.logging.LogManager;
import org.uacr.utilities.logging.Logger;

import java.util.Set;

/**
 * Example behavior to copy for other behaviors
 */

public class Robot_Configuration_Example implements Behavior {

	private static final Logger sLogger = LogManager.getLogger(Robot_Configuration_Example.class);
	private static final Set<String> sSubsystems = Set.of("ss_robot_configuration_example");

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;
	private final String fXAxis;
	private final String fYAxis;
	private final String mStateName;


	public Robot_Configuration_Example(InputValues inputValues, OutputValues outputValues, Config config, RobotConfiguration robotConfiguration) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;
		fXAxis = robotConfiguration.getString("global_robot_configuration_example", "x");
		fYAxis = robotConfiguration.getString("global_robot_configuration_example", "y");

		mStateName = "Drivetrain";

	}

	@Override
	public void initialize(String mStateName, Config config) {
		sLogger.debug("Entering state {}", mStateName);

	}

	@Override
	public void update() {
		double xAxis = fSharedInputValues.getNumeric(fXAxis);
		double yAxis = fSharedInputValues.getNumeric(fYAxis);

		double leftMotorSpeed = yAxis + xAxis;
		double rightMotorSpeed = yAxis - xAxis;

		if (leftMotorSpeed > 1) {
			rightMotorSpeed = rightMotorSpeed - (leftMotorSpeed - 1);
			leftMotorSpeed = 1;
		} else if (leftMotorSpeed < -1) {
			rightMotorSpeed = rightMotorSpeed - (1 + leftMotorSpeed);
			leftMotorSpeed = -1;
		} else if (rightMotorSpeed > 1) {
			leftMotorSpeed = leftMotorSpeed - (rightMotorSpeed - 1);
			rightMotorSpeed = 1;
		} else if (rightMotorSpeed < -1) {
			leftMotorSpeed = leftMotorSpeed - (1 + rightMotorSpeed);
			rightMotorSpeed = -1;
		}

		fSharedOutputValues.setNumeric("opn_drivetrain_left", "percent", leftMotorSpeed);
		fSharedOutputValues.setNumeric("opn_drivetrain_right", "percent", rightMotorSpeed);

	}

	@Override
	public void dispose() {
		sLogger.trace("Leaving state {}", mStateName);
		fSharedOutputValues.setNumeric("opn_drivetrain_left", "percent", 0.0);
		fSharedOutputValues.setNumeric("opn_drivetrain_right", "percent", 0.0);

	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public Set<String> getSubsystems() {
		return sSubsystems;
	}
}