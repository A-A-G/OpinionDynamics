/**
 * 
 */
package graph;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class GraphEdgeLabel extends GraphLabel
{

	public float distance = -1;

	@Override
	public boolean check()
	{
		if (this.distance == -1)
		{
			System.out.println("GraphEdgeLabel check error: distance = -1");
			return false;
		}
		return super.check();
	}

}
