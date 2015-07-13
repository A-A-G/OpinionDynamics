/**
 * 
 */
package startup;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class Applet extends JApplet
{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	public Applet()
	{

	}

	@Override
	public void init()
	{
		// Execute a job on the event-dispatching thread:
		// creating this applet's GUI.
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				@SuppressWarnings("unused")
				@Override
				public void run()
				{
					new Main();
				}
			});
		} catch (Exception e)
		{
			System.err.println("GUI didn't successfully complete");
		}
	}

}
