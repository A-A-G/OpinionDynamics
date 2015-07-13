/**
 * 
 */
package gui.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import utils.ColorHandler;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class GraphDrawSettings
{
	private boolean										showZeroEdges		= false;
	private boolean										printEdgeLabels	= true;
	private boolean										printNodeLabels	= true;
	private boolean										drawArrow				= true;
	private boolean										colorEdgeLabels	= false;
	private boolean										colorNodeLabels	= false;
	private boolean										showEdgeValues	= false;
	private boolean										showNodeValues	= false;
	private boolean										colorEdgeValues	= false;
	private boolean										colorNodeValues	= false;
	private boolean										colorArrow			= true;
	private boolean										autoEdgeGap			= true;
	private boolean										drawBorders			= true;
	private ColorHandler.EColorSpace	colorSpace			= ColorHandler.EColorSpace.RED_GREEN;
	private double										edgeGap					= 2;
	private double										valueDistance		= 10;
	private int												borderGap				= 10;
	private int												lineWidth				= 1;
	private Dimension									arrowSize				= new Dimension(2, 10);
	private double										arrowEdge				= 0.5;
	private Color											defaultColor		= Color.black;

	public static void drawSettingsDialog(final JFrame parent, final GraphPanel graph)
	{
		GraphDrawSettings backup = graph.getDrawSettings();
		final JPanel settings = new JPanel();
		settings.setLayout(new GridLayout(4, 4));
		settings.add(new JLabel("Border Gap:"));
		final SpinnerNumberModel borderGapModel = new SpinnerNumberModel(graph.getDrawSettings().getBorderGap(), 0, 100, 1);
		final JSpinner borderGapSpinner = new JSpinner(borderGapModel);
		borderGapSpinner.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				graph.setBorderGap(borderGapModel.getNumber().intValue());
			}
		});
		settings.add(borderGapSpinner);
		settings.add(new JLabel("Edge Gap:"));
		final SpinnerNumberModel edgeGapModel = new SpinnerNumberModel(graph.getDrawSettings().getEdgeGap(), 0, 50, 0.1);
		final JSpinner edgeGapSpinner = new JSpinner(edgeGapModel);
		edgeGapSpinner.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				graph.setEdgeGap(edgeGapModel.getNumber().doubleValue());
			}
		});
		settings.add(edgeGapSpinner);
		settings.add(new JLabel("Line Width:"));
		final SpinnerNumberModel lineWidthMode = new SpinnerNumberModel(graph.getDrawSettings().getLineWidth(), 0, 100, 1);
		final JSpinner lineWidthSpinner = new JSpinner(lineWidthMode);
		lineWidthSpinner.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				graph.setLineWidth(lineWidthMode.getNumber().intValue());
			}
		});
		settings.add(lineWidthSpinner);
		settings.add(new JLabel("Default Color:"));
		final JButton selectColorButton = new JButton("select");
		selectColorButton.setBackground(graph.getDrawSettings().getDefaultColor());
		selectColorButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				graph.setDefaultColor(JColorChooser.showDialog(parent, "Select Default Color", graph.getDrawSettings().getDefaultColor()));
				selectColorButton.setBackground(graph.getDrawSettings().getDefaultColor());
				graph.repaint();
			}
		});
		settings.add(selectColorButton);
		Object[] array = { new JLabel("Draw settings", SwingConstants.CENTER), settings };
		Object[] options = { "OK", "Cancel" };
		if (!(JOptionPane.OK_OPTION == JOptionPane.showOptionDialog(parent, array, "Drawing Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0])))
		{
			graph.setDrawSettings(backup);
			graph.repaint();
		}
	}

	/**
	 * @return the printEdgeLabels
	 */
	public boolean isPrintEdgeLabels()
	{
		return this.printEdgeLabels;
	}

	/**
	 * @param newPrintEdgeLabels
	 *          the printEdgeLabels to set
	 */
	public void setPrintEdgeLabels(boolean newPrintEdgeLabels)
	{
		this.printEdgeLabels = newPrintEdgeLabels;
	}

	/**
	 * @return the printNodeLabels
	 */
	public boolean isPrintNodeLabels()
	{
		return this.printNodeLabels;
	}

	/**
	 * @param newPrintNodeLabels
	 *          the printNodeLabels to set
	 */
	public void setPrintNodeLabels(boolean newPrintNodeLabels)
	{
		this.printNodeLabels = newPrintNodeLabels;
	}

	/**
	 * @return the drawArrow
	 */
	public boolean isDrawArrow()
	{
		return this.drawArrow;
	}

	/**
	 * @param newDrawArrow
	 *          the drawArrow to set
	 */
	public void setDrawArrow(boolean newDrawArrow)
	{
		this.drawArrow = newDrawArrow;
	}

	/**
	 * @return the colorEdgeLabels
	 */
	public boolean isColorEdgeLabels()
	{
		return this.colorEdgeLabels;
	}

	/**
	 * @param newColorEdgeLabels
	 *          the colorEdgeLabels to set
	 */
	public void setColorEdgeLabels(boolean newColorEdgeLabels)
	{
		this.colorEdgeLabels = newColorEdgeLabels;
	}

	/**
	 * @return the colorNodeLabels
	 */
	public boolean isColorNodeLabels()
	{
		return this.colorNodeLabels;
	}

	/**
	 * @param newColorNodeLabels
	 *          the colorNodeLabels to set
	 */
	public void setColorNodeLabels(boolean newColorNodeLabels)
	{
		this.colorNodeLabels = newColorNodeLabels;
	}

	/**
	 * @return the colorArrow
	 */
	public boolean isColorArrow()
	{
		return this.colorArrow;
	}

	/**
	 * @param newColorArrow
	 *          the colorArrow to set
	 */
	public void setColorArrow(boolean newColorArrow)
	{
		this.colorArrow = newColorArrow;
	}

	/**
	 * @return the autoEdgeGap
	 */
	public boolean isAutoEdgeGap()
	{
		return this.autoEdgeGap;
	}

	/**
	 * @param newAutoEdgeGap
	 *          the autoEdgeGap to set
	 */
	public void setAutoEdgeGap(boolean newAutoEdgeGap)
	{
		this.autoEdgeGap = newAutoEdgeGap;
	}

	/**
	 * @return the drawBorders
	 */
	public boolean isDrawBorders()
	{
		return this.drawBorders;
	}

	/**
	 * @param newDrawBorders
	 *          the drawBorders to set
	 */
	public void setDrawBorders(boolean newDrawBorders)
	{
		this.drawBorders = newDrawBorders;
	}

	/**
	 * @return the colorSpace
	 */
	public ColorHandler.EColorSpace getColorSpace()
	{
		return this.colorSpace;
	}

	/**
	 * @param newColorSpace
	 *          the colorSpace to set
	 */
	public void setColorSpace(ColorHandler.EColorSpace newColorSpace)
	{
		this.colorSpace = newColorSpace;
	}

	/**
	 * @return the edgeGap
	 */
	public double getEdgeGap()
	{
		return this.edgeGap;
	}

	/**
	 * @param newEdgeGap
	 *          the edgeGap to set
	 */
	public void setEdgeGap(double newEdgeGap)
	{
		this.edgeGap = newEdgeGap;
	}

	/**
	 * @return the borderGap
	 */
	public int getBorderGap()
	{
		return this.borderGap;
	}

	/**
	 * @param newBorderGap
	 *          the borderGap to set
	 */
	public void setBorderGap(int newBorderGap)
	{
		this.borderGap = newBorderGap;
	}

	/**
	 * @return the lineWidth
	 */
	public int getLineWidth()
	{
		return this.lineWidth;
	}

	/**
	 * @param newLineWidth
	 *          the lineWidth to set
	 */
	public void setLineWidth(int newLineWidth)
	{
		this.lineWidth = newLineWidth;
	}

	/**
	 * @return the arrowSize
	 */
	public Dimension getArrowSize()
	{
		return this.arrowSize;
	}

	/**
	 * @param newArrowSize
	 *          the arrowSize to set
	 */
	public void setArrowSize(Dimension newArrowSize)
	{
		this.arrowSize = newArrowSize;
	}

	/**
	 * @return the arrowEdge
	 */
	public double getArrowEdge()
	{
		return this.arrowEdge;
	}

	/**
	 * @param newArrowEdge
	 *          the arrowEdge to set
	 */
	public void setArrowEdge(double newArrowEdge)
	{
		this.arrowEdge = newArrowEdge;
	}

	/**
	 * @return the defaultColor
	 */
	public Color getDefaultColor()
	{
		return this.defaultColor;
	}

	/**
	 * @param newDefaultColor
	 *          the defaultColor to set
	 */
	public void setDefaultColor(Color newDefaultColor)
	{
		this.defaultColor = newDefaultColor;
	}

	/**
	 * @return the colorNodeValues
	 */
	public boolean isColorNodeValues()
	{
		return this.colorNodeValues;
	}

	/**
	 * @param newColorNodeValues
	 *          the colorNodeValues to set
	 */
	public void setColorNodeValues(boolean newColorNodeValues)
	{
		this.colorNodeValues = newColorNodeValues;
	}

	/**
	 * @return the showEdgeValues
	 */
	public boolean isShowEdgeValues()
	{
		return this.showEdgeValues;
	}

	/**
	 * @param newShowEdgeValues
	 *          the showEdgeValues to set
	 */
	public void setShowEdgeValues(boolean newShowEdgeValues)
	{
		this.showEdgeValues = newShowEdgeValues;
	}

	/**
	 * @return the showNodeValues
	 */
	public boolean isShowNodeValues()
	{
		return this.showNodeValues;
	}

	/**
	 * @param newShowNodeValues
	 *          the showNodeValues to set
	 */
	public void setShowNodeValues(boolean newShowNodeValues)
	{
		this.showNodeValues = newShowNodeValues;
	}

	/**
	 * @return the colorEdgeValues
	 */
	public boolean isColorEdgeValues()
	{
		return this.colorEdgeValues;
	}

	/**
	 * @param newColorEdgeValues
	 *          the colorEdgeValues to set
	 */
	public void setColorEdgeValues(boolean newColorEdgeValues)
	{
		this.colorEdgeValues = newColorEdgeValues;
	}

	/**
	 * @return the valueDistance
	 */
	public double getValueDistance()
	{
		return this.valueDistance;
	}

	/**
	 * @param newValueDistance
	 *          the valueDistance to set
	 */
	public void setValueDistance(double newValueDistance)
	{
		this.valueDistance = newValueDistance;
	}

	/**
	 * @return the showZeroEdges
	 */
	public boolean isShowZeroEdges()
	{
		return this.showZeroEdges;
	}

	/**
	 * @param newShowZeroEdges the showZeroEdges to set
	 */
	public void setShowZeroEdges(boolean newShowZeroEdges)
	{
		this.showZeroEdges = newShowZeroEdges;
	}

}
