package p1;

import java.io.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import hebtu.myh.Stock;
import hebtu.myh.StockPrice;
/*查询股票都在这个类里*/
public class Function {
	public static String getStartTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar cal=Calendar.getInstance();
		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH)+1;
		int day=cal.get(Calendar.DATE);
		String date=null;
		try {
			if(month-6>0){
				date=format.format(format.parse(String.valueOf(year)+"-"+String.valueOf(month-6)+"-"+String.valueOf(day)));	
			}
			else{		
				date=format.format(format.parse(String.valueOf(year-1)+"-"+String.valueOf(month+6)+"-"+String.valueOf(day)));			
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static String getStartTime(String arg0){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		int year=Integer.valueOf(arg0.substring(0, 4));
		int month=Integer.valueOf(arg0.substring(5, 7));
		int day=Integer.valueOf(arg0.substring(8, 10));
		String date=null;
		try {
			date=format.format(format.parse(String.valueOf(year-5)+"-"+String.valueOf(month)+"-"+String.valueOf(day)));		
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static String getEndTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar cal=Calendar.getInstance();
		String endData=null;
		try {
			endData=format.format(format.parse(String.valueOf(cal.get(Calendar.YEAR))+"-0"+String.valueOf(cal.get(Calendar.MONTH)+1)+"-"+String.valueOf(cal.get(Calendar.DATE))));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return endData;
	}
	/*查询股票数据*/
	public static ArrayList<StockData> getStockList(String sNum,String startDate,String endDate,int dayorWeek){
		ArrayList<StockData> productList = new ArrayList<StockData>();
		Stock stock= new Stock(sNum,startDate,endDate);
		ArrayList<StockPrice> arrayStock;
		int errNumber;
		errNumber=stock.open();
		if (errNumber==1){
			arrayStock=new ArrayList<StockPrice>();
			arrayStock=stock.getArrayListStock();
			StockData diffrent=new StockData();
			diffrent.setHigh(stock.MAXhigh);
			diffrent.setLow(stock.MINlow);
			productList.add(diffrent);
			if(dayorWeek==0){
				for(StockPrice sp:arrayStock){
					StockData p = new StockData();
					p.setMonth(sp.getStockdate());
					p.setOpen(sp.getStockopen());
					p.setHigh(sp.getStockhigh());
					p.setLow(sp.getStocklow());
					p.setClose(sp.getStockclose());
					p.setBargin(sp.getStockvolume());
					productList.add(p);
				}
			}
			else if(dayorWeek==1){
				productList.addAll(StockData.toWeekData(arrayStock));
			}
			else if(dayorWeek==2){
				productList.addAll(StockData.toMonthData(arrayStock));
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

		return productList;
    }  
	/*返回总量分布*/
	@SuppressWarnings("unchecked")
	public static double[] getStockListBargin(ArrayList<StockData> arg0) {
		int high=(int)(arg0.get(0).getHigh()*10);
		int low=(int)(arg0.get(0).getLow()*10);
		double[] important=new double[high-low+1];
		for(int i=0;i<high-low+1;++i){
			important[i]=0;
		}
        for(int i=1;i<arg0.size();++i){
        	int sHigh=(int)(arg0.get(i).getHigh()*10);
        	int sLow=(int)(arg0.get(i).getLow()*10);
        	int sRow=sHigh-sLow+1;
        	int spingjun=(sHigh+sLow)/2;
        	int sNumB=(sRow+1)/2;
			double tempV=Double.valueOf(arg0.get(i).getBargin())/sNumB;
			important[spingjun-low]+=tempV;
			for(int m=1;m<sNumB;m++){
				int xiabiaoUp=spingjun-low+m;
				int xiabiaoDown=spingjun-low-m;
				
				important[xiabiaoUp]+=(sNumB-m)*tempV/sNumB;
				important[xiabiaoDown]+=(sNumB-m)*tempV/sNumB;
			}
        }
        return important;	
    }
	/*画一条线，尽量穿过更多的价格区间*/
	@SuppressWarnings("unchecked")
	public static double[] getStockListJX(ArrayList<StockData> arg0){
		int high=(int)(arg0.get(0).getHigh()*100);
		int low=(int)(arg0.get(0).getLow()*100);
		double[] important=new double[high-low+1];
		for(int i=0;i<high-low+1;++i){
			important[i]=0;
		}
        for(int i=1;i<arg0.size();++i){
        	int sHigh=(int)(arg0.get(i).getHigh()*100);
        	int sLow=(int)(arg0.get(i).getLow()*100);
        	int sRow=sHigh-sLow+1;
        	int spingjun=(sHigh+sLow)/2;
        	int sNumB=(sRow+1)/2;
        	important[spingjun-low]+=1;
			for(int m=1;m<sNumB;m++){
				int xiabiaoUp=spingjun-low+m;
				int xiabiaoDown=spingjun-low-m;
				important[xiabiaoUp]+=1;
				important[xiabiaoDown]+=1;
			}
        }
        return important;
    }
}


