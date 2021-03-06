package de.buehmann.jubula.api.runner.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jubula.toolkit.base.config.AbstractOSProcessAUTConfiguration;
import org.eclipse.jubula.tools.internal.constants.AutConfigConstants;
import org.eclipse.jubula.tools.internal.constants.ToolkitConstants;

import de.buehmann.jubula.api.runner.JavaFXRunnerAUT;
import de.buehmann.jubula.api.runner.RunnerAUT;
import de.buehmann.jubula.api.runner.SwingRunnerAUT;
import de.buehmann.jubula.api.runner.annotation.ClassAUT;
import de.buehmann.jubula.api.runner.annotation.ConfigAUT;
import de.buehmann.jubula.api.runner.annotation.JarAUT;
import de.buehmann.jubula.api.runner.annotation.NativeAUT;

/**
 * A data class storing the Jubula Runner AUT configuration.
 */
public class RunnerAUTConfiguration extends AbstractOSProcessAUTConfiguration {

	private final Field autField;

	private final RunnerAUT runnerAUT;

	/**
	 * Constructor for a {@link ClassAUT} based AUT configuration.
	 * 
	 * @param javaClass
	 *            The java test class with an annotated Jubula field for the
	 *            AUT.
	 */
	public RunnerAUTConfiguration(final Class<?> javaClass) {
		this(validateFields(javaClass));
	}

	private RunnerAUTConfiguration(final Field autField) {
		this(createAutConfiguration(autField), autField);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param autConfiguration
	 *            The AUT configuration to copy.
	 * @param autField
	 *            The field annotated with the given AUT configuration.
	 */
	private RunnerAUTConfiguration(final AbstractOSProcessAUTConfiguration autConfiguration, final Field autField) {
		super(autConfiguration.getName(), autConfiguration.getAutID().getID(), autConfiguration.getCommand(),
				autConfiguration.getWorkingDir(), autConfiguration.getArgs());
		this.autField = autField;
		for (final Entry<String, String> entry : autConfiguration.getLaunchInformation().entrySet()) {
			add(entry.getKey(), entry.getValue());
		}
		@SuppressWarnings("unchecked")
		final Class<? extends RunnerAUT> fieldType = (Class<? extends RunnerAUT>) autField.getType();
		try {
			runnerAUT = fieldType.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
		add(ToolkitConstants.ATTR_TOOLKITID, runnerAUT.getToolkitId());
	}

	/**
	 * Constructor
	 * 
	 * @param autID
	 *            the AUT ID
	 * @param command
	 *            the command
	 * @param workingDir
	 *            the working directory for the AUT process. If a relative path
	 *            is given the base path is relative to the process working
	 *            directory of the connected
	 *            {@link org.eclipse.jubula.client.AUTAgent AUTAgent}
	 * @param args
	 *            the commands arguments
	 * @param agentHost
	 *            The host of the AUT agent.
	 * @param agentPort
	 *            The port of the AUT agent.
	 */
	private RunnerAUTConfiguration(final String autID, final String command, final String workDir, final String[] args,
			final String agentHost, final int agentPort) {
		super(null, autID, command, workDir, args);
		add(AutConfigConstants.AUT_AGENT_HOST, agentHost);
		add(AutConfigConstants.AUT_AGENT_PORT, Integer.toString(agentPort));
		autField = null;
		runnerAUT = null;
	}

	/**
	 * @param classAUT
	 *            The class based AUT annotation.
	 */
	private RunnerAUTConfiguration(final ClassAUT classAUT) {
		this(classAUT.value().getName(), classAUT.command(),
				classAUT.workingDir().isEmpty() ? getAbsoluteJavaRootPath(classAUT.value()) : classAUT.workingDir(),
				createAUTArgsFromClassAut(classAUT), classAUT.agentHost(), classAUT.agentPort());
	}

	private static String[] createAUTArgsFromClassAut(final ClassAUT classAUT) {
		final String[] source = classAUT.args();
		final String[] target = new String[source.length + 1];
		for (int i = 1; i < target.length; i++) {
			target[i] = source[i - 1];
		}
		target[0] = classAUT.value().getCanonicalName();
		return target;
	}

	/**
	 * @param jarAUT
	 *            The JAR based AUT annotation.
	 */
	private RunnerAUTConfiguration(final JarAUT jarAUT) {
		this(jarAUT.value(), jarAUT.command(), jarAUT.workingDir(), addJarArgument(jarAUT.args()), jarAUT.agentHost(),
				jarAUT.agentPort());
	}

	/**
	 * @param nativeAUT
	 *            The native AUT annotation.
	 */
	private RunnerAUTConfiguration(final NativeAUT nativeAUT) {
		this(nativeAUT.value(), nativeAUT.value(), nativeAUT.workingDir(), nativeAUT.args(), nativeAUT.agentHost(),
				nativeAUT.agentPort());
	}

	/**
	 * @return The annotated Jubula field storing the runner AUT.
	 */
	public Field getRunnerAUTField() {
		return autField;
	}

	/**
	 * @return The runner AUT for this runner AUT configuration.
	 */
	public RunnerAUT getRunnerAUT() {
		return runnerAUT;
	}

	private static Field validateFields(final Class<?> testClass) {
		final Class<?>[] annotations = new Class<?>[] { ClassAUT.class, JarAUT.class, NativeAUT.class,
				ConfigAUT.class };
		final List<Field> fields = new ArrayList<>();
		Class<? extends Annotation> autAnnotation = null;
		final ReflectionUtil reflectionUtil = new ReflectionUtil(testClass);
		for (final Class<?> annotation : annotations) {
			@SuppressWarnings("unchecked")
			final Class<? extends Annotation> annotationType = (Class<? extends Annotation>) annotation;
			final List<Field> annotatedFields = reflectionUtil.getAnnotatedFields(annotationType);
			fields.addAll(annotatedFields);
			if (!annotatedFields.isEmpty()) {
				autAnnotation = annotationType;
			}
		}
		if (1 != fields.size() || null == autAnnotation) {
			throw new IllegalStateException(
					"Expected exactly one field annotated with " + ClassAUT.class.getPackage().getName() + ".*AUT!");
		}
		final Field field = fields.iterator().next();
		if (!ReflectionUtil.isStatic(field)) {
			throw new IllegalStateException(
					"Field " + field.getName() + " in class " + field.getDeclaringClass().getCanonicalName()
							+ " annotated with @" + autAnnotation.getName() + " must be static.");
		}
		final Class<?> fieldType = field.getType();
		if (!SwingRunnerAUT.class.isAssignableFrom(fieldType) && !JavaFXRunnerAUT.class.isAssignableFrom(fieldType)) {
			throw new IllegalStateException("Field " + field.getName() + " in class "
					+ field.getDeclaringClass().getCanonicalName() + " annotated with @" + autAnnotation.getName()
					+ " must have type " + RunnerAUT.class.getPackage().getName() + ".*RunnerAUT!");
		}
		return field;
	}

	private static AbstractOSProcessAUTConfiguration createAutConfiguration(final Field autField) {
		AbstractOSProcessAUTConfiguration autConfig = null;
		final ConfigAUT configAUT = autField.getAnnotation(ConfigAUT.class);
		if (null != configAUT) {
			try {
				autConfig = configAUT.value().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
		} else {
			final ClassAUT classAUT = autField.getAnnotation(ClassAUT.class);
			final JarAUT jarAUT = autField.getAnnotation(JarAUT.class);
			final NativeAUT nativeAUT = autField.getAnnotation(NativeAUT.class);
			if (null != classAUT) {
				autConfig = new RunnerAUTConfiguration(classAUT);
			} else if (null != jarAUT) {
				autConfig = new RunnerAUTConfiguration(jarAUT);
			} else if (null != nativeAUT) {
				autConfig = new RunnerAUTConfiguration(nativeAUT);
			} else {
				throw new IllegalStateException("Missing field annotated with AUT configuration!");
			}
		}
		return autConfig;
	}

	private static String getAbsoluteJavaRootPath(final Class<?> clazz) {
		return clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
	}

	private static String[] addJarArgument(final String[] args) {
		final String[] result = new String[args.length + 1];
		result[0] = "-jar";
		for (int i = 0; i < args.length; i++) {
			result[i + 1] = args[i];
		}
		return result;
	}

}
