//package datamining.binghamton.edu;

import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Class for algorithm
 * ALGORITHM USED: Pearson Correlation / Centered Cosine Similarity
 * USER - USER based Similarity.
 * @author Saurabh Chaudhari
 *
 */
public class RecommenderSystem {
	/**
	 * Total number of users and items
	 */
	final int users = 943;
	final int items = 1682;
	/**
	 * Matrix for Storing input rating
	 */
	int[][] input_matrix = new int[users+1][items+1];
	/**
	 * Matrix for storing output / predicted ratings
	 */
	int[][] output_matrix = new int[users+1][items+1];
	/**
	 * Matrix to store similarity
	 */
	double[][] similarityMatrix = new double[users+1][users+1];
	double[][] adjustedMatrix = new double[users+1][items+1];
	/**
	 * Method to set Input data in input matrix
	 * @param row
	 * @param column
	 * @param data
	 */
	public void setInputdata(int row,int column,int data)
	{
		input_matrix[row][column] = data;
	}
	
	/**
	 * Method to fill initial input matrix by parsing input file line by line.
	 * @param rs
	 * @param fp
	 */
	public void fillInputMatrix(RecommenderSystem rs,FileProcessor fp)
	{
		String inputLine = null;
		while((inputLine = fp.readInputLine())!=null)
		{
			String [] inputArray = inputLine.split(" ");
			rs.setInputdata(Integer.parseInt(inputArray[0]), 
							Integer.parseInt(inputArray[1]), 
							Integer.parseInt(inputArray[2]));
		}
	}
	
	public void adjustMatrix() 
	{
		for (int i = 1; i <= this.users; i++)
		{
			double avg=0;
			double count=0;
			for (int j = 1; j <= this.items; j++)
			{
				if(!checkzero(input_matrix[i][j]))
				{
					avg += input_matrix[i][j];
					count++;
				}
			}
			avg = avg/count;
			for (int j = 1; j <= this.items; j++)
			{
				if(!checkzero(input_matrix[i][j]))
				{
					adjustedMatrix[i][j] = input_matrix[i][j] - avg;
				}
					
			}
			
		}
	}

	/**
	 * Method to get Final predictions
	 * This method will fill the zero values in input matrix with predicted ratings
	 */
	public void getPredictions() {
		for (int user1 = 1; user1 <= this.users; user1++)
		{
			for (int item = 1; item <= this.items; item++)
			{	
				if(checkzero(input_matrix[user1][item]))
				{
					double numerator = 0;
					double denominator = 0;
					double prediction = 0;
					TreeMap<Integer,Double> sortedMap = new TreeMap<Integer,Double>();
					for(int user2 = 1;user2<this.users;user2++)
					{
						/**
						 * Calculations for prediction of rating for user1 and item.
						 * based on all user who have rated the same item.
						 */
						if(user1!=user2 && !checkzero(input_matrix[user2][item]))
						{
							sortedMap.put(user2, similarityMatrix[user1][user2]);
						}
					}
					int i = 0;
						for(Entry<Integer, Double> entry : sortedMap.entrySet()) 
						{
							if(i<Math.min(10,sortedMap.size()))
							{
								numerator += input_matrix[entry.getKey()][item]*entry.getValue();
								denominator += Math.abs(similarityMatrix[user1][entry.getKey()]);
								i++;
							}
						}
					prediction = numerator/denominator;
					int final_prediction = correctPrediction(prediction);
					setRating(user1, item, final_prediction);
				}
				else
				{
					/**
					 * Rating already available in input data.
					 */
					setRating(user1, item, input_matrix[user1][item]);				}
			}
		}
	}
	
	/**
	 * Method to normalize the predictions in int values and [1,5] range.
	 * @param prediction
	 * @return
	 */
	private int correctPrediction(double prediction) {
		int final_prediction = (int)Math.round(prediction);
		if(final_prediction < 1)
		{
			final_prediction = 1;
		}
		else if(final_prediction > 5)
		{
			final_prediction = 5;
		}
		return final_prediction;
	}
	
	/**
	 * Method to calculate mean rating of user.
	 * @param row
	 * @return
	 */
	private double calculateMean(int row)
	{
		double avg=0;
		//int count = 0;
		for (int j = 1; j <= this.items; ++j)
		{
			{
				//if(!checkzero(input_matrix[row][j]))
				{
					avg += input_matrix[row][j];
			//		count++;
				}
			}
		}
		
		avg = avg/items;
		return avg;
	}
	/**
	 * Method for calculating similarity between all users.
	 * Fills the Similarity Matrix
	 * Similarity Formula: Pearson Correlation
	 */
	public void applyPearsonCorrelation() {
		for (int user1 = 1; user1 <= this.users; user1++)
		{
			double numerator = 0;
			double denomenator = 0;
			double meani = 0;
			double meanj = 0;
			meani = this.calculateMean(user1);
			for (int user2 = 1; user2 <= this.users; user2++)
			{
				if(user1==user2)
				{
					/**
					 * If same user then set similarity as zero since we don't consider that while predicting.
					 */
					continue;
					//similarityMatrix[i][j] = 0;
				}
				meanj = this.calculateMean(user2);
				double xsq = 0;
				double ysq = 0;
				int intersectionCount = 0;
				for(int item = 1;item<=this.items;item++)
				{
					/**
					 * Check if both users have rated the item
					 */
					if(checkzero(input_matrix[user1][item]) &&  checkzero(input_matrix[user2][item]))
					{
						/**
						 * Similarity Calculations
						 */
						numerator = (input_matrix[user1][item] - meani)*(input_matrix[user2][item] - meanj);
						 xsq += Math.pow((input_matrix[user1][item]- meani),2);
						 ysq += Math.pow((input_matrix[user2][item] - meanj),2);
						 intersectionCount++;
					}
				}
				denomenator = Math.sqrt(xsq*ysq);
				if(!checkzero(denomenator))
				{
					if(!checkzero((double)intersectionCount))
					{
						/**
						 * Apply significant weight
						 */
						int adjustment = (Math.min(intersectionCount, 50))/50;
						double similarity = (numerator/denomenator)*adjustment;
						setSimilarity(user1, user2, similarity);
					}
				}
			}
		}
	}
	
	/**
	 * Method to set data in similarity Matrix.
	 * @param row
	 * @param column
	 * @param value
	 */
	private void setSimilarity(int row,int column,double value)
	{
		similarityMatrix[row][column] = value;
	}
	
	/**
	 * Method to set data in output Matrix
	 * @param row
	 * @param column
	 * @param value
	 */
	private void setRating(int row,int column,int value)
	{
		output_matrix[row][column] = value;
	}
	
	/**
	 * Method to check if value is zero or not
	 * @param a
	 * @return boolean
	 */
	private boolean checkzero(double a)
	{
		return (a==0);
	}
	
	/**
	 * Method to print final predicted ratings to output file.	
	 * @param fpOut
	 */
	public void printPredictionMatrix(FileProcessor fpOut)
	{
		for (int i = 1; i <= users; i++)
		{
			for (int j = 1; j <= items; j++)
			{
				StringBuilder strBuild = new StringBuilder();
				strBuild.append(i).append(" ");
				strBuild.append(j).append(" ");
				strBuild.append(output_matrix[i][j]);
				fpOut.writeToFile(strBuild.toString());
			}
		}	
	}
}
	