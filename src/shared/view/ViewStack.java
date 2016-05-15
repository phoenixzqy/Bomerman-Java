package shared.view;

import java.awt.*;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Responsible for maintaining a view stack for the application.  Allows a user to add and remove 
 * views from the view stack.
 */
public class ViewStack implements IViewStack
{
	// the view stack
	private Stack<JPanel> viewStack;
	
	// the view frame
	private JFrame frame;
	
	/**
	 * The default width of a frame.
	 */
	public static final int DEFAULT_FRAME_WIDTH = 736;
	
	/**
	 * The default height of a frame.
	 */
	public static final int DEFAULT_FRAME_HEIGHT = 576;
	
	/**
	 * Creates a new IViewStack.
	 * @param title The title of the frame in which the view stack will be displayed in.
	 */
	public ViewStack(String title)
	{
		// check the parameters
		if (title == null)
			throw new NullPointerException();
		
		// set up the view stack
		viewStack = new Stack<JPanel>();
		
		// set up the frame
		frame = new JFrame();
		frame.pack();
		Insets frameInsets = frame.getInsets();
		frame.setResizable(false);
		frame.setSize(DEFAULT_FRAME_WIDTH + frameInsets.left + frameInsets.right, 
				DEFAULT_FRAME_HEIGHT + frameInsets.top + frameInsets.bottom);
		frame.setTitle(title);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  
	  	// set the initial background color
	  	frame.getContentPane().setBackground(new Color(33, 33, 33));
	   
	  	// make the frame visible on a GUI thread  
		SwingUtilities.invokeLater(new Runnable() {
		  public void run() {
			   frame.setVisible(true);
		  }
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void pop()
	{
		if (viewStack.empty())
			throw new IllegalStateException();
		
		// remove the previous view and store it
		final JPanel previousPanel = viewStack.pop();
		
		// set the value of the new panel if it's not empty
		final JPanel newPanel = viewStack.empty() ? null : viewStack.peek();
		
		// remove the old panel and display the new panel on the GUI thread
		try
		{
			SwingUtilities.invokeLater(new Runnable() 
			{
				public void run()
				{
					// remove the old panel
					frame.remove(previousPanel);
						
					// add the new panel if possible
					if (newPanel != null)
					{
						frame.add(newPanel);
						newPanel.revalidate();
						newPanel.repaint();
					}
						
					// call invalidate to force the view to redraw
					frame.validate();
				}
			});
		}
		catch (Exception exception)
		{
			// this should never happen, so if it does throw an unchecked exception
			throw new IllegalStateException();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void push(final JPanel view) 
	{
		// check the parameters
		if (view == null)
			throw new NullPointerException();
		
		// save the old view
		final JPanel previousPanel = viewStack.empty() ? null : viewStack.peek();

		// reset the background color
		view.setBackground(new Color(33, 33, 33));
		
		// add the new view
		viewStack.push(view);
		
		
		// remove the old panel and display the new panel on the GUI thread
		if (!viewStack.empty())
		{
			// add and remove the panels from the frame on the GUI thread
			try
			{
				SwingUtilities.invokeLater(new Runnable() 
				{
					public void run()
					{
						// remove the previous panel if possible
						if (previousPanel != null)
							frame.remove(previousPanel);
						
						// add the new panel
						frame.add(view);
						
						// call invalidate to force the view to redraw
						frame.validate();
						view.revalidate();
						view.repaint();
					}
				});
			}
			catch (Exception exception)
			{
				// this shouldn't happen, so throw an IllegalStateException
				throw new IllegalStateException();
			}
		}
	}
	
	/**
	 * Returns the JFrame associated with this IViewStack.  This method is provided for testing.
	 * @Return The JFrame associated with this IViewStack.
	 */
	protected JFrame frame()
	{
		return frame;
	}
}
