/**
 * 
 */
package gui.editpanels;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import graph.Graph;
import graph.GraphNode;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class StubEditPanel extends EditPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private GraphNode node;

	public StubEditPanel(GraphNode newGraphElement, Graph newGraph)
	{
		super(newGraphElement, newGraph);
		this.node = newGraphElement;
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(newGraphElement.getStubbornness(), 0, 1, 0.1);
		this.valueSpinner.setModel(spinnerModel);
	}

	@Override
	public void stateChanged(ChangeEvent arg0)
	{
		if (!this.graph.isLocked())
		{
			this.node.setStubbornness(((SpinnerNumberModel) this.valueSpinner.getModel()).getNumber().doubleValue());
		}
		else
		{
			update();
		}
	}

	@Override
	public void update()
	{
		((SpinnerNumberModel) this.valueSpinner.getModel()).setValue(new Double(this.node.getStubbornness()));
	}

}
