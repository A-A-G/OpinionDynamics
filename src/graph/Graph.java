/**
 * 
 */
package graph;

import gui.Dialogs;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Collections;

import javax.swing.JFrame;

import xml.GraphMLKey;
import xml.GraphMLReader;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class Graph extends Observable
{

	public enum ChangeType
	{
		NONE, VALUES, ELEMENTS
	}

	public enum SelectedKeys
	{
		NodeValue(0), NodeDescription(1), EdgeValue(2), EdgeDescription(3), StubValue(4);

		private final int	value;

		private SelectedKeys(int newValue)
		{
			this.value = newValue;
		}

		public int value()
		{
			return this.value;
		}
	}

	private boolean											locked				= false;
	private ChangeType									changeType		= ChangeType.NONE;
	private HashMap<String, GraphNode>	nodeList			= new HashMap<String, GraphNode>();
	private HashMap<String, GraphMLKey>	keys					= new HashMap<String, GraphMLKey>();
	private GraphMLKey[]								selectedKeys	= new GraphMLKey[] { new GraphMLKey(), new GraphMLKey(), new GraphMLKey(), new GraphMLKey(), new GraphMLKey() };

	public void importData(JFrame parent, String file)
	{
		HashMap<String, GraphNode> tmpNodes = new HashMap<String, GraphNode>(this.nodeList);
		HashMap<String, GraphMLKey> tmpKeys = new HashMap<String, GraphMLKey>(this.keys);
		try
		{
			this.nodeList.clear();
			this.keys.clear();
			GraphMLReader.readGraphFromXML(file, this.nodeList, this.keys);
			GraphMLKey[] newSelectedKeys = Dialogs.selectValuesDialog(parent, this.keys, new GraphMLKey[] { new GraphMLKey(), new GraphMLKey(), new GraphMLKey(), new GraphMLKey(), new GraphMLKey() });
			if (null != newSelectedKeys)
			{
				changeValuesAndDescription(newSelectedKeys, false);
				setDefaultValues(true, true, true);
				this.changeType = ChangeType.ELEMENTS;
				changed();
				return;
			}
		}
		catch (Exception exeption)
		{
			importError(exeption);
		}
		this.nodeList = tmpNodes;
		this.keys = tmpKeys;
	}

	public void changeValuesAndDescription(GraphMLKey[] newKeys)
	{
		changeValuesAndDescription(newKeys, true);
	}

	public void changeValuesAndDescription(GraphMLKey[] newKeys, boolean update)
	{
		if ((null == newKeys) || (newKeys.length == 0))
		{
			return;
		}
		this.selectedKeys = Arrays.copyOf(newKeys, 5);
		for (GraphNode node : this.nodeList.values())
		{
			if ("" == this.selectedKeys[SelectedKeys.NodeValue.value()].name)
			{
				node.value = 0;
			}
			else
			{
				node.newKeySelected(this.selectedKeys[SelectedKeys.NodeValue.value()]);
			}
			if ("" == this.selectedKeys[SelectedKeys.NodeDescription.value()].name)
			{
				node.description = "";
			}
			else
			{
				node.newKeySelected(this.selectedKeys[SelectedKeys.NodeDescription.value()]);
			}
			if ("" == this.selectedKeys[SelectedKeys.StubValue.value()].name)
			{
				node.setStubbornness(0.5);
			}
			else
			{
				node.newStubKeySelected(this.selectedKeys[SelectedKeys.StubValue.value()]);
			}
			for (GraphEdge edge : node.getIncomingEdges().values())
			{
				if ("" == this.selectedKeys[SelectedKeys.EdgeValue.value()].name)
				{
					edge.value = 0;
				}
				else
				{
					edge.newKeySelected(this.selectedKeys[SelectedKeys.EdgeValue.value()]);
				}
				if ("" == this.selectedKeys[SelectedKeys.EdgeDescription.value()].name)
				{
					edge.description = "";
				}
				else
				{
					edge.newKeySelected(this.selectedKeys[SelectedKeys.EdgeDescription.value()]);
				}
			}
		}
		if (update)
		{
			this.changeType = ChangeType.ELEMENTS;
			changed();
		}
	}

	public HashMap<String, GraphMLKey> getSpecificKeys(String elementType, String valueType)
	{
		return getSpecificKeys(this.keys, elementType, valueType);
	}

	public static HashMap<String, GraphMLKey> getSpecificKeys(HashMap<String, GraphMLKey> keys, String elementType, String valueType)
	{
		HashMap<String, GraphMLKey> specificKeys = new HashMap<String, GraphMLKey>();
		for (GraphMLKey key : keys.values())
		{
			if (!((null != elementType) && (!key.elementType.equals(elementType))) || ((null != valueType) && (!key.valueType.equals(valueType))))
			{
				specificKeys.put(key.name, key);
			}
		}
		return specificKeys;
	}

	private static void importError(Exception exeption)
	{
		System.out.println("Error reading graphml file!");
		System.out.println(exeption.toString());
		exeption.printStackTrace();
	}

	public void setNodeList(HashMap<String, GraphNode> newNodeList)
	{
		if (newNodeList != null)
		{
			this.nodeList = newNodeList;
			this.changeType = ChangeType.ELEMENTS;
			changed();
		}
		else
		{
			clear();
		}
	}

	public void randomize(boolean nodes, boolean edges)
	{
		if (!nodes && !edges)
		{
			return;
		}
		Random r = new Random();
		for (GraphNode node : this.nodeList.values())
		{
			if (nodes)
			{
				node.setValue(1 - 2 * r.nextFloat());
			}
			for (GraphEdge edge : node.getIncomingEdges().values())
			{
				if (edges)
				{
					edge.setValue(1 - 2 * r.nextFloat());
				}
			}
		}
		update();
	}

	public double[] getExtrema()
	{
		double extrema[] = { Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE };
		for (GraphNode node : getNodeCollection())
		{
			if (extrema[0] > node.getGeometry().getX())
			{
				extrema[0] = node.getGeometry().getX();
			}
			if (extrema[1] < (node.getGeometry().getX() + node.getGeometry().getWidth()))
			{
				extrema[1] = node.getGeometry().getX() + node.getGeometry().getWidth();
			}
			if (extrema[2] > node.getGeometry().getY())
			{
				extrema[2] = node.getGeometry().getY();
			}
			if (extrema[3] < (node.getGeometry().getY() + node.getGeometry().getHeight()))
			{
				extrema[3] = node.getGeometry().getY() + node.getGeometry().getHeight();
			}
		}
		return extrema;
	}

	public Map<String, GraphNode> getNodeMap()
	{
		return Collections.unmodifiableMap(this.nodeList);
	}

	public Collection<GraphNode> getNodeCollection()
	{
		return Collections.unmodifiableCollection(this.nodeList.values());
	}

	public void clear()
	{
		if (this.nodeList.size() > 0)
		{
			this.nodeList.clear();
			this.changeType = ChangeType.ELEMENTS;
			changed();
		}
	}

	public void setDefaultValues(boolean nodes, boolean edges, boolean stubborness)
	{
		if (!nodes && !edges)
		{
			return;
		}
		for (GraphNode node : this.nodeList.values())
		{
			if (nodes)
			{
				node.updateDefaultValue();
			}
			if (stubborness)
			{
				node.updateDefaultStubborness();
			}
			if (edges)
			{
				for (GraphEdge edge : node.getIncomingEdges().values())
				{
					edge.updateDefaultValue();
				}
			}
		}
	}

	public void loadDefaultValues(boolean nodes, boolean edges, boolean stubborness)
	{
		if (!nodes && !edges)
		{
			return;
		}
		for (GraphNode node : this.nodeList.values())
		{
			if (nodes)
			{
				node.updateDefaultValue();
			}
			if (stubborness)
			{
				node.updateDefaultStubborness();
			}
			if (edges)
			{
				for (GraphEdge edge : node.getIncomingEdges().values())
				{
					edge.resetValue();
				}
			}
		}
		update();
	}

	private void changed()
	{
		setChanged();
		notifyObservers();
	}

	public void update()
	{
		this.changeType = ChangeType.VALUES;
		changed();
	}

	/**
	 * @return the changeType
	 */
	public ChangeType getChangeType()
	{
		return this.changeType;
	}

	/**
	 * @param locked
	 *          the locked to set
	 */
	public void setLocked(boolean newLocked)
	{
		this.locked = newLocked;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked()
	{
		return this.locked;
	}

	/**
	 * @return the keys
	 */
	public HashMap<String, GraphMLKey> getKeys()
	{
		return this.keys;
	}

	/**
	 * @return the selectedKeys
	 */
	public GraphMLKey[] getSelectedKeys()
	{
		return this.selectedKeys;
	}

}