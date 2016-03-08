
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
/*锤子
 * 
 * 局部最低价，当日收盘大于前一天收盘，实体部分大于0.3%并且上引小于实体，下引大于两倍实体。则认为符合条件（包括阳实体，阴实体）
 * 十字型约束条件下引线大于0.7倍的最高减最低并且最高价减最低价大于0.02倍的收盘价
 * 
 * */
public class BotLenCom extends SwingWorker<Integer, Integer>{
	
	private String arg0Enddate=null;
	private int day_count=0;
	BotLenCom(String arg0,int arg1){
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
	
	
	private double getLow(ArrayList<StockPrice> arrayStock,int arg1){
		int xiabiao=arrayStock.size()-1;
		double min=9999999;
		for(int i=0;i<arg1;++i){
			min=Math.min(arrayStock.get(xiabiao-i).getStocklow(),min);
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
				ArrayList<StockPrice> arrayStock;
				int errNumber;
				errNumber=stock.open();
				if (errNumber==1){
					arrayStock=new ArrayList<StockPrice>();
        			arrayStock=stock.getArrayListStock();
        			int arrayXB=arrayStock.size()-1;
        			if(arrayXB>day_count){
        				double open=arrayStock.get(arrayXB).getStockopen();
        				double close=arrayStock.get(arrayXB).getStockclose();
        				double low=arrayStock.get(arrayXB).getStocklow();
        				double high=arrayStock.get(arrayXB).getStockhigh();
        				double preclose=arrayStock.get(arrayXB-1).getStockclose();
        				String date=arrayStock.get(arrayXB).getStockdate().toString();
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
                				if(((close-low)/(high-low))>0.7&&(high-low)>0.02*close){//十字
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

class PresCOMComparator implements Comparator {
    public int compare(Object op1, Object op2) {
    	UnitB eOp1 = (UnitB) op1;
    	UnitB eOp2 = (UnitB) op2;
   
       return eOp1.Getkey()>eOp2.Getkey()?-1:eOp1.Getkey()<eOp2.Getkey()?1:0;
    }
}

class UnitB{
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
}
