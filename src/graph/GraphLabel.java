/**
 * 
 */
package graph;

import utils.Vector2D;

/**
 * @author alex
 * 
 */
public class GraphLabel
{

	private int fontSize = 0;
	private String fontFamily = "";
	private int fontStyle = -1;
	private GraphGeometry geometry = null;
	private String text = "";

	public boolean check()
	{
		// font
		if (this.fontSize == 0)
		{
			System.out.println("GraphNodeLabel check error: fontSize = 0");
			return false;
		}
		if (this.fontFamily.equals(""))
		{
			System.out.println("GraphNodeLabel check error: fontFamily empty");
			return false;
		}
		if ((this.fontStyle < 0) || (this.fontStyle > 2))
		{
			System.out.println("GraphNodeLabel check error: fontStyle empty");
			return false;
		}
		if (this.geometry == null)
		{
			System.out.println("GraphNodeLabel check error: geometry not set");
			return false;
		} else if (!this.geometry.check())
		{
			return false;
		}
		if (this.text.equals(""))
		{
			//we have no Problem if the label is empty... perhaps later we can make user of the label position to display informations
			//System.out.println("GraphEdgeLabel check error: label text empty");
			// return false; //should this be possible?
		}
		return true;
	}

	public Vector2D getCenter()
	{
		return this.geometry.getCenter();
	}
	
	public Vector2D getTopLeft()
	{
		return this.geometry.getTopLeft();
	}

	/**
	 * @return the geometry
	 */
	public GraphGeometry getGeometry()
	{
		return this.geometry;
	}

	/**
	 * @param newGeometry
	 *            the geometry to set
	 */
	public void setGeometry(GraphGeometry newGeometry)
	{
		this.geometry = newGeometry;
	}

	/**
	 * @return the fontSize
	 */
	public int getFontSize()
	{
		return this.fontSize;
	}

	/**
	 * @param newFontSize the fontSize to set
	 */
	public void setFontSize(int newFontSize)
	{
		this.fontSize = newFontSize;
	}

	/**
	 * @return the fontFamily
	 */
	public String getFontFamily()
	{
		return this.fontFamily;
	}

	/**
	 * @param newFontFamily the fontFamily to set
	 */
	public void setFontFamily(String newFontFamily)
	{
		this.fontFamily = newFontFamily;
	}

	/**
	 * @return the fontStyle
	 */
	public int getFontStyle()
	{
		return this.fontStyle;
	}

	/**
	 * @param newFontStyle the fontStyle to set
	 */
	public void setFontStyle(int newFontStyle)
	{
		this.fontStyle = newFontStyle;
	}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return this.text;
	}

	/**
	 * @param newText the text to set
	 */
	public void setText(String newText)
	{
		this.text = newText;
	}
}
