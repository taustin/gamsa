package gamsa.scorer;

import gamsa.population.Sequence;

/**
   Interface for scoring two sequences.
   @author Tom Austin and Amie Radenbaugh.
 */
public interface Scorer
{
	/**
	   Returns a score for the 2 sequences, as they are aligned.
	 */
	public float compareSequences(Sequence s1, Sequence s2);
}
