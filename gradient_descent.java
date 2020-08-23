import java.io.IOException;

import javax.swing.JFrame;

public class GradientDescent 
{
	// parameters
	static double t0;
	static double t1;
	
	static double[] a_vals = {2, 3, 4, 5, 6, 8, 9, 10, 11, 12, 13, 14, 14, 15, 17, 18, 19, 20};
	static double[] b_vals = {2, 4, 5, 8, 5, 7, 8, 10, 12, 12, 14, 14, 15, 16, 16, 19, 18, 19};

	public static void main(String[] args) throws IOException
	{	
		// Algorithm settings
		    // learning rate 
		double toler = 1e-6;        // tolerance to determine convergence
		int max_iterat = 12000;      // maximum number of iterations (in case convergence is not reached)
		double alpha = 0.01;  
		
		// initial guesses for parameters
		// 0,0 are typical; 15,-1 are particularly bad for the sample data here, but good for illustration.
		t0 = 1000;
		t1 = -1000;
		
		// other variables needed
		double d0, d1;
		int iter = 0;
		
		do {
			// Simultaneous updates of thetas: compute all changes first, then update all thetas with them
			d0 = alpha * dJ_dtheta0();
			d1 = alpha * dJ_dtheta1();
			
			iter++;
			t0 -= d0;
			t1 -= d1;

			if (iter %100 == 0)
			   System.out.println("Iteration " + iter + ": theta_0=" + t0 + " - " + d0 + ", theta_1=" + t1 + " - "+ d1);

			if (iter > max_iterat) break;
		} 
		while (toler < Math.abs(d1)  || toler < Math.abs(d0));

		System.out.println("\nConvergence after " + iter + " iterations: theta_0=" + t0 + ", theta_1=" + t1);
		
		// All finished
		System.out.println("\nPress return to end ...");
		System.in.read();
		System.exit(0);
	}

	/** Computes partial derivative of J wrt theta0 */
	public static double dJ_dtheta0()
	{
		double sum = 0;
		
		for (int i=0; i<a_vals.length; i++) {
			sum += h(a_vals[i]) - b_vals[i];
		}
		return sum / a_vals.length;
	}

	/** Computes partial derivative of J wrt theta1 */
	public static double dJ_dtheta1()
	{
		double sum = 0;
		
		for (int i=0; i<a_vals.length; i++) {
			sum += (h(a_vals[i]) - b_vals[i]) * a_vals[i];
		}
		return sum / a_vals.length;
	}


	/** Computes h(x) = theta0 + theta1 . x */
	public static double h(double x) 
	{
		 return t0 + t1*x;
	}

	//////////////////////////////////////////////////////////////////////
	// The remainder of the code in this class is just needed for plotting
	//////////////////////////////////////////////////////////////////////

	// All of the following member variables are just to support plotting
	static int dispiter = 50;        // interval for displaying results during iterations 
	static int pausems = 100;         // pause in milliseconds after displaying each plot: set to 0 if  not wanted
	static boolean replacetrendlines = true; // if false, new trendlines are added to old ones rather than replacing old ones as algorithm proceeds
	static boolean plotcost = true;  // This is diagnostic: runs faster if false.
	static int trendline; 			 // handle used for adding/removing trendline
	static double[] costplot, theta0plot, theta1plot, timestepplot;  // Will store data for plotting
	static double cost, prevcost;	 // For the cost plots


	
	/** J(theta0, theta1) is the squared error cost function. Only needed here to plot the change in cost. */
	public static double J()
	{
		double sum = 0;
		
		for (int i=0; i<a_vals.length; i++) {
			sum += Math.pow((h(a_vals[i]) - b_vals[i]), 2);
		}
		return sum / (2 * a_vals.length);
	}

	/** Initate the main plot */ 
	public static void initPlot(int maxiter)
	{
		// These arrays store results for plotting
		costplot = new double[maxiter+1];
		theta0plot = new double[maxiter+1];
		theta1plot = new double[maxiter+1];
		timestepplot = new double[maxiter+1];

		// Initiate the cost
		prevcost = 1e50;

		
		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("Original X-Y Data & Regression Line");
		frame.setSize(600, 600);
		frame.setVisible(true);
	}

	
	public static void updatePlot(int iter)
	{
		// Store data for plotting
		timestepplot[iter] = iter;
		theta0plot[iter] = t0;
		theta1plot[iter] = t1;
		
		// If tracking cost, check for failure to converge and store data for plotting
		if (plotcost)
		{
			cost = J();
			if (cost > prevcost) {
				System.err.println("ERROR at iteration " + iter + ": not converging, re-run with a lower value for alpha.");
				System.err.println("The cost should decrease but prev > current: " + prevcost + " > " + cost);
				System.exit(0);
				
			}
			costplot[iter] = cost; // store data for plotting
			prevcost = cost;
		}
		
		if (iter % dispiter == 0) 
		{
			if (pausems > 0) {
				try {
					Thread.sleep(pausems); // pause before updating so that first plot can be seen
				} catch (InterruptedException e) {
					// Nothing to do here
				} 
			}
		}
	}

}
