/**
 * 
 */
package gui.editpanels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;

import utils.AlphanumComparatorGE;
import graph.Graph;
import graph.GraphNode;

/**
 * @author Alex
 *
 */
public class StubPanel extends NodePanel
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public StubPanel(Graph newGraph){
		super(newGraph);
	}
	
	@Override
	protected void createEditPanels()
	{
		removeAll();
		List<GraphNode> list = new ArrayList<GraphNode>( this.graph.getNodeCollection() );
		Collections.sort(list, new AlphanumComparatorGE());
		for (GraphNode node : list)
		{
			add(new StubEditPanel(node, this.graph));
		}
		add(Box.createVerticalGlue());
		this.updateUI();
	}
	
}
