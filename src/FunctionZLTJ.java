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
/*总量统计*/
public class FunctionZLTJ extends SwingWorker<Integer, Integer>{
	
	private String arg0FunctionZLTJ;
	private String arg2FunctionZLTJ;
	private int arg1daycount;
	private double arg3biaozhun;
	private ArrayList<GpZLTJ> productList = new ArrayList<GpZLTJ>();
	
	public FunctionZLTJ(String arg0,int arg1,String arg2,double arg3){
		arg0FunctionZLTJ=arg0;
		arg1daycount=arg1;
		arg2FunctionZLTJ=arg2;
		arg3biaozhun=arg3;
	}
	
	public ArrayList<GpZLTJ> getZLTJ(){
		return productList;
	}

	
	public  void getStockZLTJ(String start,int daycount,String end,double biaozhunarg){
		int _count=0;
		Calendar cal=Calendar.getInstance();
		GpZLTJ[] finSort=new GpZLTJ[10];
        for(int m=0;m<10;m++){
        	finSort[m]=new GpZLTJ();
        }
		//System.out.println(endData);
        
		File f = new File("e:\\number.txt");
		try {
            BufferedReader dis = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = dis.readLine()) != null){
                String[] arr = line.trim().split("\\s+");
        		Stock stock= new Stock( arr[0].toString(),start,end);
        		ArrayList<StockPrice> arrayStock;
        		int errNumber;
        		errNumber=stock.open();
        		if (errNumber==1){
        			arrayStock=new ArrayList<StockPrice>();
        			arrayStock=stock.getArrayListStock();
        			int arrayXB=arrayStock.size()-1;
        			double volumeSum=1;
        			double volumeToday=0;
        			double result=1.0;
        			double stockMax=stock.MAXhigh;
        			double stockMin=stock.MINlow;
        			double biaoZhun=((stockMax-stockMin)/100)*biaozhunarg+stockMin;
        			if(arrayXB>30){
        				for(int i=arrayXB-daycount;i<arrayXB+1;i++){
        					double tempopen=arrayStock.get(i).getStockopen();
        					double tempclose=arrayStock.get(i).getStockclose();
        					if(tempopen>tempclose&&(tempopen-tempclose)/tempopen<0.096){
        						volumeSum+=arrayStock.get(i).getStockvolume();
        					}
        					else if(tempopen<tempclose&&(tempclose-tempopen)/tempopen<0.096){
        						volumeSum+=arrayStock.get(i).getStockvolume();
        					}	
        				}
        				volumeToday=arrayStock.get(arrayXB).getStockvolume();
        				if(volumeToday>0){
        				result=volumeToday/volumeSum;
        				}
        			
        			if(result<finSort[0].getPres()&&arrayStock.get(arrayXB).getStockopen()<=biaoZhun&&arrayStock.get(arrayXB).getStockclose()>biaoZhun){
            			finSort[0].setPres(result);
            			finSort[0].setsNum(arr[0]);
            			finSort[0].setDate(arrayStock.get(arrayXB).getStockdate().toString());
            			Arrays.sort(finSort,new PresZLTJComparator());
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
            
            dis.close();
        }
		catch (IOException e) {
            e.printStackTrace();
        }
		for(int i=9;i>-1;--i){
			productList.add(finSort[i]);
		}
		
	}

	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		getStockZLTJ(arg0FunctionZLTJ,arg1daycount,arg2FunctionZLTJ,arg3biaozhun);
		return 0;
	}
}

class PresZLTJComparator implements Comparator {
    public int compare(Object op1, Object op2) {
    	GpZLTJ eOp1 = (GpZLTJ) op1;
    	GpZLTJ eOp2 = (GpZLTJ) op2;
   
    	 return eOp1.getPres()<eOp2.getPres()?1:eOp1.getPres()>eOp2.getPres()?-1:0;
    }
}

class GpZLTJ {
    private String pRes;
    private String date;
    private String sNum;
    
    public GpZLTJ() {
        this.pRes = "1.0";
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

    public double getPres() {
        return Double.valueOf(pRes);
    }

    public void setPres(double pRes) {
    	this.pRes = String.valueOf(pRes);
    }
}
