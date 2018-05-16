import java.math.*;
import java.util.concurrent.TimeUnit;

public class BacktestEngine extends RollingMean{
	
	int winShort;
	int winLong;
	int bollcount=0, normcount=0;
	float investment;
	//float limiter = 20000;
	
	double fee;
	boolean marketPos = false;
	
	float[] portfolio; // = new float[series.length];
	float[] shares;
	float[] logRet;
	float[] longMean;
	float[] shortMean;
	float[] bollinger;
	
	//constructor
	BacktestEngine(){
		portfolio = new float[series.length];
		shares = new float[series.length];
		logRet = new float[series.length];
		longMean = new float[series.length];
		shortMean = new float[series.length];
		bollinger = new float[series.length];
		System.out.println("\n Length of time series: "+series.length);
		
		//TimeUnit.SECONDS.sleep(1);
	}
	
    public static float[] combine(float[] a, float[] b){
        int length = a.length ;
        float[] result = new float[length];
        for(int i=0;i<length;i++){
        	result[i]=a[i] + 2*b[i];
        }
        return result;
    }
	
	public void setInvestment(float inv){
		investment=inv; //1000;
		fee=0.0016;
		// initialize portfolio array new
		for(int i = 0; i < portfolio.length; i++) {
            portfolio[i] = investment;//0;			
        }  
	}
	
	public void reset(){
		for(int i = 0; i < series.length; i++) {
            portfolio[i] = investment; //0;
    		shares[i] = 0;
    		logRet[i] = 0;
    		longMean[i] = 0;
    		shortMean[i] = 0;    		
		}
	}
	
	public void enterMarket(int pos){
		shares[pos]= portfolio[pos-1]/series[pos];
		portfolio[pos]=(float) ((shares[pos] * series[pos]) * (1.0 - fee));	//fee is hard coded
		marketPos=true;
		if(portfolio[pos]<10){
			System.out.println("Alarm!");
		}
	}
	
	public void exitMarket(int pos){
		portfolio[pos]=(float) ((shares[pos-1] * series[pos]) * (1.0 - fee));	// fee is hard coded
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
		marketPos = false;
		winShort = shortW;
		winLong = longW;
		
		// resest all the attributes
		reset();
		
        // get the rolling means
        longMean = computeSMA(winLong);
        shortMean = computeSMA(winShort);
        
        // upper bollinger boundary
		// check this with the python version
        bollinger = combine(longMean,rollingSTD((2*winLong)));
        
        System.out.printf("\nShort win: %s\n",shortW);
        System.out.printf("Long win: %s",longW);
        System.out.println("\n");
	
		for(int i=winLong; i<series.length; i++)
		{
			// compute log return
			logReturn(i);
		
			//check if it is to buy or sell:
			
			if(shortMean[i]>longMean[i] )
			{
				if(marketPos==false  && series[i]>bollinger[i])
				{
					enterMarket(i);

				}else if(marketPos==false){
					downPortfolio(i);
				}
				else if(marketPos==true){
					upPortfolio(i);
				}
			}			
			else
			{
				if(marketPos==true){
					exitMarket(i);
					//System.out.println("SELL");
				}else{
					downPortfolio(i);
				}
			}
		}
       
		System.out.printf("Portfolio after SMA: %s ", portfolio[portfolio.length-1]);
		//System.out.println();
		//System.out.printf("Only Hodl: %s", Hodl());
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
		int step_long = 25;
                int step_short = 10;
		
		// loop over long window
		for(int i=longStart;i<longEnd;i=i+step_long){
			// loop over short win
			for(int j=shortStart;j<shortEnd;j=j+step_short){
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
		System.out.printf("\n \n Best Portfolio is at: %s \n", finalPortf);
		System.out.printf("Best short window: %s \n", bestShort);
		System.out.printf("Best long window: %s \n", bestLong);
		System.out.println("\n");
		
	}
}
