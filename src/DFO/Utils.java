package DFO;
import java.util.Random;
import java.math.*;

public class Utils {
	
	Random r = new Random();

	public double evaluate(double[] a) {
		Global.evalCount++;
		return Math.abs( symmetry(a.clone()) );
		
		// Write the relevant code for the 'symmetry' function below
		
		// other existing functions
		// Sphere Schwefel12 Rosenbrock GSchwefel26
		// Rastrigin Ackley Griewank PenalizedP8 PenalizedP16
	}

	public void findClosestNeighbours(int curr) {
		double minDL = 10E15;
		for (int i = 0; i < Global.popSize; i++) {
			if (i == curr)
				continue;

			double d = Global.fly[curr].getDistance(i);
			if (d < minDL) {
				minDL = d;
				Global.leftN = i;
			}
		}

		double minDR = 10E15;
		for (int i = 0; i < Global.popSize; i++) {
			if (i == curr)
				continue;
			if (i == Global.leftN)
				continue;

			double d = Global.fly[curr].getDistance(i);
			if (d < minDR) {
				minDR = d;
				Global.rightN = i;
			}
		}

	}

	public void getRandF_or_RingT_Neighbours(int curr, boolean topology) {
		
		if (topology) // RING
		{
			Global.leftN = curr - 1;
			Global.rightN = curr + 1;

			if (curr == 0)
				Global.leftN = Global.popSize - 1;
			if (curr == Global.popSize - 1)
				Global.rightN = 0;
		}
		else // RANDOM
		{
			Global.leftN = r.nextInt(Global.popSize);
			while (Global.leftN == curr)
				Global.leftN = r.nextInt(Global.popSize);

			Global.rightN = r.nextInt(Global.popSize);
			while ((Global.rightN == curr) || (Global.rightN == Global.leftN))
				Global.rightN = r.nextInt(Global.popSize);
		} 
		
	}

	double min = 10E10;
	public void findBestFly() {
		min = Global.fly[0].getFitness();

		for (int i = 1; i < Global.popSize; i++) {
			if (Global.fly[i].getFitness() <= min) {
				min = Global.fly[i].getFitness();
				Global.bestIndex = i;
			}
		}
	}

	public void printSummary() {
		if (Global.evalCount % 1000 == 0)
			System.out.println( "\nFE: " + Global.evalCount / 1000 + " ===> \n\t"
					+ Global.fly[Global.bestIndex].getFitness() + "\t" + Global.bestIndex + "\t"
					+ Global.fly[Global.bestIndex].toString());

	}

	public double[] getRandPos() {
		double[] a = new double[Global.dim];
		for (int d = 0; d < Global.dim; d++)
			a[d] = r.nextInt(Global.imgW);

		return a.clone();
	}

	public double[] getRandPos1() {
		double[] a = new double[Global.dim];
		for (int d = 0; d < Global.dim; d++)
			a[d] = -Global.imgW + Global.imgW / 2 * r.nextDouble();
		return a.clone();
	}

	public double getGaussian(double aMean, double aVariance) {
		return aMean + r.nextGaussian() * aVariance;
	}
	
	// ===================== functions
	
	int dimension = Global.dim;
	double offset = -0;

	public double symmetry(double[] p) {
		
		double a = 0;
		int rad = 50; // coverage radius
		
		int x = (int)p[0];
		int y = (int)p[1];
		
		int x1, x2, y1, y2;
		int maxX, maxY;
		int randX, randY;

		maxX = (Global.imgW-1);
	    maxY = (Global.imgH-1);

	    randX = r.nextInt(maxX+1);
	    randY = r.nextInt(maxY+1);

	    x1 = torus(x + randX);
	    x2 = torus(x - randX);

	    // Use torus function again to calculate the value of y1 and y2
	    y1 = torus(y + randY);
	    y2 = torus(y - randY);
	    
	    int sum1 = 0;
	    // Calculate the value of sum1 here
	    //Global.ss
	    for(int i =0; i<rad; i++)
	    	for(int j=0; j<rad; j++){
	    		double[] newA = {torus(x1+i), torus(y1+j)};
	    		sum1 += brightness(newA.clone());
	    	}
	    
	    int sum2 = 0;
	    // Calculate the value of sum2 here
	    // ...
	    for(int i =0; i<rad; i++)
	    	for(int j=0; j<rad; j++){
	    		double[] newA = {torus(x2-i), torus(y2-j)};
	    		sum2 += brightness(newA.clone());
	    	}
	    
	    a = Math.abs( sum1 - sum2 );
		
		Global.funcName = "Symmetery";
		return a;				
	}
	
	
	public int torus( int n )
	{
	  int result = n;
	  if ( n >= Global.imgW )
	    result = n % Global.imgW;
	  else if ( n < 0 )
	    result = Global.imgW+n;

	  return result;
	}
	
	
	public double brightness(double[] p) {
		double a = 0;
		a = 255 - Global.ss[(int)p[0]][(int)p[1]];
		System.out.println(a);
		Global.funcName = "Brightness";
		return a;
	}
	

	public double Sphere(double[] p) {
		double a = 0;
		for (int i = 0; i < dimension; i++) {
			a = a + Math.pow(p[i] + offset, 2);
		}
		Global.funcName = "Sphere";
		return a;
	}

	public double Rastrigin(double[] p) {
		double a = 0;
		for (int i = 0; i < dimension; i++) {
			a = a + Math.pow(p[i], 2) - 10 * Math.cos(2 * Math.PI * (p[i])) + 10;
		}
		Global.funcName = "Rastrigin";
		return a;
	}

	public double Rosenbrock(double[] p) {
		double a = 0;
		for (int i = 0; i < dimension - 1; i++) {
			a = a + Math.pow((1 - p[i]), 2) + 100
					* Math.pow((p[i + 1] - Math.pow(p[i], 2)), 2); // Generalised
		}
		Global.funcName = "Rosenbrock";
		return a;
	}

	public double Griewank(double[] p) {
		double a = 0;
		double b = 1;
		for (int i = 0; i < dimension; i++) {
			a = a + Math.pow(p[i], 2);
			b = b * Math.cos(p[i] / Math.sqrt(i + 1));
		}

		double c = a / 4000.0 - b + 1;
		Global.funcName = "Griewank";
		return c;
	}

	public double Ackley(double[] p) {
		double a = 0;
		double b = 0;
		for (int i = 0; i < dimension; i++) {
			a = a + Math.pow(p[i], 2);
			b = b + Math.cos(2 * Math.PI * p[i]);
		}

		double c = -20 * Math.exp(-0.2 * Math.sqrt(a / dimension))
				- Math.exp(b / dimension) + 20 + Math.E;
		Global.funcName = "Ackley";
		return c;
	}

	public double Schwefel12(double[] p) {
		double a = 0;
		double b = 0;

		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j <= i; j++) {
				b = b + p[j];
			}

			a = a + Math.pow(b, 2);
			b = 0;
		}
		Global.funcName = "Schwefel12";
		return a;
	}

	public double GSchwefel26(double[] p) {
		double a = 0;
		for (int i = 0; i < dimension; i++)
			a = a + p[i] * Math.sin(Math.sqrt(Math.abs(p[i])));

		Global.funcName = "GSchwefel26";
		return -a - 12569.48661816488;
	}

	public double mu(double x, int a, int k, int m) // FOR PenalizedP8 and
													// PenalizedP16
	{
		if (x > a)
			return k * Math.pow(x - a, m);
		else if ((x >= -a) | (x <= a))
			return 0;
		else if (x < -a)
			return k * Math.pow(-x - a, m);
		else
			return 0.0;
	}

	public double yi(double x) // FOR PenalizedP8 and PenalizedP16
	{
		return 1 + 0.25 * (x + 1);
	}

	public double PenalizedP8(double[] p) {
		double a1 = (Math.PI / dimension);
		double a2 = 10 * Math.pow(Math.sin(Math.PI * yi(p[0])), 2);
		double a3 = 0;
		for (int i = 0; i < dimension - 1; i++)
			a3 = a3 + Math.pow(yi(p[i]) - 1, 2)
					* (1 + 10 * Math.pow(Math.sin(Math.PI * yi(p[i + 1])), 2));
		double a4 = Math.pow(yi(p[dimension - 1]) - 1, 2);
		double a5 = 0;

		for (int i = 0; i < dimension; i++)
			a5 = a5 + mu(p[i], 10, 100, 4);

		Global.funcName = "PenalizedP8";
		return a1 * (a2 + a3 + a4) + a5;
	}

	public double PenalizedP16(double[] p) {
		double a1 = 0.1;
		double a2 = Math.pow(3 * Math.sin(Math.PI * p[0]), 2);
		double a3 = 0;
		for (int i = 0; i < dimension - 1; i++)
			a3 = a3 + Math.pow(p[i] - 1, 2)
					* (1 + Math.pow(Math.sin(3 * Math.PI * p[i + 1]), 2));
		double a4 = Math.pow(p[dimension - 1] - 1, 2);
		double a5 = 0;
		for (int i = 0; i < dimension; i++)
			a5 = a5 + mu(p[i], 5, 100, 4);
		Global.funcName = "PenalizedP16";
		return a1 * (a2 + a3 + a4) + a5;
	}

}
