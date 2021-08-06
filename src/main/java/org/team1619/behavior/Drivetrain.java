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
 * A tank drive behavior
 */

public class Drivetrain implements Behavior {

	private static final Logger sLogger = LogManager.getLogger(Drivetrain.class);
	private static final Set<String> sSubsystems = Set.of("ss_drivetrain");

	private final InputValues fSharedInputValues;
	private final OutputValues fSharedOutputValues;
	private String fYAxis;
	private String fXAxis;
	private Timer mTimer;

	private int mConfigurationValue;

	public Drivetrain(InputValues inputValues, OutputValues outputValues, Config config, RobotConfiguration robotConfiguration) {
		fSharedInputValues = inputValues;
		fSharedOutputValues = outputValues;
		fYAxis = robotConfiguration.getString("global_drivetrain", "y");
		fXAxis = robotConfiguration.getString("global_drivetrain", "x");

		mConfigurationValue = 0;
		mTimer = new Timer();
	}

	@Override
	public void initialize(String stateName, Config config) {
		sLogger.debug("Entering state {}", stateName);

		mConfigurationValue = config.getInt("config_key", 0);
		mTimer.start(mConfigurationValue);
	}

	@Override
	public void update() {
		double leftMotorSpeed = fSharedInputValues.getNumeric(fYAxis);
		double rightMotorSpeed = fSharedInputValues.getNumeric(fXAxis);

		fSharedOutputValues.setNumeric("opn_drivetrain_left", "percent", leftMotorSpeed);
		fSharedOutputValues.setNumeric("opn_drivetrain_right", "percent", rightMotorSpeed);
	}

	@Override
	public void dispose() {
		fSharedOutputValues.setNumeric("opn_drivetrain_left", "percent", 0);
		fSharedOutputValues.setNumeric("opn_drivetrain_right", "percent", 0);
	}

	@Override
	public boolean isDone() {
		return mTimer.isDone();
	}

	@Override
	public Set<String> getSubsystems() {
		return sSubsystems;
	}
}