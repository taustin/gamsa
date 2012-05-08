package gamsa.operator;

import gamsa.InfoCenter;
import gamsa.population.Alignment;
import gamsa.population.Sequence;

/**
   Mutation that inserts one random gap in each sequence.
   
   @author Tom Austin and Amie Radenbaugh
 */
public class GapInsertionMutation extends Mutation
{
	private static InfoCenter i_rand = InfoCenter.getCenter();
	
	/**
	   Returns an alignment with one gap inserted into each sequence.
	   
	   @see gamsa.operator.Mutation#perform(gamsa.population.Alignment)
	 */
	@Override
	public Alignment perform(Alignment parent)
	{
		Alignment child = parent.clone();
		Sequence s;
		int gapPosition = 0;
		String beforeGap;
		String afterGap;
		
		for (int i=0; i<child.getSize(); i++)
		{
			s = child.getSequenceAt(i);
			gapPosition = i_rand.getRandomInt(s.getSize());
			beforeGap = s.getSubSequence(0,gapPosition);
			afterGap = s.getSubSequence(gapPosition, s.getSize());
			s = new Sequence(beforeGap + "-" + afterGap);
			child.setSequenceAt(i, s);
		}
		
		return child;
	}
	
	/**
	   Tests mutation.
	 */
	public static void main(String[] args)
	{
		Mutation m = new GapInsertionMutation();
		
		i_rand.setScorer(new gamsa.scorer.DNAScorer());
		Alignment a = new Alignment();
		
		a.addSequence(new Sequence("actg-"));
		a.addSequence(new Sequence("acttg"));
		
		System.out.println(a);
		a = m.perform(a);
		System.out.println(a);
	}
}
