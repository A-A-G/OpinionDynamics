package startup;

import javax.swing.SwingUtilities;

/**
 * 
 */

/**
 * @author Alexander Artiga Gonzalez
 *
 */
public class Standalone
{
	
	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
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
