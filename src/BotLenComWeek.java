package p1;

import hebtu.myh.Stock;
import hebtu.myh.StockPrice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.SwingWorker;
/*周锤子
 * 同锤子
 * */
public class BotLenComWeek extends SwingWorker<Integer, Integer>{
	private String arg0Enddate=null;
	private int day_count=0;
	BotLenComWeek(String arg0,int arg1){
		arg0Enddate=arg0;
		day_count=arg1;
	}
	ArrayList<UnitB> productList = new ArrayList<UnitB>();
	ArrayList<UnitB> productList1 = new ArrayList<UnitB>();
	ArrayList<UnitB> productList2 = new ArrayList<UnitB>();
	ArrayList<UnitB> productList3 = new ArrayList<UnitB>();
	public ArrayList<UnitB> getBotLenCom(){
		return productList;
	}
	
	
	private double getLow(ArrayList<StockData> arrayStock,int arg1){
		int xiabiao=arrayStock.size()-1;
		double min=9999999;
		for(int i=0;i<arg1;++i){
			min=Math.min(arrayStock.get(xiabiao-i).getLow(),min);
		}
		return min;
	}
	
	public void GetBotLenCom(){
		File f = new File("e:\\number.txt");
		try{
			BufferedReader dis = new BufferedReader(new FileReader(f));
			String line = null;
			int _count=0;
			while ((line = dis.readLine()) != null){
				String[] arr = line.trim().split("\\s+");
				Stock stock= new Stock( arr[0].toString(),Function.getStartTime(arg0Enddate),arg0Enddate);
				ArrayList<StockPrice> arrayStock1;
				ArrayList<StockData> arrayStock;
				int errNumber;
				errNumber=stock.open();
				if (errNumber==1){
					arrayStock1=new ArrayList<StockPrice>();
        			arrayStock1=stock.getArrayListStock();
        			arrayStock=StockData.toWeekData(arrayStock1);
        			int arrayXB=arrayStock.size()-1;
        			if(arrayXB>day_count){
        				double open=arrayStock.get(arrayXB).getOpen();
        				double close=arrayStock.get(arrayXB).getClose();
        				double low=arrayStock.get(arrayXB).getLow();
        				double high=arrayStock.get(arrayXB).getHigh();
        				double preclose=arrayStock.get(arrayXB-1).getClose();
        				String date=arrayStock.get(arrayXB).getMonth().toString();
        				if(getLow(arrayStock,day_count)==low&&close>preclose){
        					if(Math.abs(open-close)>0.003*close&&(high-Math.max(open, close))<Math.abs(open- close)){
        						if(close>open&&(open-low)>2*(close-open)){//阳实体
        							UnitB temp=new UnitB();
        							temp.SetsNum(arr[0].toString());
        							temp.Setkey((open-low)/(close-open));
        							temp.Setdate(date);
        							temp.setType("阳实体");
        							productList1.add(temp);
        						}
        						else if(close<open&&(close-low)>2*(open-close)){//阴实体
        							UnitB temp=new UnitB();
        							temp.SetsNum(arr[0].toString());
        							temp.Setkey((close-low)/(open-close));
        							temp.Setdate(date);
        							temp.setType("阴实体");
        							productList2.add(temp);
        						}
        					}
        					else{
        						if(((close-low)/(high-low))>0.7&&(high-low)>0.02*close){
                					UnitB temp=new UnitB();
            						temp.SetsNum(arr[0].toString());
            						temp.Setkey((close-low)/close);
            						temp.Setdate(date);
            						temp.setType("十字");
            						productList3.add(temp);
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
				_count++;
				setProgress(_count*100/2704);
			}	
			//排序
			productList1.trimToSize();
			productList2.trimToSize();
			productList3.trimToSize();
			
			Collections.sort(productList1, new PresCOMComparator());
			Collections.sort(productList2, new PresCOMComparator());
			Collections.sort(productList3, new PresCOMComparator());
			
			productList.addAll(productList1);
			productList.addAll(productList2);
			productList.addAll(productList3);
			
			productList.trimToSize();
			
		}
		
		catch (IOException e) {
            e.printStackTrace();
        }
		}

	@Override
		protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		GetBotLenCom();
		return 0;
	}

}





