package shared.core;

/**
 * Define a command interface.  A command encapsulates some logic, which allow's objects to execute 
 * the command without knowing its contents.
 */
public interface ICommand
{
	/**
	 * Executes the command.
	 */
	public void execute();
}
