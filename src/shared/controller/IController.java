package shared.controller;

import shared.core.ICommand;

/**
 * Controller interface.
 */

public interface IController 
{
	/**
	 * Binds to the provided property of the controller.
	 * @param property The property to bind to.  This should be a string representation of the 
	 * property.
	 * @param command The command to execute when the bound property changes.
	 * @throws NullPointerException Thrown if property or command are null.
	 * @throws IllegalArgumentException Thrown if this Controller does not contain a method for 
	 * the provided property.
	 */
	public void bind(String property, ICommand command);
	
	/**
	 * Unbinds the command to the property.
	 * @param property The property from which the command will be unbound.
	 * @param command The command to unbind.
	 * @throws NullPointerException Thrown if property or command are null.
	 * @throws IllegalArgumentException Thrown if this Controller does not contain a method for 
	 * the provided property or if the provided property and command are not bound to the controller. 
	 */
	public void unbind(String property, ICommand command);
	
	/**
	 * Returns the navigator for this IController.
	 * @return The navigator for this IController.
	 */
	public INavigator navigator();

}
