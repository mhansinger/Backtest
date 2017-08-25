import java.io.*;
import java.util.StringTokenizer;
import java.util.Scanner;

// Vererbung von ReadFiles, alle Methoden sollten da sein
public class RollingMean{
	
	//constructor
	int window;
	
	float sum=0;
	float[] SMAarray; 	//SMA obacht mit null pointers
	float[] series;
	// file reader
	ReadFiles r = new ReadFiles();
	
	// constructor
	RollingMean(){
		// initialisiert den data reader und das dataArray
		r.getfilename();
		r.convertToArray();
		r.createDataArray();
		series = r.dataArray;
	}
	
	public float[] computeSMA(int win){
		SMAarray = new float[series.length]; // initialize array always new
		window = win;
		int iterPos=0;
		float val;
		int arrPos;
		// data array is known from parent class
		for(int i=window;i<series.length;i++){ 
			for(int j=iterPos;j<(window+iterPos)&j<series.length;j++){
				sum+=series[j]; // sums up all data points from dataArray over window span
			}
			val = sum/window;
			arrPos = window+iterPos;
			SMAarray[arrPos] = val; // assigns the SMA value to the SMA array
			iterPos++; sum=0; 	//reset sum, advance with iterPos
		}
		return SMAarray;
	}
	
	public void printSMA(){
		for(int x=0;x<SMAarray.length;x++){
			System.out.printf("%s ", SMAarray[x]);
			System.out.println();
		}	
	}
	
	
}
