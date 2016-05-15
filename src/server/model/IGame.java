package server.model;

import shared.model.communication.CommunicationException;

/**
 * Controls the game logic.
 */
public interface IGame 
{
	/**
	 * Moves the game forward one step.
	 * @throws CommunicationException 
	 */
	public void step() throws CommunicationException;

	/**
	 * Returns the number of remaining steps in the game.
	 * 
	 * @return The number of steps remaining in the game.
	 */
	public int numberOfRemainingSteps();
}
