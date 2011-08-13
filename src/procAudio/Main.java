package procAudio;
import processing.core.*;
//import processing.*;
import ddf.minim.*;
import ddf.minim.signals.*;
import ddf.minim.effects.*;
//import java.lang.*;
//import java.util.Arrays;



public class Main extends PApplet{
	Minim minim;
	AudioOutput out;
	SquareWave square0[]= new SquareWave[24];
	SawWave saw0[]= new SawWave[24];
	SineWave sine0[]= new SineWave[24];
	SquareWave square1[]= new SquareWave[24];
	SawWave saw1[]= new SawWave[24];
	SineWave sine1[]= new SineWave[24];
	SquareWave square2[]= new SquareWave[24];
	SawWave saw2[]= new SawWave[24];
	SineWave sine2[]= new SineWave[24];
	LowPassSP   lowpass;
	HighPassSP  highpass;
	
	
	int nBianchi=14,nNeri=10;   //numero tasti
	int[] flag0 = new int[3];
	int[] flag1 = new int[3];
	int[] flag2 = new int[3];
	
	             //dichiarazione bottoni e array di bottoni
	
	Button keys[] = new Button[nBianchi];
	Button keysBlack[] = new Button[nNeri];
	int colorazione=-1;
	int[] flagkey= new int[24];
	int[]indBianchi = {0,2,4,5,7,9,11,12,14,16,17,19,21,23};
	//int[]indNeri = new int [nNeri];
	int[] indNeri = {1,3,6,8,10,13,15,18,20,22};
	
	char[] tastiera ={'z','s','x','d','c','v','g','b','h','n','j','m','r','5','t','6','y','u','8','i','9','o','0','p'};
	//float[] frequenze;
	float[] frequenze = new float[24];
	float[] vControlGlobal = {0.01f,0.01f,0.01f};
	double dee;

	Button bOsc[] = new Button[3];
	Button bSine[] = new Button [3];
	Button bSquare[] = new Button [3];
	Button bSaw[] = new Button [3];
	Knob kVolOsc[] = new Knob[3];
	Knob fHP,fLP;
	
	Button OctUp, OctDown, Oct0;
	Button bHP,bLP;
	float fLPGlobal=0.1f,fHPGlobal=0.0f;
	int flagLP=0,flagHP=0;
	
	
	boolean /*sineAct=false, sawAct=false, squareAct= false,*/ osc0Act=false, osc1Act=false, osc2Act=false;  //flag di pressione shape
	boolean[] sineAct = new boolean [3];
	boolean[] squareAct = new boolean [3];
	boolean[] sawAct = new boolean [3];
	int flagOct=0;
	int ascis;  //indice coordinate per disegno
	int ord=300;
	
	public void inizializzazioneboolean(){
	
		for(int i=0; i<3;i++){
		sineAct[i]=false;
		squareAct[i]=false;
		sawAct[i]=false;
		
		}
		
	}	
		
	public void inizializzazionetasti(){
	    float Do = (261.6f);
		frequenze[0]=Do;
		for (int i=0; i<24;i++)
		{
		    float j = (float) i;
			frequenze[i]=(float) ((Math.pow(2, j/12))*(Do));
			
		}	
	}
	
	
	
	
	
	public void setup()
	{
	  size(640, 400);
	  inizializzazionetasti();
	  minim = new Minim(this);
	 
	  
	  out = minim.getLineOut(Minim.STEREO, 512); //uscita audio stereo 
	
	  bOsc[0] = new Button(240, 170, 40, 10);      //bottoni di oscillatore
	  bOsc[1] = new Button(300, 170, 40, 10);
	  bOsc[2] = new Button(360, 170, 40, 10);
	  
	  bSine[0]= new Button(25, 25, 40, 20);        // bottoni forme d'onda
	  bSquare[0]= new Button(25, 55, 40, 20);
	  bSaw[0]= new Button(25, 85, 40, 20);
	  
	  bSine[1]= new Button(75, 25, 40, 20);
	  bSquare[1]= new Button(75, 55, 40, 20);
	  bSaw[1]= new Button(75, 85, 40, 20);
	  
	  bSine[2]= new Button(125, 25, 40, 20);
	  bSquare[2]= new Button(125, 55, 40, 20);
	  bSaw[2]= new Button(125, 85, 40, 20);
	  
	  
	  //bottoni filtri
	  bLP = new Button(450, 100, 40,10);
	  bHP = new Button(520, 100, 40,10);
	  
	  // manopole ft filtri
	  fLP = new Knob(460, 90, 20,10);
	  fHP = new Knob(530, 90, 20,10);
	  
	  
	  //manopole di volume di shape
	  kVolOsc[0] = new Knob(250, 160, 20, 10);
	  kVolOsc[1] = new Knob(310, 160, 20, 10);
	  kVolOsc[2] = new Knob(370, 160, 20, 10);
	  
	  // tasti ottava
	  OctDown = new Button(40, 220, 10, 25);
	  Oct0 = new Button(60, 220, 10, 25);
	  OctUp = new Button(80, 220, 10, 25);
	  
	  
	  
	  //  tasti bianchi
	  ascis=20;
	  for(int i=0;i<nBianchi;i++){
		  keys[i] = new Button(ascis,ord,40,80);
		  ascis=ascis+40;

		 
	  }

	  //  diesis+bemolle
	  ascis=40;
	  for(int i=0;i<nNeri;i++){
		  

		  keysBlack[i] = new Button(ascis,ord-41,40,40);
		  if (i==1 || i==4 || i==6 || i==11){
		  //if (i % 5 == 0 ){
		     ascis=ascis+80;
		     }
		  else
		     ascis=ascis+40;
	  }
      
	  
	
	//azzeramento flagkey
	for (int i=0;i<24;i++){
		flagkey[i]=0;}
	  	  
	  
	}  //fine setup
	
	public void passaBasso(/*float fTl*/){             //filtro passa basso
		lowpass = new LowPassSP(fLPGlobal, 44100);
		out.addEffect(lowpass);
	}
	
	public void RpassaBasso(){             
		out.removeEffect(lowpass);
	}
	
	public void passaAlto(/*float fTh*/){             //filtro passa basso
		highpass = new HighPassSP(fHPGlobal, 44100);
		out.addEffect(highpass);
	}
	
	
	public void squareWave0(float freq, int i, float vControlSquare) {    //funzioni forme d'onda con addsignal
		  square0[i] = new SquareWave(freq, vControlSquare, 44100);
		  out.addSignal(square0[i]);		  			
		}
	
	public void sineWave0(float freq,int i,float vControlSine) {
		sine0[i] = new SineWave(freq, vControlSine, 44100);
		out.addSignal(sine0[i]);
	}
	
	public void sawWave0(float freq,int i, float vControlSaw) {
		saw0[i] = new SawWave(freq, vControlSaw, 44100);
		out.addSignal(saw0[i]);
	}
	
	
	public void squareWave1(float freq, int i, float vControlSquare) {    //funzioni forme d'onda con addsignal
		  square1[i] = new SquareWave(freq, vControlSquare, 44100);
		  out.addSignal(square1[i]);		  			
		}
	
	public void sineWave1(float freq,int i,float vControlSine) {
		sine1[i] = new SineWave(freq, vControlSine, 44100);
		out.addSignal(sine1[i]);
	}
	
	public void sawWave1(float freq,int i, float vControlSaw) {
		saw1[i] = new SawWave(freq, vControlSaw, 44100);
		out.addSignal(saw1[i]);
	}
	
	
	public void squareWave2(float freq, int i, float vControlSquare) {    //funzioni forme d'onda con addsignal
		  square2[i] = new SquareWave(freq, vControlSquare, 44100);
		  out.addSignal(square2[i]);		  			
		}
	
	public void sineWave2(float freq,int i,float vControlSine) {
		sine2[i] = new SineWave(freq, vControlSine, 44100);
		out.addSignal(sine2[i]);
	}
	
	public void sawWave2(float freq,int i, float vControlSaw) {
		saw2[i] = new SawWave(freq, vControlSaw, 44100);
		out.addSignal(saw2[i]);
	}
	
	public void mousePressed(){
	
	  //ottave
	  if (Oct0.over(mouseX, mouseY)) 
		  flagOct=0;  
	  if (OctUp.over(mouseX, mouseY)) 
		  flagOct=1;
	  if (OctDown.over(mouseX, mouseY)) 
		  flagOct=-1;
	   
	  // filtri
	  if(bLP.over(mouseX, mouseY)){

		  bLP.pressed = !(bLP.pressed);
		  if(flagLP==1){
		  flagLP=0;
		  RpassaBasso();}

	  }
	  
	  if(bHP.over(mouseX, mouseY))
		  if(bHP.over(mouseX, mouseY)){

			  bHP.pressed = !(bHP.pressed);
			  if(flagHP==1){
			  flagHP=0;
			  RpassaBasso();}

		  }
	  
	  if(fLP.over(mouseX, mouseY))
		  clickedKnob(fLP);
	  
	  if(fHP.over(mouseX, mouseY))
		  clickedKnob(fHP);
	  
	  
	  
	  if (bOsc[0].over(mouseX, mouseY)) {  //attivazione osc
		  osc0Act = !( osc0Act );  
	      clicked(bOsc[0], bOsc[1], bOsc[2]);
	  }
	  if (bOsc[2].over(mouseX, mouseY)) {
		  osc2Act = !( osc2Act );    
	      clicked(bOsc[2], bOsc[1], bOsc[0]);
	  }   
	  if (bOsc[1].over(mouseX, mouseY)) {
		  osc1Act = !( osc1Act );    
	      clicked(bOsc[1], bOsc[0], bOsc[2]); 
	  }   
	  
	  for(int i=0; i<3;i++){
	  if (bSine[i].over(mouseX, mouseY)) {  //attivazione forma d'onda
		  sineAct[i] = !( sineAct[i] );  
	   
		  if(squareAct[i])
			  squareAct[i] = !( squareAct[i] );  
		  
		  if(sawAct[i])
			  sawAct[i] = !( sawAct[i] );  
		  
		  clicked(bSine[i],bSquare[i],bSaw[i]);
			  
	  }
	  if (bSquare[i].over(mouseX, mouseY)) {  
		  squareAct[i] = !( squareAct[i] );  
			   
		  if(sineAct[i])
			  sineAct[i] = !( sineAct[i] );  
				  
		  if(sawAct[i])
			  sawAct[i] = !( sawAct[i] );  
				  
		  clicked(bSquare[i],bSine[i],bSaw[i]);
				  
	  }  
	  if (bSaw[i].over(mouseX, mouseY)) {  

		  sawAct[i] = !( sawAct[i] );  
			   
		  if(sineAct[i])
			  sineAct[i] = !( sineAct[i] );  
				  
		  if(squareAct[i])
			  squareAct[i] = !( squareAct[i] );  
				  
		 clicked(bSaw[i], bSine[i], bSaw[i]);
					  
			  }
	}
	  
	  if (kVolOsc[0].over(mouseX, mouseY)) {
		      
	      clickedKnob(kVolOsc[0]);
	  }
	  
	  if (kVolOsc[2].over(mouseX, mouseY)) {
	      
	      clickedKnob(kVolOsc[2]);
	  }
	  
	  if (kVolOsc[1].over(mouseX, mouseY)) {
	      
	      clickedKnob(kVolOsc[1]);
	  }
	  
	}
	
	
	public void mouseReleased(){
		  for (int i=0; i < 3; i++) {
			  Knob knob = kVolOsc[i];
			    if (knob.pressed)
			    clickedKnob(knob);
		  }
		  
		  if (fLP.pressed)
			  clickedKnob(fLP);
		  if (fHP.pressed)
			  clickedKnob(fHP);
	}
	
	
	public void mouseDragged() {
		for (int i=0; i < 3; i++){ 
			Knob knob = kVolOsc[i];
			if(knob.pressed)
			{    float[] vControl= {0.0f,0.0f,0.0f};
				//if (knob.over(mouseX, mouseY)) {
					 
					  vControl[i] =(float) mouseY;
					  float Vtemp = (float) ((vControl[i]-160)/-150);
					  if (Vtemp>0.0f && Vtemp<0.5f)
						 vControlGlobal[i]=Vtemp;
				       //  System.out.println("vonctrolglobal "+vControlGlobal[i] + " i(onda)" + i);

				 // }
			}
	    }
		
		
		//filtri 
		float fControlLP= 15000.0f;
		if (fLP.over(mouseX, mouseY)) {			 
			fControlLP=(float) mouseY;
			float fTempL = (float) (fControlLP);
	//		if (fTempL)
			fLPGlobal=(float) (Math.pow(2, (fTempL-30)/5));
	//		if (fTempL>400)
		//		fLPGlobal=1200.0f;
			System.out.println("ftagliobasso "+fLPGlobal);
		 }
		
		float fControlHP= 0.0f;
		if (fHP.over(mouseX, mouseY)) {			 
			fControlHP=(float) mouseY;
			float fTempL = (float) (fControlHP);
	//		if (fTempL)
			fHPGlobal=(float) (Math.pow(2, (fTempL-30)/5));
	//		if (fTempL>400)
		//		fLPGlobal=1200.0f;
			System.out.println("ftaglioalto "+fHPGlobal);
		 }
	
	    
		 
		}
	
	public void clicked(Button button, Button button1, Button button2) {
		if (button.pressed==false){
		    button.pressed = !(button.pressed);
		    if (button1.pressed)
		    button1.pressed = !(button1.pressed);
		    if (button2.pressed)
		    button2.pressed = !(button2.pressed);
		}
		else
		    button.pressed = !(button.pressed);
			  
	}
	
	//metodo clicked che nega la variabile pressed della classe knob (sopra per button)
	public void clickedKnob(Knob knob) {
		
		
		
		knob.pressed = !(knob.pressed);
			  
	}
	
	public void keyPressed()    //pressione tasti per suonare
	{
	 // System.out.println("pressed " + key);
	  
	 
	  for (int i=0;i<24;i++) {
	  
	      if ( key ==  tastiera[i]  )
	      {
	        
	      if (flagkey[i] != 1) {
	//	      System.out.println("premuto " + key); 
		      System.out.println(" stato premuto il tasto la cui frequenza  "+ frequenze[i] + " Hz");
	          flagkey[i] = 1;
	          colorazione = i;
	          
	          if(bLP.pressed && flagLP==0)
	    	  	  passaBasso();
	          flagLP=1;
	          if(bHP.pressed && flagHP==0)
	    	  	  passaAlto();
	          flagHP=1;
	      //    float freq = 440;
	     //     if (osc0Act){
	          //trasposizione eventuale di ottava della cella 
	          if (flagOct==1)
	        	  frequenze[i]=frequenze[i]*2;
	          if (flagOct==-1)
	        	  frequenze[i]=frequenze[i]/2;
		          if (squareAct[0])
			         squareWave0(frequenze[i],i,vControlGlobal[0]);
		          if (sineAct[0])
			         sineWave0(frequenze[i],i,vControlGlobal[0]);
		          if (sawAct[0])
			         sawWave0(frequenze[i],i,vControlGlobal[0]);
	    //      }
		//	  if (osc1Act){
				  if (squareAct[1])
					 squareWave1(frequenze[i],i,vControlGlobal[1]);
				  if (sineAct[1])
					 sineWave1(frequenze[i],i,vControlGlobal[1]);
				  if (sawAct[1])
					 sawWave1(frequenze[i],i,vControlGlobal[1]);
		//	  }
		//	  if (osc2Act){
				  if (squareAct[2])
						 squareWave2(frequenze[i],i,vControlGlobal[2]);
				  if (sineAct[2])
						 sineWave2(frequenze[i],i,vControlGlobal[2]);
				  if (sawAct[2])
						 sawWave2(frequenze[i],i,vControlGlobal[2]);
				  
			

		    //rinormalizzazione della cella 
			if (flagOct==1)
		        	  frequenze[i]=frequenze[i]/2;
		    if (flagOct==-1)
		        	  frequenze[i]=frequenze[i]*2;
		//	  }
				         
	       }
	   
	      }
	  }
	  
	}
	
    public void keyReleased()
    {
     
   //  System.out.println("rilasciato " + key);
     for (int i=0;i<24;i++) {
     if ( key ==  tastiera[i]  )
	  {//if (osc0Act){
          if (squareAct[0])
        	  out.removeSignal(square0[i]);
	      if (sineAct[0])
	    	  out.removeSignal(sine0[i]);
	      if (sawAct[0])
	          out.removeSignal(saw0[i]);
     //  }
	//	  if (osc1Act){
			  if (squareAct[1])
	        	  out.removeSignal(square1[i]);
		      if (sineAct[1])
		    	  out.removeSignal(sine1[i]);
		      if (sawAct[1])
		          out.removeSignal(saw1[i]);
	//	  }
	//	  if (osc2Act){
			  if (squareAct[2])
	        	  out.removeSignal(square2[i]);
		      if (sineAct[2])
		    	  out.removeSignal(sine2[i]);
		      if (sawAct[2])
		          out.removeSignal(saw2[i]);

		      
		    
	//	  }
	   
	   flagkey[i] = 0;
	   colorazione= -1;
	  }
    }
    }
	
    public void drawButton(Button button) { //funzione disegnamento bottoni shape

		rect(button.x, button.y, button.weight, button.height);
    	
    }
    
    public void drawKnob(Knob knob) {
    	
    	rect(knob.x, knob.y, knob.width, knob.height);
    }
	
	
	public void draw()  //funzione disegnamento generale
	{   stroke(0);
		fill(0);
		rect(240,20,160,70);
		for(int i = 0; i<out.bufferSize()-1;i++){
			float x1 = map(i,0, out.bufferSize(),230,70);
			float x2 = map(i+1, 0, out.bufferSize(),230,70);
			
			stroke(255);
			if(((50+out.left.get(i)*50)>20) && ((50+out.left.get(i)*50)<89)&&((50+out.left.get(i+1)*50)>20) && ((50+out.left.get(i+1)*50)<89))
			line(x1+170,50+out.left.get(i)*50,x2+170,50+out.left.get(i+1)*50);
			
		}
//	    fill(123);
		smooth();
	    stroke(0);
	    
	    fill(255);
	    
	    drawButton(bSine[0]);
	    drawButton(bSquare[0]);
	    drawButton(bSaw[0]);
	    drawButton(bSine[1]);
	    drawButton(bSquare[1]);
	    drawButton(bSaw[1]);
	    drawButton(bSine[2]);
	    drawButton(bSquare[2]);
	    drawButton(bSaw[2]);
	    drawButton(bOsc[0]);
	    drawButton(bOsc[1]);
	    drawButton(bOsc[2]);
	    
	    drawButton(OctDown);
	    drawButton(Oct0);
	    drawButton(OctUp);
	    drawButton(bLP);
	    drawButton(bHP);
	    fill(0);
	    text("sine",30,40);
	    text("squar",27,70);
	    text("saw",30,100);
	    text("sine",80,40);
	    text("squar",77,70);
	    text("saw",80,100);
	    text("sine",130,40);
	    text("squar",127,70);
	    text("saw",130,100);
	    text("osc1",245,200);
	    text("osc2",305,200);
	    text("osc3",365,200);
	    text("LPF",460,130);
	    text("HPF",525,130);
	    text("octave",45,210);
	   // drawKnob(fHP);
	  //  drawKnob(fLP);
	    fill(0);
	    rect(250, 100, 20, 60);
	    rect(460, 30, 20,60);
	    rect(530,30,20,60);
	    drawKnob(kVolOsc[0]);
	    drawKnob(kVolOsc[1]);
	    drawKnob(fLP);
	    drawKnob(fHP);
	    fill(0);
	    rect (310, 100, 20, 60);
	    drawKnob(kVolOsc[2]);
	    fill(0);
	    rect (370, 100, 20, 60);
	    
	    
	    
	    //draw tasti ottava
	    if  (flagOct==0){
	    	fill(120);
	    	drawButton(Oct0);
	    }
	    if  (flagOct==1){
	    	fill(120);
	    	drawButton(OctUp);
	    }
	    if  (flagOct==-1){
	    	fill(120);
	    	drawButton(OctDown);
	    }
	    
	    // draw osc buttons (chiedere)
		  for (int i=0; i < 3; i++) {
			  Button button = bOsc[i];
			    if (button.pressed)
					fill(150);
				else
					fill(255);
			    drawButton(button);
		  }
		  
		
		  
	//nuovo nuovo shape buttons
		  for(int i=0;i<3 ;i++){
			  if (sineAct[i]){
				  fill(150);
				  drawButton(bSine[i]);
			  }
			  if  (sawAct[i]){
				  fill(150);
				  drawButton(bSaw[i]);
			  }
			  if (squareAct[i]){
				  fill(150);
				  drawButton(bSquare[i]);
			  }
		  }
		  // draw knobs volumi
		  for (int i=0; i < 3; i++) {
			  Knob knob = kVolOsc[i];
			    if (knob.pressed && (160>mouseY) && (mouseY>100))
					knob.y=mouseY;
				else
					fill(123);
			    drawKnob(knob);
		  }
		  
		  // draw knobs filtri
		  if (fLP.pressed && (  90>mouseY)&& (mouseY>30  ))
			  fLP.y=mouseY;
		  else
			  fill(123);
		  drawKnob(fLP);
		  
		  if (fHP.pressed && (  90>mouseY)&& (mouseY>30  ))
			  fHP.y=mouseY;
		  else
			  fill(123);
		  drawKnob(fHP);
		  
		  //filtri
		  if(bLP.pressed){
			  fill(123);
			  drawButton(bLP);}
		  if(bLP.pressed==false){
			  fill(250);
		  	  drawButton(bLP);}
		  
		  if(bHP.pressed){
			  fill(123);
			  drawButton(bHP);}
		  if(bHP.pressed==false){
			  fill(250);
		  	  drawButton(bHP);}
		  
	 
		// tasti
		int flagB=0;
		for(int i=0;i<24;i++){
			  for (int j=0;j<nBianchi;j++){
				  if (flagkey[i]==1 && (i == indBianchi[j])){ 	
					  Button key= keys[j];
					//  System.out.println("indbianchij"+indBianchi[j]);
					  flagB=1;
					  fill(100);
					  stroke(0);
					  rect(key.x, key.y, key.weight, key.height);
				  }
				  if(flagB==0) {
					  if(i<14){
					  Button key= keys[i];
					  fill(255);
					  stroke(0);
					  rect(key.x, key.y, key.weight, key.height);
					  }
				  }              				  				  
			  }
		}
		int flagN=0;
		for(int i=0;i<24;i++){
		  for (int j=0;j<nNeri;j++){
			  if (flagkey[i]==1 && (i == indNeri[j])){ 	
				  Button key= keysBlack[j];
			//	  System.out.println("indnerij"+indNeri[j]);
				  flagN=1;
				  fill(80);
				  stroke(255);
				  rect(key.x, key.y, key.weight, key.height);
			  }
		  }
		  	  if(flagN==0){
		  	 // System.out.println("sono entrato anche nell'else");
			  	  if (i<10){
			  		  	Button key = keysBlack[i];
			  	  		fill(0);
			  	  		stroke(255);
			  	  		rect(key.x, key.y, key.weight, key.height);
			  	  }
		  	  }			              		  	  		  
		}
	}
	
	
	
	public void stop()
	{
	  out.close();
	  minim.stop();
	 
	  super.stop();
	}
	
	

}