/**
 * 
 */
package gui;

import graph.Graph;
import gui.editpanels.EdgePanel;
import gui.editpanels.NodePanel;
import gui.editpanels.StubPanel;
import gui.graph.GraphDrawSettings;
import gui.graph.GraphPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import utils.ColorHandler;
import logic.Simulation;

import bibliothek.gui.dock.common.CContentArea;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;
import bibliothek.gui.dock.common.intern.DefaultCDockable;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class MainFrame extends JFrame implements Observer
{

	/**
	 * 
	 */
	private static final long				serialVersionUID			= 1L;

	//private String									selectedFile					= "/home/alex/Dropbox//OpinionDynamics/graphml/";		// Linux
	private String selectedFile = "C:\\Users\\Alex\\Dropbox\\OpinionDynamics\\graphml"; // Windows

	private final Graph							graph									= new Graph();

	private Thread									simulationThread			= null;
	private Simulation							simulation						= null;

	private final GraphPanel				graphPanel						= new GraphPanel(this.graph);
	private final NodePanel					nodePanel							= new NodePanel(this.graph);
	private final EdgePanel					edgePanel							= new EdgePanel(this.graph);
	private final StubPanel					stubPanel							= new StubPanel(this.graph);
	private final StatusPanel				statusPanel						= new StatusPanel();

	private final JMenuItem					empty									= new JMenuItem("Empty");
	private final JMenuItem					select								= new JMenuItem("Select Values");
	private final JMenuItem					stop									= new JMenuItem("Stop");
	private final JMenuItem					run										= new JMenuItem("Run");
	private final JMenuItem					runChoose							= new JMenuItem("Run...");
	private final JMenuItem					runSettings						= new JMenuItem("Settings");
	private final JMenuItem					imp										= new JMenuItem("Import");
	private final JMenuItem					setDefaultGraphValues	= new JMenuItem("Graph Values");
	private final JMenuItem					loadGraphValues				= new JMenuItem("Graph Values");
	private final JMenuItem					setDefaultNodeValues	= new JMenuItem("Node Values");
	private final JMenuItem					loadNodeValues				= new JMenuItem("Node Values");
	private final JMenuItem					setDefaultEdgeValues	= new JMenuItem("Edge Values");
	private final JMenuItem					loadEdgeValues				= new JMenuItem("Edge Values");
	private final JMenuItem					setStubborness				= new JMenuItem("Stubborness");
	private final JMenuItem					loadStubborness				= new JMenuItem("Stubborness");
	private final JMenuItem					randomizeGraph				= new JMenuItem("Random Graph Values");
	private final JMenuItem					randomizeNodes				= new JMenuItem("Random Node Values");
	private final JMenuItem					randomizeEdges				= new JMenuItem("Random Edge Values");
	private final JCheckBoxMenuItem	zoom									= new JCheckBoxMenuItem("Zoom", true);
	private final JCheckBoxMenuItem	edgeLabels						= new JCheckBoxMenuItem("Edge Labels", false);
	private final JCheckBoxMenuItem	colorEdgeLabels				= new JCheckBoxMenuItem("Color Edge Labels", true);
	private final JCheckBoxMenuItem	nodeLabels						= new JCheckBoxMenuItem("Node Labels", true);
	private final JCheckBoxMenuItem	colorNodeLabels				= new JCheckBoxMenuItem("Color Node Labels", true);
	private final JCheckBoxMenuItem	showEdgeValues				= new JCheckBoxMenuItem("Edge Values", false);
	private final JCheckBoxMenuItem	colorEdgeValues				= new JCheckBoxMenuItem("Color Edge Values", false);
	private final JCheckBoxMenuItem	showNodeValues				= new JCheckBoxMenuItem("Node Values", false);
	private final JCheckBoxMenuItem	colorNodeValues				= new JCheckBoxMenuItem("Color Node Values", false);
	private final JCheckBoxMenuItem	drawBorders						= new JCheckBoxMenuItem("Draw Border", false);
	private final JCheckBoxMenuItem	autoEdgeGap						= new JCheckBoxMenuItem("Auto Edge-Gap", false);

	public MainFrame(String name)
	{
		super(name);
		this.setMinimumSize(new Dimension(200, 200));
		this.add(this.statusPanel, BorderLayout.SOUTH);
		addMenu();
		this.graphPanel.enableZoom(this.zoom.getState());
		this.graphPanel.setColorEdgeLabels(this.colorEdgeLabels.getState());
		this.graphPanel.setPrintEdgeLabels(this.edgeLabels.getState());
		this.colorEdgeLabels.setEnabled(this.edgeLabels.getState());
		this.graphPanel.setColorNodeLabels(this.colorNodeLabels.getState());
		this.graphPanel.setPrintNodeLabels(this.nodeLabels.getState());
		this.colorNodeLabels.setEnabled(this.nodeLabels.getState());
		this.graphPanel.setDrawBorders(this.drawBorders.getState());
		this.graphPanel.setAutoEdgeGap(this.autoEdgeGap.getState());
		this.graphPanel.setShowEdgeValues(this.showEdgeValues.getState());
		this.graphPanel.setColorEdgeValues(this.colorEdgeValues.getState());
		this.colorEdgeValues.setEnabled(this.showEdgeValues.getState());
		this.graphPanel.setShowNodeValues(this.showNodeValues.getState());
		this.graphPanel.setColorNodeValues(this.colorNodeValues.getState());
		this.colorNodeValues.setEnabled(this.showNodeValues.getState());
		CControl control = new CControl(this);
		CContentArea area = control.getContentArea();
		this.add(area);
		CGrid grid = new CGrid(control);
		DefaultSingleCDockable graphDock = new DefaultSingleCDockable("GraphWindow", null, "Graph", new JScrollPane(this.graphPanel), DefaultCDockable.Permissions.NONE);
		grid.add(0, 0, 1, 1, graphDock);
		area.deploy(grid);
		JScrollPane nodeScroll = new JScrollPane(this.nodePanel);
		nodeScroll.getVerticalScrollBar().setUnitIncrement(20);
		SingleCDockable nodeDock = new DefaultSingleCDockable("NodeWindow", "Nodes", nodeScroll);
		control.addDockable(nodeDock);
		nodeDock.setLocation(CLocation.base().normalWest(0.1));
		nodeDock.setVisible(true);
		JScrollPane stubScroll = new JScrollPane(this.stubPanel);
		stubScroll.getVerticalScrollBar().setUnitIncrement(20);
		SingleCDockable stubDock = new DefaultSingleCDockable("StubbornnessWindow", "Stubbornness", stubScroll);
		control.addDockable(stubDock);
		stubDock.setLocation(CLocation.base().minimalEast());
		stubDock.setVisible(true);
		JScrollPane edgeScroll = new JScrollPane(this.edgePanel);
		edgeScroll.getVerticalScrollBar().setUnitIncrement(20);
		SingleCDockable edgeDock = new DefaultSingleCDockable("EdgeWindow", "Edges", edgeScroll);
		control.addDockable(edgeDock);
		edgeDock.setLocation(CLocation.base().normalEast(0.1));
		edgeDock.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	private void addMenu()
	{
		JMenuBar menu = new JMenuBar();
		menu.add(createFileMenu());
		menu.add(createGraphMenu());
		menu.add(createRunMenu());
		menu.add(createZoomMenu());
		menu.add(createViewMenu());
		this.add(menu, BorderLayout.PAGE_START);
	}

	private JMenu createFileMenu()
	{
		final JMenu file = new JMenu("File");
		this.empty.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				if (checkNotRunning("Clear"))
				{
					MainFrame.this.graph.clear();
				}
			}
		});
		file.add(this.empty);
		JMenuItem close = new JMenuItem("Close");
		close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
				setVisible(false);
			}
		});
		file.add(close);
		return file;
	}

	private JMenu createGraphMenu()
	{
		final JMenu dataMenu = new JMenu("Graph");
		this.imp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Import"))
				{
					String s = Dialogs.selectFileDialog(MainFrame.this, MainFrame.this.selectedFile);
					if (s != null)
					{
						MainFrame.this.selectedFile = s;
						MainFrame.this.graph.importData(MainFrame.this, s);
					}
				}
			}
		});
		dataMenu.add(this.imp);
		JMenuItem exp = new JMenuItem("Export");
		exp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// something
			}
		});
		exp.setEnabled(false);
		dataMenu.add(exp);
		dataMenu.addSeparator();
		this.select.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graph.changeValuesAndDescription(Dialogs.selectValuesDialog(MainFrame.this, MainFrame.this.graph));
			}
		});
		dataMenu.add(this.select);
		dataMenu.addSeparator();
		final JMenu defaultLoadMenu = new JMenu("Load from default...");
		dataMenu.add(defaultLoadMenu);
		this.loadGraphValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Load Graph Values"))
				{
					MainFrame.this.graph.loadDefaultValues(true, true, true);
				}
			}
		});
		defaultLoadMenu.add(this.loadGraphValues);
		this.loadNodeValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Load Node Values"))
				{
					MainFrame.this.graph.loadDefaultValues(true, false, false);
				}
			}
		});
		defaultLoadMenu.add(this.loadNodeValues);
		this.loadEdgeValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Load Edge Values"))
				{
					MainFrame.this.graph.loadDefaultValues(false, true, false);
				}
			}
		});
		defaultLoadMenu.add(this.loadEdgeValues);
		this.loadStubborness.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Load Stubborness"))
				{
					MainFrame.this.graph.loadDefaultValues(false, false, true);
				}
			}
		});
		defaultLoadMenu.add(this.loadStubborness);
		dataMenu.addSeparator();
		final JMenu defaultSetMenu = new JMenu("Set default values...");
		dataMenu.add(defaultSetMenu);
		this.setDefaultGraphValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Set Default Graph Values"))
				{
					MainFrame.this.graph.setDefaultValues(true, true, true);
				}
			}
		});
		defaultSetMenu.add(this.setDefaultGraphValues);
		this.setDefaultNodeValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Set Default Node Values"))
				{
					MainFrame.this.graph.setDefaultValues(true, false, false);
				}
			}
		});
		defaultSetMenu.add(this.setDefaultNodeValues);
		this.setDefaultEdgeValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Set Default Edge Values"))
				{
					MainFrame.this.graph.setDefaultValues(false, true, false);
				}
			}
		});
		defaultSetMenu.add(this.setDefaultEdgeValues);
		this.setStubborness.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Set Stubborness"))
				{
					MainFrame.this.graph.setDefaultValues(false, true, false);
				}
			}
		});
		defaultSetMenu.add(this.setStubborness);
		dataMenu.addSeparator();
		this.randomizeGraph.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Randomize Graph Values"))
				{
					MainFrame.this.graph.randomize(true, true);
				}
			}
		});
		dataMenu.add(this.randomizeGraph);
		this.randomizeNodes.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Randomize Node Values"))
				{
					MainFrame.this.graph.randomize(true, false);
				}
			}
		});
		dataMenu.add(this.randomizeNodes);
		this.randomizeEdges.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Randomize Edge Values"))
				{
					MainFrame.this.graph.randomize(false, true);
				}
			}
		});
		dataMenu.add(this.randomizeEdges);
		return dataMenu;
	}

	private JMenu createRunMenu()
	{
		final JMenu runMenu = new JMenu("Run");
		this.run.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Run"))
				{
					run();
				}
			}
		});
		runMenu.add(this.run);
		this.stop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (MainFrame.this.simulation != null)
				{
					MainFrame.this.simulation.halt();
				}
			}
		});
		this.stop.setEnabled(false);
		runMenu.add(this.stop);
		runMenu.addSeparator();
		this.runChoose.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Run..."))
				{
					if (JOptionPane.OK_OPTION == Dialogs.simulationSettingsDialog(MainFrame.this))
					{
						run();
					}
				}
			}
		});
		runMenu.add(this.runChoose);
		this.runSettings.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (checkNotRunning("Settings"))
				{
					Dialogs.simulationSettingsDialog(MainFrame.this);
				}
			}
		});
		runMenu.add(this.runSettings);
		return runMenu;
	}

	private JMenu createZoomMenu()
	{
		final JMenu zoomMenu = new JMenu("Zoom");
		this.zoom.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.enableZoom(MainFrame.this.zoom.getState());
			}
		});
		zoomMenu.add(this.zoom);
		final JMenuItem zoomReset = new JMenuItem("Reset");
		zoomReset.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.unzoom();
			}
		});
		zoomMenu.add(zoomReset);
		return zoomMenu;
	}

	private JMenu createViewMenu()
	{
		final JMenu colorMenu = new JMenu("View");
		colorMenu.add(createColorSchemesMenu());
		colorMenu.addSeparator();
		colorMenu.add(createLabelColorMenu());
		colorMenu.addSeparator();
		this.drawBorders.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setDrawBorders(MainFrame.this.drawBorders.getState());
			}
		});
		colorMenu.add(this.drawBorders);
		this.autoEdgeGap.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setAutoEdgeGap(MainFrame.this.autoEdgeGap.getState());
			}
		});
		colorMenu.add(this.autoEdgeGap);
		colorMenu.addSeparator();
		final JMenuItem drawSettings = new JMenuItem("Settings");
		drawSettings.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GraphDrawSettings.drawSettingsDialog(MainFrame.this, MainFrame.this.graphPanel);
			}
		});
		colorMenu.add(drawSettings);
		return colorMenu;
	}

	private JMenu createColorSchemesMenu()
	{
		final JMenu colorSchemesMenu = new JMenu("Color schemes...");
		final ButtonGroup colorSchemesGroup = new ButtonGroup();
		final JRadioButtonMenuItem redgreen = new JRadioButtonMenuItem("Red-Green", true);
		colorSchemesGroup.add(redgreen);
		redgreen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setColorSpace(ColorHandler.EColorSpace.RED_GREEN);
			}
		});
		colorSchemesMenu.add(redgreen);
		final JRadioButtonMenuItem redyellow = new JRadioButtonMenuItem("Red-Yellow");
		colorSchemesGroup.add(redyellow);
		redyellow.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setColorSpace(ColorHandler.EColorSpace.RED_YELLOW);
			}
		});
		colorSchemesMenu.add(redyellow);
		final JRadioButtonMenuItem yellowcyan = new JRadioButtonMenuItem("Yellow-Blue");
		colorSchemesGroup.add(yellowcyan);
		yellowcyan.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setColorSpace(ColorHandler.EColorSpace.YELLOW_BLUE);
			}
		});
		colorSchemesMenu.add(yellowcyan);
		final JRadioButtonMenuItem gray = new JRadioButtonMenuItem("Gray");
		colorSchemesGroup.add(gray);
		gray.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setColorSpace(ColorHandler.EColorSpace.GREY);
			}
		});
		colorSchemesMenu.add(gray);
		return colorSchemesMenu;
	}

	private JMenu createLabelColorMenu()
	{
		final JMenu labelColorMenu = new JMenu("Label");
		this.edgeLabels.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setPrintEdgeLabels(MainFrame.this.edgeLabels.getState());
				MainFrame.this.colorEdgeLabels.setEnabled(MainFrame.this.edgeLabels.getState());
			}
		});
		labelColorMenu.add(this.edgeLabels);
		this.colorEdgeLabels.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setColorEdgeLabels(MainFrame.this.colorEdgeLabels.getState());
			}
		});
		labelColorMenu.add(this.colorEdgeLabels);
		this.nodeLabels.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setPrintNodeLabels(MainFrame.this.nodeLabels.getState());
				MainFrame.this.colorNodeLabels.setEnabled(MainFrame.this.nodeLabels.getState());
			}
		});
		labelColorMenu.add(this.nodeLabels);
		this.colorNodeLabels.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setColorNodeLabels(MainFrame.this.colorNodeLabels.getState());
			}
		});
		labelColorMenu.add(this.colorNodeLabels);
		labelColorMenu.addSeparator();
		this.showEdgeValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setShowEdgeValues(MainFrame.this.showEdgeValues.getState());
				MainFrame.this.colorEdgeValues.setEnabled(MainFrame.this.showEdgeValues.getState());
			}
		});
		labelColorMenu.add(this.showEdgeValues);
		this.colorEdgeValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setColorEdgeValues(MainFrame.this.colorEdgeLabels.getState());
			}
		});
		labelColorMenu.add(this.colorEdgeValues);
		this.showNodeValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setShowNodeValues(MainFrame.this.showNodeValues.getState());
				MainFrame.this.colorNodeValues.setEnabled(MainFrame.this.showNodeValues.getState());
			}
		});
		labelColorMenu.add(this.showNodeValues);
		this.colorNodeValues.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainFrame.this.graphPanel.setColorNodeValues(MainFrame.this.colorNodeValues.getState());
			}
		});
		labelColorMenu.add(this.colorNodeValues);
		return labelColorMenu;
	}

	private boolean checkNotRunning(String name)
	{
		if (!((this.simulationThread != null) && this.simulationThread.isAlive())) { return true;

		}
		this.statusPanel.setMessage(name + ": Simulation already running...");
		System.out.println(name + ": Simulation already running...");
		return false;
	}

	private void run()
	{
		if (this.simulation == null)
		{
			this.simulation = new Simulation(this.graph);
			this.simulation.addObserver(this);
		}
		this.simulationThread = new Thread(this.simulation);
		this.simulationThread.start();
		this.enableRunAndEdit(false);
	}

	private void enableRunAndEdit(boolean b)
	{
		this.stop.setEnabled(!b);
		this.run.setEnabled(b);
		this.runChoose.setEnabled(b);
		this.runSettings.setEnabled(b);
		this.imp.setEnabled(b);
		this.randomizeGraph.setEnabled(b);
		this.randomizeNodes.setEnabled(b);
		this.randomizeEdges.setEnabled(b);
		this.setDefaultGraphValues.setEnabled(b);
		this.loadGraphValues.setEnabled(b);
		this.setDefaultNodeValues.setEnabled(b);
		this.loadNodeValues.setEnabled(b);
		this.setDefaultEdgeValues.setEnabled(b);
		this.loadEdgeValues.setEnabled(b);
		this.setStubborness.setEnabled(b);
		this.loadStubborness.setEnabled(b);
		this.empty.setEnabled(b);
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		// TODO Auto-generated method stub
		if (arg1 instanceof String)
		{
			String s = (String) arg1;
			if ("stopped".compareTo(s) == 0)
			{
				this.enableRunAndEdit(true);
				this.statusPanel.setSimulationData(this.simulation.getSimulationData());
			}
			else
			{
				this.statusPanel.setMessage(s);
			}
		}
	}

}
