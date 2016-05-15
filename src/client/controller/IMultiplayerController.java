package client.controller;

/**
 * Controls the multiplayer screen.
 */
public interface IMultiplayerController extends IController 
{	
	/**
	 * Returns the server address.  This is a bindable property.
	 * @return The server address.
	 */
	public String serverAddress();
	
	/**
	 * Sets the server address.  This is a bindable property.
	 * @param address The value of the new address to set.
	 */
	public void setServerAddress(String address);
	
	/**
	 * Returns the error message.  This is a bindable property.
	 * @return The error message.
	 */
	public String errorMessage();
	
	/**
	 * Sets the error message.  This is a bindable property.
	 * @param errorMessage The value of the new error message to set.
	 */
	public void setErrorMessage(String errorMessage);
	
	/**
	 * Joins the server as a player.  If successful, it navigates the application to the game lobby
	 * screen.  If not successful, it sets its error message.
	 */
	public void join();
	
	
	/**
	 * Navigates the application to the main menu screen.
	 */
	public void mainMenu();
}