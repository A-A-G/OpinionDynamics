/**
 * 
 */
package utils;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class Vector2D extends Point2D.Double
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public Vector2D(double newX, double newY)
	{
		this.x = newX;
		this.y = newY;
	}

	public Vector2D(Point2D p)
	{
		this.x = p.getX();
		this.y = p.getY();
	}

	public Vector2D(Point2D p1, Point2D p2)
	{
		this.x = p2.getX() - p1.getX();
		this.y = p2.getY() - p1.getY();
	}
	
	public Vector2D add(double scalar)
	{
		this.x = this.x + scalar;
		this.y = this.y + scalar;
		return this;
	}

	public static Vector2D add(Point2D p1, double scalar)
	{
		return new Vector2D(p1).add(scalar);
	}

	public Vector2D add(Point2D other)
	{
		this.x = this.x + other.getX();
		this.y = this.y + other.getY();
		return this;
	}

	public static Vector2D add(Point2D p1, Point2D p2)
	{
		return new Vector2D(p1).add(p2);
	}

	public Vector2D sub(Point2D other)
	{
		this.x = this.x - other.getX();
		this.y = this.y - other.getY();
		return this;
	}

	public static Vector2D sub(Point2D p1, Point2D p2)
	{
		return new Vector2D(p1).sub(p2);
	}

	public Vector2D mult(double scalar)
	{
		this.x = this.x * scalar;
		this.y = this.y * scalar;
		return this;
	}

	public static Vector2D mult(Point2D p, double scalar)
	{
		return (new Vector2D(p)).mult(scalar);
	}

	public double mult(Point2D p)
	{
		return this.x * p.getX() + this.y * p.getY();
	}

	public static double mult(Point2D p1, Point2D p2)
	{
		return (new Vector2D(p1).mult(p2));
	}

	public Vector2D div(double scalar)
	{
		this.x = this.x / scalar;
		this.y = this.y / scalar;
		return new Vector2D(this);
	}

	public static Vector2D div(Point2D p, double scalar)
	{
		return (new Vector2D(p)).div(scalar);
	}

	public void norm()
	{
		div(getNorm());
	}

	public double getNorm()
	{
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}

	public static double getNorm(Vector2D vector)
	{
		return vector.getNorm();
	}

	public static double getNorm(Point2D p)
	{
		return new Vector2D(p).getNorm();
	}

	public double getGradient()
	{
		return this.y / this.x;
	}

	public double getGradient(Point2D p)
	{
		return (p.getY() - this.y) / (p.getX() - this.x);
	}

	public static double getGradient(Point2D p1, Point2D p2)
	{
		return new Vector2D(p1).getGradient(p2);
	}

	public Vector2D getNormal()
	{
		return getNormal(getGradient());
	}

	public static Vector2D getNormal(double gradient)
	{
		Vector2D normal = new Vector2D(1, -1 / gradient);
		normal.norm();
		return normal;
	}
	
	public static Vector2D intersection(Vector2D p, double m, Vector2D M, double r)
	{
		double n = p.getY() - m * p.getX();
		double a = 1 + Math.pow(m, 2);
		double b = 2 * m * n - 2 * m * M.getY() - 2 * M.getX();
		double c = Math.pow(n, 2) + Math.pow(M.getX(), 2) + Math.pow(M.getY(), 2) - Math.pow(r, 2) - 2 * n * M.getY();
		if ((Math.pow(b, 2) - 4 * a * c) < 0)
		{
			return new Vector2D(p);
		}
		double x1 = (-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);
		double y1 = m * x1 + n;
		double x2 = (-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);
		double y2 = m * x2 + n;
		if (p.distance(x1, y1) < p.distance(x2, y2))
		{
			return new Vector2D(x1, y1);
		}
		return new Vector2D(x2,y2);
	}

	public Point toPoint()
	{
		return new Point((int) this.x, (int) this.y);
	}
}
