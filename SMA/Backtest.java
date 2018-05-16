
public class Backtest {
	public static void main(String[] args){
		
        for(int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
		// initialisieren

		BacktestEngine engine = new BacktestEngine();
		
		//compute SMA
		
		engine.setInvestment(1000);
		
		long startTime = System.currentTimeMillis();
		
		//engine.optimizeSMA(300, 600, 900, 2100);
		engine.optimizeSMA(250, 500, 800, 1300);
		
		long estimatedTime = System.currentTimeMillis() - startTime;
		System.out.println("\n Elapsed Time is: "+estimatedTime);
	}
	
}
