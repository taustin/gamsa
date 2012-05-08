package gamsa.population;

import gamsa.InfoCenter;
import gamsa.scorer.Scorer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
   An alignment of multiple sequences.
  
   @author Tom Austin and Amie Radenbaugh
 */
public class Alignment extends Individual implements Cloneable, Iterable<Sequence>
{
	//List of Sequences that make up the alignment.
	private List<Sequence> i_sequences;
	
	//Determines how to compare sequences.
	private Scorer i_scorer;
	
	private Double i_cachedFitness = null;
	
	/**
	    Creates an empty alignment.
	 */
	public Alignment()
	{
		i_sequences = new ArrayList<Sequence>();
		
		//Get the scorer to use.
		i_scorer = InfoCenter.getCenter().getScorer();
	}
	
	/**
	   Takes a list of sequences and aligns them, spaced out
	   to the specified length.
	 */
	public Alignment(String[] aSequencesArray, int length)
	{
		this();
		
		//Add all sequences to the alignment
		for (String sequence : aSequencesArray)
		{
			i_sequences.add(new Sequence(sequence, length));
		}
	}
    
    /**
       Clones this sequence.
     */
    @Override
    public Alignment clone()
    {
        Alignment clonedAlign = new Alignment();
        
        for (Sequence s : this.i_sequences)
        {
            clonedAlign.i_sequences.add(s.clone());
        }
        clonedAlign.i_scorer = this.i_scorer;
        
        return clonedAlign;
    }
	
    /**
       Returns a string with the sequences aligned, '-' representing
       a gap.  Also includes score of the alignment.
     */
    @Override
	public String toString()
	{
		String s = "Individual:\n";
		for (Sequence seq : i_sequences)
		{
			s += seq.toString() + "\n";
		}
		s += "Fitness: " + getFitness();
		return s;
	}
	
	/**
	   Calculate the fitness for the aligned sequences.
	 */
	public double getFitness()
	{
		if (i_cachedFitness != null)
			return i_cachedFitness.doubleValue();
		
		double fitness = 0;
		Sequence comparingSequence;
		Sequence tempSequence;
		
		//Compare each sequence with every other.
		for (int i=0; i<i_sequences.size(); i++)
		{
			comparingSequence = i_sequences.get(i);
			for (int j=i+1; j<i_sequences.size(); j++)
			{
				tempSequence = i_sequences.get(j);
				fitness += i_scorer.compareSequences(comparingSequence, tempSequence);
			}
		}
		i_cachedFitness = new Double(fitness);
		return fitness;
	}
	
    /**
	 * Returns the number of sequences in the alignment.
	 */
	public int getSize()
	{
		return i_sequences.size();
	}

	/**
	 * Returns the length of the sequences in the alignment.
	 */
	public int getSequenceLength()
	{
		return i_sequences.get(0).getSize();
	}

	/**
	 * Returns the sequence at a certain index.
	 */
	public Sequence getSequenceAt(int anIndex)
	{
		return i_sequences.get(anIndex);
	}
	
	/**
	   Checks if the specified sequence represent the same alignment.
	   @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other)
	{
		if (!(other instanceof Alignment)) return false;
		//Crude comparison -- we check the string representations.
		return this.toString().equals(other.toString());
	}
	
	/**
	   Adds a sequence to the alignment in the next available index.
	 */
	public void addSequence(Sequence aSequence)
	{
		i_cachedFitness = null;
		i_sequences.add(aSequence);
	}

	/**
	   Sets the sequence at the specified position.
	 */
	public void setSequenceAt(int seqNumber, Sequence seq)
	{
		i_cachedFitness = null;
        i_sequences.set(seqNumber, seq);
	}

	/**
	   Returns an iterator for the sequences in this alignment.
	   @see java.lang.Iterable#iterator()
	 */
    public Iterator<Sequence> iterator() {
        return i_sequences.iterator();
    }

	/**
	   Verifies that this alignment is a valid permutation
	   of its parent.
	 */
	public boolean verifyAlignment(Alignment parent)
	{
		String s1;
		String s2;
		for (int i=0; i<i_sequences.size(); i++)
		{
			//Comapring sequences with gaps removed.
			s1 = i_sequences.get(i).toString().replaceAll("-","");
			s2 = parent.i_sequences.get(i).toString().replaceAll("-","");
			if (!s1.equals(s2))
				return false;
		}
		return true;
	}
}