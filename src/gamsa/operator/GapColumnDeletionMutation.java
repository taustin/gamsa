package gamsa.operator;

import gamsa.InfoCenter;
import gamsa.population.Alignment;
import gamsa.population.Sequence;

/**
Deletes all columns of gaps from all the sequences in the alignment.

@author Tom Austin and Amie Radenbaugh
*/
public class GapColumnDeletionMutation extends Mutation
{

	private static InfoCenter i_rand = InfoCenter.getCenter();
	
	/**
	   Returns a new alignment with one gap removed from each sequence,
	   or if that is not possible, returns the original alignment unchanged.
	   
	   @see gamsa.operator.Mutation#perform(gamsa.population.Alignment)
	 */
	@Override
	public Alignment perform(Alignment parent)
	{
		Alignment child = parent.clone();
		Sequence tempSequence;
		boolean bAllGaps;
		String temp;
		Sequence newSequence;
		
		// loop through the columns, starting at the end
		for (int c=child.getSequenceLength()-1; c>-1; c--)
		{			
			bAllGaps = true;
			// loop through the sequences of the selected alignments
			for (int s=0; s<child.getSize(); s++)
			{
				tempSequence = child.getSequenceAt(s);
				if (tempSequence.getElementAt(c) != '-')
				{
					bAllGaps = false;
					break;
				}
			}
			
			// if all the sequences have a gap in this column,
			// then remove them
			if (bAllGaps)
			{				
				// loop through the sequences of the selected alignments
				for (int s=0; s<child.getSize(); s++)
				{						
					temp = child.getSequenceAt(s).toString();
					
					// remove the gap column
					newSequence = new Sequence(temp.substring(0,c) +
								  			   temp.substring(c+1));
					
					// set the sequence back in the alignment
					child.setSequenceAt(s, newSequence);
				}
			}
		}
		
		return child;
	}
	
	/**
	   Tests mutation.
	 */
	public static void main(String[] args)
	{
        Mutation m = new GapColumnDeletionMutation();
        
        i_rand.setScorer(new gamsa.scorer.DNAScorer());
        Alignment a = new Alignment();
        
        a.addSequence(new Sequence("ac-tg-----a--"));
        a.addSequence(new Sequence("ac-tt----g---"));
        
        System.out.println(a);
        a = m.perform(a);
        System.out.println(a);
	}

}
