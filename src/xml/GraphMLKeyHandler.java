/**
 * 
 */
package xml;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class GraphMLKeyHandler extends DefaultHandler
{

	private HashMap<String, GraphMLKey> nodeKeys = null;

	private HashMap<String, GraphMLKey> edgeKeys = null;

	public GraphMLKeyHandler(HashMap<String, GraphMLKey> newNodeKeys, HashMap<String, GraphMLKey> newEdgeKeys)
	{
		this.nodeKeys = newNodeKeys;
		this.edgeKeys = newEdgeKeys;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		if (qName.equals("key"))
		{
			if (attributes.getValue("for").equals("node"))
			{
				GraphMLKey key = new GraphMLKey();
				key.elementType = "node";
				key.id = attributes.getValue("id");
				key.name = attributes.getValue("attr.name");
				key.valueType = attributes.getValue("attr.type");
				this.nodeKeys.put(key.name, key);
			}
			if (attributes.getValue("for").equals("edge"))
			{
				GraphMLKey key = new GraphMLKey();
				key.elementType = "edge";
				key.id = attributes.getValue("id");
				key.name = attributes.getValue("attr.name");
				key.valueType = attributes.getValue("attr.type");
				this.edgeKeys.put(key.name, key);
			}
		}
	}
}
