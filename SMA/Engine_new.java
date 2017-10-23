import java.math.*;

public class Engine_new extends RollingMean{
	
	int winShort;
	int winLong;
	float investment;
	
	float fee;
	boolean marketPos = false;
	
	float[] portfolio; // = new float[series.length];
	float[] shares;
	float[] logRet;
	float[] longMean;
	float[] shortMean;
	
	//constructor
	Engine_new(){
		portfolio = new float[series.length];
		shares = new float[series.length];
		logRet = new float[series.length];
		longMean = new float[series.length];
		shortMean = new float[series.length];
	}
	
	public void setInvestment(int inv){

		investment=inv;//1000;
		//fee=0.0016;
		// initialize portfolio array new
		for(int i = 0; i < portfolio.length; i++) {
            portfolio[i] = investment;
            //logRet[i]=0;
        }  
	}
	
	public void reset(){

		for(int i = 0; i < series.length; i++) {
            portfolio[i] = investment;
    		shares[i] = 0;
    		logRet[i] = 0;
    		longMean[i] = 0;
    		shortMean[i] = 0;
		}
	}
	
	public void enterMarket(int pos){
		shares[pos]= portfolio[pos-1]/series[pos];
		portfolio[pos]=(float) ((shares[pos] * series[pos]) * (1.0 - 0.0016));	//fee is hard coded
		marketPos=true;
	}
	
	public void exitMarket(int pos){
		portfolio[pos]=(float) ((shares[pos-1] * series[pos]) * (1.0 - 0.0016));	// fee is hard coded
		shares[pos]= 0;
		marketPos=false;
	}
	
	public void upPortfolio(int pos){
		shares[pos]=shares[pos-1];
		portfolio[pos]=shares[pos]*series[pos];
		marketPos=true;
	}
	
	public void downPortfolio(int pos){
		shares[pos]=0;
		portfolio[pos]=portfolio[pos-1];
		marketPos=false;
	}
	
	public void logReturn(int pos){
		logRet[pos]=(float) Math.log(series[pos]/series[pos-1]);
	}
	
	public float Hodl(){
		float initialShares=investment/series[1];
		return (initialShares*series[series.length-1]);
	}

	public void SMAcrossover(int shortW, int longW){
		marketPos=false;
		winShort = shortW;
		winLong = longW;
		
		// resest all the attributes
		reset();
		
        // get the rolling means
        longMean = computeSMA(winLong);
        shortMean = computeSMA(winShort);
        
        System.out.printf("Short win: %s\n",shortW);
        System.out.printf("Long win: %s",longW);
        System.out.println("\n");
	
		for(int i=winLong; i<series.length; i++)
		{
			// compute log return
			logReturn(i);
		
			//check if it is to buy or sell:
			if(shortMean[i]>longMean[i])
			{
				if(marketPos==false)
				{
					//System.out.println("BUY");
					enterMarket(i);
				}else{
					upPortfolio(i);
				}
			}
			else if(shortMean[i]<=longMean[i])
			{
				if(marketPos==true){
					exitMarket(i);
					//System.out.println("SELL");
				}else{
					downPortfolio(i);
				}
			}
			//System.out.printf("Portfolio after SMA: %s \n", portfolio[i]);
			//System.out.printf("shares: %s ", shares[i]);
		}
       
		System.out.printf("Portfolio after SMA: %s ", portfolio[portfolio.length-1]);
		//System.out.println();
		//System.out.printf("Only Hodl: %s", Hodl());
		System.out.println();
	}
	
	public void printPortfolio(){
		for(int x=0;x<portfolio.length;x++){
			System.out.printf("%s ", portfolio[x]);
			System.out.println();
		}	
	}
	
	// member to do optimization; just loops; could also be implemented with stochastics
	public void optimizeSMA(int shortStart, int shortEnd, int longStart, int longEnd){
		// initialize some storage variables
		int bestShort = 0;
		int bestLong = 0;
		float finalPortf =0;
		int step = 10;
		
		// loop over long window
		for(int i=longStart;i<longEnd;i=i+step){
			// loop over short win
			for(int j=shortStart;j<shortEnd;j=j+step){
				if(i>j){		//checks that longer is always bigger than shorter 
					// here comes the SMA function
					SMAcrossover(j,i);
					
					// check if the results are better than previous ones
					if(portfolio[portfolio.length-1]>finalPortf){
						finalPortf = portfolio[portfolio.length-1];
						bestShort = j;
						bestLong = i;
					}
				}
			}
		}
		System.out.printf("Best Portfolio is at: %s \n", finalPortf);
		System.out.printf("Best short window: %s \n", bestShort);
		System.out.printf("Best long window: %s \n", bestLong);
		System.out.println("\n");
		
	}
}