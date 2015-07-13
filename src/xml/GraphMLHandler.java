/**
 * 
 */
package xml;

import graph.GraphEdge;
import graph.GraphEdgeLabel;
import graph.GraphLabel;
import graph.GraphNode;
import graph.GraphGeometry;

import java.util.ArrayDeque;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class GraphMLHandler extends DefaultHandler
{

	private ArrayDeque<String>					qNames			= new ArrayDeque<String>();

	// nodes
	private HashMap<String, GraphNode>	nodeList		= null;
	private GraphNode										actNode			= null;
	private int													nodeCounter	= 0;
	private GraphGeometry								geometry		= null;
	private GraphLabel									nLabel			= null;

	// edges
	private GraphEdge										actEdge			= null;
	private GraphEdgeLabel							eLabel			= null;

	// keys
	private HashMap<String, GraphMLKey>	keys				= new HashMap<String, GraphMLKey>();
	private GraphMLKey									actKey			= null;

	// string handling
	private StringBuilder								tmp					= new StringBuilder();

	public GraphMLHandler(HashMap<String, GraphNode> newNodeList, HashMap<String, GraphMLKey> newKeys)
	{
		this.nodeList = newNodeList;
		this.keys = newKeys;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{

		this.qNames.push(qName);

		// keys
		if (qName.equals("key"))
		{

			GraphMLKey key = new GraphMLKey();
			key.elementType = attributes.getValue("for");
			if ((null != key.elementType) && !(key.elementType.equals("node") || key.elementType.equals("edge")))
			{
				key.elementType = null;
			}
			key.id = attributes.getValue("id");
			key.name = attributes.getValue("attr.name");
			key.valueType = attributes.getValue("attr.type");
			if ((null != key.valueType) && (key.valueType.equals("int") || key.valueType.equals("double")))
			{
				key.valueType = "number";
			}
			else if ((null != key.valueType) && (!key.valueType.equals("string")))
			{
				key.valueType = null;
			}
			if (key.usable())
			{
				this.keys.put(key.id, key);
			}
		}

		// nodes
		if (qName.equals("node"))
		{
			this.actNode = new GraphNode(attributes.getValue("id"), this.nodeCounter++);
		}
		if (qName.equals("y:Geometry"))
		{
			this.geometry = new GraphGeometry();
			this.geometry.setHeight(Float.parseFloat(attributes.getValue("height")));
			this.geometry.setWidth(Float.parseFloat(attributes.getValue("width")));
			this.geometry.setX(Float.parseFloat(attributes.getValue("x")));
			this.geometry.setY(Float.parseFloat(attributes.getValue("y")));
		}
		if (qName.equals("y:NodeLabel"))
		{
			this.nLabel = new GraphLabel();
			parseLabel(this.nLabel, attributes);
		}

		// edges
		if (qName.equals("edge"))
		{
			this.actEdge = new GraphEdge(attributes.getValue("id"));
			this.actEdge.setSourceID(attributes.getValue("source"));
			this.actEdge.setTargetID(attributes.getValue("target"));
		}
		if (qName.equals("y:EdgeLabel"))
		{
			this.eLabel = new GraphEdgeLabel();
			parseLabel(this.eLabel, attributes);
			this.eLabel.distance = Float.parseFloat(attributes.getValue("distance"));
		}

		// values
		if (qName.equals("data"))
		{
			if ((null != attributes.getValue("key")) && (this.keys.containsKey(attributes.getValue("key"))))
			{
				this.actKey = new GraphMLKey(this.keys.get(attributes.getValue("key")));
			}
		}
	}

	private static void parseLabel(GraphLabel label, Attributes attributes)
	{
		GraphGeometry lgeometry = new GraphGeometry();
		lgeometry.setHeight(Float.parseFloat(attributes.getValue("height")));
		lgeometry.setWidth(Float.parseFloat(attributes.getValue("width")));
		lgeometry.setX(Float.parseFloat(attributes.getValue("x")));
		lgeometry.setY(Float.parseFloat(attributes.getValue("y")));
		label.setGeometry(lgeometry);
		label.setFontFamily(attributes.getValue("fontFamily"));
		if (attributes.getValue("fontStyle").equals("plain"))
		{
			label.setFontStyle(0);
		}
		else if (attributes.getValue("fontStyle").equals("bold"))
		{
			label.setFontStyle(1);
		}
		else if (attributes.getValue("fontStyle").equals("italic"))
		{
			label.setFontStyle(2);
		}
		label.setFontSize(Integer.parseInt(attributes.getValue("fontSize")));
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException
	{
		if (this.qNames.peek().equals("y:NodeLabel") && (this.nLabel != null))
		{
			this.nLabel.setText(new String(ch, start, length));
		}
		if (this.qNames.peek().equals("y:EdgeLabel") && (this.eLabel != null))
		{
			this.eLabel.setText(new String(ch, start, length));
		}
		if (null != this.actKey)
		{
			if (this.actKey.valueType.equals("number"))
			{
				this.actKey.value = Float.parseFloat(new String(ch, start, length));
			}
			if ((this.actKey.valueType.equals("string")))
			{
				this.tmp.append(new String(ch, start, length));
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		// nodes
		if (qName.equals("node") && (this.actNode != null) && this.actNode.check())
		{
			this.nodeList.put(this.actNode.getId(), this.actNode);
			this.actNode = null;
		}
		if (qName.equals("y:Geometry") && (this.geometry != null) && this.geometry.check())
		{
			this.actNode.setGeometry(this.geometry);
			this.geometry = null;
		}
		if (qName.equals("y:NodeLabel") && (this.nLabel != null) && this.nLabel.check())
		{
			this.actNode.setLabel(this.nLabel);
			this.nLabel = null;
		}

		// edges
		if (qName.equals("edge") && (this.actEdge != null) && this.actEdge.check())
		{
			if (this.nodeList.containsKey(this.actEdge.getTargetID()))
			{
				this.nodeList.get(this.actEdge.getTargetID()).addEdge(this.actEdge);
			}
			else
			{
				System.out.println("Cannot find target node " + this.actEdge.getTargetID() + " for edge: " + this.actEdge.getId());
			}
			this.actEdge = null;
		}
		if (qName.equals("y:EdgeLabel") && (this.eLabel != null) && this.eLabel.check())
		{
			this.actEdge.setLabel(this.eLabel);
			this.eLabel = null;
		}

		// values
		if (qName.equals("data"))
		{
			if (null != this.actKey)
			{
				if (this.actKey.valueType.equals("string"))
				{
					this.actKey.stringValue = StringHelper.unescapeHTML(this.tmp.toString());
					this.tmp = new StringBuilder();
				}
				if ((null != this.actNode) && (this.actKey.elementType.equals("node")))
				{
					this.actNode.addKey(this.actKey);
				}
				if ((null != this.actEdge) && (this.actKey.elementType.equals("edge")))
				{
					this.actEdge.addKey(this.actKey);
				}
			}
			this.actKey = null;
		}

		this.qNames.pop();
	}

}
