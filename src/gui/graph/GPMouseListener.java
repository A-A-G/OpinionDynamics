/**
 * 
 */
package gui.graph;


import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.event.MouseInputAdapter;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class GPMouseListener extends MouseInputAdapter
{

	private final GraphPanel	graphPanel;

	private Rectangle					currentRect				= null;
	private Rectangle					rectToDraw				= null;
	private Rectangle					previousRectDrawn	= new Rectangle();

	/**
	 * 
	 */
	public GPMouseListener(GraphPanel newGraphPanel)
	{
		this.graphPanel = newGraphPanel;
	}
	
	public void drawRect(Graphics2D g2D)
	{
		if (this.currentRect != null)
		{
			g2D.drawRect(this.rectToDraw.x, this.rectToDraw.y, this.rectToDraw.width - 1, this.rectToDraw.height - 1);
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		this.currentRect = new Rectangle(x, y, 0, 0);
		updateDrawableRect(this.graphPanel.getWidth(), this.graphPanel.getHeight());
		this.graphPanel.repaint(this.rectToDraw.x, this.rectToDraw.y, this.rectToDraw.width, this.rectToDraw.height);
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		updateSize(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		updateSize(e);
		this.currentRect = null;
		if ((this.rectToDraw.width > 10) && (this.rectToDraw.height > 10))
		{
			if (this.graphPanel.getOldSize() == null)
			{
				this.graphPanel.setOldSize(new Dimension(this.graphPanel.getWidth(), this.graphPanel.getHeight()));
			}
			this.graphPanel.zoomAndMove(Math.min(this.graphPanel.getOldSize().getWidth() / this.rectToDraw.width, this.graphPanel.getOldSize().getHeight() / this.rectToDraw.height), new Point(this.rectToDraw.x, this.rectToDraw.y));
		}
		this.rectToDraw = null;
		this.previousRectDrawn = new Rectangle();
	}

	/*
	 * Update the size of the current rectangle and call repaint. Because currentRect always has the same origin, translate it if the width or height is negative.
	 * 
	 * For efficiency (though that isn't an issue for this program), specify the painting region using arguments to the repaint() call.
	 */
	void updateSize(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		this.currentRect.setSize(x - this.currentRect.x, y - this.currentRect.y);
		updateDrawableRect(this.graphPanel.getWidth(), this.graphPanel.getHeight());
		Rectangle totalRepaint = this.rectToDraw.union(this.previousRectDrawn);
		this.graphPanel.repaint(totalRepaint.x, totalRepaint.y, totalRepaint.width, totalRepaint.height);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		updatePreferredSize(e.getWheelRotation(), e.getPoint());
	}
	
	private void updatePreferredSize(int n, Point p)
	{
		double d = n * 1.08;
		d = (n > 0) ? 1 / d : -d;
		this.graphPanel.zoomAtP(d, p);
	}

	private void updateDrawableRect(int compWidth, int compHeight)
	{
		int x = this.currentRect.x;
		int y = this.currentRect.y;
		int width = this.currentRect.width;
		int height = this.currentRect.height;
		// Make the width and height positive, if necessary.
		if (width < 0)
		{
			width = 0 - width;
			x = x - width + 1;
			if (x < 0)
			{
				width += x;
				x = 0;
			}
		}
		if (height < 0)
		{
			height = 0 - height;
			y = y - height + 1;
			if (y < 0)
			{
				height += y;
				y = 0;
			}
		}
		// The rectangle shouldn't extend past the drawing area.
		if ((x + width) > compWidth)
		{
			width = compWidth - x;
		}
		if ((y + height) > compHeight)
		{
			height = compHeight - y;
		}
		// Update rectToDraw after saving old value.
		if (this.rectToDraw != null)
		{
			this.previousRectDrawn.setBounds(this.rectToDraw.x, this.rectToDraw.y, this.rectToDraw.width, this.rectToDraw.height);
			this.rectToDraw.setBounds(x, y, width, height);
		} else
		{
			this.rectToDraw = new Rectangle(x, y, width, height);
		}
	}

}
