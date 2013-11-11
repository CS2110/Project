package edu.virginia.cs2110.framework;

	import edu.virginia.cs2110.framework.Graphics.ImageFormat;

	public interface Image {
	    public int getWidth();
	    public int getHeight();
	    public ImageFormat getFormat();
	    public void dispose();
	}
