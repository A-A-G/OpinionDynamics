/**
 * 
 */
package xml;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class GraphMLKey
{
	public String	name				= null;
	public String	id					= null;
	public String	valueType		= null;
	public String	elementType	= null;
	public String	stringValue	= "";
	public double	value				= 0;
	public double	minValue		= -1;
	public double	maxValue		= 1;

	public GraphMLKey()
	{
	}

	public GraphMLKey(GraphMLKey otherKey)
	{
		this.name = otherKey.name;
		this.id = otherKey.id;
		this.valueType = otherKey.valueType;
		this.elementType = otherKey.elementType;
		this.stringValue = otherKey.stringValue;
		this.value = otherKey.value;
		this.minValue = otherKey.minValue;
		this.maxValue = otherKey.maxValue;
	}

	public boolean usable()
	{
		if ((null == this.name) || (null == this.id) || (null == this.valueType) || (null == this.elementType))
		{
			return false;
		}
		if (this.valueType.equals("string") || this.valueType.equals("number"))
		{
			return true;
		}
		return false;
	}

	public double getValueOffset()
	{
		return (this.maxValue + this.minValue) / 2.0;
	}

	public double getValueFactor()
	{
		return 2.0 / (this.maxValue - this.minValue);
	}

	@Override
	public String toString()
	{
		return this.name;
	}

}
