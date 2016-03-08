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
/*ͳ��MA����
 * ���û�������ݿ�
 * ǰ��5����С��ʮ���ߣ����һ�����̼�վ��ʮ��������
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
        					if(sum>10015){//���������ˣ������Ʊ
        						//System.out.println("�����Ʊ");
        						double todayOpen=arrayStock.get(i+1).getStockopen();//�ڶ��쿪�̼�����
								magic=getMAXOpen(arrayStock,i+2);//��δ��25�������յ����ֵ
								if(todayOpen<magic[0]){//׬Ǯ��
									winP+=(magic[0]-todayOpen)/todayOpen;
									++winCount;
								}
								else{//û׬Ǯ
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
					System.out.println("ִ�гɹ�,��û��Ҫ��ȡ���κ����ݡ�");
				}
				else if(errNumber==-1){
					System.out.println("�������ڸ�ʽ����ȷ��");
				}
				else if(errNumber==-2){
					System.out.println("�����ݿ�����ӳ������⡣");
				}
				else if(errNumber==-3){
					System.out.println("SQL���ִ�г��ִ���");
				}
				else if(errNumber==-4){
					System.out.println("��鿴���������Ƿ���������");
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