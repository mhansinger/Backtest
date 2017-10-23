
public class Backtest {
	public static void main(String[] args){
		// initialisieren
		// ReadFiles r = new ReadFiles();
		// RollingMean m = new RollingMean();
		BacktestEngine engine = new BacktestEngine();
		
		// get filename
		
		//compute SMA
		
		engine.setInvestment(1000);
		engine.computeSMA(500);
		
		float[] test = engine.computeSMA(10);
		//System.out.println(test);
		
		//engine.SMAcrossover(470,1000);
		
		long startTime = System.currentTimeMillis();
		
		engine.optimizeSMA(200, 600, 700, 2000);
		//engine.optimizeSMA(200, 220, 1000, 1040);
		
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("\n Elapsed Time is: "+estimatedTime);
	}
	
}
