package shared.view;

import static org.junit.Assert.fail;

import java.awt.Component;
import java.awt.Container;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Contains components common for testing both the client and server ViewFactory classes.
 */
public class ViewTestHelper
{
	
	/**
	 * Private helper method which ensures the provided label exists.
	 * @param view The view to check.
	 * @param labelText The label for the button.
	 */
	public static void checkLabelExists(JPanel view, String labelText)
	{
		// push the view on a stack of components, used to traverse every child of the JPanel
		Stack<Component> componentStack = new Stack<Component>();
		componentStack.push(view);
		
		// perform a depth first traversal on the parent component
		while (!componentStack.empty())
		{
			Component component = componentStack.pop();
			
			// if component is a JLabel, check for matching text
			if (component instanceof JLabel)
			{
				if (((JLabel) component).getText().equals(labelText))
				{
					return;
				}
			}
			else if (component instanceof JPanel)
			{
				// component is a JPanel, add children to component stack
				for (Component child : ((Container) component).getComponents())
				{
					componentStack.push(child);
				}
			}
		}
		
		// a match was never found
		fail("A label with the text \"" + labelText + "\" does not exist.");
	}
	
	/**
	 * Helper method which checks that a button with the provided text exists.
	 * @param view The view to check.
	 * @param buttonText The text for the button.
	 */
	public static void checkButtonExists(JPanel view, String buttonText)
	{
		// push the view on a stack of components, used to traverse every child of the JPanel
		Stack<Component> componentStack = new Stack<Component>();
		componentStack.push(view);
		
		// perform a depth first traversal on the parent component
		while (!componentStack.empty())
		{
			Component component = componentStack.pop();
			
			// if component is a JButton, check for matching text
			if (component instanceof JButton)
			{
				if (((JButton) component).getText().equals(buttonText))
				{
					return;
				}
			}
			else if (component instanceof JPanel)
			{
				// component is a JPanel, add children to component stack
				for (Component child : ((Container) component).getComponents())
				{
					componentStack.push(child);
				}
			}
		}
		
		fail("A button with the text \"" + buttonText + "\" does not exist.");
	}
	
	/**
	 * Private helper method which ensures a text field exists.
	 * @param view The view to check.
	 */
	public static void checkTextFieldExists(JPanel view)
	{
		// push the view on a stack of components, used to traverse every child of the JPanel
		Stack<Component> componentStack = new Stack<Component>();
		componentStack.push(view);
		
		// perform a depth first traversal on the parent component
		while (!componentStack.empty())
		{
			Component component = componentStack.pop();
			
			// if component is a JTextField, we have found our component
			if (component instanceof JTextField)
			{
				return;
			}
			else if (component instanceof JPanel)
			{
				// component is a JPanel, add children to component stack
				for (Component child : ((Container) component).getComponents())
				{
					componentStack.push(child);
				}
			}
		}
		
		fail("A text field does not exist.");
	}
	
	/**
	 * Clicks on a button with the provided text.
	 * @param view The view to check.
	 * @param buttonText The text for the button.
	 */
	public static void clickOnButton(JPanel view, String buttonText)
	{
		// push the view on a stack of components, used to traverse every child of the JPanel
		Stack<Component> componentStack = new Stack<Component>();
		componentStack.push(view);
		
		// perform a depth first traversal on the parent component
		while (!componentStack.empty())
		{
			Component component = componentStack.pop();
			
			// if component is a button, click on the button
			if (component instanceof JButton)
			{
				if (((JButton) component).getText().equals(buttonText))
				{
					((JButton) component).doClick();
					return;
				}
			}
			else if (component instanceof JPanel)
			{
				// component is a JPanel, add children to component stack
				for (Component child : ((Container) component).getComponents())
				{
					componentStack.push(child);
				}
			}
		}
	}
	
	/**
	 * Helper method which ensures the provided button exists.  It then clicks on the button.
	 * @param view The view to check.
	 * @param buttonText The text for the button.
	 */
	public static void checkButtonExistsAndClickOnIt(JPanel view, String buttonText)
	{
		checkButtonExists(view, buttonText);
		clickOnButton(view, buttonText);
	}
}
