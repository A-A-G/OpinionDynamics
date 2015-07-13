/**
 * 
 */
package utils;

import graph.GraphElement;

import java.util.Comparator;

/**
 * @author Alexander Artiga Gonzalez
 *
 */
public class AlphanumComparatorGE implements Comparator<GraphElement>
{

	@Override
	public int compare(GraphElement g1, GraphElement g2)
	{
		// TODO Auto-generated method stub
		return (new AlphanumComparator()).compare(g1.toString(), g2.toString());
	}

}
