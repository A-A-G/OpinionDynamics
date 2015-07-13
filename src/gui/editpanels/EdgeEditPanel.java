/**
 * 
 */
package gui.editpanels;

import graph.Graph;
import graph.GraphEdge;


/**
 * @author Alexander Artiga Gonzalez
 *
 */
public class EdgeEditPanel extends EditPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EdgeEditPanel(GraphEdge newgraphElement, Graph newGraph){
		super(newgraphElement, newGraph);
		this.label.setText(newgraphElement.toString() + ": " + this.graph.getNodeMap().get(newgraphElement.getSourceID()).toString() + " --> " + this.graph.getNodeMap().get(newgraphElement.getTargetID()).toString());
	}
}
