/**
 * 
 */
package gui.editpanels;

import java.awt.Dimension;

import graph.Graph;
import graph.GraphElement;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class EditPanel extends JPanel implements ChangeListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected GraphElement graphElement;

	protected JSpinner valueSpinner;
	
	protected JLabel label;
	
	protected Graph graph;
	
	public EditPanel(GraphElement newGraphElement, Graph newGraph)
	{
		this.graphElement = newGraphElement;
		this.graph = newGraph;
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.label = new JLabel(newGraphElement.toString());
		this.add(this.label);
		this.add(Box.createHorizontalGlue());
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(newGraphElement.getValue(), -1, 1, 0.1);
		this.valueSpinner = new JSpinner(spinnerModel);
		this.valueSpinner.addChangeListener(this);
		Dimension d = this.valueSpinner.getMaximumSize();
		d.height = 2*this.valueSpinner.getFont().getSize();
		this.valueSpinner.setMaximumSize(d);
		this.add(this.valueSpinner);
	}

	@Override
	public void stateChanged(ChangeEvent arg0)
	{
		if (!this.graph.isLocked())
		{
			this.graphElement.setValue(((SpinnerNumberModel)this.valueSpinner.getModel()).getNumber().floatValue());
			this.graph.update();
		}
		else
		{
			update();
		}
	}
	
	public void update()
	{
		((SpinnerNumberModel)this.valueSpinner.getModel()).setValue(new Double(this.graphElement.getValue()));
	}
}
