// ELITIST APPROACH (Keep the Best into consideration, instead of just the neighbours)

package DFO;

import processing.core.PApplet;
import processing.core.PImage;

public class DFO extends PApplet {

	private static final long serialVersionUID = 1L;
	
	int imgNo = 1;
	
	Utils utils = new Utils();
	public void setup() 
	{
		frame.setTitle("Dispersive Flies Optimisation");
		frameRate(Global.speed);
		
		//Global.img = loadImage("input" + imgNo + ".bmp");
		Global.img = loadImage("Exp/" + imgNo + ".png");
		loadImageFile(Global.img);
		
		Global.imgW = Global.img.width;
		Global.imgH = Global.img.height;
		
		size(Global.imgW,Global.imgH);

		// Initialisation Phase
		for (int i = 0; i < Global.popSize; i++)
			Global.fly[i] = new Fly(utils.getRandPos());

		image(Global.img, 0,0);
		
		utils.findBestFly();
	}
	
	int goalX, goalY;
	public void draw() 
	{
		smooth();

		// remove the visuals of the previous iteration 
		fill(255, 255, 255, 150);
		rect(0, 0, width, height);
		image(Global.img, 0,0);
		

		// show the summary of the results on the top-left corner
		if (false)
		{
			noStroke();
			fill(255, 255, 255, 150);
			rect(0, 0, width / 2 - 5, 80);
			fill(0);// ,0,0,20);
			text("Dimensions: " + Global.dim, 25, 20);
			text("Function: " + Global.funcName, 25, 35);
			text("FEs: " + Global.evalCount, 25, 50);
			text("Fitness: " + Global.fly[Global.bestIndex].getFitness(), 25, 65);
		}
		
		
		// Draw the flies and update their positions
		for (int i = 0; i < Global.popSize; i++) {
			fill(255 / Global.popSize, 255 / Global.popSize, 255 / Global.popSize);
			stroke(0);

			// ======================= Visualising the flies on the canvas
			for ( int d = 0; d < Global.dim-1; d++ )
			{
				int eSize = 5; // ellipse size
				
				if (i == Global.bestIndex) // make the colour of the best fly RED and larger in size
				{
					fill(255, 0, 0);
					eSize = eSize * 3;
				}
				else
					fill(0,255,0,100);
				
				ellipse((float) Global.fly[i].getPos(d), (float) Global.fly[i].getPos(d + 1) , eSize, eSize);
			}
			
		}

		if (!pause)
		// ========================= EQUATION PART ============================
		//if (Global.evalCount < Global.FE_allowed) 
		{
			// EVALUATION Phase
			for (int i = 0; i < Global.popSize; i++){
				Global.fly[i].setFitness(utils.evaluate(Global.fly[i].getPos()));
			}

			utils.findBestFly();

			// INTERACTION Phase
			for (int i = 0; i < Global.popSize; i++) {
				
				//if (false)
				if (i == Global.bestIndex)
					continue;

				// utils.findClosestNeighbours(i);
				utils.getRandF_or_RingT_Neighbours(i, true); // false: Random; true: Ring

				// neighbours
				double leftP, rightP;
				leftP = Global.fly[Global.leftN].getFitness();
				rightP = Global.fly[Global.rightN].getFitness();

				int chosen; // choosing the best neighbour from the left and right neighbours
				if (leftP <= rightP)
					chosen = Global.leftN;
				else
					chosen = Global.rightN;
				
				// ================ Apply the update equation
				// =======================
				double[] temp = new double[Global.dim];
				for (int d = 0; d < Global.dim; d++) {
					temp[d] = Global.fly[chosen].getPos(d) + 
									   random(1) * (Global.fly[Global.bestIndex].getPos(d) - Global.fly[i].getPos(d));// local nei
									   //random(1) * (Global.fly[chosen].getPos(d) - Global.fly[i].getPos(d));// FINAL

					// disturbance mechanism
					if (random(1) < Global.dt)
						temp[d] = random(-Global.imgW, Global.imgW);
					
					// clamping (and keeping the flies within the range)
					if (temp[d] < 0 || temp[d] > Global.img.height) temp[d] = random (Global.img.height);
				}
				
				Global.fly[i].setPos(temp.clone());
				
				//println(Global.bestIndex + ": " + Global.fly[i].toString() + "\t" + Global.fly[i].fitness);

			} // end of interaction phase
			
			

		} else
			noLoop();
	    
	}
	
	
	
	public void loadImageFile(PImage image ) 
	{
		Global.ss = new int[image.width][image.height];
	
		image.loadPixels();
		for (int i = 0; i < image.width; i++) {
			for (int j = 0; j < image.height; j++) {
				int val = (int) brightness(image.pixels[j* image.width + i]);

				Global.ss[i][j] = val;
			}
		}
	}
	
	public static boolean pause = false;
	
	public void keyPressed() {
		if (key == 's')
			save((int) random(100) + ".png");
		else if (key == 'p')
			pause = !pause;
		else if (key == 'n')
		{
			if (imgNo == 12)
				imgNo = 1;
			else
				imgNo++;
			//Global.img = loadImage("input" + imgNo + ".bmp");
			Global.img = loadImage("Exp/" + imgNo + ".png");
			loadImageFile(Global.img);

		}

	}
	
	
	public static void main(String[] args) {
		PApplet.main(new String[] { "DFO.DFO" });

	}
	
	

}
