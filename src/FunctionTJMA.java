package p1;

import hebtu.myh.Stock;
import hebtu.myh.StockPrice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.SwingWorker;
/*统计MA-最高价卖出
 * 
 * 这个类涉及到数据库，其实没必要用数据库
 * 
 * */
public class FunctionTJMA extends SwingWorker<Integer, Integer>{

	private String[] namearg0;
	private String startdate;
	private String enddate;
	private int arg0MAday;
	private int arg0MAday1;
	public FunctionTJMA(String[] arg0,int arg1,int arg2){
		startdate="1990-01-01";
		enddate=Function.getEndTime();
		namearg0=arg0;
		arg0MAday=arg1;
		arg0MAday1=arg2;
	}
	
	/*public FunctionTJMA(String[] arg0,String arg1,String arg2){
		startdate=arg1;
		enddate=arg2;
		namearg0=arg0;
	}*/
	
	
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
	
	private double getavargeMA(ArrayList<StockPrice> arrayStock,int m,int arg0){
		double temp=0;
		switch(arg0){
		case 8 :
			for(int i=0;i<8;i++){
				temp+=arrayStock.get(m-i).getStockclose();
			}
			break;
		case 10 :
			for(int i=0;i<10;i++){
				temp+=arrayStock.get(m-i).getStockclose();
			}
			break;
		
		case 15 :
			for(int i=0;i<15;i++){
				temp+=arrayStock.get(m-i).getStockclose();
			}
			break;
		}
		return temp/arg0;
		
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
	
	private void getTJMA(String[] arg0,String arg1,String arg2,int MAday,int MAday1){
		
		int _count=0;
		
		for(int i=0;i<arg0.length;++i){
			int flag=0;
			Stock stock= new Stock(arg0[i],arg1,arg2);
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
				int arrayStocklength=arrayStock.size();
				int sum=0;
				int sumsum=0;
				winCount=0;
				loseCount=0;
				winP=0;
				loseP=0;
				double[] magic={0,0};
				if(arrayStocklength>100){
					++flag;
				for(int m=33;m<arrayStocklength-30;++m){
					double MA10;
					MA10=getavargeMA(arrayStock,m,MAday);
					if(arrayStock.get(m).getStockclose()<MA10){
						++sum;
						sumsum=0;
					}
					else if(arrayStock.get(m).getStockclose()>MA10){	
						if(sum>15&&sumsum<=MAday1){
							++sumsum;
							if(sumsum==MAday1){//买入股票
								double todayOpen=arrayStock.get(m).getStockclose();//当天收盘价买入
								magic=getMAXOpen(arrayStock,m+1);
								if(todayOpen<magic[0]){//赚钱了
									winP+=(magic[0]-todayOpen)/todayOpen;
									++winCount;
								}
								else{//没赚钱
									loseP+=(todayOpen-magic[0])/todayOpen;
									++loseCount;
								}
								sum=0;
								sumsum=0;
								m+=(int)magic[1];
								magic[1]=0;
							}
						}
						else{
							sum=0;
							sumsum=0;
						//	m+=(int)magic[1];
						//	magic[1]=0;
						}
					}
					
				}
				
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
			
			if(flag!=0){
			try {
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/mystockdata", "root", "xinzhi200766");
				Statement statement=connection.createStatement();
				int resultSet=statement.executeUpdate("UPDATE ma SET wincount="+double2String(100*winCount/(winCount+loseCount))+", winmax="+double2String(100*winP/winCount)+", losecount="+double2String(100*loseCount/(winCount+loseCount))+", losemin="+double2String(100*loseP/loseCount)+" where name="+arg0[i]);
				statement.close();
				connection.close();
				//System.out.println(resultSet);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			_count++;
            setProgress(_count*100/arg0.length);
			
		}
	}
	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		getTJMA(namearg0,startdate,enddate,arg0MAday,arg0MAday1);
		return 0;
	}

}
