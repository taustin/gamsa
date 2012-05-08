package gamsa.operator;

import gamsa.population.Alignment;
import gamsa.population.Individual;

/**
   This class represents a transformation that can happen
   to individuals in the population.
   
   @author Tom Austin and Amie Radenbaugh
 */
public abstract class Operator extends Individual
{
	private double i_fitness;
	
	//value so far this round.
	private double i_currentGenerationScore;
	private int i_useCount;
	
	/**
	   Constructor.
	 */
	public Operator()
	{
		i_fitness = 0;
		i_currentGenerationScore = 0;
		i_useCount = 0;
	}
	
	/**
	   Add the specified score
	 */
	public void addScore(double d)
	{
		i_currentGenerationScore += d;
		i_useCount++;
	}
	
	/**
	   The fitness value for an operator is determined by its
	   recent history in producing successful matches.
	   
	   @see gamsa.population.Individual#getFitness()
	 */
	public double getFitness()
	{
		i_fitness = (2 * i_fitness + (i_currentGenerationScore)) / 3;
		
		i_currentGenerationScore = 0;
		i_useCount = 0;
		
		return i_fitness;
	}
	
	/**
	   This method is used when the process does not wish to distinguish
	   between Crossovers and Mutations.
	 */
	public abstract Alignment[] performOp(Alignment... parents);
}
