package de.buehmann.jubula.api.runner.internal;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.buehmann.jubula.api.runner.SwingRunnerAUT;
import de.buehmann.jubula.api.runner.annotation.ClassAUT;

public class RunnerAUTConfigurationTest {

	@ClassAUT(RunnerAUTConfiguration.class)
	private static SwingRunnerAUT aut;

	@Test
	public void testRunnerAUTConfigurationWithClass() {
		final RunnerAUTConfiguration config = new RunnerAUTConfiguration(RunnerAUTConfigurationTest.class);
		final String filename = config.getWorkingDir() + "/"
				+ RunnerAUTConfiguration.class.getCanonicalName().replace('.', '/') + ".class";
		final File file = new File(filename);
		Assert.assertTrue("File does not exist: " + file.toString(), file.exists());
	}

}
