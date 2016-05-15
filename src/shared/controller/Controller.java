package shared.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

import shared.core.ICommand;

/**
 * Abstract controller base class.  The main feature of this class is its ability to offer bindable
 * properties.  Bindable properties allow another object, such as a view, to bind to a property in
 * this controller class.  When the property changes, the bound object is automatically notified of
 * the change and the new value is passed to it by executing a Command object.  
 */
public abstract class Controller implements IController
{
	// a map of properties bound to sets of commands
	HashMap<String, HashSet<ICommand>> bindings;
	
	/**
	 * Initializes this Controller.
	 */
	public Controller()
	{
		bindings = new HashMap<String, HashSet<ICommand>>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void bind(String property, ICommand command)
	{
		// check the parameters
		if (property == null || command == null)
		{
			throw new NullPointerException();
		}
		
		// make sure the property exists
		methodForProperty(property);
		
		// if bindings does not contain the key, create the association
		if (!bindings.containsKey(property))
		{
			bindings.put(property, new HashSet<ICommand>());
		}
		
		// if the command is not already in the bindings, insert it
		bindings.get(property).add(command);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void unbind(String property, ICommand command)
	{
		// check the parameters
		if (property == null || command == null)
		{
			throw new NullPointerException();
		}
		
		// make sure the property exists
		methodForProperty(property);
		
		// check to see if the property has been bound at all and remove it if it does
		if (!bindings.containsKey(property) || !bindings.get(property).remove(command))
		{
			throw new IllegalArgumentException();
		}
		
		
		// remove the set if it is empty
		if (bindings.get(property).isEmpty())
		{
			bindings.remove(property);
		}
		
	}
	
	/**
	 * Indicates the specified property has changed.  This method should be called in a controller 
	 * whenever a bindable property is changed.
	 * @param property The property which has changed.
	 * @throws NullPointerException Thrown if property is null.
	 * @throws IllegalArgumentException Thrown if this Controller does not contain a method for the 
	 * provided property or if the provided property method cannot be invoked.
	 */
	protected void propertyDidChange(String property)
	{
		// check the parameters
		if (property == null)
		{
			throw new NullPointerException();
		}
		
		// if the property is not bound to anything, do nothing
		if (!bindings.containsKey(property))
		{
			return;
		}
		
		// iterate through each of the commands for the property, calling then with the new value
		for (ICommand command : bindings.get(property))
		{
			command.execute();
		}
	}
	
	/**
	 * Helper method which returns the Method object associated with a given property.
	 * @param property The property to retrieve the Method object for.
	 * @throws NullPointerException Thrown if property is null.
	 * @throws IllegalArgumentException Thrown if the provided property is not a method for this
	 * Controller.
	 * @return Returns the method associated with the provided property.
	 */
	private Method methodForProperty(String property)
	{
		// check the parameters
		if (property == null)
		{
			throw new NullPointerException();
		}
		
		// try to return the method, throwing an IllegalArgumentException if it fails
		try 
		{
			return this.getClass().getMethod(property);
		} 
		catch (Exception e) 
		{
			throw new IllegalArgumentException();
		} 
	}
}
