package DFO;

import processing.core.PImage;

public class Global {
	static int dim = 2; //10
	static int popSize = 100; //20 
	
	public static int[][] ss; // ref image
	public static PImage img;
	
	static double dt = 0.001; // 0.001
	 
	static int speed = 100; 
	
	static Fly[] fly = new Fly[popSize];
	static int imgW, imgH;
	
	static int FE_allowed = 300000;
	static int bestIndex = -1;
	static int evalCount = 0;
	
	static int leftN;
	static int rightN;
	
	static String funcName = "";

}
