package procAudio;

import processing.core.*;


public class Button
{
  int x, y;
  int weight;
  int height;
  public boolean pressed = false;   
 
  
  Button(int ix, int iy, int iweight, int iheight) {
    x = ix;
    y = iy;
    weight = iweight;
    height = iheight;
  }
  
  void draw() {
	//rect(x, y, size, size);
  }

  boolean over(int mouseX, int mouseY)
  {
	if (mouseX > x && mouseX < x+weight && mouseY > y && mouseY < y+height) 
		return true;
	else
		return false;
  }

}
