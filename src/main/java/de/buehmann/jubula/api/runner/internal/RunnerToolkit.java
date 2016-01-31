package de.buehmann.jubula.api.runner.internal;

import org.eclipse.jubula.toolkit.ToolkitInfo;
import org.eclipse.jubula.tools.internal.constants.CommandConstants;

public enum RunnerToolkit {

	SWING(CommandConstants.SWING_TOOLKIT, "Swing"),

	JAVA_FX(CommandConstants.JAVAFX_TOOLKIT, "JavaFX");

	private String toolkitId;

	private String toolkitComponentsClassName;

	private RunnerToolkit(final String toolkitId, final String name) {
		this.toolkitId = toolkitId;
		this.toolkitComponentsClassName = createToolkitComponentClassName(name);
	}

	private static String createToolkitComponentClassName(final String name) {
		final StringBuilder sb = new StringBuilder(ToolkitInfo.class.getPackage().getName());
		sb.append('.').append(name.toLowerCase()).append('.').append(name).append("Components");
		return sb.toString();
	}

	/**
	 * @return The toolkit ID, e.g. {@link CommandConstants#SWING_TOOLKIT}.
	 */
	public String getToolkitId() {
		return toolkitId;
	}

	/**
	 * @return The toolkit component class name, e.g.
	 *         {@code org.eclipse.jubula.toolkit.swing.SwingComponents}.
	 */
	public String getToolkitComponentsClassName() {
		return toolkitComponentsClassName;
	}

	/**
	 * @param toolkitId
	 *            The toolkit ID.
	 * @return The runner toolkit for the given toolkit ID, if it has been
	 *         found, otherwise false.
	 */
	public static RunnerToolkit getByToolkitId(final String toolkitId) {
		for (final RunnerToolkit value : values()) {
			if (value.getToolkitId().equals(toolkitId)) {
				return value;
			}
		}
		throw new IllegalArgumentException("Toolkit ID " + toolkitId + " does not exist.");
	}

}
