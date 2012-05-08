package gamsa.operator;

import gamsa.population.Alignment;

/**
   Represents an operator that takes one individual
   and produces another.
   
   @author Tom Austin and Amie Radenbaugh
 */
public abstract class Mutation extends Operator
{
	/**
	   Takes one Alignment and returns a mutation derived from it.
	 */
	public abstract Alignment perform(Alignment parent);
	
	/**
	   Wrapper around perform method.
	   @see gamsa.operator.Operator#performOp(gamsa.population.Alignment[])
	 */
	public Alignment[] performOp(Alignment... parents)
	{
		if (parents.length != 1)
			throw new IllegalArgumentException("Mutations can only use one parent.");
		
		Alignment[] child = new Alignment[1];
		child[0] = perform(parents[0]);
		return child;
		
	}
}
