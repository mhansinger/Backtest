import java.io.*;
import java.util.StringTokenizer;
import java.util.Scanner;


public class ReadFiles {
	File file;// = new File("Data/Series.csv");
	int row = 0;
	String[][] items;
	float[] dataArray; 	// is the actual time series

	Scanner scanner = new Scanner(System.in);
	//Scanner scanner2 = new Scanner(System.in);
	String name;
	String zahl;
	
	float limiter=1;
	
	// read in file name
	public void getfilename(){
	 System.out.println("Where is your data?");
	 name = scanner.next();
	 file = new File(name);
	 scanner.close();
	}

/*
	public void getlimit(){
		System.out.println("Set the limit (removes spikes)");
		zahl = scanner2.next();
		//System.out.printf("%s",zahl);
		//limiter=Float.parseFloat(string);
		scanner.close();
	}
	*/
	
	
	// check if file exists
	public boolean checkIsFile(){
		return file.isFile();
	}
	
	//find nr of rows in CSV
	public int findRowNumber(){
		row =0;
		if(checkIsFile()){
			try{
				BufferedReader reader = new BufferedReader(new FileReader(file));
				while((reader.readLine())!=null){
					row++;				
				}// close the reader
				//reader.close();
			}catch(Exception e){
				System.out.println(e);
			}
		}else{
			System.out.println("File is not available");
		}
		return row;
	}
	
	public void convertToArray(){
		int r = 0;
		int nCols = 3;
		items = new String[findRowNumber()][nCols];
		//items = new double[findRowNumber()];
		
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			
			while((line=reader.readLine())!=null){
				//reads through the lines and breaks it up into chunks, comma separated
				StringTokenizer z = new StringTokenizer(line,",");	
				while(z.hasMoreTokens()){
					for(int c=0;c<nCols;c++){
						// assigns the tokens to items
						items[r][c]=z.nextToken();				
					}
					// loop to next row
					r++;
				}
			}
			// close the reader
			// reader.close();
			
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	// print out items array
	public void printArray(){
		for(int x=0;x<items.length;x++){
			System.out.printf("%s - ",x);
			for(int y=0;y<items[x].length;y++){
				System.out.printf("%s -",items[x][y]);
			}
			System.out.println();
		}
	}
	
	// get the Array back
	public String[][] getArray(){
		return items;
	}

	public void createDataArray(){
		// set up the size of the array
		dataArray = new float[findRowNumber()];
		//loop over the items
		for(int x=0; x<items.length;x++){
			dataArray[x] = Float.parseFloat(items[x][2]);	// issue resolved...
			if(dataArray[x]>limiter)
			{
				dataArray[x] = dataArray[x-1];
			}
		}
	}
	
	public void printFloatArray(){
		for(int x=0;x<dataArray.length;x++){
			System.out.printf("%s ", dataArray[x]);
			System.out.println();
		}	
	}
	
	public float[] getData(){
		// initialisiert Daten
		createDataArray();
		return dataArray;
	}
}
