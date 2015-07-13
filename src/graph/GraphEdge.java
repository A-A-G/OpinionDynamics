/**
 * 
 */
package graph;

import gui.graph.GraphDrawSettings;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import utils.ColorHandler;
import utils.Draw2D;
import utils.Vector2D;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class GraphEdge extends GraphElement implements Comparable<GraphEdge>
{

	private String sourceId = "";
	private String targetId = "";
	private GraphEdgeLabel label = null;

	public GraphEdge(String newId)
	{
		super(newId);
	}
	
	public void drawEdge(Graphics2D g2D, GraphNode source, GraphNode target, Vector2D offSet, GraphDrawSettings drawSettings, double scale, double edgeGapScaled)
	{
		drawEdge(g2D, source, target, offSet, drawSettings, scale, edgeGapScaled, false, false);
	}
	
	public void drawEdge(Graphics2D g2D, GraphNode source, GraphNode target, Vector2D offSet, GraphDrawSettings drawSettings, double scale, double edgeGapScaled, boolean printLabel, boolean printValue)
	{
		if (!drawSettings.isShowZeroEdges()&&(0 == this.value))
		{
			return;
		}
		Vector2D gradient2D = new Vector2D(source.getCenter(), target.getCenter());
		gradient2D.norm();
		Vector2D normal = gradient2D.getNormal();
		double sign = (source.compareTo2D(target) < 0) ? -1 : 1;
		normal.mult(sign).mult(edgeGapScaled).mult(scale);
		double r = source.getGeometry().getHeight() / 2.0;
		Vector2D rOffSet = Vector2D.mult(gradient2D, r * scale);
		Vector2D sourceEnd = Vector2D.intersection(moveCenter(source.getCenter(), scale, offSet, normal).add(rOffSet), gradient2D.getGradient(), moveCenter(source.getCenter(), scale, offSet), r * scale);
		Vector2D targetEnd = Vector2D.intersection(moveCenter(target.getCenter(), scale, offSet, normal).sub(rOffSet), gradient2D.getGradient(), moveCenter(target.getCenter(), scale, offSet), r * scale);
		Color c = g2D.getColor();
		g2D.setColor(ColorHandler.getColor((float) this.value, drawSettings.getColorSpace()));
		if (drawSettings.isDrawArrow())
		{
			float lineWidth = ((BasicStroke) g2D.getStroke()).getLineWidth();
			g2D.draw(new Line2D.Double(sourceEnd, Vector2D.sub(targetEnd, Vector2D.mult(gradient2D, drawSettings.getArrowEdge() * drawSettings.getArrowSize().getHeight() * lineWidth))));
			if (!drawSettings.isColorArrow())
			{
				g2D.setColor(c);
			}
			Draw2D.drawArrow(g2D, targetEnd, gradient2D, drawSettings.getArrowSize(), drawSettings.getArrowEdge());
		}
		else
		{
			g2D.draw(new Line2D.Double(sourceEnd, targetEnd));
		}
		g2D.setColor(c);
		if (printLabel)
		{
			drawLabel(g2D, source, offSet, scale, drawSettings );
		}
		if (printValue)
		{
			drawValue(g2D, source, target, offSet, scale, edgeGapScaled, drawSettings );
		}
	}

	private static Vector2D moveCenter(Vector2D center, double scale, Vector2D offSet, Vector2D normal)
	{
		return moveCenter(center, scale, offSet).add(normal);
	}

	private static Vector2D moveCenter(Vector2D center, double scale, Vector2D offSet)
	{
		return center.sub(offSet).mult(scale);
	}

	public void drawLabel(Graphics2D g2D, GraphNode source, Vector2D offSet, double scale, GraphDrawSettings drawSettings)
	{
		if (!drawSettings.isShowZeroEdges()&&(0 == this.value))
		{
			return;
		}
		Font stdFont = g2D.getFont();
		g2D.setFont(new Font(this.label.getFontFamily(), this.label.getFontStyle(), this.label.getFontSize()));
		Color c = g2D.getColor();
		if (drawSettings.isColorEdgeLabels())
		{
			g2D.setColor(ColorHandler.getColor((float) this.value, drawSettings.getColorSpace()));
		}
		Vector2D labelTopLeft = new Vector2D(source.getCenter());
		labelTopLeft.sub(offSet).add(this.label.getTopLeft()).mult(scale);
		g2D.drawString(this.label.getText(), (float) labelTopLeft.x, (float) labelTopLeft.y);
		g2D.setFont(stdFont);
		g2D.setColor(c);
	}
	
	public void drawValue(Graphics2D g2D, GraphNode source, GraphNode target, Vector2D offSet, double scale, double edgeGapScaled, GraphDrawSettings drawSettings)
	{
		if (!drawSettings.isShowZeroEdges()&&(0 == this.value))
		{
			return;
		}
		Vector2D gradient2D = new Vector2D(source.getCenter(), target.getCenter());
		gradient2D.norm();
		Vector2D normal = gradient2D.getNormal();
		double sign = (source.compareTo2D(target) < 0) ? -1 : 1;
		normal.mult(sign).mult(edgeGapScaled).mult(scale);
		double r = source.getGeometry().getHeight() / 2.0;
		Vector2D rOffSet = Vector2D.mult(gradient2D, r * scale);
		Vector2D targetEnd = Vector2D.intersection(moveCenter(target.getCenter(), scale, offSet, normal).sub(rOffSet), gradient2D.getGradient(), moveCenter(target.getCenter(), scale, offSet), r * scale);
		targetEnd.sub(Vector2D.mult(gradient2D, drawSettings.getValueDistance()));
		Color c = g2D.getColor();
		if (drawSettings.isColorEdgeValues())
		{
			g2D.setColor(ColorHandler.getColor((float) this.value, drawSettings.getColorSpace()));
		}
		g2D.drawString(String.format("%.2f", new Double(this.value)), (float) targetEnd.x, (float) targetEnd.y);
		g2D.setColor(c);
	}

	public boolean check()
	{
		if (this.id.equals(""))
		{
			System.out.println("GraphEdge error: empty id");
			return false;
		}
		else if (this.sourceId.equals(""))
		{
			System.out.println("GraphEdge error: empty sourceID");
			return false;
		}
		else if (this.targetId.equals(""))
		{
			System.out.println("GraphEdge error: empty targetID");
			return false;
		}
		else if (this.label == null)
		{
			System.out.println("GraphEdge error: no label set");
			return false;
		}
		else if (!this.label.check()) { return false; }
		return true;
	}

	// getter and setter
	/**
	 * @return sourceID
	 */
	public String getSourceID()
	{
		return this.sourceId;
	}

	/**
	 * @param newSourceId
	 *          sets sourceID
	 */
	public void setSourceID(String newSourceId)
	{
		this.sourceId = newSourceId;
	}

	/**
	 * @return targetID
	 */
	public String getTargetID()
	{
		return this.targetId;
	}

	/**
	 * @return label
	 */
	public GraphEdgeLabel getLabel()
	{
		return this.label;
	}

	/**
	 * @param newLabel
	 *          sets label
	 */
	public void setLabel(GraphEdgeLabel newLabel)
	{
		this.label = newLabel;
	}

	/**
	 * @param newTargetId
	 *          sets targetID
	 */
	public void setTargetID(String newTargetId)
	{
		this.targetId = newTargetId;
	}

	@Override
	public int compareTo(GraphEdge o)
	{
		// TODO Auto-generated method stub
		return this.id.compareToIgnoreCase(o.id);
	}

}
