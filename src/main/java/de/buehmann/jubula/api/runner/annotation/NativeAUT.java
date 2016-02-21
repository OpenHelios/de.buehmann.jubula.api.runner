package de.buehmann.jubula.api.runner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for the Jubula field starting a normal Java class as AUT. The type
 * of the annotated field must implement the interface
 * {@link de.buehmann.jubula.api.runner.RunnerAUT}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface NativeAUT {

	/**
	 * The path to the executable of the Java application.
	 */
	String value();

	/**
	 * The optional arguments passed to the Java application. Default is
	 * {@code null}.
	 */
	String[] args() default {};

	/**
	 * The optional working directory of the Java application. Default is an
	 * empty string.
	 */
	String workingDir() default "";

	/** The optional host of the AUT agent. Default is {@code localhost}. */
	String agentHost() default "localhost";

	/**
	 * The optional port of the AUT agent (currently needed, because the
	 * embedded AUT agent can not be started by Java API). Default is 0 for
	 * using the embedded AUT agent.
	 */
	int agentPort() default 0;

}
