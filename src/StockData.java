package p1;

import hebtu.myh.StockPrice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StockData{
	private Date month;
	private double open;
	private double high;
	private double low;
	private double close;
	private long bargin;
	
	public void setMonth(Date arg0){
		month=arg0;
	}
	
	public Date getMonth(){
		return month;
	}
	
	public void setOpen(double arg0){
		open=arg0;
	}
	
	public double getOpen(){
		return open;
	}
	
	public void setHigh(double arg0){
		high=arg0;
	}
	
	public double getHigh(){
		return high;
	}
	
	public void setLow(double arg0){
		low=arg0;
	}
	
	public double getLow(){
		return low;
	}
	
	public void setClose(double arg0){
		close=arg0;
	}
	
	public double getClose(){
		return close;
	}
	
	public void setBargin(long arg0){
		bargin=arg0;
	}
	public long getBargin(){
		return bargin;
	}
	
	public static ArrayList<StockData> toWeekData(ArrayList<StockPrice> arrayStock){
		ArrayList<StockData> arg0=new ArrayList<StockData>();
		arrayStock.trimToSize();
		int arrayStockLength=arrayStock.size();
		int i=0;
		int flag=0;
		int flagOpen=0;
		String oldDate=null;
		String date=null;
		double open=0;
		double high=0;
		double low=9999999;
		double close=0;
		long bargin=0;
		while(i<arrayStockLength-1){
			oldDate=arrayStock.get(i).getStockdate().toString();
			date=arrayStock.get(i+1).getStockdate().toString();
			try {
				if(isThisWeek(oldDate,date)){
					if(flagOpen==0){
						open=arrayStock.get(i).getStockopen();
						++flagOpen;
					}
					high=Math.max(high, arrayStock.get(i).getStockhigh());
					low=Math.min(low, arrayStock.get(i).getStocklow());
					bargin+=arrayStock.get(i).getStockvolume();
					
					if(i==arrayStockLength-2){
						++i;
						high=Math.max(high, arrayStock.get(i).getStockhigh());
						low=Math.min(low, arrayStock.get(i).getStocklow());
						close=arrayStock.get(i).getStockclose();
						bargin+=arrayStock.get(i).getStockvolume();
						StockData swd=new StockData();
						swd.setMonth(arrayStock.get(i).getStockdate());
						swd.setOpen(open);
						swd.setHigh(high);
						swd.setLow(low);
						swd.setClose(close);
						swd.setBargin(bargin);
						arg0.add(swd);
						open=0;
						high=0;
						low=9999999;
						close=0;
						bargin=0;
						flagOpen=0;
					}
				}
				else{
					if(flagOpen==0){
						open=arrayStock.get(i).getStockopen();
						++flagOpen;
					}
					high=Math.max(high, arrayStock.get(i).getStockhigh());
					low=Math.min(low, arrayStock.get(i).getStocklow());
					close=arrayStock.get(i).getStockclose();
					bargin+=arrayStock.get(i).getStockvolume();
					StockData swd=new StockData();
					swd.setMonth(arrayStock.get(i).getStockdate());
					swd.setOpen(open);
					swd.setHigh(high);
					swd.setLow(low);
					swd.setClose(close);
					swd.setBargin(bargin);
					arg0.add(swd);
					open=0;
					high=0;
					low=9999999;
					close=0;
					bargin=0;
					flagOpen=0;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			++i;
		}
		arg0.trimToSize();
		return arg0;
	}
	public static ArrayList<StockData> toMonthData(ArrayList<StockPrice> arrayStock){
		ArrayList<StockData> arg0=new ArrayList<StockData>();
		arrayStock.trimToSize();
		int arrayStockLength=arrayStock.size();
		int i=0;
		int flag=0;
		int flagOpen=0;
		String oldDate=null;
		String date=null;
		double open=0;
		double high=0;
		double low=9999999;
		double close=0;
		long bargin=0;
		while(i<arrayStockLength-1){
			oldDate=arrayStock.get(i).getStockdate().toString();
			date=arrayStock.get(i+1).getStockdate().toString();
			try {
				if(isThisMonth(oldDate,date)){
					if(flagOpen==0){
						open=arrayStock.get(i).getStockopen();
						++flagOpen;
					}
					high=Math.max(high, arrayStock.get(i).getStockhigh());
					low=Math.min(low, arrayStock.get(i).getStocklow());
					bargin+=arrayStock.get(i).getStockvolume();
					
					if(i==arrayStockLength-2){
						++i;
						high=Math.max(high, arrayStock.get(i).getStockhigh());
						low=Math.min(low, arrayStock.get(i).getStocklow());
						close=arrayStock.get(i).getStockclose();
						bargin+=arrayStock.get(i).getStockvolume();
						StockData swd=new StockData();
						swd.setMonth(arrayStock.get(i).getStockdate());
						swd.setOpen(open);
						swd.setHigh(high);
						swd.setLow(low);
						swd.setClose(close);
						swd.setBargin(bargin);
						arg0.add(swd);
						open=0;
						high=0;
						low=9999999;
						close=0;
						bargin=0;
						flagOpen=0;
					}
				}
				else{
					if(flagOpen==0){
						open=arrayStock.get(i).getStockopen();
						++flagOpen;
					}
					high=Math.max(high, arrayStock.get(i).getStockhigh());
					low=Math.min(low, arrayStock.get(i).getStocklow());
					close=arrayStock.get(i).getStockclose();
					bargin+=arrayStock.get(i).getStockvolume();
					StockData swd=new StockData();
					swd.setMonth(arrayStock.get(i).getStockdate());
					swd.setOpen(open);
					swd.setHigh(high);
					swd.setLow(low);
					swd.setClose(close);
					swd.setBargin(bargin);
					arg0.add(swd);
					open=0;
					high=0;
					low=9999999;
					close=0;
					bargin=0;
					flagOpen=0;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			++i;
		}
		arg0.trimToSize();
		return arg0;
	}
	public static boolean isThisWeek(String arg0,String arg1) throws Exception {  
		long oneWeek=7*24*60*60*1000;
		long threeDay=3*24*60*60*1000;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar c = Calendar.getInstance();  
		c.setTime(format.parse(arg0));  
		Calendar c1 = Calendar.getInstance();  
		c1.setTime(format.parse(arg1));  
		if((c.getTimeInMillis()-threeDay)/oneWeek==(c1.getTimeInMillis()-threeDay)/oneWeek){
			return true;
		}else{ 
			return false;  
		}
	}
	public static boolean isThisMonth(String arg0,String arg1) throws Exception {  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar c = Calendar.getInstance();  
		c.setTime(format.parse(arg0));  
		Calendar c1 = Calendar.getInstance();  
		c1.setTime(format.parse(arg1));  
		if(c.get(Calendar.MONTH)==c1.get(Calendar.MONTH)){
			return true;
		}else{ 
			return false;  
		}
	}
}

