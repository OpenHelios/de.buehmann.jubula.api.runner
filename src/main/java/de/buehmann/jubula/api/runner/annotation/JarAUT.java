package de.buehmann.jubula.api.runner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for the Jubula field starting a Java JAR file as AUT. The type
 * of the annotated field must implement the interface
 * {@link de.buehmann.jubula.api.runner.RunnerAUT}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface JarAUT {

	/**
	 * The path of the JAR for the Java application, which will be started with
	 * {@code java -jar path/to/jar/file.jar}.
	 */
	String value();

	/** The optional arguments passed to the Java application. */
	String[] args() default "";

	/** The optional working directory of the Java application. */
	String workingDir() default "";

	/** The optional command, which is {@code java} as default. */
	String command() default "java";
	
	/** The optional host of the AUT agent. Default is {@code localhost}. */
	String agentHost() default "localhost";

	/**
	 * The optional port of the AUT agent (currently needed, because the
	 * embedded AUT agent can not be started by Java API). Default is 0 for
	 * using the embedded AUT agent.
	 */
	int agentPort() default 0;

}
