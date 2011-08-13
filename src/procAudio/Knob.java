package procAudio;

import processing.core.*;

public class Knob {
	  int x, y;
	  int width;
	  int height;
	  public boolean pressed = false;   
	 
	  
	  Knob(int ix, int iy, int iwidth, int iheight) {
	    x = ix;
	    y = iy;
	    width = iwidth;
	    height = iheight;
	  }
	  
	  

	  boolean over(int mouseX, int mouseY)
	  {
		if (mouseX > x && mouseX < x+width && mouseY > y && mouseY < y+height) 
			return true;
		else
			return false;
	  }

}
