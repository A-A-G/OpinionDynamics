package xml;

import graph.GraphNode;

import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class GraphMLReader
{

	public static void readGraphFromXML(String fileName, HashMap<String, GraphNode> nodeList, HashMap<String, GraphMLKey>	keys) throws Exception
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		GraphMLHandler handler = new GraphMLHandler(nodeList, keys);
		saxParser.parse(fileName, handler);
	}
	
	public static void readKeysFromXML(String fileName, HashMap<String, GraphMLKey> nodeKeys, HashMap<String, GraphMLKey> edgeKeys) throws Exception
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		GraphMLKeyHandler handler = new GraphMLKeyHandler(nodeKeys, edgeKeys);
		saxParser.parse(fileName, handler);
	}	
	
}
