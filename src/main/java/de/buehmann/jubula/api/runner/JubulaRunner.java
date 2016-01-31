package de.buehmann.jubula.api.runner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.jubula.client.AUT;
import org.eclipse.jubula.client.AUTAgent;
import org.eclipse.jubula.client.MakeR;
import org.eclipse.jubula.client.exceptions.ExecutionException;
import org.eclipse.jubula.client.exceptions.ExecutionExceptionHandler;
import org.eclipse.jubula.toolkit.ToolkitInfo;
import org.eclipse.jubula.tools.AUTIdentifier;
import org.eclipse.jubula.tools.internal.constants.AutConfigConstants;

import de.buehmann.jubula.api.runner.annotation.OnTestFailure;
import de.buehmann.jubula.api.runner.internal.ReflectionUtil;
import de.buehmann.jubula.api.runner.internal.RunnerAUTConfiguration;
import de.buehmann.jubula.api.runner.internal.RunnerToolkit;

/**
 * Jubula test runner for a test class containing a field with a Jubula
 * annotation like {@link de.buehmann.jubula.api.runner.annotation.ClassAUT} to
 * specify the AUT. Then the AUT can be started and stopped.
 */
// hack: use @OnTestFailure annotation with no arguments to get the default value
@OnTestFailure
public class JubulaRunner implements ExecutionExceptionHandler {

	private final RunnerAction exceptionAction;

	private final RunnerAUTConfiguration runnerAUTConfig;

	private AUTAgent agent;

	private ExecutionExceptionHandler exceptionHandler;

	/**
	 * Constructor creates the AUT configuration specified by a field with the
	 * Jubula annotation.
	 * 
	 * @param testClass
	 *            The test class with a field annotated with one of
	 *            {@link org.eclipse.jubula.api.runner.annotations.*}.
	 */
	public JubulaRunner(final Class<?> testClass) {
		OnTestFailure annotation = testClass.getAnnotation(OnTestFailure.class);
		if (null == annotation) {
			// hack: use @OnTestFailure annotation with no arguments to get the default value
			annotation = getClass().getAnnotation(OnTestFailure.class);
		}
		exceptionAction = annotation.value();
		runnerAUTConfig = new RunnerAUTConfiguration(testClass);
		exceptionHandler = this;
	}

	/**
	 * @param handler The execution exception handler, which can be null to remove.
	 */
	public void setExceptionHandler(final ExecutionExceptionHandler handler) {
		exceptionHandler = handler;
	}

	/**
	 * Starts the AUT agent and then calls {@link #startAUT()}.
	 */
	public void start() {
		final Map<String, String> launchInformation = runnerAUTConfig.getLaunchInformation();
		agent = MakeR.createAUTAgent(launchInformation.get(AutConfigConstants.AUT_AGENT_HOST),
				Integer.valueOf(launchInformation.get(AutConfigConstants.AUT_AGENT_PORT)));
		agent.connect();
		startAUT();
	}

	/**
	 * Stops the AUT itself by calling {@link #stopAUT()} and then disconnects
	 * from the AUT agent.
	 */
	public void stop() {
		stopAUT();
		if (null != agent) {
			agent.disconnect();
		}
	}

	/**
	 * Starts the AUT and injects the runner AUT. It assumes, the agent has
	 * already been started.
	 */
	public void startAUT() {
		if (null != agent) {
			final AUTIdentifier id = agent.startAUT(runnerAUTConfig);
			if (null == id) {
				throw new IllegalStateException("No ID from AUT start.");
			}
			final AUT aut = agent.getAUT(id, getToolkitInformation());
			aut.setHandler(exceptionHandler);
			runnerAUTConfig.getRunnerAUT().setAUT(aut);
			ReflectionUtil.setStaticField(runnerAUTConfig.getRunnerAUTField(), runnerAUTConfig.getRunnerAUT());
		}
	}

	/**
	 * Stops the connection then the execution of the AUT.
	 */
	public void stopAUT() {
		final RunnerAUT aut = ReflectionUtil.getStaticField(runnerAUTConfig.getRunnerAUTField());
		if (null != aut) {
			aut.getAUT().disconnect();
			if (null != agent) {
				agent.stopAUT(aut.getAUT().getIdentifier());
			}
		}
	}

	/**
	 * Restarts the running AUT without closing the connection to the AUT agent.
	 * It calls only {@link #stopAUT()} followed by {@link #startAUT()}.
	 */
	public void restart() {
		stopAUT();
		startAUT();
	}

	@Override
	public void handle(final ExecutionException e) throws ExecutionException {
		switch (exceptionAction) {
		case RESTART_AUT:
			restart();
			break;
		case RESTART_AUT_AGENT_AND_AUT:
			stop();
			start();
			break;
		case DO_NOTHING:
		default:
		}
		throw e;
	}

	private ToolkitInfo getToolkitInformation() {
		final RunnerToolkit toolkit = RunnerToolkit.getByToolkitId(runnerAUTConfig.getRunnerAUT().getToolkitId());
		try {
			final Class<?> toolkitComponents = getClass().getClassLoader()
					.loadClass(toolkit.getToolkitComponentsClassName());
			final Method method = toolkitComponents.getMethod("getToolkitInformation");
			return (ToolkitInfo) method.invoke(null);
		} catch (final ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalStateException(e);
		}
	}

}
