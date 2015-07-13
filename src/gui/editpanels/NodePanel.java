/**
 * 
 */
package gui.editpanels;

import graph.Graph;
import graph.GraphNode;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import utils.AlphanumComparatorGE;


/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class NodePanel extends JPanel implements Observer
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Graph graph;


	public NodePanel(Graph newGraph)
	{
		this.graph = newGraph;
		this.graph.addObserver(this);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		createEditPanels();
	}

	protected void createEditPanels()
	{
		removeAll();
		List<GraphNode> list = new ArrayList<GraphNode>( this.graph.getNodeCollection() );
		Collections.sort(list, new AlphanumComparatorGE());
		for (GraphNode node : list)
		{
			add(new EditPanel(node, this.graph));
		}
		add(Box.createVerticalGlue());
		this.updateUI();
	}
	
	private void updateEditPanels()
	{
		for (Component c : getComponents())
		{
			if (c instanceof EditPanel)
			{
				((EditPanel)c).update();
			}
		}
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		switch (this.graph.getChangeType())
		{
		case NONE:
			//do nothing
			break;
		case VALUES:
			updateEditPanels();
			break;
		case ELEMENTS:
			createEditPanels();
			break;
		default:
		}
	}
}
