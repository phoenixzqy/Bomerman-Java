package server.controller;

/**
 * Controls the main menu screen.
 */
public interface IMainMenuController extends IController
{	
	/**
	 * Exits the application.
	 */
	public void exit();
	
	/**
	 * Starts the server listening for incoming connections and navigates the application to the game
	 * hosting screen.  If the application cannot start listening, this method displays a message to
	 * the user and does not navigate the application.
	 */
	public void startListening();
	
	/**
	 * Returns the error message for this IMainMenuController.  This is a bindable property.
	 * @return The error message for this IMainMenuController.
	 */
	public String errorMessage();
}
