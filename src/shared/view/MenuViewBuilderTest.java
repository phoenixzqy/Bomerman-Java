package shared.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import shared.core.ICommand;

/**
 * Tests MenuViewBuilder.
 */
public class MenuViewBuilderTest
{
	// a menu view builder
	private IMenuViewBuilder menuViewBuilder;

	// a mock command
	private ICommand mockCommand;

	// an array of test components
	private JComponent[] mockComponents;

	/**
	 * Sets up the tests
	 */
	@Before
	public void setUp()
	{
		mockCommand = mock(ICommand.class);
		menuViewBuilder = new MenuViewBuilder();
		mockComponents = new JComponent[] { mock(JComponent.class),
				mock(JComponent.class), mock(JComponent.class) };

		for (int i = 0; i < mockComponents.length; i++)
		{
			Component mockComponent = mockComponents[i];
			stub(mockComponent.getWidth()).toReturn(i * 20);
			stub(mockComponent.getHeight()).toReturn(i * 10);
		}
	}

	/**
	 * Ensures the buildMenu method throws an exception when provided with a
	 * null array of components.
	 */
	@Test(expected = NullPointerException.class)
	public void testBuildMenuNullComponentsThrowsException()
	{
		menuViewBuilder.buildMenu(null);
	}

	/**
	 * Ensures the buildMenu command has the provided components and they are
	 * all visible.
	 */
	@Test
	public void testBuildMenuComponents()
	{
		/**
		 * Test with real components here because we need for their properties
		 * to be set properly.
		 */
		JComponent[] components = { new JLabel("Test Label"),
				new JButton("Test Button"), new JTextField("Test Slider") };

		JPanel menu = menuViewBuilder.buildMenu(components);

		for (Component component : components)
		{
			assertEquals(menu, component.getParent().getParent());

		}
	}

	/**
	 * Ensures the buildButton method throws a NullPointerException when
	 * provided with a null command.
	 */
	@Test(expected = NullPointerException.class)
	public void testBuildButtonNullCommandThrowsException()
	{
		menuViewBuilder.buildButton("Test Button", null);
	}

	/**
	 * Ensures the buildButton method returned button has the provided label.
	 */
	@Test
	public void testBuildButtonLabel()
	{
		String label = "Test Button";
		JButton button = menuViewBuilder.buildButton(label, mockCommand);
		assertEquals(label, button.getText());
	}

	/**
	 * Ensures the buildButton method executes the provided command when it is
	 * clicked on.
	 */
	@Test
	public void testBuildButtonCommand()
	{
		JButton button = menuViewBuilder
				.buildButton("Test Button", mockCommand);
		verify(mockCommand, never()).execute();
		button.doClick();
		verify(mockCommand).execute();
	}

	/**
	 * Ensures the buildMenu method throws a NullPointerException when the
	 * components array contains a null element.
	 */
	@Test(expected = NullPointerException.class)
	public void testBuildMenuComponentsContainsNullThrowsException()
	{
		mockComponents[1] = null;
		menuViewBuilder.buildMenu(mockComponents);
	}

	/**
	 * Ensures the buildTitleLabel method throws a NullPointerException when
	 * provided with null text.
	 */
	@Test(expected = NullPointerException.class)
	public void testBuildTitleLabelNullTextThrowsException()
	{
		menuViewBuilder.buildTitleLabel(null);
	}

	/**
	 * Ensures the buildTitleLabel method works properly.
	 */
	@Test
	public void testBuildTitleLabel()
	{
		String title = "Test Title";
		JLabel titleLabel = menuViewBuilder.buildTitleLabel(title);

		assertEquals(title, titleLabel.getText());
	}

	/**
	 * Ensures the buildLabel method throws a NullPointerException when provided
	 * with null text.
	 */
	@Test(expected = NullPointerException.class)
	public void testBuildLabelNullTextThrowsException()
	{
		menuViewBuilder.buildLabel(null);
	}

	/**
	 * Ensures the buildLabel method works properly.
	 */
	@Test
	public void testBuildLabel()
	{
		String testLabelString = "Test Label";
		JLabel testLabel = menuViewBuilder.buildLabel(testLabelString);

		assertEquals(testLabelString, testLabel.getText());
	}

	/**
	 * Ensures the buildTextField method works properly.
	 */
	@Test
	public void testBuildTextField()
	{
		assertNotNull(menuViewBuilder.buildTextField());
	}
}
