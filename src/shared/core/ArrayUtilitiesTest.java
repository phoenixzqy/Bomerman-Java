package shared.core;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Provides several useful methods for arrays.
 */
public class ArrayUtilitiesTest 
{
	/**
	 * Ensures the contains method throws an exception if the provided array is null.
	 */
	@Test(expected=NullPointerException.class)
	public void testContainsArrayNullThrowsException()
	{
		ArrayUtilities.contains(null, 1);
	}
	
	/**
	 * Ensures the contains method returns false if the array is empty.
	 */
	@Test
	public void testContainsReturnsFalseWhenArrayEmpty()
	{
		assertFalse(ArrayUtilities.contains(new Integer[0], 0));
	}
	
	/**
	 * Ensure the contains method returns true if the array has one element and it contains the
	 * object.
	 */
	@Test
	public void testContainsReturnsTrueIfArrayHasOneElementAndContainsObject()
	{
		Integer[] array = { new Integer(1) };
		assertTrue(ArrayUtilities.contains(array, new Integer(1)));
	}
	
	/**
	 * Ensure the contains method returns false if the array has one element and it is not the 
	 * object.
	 */
	@Test
	public void testContainsReturnsFalseIfArrayHasOneElementAndDoesntContainObject()
	{
		Integer[] array = { new Integer(1) };
		assertFalse(ArrayUtilities.contains(array, new Integer(2)));
	}
	
	/**
	 * Ensure the contains method returns true if null is provided for the object, the array has 
	 * one element and it contains null.
	 */
	@Test
	public void testContainsReturnsTrueIfNullObjectAndArrayHasOneElementAndContainsNull()
	{
		Integer[] array = { null };
		assertTrue(ArrayUtilities.contains(array, null));
	}
	
	/**
	 * Ensure the contains method returns false if null is provided for the object, the array has 
	 * one element and does not contain null. 
	 */
	@Test
	public void testContainsReturnsFalseIfNullObjectAndArrayHasOneElementAndDoesntContainNull()
	{
		Integer[] array = { new Integer(2) };
		assertFalse(ArrayUtilities.contains(array, null));
	}
	
	/**
	 * Ensures the contains method return true if the array has two elements and contains the object.
	 */
	@Test
	public void testContainsReturnsTrueIfArrayHasTwoElementsAndContainsObject()
	{
		Integer[] array1 = { new Integer(1), new Integer(2) };
		assertTrue(ArrayUtilities.contains(array1, new Integer(1)));

		Integer[] array2 = { new Integer(2), new Integer(1) };
		assertTrue(ArrayUtilities.contains(array2, new Integer(1)));

		Integer[] array3 = { new Integer(1), new Integer(1) };
		assertTrue(ArrayUtilities.contains(array3, new Integer(1)));
	}
	
	/**
	 * Ensure the contains method returns false if the array has two elements and does not contain
	 * the object.
	 */
	@Test
	public void testContainsReturnsFalseIfArrayHasTwoElementsAndDoesntContainObject()
	{
		Integer[] array = { new Integer(2), new Integer(3) };
		assertFalse(ArrayUtilities.contains(array, new Integer(1)));
	}
	
	/**
	 * Ensures the contains method return true if null is provided for the object, the array has 
	 * two elements and contains null.
	 */
	@Test
	public void testContainsReturnsTrueIfObjectNullAndArrayHasTwoElementsAndContainsNull()
	{
		Integer[] array1 = { null, new Integer(2) };
		assertTrue(ArrayUtilities.contains(array1, null));

		Integer[] array2 = { new Integer(2), null };
		assertTrue(ArrayUtilities.contains(array2, null));

		Integer[] array3 = { null, null };
		assertTrue(ArrayUtilities.contains(array3, null));
	}
	
	/**
	 * Ensure the contains method returns false if null is provided for the object, the array has 
	 * two elements and does not contain null.
	 */
	@Test
	public void testContainsReturnsFalseIfObjectNullAndArrayHasTwoElementsAndDoesntContainNull()
	{
		Integer[] array = { new Integer(2), new Integer(3) };
		assertFalse(ArrayUtilities.contains(array, null));
	}
	
	/**
	 * Ensures the contains method return true if the array has three elements and contains the
	 * object.
	 */
	@Test
	public void testContainsReturnsTrueIfArrayHasThreeElementsAndContainsObject()
	{
		Integer[] array1 = { new Integer(1), new Integer(2), new Integer(3) };
		assertTrue(ArrayUtilities.contains(array1, new Integer(1)));

		Integer[] array2 = { new Integer(2), new Integer(1), new Integer(3) };
		assertTrue(ArrayUtilities.contains(array2, new Integer(1)));

		Integer[] array3 = { new Integer(3), new Integer(2), new Integer(1) };
		assertTrue(ArrayUtilities.contains(array3, new Integer(1)));

		Integer[] array4 = { new Integer(1), new Integer(1), new Integer(3) };
		assertTrue(ArrayUtilities.contains(array4, new Integer(1)));

		Integer[] array5 = { new Integer(1), new Integer(2), new Integer(1) };
		assertTrue(ArrayUtilities.contains(array5, new Integer(1)));

		Integer[] array6 = { new Integer(2), new Integer(1), new Integer(1) };
		assertTrue(ArrayUtilities.contains(array6, new Integer(1)));

		Integer[] array7 = { new Integer(1), new Integer(1), new Integer(1) };
		assertTrue(ArrayUtilities.contains(array7, new Integer(1)));
	}
	
	/**
	 * Ensure the contains method returns false if the array has three elements and does not 
	 * contain the object.
	 */
	@Test
	public void testContainsReturnsFalseIfArrayHasThreeElementsAndDoesntContainObject()
	{
		Integer[] array = { new Integer(2), new Integer(3), new Integer(4) };
		assertFalse(ArrayUtilities.contains(array, new Integer(1)));
	}
	
	/**
	 * Ensures the contains method return true if null is provided for the object, the array has 
	 * three elements and contains null.
	 */
	@Test
	public void testContainsReturnsTrueIfNullObjectAndArrayHasThreeElementsAndContainsNull()
	{
		Integer[] array1 = { null, new Integer(2), new Integer(3) };
		assertTrue(ArrayUtilities.contains(array1, null));

		Integer[] array2 = { new Integer(2), null, new Integer(3) };
		assertTrue(ArrayUtilities.contains(array2, null));

		Integer[] array3 = { new Integer(3), new Integer(2), null };
		assertTrue(ArrayUtilities.contains(array3, null));

		Integer[] array4 = { null, null, new Integer(3) };
		assertTrue(ArrayUtilities.contains(array4, null));

		Integer[] array5 = { null, new Integer(2), null };
		assertTrue(ArrayUtilities.contains(array5, null));

		Integer[] array6 = { new Integer(2), null, null };
		assertTrue(ArrayUtilities.contains(array6, null));

		Integer[] array7 = { null, null, null };
		assertTrue(ArrayUtilities.contains(array7, null));
	}
	
	/**
	 * Ensure the contains method returns false if null is provided for the object, the array has 
	 * three elements and does not contain null.
	 */
	@Test
	public void testContainsReturnsFalseIfNullObjectAndArrayHasThreeElementsAndDoesntContainNull()
	{
		Integer[] array = { new Integer(2), new Integer(3), new Integer(4) };
		assertFalse(ArrayUtilities.contains(array, null));
	}
	
	/**
	 * Ensures the join method throws a NullPointerException when provided with a null string array.
	 */
	@Test(expected=NullPointerException.class)
	public void testJoinStringArrayNull()
	{
		ArrayUtilities.join(null, "A");
	}
	
	/**
	 * Ensures the join method throws a NullPointerException when provided with a null join string.
	 */
	@Test(expected=NullPointerException.class)
	public void testJoinJoinStringNull()
	{
		ArrayUtilities.join(new String[] { "A" }, null);
	}
	
	/**
	 * Ensures the join method throws an exception when the first string in strings array is null.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testJoinStringArrayFirstStringNull()
	{
		ArrayUtilities.join(new String[] { null, "A", "A" }, "A");
	}
	
	/**
	 * Ensures the join method throws an exception when the second string in strings array is null.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testJoinStringArraySecondStringNull()
	{
		ArrayUtilities.join(new String[] { "A", null, "A" }, "A");
	}
	
	/**
	 * Ensures the join method throws an exception when the third string in strings array is null.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testJoinStringArrayThreeStringNull()
	{
		ArrayUtilities.join(new String[] { "A", "A", null }, "A");
	}
	
	/**
	 * Ensures the join method returns an empty string when the strings array is empty.
	 */
	@Test
	public void testJoinStringsArrayEmpty()
	{
		String result = ArrayUtilities.join(new String[0], "A");
		assertEquals("", result);
	}
	
	/**
	 * Ensures the join method returns the contents of the strings array when there is only one
	 * element.
	 */
	@Test
	public void testJoinStringsArrayOneElement()
	{
		String result = ArrayUtilities.join(new String[] {"A"}, "B");
		assertEquals("A", result);
	}
	
	/**
	 * Ensures the join method joins just two elements without anything between them when the join
	 * string is empty.
	 */
	@Test
	public void testJoinTwoElementsJoinStringEmpty()
	{
		String result = ArrayUtilities.join(new String[] { "CA", "T" }, "");
		assertEquals("CAT", result);
	}
	
	/**
	 * Ensures the join method joins two elements with a join string between them when the join
	 * string is not empty.
	 */
	@Test
	public void testJoinTwoElementsJoinStringNotEmpty()
	{
		String result = ArrayUtilities.join(new String[] { "RA", "OR" }, "PT");
		assertEquals("RAPTOR", result);
	}
	
	/**
	 * Ensures the join method joins three elements without anything between them when the join
	 * string is empty.
	 */
	@Test
	public void testJoinThreeElementsJoinStringEmpty()
	{
		String result = ArrayUtilities.join(new String[] { "OP", "R", "AH" }, "");
		assertEquals("OPRAH", result);
	}
	
	/**
	 * Ensures the join method joins three elements with a join string between them when the join
	 * string is not empty.
	 */
	@Test
	public void testJoinThreeElementsJoinStringNotEmpty()
	{
		String result = ArrayUtilities.join(new String[] { "o", "mo", "pia" }, "no");
		assertEquals("onomonopia", result);
	}
}
