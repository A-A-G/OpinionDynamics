/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.Set;

import xml.GraphMLKey;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class GraphElement
{

	protected double											defaultValue	= 0;
	protected double											value					= 0;
	protected String											id						= "";
	protected String											description		= "";
	protected HashMap<String, GraphMLKey>	values				= new HashMap<String, GraphMLKey>();
	
	public GraphElement(String newId)
	{
		this.id = newId;
	}

	public double getValue()
	{
		return this.value;
	}

	public void setValue(double newValue)
	{
		if (newValue > 1)
		{
			this.value = 1;
			System.out.println("GraphElement.setValue Error: " + newValue + " > 1");
		}
		else if (newValue < -1)
		{
			this.value = -1;
			System.out.println("GraphElement.setValue Error: " + newValue + " < -1");
		}
		else
		{
			this.value = newValue;
		}
	}

	public void updateDefaultValue()
	{
		this.defaultValue = this.value;
	}

	public void resetValue()
	{
		this.value = this.defaultValue;
	}

	/**
	 * @return id
	 */
	public String getId()
	{
		return this.id;
	}

	public void addKey(GraphMLKey key)
	{
		this.values.put(key.id, key);
	}

	public void newKeySelected(GraphMLKey key)
	{
		if (null != key)
		{
			if (this.values.containsKey(key.id))
			{
				if (key.valueType.equals("number"))
				{
					setValue((this.values.get(key.id).value - key.getValueOffset()) * key.getValueFactor());
				}
				else if (key.valueType.equals("string"))
				{
					this.description = this.values.get(key.id).stringValue;
				}
			}
		}
	}
	
	public Set<String> getKeys()
	{
		return this.values.keySet();
	}

	public String getDescription()
	{
		return this.description;
	}

	@Override
	public String toString()
	{
		if (this.description.equals(""))
		{
			return this.id;
		}
		return this.description;
	}

}
