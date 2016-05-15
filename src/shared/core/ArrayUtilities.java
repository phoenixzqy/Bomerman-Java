package shared.core;

/**
 * Provides useful helper methods for arrays.
 */
public class ArrayUtilities 
{
	/**
	 * Determines if the provided array contains the provided object.
	 * @param array The array to check.
	 * @param object The object to check.
	 * @return Returns true if the provided array contains the provided object.
	 */
	public static <E> boolean contains(E[] array, E object)
	{
		// if object is null, search for an array element that is also null
		if (object == null)
		{
			for (E arrayObject : array)
			{
				if (arrayObject == null)
					return true;
			}
		}
		else
		{
			// object is not null, search for an element such that .equals() returns true
			for (E arrayObject : array)
			{
				if (object.equals(arrayObject))
					return true;
			}
		}
		
		// no match was found
		return false;
	}
	
	/**
	 * Joins all of the strings in the provided array together with the provided join string.  For
	 * example, if ["C", "L", "R"] is provided, and the join string is "O", then the output will
	 * be "COLOR".
	 * @param stringArray The array of strings to join.
	 * @param joinString The string used to join the string array.
	 * @return Returns an array of joined strings.
	 * @throws NullPointerException Thrown if stringArray or joinString is null.
	 * @throws IllegalArgumentException Thrown if any of the string in stringArray are null.
	 */
	public static String join(String[] stringArray, String joinString)
	{
		if (stringArray == null || joinString == null)
			throw new NullPointerException();
		
		// search for null strings, which cannot be joined
		for (String string : stringArray)
			if (string == null)
				throw new IllegalArgumentException();
		
		// return empty string for an empty array
		if (stringArray.length == 0)
			return "";
		
		// append the strings with the joining string in between each element
		String baseString = stringArray[0];
		for (int i = 1; i < stringArray.length; i++)
			baseString += (joinString + stringArray[i]);
		
		return baseString;
	}
}
