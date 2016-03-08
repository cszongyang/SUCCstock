package p1;

import hebtu.myh.Stock;
import hebtu.myh.StockPrice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import javax.swing.SwingWorker;
/*统计MA单边
 * 这个没有用数据库
 * 前面5日线小于十日线，最后一天收盘价站到十日线上面
 * */
public class FunctionTJMADB extends SwingWorker<Integer, Integer>{
	
	private String[] namearg0;
	private String startdate;
	private String enddate;
	private ArrayList<tjMADBResult> productList=new ArrayList<tjMADBResult>();
	public ArrayList<tjMADBResult> getResult(){
		return productList;
	}
	public FunctionTJMADB(String[] arg0){
		startdate="1990-01-01";
		enddate=Function.getEndTime();
		namearg0=arg0;
	}

	
	private double[] getMAXOpen(ArrayList<StockPrice> arrayStock,int arg0){
		double[] MAXOpen={0,0};
		for(int i=0;i<25;++i){
			double temp=arrayStock.get(arg0).getStockopen();
			if(temp>MAXOpen[0]){
				MAXOpen[0]=temp;
				MAXOpen[1]=i;
			}
			++arg0;
		}
		return MAXOpen;
	}
	
	public String double2String(double arg0){
		String temp=String.valueOf(arg0);
		if(temp.length()>4){
			return temp.substring(0, 5);
		}
		else{
		return temp;
		}
	}
	public  void getStockMA(String[] arg0,String arg1,String arg2){
		int _count=0;
		for(int mm=0;mm<arg0.length;++mm){
				tjMADBResult noname=new tjMADBResult();
				Stock stock= new Stock(arg0[mm],arg1,arg2);
				ArrayList<StockPrice> arrayStock;
				int errNumber;
				errNumber=stock.open();
				double winCount=0;
				double loseCount=0;
				double winP=0;
				double loseP=0;
				if (errNumber==1){
					arrayStock=new ArrayList<StockPrice>();
					arrayStock=stock.getArrayListStock();
					int arrayXB=arrayStock.size()-1;
					int sum=0;
					double avargeMA5=0;
        			double avargeMA10=0;
					double[] magic={0,0};
					if(arrayXB>100){
						for(int i=9;i<arrayXB-30;i++){
        					avargeMA5=(arrayStock.get(i).getStockclose()+arrayStock.get(i-1).getStockclose()+arrayStock.get(i-2).getStockclose()+arrayStock.get(i-3).getStockclose()+arrayStock.get(i-4).getStockclose())/5;
        					avargeMA10=(arrayStock.get(i).getStockclose()+arrayStock.get(i-1).getStockclose()+arrayStock.get(i-2).getStockclose()+arrayStock.get(i-3).getStockclose()+arrayStock.get(i-4).getStockclose()+arrayStock.get(i-5).getStockclose()+arrayStock.get(i-6).getStockclose()+arrayStock.get(i-7).getStockclose()+arrayStock.get(i-8).getStockclose()+arrayStock.get(i-9).getStockclose())/10;
        					if(avargeMA5<avargeMA10){
        						sum++;
        						if(sum>15&&arrayStock.get(i).getStockclose()>avargeMA10){
        							sum+=10000;
        						}
        					}
        					else if(avargeMA5>avargeMA10){
        						if(sum>15&&arrayStock.get(i).getStockclose()>avargeMA10){
        							sum+=10000;
        						}
        						else{
        							sum=0;}
        					}
        					if(sum>10015){//满足条件了，买入股票
        						//System.out.println("买入股票");
        						double todayOpen=arrayStock.get(i+1).getStockopen();//第二天开盘价买入
								magic=getMAXOpen(arrayStock,i+2);//找未来25个交易日的最高值
								if(todayOpen<magic[0]){//赚钱了
									winP+=(magic[0]-todayOpen)/todayOpen;
									++winCount;
								}
								else{//没赚钱
									loseP+=(todayOpen-magic[0])/todayOpen;
									++loseCount;
								}
								sum=0;
								i+=(int)magic[1];
								magic[1]=0;
        					}
        				}
						noname.name=arg0[mm];
						noname.winP=double2String(100*winP/winCount);
						noname.winCount=double2String(100*winCount/(winCount+loseCount));
						noname.loseP=double2String(100*loseP/loseCount);
						noname.loseCount=double2String(100*loseCount/(winCount+loseCount));
						productList.add(noname);
						
						//System.out.println(winP);
						
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
	            setProgress(_count*100/arg0.length);
		}
		
		productList.trimToSize();
		
		
	}

	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		getStockMA(namearg0,startdate,enddate);
		return 0;
	}
}

class tjMADBResult{
	String name=null;
	String winCount=null;
	String loseCount=null;
	String winP=null;
	String loseP=null;	
}