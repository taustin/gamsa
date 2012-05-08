package gamsa.operator;

import gamsa.InfoCenter;
import gamsa.population.Alignment;
import gamsa.population.Sequence;

/**
   Randomly shuffles a block of elements (or gaps) for one sequence in the alignment.
   
   @author Tom Austin and Amie Radenbaugh
 */
public class BlockShuffleMutation extends Mutation
{
	private static InfoCenter i_rand = InfoCenter.getCenter();
	
	/**
	   Returns a child with one sequence shifted around.
	   @see gamsa.operator.Mutation#perform(gamsa.population.Alignment)
	 */
	@Override
	public Alignment perform(Alignment parent)
	{
		Alignment child = parent.clone();
		
		//Pick a random sequence to modify
		int seqNumber = i_rand.getRandomInt(parent.getSize());
		Sequence seq = child.getSequenceAt(seqNumber);
		
		//pick a random shifting point
		int shiftPos = i_rand.getRandomInt(seq.getSize());
		
		if (i_rand.getRandomInt(2) == 1)
			seq = shiftLeft(seq, shiftPos);
		else
			seq = shiftRight(seq, shiftPos);
		
		child.setSequenceAt(seqNumber, seq);
		return child;
	}
	
	/**
	   Shifts a block in the sequence (ending at the specified position) to the left.
	 */
	private Sequence shiftLeft(Sequence seq, int endPos)
	{
		//Can't deal with shifts at the very beginning
		if (endPos == 0) return seq;
		
		char endElem = seq.getElementAt(endPos-1);
		int begPos = 0;
		int newBegPos = 0;
		boolean begFound = false;
		
		out: for (int i=endPos-1; i>=0; i--)
		{
			char elem = seq.getElementAt(i);
			//We found our match (logic varies depending on whether we
			// are shuffling gaps or elements).
			if (!begFound && ((elem == '-' && endElem != '-')
					|| (elem != '-' && endElem == '-')))
			{
				begPos = i+1;
				begFound = true;
			}
			//Now we need to find the end of the gap.
			else if (begFound && ((elem != '-' && endElem != '-')
					|| (elem == '-' && endElem == '-')))
			{
				newBegPos = i+1;
				break out;
			}
		}
		String begin = seq.getSubSequence(0, newBegPos);
		String block = seq.getSubSequence(newBegPos, begPos);
		String gapBlock = seq.getSubSequence(begPos, endPos);
		String tail = seq.getSubSequence(endPos, seq.getSize());
		
		return new Sequence(begin + gapBlock + block + tail);
	}
	
	/**
	   Shifts a block in the sequence (starting at the specified position) to the right.
	*/
	private Sequence shiftRight(Sequence seq, int startPos)
	{
		char startElem = seq.getElementAt(startPos);
		int endPos = seq.getSize();
		int newEndPos = seq.getSize();
		boolean endFound = false;
		
		out: for (int i=startPos+1; i<seq.getSize(); i++)
		{
			char elem = seq.getElementAt(i);
			//We found our match (logic varies depending on whether we
			// are shuffling gaps or elements).
			if (!endFound && ((elem == '-' && startElem != '-')
					|| (elem != '-' && startElem == '-')))
			{
				endPos = i;
				endFound = true;
			}
			//Now we need to find the end of the gap.
			else if (endFound && ((elem != '-' && startElem != '-')
					|| (elem == '-' && startElem == '-')))
			{
				newEndPos = i;
				break out;
			}
		}
		
		String begin = seq.getSubSequence(0, startPos);
		String block = seq.getSubSequence(startPos, endPos);
		String gapBlock = seq.getSubSequence(endPos, newEndPos);
		String tail = seq.getSubSequence(newEndPos, seq.getSize());
		
		return new Sequence(begin + gapBlock + block + tail);
	}
	
	/**
	   Tests private shift methods.
	 */
	public static void main(String[] args)
	{
		BlockShuffleMutation m = new BlockShuffleMutation();

		Sequence s = new Sequence("A-TTGCCAT-T");
		for (int i=0; i<11; i++)
		{
			System.out.println("\nOriginal: " + s);
			//System.out.println("	left: " + m.shiftLeft(s, i));
			System.out.println("   right: " + m.shiftRight(s, i));
		}
	}

}
