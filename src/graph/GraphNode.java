/**
 * 
 */
package graph;

import gui.graph.GraphDrawSettings;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import utils.AlphanumComparator;
import utils.ColorHandler;
import utils.Vector2D;
import xml.GraphMLKey;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class GraphNode extends GraphElement implements Comparable<GraphNode>
{

	private GraphGeometry								geometry						= null;
	private GraphLabel									label								= null;
	private HashMap<String, GraphEdge>	incomingEdges				= new HashMap<String, GraphEdge>();
	private boolean											changed							= false;
	private double											old									= 0;
	private double											defaultStubborness	= 0.5;
	private double											stubbornness				= 0.5;
	private int													nodeNumber;

	public GraphNode(String newId, int newNodeNumber)
	{
		super(newId);
		this.nodeNumber = newNodeNumber;
	}

	public void drawNode(Graphics2D g2D, Vector2D offSet, double scale, GraphDrawSettings drawSettings)
	{
		drawNode(g2D, offSet, scale, drawSettings, true, false);
	}

	public void drawNode(Graphics2D g2D, Vector2D offSet, double scale, GraphDrawSettings drawSettings, boolean withLabel, boolean withValue)
	{
		Vector2D nodeTopLeft = getTopLeft().sub(offSet).mult(scale);
		Color c = g2D.getColor();
		g2D.setColor(ColorHandler.getColor((float) this.value, drawSettings.getColorSpace()));
		g2D.fill(new Ellipse2D.Double(nodeTopLeft.x, nodeTopLeft.y, this.geometry.getWidth() * scale, this.geometry.getHeight() * scale));
		g2D.setColor(c);
		if (drawSettings.isDrawBorders())
		{
			g2D.draw(new Ellipse2D.Double(nodeTopLeft.x, nodeTopLeft.y, this.geometry.getWidth() * scale, this.geometry.getHeight() * scale));
		}
		if (withLabel)
		{
			drawLabel(g2D, offSet, scale, drawSettings);
		}
		if (withValue)
		{
			drawValue(g2D, offSet, scale, drawSettings);
		}
	}

	public void drawLabel(Graphics2D g2D, Vector2D offSet, double scale, GraphDrawSettings drawSettings)
	{
		Font stdFont = g2D.getFont();
		g2D.setFont(new Font(this.label.getFontFamily(), this.label.getFontStyle(), this.label.getFontSize()));
		Vector2D labelTopLeft = getCenter().sub(offSet).mult(scale);
		Color c = g2D.getColor();
		if (drawSettings.isColorNodeLabels())
		{
			g2D.setColor(ColorHandler.getColor((float) this.value, drawSettings.getColorSpace()));
		}
		g2D.drawString(this.label.getText(), (float) (labelTopLeft.x - this.label.getGeometry().getWidth() / 2), (float) (labelTopLeft.y + this.label.getGeometry().getHeight() / 2));
		g2D.setFont(stdFont);
		g2D.setColor(c);
	}

	public void drawValue(Graphics2D g2D, Vector2D offSet, double scale, GraphDrawSettings drawSettings)
	{
		Font stdFont = g2D.getFont();
		g2D.setFont(new Font(this.label.getFontFamily(), java.awt.Font.BOLD, 15));
		Vector2D labelTopLeft = getCenter().sub(offSet).mult(scale);
		Color c = g2D.getColor();
		if (drawSettings.isColorNodeValues())
		{
			g2D.setColor(ColorHandler.getColor((float) this.value, drawSettings.getColorSpace()));
		}
		FontMetrics m = g2D.getFontMetrics();
		g2D.drawString(String.format("%.2f", new Double(this.value)), (float) labelTopLeft.x - m.stringWidth(String.format("%.2f", new Double(this.value))) / 2.0F, (float) labelTopLeft.y + m.getHeight() / 4.0F);
		g2D.setFont(stdFont);
		g2D.setColor(c);
	}

	public void addEdge(GraphEdge edge)
	{
		this.incomingEdges.put(edge.getId(), edge);
	}

	public boolean check()
	{
		if (this.id.equals(""))
		{
			System.out.println("GraphNode check error: id empty");
			return false;
		}
		else if (this.label == null)
		{
			System.out.println("GraphNode check error: label not set");
			return false;
		}
		else if (this.geometry == null)
		{
			System.out.println("GraphNode check error: geometry not set");
			return false;
		}
		else if (!this.geometry.check() || !this.label.check()) { return false; }
		return true;
	}

	public Vector2D getCenter()
	{
		return this.geometry.getCenter();
	}

	public Vector2D getTopLeft()
	{
		return this.geometry.getTopLeft();
	}

	// getter and setter
	/**
	 * @return the incomingEdges
	 */
	public Map<String, GraphEdge> getIncomingEdges()
	{
		return Collections.unmodifiableMap(this.incomingEdges);
	}

	/**
	 * @return geometry
	 */
	public GraphGeometry getGeometry()
	{
		return this.geometry;
	}

	/**
	 * @param newGeometry
	 *          sets geometry
	 */
	public void setGeometry(GraphGeometry newGeometry)
	{
		this.geometry = newGeometry;
	}

	/**
	 * @return label
	 */
	public GraphLabel getLabel()
	{
		return this.label;
	}

	/**
	 * @param newLabel
	 *          sets label
	 */
	public void setLabel(GraphLabel newLabel)
	{
		this.label = newLabel;
	}

	@Override
	public int compareTo(GraphNode otherNode)
	{
		return (new AlphanumComparator()).compare(this.id, otherNode.id);
	}

	public int compareTo2D(GraphNode otherNode)
	{
		if (this.geometry.getX() < otherNode.getGeometry().getX())
		{
			return -1;
		}
		else if (this.geometry.getX() > otherNode.getGeometry().getX())
		{
			return 1;
		}
		else if (this.geometry.getY() > otherNode.getGeometry().getY())
		{
			return 1;
		}
		else if (this.geometry.getY() < otherNode.getGeometry().getY()) { return -1; }
		return 0;
	}

	public double getSimulationValue()
	{
		if (this.changed) { return this.old; }
		return this.value;
	}

	public void setSimulationValue(double newValue)
	{
		this.old = this.value;
		if (newValue > 1)
		{
			System.out.println("GraphNode.setSimulationValue error: newValue > 1");
			this.value = 1;
		}
		else if (newValue < -1)
		{
			System.out.println("GraphNode.setSimulationValue error: newValue < -1");
			this.value = -1;
		}
		else
		{
			this.value = newValue;
		}
		this.changed = true;
	}

	public void newStubKeySelected(GraphMLKey key)
	{
		if (null != key)
		{
			if (this.values.containsKey(key.id))
			{
				if (key.valueType.equals("number"))
				{
					this.setStubbornness((int) this.values.get(key.id).value);
				}
			}
		}
	}

	public void setChanged(boolean newChanged)
	{
		this.changed = newChanged;
	}

	/**
	 * @return the stubbornness
	 */
	public double getStubbornness()
	{
		return this.stubbornness;
	}

	/**
	 * @param newStubbornness
	 *          the stubbornness to set
	 */
	public void setStubbornness(double newStubbornness)
	{
		if (this.stubbornness < 0)
		{
			System.out.println("Minimal Stubborness is 0!");
			this.stubbornness = 0;
			return;
		}
		if (this.stubbornness > 1)
		{
			System.out.println("Maximal Stubborness is 1!");
			this.stubbornness = 1;
			return;
		}
		this.stubbornness = newStubbornness;
	}

	/**
	 * @return the nodeNumber
	 */
	public int getNodeNumber()
	{
		return this.nodeNumber;
	}

	public void updateDefaultStubborness()
	{
		this.defaultStubborness = this.stubbornness;
	}

	public void resetStubborness()
	{
		this.stubbornness = this.defaultStubborness;
	}

}