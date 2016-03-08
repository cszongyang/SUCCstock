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
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.SwingWorker;
/*阴坡阳
 * 
 * 前一天是阴线（有一定实体0.03），后一天是阳线，收盘价大于前一天开盘价，大的越多，排名越靠前
 * 
 * 
 * 
 * */
public class Backward extends SwingWorker<Integer, Integer>{
	
	ArrayList<Unit> productList1 = new ArrayList<Unit>();
	public ArrayList<Unit> getBotLenCom(){
		return productList1;
	}
	
	public  void GetBackward(){

		String endData=null;
		Calendar cal=Calendar.getInstance();
		Unit[] finSort=new Unit[20];
        for(int m=0;m<20;m++){
        	finSort[m]=new Unit();
        }
		
		if(cal.get(Calendar.MONTH)+1<10){
			endData=String.valueOf(cal.get(Calendar.YEAR))+"-0"+String.valueOf(cal.get(Calendar.MONTH)+1)+"-"+String.valueOf(cal.get(Calendar.DATE));
			}
			else{endData=String.valueOf(cal.get(Calendar.YEAR))+"-"+String.valueOf(cal.get(Calendar.MONTH)+1)+"-"+String.valueOf(cal.get(Calendar.DATE));}	File f = new File("e:\\number.txt");
		try{
			BufferedReader dis = new BufferedReader(new FileReader(f));
			String line = null;
			int _count=0;
			while ((line = dis.readLine()) != null){
				String[] arr = line.trim().split("\\s+");
				Stock stock= new Stock( arr[0].toString(),Function.getStartTime(),endData);
				ArrayList<StockPrice> arrayStock;
				int errNumber;
				errNumber=stock.open();
				if (errNumber==1){
					arrayStock=new ArrayList<StockPrice>();
        			arrayStock=stock.getArrayListStock();
        			int arraySZ=arrayStock.size()-1;
        			if(arraySZ>0){
        				double preopen=arrayStock.get(arraySZ-1).getStockopen();
            			double preclose=arrayStock.get(arraySZ-1).getStockclose();
            			double close=arrayStock.get(arraySZ).getStockclose();
            			double open=arrayStock.get(arraySZ).getStockopen(); 
            			Date date=arrayStock.get(arraySZ).getStockdate();
            			double temp1=preopen-preclose;
            			double temp2=close-preopen;
            			double temp3=close-open;
            			double temp4=temp2/open;
            			if(temp1>0.03*preopen&&temp4>0&&temp3>0){
            				finSort[0].SetsNum(arr[0]);
                			finSort[0].Setkey(temp4);
                			finSort[0].Setdate(date.toString());
                			Arrays.sort(finSort,new PresBACKComparator());
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
		}
		
		catch (IOException e) {
            e.printStackTrace();
        }

		for(int i=19;i>-1;i--){
			productList1.add(finSort[i]);
		}
		}

	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		GetBackward();
		return 0;
	}
}
/*比较方法*/
class PresBACKComparator implements Comparator {
    public int compare(Object op1, Object op2) {
    	Unit eOp1 = (Unit) op1;
    	Unit eOp2 = (Unit) op2;
   
       return eOp1.Getkey()>eOp2.Getkey()?1:eOp1.Getkey()<eOp2.Getkey()?-1:0;
    }
}

class Unit{
	private String date;
	private double key;
	private String sNum;
	
	public Unit(){
		date=null;
		key=0.0;
		sNum=null;
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
}
