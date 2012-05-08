package gamsa.population;

import gamsa.InfoCenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
   A collection of individuals.
   
   @author Tom Austin and Amie Radenbaugh
 */
public class Population<T extends Individual> implements Iterable<T>{

	private List<T> i_individuals = new ArrayList<T>();
	
	//Wheel specifies the chance of selecting a given individual.
	private RouletteWheel i_wheel = null;
	
	private InfoCenter i_rand = InfoCenter.getCenter();
	
	/**
	   Wheel to determine which entity makes it to the new generation.
	   Can be used for alignments or operators.
	 */
	private class RouletteWheel
	{
		private Individual[] wheel;
		
		/**
		   Constructor.
		 */
		public RouletteWheel()
		{
			wheel = new Individual[i_individuals.size() * 10];
		}
		
		/**
		   Calculates the probability of selecting each individual, based
		   on their fitness.  Every individual is given at least some chance
		   of being selected.
		 */
		private void recalculate()
		{
			int i = 0;
			
			//The remainder of the slots are divided up according to fitness
			long offset = calculateOffset();
			
			//Every resident of the population gets at least one slot
			// we also calculat the total score at the same time.
			double totalScore = 0;
			for (T ind : i_individuals)
			{
				wheel[i++] = ind;
				totalScore += ind.getFitness() + offset;
			}
			
			int freeSlots = wheel.length - i_individuals.size();
			
			double percentage;
			long numSlices;
			for (T ind : i_individuals)
			{
				percentage = (ind.getFitness() + offset) / totalScore;
				numSlices = Math.round(percentage * freeSlots);
				while (numSlices > 0 && i < wheel.length)
				{
					wheel[i++] = ind;
					numSlices--;
				}
			}
			
			//Add the best one a few more times if there are any spaces left over
			while (i < wheel.length)
			{
				wheel[i++] = best();
			}
		}
		
		/**
		   This calcluates an offset value.  This will make all fitness
		   values positive.
		 */
		private long calculateOffset()
		{
			long worstFitness = Math.round(worst().getFitness());
			if (worstFitness < 0)
				return worstFitness * -1;
			else
				return 1;
		}
		
		/**
		   Randomly returns an object from the wheel.
		 */
		@SuppressWarnings("unchecked")
		public T spin()
		{
			int n = i_rand.getRandomInt(wheel.length);
			return (T) wheel[n];
		}
		
		/**
		   Returns a string representing the distribution of
		   the population in the wheel.  Useful for troubleshooting.
		   @see java.lang.Object#toString()
		 */
		public String toString()
		{
			//TODO: Nice if each individual was not printed twice,
			// but I'd rather do that than slow down performance
			// or miss an operator alltogether.
			String s = "WHEEL DIST: offset=" + calculateOffset();
			Individual lastIndividual = null;
			Individual individ;
			for (int i=0; i<wheel.length; i++)
			{
				individ = wheel[i];
				if (!individ.equals(lastIndividual))
					s+= "\n\t(" + individ.toString() + ") " + individ.getFitness() + ": ";
				lastIndividual = individ;
				s += "X";
			}
			return s;
		}
	}
	////End of wheel class.
	
	
	/**
	   Default constructor.
	 */
	public Population(){}
	
	/**
	   Returns the best solution in the current population.
	 */
	public T best()
	{
		return Collections.max(i_individuals);
	}
	
	/**
	   Returns the best solution in the current population.
	 */
	public T worst() {
		return Collections.min(i_individuals);
	}
	
	/**
	   Returns number of alignments in the population.
	 */
	public int getPopulationSize()
	{
		return i_individuals.size();
	}
	
	/**
	   Returns a list of the alignments in the population.
	 */
	public List<T> getIndividuals()
	{
		return i_individuals;
	}
	
	/**
	   Returns the specified individual.
	 */
	public T getIndividualAt(int anIndex)
	{
		return i_individuals.get(anIndex);
	}
	
	/**
	   Adds the specified individual to the population.
	 */
	public void addIndividual(T anIndividual)
	{
		i_individuals.add(anIndividual);
	}
	
	/**
	   Adds the specified individuals to the population.
	 */
	public void addIndividuals(T... individuals)
	{
		for (T ind : individuals)
			i_individuals.add(ind);
	}
	
	/**
	   Adds all individuals from the specified populations to
	   this population.  Useful for merging populations.
	 */
	public void addAllIndividuals(Population<? extends T>... otherPops)
	{
		for (Population<? extends T> other : otherPops)
		{
			this.i_individuals.addAll(other.i_individuals);
		}
	}
	
	/**
	   Returns the fitness of the population.
	 */
	public float getPopulationFitness()
	{
		float fitness = 0;
		
		for (T indiv : i_individuals)
		{
			fitness += indiv.getFitness();
		}
		return fitness;
	}
	
	/**
	   Returns a randomly selected individual.  A more fit
	   individual is more likely to be returned, but every
	   individual in the population has at least some chance
	   of being selected.
	 */
	public T getRandomIndividual()
	{
		if (i_wheel == null)
		{
			i_wheel = new RouletteWheel();
			i_wheel.recalculate();
		}
		return i_wheel.spin();
	}
	
	/**
	   Recalculates the probabilities for individuals
	   being returned.  This is needed when the fitness
	   values of the individuals have changed.
	 */
	public void recalculateProbabilities()
	{
		i_wheel.recalculate();
	}
	
	/**
	   Returns a String that shows the probability of
	   selecting different individuals from the population.
	 */
	public String getDistributionString()
	{
		return i_wheel.toString();
	}

	/**
	   Returns an iterator that iterates over the list of
	   elements (and allows use the simplified for loop).
	   
	   @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator()
	{
		return i_individuals.iterator();
	}
}
