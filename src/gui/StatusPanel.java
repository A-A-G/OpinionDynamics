/**
 * 
 */
package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import logic.SimulationData;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class StatusPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long				serialVersionUID	= 1L;

	private final JLabel						statusLabel				= new JLabel();
	private final JToggleButton			chartButton				= new JToggleButton("Show AGV");
	private final JToggleButton			flowButton				= new JToggleButton("Show Flow");
	private final JFrame						chartFrame				= new JFrame("AVG Chart");
	private final JFrame						flowFrame					= new JFrame("Flow");
	private final DefaultXYDataset	chartData					= new DefaultXYDataset();
	private final DefaultXYDataset	flowData					= new DefaultXYDataset();
	private ChartPanel							flowPanel					= null;

	public StatusPanel()
	{
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.setPreferredSize(new Dimension(this.getWidth(), 22));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.add(this.statusLabel);
		this.add(Box.createHorizontalGlue());
		this.add(this.chartButton);
		this.chartButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				StatusPanel.this.chartFrame.setVisible(StatusPanel.this.chartButton.isSelected());
			}
		});
		this.add(this.flowButton);
		this.flowButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				StatusPanel.this.flowFrame.setVisible(StatusPanel.this.flowButton.isSelected());
			}
		});
		createChartFrame();
		createFlowFrame();
	}

	private void createChartFrame()
	{
		this.chartFrame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(final WindowEvent event)
			{
				StatusPanel.this.chartButton.setSelected(false);
			}
		});
		this.chartFrame.setLayout(new BorderLayout());
		this.chartFrame.add(new ChartPanel(ChartFactory.createXYLineChart("Average Opinion", "Time", "Opinion", this.chartData, PlotOrientation.VERTICAL, true, true, false)), BorderLayout.CENTER);
		this.chartFrame.pack();
	}

	private void createFlowFrame()
	{
		this.flowFrame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(final WindowEvent event)
			{
				StatusPanel.this.flowButton.setSelected(false);
			}
		});
		this.flowFrame.setLayout(new BorderLayout());
		this.flowPanel = new ChartPanel(ChartFactory.createXYLineChart("Opinion Flow", "Time", "Opinion", this.flowData, PlotOrientation.VERTICAL, false, true, false));
		this.flowFrame.add(this.flowPanel, BorderLayout.CENTER);
		this.flowFrame.pack();
	}

	public void setMessage(String m)
	{
		this.statusLabel.setText(m);
	}

	public void setSimulationData(SimulationData simData)
	{
		if (null == simData) { return; }
		this.flowButton.setVisible(true);
		updateFlow(simData);
		this.chartButton.setVisible(true);
		updateChart(simData);
	}

	private void updateChart(SimulationData simData)
	{
		this.chartData.addSeries("Variable Average Opinion", makeSeries(simData.VAR_AVG));
		this.chartData.addSeries("Total Average Opinion", makeSeries(simData.FULL_AVG));
		this.chartData.addSeries("Variable Absolut Average Opinion", makeSeries(simData.ABS_VAR_AVG));
		this.chartData.addSeries("Total Absolut Average Opinion", makeSeries(simData.ABS_FULL_AVG));
	}

	private void updateFlow(SimulationData simData)
	{
		for (int i = 0; i < this.flowData.getSeriesCount(); i++)
		{
			this.flowData.removeSeries(this.flowData.getSeriesKey(i));
		}
		for (int i = 0; i < simData.FLOW.length; i++)
		{
			this.flowData.addSeries(new Integer(i), makeSeries(simData.FLOW[i]));
		}
		for (int i = 0; i < this.flowData.getSeriesCount(); i++)
		{
			this.flowPanel.getChart().getXYPlot().getRenderer().setSeriesStroke(i, new BasicStroke(2.0f));
		}
	}

	private static double[][] makeSeries(double[] d)
	{
		double[][] result = new double[2][d.length];
		for (int i = 0; i < d.length; i++)
		{
			result[0][i] = i;
			result[1][i] = d[i];
		}
		return result;
	}

}
