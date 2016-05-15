package shared.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import shared.core.ICommand;
/**
 * Tests the abstract Controller class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(JOptionPane.class)
public class ControllerTest {
	
	// a test controller class
	class TestController extends Controller {
		
		// a bindable property
		private int bindable;
		
		// a second bindable property
		private int bindable2;
		
		// an unbindable property
		private int unbindable;
		
		// a mocked navigator
		private Navigator mockNavigator;
		
		/**
		 * Returns a sample bindable property.
		 * @return A sample bindable property.
		 */
		public int bindable() {
			return bindable;
		}
		
		/**
		 * Sets the sample bindable property.
		 * @param value The value of the sample bindable property.
		 */
		public void setBindable(int value) {
			bindable = value;
			propertyDidChange("bindable");
		}
		
		/**
		 * Returns a second bindable property.
		 * @return A second bindable property.
		 */
		public int bindable2() {
			return bindable2;
		}
		
		/**
		 * Sets the second bindable property.
		 * @param value The value of the second bindable property.
		 */
		public void setBindable2(int value) {
			bindable2 = value;
			propertyDidChange("bindable2");
		}
		
		/**
		 * Returns the value of the unbindable property.
		 * @return The value of the unbindable property.
		 */
		public int unbindable() {
			return unbindable;
		}
		
		/**
		 * Sets the unbindable property.
		 * @param value The value of the unbindable property.
		 */
		public void setUnbindable(int value) {
			unbindable = value;
		}

		/**
		 * {@inheritDoc}
		 */
		public INavigator navigator()
		{
			if (mockNavigator == null) {
				mockNavigator = mock(Navigator.class);
			}
			return mockNavigator;
		}
	}
	
	// the controller to test
	private TestController controller;
	
	// command which, when called, increments the execution count
	private ICommand executeCommand;
	
	// a second did execute command
	private ICommand executeCommand2;
	
	// determines if the should execute command did execute
	private int executionCount;
	
	// a second did execute value
	private int executionCount2;
	
	// a mock command that does nothing
	private ICommand mockCommand;
	
	/**
	 * Creates a new controller before every test.
	 */
	@Before
	public void SetUp() {
		
		// controller anonymous class
		controller = new TestController();
		
		executionCount = 0;
		executionCount2 = 0;
		
		executeCommand = new ICommand() {
			public void execute() {
				executionCount++;
			}
		};
		
		executeCommand2 = new ICommand() {
			public void execute() {
				executionCount2++;
			}
		};
		
		mockCommand = new ICommand() {
			public void execute() {
				// do nothing
			}
		};
	}
	
	/**
	 * Ensures the bind() controller method throws a NullPointerException when null is provided for 
	 * the property argument.
	 */
	@Test(expected=NullPointerException.class)
	public void testBindPropertyArgumentNullThrowsException() {
		controller.bind(null, mockCommand);
	}
	
	/**
	 * Ensures the bind() controller method throws a NullPointerException when null is provided for 
	 * the command argument.
	 */
	@Test(expected=NullPointerException.class)
	public void testBindCommandArgumentNullThrowsException() {
		controller.bind("bindable", null);
	}
	
	/**
	 * Ensures the controller throws an exception when binding to a property is does not possess.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testBindThrowsExceptionWhenBindingToNonexistentProperty() {
		controller.bind("nonexistantProperty", mockCommand);
	}
	
	/**
	 * Ensures the unbind() method throws a NullPointerException when null is passed in for the 
	 * property argument.
	 */
	@Test(expected=NullPointerException.class)
	public void testUnindPropertyArgumentNullThrowsException() {
		controller.bind("bindable", mockCommand);
		controller.unbind(null, mockCommand);
	}
	
	/**
	 * Ensures the unbind() method throws a NullPointerException when null is passed in for the
	 * command argument.
	 */
	@Test(expected=NullPointerException.class)
	public void testUnbindCommandArgumentNullThrowsException() {
		controller.bind("bindable", mockCommand);
		controller.unbind("bindable", null);
	}
	
	/**
	 * Ensures the unbind() method throws an IllegalArgumentException when the controller does
	 * not contain a property with the specified property name.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testUnbindCommandNonexistantPropertyThrowsException() {
		controller.bind("bindable", mockCommand);
		controller.unbind("nonexistantProperty", mockCommand);
	}
	
	/**
	 * Ensures the unbind() method throws an IllegalArgumentException when the controller does
	 * not contain a binding for the specified property and command.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testUnbindCommandNotBoundThrowsException() {
		controller.bind("bindable", executeCommand);
		controller.unbind("bindable", mockCommand);
	}
	
	/**
	 * Ensures the unbind() method throws an IllegalArgumentException when the controller does
	 * not contain a binding for the specified property and command.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testUnbindPropertyNotBoundThrowsException() {
		controller.bind("bindable2", mockCommand);
		controller.unbind("bindable", mockCommand);
	}
	
	// the following tests are not able to be tested independently
	
	/**
	 * Ensures binding to a property and later changing that property triggers the expected event.
	 */
	@Test
	public void testBoundCommandExecutedWhenPropertyChanged() {
		controller.bind("bindable", executeCommand);
		controller.setBindable(1);
		assertEquals(executionCount, 1);
	}
	
	/**
	 * Ensures binding to a command and changing a different property does not trigger the bound command.
	 */
	@Test
	public void testBoundCommandNotExecutedWhenDifferentPropertyChanged() {
		controller.bind("bindable", executeCommand);
		controller.setBindable2(1);
		assertEquals(executionCount, 0);
	}
	
	/**
	 * Ensures when a command is bound twice it still only executes once.
	 */
	@Test
	public void testSameCommandBoundTwiceOnlyExecutesOnceWhenPropertyChanged() {
		controller.bind("bindable", executeCommand);
		controller.bind("bindable", executeCommand);
		controller.setBindable(1);
		assertEquals(executionCount, 1);
	}
	
	/**
	 * Ensures multiple bound commands will be executed when they are bound to the same property.
	 */
	@Test
	public void testMultipleBoundCommandsExecutedWhenPropertyChanged() {
		controller.bind("bindable", executeCommand);
		controller.bind("bindable", executeCommand2);
		controller.setBindable(1);
		assertEquals(executionCount, 1);
		assertEquals(executionCount2, 1);
	}
	
	/**
	 * Ensures nothing happens when no commands are bound to a bindable property and it is changed.
	 */
	@Test
	public void testNoBindingsPropertyChanged() {
		controller.setBindable(1);
		assertEquals(executionCount, 0);
		assertEquals(executionCount2, 0);
	}
	
	/**
	 * Ensures binding to an unbindable property will not execute the command when the proeprty chagnes.
	 */
	@Test
	public void testUnbindablePropertyDoesNotExecuteCommand() {
		controller.bind("unbindable", executeCommand);
		controller.setUnbindable(1);
		assertEquals(executionCount, 0);
	}
	
	/**
	 * Ensures the command is not executed after it is bound and then unbound when a property changes.
	 */
	@Test
	public void testBindUnbindDoesNotExcecuteCommandWhenPropertyChanged() {
		controller.bind("bindable", executeCommand);
		controller.unbind("bindable", executeCommand);
		controller.setBindable(1);
		assertEquals(executionCount, 0);
	}
	
	/**
	 * Ensures a command does not fire when two commands are bound, the specified command is unbound
	 * and the controller property changes.
	 */
	@Test
	public void testBindUnbindTwiceDoesNotExecuteCommandWhenPropertyChanged() {
		controller.bind("bindable", executeCommand);
		controller.unbind("bindable", executeCommand);
		controller.setBindable(1);
		assertEquals(executionCount, 0);
	}
	
	/**
	 * Ensures a command is fired when another command is bound to the provided property and then 
	 * unbound and the property changes. 
	 */
	@Test
	public void testBindTwiceUnbindExecutesDifferentCommandWhenPropertyChanged() {
		controller.bind("bindable", executeCommand);
		controller.bind("bindable", executeCommand2);
		controller.unbind("bindable", executeCommand);
		controller.setBindable(1);
		assertEquals(executionCount2, 1);
	}
	/**
	* Ensures binding, unbinding and then binding a command again to a specified property will 
	* execute the bound command.
	*/
	@Test
	public void testBindUnbindBindExecutesCommandWhenPropertyChanged() {
		controller.bind("bindable", executeCommand);
		controller.unbind("bindable", executeCommand);
		controller.bind("bindable", executeCommand);
		controller.setBindable(1);
		assertEquals(executionCount, 1);
	}
}
