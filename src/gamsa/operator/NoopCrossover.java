package gamsa.operator;

import gamsa.population.Alignment;

/**
   Crossover that does nothing.
   
   @author Tom Austin and Amie Radenbaugh
 */
public class NoopCrossover extends Crossover
{
	/**
	 @see gamsa.operator.Crossover#perform(gamsa.population.Alignment, gamsa.population.Alignment)
	 */
	public Alignment[] perform(Alignment mother, Alignment father)
	{
		Alignment[] alarr = {mother, father};
		return alarr;
	}
}
