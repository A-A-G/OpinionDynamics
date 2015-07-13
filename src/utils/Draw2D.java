/**
 * 
 */
package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

/**
 * @author Alexander Artiga Gonzalez
 *
 */
public class Draw2D
{
	
	public static void drawArrow(Graphics2D g2D, Vector2D endPoint, Vector2D gradient2D, Dimension arrowSize, double arrowEdge)
	{
		float lineWidth = ((BasicStroke) g2D.getStroke()).getLineWidth();
		g2D.setColor(Color.black);
		gradient2D.norm();
		endPoint.add(Vector2D.mult(gradient2D, lineWidth*0.5));
		Path2D arrow = new Path2D.Double();
		arrow.moveTo(endPoint.x, endPoint.y);
		Vector2D arrowStartPoint = Vector2D.sub(endPoint, Vector2D.mult(gradient2D, arrowEdge*arrowSize.getHeight()*lineWidth));
		Vector2D startPoint = Vector2D.sub(endPoint, gradient2D.mult(1*arrowSize.getHeight()*lineWidth));
		Vector2D normal = gradient2D.getNormal();
		normal.mult(arrowSize.getWidth()*lineWidth);
		Vector2D wing = Vector2D.add(startPoint, normal);
		arrow.lineTo(wing.x, wing.y);
		arrow.lineTo(arrowStartPoint.x,	arrowStartPoint.y);
		wing = Vector2D.sub(startPoint, normal);
		arrow.lineTo(wing.x, wing.y);
		arrow.lineTo(endPoint.x, endPoint.y);
		arrow.closePath();
		g2D.fill(arrow);
	}

}
