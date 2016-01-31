package de.buehmann.jubula.api.runner;

import org.eclipse.jubula.client.AUT;

/**
 * Interface for the type of the Jubula field.
 * 
 * @see de.buehmann.jubula.api.runner.annotation.ClassAUT
 * @see de.buehmann.jubula.api.runner.annotation.JarAUT
 * @see de.buehmann.jubula.api.runner.annotation.ConfigAUT
 */
public interface RunnerAUT {

	/**
	 * @return The toolkit ID, e.g.
	 *         {@link org.eclipse.jubula.tools.internal.constants.CommandConstants#SWING_TOOLKIT}
	 *         .
	 */
	String getToolkitId();

	/**
	 * @param aut The AUT to use with Jubula.
	 */
	void setAUT(final AUT aut);

	/**
	 * @return The AUT to use with Jubula.
	 */
	AUT getAUT();

	}
