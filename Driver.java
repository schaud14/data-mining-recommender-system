//package datamining.binghamton.edu;
/**
 * 
 * Driver Class for Recommender System.
 * @author Saurabh Chaudhari
 *
 */
public class Driver {
	/**
	 * Main Method to take input and process output 
	 * by calling the algorithm functions
	 * @param args
	 */
	public static void main(String args[])
	{
		try
		{
			/**
			 * Check for arguments.
			 */
			if(args.length != 2)
			{
				System.out.println("Enter Correct arguments");
			}
			/**
			 * Creating object for RecommenderSystem
			 */
			RecommenderSystem rs = new RecommenderSystem();
			/**
			 * Reading input file and populating data.
			 */
			FileProcessor fp = new FileProcessor(args[0], true);
			rs.fillInputMatrix(rs, fp);
			System.out.println("Input Matrix Filled");
			/**
			 * Applying recommendation algorithm on input data.
			 */
			System.out.println("Applying Prediction Algorithm.");
			//rs.adjustMatrix();
			rs.applyPearsonCorrelation();
			rs.getPredictions();
			/**
			 * Writing predictions to output file.
			 */
			System.out.println("Writing to Output to file "+ args[1]);
			FileProcessor fpOut = new FileProcessor(args[1], false);
			rs.printPredictionMatrix(fpOut);
			System.out.println("Output Ready in file "+ args[1]);
		}
		catch (Exception e) 
		{
			System.out.println("Exception Caught");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
