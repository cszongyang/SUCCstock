package p1;

import hebtu.myh.Stock;
import hebtu.myh.StockPrice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.SwingWorker;
/*最低价连续上升*/
public class FunctionLowCU extends SwingWorker<Integer, Integer>{
	private int ContinueUp=0;
	private String endDate=null;
	private int type=0;
	ArrayList<GpLCU> resultList=new ArrayList<GpLCU>();
	public FunctionLowCU(int arg0,String arg1,int arg2){
		ContinueUp=arg0;
		endDate=arg1;
		type=arg2;
	}
	public ArrayList<GpLCU> getResultList(){
		return resultList;
	}
	private boolean isTrueLCU(ArrayList<StockPrice> arrayStock,int ContinueUp){
		int xiabiao=arrayStock.size()-1;
		if(xiabiao<ContinueUp+1){
			return false;
		}
		for(int i=0;i<ContinueUp;++i){
				if(arrayStock.get(xiabiao-i).getStocklow()>arrayStock.get(xiabiao-i-1).getStocklow()){	
				}
				else{return false;}
		}
		return true;
	}
	
	private boolean isTrueLCUWeek(ArrayList<StockData> arrayStock,int ContinueUp){
		int xiabiao=arrayStock.size()-1;
		if(xiabiao<ContinueUp+1){
			return false;
		}
		for(int i=0;i<ContinueUp;++i){
				if(arrayStock.get(xiabiao-i).getLow()>arrayStock.get(xiabiao-i-1).getLow()){	
				}
				else{return false;}
		}
		return true;
	}
	public void getResult(){
		int _count=0;
		File f = new File("e:\\number.txt");
		try {
            BufferedReader dis = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line = dis.readLine()) != null){
                String[] arr = line.trim().split("\\s+");
        		Stock stock= new Stock( arr[0].toString(),Function.getStartTime(endDate),endDate);
        		ArrayList<StockPrice> arrayStock;
        		int errNumber;
        		errNumber=stock.open();
        		if (errNumber==1){
        			arrayStock=new ArrayList<StockPrice>();
        			arrayStock=stock.getArrayListStock();
        			if(type==0&&isTrueLCU(arrayStock,ContinueUp)){
        				GpLCU temp=new GpLCU();
        				temp.setsNum(arr[0].toString());
        				temp.setPres(ContinueUp);
        				temp.setDate(endDate);
        				resultList.add(temp);
        			}
        			if(type==1&&isTrueLCUWeek(StockData.toWeekData(arrayStock),ContinueUp)){
        				GpLCU temp=new GpLCU();
        				temp.setsNum(arr[0].toString());
        				temp.setPres(ContinueUp);
        				temp.setDate(endDate);
        				resultList.add(temp);
        			}
        			if(type==2&&isTrueLCUWeek(StockData.toMonthData(arrayStock),ContinueUp)){
        				GpLCU temp=new GpLCU();
        				temp.setsNum(arr[0].toString());
        				temp.setPres(ContinueUp);
        				temp.setDate(endDate);
        				resultList.add(temp);
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
		
		resultList.trimToSize();
	}
	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		getResult();
		return 0;
	}
}

class GpLCU {
    private String pRes;
    private String date;
    private String sNum;
    
    public GpLCU() {
        this.pRes=null;
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