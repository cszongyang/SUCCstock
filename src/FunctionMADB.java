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

import javax.swing.SwingWorker;
/*MA����
 * 
 * ǰ��5����С��ʮ���ߣ����һ�����̼�վ��ʮ��������
 * */
public class FunctionMADB extends SwingWorker<Integer, Integer>{
	
	private String arg0FunctionMADB;
	private int type=0;
	private ArrayList<GpMA> productList = new ArrayList<GpMA>();
	
	public FunctionMADB(String arg0,int arg1){
		arg0FunctionMADB=arg0;
		type=arg1;
	}
	
	public ArrayList<GpMA> getMA(){
		return productList;
	}

	
	public void getStockMA(String end,int type){
		int _count=0;
		
		GpMA[] finSort=new GpMA[10];
        for(int m=0;m<10;m++){
        	finSort[m]=new GpMA();
        }
		//System.out.println(endData);
        
		File f = new File("e:\\number.txt");
		try {
            BufferedReader dis = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = dis.readLine()) != null){
                String[] arr = line.trim().split("\\s+");
        		Stock stock= new Stock( arr[0].toString(),Function.getStartTime(end),end);
        		ArrayList<StockPrice> arrayStock;
        		int errNumber;
        		errNumber=stock.open();
        		if (errNumber==1){
        			arrayStock=new ArrayList<StockPrice>();
        			arrayStock=stock.getArrayListStock();
        			int arrayXB=arrayStock.size()-1;
        			int sum=0;
        			if(type==0){
        				sum=dayF(arrayStock);
        			}
        			else if(type==1){
        				sum=weekF(StockData.toWeekData(arrayStock));
        			}
        			else if(type==2){
        				sum=weekF(StockData.toMonthData(arrayStock));
        			}
        			if(sum>finSort[0].getPres()){
            			finSort[0].setPres(sum);
            			finSort[0].setsNum(arr[0]);
            			finSort[0].setDate(arrayStock.get(arrayXB).getStockdate().toString());
            			Arrays.sort(finSort,new PresMAComparator());
            		//	System.out.println(finSort[0].getPres());
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
                setProgress(_count*100/2704);
            }
            
            dis.close();
        }
		catch (IOException e) {
            e.printStackTrace();
        }
		for(int i=9;i>-1;i--){
			productList.add(finSort[i]);
		}
		
	}

	private int dayF(ArrayList<StockPrice> arrayStock){
		int arrayXB=arrayStock.size()-1;
		double avargeMA5=0;
		double avargeMA10=0;
		int sum=0;
		if(arrayXB>10){
			for(int i=9;i<arrayXB+1;i++){
				avargeMA5=(arrayStock.get(i).getStockclose()+arrayStock.get(i-1).getStockclose()+arrayStock.get(i-2).getStockclose()+arrayStock.get(i-3).getStockclose()+arrayStock.get(i-4).getStockclose())/5;
				avargeMA10=(arrayStock.get(i).getStockclose()+arrayStock.get(i-1).getStockclose()+arrayStock.get(i-2).getStockclose()+arrayStock.get(i-3).getStockclose()+arrayStock.get(i-4).getStockclose()+arrayStock.get(i-5).getStockclose()+arrayStock.get(i-6).getStockclose()+arrayStock.get(i-7).getStockclose()+arrayStock.get(i-8).getStockclose()+arrayStock.get(i-9).getStockclose())/10;
				if(avargeMA5<avargeMA10){
					sum++;
					if(i==arrayXB&&sum>15&&arrayStock.get(i).getStockclose()>avargeMA10){
						sum+=10000;
					}
				}
				else if(avargeMA5>avargeMA10){
					if(i==arrayXB&&sum>15&&arrayStock.get(i).getStockclose()>avargeMA10){
						sum+=10000;
					}
					else{
						sum=0;}
				}

			}
		}
		return sum;
		
	}
	
	private int weekF(ArrayList<StockData> arrayStock){
		int arrayXB=arrayStock.size()-1;
		double avargeMA5=0;
		double avargeMA10=0;
		int sum=0;
		if(arrayXB>10){
			for(int i=9;i<arrayXB+1;i++){
				avargeMA5=(arrayStock.get(i).getClose()+arrayStock.get(i-1).getClose()+arrayStock.get(i-2).getClose()+arrayStock.get(i-3).getClose()+arrayStock.get(i-4).getClose())/5;
				avargeMA10=(arrayStock.get(i).getClose()+arrayStock.get(i-1).getClose()+arrayStock.get(i-2).getClose()+arrayStock.get(i-3).getClose()+arrayStock.get(i-4).getClose()+arrayStock.get(i-5).getClose()+arrayStock.get(i-6).getClose()+arrayStock.get(i-7).getClose()+arrayStock.get(i-8).getClose()+arrayStock.get(i-9).getClose())/10;
				if(avargeMA5<avargeMA10){
					sum++;
					if(i==arrayXB&&sum>15&&arrayStock.get(i).getClose()>avargeMA10){
						sum+=10000;
					}
				}
				else if(avargeMA5>avargeMA10){
					if(i==arrayXB&&sum>15&&arrayStock.get(i).getClose()>avargeMA10){
						sum+=10000;
					}
					else{
						sum=0;}
				}

			}
		}
		return sum;
		
	}
	
	
	private int monthF(ArrayList<StockData> arrayStock){
		int arrayXB=arrayStock.size()-1;
		double avargeMA5=0;
		double avargeMA10=0;
		int sum=0;
		if(arrayXB>10){
			for(int i=9;i<arrayXB+1;i++){
				avargeMA5=(arrayStock.get(i).getClose()+arrayStock.get(i-1).getClose()+arrayStock.get(i-2).getClose()+arrayStock.get(i-3).getClose()+arrayStock.get(i-4).getClose())/5;
				avargeMA10=(arrayStock.get(i).getClose()+arrayStock.get(i-1).getClose()+arrayStock.get(i-2).getClose()+arrayStock.get(i-3).getClose()+arrayStock.get(i-4).getClose()+arrayStock.get(i-5).getClose()+arrayStock.get(i-6).getClose()+arrayStock.get(i-7).getClose()+arrayStock.get(i-8).getClose()+arrayStock.get(i-9).getClose())/10;
				if(avargeMA5<avargeMA10){
					sum++;
					if(i==arrayXB&&sum>15&&arrayStock.get(i).getClose()>avargeMA10){
						sum+=10000;
					}
				}
				else if(avargeMA5>avargeMA10){
					if(i==arrayXB&&sum>15&&arrayStock.get(i).getClose()>avargeMA10){
						sum+=10000;
					}
					else{
						sum=0;}
				}

			}
		}
		return sum;
		
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		getStockMA(arg0FunctionMADB,type);
		return 0;
	}
}