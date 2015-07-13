/**
 * 
 */
package logic;

import java.util.Observable;

import graph.Graph;
import graph.GraphEdge;
import graph.GraphNode;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class Simulation extends Observable implements Runnable
{
	public static int				ITERATIONS	= 100;
	public static int				SLEEP_TIME	= 100;

	private Graph						graph;
	private boolean					stop				= false;

	private SimulationData	simData			= new SimulationData();

	public Simulation(Graph newGraph, int iterations)
	{
		ITERATIONS = iterations;
		this.graph = newGraph;
	}

	public Simulation(Graph newGraph)
	{
		this.graph = newGraph;
	}

	public void halt()
	{
		this.stop = true;
	}

	@Override
	public void run()
	{
		this.graph.setLocked(true);
		String tab = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		int i = 0;
		long startTime = System.currentTimeMillis();
		long timeLast = System.currentTimeMillis();
		this.simData.VAR_AVG = new double[ITERATIONS + 1];
		this.simData.ABS_VAR_AVG = new double[ITERATIONS + 1];
		this.simData.FULL_AVG = new double[ITERATIONS + 1];
		this.simData.ABS_FULL_AVG = new double[ITERATIONS + 1];
		this.simData.FLOW = new double[this.graph.getNodeCollection().size()][ITERATIONS + 1];
		calcData(i);
		sendMessage(String.format("<html>Iterations: " + i + tab + "Var-AVG: %1.2g" + tab + "Full-AVG: %1.2g" + tab + "Time total: " + (System.currentTimeMillis() - startTime) / 1000 + "s" + tab + "Time last: " + (System.currentTimeMillis() - timeLast) + " ms", new Double(this.simData.VAR_AVG[i]), new Double(this.simData.FULL_AVG[i])));
		for (i = 1; i <= ITERATIONS; i++)
		{
			timeLast = System.currentTimeMillis();
			nextIteration(i);
			sendMessage(String.format("<html>Iterations: " + i + tab + "VAR-AVG: %1.2g" + tab + "Full-AVG: %1.2g" + tab + "Time total: " + (System.currentTimeMillis() - startTime) / 1000 + "s" + tab + "Time last: " + (System.currentTimeMillis() - timeLast) + " ms", new Double(this.simData.VAR_AVG[i]), new Double(this.simData.FULL_AVG[i])));
			try
			{
				Thread.sleep(SLEEP_TIME);
			}
			catch (InterruptedException e)
			{
				System.out.println(e);
			}
			if (this.stop)
			{
				break;
			}
		}
		this.graph.setLocked(false);
		this.stop = false;
		sendMessage("stopped");
	}

	public void nextIteration(int i)
	{
		double sumVar = 0;
		double sumVarAbs = 0;
		double sumFull = 0;
		double sumFullAbs = 0;
		int activeNodeCounter = 0;
		for (GraphNode node : this.graph.getNodeCollection())
		{
			if ((node.getIncomingEdges().size() > 0)&&(node.getStubbornness()<1))
			{
				double x_i = 0;
				double abs = 0;
				for (GraphEdge edge : node.getIncomingEdges().values())
				{
					x_i = x_i + this.graph.getNodeMap().get(edge.getSourceID()).getSimulationValue() * edge.getValue();
					abs = abs + Math.abs(edge.getValue());
				}
				if (abs > 0)
				{
					double newValue = node.getStubbornness() * node.getSimulationValue() + (1 - node.getStubbornness()) * x_i / abs;
					node.setSimulationValue(newValue);
					sumVar = sumVar + newValue;
					sumVarAbs = sumVarAbs + Math.abs(newValue);
					activeNodeCounter++;
				}
			}
			sumFull = sumFull + node.getValue();
			sumFullAbs = sumFullAbs + Math.abs(node.getValue());
			this.simData.FLOW[node.getNodeNumber()][i] = node.getValue();
		}
		if (activeNodeCounter > 0)
		{
			this.simData.VAR_AVG[i] = sumVar / activeNodeCounter;
			this.simData.ABS_VAR_AVG[i] = sumVarAbs / activeNodeCounter;
		}
		else
		{
			this.simData.VAR_AVG[i] = 0;
		}
		if (this.graph.getNodeCollection().size() > 0)
		{
			this.simData.FULL_AVG[i] = sumFull / this.graph.getNodeCollection().size();
			this.simData.ABS_FULL_AVG[i] = sumFullAbs / this.graph.getNodeCollection().size();
		}
		else
		{
			this.simData.FULL_AVG[i] = 0;
		}
		resetChanged();
		this.graph.update();
	}

	public void resetChanged()
	{
		for (GraphNode node : this.graph.getNodeCollection())
		{
			node.setChanged(false);
		}
	}

	private void sendMessage(String s)
	{
		setChanged();
		notifyObservers(s);
	}

	private void calcData(int i)
	{
		double sumVar = 0;
		double sumVarAbs = 0;
		double sumFull = 0;
		double sumFullAbs = 0;
		int activeNodeCounter = 0;
		for (GraphNode node : this.graph.getNodeCollection())
		{
			if (node.getIncomingEdges().size() > 0)
			{
				double abs = 0;
				for (GraphEdge edge : node.getIncomingEdges().values())
				{
					abs = abs + Math.abs(edge.getValue());
				}
				if ((abs > 0) && (node.getStubbornness() < 1))
				{
					sumVar = sumVar + node.getValue();
					sumVarAbs = sumVarAbs + Math.abs(node.getValue());
					activeNodeCounter++;
				}
			}
			this.simData.FLOW[node.getNodeNumber()][i] = node.getValue();
			sumFull = sumFull + node.getValue();
			sumFullAbs = sumFullAbs + Math.abs(node.getValue());
		}
		if (activeNodeCounter > 0)
		{
			this.simData.VAR_AVG[i] = sumVar / activeNodeCounter;
			this.simData.ABS_VAR_AVG[i] = sumVarAbs / activeNodeCounter;
		}
		else
		{
			this.simData.VAR_AVG[i] = 0;
		}
		if (this.graph.getNodeCollection().size() > 0)
		{
			this.simData.FULL_AVG[i] = sumFull / this.graph.getNodeCollection().size();
			this.simData.ABS_FULL_AVG[i] = sumFullAbs / this.graph.getNodeCollection().size();
		}
		else
		{
			this.simData.FULL_AVG[i] = 0;
		}
	}

	/**
	 * @return the data
	 */
	public SimulationData getSimulationData()
	{
		return this.simData;
	}

}