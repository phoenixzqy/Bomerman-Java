package client.controller;

/**
 * Controls the connection failure screen.
 */
public interface IConnectionFailureController extends IController
{
	/**
	 * Navigates the application to the multiplayer screen.
	 */
	public void multiplayer();
	
	/**
	 * Returns the error message for this IConnectionFailureController.
	 * @return The error message for this IConnectionFailureController.
	 */
	public String errorMessage();
}
