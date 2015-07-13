/**
 * 
 */
package gui.editpanels;

import graph.Graph;
import graph.GraphEdge;
import graph.GraphNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;


import utils.AlphanumComparatorGE;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class EdgePanel extends NodePanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EdgePanel(Graph newGraph){
		super(newGraph);
	}
	
	@Override
	protected void createEditPanels()
	{
		removeAll();
		List<GraphEdge> list = new ArrayList<GraphEdge>();
		for (GraphNode node :  this.graph.getNodeCollection())
		{
			for (GraphEdge edge : node.getIncomingEdges().values())
			{
				list.add(edge);
			}
		}
		Collections.sort(list, new AlphanumComparatorGE());
		for (GraphEdge edge : list)
		{
			add(new EdgeEditPanel(edge, this.graph));			
		}
		add(Box.createVerticalGlue());
		this.updateUI();
	}

}
