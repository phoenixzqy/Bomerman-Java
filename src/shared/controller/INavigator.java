package shared.controller;

/**
 * Allows controllers to navigation the application.  Any controller can simply push a new
 * controller class onto the controller stack, and INavigator will handle all of the set up.  It
 * will automatically create the controller, create an associated view and make the view visible. 
 */
public interface INavigator 
{
	/**
	 * Given a controller interface, this method creates the controller, creates a view associated 
	 * with that controller and makes the provided controller visible.
	 * @param controller The controller to push onto the navigator.
	 * @throws NullPointerException thrown if controllerInterface is null.
	 */
	public void push(IController controller);

	/**
	 * Stops the current controller from controlling the application and removes its view.  Gives
	 * control to the previous controller and its associated view.
	 * @throws IllegalStateException Thrown if there is no previous controller or view.
	 */
	public void pop();
	
	/**
	 * Given a controller interface, this method creates the controller, creates a view associated
	 * with that controller and replaces the current view and controller with the new view and 
	 * controller.
	 * @param controller The controller which will replace the current top controller.
	 * @throws NullPointerException Thrown if the provided controllerInterface is null.
	 * @throws IllegalStateException Thrown if there is not a current controller.
	 */
	public void replaceTop(IController controller);
}