package de.buehmann.jubula.api.runner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for the Jubula field to define a normal Java class as AUT. The
 * type of the annotated field must implement the interface
 * {@link de.buehmann.jubula.api.runner.RunnerAUT}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ClassAUT {

	/**
	 * The reflection class of the Java application to test with Jubula.
	 * <ul>
	 * <li>A Swing application must define a static main method with signature
	 * <br/>
	 * {@code public static void main(String[])}.</li>
	 * <li>A Java FX application must inherit from<br/>
	 * {@code javafx.application.Application}.</li>
	 * </ul>
	 */
	Class<?> value();

	/**
	 * The arguments passed to the Java application. Default is a String array
	 * with no elements.
	 */
	String[] args() default {};

	/**
	 * The working directory of the Java application. Default is an empty
	 * String, which is transformed at runtime to the root folder of the
	 * specified class given to field named value.
	 */
	String workingDir() default "";

	/** The command. Default is {@code java}. */
	String command() default "java";

	/** The host of the AUT agent. Default is {@code localhost}. */
	String agentHost() default "localhost";

	/**
	 * The port of the AUT agent (currently needed, because the embedded AUT
	 * agent can not be started by Java API). Default is 0 for using the
	 * embedded AUT agent.
	 */
	int agentPort() default 0;

}
