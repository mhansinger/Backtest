import java.io.*;
import java.math.*;
import java.util.StringTokenizer;
import java.util.Scanner;

// Vererbung von ReadFiles, alle Methoden sollten da sein
public class RollingMean{
	
	//constructor
	int window;
	
	float[] SMAarray; 	//SMA obacht mit null pointers
	float[] series;
	float[] SMAsquare; 	
	float[] STDarray;
	// file reader
	ReadFiles r = new ReadFiles();
	
	// constructor
	RollingMean(){
		// initialisiert den data reader und das dataArray
		r.getfilename();
		//r.getlimit();
		r.convertToArray();
		r.createDataArray();
		series = r.dataArray;
	}
	
	public float[] computeSMA(int win){
		SMAarray = new float[series.length]; // initialize array always new
		window = win;
		int iterPos=0;
		float val; float sum=0; float varSum=0;
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
	
	// computes the rolling standard deviation of a time series
	public float[] rollingSTD(int win){
		SMAsquare = new float[series.length];
		STDarray = new float[series.length];
		window = win;
		int iterPos=0;
		float mu; float sum=0; float varSum=0; float sigSquare = 0;
		int arrPos;
		// data array (series) is known from parent class
		for(int i=window;i<series.length;i++){ 
			// computes the mean for each window
			for(int j=iterPos; j<(window+iterPos)&j<series.length;j++){
				sum+=series[j]; // sums up all data points from dataArray over window span  
			}
			mu = sum/window;
			for(int j=iterPos;j<(window+iterPos)&j<series.length;j++){
				sigSquare += ((series[j]-mu)*(series[j]-mu));
			}
			arrPos = window+iterPos;
			// computes the standard deviation:
			STDarray[arrPos] = (float) Math.sqrt(sigSquare/window);
			iterPos++; sum=0; sigSquare=0;	//reset sum, advance with iterPos
		}
		return STDarray;
	}
	
	public void printSMA(){
		for(int x=0;x<SMAarray.length;x++){
			System.out.printf("%s ", SMAarray[x]);
			System.out.println();
		}	
	}
	
	
}
