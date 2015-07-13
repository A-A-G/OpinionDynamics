/**
 * 
 */
package utils;

import java.awt.Color;

/**
 * @author Alexander Artiga Gonzalez
 * 
 */
public class ColorHandler
{
	public static float GREY_OFFSET = 0.0F;
	
	public enum EColorSpace
	{
		RED_GREEN, RED_YELLOW, YELLOW_BLUE, GREY
	}

	public static Color getColor(float d, EColorSpace ecolorspace)
	{
		switch (ecolorspace)
		{
		case RED_GREEN:
		{
			return getRGColor(d);
		}
		case RED_YELLOW:
		{
			return getRYColor(d);
		}
		case YELLOW_BLUE:
		{
			return getYBColor(d);
		}
		case GREY:
		{
			return getGreyColor(d);
		}
		default:
		{
			return getRGColor(d);
		}
		}
	}

	public static Color getRGColor(float d)
	{
		if (Math.abs(d) <= 1)
		{
			if (d < 0)
			{
				return new Color(1, (1 + d), 0);
			}
			return new Color((1 - d), 1, 0);
		}
		return Color.black;
	}

	public static Color getRYColor(float d)
	{
		if (Math.abs(d) <= 1)
		{
			return new Color(1, (d + 1) / 2.0F, 0);
		}
		return Color.black;
	}

	public static Color getYBColor(float d)
	{
		if (Math.abs(d) <= 1)
		{
			return new Color((1 - d) / 2.0F, (1 - d) / 2.0F, (1 + d) / 2.0F);
		}
		return Color.black;
	}

	public static Color getGreyColor(float d)
	{
		if (Math.abs(d) <= 1)
		{
			float colorValue = (d + 1) * (0.5F - GREY_OFFSET) + GREY_OFFSET;
			return new Color(colorValue, colorValue, colorValue);
		}
		return Color.black;
	}

}
