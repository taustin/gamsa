package gamsa.population;

/**
 * Represents a sequence of nucleotides or amino acids.
 * Gaps are indicated by '-'.
 * 
 * @author Tom Austin and Amie Radenbaugh
 */
public class Sequence implements Cloneable
{
	// Sequence of elements, including gaps.
	char[] i_finalSequence;
	
	/**
     * Takes a String representing a sequence of Nucleotides
     * or Amino acids that already has gaps inserted.
     */
	public Sequence(String aSequence)
	{
		i_finalSequence = aSequence.toCharArray();
	}
	
    /**
     * Takes a String representing a sequence of Nucleotides
     * or Amino acids and spreads them out to the specified length
     * by inserting spaces randomly.
     */
	public Sequence(String aSequence, int alignmentLength)
	{
		char[] inputSequence = aSequence.toCharArray();
		i_finalSequence = new char[alignmentLength];
		
		int inputSequenceIndex = 0;
		int finalSequenceIndex = 0;
		
        //Odds of getting a gap at any given position.
        double gapPercentage = 1.0 - (((double)inputSequence.length)/((double)alignmentLength));
        
        //Number of gaps in this sequence
        int numGaps = alignmentLength - inputSequence.length;
        
        //Insert spaces into the sequence.
		for (int i=0; i<alignmentLength; i++)
		{
            //If there are no more gaps, get the next element.
            //If there are no more elements, get a gap.
            //Otherwise, roll the dice.
            if (numGaps==0 || (inputSequenceIndex<inputSequence.length
                    && Math.random() > gapPercentage))
			{
				i_finalSequence[finalSequenceIndex++] = inputSequence[inputSequenceIndex++];
			}
			else
			{
				i_finalSequence[finalSequenceIndex++] = '-';
                numGaps--;
			}
		}		
	}
    
    /**
     * Return a clone of this sequence.
     */
    @Override
    public Sequence clone()
    {
        char[] newSequence = new char[i_finalSequence.length];
        
        for (int i=0; i<i_finalSequence.length; i++)
        {
            newSequence[i] = i_finalSequence[i];
        }
        return new Sequence(new String(newSequence), newSequence.length);
    }
	
    /**
     * Get the element at the specified position in the sequence.
     */
	public char getElementAt(int anIndex)
	{
		return i_finalSequence[anIndex];
	}
	
	/**
	 * Returns a String of this sequence, including gaps.
	 */
	public String toString()
	{
		return new String(i_finalSequence);
	}
	
    /**
     * Returns the string representation of the sequence with the gaps.
     */
	public String getInputString()
	{		
		return new String(i_finalSequence);
	}
	
	/**
	 * Returns the size of the sequence.
	 */
	public int getSize()
	{
		return i_finalSequence.length;
	}
	
	/**
	 * Returns the number of non gap elements in the sequence
	 * starting at the 0th index and counting until the specified
	 * end index is reached.
	 * 
	 * @param anEndIndex
	 * @return int number of non gap elements in the sequence
	 */
	public int getNonGapCount(int anEndIndex)
	{
		int count = 0;
		for (int i=0; i<anEndIndex; i++)
		{
			if (getElementAt(i) != '-')
				count++;
		}
		return count;
	}
	
	
	
	/**
	 * Returns the index in the sequence so that the sequence starting at
	 * the 0th index and going to the returned index contains the specified 
	 * number of non gap elements. 
	 * 
	 * @param aNumberOfElements
	 * @return int 
	 */
	public int getIndexContainingElements(int aNumberOfElements)
	{
		// if there are no elements in the sequence
		// then just return the 0th index
		if (aNumberOfElements == 0)
			return aNumberOfElements;
		
		int count = 0;
		for (int i=0; i<getSize(); i++)
		{
			if (getElementAt(i) != '-')
				count++;
			if (count == aNumberOfElements)
				return ++i;
		}
		return getSize();
	}
	
	/**
	 * Returns a string representation of the sequence from a specified index
	 * to a specified index.
	 * @param anStartIndex
	 * @param anEndIndex
	 * @return String
	 */
	public String getSubSequence(int aStartIndex, int anEndIndex)
	{
		return getInputString().substring(aStartIndex, anEndIndex);
	}
	
	/**
	 * Returns a clone of this sequence with leading and trailing gaps
	 * converted to '.' instead.
	 */
	public Sequence getSequenceWithEdgeGapsTranslated()
	{
		char[] newSeq = i_finalSequence.clone();
		int i=0;
		
		// convert leading '-' to '.'
		boolean firstElementFound = false;
		while (i<newSeq.length && !firstElementFound)
		{
			if (newSeq[i] != '-')
				firstElementFound = true;
			else
				newSeq[i++] = '.';
		}
		
		// convert trailing '-' to '.'
		boolean lastElementFound = false;
		i = newSeq.length-1;
		while (i>0 && !lastElementFound)
		{
			if (newSeq[i] != '-')
				lastElementFound = true;
			else
				newSeq[i--] = '.';
		}
		return new Sequence(new String(newSeq));
	}

	/**
	 * Returns the left half of this sequence that corresponds
	 * to the specified other left sequence portion, ignoring the
	 * gaps in both.
	 */
	public String getLeftMatch(String otherLeft)
	{
		int j = 0;
		StringBuilder sb = new StringBuilder();
		char c;
		for (int i=0; i<otherLeft.length(); i++)
		{
			c = otherLeft.charAt(i);
			//Skip over gaps in otherLeft.
			if (c != '-')
			{
				while (i_finalSequence[j] == '-')
					sb.append(i_finalSequence[j++]);
				//Increment j once more so it is ready for the next round
				sb.append(i_finalSequence[j++]);
			}
		}
		
		return sb.toString();
	}

	/**
	 * Returns the right half of this sequence that corresponds
	 * to the specified other right sequence portion, ignoring the
	 * gaps in both.
	 */
	public String getRightMatch(String otherRight)
	{
		int j = i_finalSequence.length - 1;
		StringBuilder sb = new StringBuilder();
		char c;
		for (int i=otherRight.length()-1; i>=0; i--)
		{
			c = otherRight.charAt(i);
			//Skip over gaps in otherLeft.
			if (c != '-')
			{
				while (i_finalSequence[j] == '-')
					sb.append(i_finalSequence[j--]);
				//Decrement j once more so it is ready for the next round
				sb.append(i_finalSequence[j--]);
			}
		}
		
		return sb.reverse().toString();
	}
}