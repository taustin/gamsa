package gamsa.operator;

import gamsa.InfoCenter;
import gamsa.population.Alignment;
import gamsa.population.Sequence;

public class OnePointCrossover extends Crossover
{
	public final static String GAPS_BEGINNING = "GapsBeginning";
	public final static String GAPS_MIDDLE = "GapsMiddle";
	public final static String GAPS_END = "GapsEnd";
	
	private String i_gapPlacement;
	
	public OnePointCrossover(String aGapPlacement)
	{
		i_gapPlacement = aGapPlacement;
	}
	
	/**
	 * @see gamsa.operator.Crossover#perform(gamsa.population.Alignment, gamsa.population.Alignment)
	 */
	@Override
	public Alignment[] perform(Alignment mother, Alignment father)
	{
		InfoCenter ic = InfoCenter.getCenter();		
		Alignment[] returnAligns = new Alignment[2];
		int crossoverPoint;
		Alignment newAlign1;
		Alignment newAlign2;
		Sequence tempSequence1;
		Sequence tempSequence2;		
		int tempSeq1NumberOfElements;
		int tempSeq2CrossPoint;
		int maxSeq2CrossPoint = 0;
		int minSeq2CrossPoint = 0;
		String newSubSequence1;
		String newSubSequence2;
		
		//System.out.println("mother: " + mother);
		//System.out.println("father: " + father);
		
		newAlign1 = new Alignment();
		newAlign2 = new Alignment();
		
		// get a random crossover point
		crossoverPoint = ic.getRandomInt(mother.getSequenceLength());
		//System.out.println("crossoverPoint: " + crossoverPoint);
		
		if (crossoverPoint != 0)
		{
			minSeq2CrossPoint = mother.getSequenceLength();
								
			// loop through the sequences of the selected alignments
			for (int s=0; s<mother.getSize(); s++)
			{
				tempSequence1 = mother.getSequenceAt(s);
				tempSequence2 = father.getSequenceAt(s);
				//System.out.println(tempSequence1);
				//System.out.println(tempSequence2);
					
				// get the number of elements in sequence 1 up to the crossover point
				tempSeq1NumberOfElements = tempSequence1.getNonGapCount(crossoverPoint);
				//System.out.println("tempSeq1NumberOfElements: " + tempSeq1NumberOfElements);
				
				// get the index of sequence 2 that has the same number of elements
				tempSeq2CrossPoint = tempSequence2.getIndexContainingElements(tempSeq1NumberOfElements);
				//System.out.println("tempSeq2CrossPoint: " + tempSeq2CrossPoint);
				
				if (maxSeq2CrossPoint <= tempSeq2CrossPoint)
				{							
					maxSeq2CrossPoint = tempSeq2CrossPoint;						
					//System.out.println("maxSeq2: " + maxSeq2CrossPoint);
				}
				
				if (minSeq2CrossPoint >= tempSeq2CrossPoint)
				{						
					minSeq2CrossPoint = tempSeq2CrossPoint;								
					//System.out.println("minSeq2: " + minSeq2CrossPoint);
				}
			}
			
			//System.out.println("maxSeq2CrossPoint: " + maxSeq2CrossPoint);						
			//System.out.println("minSeq2CrossPoint: " + minSeq2CrossPoint);
									
			// loop through the sequences of the selected alignments
			for (int s=0; s<mother.getSize(); s++)
			{
				tempSequence1 = mother.getSequenceAt(s);
				tempSequence2 = father.getSequenceAt(s);
				//System.out.println(tempSequence1);
				//System.out.println(tempSequence2);
					
				// get the number of elements in sequence 1 up to the crossover point
				tempSeq1NumberOfElements = tempSequence1.getNonGapCount(crossoverPoint);
				//System.out.println("tempSeq1NumberOfElements: " + tempSeq1NumberOfElements);
				
				// get the index of sequence 2 that has the same number of elements
				tempSeq2CrossPoint = tempSequence2.getIndexContainingElements(tempSeq1NumberOfElements);
				//System.out.println("tempSeq2CrossPoint: " + tempSeq2CrossPoint);																										
												
				newSubSequence1 = "";
				// add gaps to front
				if (i_gapPlacement.equalsIgnoreCase(GAPS_BEGINNING))
				{
					if ((minSeq2CrossPoint < tempSeq2CrossPoint))
					{
						for (int g=minSeq2CrossPoint; g<tempSeq2CrossPoint; g++)
						{
							newSubSequence1 += "-";
						}
					}
					newSubSequence1 += tempSequence1.getSubSequence(0, crossoverPoint);
					newSubSequence1 += tempSequence2.getSubSequence(tempSeq2CrossPoint, tempSequence2.getSize());					
				}
				// add gaps to middle
				else if (i_gapPlacement.equalsIgnoreCase(GAPS_MIDDLE))
				{					
					newSubSequence1 = tempSequence1.getSubSequence(0, crossoverPoint);
					if ((minSeq2CrossPoint < tempSeq2CrossPoint))
					{
						for (int g=minSeq2CrossPoint; g<tempSeq2CrossPoint; g++)
						{
							newSubSequence1 += "-";
						}
					}
					newSubSequence1 += tempSequence2.getSubSequence(tempSeq2CrossPoint, tempSequence2.getSize());
				}
				// add gaps to end
				else if (i_gapPlacement.equalsIgnoreCase(GAPS_END))
				{					
					newSubSequence1 = tempSequence1.getSubSequence(0, crossoverPoint);					
					newSubSequence1 += tempSequence2.getSubSequence(tempSeq2CrossPoint, tempSequence2.getSize());
					if ((minSeq2CrossPoint < tempSeq2CrossPoint))
					{
						for (int g=minSeq2CrossPoint; g<tempSeq2CrossPoint; g++)
						{
							newSubSequence1 += "-";
						}
					}
				}
				//System.out.println("newSubSequence1: " + newSubSequence1);
				
				// add gaps to front
				newSubSequence2 = "";
				if (i_gapPlacement.equals(GAPS_BEGINNING))
				{
					if (tempSeq2CrossPoint < maxSeq2CrossPoint)
					{
						for (int g=tempSeq2CrossPoint; g<maxSeq2CrossPoint; g++)
						{
							newSubSequence2 += "-";
						}
					}	
					newSubSequence2 += tempSequence2.getSubSequence(0, tempSeq2CrossPoint);							
					newSubSequence2 += tempSequence1.getSubSequence(crossoverPoint, tempSequence1.getSize());
				}
				// add gaps to middle
				else if (i_gapPlacement.equalsIgnoreCase(GAPS_MIDDLE))
				{					
					newSubSequence2 += tempSequence2.getSubSequence(0, tempSeq2CrossPoint);	
					if (tempSeq2CrossPoint < maxSeq2CrossPoint)
					{
						for (int g=tempSeq2CrossPoint; g<maxSeq2CrossPoint; g++)
						{
							newSubSequence2 += "-";
						}
					}	
					newSubSequence2 += tempSequence1.getSubSequence(crossoverPoint, tempSequence1.getSize());
				}
				// add gaps to end
				else if (i_gapPlacement.equalsIgnoreCase(GAPS_END))
				{
					newSubSequence2 += tempSequence2.getSubSequence(0, tempSeq2CrossPoint);						
					newSubSequence2 += tempSequence1.getSubSequence(crossoverPoint, tempSequence1.getSize());
					if (tempSeq2CrossPoint < maxSeq2CrossPoint)
					{
						for (int g=tempSeq2CrossPoint; g<maxSeq2CrossPoint; g++)
						{
							newSubSequence2 += "-";
						}
					}
				}
				//System.out.println("newSubSequence2: " + newSubSequence2);
				
				newAlign1.addSequence(new Sequence(newSubSequence1));
				newAlign2.addSequence(new Sequence(newSubSequence2));								
			}
			//System.out.println("resulting mother: " + newAlign1);
			//System.out.println("resulting fatehr: " + newAlign2);
			returnAligns[0] = newAlign1;
			returnAligns[1] = newAlign2;			
		}
		else
		{
			// crossover point is 0, so just return originals
			returnAligns[0] = mother;
			returnAligns[1] = father;
		}
		return returnAligns;
	}
	
	/**
	 * Sample main method to test this mutations.
	 */
	public static void main(String[] args)
	{
		InfoCenter.getCenter().setScorer(new gamsa.scorer.DNAScorer());
		Crossover xover = new OnePointCrossover(GAPS_MIDDLE);
		
		Alignment mom = new Alignment();
		//mom.addSequence(new Sequence("ac-tg"));
		//mom.addSequence(new Sequence("acttg"));
		
		
		mom.addSequence(new Sequence("ATTGC-CATT-"));
		mom.addSequence(new Sequence("ATGGCCATT--"));
		mom.addSequence(new Sequence("ATCCAATTTT-"));
		mom.addSequence(new Sequence("A-TCTTCTT--"));
		mom.addSequence(new Sequence("ACTGAC-C---"));
		mom.addSequence(new Sequence("GG-----CCAT"));
		mom.addSequence(new Sequence("ATTG-------"));
		
		
		Alignment dad = new Alignment();
		//dad.addSequence(new Sequence("ac-t-g---"));
		//dad.addSequence(new Sequence("-acttg---"));
		
		
		dad.addSequence(new Sequence("-ATTGCCATT-"));
		dad.addSequence(new Sequence("ATGGCCATT--"));
		dad.addSequence(new Sequence("ATCCAATTTT-"));
		dad.addSequence(new Sequence("A-T-CTTCTT-"));
		dad.addSequence(new Sequence("ACTGAC-C---"));
		dad.addSequence(new Sequence("--G-G--CCAT"));
		dad.addSequence(new Sequence("-------ATTG"));
		
		
		System.out.println(mom);
		System.out.println(dad);
		Alignment[] children = xover.perform(mom,dad);
		System.out.println("\t***RESULTS***");
		System.out.println(children[0]);
		System.out.println(children[1]);
	}
}
