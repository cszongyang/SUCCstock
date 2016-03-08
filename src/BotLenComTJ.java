
package p1;

import hebtu.myh.Stock;
import hebtu.myh.StockPrice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.SwingWorker;
/*锤子统计
 * 
 * 符合锤子时买入，看未来25日最高价。
 * 
 * */
public class BotLenComTJ extends SwingWorker<Integer, Integer>{
	ArrayList<Float> CompHighList;
	ArrayList<Float> CompLowList;
	ArrayList<String> ProfileList;
	String[] sNum=null;
	int daycount=25;
	
	public BotLenComTJ(String[] sNum,int daycount){
		this.sNum=sNum;
		this.daycount=daycount;
	}
	
	public ArrayList<String> getResult(){
		return ProfileList;
	}
	public String _2String(double arg0){
		String temp=String.valueOf(arg0);
		if(temp.length()>4){
			return temp.substring(0, 6);
		}
		else{
		return temp;
		}
	}
	public  void profile(String[] sNum,int daycount){
    	ProfileList=new ArrayList<String>();
    	
    	String start="1990-05-01";
		
		Calendar cal=Calendar.getInstance();
		
		int _count=0;
		
		File f = new File("e:\\number.txt");
		
			for(int m=0;m<sNum.length;m++){
			
			Stock stock= new Stock(sNum[m],start,Function.getEndTime());
			ArrayList<StockPrice> arrayStock;
			int errNumber;
			int num=0;
			int profilenum=0;
			int losenum=0;
			float sumProfile=0;
			float sumLose=0;
			errNumber=stock.open();
			if (errNumber==1){
				arrayStock=new ArrayList<StockPrice>();
				arrayStock=stock.getArrayListStock();
				int arraySZ=arrayStock.size()-1;
				
				int key1=1;
				if(arraySZ>25){
					for(int n=25;n<arraySZ-25;n+=key1){
						CompHighList=new ArrayList<Float>();
						CompLowList=new ArrayList<Float>();
						float open=arrayStock.get(n).getStockopen();
		    			float close=arrayStock.get(n).getStockclose();
		    			float low=arrayStock.get(n).getStocklow();
		    			float preclose=arrayStock.get(arraySZ-1).getStockclose();
            			float high=arrayStock.get(arraySZ).getStockhigh();
		    			
            			for(int i=1;i<=25;i++){
            				CompLowList.add(arrayStock.get(n-i).getStocklow());
            			}
            			if((low==getLow())&&(close>preclose)&&((high-Math.max(open, close))<Math.abs(open-close))&&(((open>close)&&((close-low)>2*(open-close)))||((open<close)&&((open-low)>2*(close-open))))){//最近一段时间内最低
		    				num++;
		    				for(int i=0;i<daycount;i++){
		        				CompHighList.add((float)arrayStock.get(n+i).getStockclose());
		        			}
		        			float PostHigh=(Float)getPostHigh().get(0);
		        			key1=(Integer)getPostHigh().get(1);
		        			
		        			float profile1=(PostHigh-close)/close;
		        			if(profile1>=0){
		        				profilenum++;
		        				sumProfile+=profile1;
		        			}
		        			else{
		        				losenum++;
		        				sumLose+=profile1;
		        			}

		        			String enddate=arrayStock.get(n).getStockdate().toString();
		        			
		        			String snum=sNum[m];
		        			
		        				UnitB u1=new UnitB();
		        				u1.Setdate(enddate);
		        				u1.Setkey((open-low)/(close-open));
		        				u1.SetsNum(snum);
		        				
		        				String profpor=String.valueOf(profilenum/num);
		        				String losepor=String.valueOf(losenum/num);
		        				}
			        	}
					String snum=sNum[m];
    				double AveProfile=sumProfile*100/profilenum;
    				ProfileList.add("股票号"+snum+"      盈利百分比:"+_2String (AveProfile)+"%");

				}
				
			}
			else if(errNumber==0){
				System.out.println("执行成功,但没有要获取的任何数据。");
			}
			else if(errNumber==-1){
				System.out.println("检索日期格式不正确。");
			}
			else if(errNumber==-2){
				System.out.println("到数据库的连接出现问题。");
			}
			else if(errNumber==-3){
				System.out.println("SQL语句执行出现错误。");
			}
			else if(errNumber==-4){
				System.out.println("请查看驱动程序是否工作正常。");
			}
			
			
			
			stock.close();
			_count++;
			setProgress(_count*100/sNum.length);
		}
    }
    
	
	public  float getLow(){
    	int i=CompLowList.size();
    	float temp=CompLowList.get(i-1);
    	for(int n=0;n<i;n++){
    		if(temp>CompLowList.get(n)){
    			temp=CompLowList.get(n);
    		}
    	}
    	return temp;
    }
	public  ArrayList<Object> getPostHigh(){
    	ArrayList<Object> high=new ArrayList<Object>();
    	int key=0;
    	int i=CompHighList.size();
    	
    	float temp=0;
    	
    	for(int n=0;n<i;n++){
    		if(temp<CompHighList.get(n)){
    			temp=CompHighList.get(n);
    			key=n;
    		}
    	}
    	high.add(0, temp);
    	high.add(1,key+1);
    	return high;
    }
	
	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		profile(sNum,daycount);
		return 0;
	}

}



/*class PresCOMComparator implements Comparator {
    public int compare(Object op1, Object op2) {
    	UnitB eOp1 = (UnitB) op1;
    	UnitB eOp2 = (UnitB) op2;
   
       return eOp1.Getkey()>eOp2.Getkey()?-1:eOp1.Getkey()<eOp2.Getkey()?1:0;
    }
}*/


/*class UnitB{
	private String date;
	private double key;
	private String sNum;
	private String type;
	public UnitB(){
		date=null;
		key=2.0;
		sNum=null;
		type=null;
	}
	
	public void Setdate(String date){
		this.date=date;
	}
	public void Setkey(double key){
		this.key=key;
	}
	public void SetsNum(String sNum){
		this.sNum=sNum;
	}
	public String Getdate(){
		return this.date;
	}
	public double Getkey(){
		return this.key;
	}
	public String GetsNum(){
		return this.sNum;
	}
	public String getType(){
		return type;
	}
	public void setType(String arg0){
		type=arg0;
	}
}*/
