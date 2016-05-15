package shared.view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import shared.core.ICommand;

/**
 * Creates JPanel menus.
 */
public interface IMenuViewBuilder
{
	/**
	 * Given an array of components, this method centers them and arranges them
	 * evenly spaced and vertically.
	 * 
	 * @param components
	 *            The components to add to the menu.
	 * @throws NullPointerException
	 *             Thrown if the title is null, if the components array is null
	 *             or if any of the individual components are null.
	 * @return A new JPanel representing the provided menu parameters.
	 */
	public JPanel buildMenu(JComponent[] components);

	/**
	 * Creates a button component using the provided label and command.
	 * 
	 * @param label
	 *            The label for the button.
	 * @param command
	 *            The command to execute when the button is clicked.
	 * @throws NullPointerException
	 *             Thrown if label or command are null.
	 * @return A new JButton representing the provided label and command.
	 */
	public JButton buildButton(String label, ICommand command);

	/**
	 * Creates a text field component.
	 * 
	 * @return A text field.
	 */
	public JTextField buildTextField();

	/**
	 * Creates a regular label.
	 * 
	 * @param text
	 *            The text for the label.
	 * @throws NullPointerException
	 *             Thrown if the text is null.
	 * @return A regular label.
	 */
	public JLabel buildLabel(String text);

	/**
	 * Creates a title label.
	 * 
	 * @param text
	 *            The text for the label.
	 * @throws NullPointerException
	 *             Thrown if the text is null.
	 * @return A title label.
	 */
	public JLabel buildTitleLabel(String text);
}
