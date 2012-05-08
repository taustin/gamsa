package gamsa.operator;

import gamsa.InfoCenter;
import gamsa.population.Alignment;
import gamsa.population.Sequence;

/**
   Deletes one random gap from each sequence in the alignment.
   
   @author Tom Austin and Amie Radenbaugh
 */
public class GapDeletionMutation extends Mutation
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
		String seq;
		Sequence newSeq;
		int pos = 0;
		int gapIndex = 0;
		
		//For each sequence, delete a gap
		for(int i=0; i<child.getSize(); i++)
		{
			seq = child.getSequenceAt(i).getInputString();
			
			//Start looking for a gap from a random position.
			pos = i_rand.getRandomInt(seq.length()-1);
			gapIndex = seq.indexOf("-", pos);
			
			//Did not find a gap in the tail, so look to the head.
			if (gapIndex == -1)
				gapIndex = seq.indexOf("-");
			
			//If we don't find any gap at all, this mutation won't work.
			// In this case, return the parent alignment unchanged.
			if (gapIndex == -1)
				return parent;
			
			newSeq = new Sequence(seq.substring(0,gapIndex)
					+ seq.substring(gapIndex+1));
			child.setSequenceAt(i, newSeq);
		}
		
		return child;
	}
	
	/**
	   Tests mutation.
	 */
	public static void main(String[] args)
	{
        Mutation m = new GapDeletionMutation();
        
        i_rand.setScorer(new gamsa.scorer.DNAScorer());
        Alignment a = new Alignment();
        
        a.addSequence(new Sequence("ac-tg-a"));
        a.addSequence(new Sequence("ac-ttg-"));
        
        System.out.println(a);
        a = m.perform(a);
        System.out.println(a);
	}
}
