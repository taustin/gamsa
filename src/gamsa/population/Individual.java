package gamsa.population;

/**
   Represents an individual in the population.  Each individual
   must have a way of measuring its fitness.
  
   @author Tom Austin and Amie Radenbaugh
 */
public abstract class Individual implements Comparable<Individual>
{
	/**
	   Returns a rating of the fitness of the individual, the more
	   positive = the more fit.
	 */
	public abstract double getFitness();
	
	/**
	   Caomparison betwwen two individuals is based on their fitness.
	   
	   @see java.lang.Comparable#compareTo(T)
	 */
	public int compareTo(Individual other)
	{
		double thisFitness = this.getFitness();
		double otherFitenss = other.getFitness();
		if (thisFitness > otherFitenss)
			return 1;
		else if (thisFitness < otherFitenss)
			return -1;
		else
			return 0;
	}
}
