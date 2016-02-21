package de.buehmann.jubula.api.runner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.jubula.client.launch.AUTConfiguration;
import org.eclipse.jubula.toolkit.base.config.AbstractOSProcessAUTConfiguration;

/**
 * Annotation for the Jubula field starting a Java application configured by the
 * specified {@link AUTConfiguration}. The type of the annotated field must
 * implement the interface {@link de.buehmann.jubula.api.runner.RunnerAUT}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ConfigAUT {

	/**
	 * The reflection class of an AUTConfiguration implementation to specify the
	 * application.
	 */
	Class<? extends AbstractOSProcessAUTConfiguration> value();

}
