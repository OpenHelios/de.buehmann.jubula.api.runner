package de.buehmann.jubula.api.runner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.buehmann.jubula.api.runner.RunnerAction;

/**
 * An optional annotation to specify the Jubula runner behavior after an assert
 * method failed. {@link RunnerAction#RESTART_AUT} is the default behavior.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface OnTestFailure {

	/**
	 * @return The runner action, after an assert method failed.
	 *         {@link RunnerAction#RESTART_AUT} is the default behavior.
	 */
	RunnerAction value() default RunnerAction.RESTART_AUT;

}
