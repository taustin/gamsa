package gamsa.scorer;

import java.util.HashMap;
import java.util.Map;

import gamsa.population.Sequence;

/**
   Uses BLOSUM62 to score protein sequences.
   Starting a gap costs -12.  Continuing a gap costs -4.
   However, leading and trailing gaps are only punished at half
   of that rate.
   
   @author Tom Austin and Amie Radenbaugh
 */
public class Blosum62Scorer implements Scorer
{
	private static final int GAP_START_PENALTY = -12;
	private static final int GAP_CONTINUE_PENALTY = -4;
	
	private final static String[] PROTEIN_SYMBOLS =
		{"C", "S", "T", "P", "A", "G", "N", "D", "E", "Q", "H", "R", "K", "M", "I", "L", "V", "F", "Y", "W"};
	
	//	 C,  S,  T,  P,  A,  G,  N,  D,  E,  Q,  H,  R,  K,  M,  I,  L,  V,  F,  Y,  W
	private final static int[][] SCORING_MATRIX =
		{{ 9, -1, -1, -3,  0, -3, -3, -3, -4, -3, -3, -3, -3, -1, -1, -1, -1, -2, -2, -2},  //C
		 {-1,  4,  1, -1,  1,  0,  1,  0,  0,  0, -1, -1,  0, -1, -2, -2, -2, -2, -2, -3},  //S
		 {-1,  1,  4,  1, -1,  1,  0,  1,  0,  0,  0, -1,  0, -1, -2, -2, -2, -2, -2, -3},  //T
		 {-3, -1,  1,  7, -1, -2, -1, -1, -1, -1, -2, -2, -1, -2, -3, -3, -2, -4, -3, -4},  //P
		 { 0,  1, -1, -1,  4,  0, -1, -2, -1, -1, -2, -1, -1, -1, -1, -1, -2, -2, -2, -3},  //A
		 {-3,  0,  1, -2,  0,  6, -2, -1, -2, -2, -2, -2, -2, -3, -4, -4,  0, -3, -3, -2},  //G
		 {-3,  1,  0, -2, -2,  0,  6,  1,  0,  0, -1,  0,  0, -2, -3, -3, -3, -3, -2, -4},  //N
		 {-3,  0,  1, -1, -2, -1,  1,  6,  2,  0, -1, -2, -1, -3, -3, -4, -3, -3, -3, -4},  //D
		 {-4,  0,  0, -1, -1, -2,  0,  2,  5,  2,  0,  0,  1, -2, -3, -3, -3, -3, -2, -3},  //E
		 {-3,  0,  0, -1, -1, -2,  0,  0,  2,  5,  0,  1,  1,  0, -3, -2, -2, -3, -1, -2},  //Q
		 {-3, -1,  0, -2, -2, -2,  1,  1,  0,  0,  8,  0, -1, -2, -3, -3, -2, -1,  2, -2},  //H
		 {-3, -1, -1, -2, -1, -2,  0, -2,  0,  1,  0,  5,  2, -1, -3, -2, -3, -3, -2, -3},  //R
		 {-3,  0,  0, -1, -1, -2,  0, -1,  1,  1, -1,  2,  5, -1, -3, -2, -3, -3, -2, -3},  //K
		 {-1, -1, -1, -2, -1, -3, -2, -3, -2,  0, -2, -1, -1,  5,  1,  2, -2,  0, -1, -1},  //M
		 {-1, -2, -2, -3, -1, -4, -3, -3, -3, -3, -3, -3, -3,  1,  4,  2,  1,  0, -1, -3},  //I
		 {-1, -2, -2, -3, -1, -4, -3, -4, -3, -2, -3, -2, -2,  2,  2,  4,  3,  0, -1, -2},  //L
		 {-1, -2, -2, -2,  0, -3, -3, -3, -2, -2, -3, -3, -2,  1,  3,  1,  4, -1, -1, -3},  //V
		 {-2, -2, -2, -4, -2, -3, -3, -3, -3, -3, -1, -3, -3,  0,  0,  0, -1,  6,  3,  1},  //F
		 {-2, -2, -2, -3, -2, -3, -2, -3, -2, -1,  2, -2, -2, -1, -1, -1, -1,  3,  7,  2},  //Y
		 {-2, -3, -3, -4, -3, -2, -4, -4, -3, -2, -2, -3, -3, -1, -3, -2, -3,  1,  2,  11}};//W
	
	//Used for a convenient lookup.
	private static Map<String,Integer> i_scoreMap;
	
	/**
	   Constructor.
	 */
	public Blosum62Scorer()
	{
		i_scoreMap = new HashMap<String,Integer>(); 
		for (int i=0; i<PROTEIN_SYMBOLS.length; i++)
		{
			String protein1 = PROTEIN_SYMBOLS[i];
			for (int j=0; j<PROTEIN_SYMBOLS.length; j++)
			{
				String protein2 = PROTEIN_SYMBOLS[j];
				i_scoreMap.put(protein1+protein2, SCORING_MATRIX[i][j]);
				i_scoreMap.put(protein2+protein1, SCORING_MATRIX[j][i]);
			}
		}
	}
	
	/**
	   Calculates score of 2 sequences using BLOSUM62.
	   @see gamsa.scorer.Scorer#compareSequences(gamsa.population.Sequence, gamsa.population.Sequence)
	 */
	public float compareSequences(Sequence seq1, Sequence seq2)
	{
		float score = 0;
		
		boolean s1Started = false;
		boolean s2Started = false;
		
		//Counters for the size of a gap.
		int s1GapCount = 0;
		int s2GapCount = 0;
		
		for (int i=0;i<seq1.getSize();i++)
		{
			char[] proteins = new char[2];
			proteins[0] = seq1.getElementAt(i);
			proteins[1] = seq2.getElementAt(i);
			String key = new String(proteins).toUpperCase();
			
			int val = 0;
			//No gaps
			if (i_scoreMap.containsKey(key))
			{
				val = i_scoreMap.get(key);

				//Gap penalties are assessed when the gap is finished.
				// Leading gaps are not counted
				val += scoreGaps(s1Started, s2Started, s1GapCount, s2GapCount);
				s1GapCount = s2GapCount = 0;
				s1Started = true;
				s2Started = true;
			}
			//Gaps
			else
			{
				//Gap penalties are not assessed until we know the size of the gap.
				val = 0;
				
				//Both gaps -- ignore
				if (key.equals("--")) {;}
				else if (proteins[0] == '-')
				{
					s1GapCount++;
					s2Started = true;
				}
				else
				{
					s2GapCount++;
					s1Started = true;
				}
			}
			score += val;
		}
		//Score any trailing gaps, at half penalty.
		score += scoreGaps(false, false, s1GapCount, s2GapCount);
		return score;
	}

	/**
	   Returns the penalty for the number of gaps.
	   Leading and trailing gaps are punished only half, so the
	   first 2 arguments specify that for each sequence.
	 */
	private int scoreGaps(boolean s1HalvePenalty, boolean s2HalvePenalty, int s1GapCount, int s2GapCount) {
		int gapPenalty = 0;
		if (s1GapCount != 0)
		{
			gapPenalty = GAP_START_PENALTY;
			gapPenalty += GAP_CONTINUE_PENALTY * (s1GapCount - 1);
			//The penalty for leading gaps is only half.
			if (!s1HalvePenalty)
				gapPenalty /= 2;
				
		}
		if (s2GapCount != 0)
		{
			gapPenalty = GAP_START_PENALTY;
			gapPenalty += GAP_CONTINUE_PENALTY * (s2GapCount - 1);
			//The penalty for leading gaps is only half.
			if (!s2HalvePenalty)
				gapPenalty /= 2;
		}
		return gapPenalty;
	}
}