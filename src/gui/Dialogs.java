/**
 * 
 */
package gui;

import graph.Graph;
import graph.Graph.SelectedKeys;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import xml.GraphMLKey;
import logic.Simulation;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class Dialogs
{

	public static int simulationSettingsDialog(JFrame parent)
	{
		JPanel settings = new JPanel();
		settings.setLayout(new GridLayout(2, 2));
		settings.add(new JLabel("Iterations:"));
		SpinnerNumberModel iterationModel = new SpinnerNumberModel(Simulation.ITERATIONS, 0, 999999, 1);
		JSpinner iterationSpinner = new JSpinner(iterationModel);
		settings.add(iterationSpinner);
		settings.add(new JLabel("Sleep:"));
		SpinnerNumberModel sleepModel = new SpinnerNumberModel(Simulation.SLEEP_TIME, 0, 999999, 1);
		JSpinner sleepSpinner = new JSpinner(sleepModel);
		settings.add(sleepSpinner);
		Object[] array = { new JLabel("Simulation settings", SwingConstants.CENTER), settings };
		Object[] options = { "OK", "Cancel" };
		int ret = JOptionPane.showOptionDialog(parent, array, "Simulation Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (JOptionPane.OK_OPTION == ret)
		{
			Simulation.ITERATIONS = iterationModel.getNumber().intValue();
			Simulation.SLEEP_TIME = sleepModel.getNumber().intValue();
		}
		return ret;
	}

	public static GraphMLKey[] selectValuesDialog(JFrame parent, Graph graph)
	{
		return selectValuesDialog(parent, graph.getKeys(), graph.getSelectedKeys());
	}

	public static GraphMLKey[] selectValuesDialog(JFrame parent, HashMap<String, GraphMLKey> keys, GraphMLKey[] selectedKeys)
	{
		JPanel keyPanel = new JPanel(new GridBagLayout());
		int lc = 0;
		JLabel selectValueLabel = new JLabel("Select Values", SwingConstants.CENTER);
		selectValueLabel.setFont(selectValueLabel.getFont().deriveFont(selectValueLabel.getFont().getSize2D() + 2));
		GridBagConstraints gbc = makeGbc(0, lc++);
		gbc.insets = new Insets(10, 5, 5, 5);
		gbc.gridwidth = 2;
		keyPanel.add(selectValueLabel, gbc);
		gbc = makeGbc(0, lc++);
		gbc.insets = new Insets(10, 5, 5, 5);
		gbc.gridwidth = 2;
		keyPanel.add(new JSeparator(), gbc);
		gbc = makeGbc(0, lc++);
		gbc.insets = new Insets(10, 5, 5, 5);
		gbc.gridwidth = 2;
		JLabel nodes = new JLabel("Nodes", SwingConstants.CENTER);
		keyPanel.add(nodes, gbc);
		gbc = makeGbc(0, lc);
		keyPanel.add(new JLabel("Description:"), gbc);
		gbc = makeGbc(1, lc++);
		//JComboBox<GraphMLKey> nodeDescBox = getKeyBox(keys, selectedKeys[SelectedKeys.NodeDescription.value()], "node", "string");
		JComboBox nodeDescBox = getKeyBox(keys, selectedKeys[SelectedKeys.NodeDescription.value()], "node", "string");
		keyPanel.add(nodeDescBox, gbc);
		gbc = makeGbc(0, lc);
		keyPanel.add(new JLabel("Values:"), gbc);
		gbc = makeGbc(1, lc++);
		//JComboBox<GraphMLKey> nodeBox = getKeyBox(keys, selectedKeys[SelectedKeys.NodeValue.value()], "node", "number");
		JComboBox nodeBox = getKeyBox(keys, selectedKeys[SelectedKeys.NodeValue.value()], "node", "number");
		keyPanel.add(nodeBox, gbc);
		JPanel nodeMinBound = new JPanel();
		BoxLayout boxMinNodeLayout = new BoxLayout(nodeMinBound, BoxLayout.X_AXIS);
		nodeMinBound.setLayout(boxMinNodeLayout);
		nodeMinBound.add(new JLabel("Min:"));
		SpinnerNumberModel nodeMin = new SpinnerNumberModel(selectedKeys[SelectedKeys.NodeValue.value()].minValue, -99, 99, 1);
		JSpinner nodeMinSpinner = new JSpinner(nodeMin);
		nodeMinSpinner.setMaximumSize(new Dimension(50, 2 * nodeMinSpinner.getFont().getSize()));
		nodeMinBound.add(nodeMinSpinner);
		gbc = makeGbc(0, lc);
		keyPanel.add(nodeMinBound, gbc);
		JPanel nodeMaxBound = new JPanel();
		BoxLayout boxMaxNodeLayout = new BoxLayout(nodeMaxBound, BoxLayout.X_AXIS);
		nodeMaxBound.setLayout(boxMaxNodeLayout);
		nodeMaxBound.add(new JLabel("Max:"));
		SpinnerNumberModel nodeMax = new SpinnerNumberModel(selectedKeys[SelectedKeys.NodeValue.value()].maxValue, -99, 99, 1);
		JSpinner nodeMaxSpinner = new JSpinner(nodeMax);
		nodeMaxSpinner.setMaximumSize(new Dimension(50, 2 * nodeMaxSpinner.getFont().getSize()));
		nodeMaxBound.add(nodeMaxSpinner);
		gbc = makeGbc(1, lc++);
		keyPanel.add(nodeMaxBound, gbc);
		gbc = makeGbc(0, lc++);
		gbc.insets = new Insets(10, 5, 5, 5);
		gbc.gridwidth = 2;
		keyPanel.add(new JSeparator(), gbc);
		gbc = makeGbc(0, lc++);
		gbc.insets = new Insets(10, 5, 5, 5);
		gbc.gridwidth = 2;
		JLabel edges = new JLabel("Edges", SwingConstants.CENTER);
		keyPanel.add(edges, gbc);
		gbc = makeGbc(0, lc);
		keyPanel.add(new JLabel("Description:"), gbc);
		gbc = makeGbc(1, lc++);
		//JComboBox<GraphMLKey> edgeDescBox = getKeyBox(keys, selectedKeys[SelectedKeys.EdgeDescription.value()], "edge", "string");
		JComboBox edgeDescBox = getKeyBox(keys, selectedKeys[SelectedKeys.EdgeDescription.value()], "edge", "string");
		keyPanel.add(edgeDescBox, gbc);
		gbc = makeGbc(0, lc);
		keyPanel.add(new JLabel("Values:"), gbc);
		gbc = makeGbc(1, lc++);
		//JComboBox<GraphMLKey> edgeBox = getKeyBox(keys, selectedKeys[SelectedKeys.EdgeValue.value()], "edge", "number");
		JComboBox edgeBox = getKeyBox(keys, selectedKeys[SelectedKeys.EdgeValue.value()], "edge", "number");
		keyPanel.add(edgeBox, gbc);
		JPanel edgeMinBound = new JPanel();
		BoxLayout boxMinEdgeLayout = new BoxLayout(edgeMinBound, BoxLayout.X_AXIS);
		edgeMinBound.setLayout(boxMinEdgeLayout);
		edgeMinBound.add(new JLabel("Min:"));
		SpinnerNumberModel edgeMin = new SpinnerNumberModel(selectedKeys[SelectedKeys.EdgeValue.value()].minValue, -99, 99, 1);
		JSpinner edgeMinSpinner = new JSpinner(edgeMin);
		edgeMinSpinner.setMaximumSize(new Dimension(50, 2 * edgeMinSpinner.getFont().getSize()));
		edgeMinBound.add(edgeMinSpinner);
		gbc = makeGbc(0, lc);
		keyPanel.add(edgeMinBound, gbc);
		JPanel edgeMaxBound = new JPanel();
		BoxLayout boxMaxEdgeLayout = new BoxLayout(edgeMaxBound, BoxLayout.X_AXIS);
		edgeMaxBound.setLayout(boxMaxEdgeLayout);
		edgeMaxBound.add(new JLabel("Max:"));
		SpinnerNumberModel edgeMax = new SpinnerNumberModel(selectedKeys[SelectedKeys.EdgeValue.value()].maxValue, -99, 99, 1);
		JSpinner edgeMaxSpinner = new JSpinner(edgeMax);
		edgeMaxSpinner.setMaximumSize(new Dimension(50, 2 * edgeMaxSpinner.getFont().getSize()));
		edgeMaxBound.add(edgeMaxSpinner);
		gbc = makeGbc(1, lc++);
		keyPanel.add(edgeMaxBound, gbc);
		gbc = makeGbc(0, lc++);
		gbc.insets = new Insets(10, 5, 5, 5);
		gbc.gridwidth = 2;
		keyPanel.add(new JSeparator(), gbc);
		gbc = makeGbc(0, lc++);
		gbc.insets = new Insets(10, 5, 5, 5);
		gbc.gridwidth = 2;
		JLabel stubs = new JLabel("Stubbornness", SwingConstants.CENTER);
		keyPanel.add(stubs, gbc);
		gbc = makeGbc(0, lc);
		keyPanel.add(new JLabel("Values:"), gbc);
		gbc = makeGbc(1, lc++);
		//JComboBox<GraphMLKey> stubBox = getKeyBox(keys, selectedKeys[SelectedKeys.StubValue.value()], "node", "number");
		JComboBox stubBox = getKeyBox(keys, selectedKeys[SelectedKeys.StubValue.value()], "node", "number");
		keyPanel.add(stubBox, gbc);
		gbc = makeGbc(0, lc++);
		gbc.insets = new Insets(10, 5, 5, 5);
		gbc.gridwidth = 2;
		keyPanel.add(new JSeparator(), gbc);
		Object[] array = { keyPanel };
		Object[] options = { "OK", "Cancel" };
		if (JOptionPane.OK_OPTION == JOptionPane.showOptionDialog(parent, array, "Import Graph", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]))
		{
			GraphMLKey[] newSelectedKeys = new GraphMLKey[5];
			GraphMLKey nodeKey = (GraphMLKey) nodeBox.getSelectedItem();
			if (null != nodeKey)
			{
				nodeKey.minValue = nodeMin.getNumber().doubleValue();
				nodeKey.maxValue = nodeMax.getNumber().doubleValue();
			}
			newSelectedKeys[SelectedKeys.NodeValue.value()] = nodeKey;
			GraphMLKey edgeKey = (GraphMLKey) edgeBox.getSelectedItem();
			if (null != edgeKey)
			{
				edgeKey.minValue = edgeMin.getNumber().doubleValue();
				edgeKey.maxValue = edgeMax.getNumber().doubleValue();
			}
			newSelectedKeys[SelectedKeys.EdgeValue.value()] = edgeKey;
			newSelectedKeys[SelectedKeys.NodeDescription.value()] = (GraphMLKey) nodeDescBox.getSelectedItem();
			newSelectedKeys[SelectedKeys.EdgeDescription.value()] = (GraphMLKey) edgeDescBox.getSelectedItem();
			newSelectedKeys[SelectedKeys.StubValue.value()] = (GraphMLKey) stubBox.getSelectedItem();
			return newSelectedKeys;
		}
		return null;
	}

	//public static JComboBox<GraphMLKey> getKeyBox(HashMap<String, GraphMLKey> keys, GraphMLKey defaultKey, String elementType, String valueType)
	public static JComboBox getKeyBox(HashMap<String, GraphMLKey> keys, GraphMLKey defaultKey, String elementType, String valueType)
	{
		//JComboBox<GraphMLKey> keyBox = new JComboBox<>();
		JComboBox keyBox = new JComboBox();
		keyBox.addItem(defaultKey);
		if (null != defaultKey.id)
		{
			GraphMLKey key = new GraphMLKey();
			key.name = "";
			keyBox.addItem(key);
		}
		else
		{
			defaultKey.name = "";
		}
		for (GraphMLKey key : keys.values())
		{
			if (key.elementType.equals(elementType) && key.valueType.equals(valueType) && !key.name.equals("") && !key.id.equals(defaultKey.id))
			{
				keyBox.addItem(key);
			}
		}
		return keyBox;
	}
	
	public static String selectFileDialog(JFrame parent, String selectedFile)
	{
		File f = new File(selectedFile);
		JFileChooser fc;
		if (f.exists())
		{
			fc = new JFileChooser(f);
		}
		else
		{
			fc = new JFileChooser();
		}
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
		{
			return fc.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	private static GridBagConstraints makeGbc(int x, int y)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.weightx = x;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = (x == 0) ? GridBagConstraints.LINE_START : GridBagConstraints.LINE_END;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		return gbc;
	}

}
