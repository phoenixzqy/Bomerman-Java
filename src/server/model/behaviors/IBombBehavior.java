package server.model.behaviors;

/**
 * Indicates how a game object can place bombs.
 */
public interface IBombBehavior
{
	/**
	 * Returns true if this IBombBehavior can place a bomb and false otherwise.
	 * @return True if this IBombBehavior can place a bomb and false otherwise.
	 */
	public boolean canPlaceBomb();
	
	/**
	 * Returns true if this IBombBehavior should attempt to place a bomb.
	 * @return True if this IBombBehavior should attempt to place a bomb.  If this IBombBehavior
	 * can't place a bomb, the return value of this method is false.
	 */
	public boolean placeBomb();
	
	/**
	 * Sets the value of place bomb.
	 * @param placeBomb True if a bomb should be placed and false otherwise.
	 * @throws IllegalStateException Thrown if this IBombBehavior can't place a bomb.
	 */
	public void setPlaceBomb(boolean placeBomb);
	
	/**
	 * Return the current number of bombs
	 * @return bombCout Number of bomb 
	 */
	public int countBomb();
	
	/**
	 * Decrements the number of bomb until destruction by one. If the number of bomb is already 0, then
	 * do nothing. If this object can not place bomb,this method does nothing.
	 *  @throws IllegalStateException Thrown if the number of bomb count is already 0.
	 */
	public void decrementBomb();
	
	/**
	 * Increments the number of bomb until destruction by one. If the current number of bomb is the max number,
	 * then do nothing. If this object can not place bomb, this method does nothing.
	 * @throws IllegalStateException Thrown if the number of bomb count is already max.
	 */
	public void incrementBomb();
}
