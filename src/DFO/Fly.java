package DFO;
public class Fly {
	double[] pos = new double[Global.dim];
	double[] exPos = new double[Global.dim];
	double fitness = 10E8;

	public Fly(double[] position) {
		pos = position.clone();
	}

	public double[] getPos() {
		return pos.clone();
	}

	public double getPos(int n) {
		return pos[n];
	}

	public double[] getExPos() {
		return exPos.clone();
	}

	public double getExPos(int n) {
		return exPos[n];
	}

	public void setPos(double[] position) {
		exPos = pos.clone();
		pos = position.clone();
	}

	public void setPos(int i, double n) {
		exPos[i] = pos[i];
		pos[i] = n;
	}

	public void setFitness(double t) {
		fitness = t;
	}

	public double getFitness() {
		return fitness;
	}

	public double getDistance(int n) {
		double squaredSum = 0;
		for (int d = 0; d < Global.dim - 1; d++) {
			squaredSum = squaredSum + Math.pow(getPos(d) - Global.fly[n].getPos(d), 2);
		}
		return Math.sqrt(squaredSum);
	}

	public String toString() {
		String s = "";
		for ( int d = 0; d < Global.dim-1; d++ )
			s = s + pos[d] + ", ";

		s = s + pos[Global.dim - 1];

		return s;

	}
}