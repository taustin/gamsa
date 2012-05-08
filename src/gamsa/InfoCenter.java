package gamsa;

import gamsa.scorer.Scorer;

import java.util.Random;

/**
   Singleton class.  This includes various utility methods (like a
   random number generator) and certain properties that we want to be
   globally available.
  
   @author Amie Radenbaugh and Tom Austin.
 */
public class InfoCenter
{
	protected static InfoCenter c_infoCenter = new InfoCenter();
	
	private Random i_rand;
	private Scorer i_scorer;
	
	/**
	   Gets the instance of this class.
	 */
	public static InfoCenter getCenter()
	{
		return c_infoCenter;
	}
	
	/**
	   Private constructor.
	 */
	private InfoCenter()
	{
		// Seeding random number generator.
		i_rand = new Random(System.currentTimeMillis());
	}
	
	/**
	   Returns a random integer between 0 and the specifed
	   number (including 0, excluding the specified integer).
	 */
	public int getRandomInt(int aRangeEnd)
	{
		return i_rand.nextInt(aRangeEnd);
	}

	/**
	   Returns the scorer.
	 */
	public Scorer getScorer()
	{
		return i_scorer;
	}

	/**
	   Sets the scorer to be used in comparing sequences..
	 */
	public void setScorer(Scorer scorer)
	{
		i_scorer = scorer;
	}
}
