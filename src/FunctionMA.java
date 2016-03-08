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
/*MA*/
public class FunctionMA extends SwingWorker<Integer, Integer>{
	
	private String arg0FunctionMA;
	private ArrayList<GpMA> productList = new ArrayList<GpMA>();
	private int arg0MAday;
	private int arg0MAday1;
	
	public FunctionMA(String arg0,int arg1,int arg2){
		arg0FunctionMA=arg0;
		arg0MAday=arg1;
		arg0MAday1=arg2;
	}
	
	public ArrayList<GpMA> getMA(){
		return productList;
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
		case 34 :
			for(int i=0;i<34;i++){
				temp+=arrayStock.get(m-i).getStockclose();
			}
			break;
		}
		return temp/arg0;	
	}
	
	public void getStockMA(String end,int MAday,int MAday1){
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
        			double avargeMA=0;
        			int sum=0;
        			int sumsum=0;
        			if(arrayXB>34){
        				for(int i=33;i<arrayXB+1;i++){
        					avargeMA=getavargeMA(arrayStock,i,MAday);//MAday MA天数
        					if(arrayStock.get(i).getStockclose()<avargeMA){
        						sum++;
        						sumsum=0;
        					}
        					else if(arrayStock.get(i).getStockclose()>avargeMA){
        						if(i>arrayXB-MAday1&&sum>15){
        							++sumsum;
        							if(i==arrayXB&&sumsum==MAday1){
        								sum+=10000;
        							}
        						}
        						else{
        							sum=0;
        							sumsum=0;
        						}
        					}

        				}
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
            
            dis.close();
        }
		catch (IOException e) {
            e.printStackTrace();
        }
		for(int i=9;i>-1;i--){
			productList.add(finSort[i]);
		}
		
	}

	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		getStockMA(arg0FunctionMA,arg0MAday,arg0MAday1);
		return 0;
	}
}

class PresMAComparator implements Comparator {
    public int compare(Object op1, Object op2) {
    	GpMA eOp1 = (GpMA) op1;
    	GpMA eOp2 = (GpMA) op2;
   
       return eOp1.getPres()-eOp2.getPres();
    }
}

class GpMA {
    private String pRes;
    private String date;
    private String sNum;
    
    public GpMA() {
        this.pRes = "10";
        this.sNum=null;
        this.date=null;
    }
    
    public String getDate(){
    	return date;
    }
    public void setDate(String arg0){
    	date=arg0;
    }
    public String getsNum() {
        return sNum;
    }

    public void setsNum(String num) {
    	this.sNum = num;
    }

    public int getPres() {
        return Integer.valueOf(pRes);
    }

    public void setPres(int pRes) {
    	this.pRes = String.valueOf(pRes);
    }
}
