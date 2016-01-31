package de.buehmann.jubula.api.runner;

public enum RunnerAction {

	/** The AUT will do nothing else. */
	DO_NOTHING,

	/** The AUT will be restarted. */
	RESTART_AUT,

	/** The AUT agent and the AUT will be restarted. */
	RESTART_AUT_AGENT_AND_AUT,

}
