//package datamining.binghamton.edu;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This is class for creating file processors
 * @author Saurabh Chaudhari
 *
 */
public class FileProcessor {
	private static FileInputStream input_stream;
	private static BufferedReader input_reader;
	private static BufferedWriter output_writer;
	/**
	 * Method to create the file processor
	 * @param filename
	 * @param mode
	 * Mode true = read mode
	 * Mode false = write mode
	 */
	public FileProcessor(String filename,boolean mode) 
	{
		if(mode)
		{
			try
			{
				input_stream = new FileInputStream(filename);
			}
			catch(FileNotFoundException e)
			{
				System.err.println(e.toString());
				System.exit(1);
			}
			catch (Exception e) {
				System.err.println(e.toString());
				System.exit(1);
			}
			try 
			{
				input_reader = new BufferedReader(new InputStreamReader(input_stream));
			}
			catch(Exception e)
			{
				System.err.println(e.toString());
				System.exit(1);
			}
		}
		else
		{
			File yourFile = new File(filename);
			if(!yourFile.exists()) {
			    try {
					yourFile.createNewFile();
				} 
			    catch (IOException e) 
			    {
					
					System.out.println(e.toString());
					System.exit(1);
				}
			} 
			try 
			{
				output_writer = new BufferedWriter(new FileWriter(yourFile));
			}
			catch(FileNotFoundException e)
			{
				System.err.println(e.toString());
				System.exit(1);
			}
			catch(Exception e)
			{
				System.err.println(e.toString());
				System.exit(1);
			}
		}
	}
	/**
	 * Read input line from file associated with file processor
	 * @return line
	 */
	public String readInputLine()
	{
		String line = "";
		try
		{
			line = input_reader.readLine();
			return line;
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			System.exit(1);
		}
		return line;
		
	}
	
	/**
	 * Write line to output file associated with file processor
	 * @param line
	 */
    public void writeToFile(String line) {
		try {
				output_writer.write(line);
				output_writer.write("\n");
				output_writer.flush();
			} 
		catch (Exception e) 
		{
			System.out.println(e.toString());
			System.exit(1);
		}
	}
    /**
     * Method to close the file processor objects.
     */
    public void closeFile() 
	{
		try 
		{
			input_reader.close();
			output_writer.close();
		} 
		catch (IOException e) 
		{
			System.out.println(e.toString());
			System.exit(1);
		}
	}
}
