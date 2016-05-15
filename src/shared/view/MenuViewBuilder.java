package shared.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;

import javax.swing.*;

import shared.core.ICommand;

/**
 * Implements IMenuViewBuilder.
 */
public class MenuViewBuilder implements IMenuViewBuilder
{
	/**
	 * The default padding between menu components in pixels.
	 */
	public final int DEFAULT_PADDING = 30;

	/**
	 * The title font.
	 */
	private final Font titleFont;

	/**
	 * The button font.
	 */
	private final Font buttonFont;
	
	// the default font color
	private final Color textColor = new Color(245, 245, 245);

	/**
	 * Default constructor.
	 */
	public MenuViewBuilder()
	{
		// set up the title font
		titleFont = new Font("sans-serif", Font.BOLD, 40);

		// set default font attributes
		final HashMap<TextAttribute, Object> fontAttributes = new HashMap<TextAttribute, Object>();
		fontAttributes.put(TextAttribute.SIZE, 40);
		fontAttributes.put(TextAttribute.WEIGHT, Font.BOLD);
		fontAttributes.put(TextAttribute.SIZE, 20);

		buttonFont = new Font(fontAttributes);
	}

	/**
	 * {@inheritDoc}
	 */
	public JPanel buildMenu(JComponent[] components)
	{
		// make sure the provided arguments aren't null
		if (components == null)
			throw new NullPointerException();

		// make sure none of the components are null
		for (JComponent component : components)
			if (component == null)
				throw new NullPointerException();

		// create a parent panel used for layout
		JPanel parentPanel = new JPanel(new GridBagLayout());

		// create the menu panel
		JPanel menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		
		// add the components to the JPanel
		for (JComponent component : components)
		{
			menu.add(Box.createRigidArea(new Dimension(0, DEFAULT_PADDING)));
			component.setAlignmentX(Component.CENTER_ALIGNMENT);
			menu.add(component);
		}

		// set the background
		menu.setBackground(new Color(33, 33, 33));
		
		parentPanel.add(menu);
		return parentPanel;
	}

	/**
	 * {@inheritDoc}
	 */
	public JButton buildButton(String label, final ICommand command)
	{
		if (label == null || command == null)
			throw new NullPointerException();

		// create new button and set size, label, and other attributes
		JButton button = new JButton(label);
		button.setFont(buttonFont);
		button.setFocusable(false);
		button.setMargin(new Insets(5, 5, 5, 5));
		button.setSize(800, button.getHeight());

		// add an action listener to the button which executes the provided
		// command
		button.addActionListener(new ActionListener()
		{
			/**
			 * Executes the provided command.
			 */
			public void actionPerformed(ActionEvent arg0)
			{
				command.execute();
			}
		});

		return button;
	}

	/**
	 * {@inheritDoc}
	 */
	public JTextField buildTextField()
	{
		// create a centered text field
		JTextField textField = new JTextField();
		textField.setAlignmentX(Component.CENTER_ALIGNMENT);
		return textField;
	}

	/**
	 * {@inheritDoc}
	 */
	public JLabel buildLabel(String text)
	{
		if (text == null)
			throw new NullPointerException();

		// create a centered label with the given text
		JLabel label = new JLabel(text);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setForeground(textColor);
		return label;
	}

	/**
	 * {@inheritDoc}
	 */
	public JLabel buildTitleLabel(String text)
	{
		if (text == null)
			throw new NullPointerException();

		// create a centered label with large font with the given text
		JLabel titleLabel = new JLabel(text);
		titleLabel.setFont(titleFont);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		titleLabel.setForeground(textColor);
		return titleLabel;
	}
}
