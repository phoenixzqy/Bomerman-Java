package client.controller;

/**
 * Controls the main menu screen.
 */
public interface IMainMenuController extends IController 
{
	/**
	 * Navigates the application to the multiplayer screen.
	 */
	public void multiplayer();
	
	/**
	 * Navigates the application to the credits screen.
	 */
	public void credits();
	
	/**
	 * Exits the application.
	 */
	public void exit();
	
}
