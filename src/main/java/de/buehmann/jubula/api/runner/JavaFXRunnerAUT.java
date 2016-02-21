package de.buehmann.jubula.api.runner;

import org.eclipse.jubula.tools.internal.constants.CommandConstants;

import de.buehmann.jubula.api.runner.internal.ConcreteRunnerAUT;

public class JavaFXRunnerAUT extends ConcreteRunnerAUT {

	@Override
	public String getToolkitId() {
		return CommandConstants.JAVAFX_TOOLKIT;
	}

}
