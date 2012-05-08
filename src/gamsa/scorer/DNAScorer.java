package gamsa.scorer;

import gamsa.population.Sequence;

/**
 * Returns the score between 2 given DNA sequences.  Loops through
 * each nucleotide in the sequence and compares it to the nucleotide
 * in the same column of the other sequence.  
 * 
 * @author Amie Radenbaugh and Tom Austin
 */
public class DNAScorer implements Scorer
{	
	/**
	 * Returns the score between 2 given DNA sequences.  Loops through
	 * each nucleotide in the sequence and compares it to the nucleotide
	 * in the same column of the other sequence.  In order
	 * to have positive fitness scores, the following scoring 
	 * function will be used for DNA sequences:
	 * 		1) 	If one of the nucleotides is a gap and the other is not, 
	 * 			then we will score -2.  However, leading and trailing
	 * 			gaps will be only counted as -1.
	 * 		2)  If neither nucleotide is a gap and there is a mismatch, 
	 * 			we will score -1.
	 * 		3)  If both of the nucleotides are gaps, we don't want to
	 * 			reward or penalize.  They will be scored as 0.
	 * 		4)  If the nucleotides match, we score +1.
	 */
	public float compareSequences(Sequence seq1, Sequence seq2)
	{
		float score = 0;
		
		// Translate leading and trailing gaps so that we know to ignore them
		seq1 = seq1.getSequenceWithEdgeGapsTranslated();
		seq2 = seq2.getSequenceWithEdgeGapsTranslated();
		
		for (int i=0;i<seq1.getSize();i++)
		{
			char comparingChar = seq1.getElementAt(i);
			char tempChar = seq2.getElementAt(i);
			if (comparingChar == '-' && tempChar == '-')
			{
				// aligned gaps -- ignore.
			}
			else if (comparingChar == '.' || tempChar == '.')
			{
				// Leading or trailing gaps.  Punish these, but at a lesser rate.
				score -= 1;
			}
			else if (comparingChar == tempChar)
			{
				// match
				score += 1;
			}
			else if (comparingChar == '-' || tempChar == '-')
			{
				// gap in one.
				score -= 2;
			}
			else
			{
				// mismatch
				score += -1;
			}
		}
		
		return score;
	}
	
}