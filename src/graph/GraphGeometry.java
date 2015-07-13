/**
 * 
 */
package graph;

import utils.Vector2D;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class GraphGeometry
{

	private double height = 0;
	private double width = 0;
	private double x = -1;
	private double y = -1;

	public boolean check()
	{
		if (this.height == 0)
		{
			System.out.println("GraphGeometryLabel check error: height = 0");
			return false;
		}
		if (this.width == 0)
		{
			System.out.println("GraphGeometryLabel check error: width = 0");
			return false;
		}
		if (this.x == -1)
		{
			System.out.println("GraphGeometryLabel check error: x = -1");
			return false;
		}
		if (this.y == -1)
		{
			System.out.println("GraphGeometryLabel check error: y = -1");
			return false;
		}
		return true;
	}

	public Vector2D getCenter()
	{
		return new Vector2D(this.x + this.width / 2, this.y + this.height / 2);
	}
	
	public Vector2D getTopLeft()
	{
		return new Vector2D(this.x, this.y);
	}

	/**
	 * @return the height
	 */
	public double getHeight()
	{
		return this.height;
	}

	/**
	 * @param newHeight the height to set
	 */
	public void setHeight(double newHeight)
	{
		this.height = newHeight;
	}

	/**
	 * @return the width
	 */
	public double getWidth()
	{
		return this.width;
	}

	/**
	 * @param newWidth the width to set
	 */
	public void setWidth(double newWidth)
	{
		this.width = newWidth;
	}

	/**
	 * @return the x
	 */
	public double getX()
	{
		return this.x;
	}

	/**
	 * @param newX the x to set
	 */
	public void setX(double newX)
	{
		this.x = newX;
	}

	/**
	 * @return the y
	 */
	public double getY()
	{
		return this.y;
	}

	/**
	 * @param newY the y to set
	 */
	public void setY(double newY)
	{
		this.y = newY;
	}

}
