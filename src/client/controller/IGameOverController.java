package client.controller;

import shared.model.IScore;

/**
 * Controls the game over screen.
 */
public interface IGameOverController extends IController
{

	/**
	 * Sets up a rematch using the same server as the previous game.
	 */
	public void rematch();

	/**
	 * Navigates the application to the main menu.
	 */
	public void mainMenu();

	/**
	 * Returns the scores from the game.
	 * 
	 * @return The scores from the game.
	 */
	public IScore[] scores();
}
