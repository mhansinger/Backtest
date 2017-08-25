
public class Backtest {
	public static void main(String[] args){
		// initialisieren
		//ReadFiles r = new ReadFiles();
		//RollingMean m = new RollingMean();
		BacktestEngine engine = new BacktestEngine();
		
		
		// get filename
		//r.getfilename();
		
		//engine.r.printFloatArray();
		
		//compute SMA
		//engine.SMAcrossover(470, 1000);
		engine.setInvestment(1000);
		engine.computeSMA(500);
		//engine.printSMA();
		float[] test = engine.computeSMA(10);
		//System.out.println(test);
		
		engine.SMAcrossover(470,1000);
		engine.SMAcrossover(470,1000);
		engine.SMAcrossover(470,1000);
		engine.optimizeSMA(200, 600, 700, 3500);
		
		//System.out.println(engine.Hodl());
		//engine.printPortfolio();
		
	}
	
}
