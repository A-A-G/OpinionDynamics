/**
 * 
 */
package gui.graph;

import graph.Graph;
import graph.GraphNode;
import graph.GraphEdge;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

import utils.ColorHandler;
import utils.Vector2D;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class GraphPanel extends JComponent implements Observer
{

	/**
	 * 
	 */
	private static final long			serialVersionUID	= 1L;

	private final Graph						graph;
	private double								zoom							= 1;
	private Dimension							oldSize						= null;
	private Dimension							preferredSize			= new Dimension(100, 100);
	private final GPMouseListener	mouseListener			= new GPMouseListener(this);
	private GraphDrawSettings			drawSettings			= new GraphDrawSettings();

	public GraphPanel(Graph newGraph)
	{
		this.graph = newGraph;
		newGraph.addObserver(this);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (this.graph.getNodeCollection().size() > 0)
		{
			Graphics2D g2D = (Graphics2D) g;
			g2D.setColor(this.drawSettings.getDefaultColor());
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2D.setStroke(new BasicStroke(this.drawSettings.getLineWidth(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
			double extrema[] = this.graph.getExtrema();
			double scale = getScale(this.getSize(), extrema);
			double edgeGapScale = scale;
			if ((this.drawSettings.isAutoEdgeGap()) && (null != this.oldSize))
			{
				edgeGapScale = getScale(this.oldSize, extrema);
			}
			double edgeGapScaled = (this.drawSettings.getEdgeGap() + this.drawSettings.getLineWidth() / 2.0) / edgeGapScale;
			Vector2D offSet = new Vector2D(extrema[0] - this.drawSettings.getBorderGap(), extrema[2] - this.drawSettings.getBorderGap());
			for (GraphNode node : this.graph.getNodeCollection())
			{
				for (GraphEdge edge : node.getIncomingEdges().values())
				{
					GraphNode source = this.graph.getNodeMap().get(edge.getSourceID());
					edge.drawEdge(g2D, source, node, offSet, this.drawSettings, scale, edgeGapScaled);
				}
			}
			for (GraphNode node : this.graph.getNodeCollection())
			{
				node.drawNode(g2D, offSet, scale, this.drawSettings, false, false);
			}
			if (this.drawSettings.isPrintEdgeLabels())
			{
				for (GraphNode node : this.graph.getNodeCollection())
				{
					for (GraphEdge edge : node.getIncomingEdges().values())
					{
						GraphNode source = this.graph.getNodeMap().get(edge.getSourceID());
						edge.drawLabel(g2D, source, offSet, edgeGapScaled, this.drawSettings);
					}
				}
			}
			if (this.drawSettings.isShowEdgeValues())
			{
				for (GraphNode node : this.graph.getNodeCollection())
				{
					for (GraphEdge edge : node.getIncomingEdges().values())
					{
						GraphNode source = this.graph.getNodeMap().get(edge.getSourceID());
						edge.drawValue(g2D, source, node, offSet, scale, edgeGapScaled, this.drawSettings);
					}
				}
			}
			if (this.drawSettings.isPrintNodeLabels())
			{
				for (GraphNode node : this.graph.getNodeCollection())
				{
					node.drawLabel(g2D, offSet, scale, this.drawSettings);
				}
			}
			if (this.drawSettings.isShowNodeValues())
			{
				for (GraphNode node : this.graph.getNodeCollection())
				{
					node.drawValue(g2D, offSet, scale, this.drawSettings);
				}
			}
			this.mouseListener.drawRect(g2D);
		}
	}

	private double getScale(Dimension d, double[] extrema)
	{
		double xscale = 0;
		if ((extrema[1] - extrema[0]) != 0)
		{
			xscale = (d.getWidth()) / (extrema[1] - extrema[0] + 2 * this.drawSettings.getBorderGap());
		}
		double yscale = 0;
		if ((extrema[3] - extrema[2]) != 0)
		{
			yscale = (d.getHeight()) / (extrema[3] - extrema[2] + 2 * this.drawSettings.getBorderGap());
		}
		return Math.min(xscale, yscale);
	}

	public void enableZoom(boolean b)
	{
		if (b)
		{
			this.addMouseListener(this.mouseListener);
			this.addMouseMotionListener(this.mouseListener);
			this.addMouseWheelListener(this.mouseListener);
		}
		else
		{
			this.removeMouseListener(this.mouseListener);
			this.removeMouseMotionListener(this.mouseListener);
			this.removeMouseWheelListener(this.mouseListener);
			unzoom();
		}
	}

	public void zoomAtP(double d, Point p)
	{
		zoom(this.zoom * d);

		if (this.zoom > 1)
		{
			int offX = (int) (p.x * d) - p.x;
			int offY = (int) (p.y * d) - p.y;
			setLocation(getLocation().x - offX, getLocation().y - offY);
			getParent().doLayout();
		}
		else
		{
			setLocation(0, 0);
			getParent().doLayout();
			this.oldSize = null;
		}
	}

	public void unzoom()
	{
		setLocation(0, 0);
		this.preferredSize.setSize(100, 100);
		getParent().doLayout();
		this.zoom = 1;
		this.oldSize = null;
	}

	public void zoomAndMove(double d, Point p)
	{
		zoom(this.zoom * d);
		setLocation((int) (0 - (p.x * d)), (int) (0 - (p.y * d)));
		getParent().doLayout();
	}

	private void zoom(double d)
	{
		if (1 == this.zoom)
		{
			if (this.oldSize == null)
			{
				this.oldSize = new Dimension();
			}
			this.oldSize.setSize(getWidth(), getHeight());
		}
		if (d > 1)
		{
			int w = (int) (this.oldSize.getWidth() * d);
			int h = (int) (this.oldSize.getHeight() * d);
			this.preferredSize.setSize(w, h);
			this.zoom = d;
		}
		else
		{
			this.zoom = 1;
			this.preferredSize.setSize(100, 100);
		}
	}

	@Override
	public Dimension getPreferredSize()
	{
		return this.preferredSize;
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		repaint();
	}

	/**
	 * @param newColorSpace
	 *          the colorSpace to set
	 */
	public void setColorSpace(ColorHandler.EColorSpace newColorSpace)
	{
		this.drawSettings.setColorSpace(newColorSpace);
		repaint();
	}

	/**
	 * @return the oldSize
	 */
	public Dimension getOldSize()
	{
		return this.oldSize;
	}

	/**
	 * @param newOldSize
	 *          the oldSize to set
	 */
	public void setOldSize(Dimension newOldSize)
	{
		this.oldSize = newOldSize;
	}

	/**
	 * @param newAutoEdgeGap
	 *          the autoEdgeGap to set
	 */
	public void setAutoEdgeGap(boolean newAutoEdgeGap)
	{
		this.drawSettings.setAutoEdgeGap(newAutoEdgeGap);
		repaint();
	}

	/**
	 * @param newDrawBorders
	 *          the drawBorders to set
	 */
	public void setDrawBorders(boolean newDrawBorders)
	{
		this.drawSettings.setDrawBorders(newDrawBorders);
		repaint();
	}

	/**
	 * @param newPrintEdgeLabels
	 *          the printEdgeLabels to set
	 */
	public void setPrintEdgeLabels(boolean newPrintEdgeLabels)
	{
		this.drawSettings.setPrintEdgeLabels(newPrintEdgeLabels);
		repaint();
	}

	/**
	 * @param newPrintNodeLabels
	 *          the printNodeLabels to set
	 */
	public void setPrintNodeLabels(boolean newPrintNodeLabels)
	{
		this.drawSettings.setPrintNodeLabels(newPrintNodeLabels);
		repaint();
	}

	/**
	 * @return the drawSettings
	 */
	public GraphDrawSettings getDrawSettings()
	{
		return this.drawSettings;
	}

	/**
	 * @param newDrawSettings
	 *          the drawSettings to set
	 */
	public void setDrawSettings(GraphDrawSettings newDrawSettings)
	{
		this.drawSettings = newDrawSettings;
		repaint();
	}

	/**
	 * @param newColorEdgeLabels
	 *          the colorEdgeLabels to set
	 */
	public void setColorEdgeLabels(boolean newColorEdgeLabels)
	{
		this.drawSettings.setColorEdgeLabels(newColorEdgeLabels);
		repaint();
	}

	/**
	 * @param newColorNodeLabels
	 *          the colorNodeLabels to set
	 */
	public void setColorNodeLabels(boolean newColorNodeLabels)
	{
		this.drawSettings.setColorNodeLabels(newColorNodeLabels);
		repaint();
	}

	/**
	 * @param newBorderGap
	 *          the borderGap to set
	 */
	public void setBorderGap(int newBorderGap)
	{
		this.drawSettings.setBorderGap(newBorderGap);
		repaint();
	}

	/**
	 * @param newLineWidth
	 *          the lineWidth to set
	 */
	public void setLineWidth(int newLineWidth)
	{
		this.drawSettings.setLineWidth(newLineWidth);
		repaint();
	}

	/**
	 * @param newDefaultColor
	 *          the defaultColor to set
	 */
	public void setDefaultColor(Color newDefaultColor)
	{
		this.drawSettings.setDefaultColor(newDefaultColor);
		repaint();
	}

	/**
	 * @param newEdgeGap
	 *          the edgeGap to set
	 */
	public void setEdgeGap(double newEdgeGap)
	{
		this.drawSettings.setEdgeGap(newEdgeGap);
		repaint();
	}

	/**
	 * @param newShowEdgeValues
	 *          the showEdgeValues to set
	 */
	public void setShowEdgeValues(boolean newShowEdgeValues)
	{
		this.drawSettings.setShowEdgeValues(newShowEdgeValues);
		repaint();
	}

	/**
	 * @param newShowNodeValues
	 *          the showNodeValues to set
	 */
	public void setShowNodeValues(boolean newShowNodeValues)
	{
		this.drawSettings.setShowNodeValues(newShowNodeValues);
		repaint();
	}

	/**
	 * @param newColorEdgeValues
	 *          the colorEdgeValues to set
	 */
	public void setColorEdgeValues(boolean newColorEdgeValues)
	{
		this.drawSettings.setColorEdgeValues(newColorEdgeValues);
		repaint();
	}

	/**
	 * @param newColorNodeValues
	 *          the colorNodeValues to set
	 */
	public void setColorNodeValues(boolean newColorNodeValues)
	{
		this.drawSettings.setColorNodeValues(newColorNodeValues);
		repaint();
	}

}