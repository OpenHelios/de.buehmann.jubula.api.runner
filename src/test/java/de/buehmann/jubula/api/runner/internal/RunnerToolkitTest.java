package de.buehmann.jubula.api.runner.internal;

import org.eclipse.jubula.tools.internal.constants.CommandConstants;
import org.junit.Assert;
import org.junit.Test;

public class RunnerToolkitTest {

	@Test
	public void testGetToolkitId() {
		Assert.assertEquals(CommandConstants.SWING_TOOLKIT, RunnerToolkit.SWING.getToolkitId());
		Assert.assertEquals(CommandConstants.JAVAFX_TOOLKIT, RunnerToolkit.JAVA_FX.getToolkitId());
	}

	@Test
	public void testGetToolkitComponentsClassName() {
		Assert.assertEquals("org.eclipse.jubula.toolkit.swing.SwingComponents", RunnerToolkit.SWING.getToolkitComponentsClassName());
		Assert.assertEquals("org.eclipse.jubula.toolkit.javafx.JavaFXComponents", RunnerToolkit.JAVA_FX.getToolkitComponentsClassName());
	}

	@Test
	public void testGetByToolkitId() {
		Assert.assertEquals(RunnerToolkit.SWING, RunnerToolkit.getByToolkitId(CommandConstants.SWING_TOOLKIT));
	}

}
