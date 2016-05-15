package shared.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

import shared.core.ArrayUtilities;

/**
 * Tests ViewStack.
 */

public class ViewStackTest 
{	
	// empty panels for testing
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	
	// view stack used for testing
	private ViewStack viewStack;
	
	/**
	 * Flushes the event queue, forcing all events currently in the queue to finish executing.
	 * This allows me to call SwingUtilities.invokeLater() in the ViewStack code but ensure the
	 * runnable code has fired in the test, thus avoiding a race condition.
	 */
	private void flushSwingUtilitiesEventQueue()
	{
		/*
		 * Create a dummy thread and force SwingUtilities to block until it executes.  This is
		 * not very efficient, but it's a JUnit test, so it efficiency doesn't really matter.
		 */
		try 
		{
			SwingUtilities.invokeAndWait(new Runnable() 
			{
				public void run() 
				{
					// do nothing
				}
			});
		} 
		catch (Exception e) 
		{
			// fail if an exception occurs
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets up the ViewStack tests.
	 */
	@Before
	public void setUp()
	{
		// set up the panels
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		
		// set up the view stack
		viewStack = new ViewStack("Test");
	}

	/**
	 * Sanity check for creating a ViewStack.
	 */
	@Test
	public void sanityCheck()
	{
		new ViewStack("Test");
	}

	/**
	 * Ensures the constructor throws a NullPointerException when provided with a null argument.
	 */
	@Test(expected=NullPointerException.class)
	public void testConstructorNullViewThrowsException()
	{
		new ViewStack(null);
	}
	
	/**
	 * Ensures the constructor displays one frame for the application.
	 */
	@Test
	public void testConstructorDisplaysFrame()
	{
		viewStack = new ViewStack("Test");
		assertNotNull(viewStack.frame());
		flushSwingUtilitiesEventQueue();
		assertTrue(viewStack.frame().isVisible());
	}
	
	/**
	 * Ensures the displayed frame contains no components.
	 */
	@Test
	public void testConstructorDisplayedFrameContainsNoComponents()
	{
		viewStack = new ViewStack("Test");

		Component[] components = ((JRootPane) viewStack.frame().getComponents()[0]).getContentPane()
			.getComponents();
		
		assertFalse(ArrayUtilities.contains(components, panel1));
		assertFalse(ArrayUtilities.contains(components, panel2));
		assertFalse(ArrayUtilities.contains(components, panel3));
	}
	
	/**
	 * Ensures the displayed frame's title is the provided title.
	 */
	@Test
	public void testConstructorDisplaysFrameWithCorrectTitle()
	{
		viewStack = new ViewStack("Test Title");
		assertEquals("Test Title", viewStack.frame().getTitle());
	}
	
	/**
	 * Ensures the push method throws a new NullPointerException when null is provided as an 
	 * argument.
	 */
	@Test(expected=NullPointerException.class)
	public void testPushViewNull()
	{
		viewStack.push(null);
	}
	
	/**
	 * Ensures the push method displays the newly pushed JPanel when no panel is in the view stack.
	 * @throws Exception 
	 */
	@Test
	public void testPushWhenNoPanelsInTheViewStack() throws Exception
	{
		viewStack.push(panel1);
		
		/*
		 * Run this on the swing utilities thread to ensure invokeLater gets called first in the view 
		 * stack.
		 */
		SwingUtilities.invokeAndWait(new Runnable() {

			public void run()
			{
				// make sure the application frame is only displaying the component
				Component[] components = ((JRootPane) viewStack.frame().getComponents()[0]).getContentPane()
					.getComponents();

				assertTrue(ArrayUtilities.contains(components, panel1));
				assertFalse(ArrayUtilities.contains(components, panel2));
				assertFalse(ArrayUtilities.contains(components, panel3));
			}
		});
	}
	
	/**
	 * Ensures the push method displays the newly pushed JPanel when one panel is in the view stack.
	 * @throws Exception An exception thrown from the SwingUtilities thread.
	 */
	@Test
	public void testPushWhenOnePanelIsInTheViewStack() throws Exception
	{
		viewStack.push(panel1);
		viewStack.push(panel2);

		/*
		 * Run this on the swing utilities thread to ensure invokeLater gets called first in the view 
		 * stack.
		 */
		SwingUtilities.invokeAndWait(new Runnable() {

			public void run()
			{
			
				// make sure the application frame is only displaying the component
				Component[] components = ((JRootPane) viewStack.frame().getComponents()[0]).getContentPane()
					.getComponents();
		
				assertFalse(ArrayUtilities.contains(components, panel1));
				assertTrue(ArrayUtilities.contains(components, panel2));
				assertFalse(ArrayUtilities.contains(components, panel3));
			}
		});
	}
	
	/**
	 * Ensures the pop() method throws an IllegalStateException if no JPanels are in the view stack.
	 */
	@Test(expected=IllegalStateException.class)
	public void testPopWhenNoPanelsAreInTheViewStack()
	{
		viewStack.pop();
	}
	
	/**
	 * Ensures no panel is displayed when one JPanel is already in the view stack.
	 * @throws Exception An exception thrown from the SwingUtilities thread.
	 */
	@Test
	public void testPopWhenOnePanelIsInTheViewStack() throws Exception
	{
		viewStack.push(panel1);
		viewStack.pop();
		
		/*
		 * Run this on the swing utilities thread to ensure invokeLater gets called first in the view 
		 * stack.
		 */
		SwingUtilities.invokeAndWait(new Runnable() 
		{
			public void run()
			{
				Component[] components = ((JRootPane) viewStack.frame().getComponents()[0])
						.getContentPane().getComponents();
				
				assertFalse(ArrayUtilities.contains(components, panel1));
				assertFalse(ArrayUtilities.contains(components, panel2));
				assertFalse(ArrayUtilities.contains(components, panel3));
			}
		});
	}
	
	/**
	 * Ensures the pop method displays the lower JPanel when two JPanels are in the view stack.
	 * @throws Exception An exception thrown from the SwingUtilities thread.
	 */
	@Test
	public void testPopWhenTwoPanelsAreInTheViewStack() throws Exception
	{
		viewStack.push(panel1);
		viewStack.push(panel2);
		viewStack.pop();

		/*
		 * Run this on the swing utilities thread to ensure invokeLater gets called first in the view 
		 * stack.
		 */
		SwingUtilities.invokeAndWait(new Runnable() 
		{
			public void run()
			{
				// make sure no panels are displayed in the frame
				Component[] components = ((JRootPane) viewStack.frame().getComponents()[0]).getContentPane()
					.getComponents();
		
				assertTrue(ArrayUtilities.contains(components, panel1));
				assertFalse(ArrayUtilities.contains(components, panel2));
				assertFalse(ArrayUtilities.contains(components, panel3));
			}
		});
	}
	
	/**
	 * Ensures the pop method displays the middle panel when three JPanels are in the view stack.
	 * @throws Exception An exception thrown from the SwingUtilities thread.
	 */
	@Test
	public void testPopWhenThreePanelsAreInTheViewStack() throws Exception
	{
		viewStack.push(panel1);
		viewStack.push(panel2);
		viewStack.push(panel3);
		viewStack.pop();

		/*
		 * Run this on the swing utilities thread to ensure invokeLater gets called first in the view 
		 * stack.
		 */
		SwingUtilities.invokeAndWait(new Runnable() 
		{
			public void run()
			{
				// make sure no panels are displayed in the frame
				Component[] components = ((JRootPane) viewStack.frame().getComponents()[0]).getContentPane()
					.getComponents();
		
				assertFalse(ArrayUtilities.contains(components, panel1));
				assertTrue(ArrayUtilities.contains(components, panel2));
				assertFalse(ArrayUtilities.contains(components, panel3));
			}
		});
	}
}