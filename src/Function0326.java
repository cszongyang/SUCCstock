package p1;

import hebtu.myh.Stock;
import hebtu.myh.StockPrice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.SwingWorker;
/*����
 * 
 * ָ�����ڵ���������ǰ���������͵��̣����Խ������Խ��ǰ
 * 
 * */
public class Function0326 extends SwingWorker<Integer, Integer>{
	private ArrayList<Gp0326> productList = new ArrayList<Gp0326>();
	private String arg0start;
	private String arg0end;
	private String arg0data;
	
	public Function0326(String s,String e,String d){
		arg0start=s;
		arg0end=e;
		arg0data=d;
	}
	
	public ArrayList<Gp0326> get0326(){
		return productList;
	}
	@SuppressWarnings("unchecked")
	public void getStock0326List(String start,String end,String data) {
		 int _count=0;
		 Gp0326[] finSort=new Gp0326[50];
         for(int i=0;i<50;i++){
         	finSort[i]=new Gp0326();
         }
		double[] pV;
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
        			if(arrayStock.size()>10){
        				int MAXhigh=(int)(stock.MAXhigh*10);
        				int MINlow=(int)(stock.MINlow*10);
        				int row=MAXhigh-MINlow+1;
        				int low=MINlow;
        				pV=new double[row];
        				for(int i=0;i<row;i++){
        					pV[i]=0.0;
        				}
        				double sum=0.0;
        				int sFUCHHIGH=0;
        				int SFUCKLOW=0;
        				for(StockPrice sp:arrayStock){
        					int sRow=(int)(sp.getStockhigh()*10)-(int)(sp.getStocklow()*10)+1;
        					int fuckHigh=(int)((sp.getStockhigh()*10));
        					int fuckLow=(int)((sp.getStocklow()*10));
        					int spingjun=(fuckHigh+fuckLow)/2;
        					int sNumB=(sRow+1)/2;
        					double tempV=Double.valueOf(sp.getStockvolume())/sNumB;
        					pV[spingjun-low]+=tempV;
        					for(int i=1;i<sNumB;i++){
        						int xiaobiaoUp=spingjun-low+i;
        						int xiaobiaoDown=spingjun-low-i;
        						
        						pV[xiaobiaoUp]+=(sNumB-i)*tempV/sNumB;
        						pV[xiaobiaoDown]+=(sNumB-i)*tempV/sNumB;
        					}
        					
        					sum+=Double.valueOf(sp.getStockvolume());
        					//������Ǹ����ڵ�
        					if(sp.getStockdate().toString().equals(data)){
        						sFUCHHIGH=fuckHigh;
        						SFUCKLOW=fuckLow;
        					}
        				}
        				double max=0;
        				double tempSum=0;

        				for(int mm=SFUCKLOW-low;mm<sFUCHHIGH-low;mm++){
        					tempSum+=pV[mm];
        					if(tempSum/sum>max){
        						max=tempSum/sum;
        					}
        				}
        				if(max>finSort[0].getPres()){
        					finSort[0].setPres(max);
        					finSort[0].setsNum(arr[0].toString());
        					Arrays.sort(finSort,new Pres0326Comparator());
        				}
        				
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
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
            
       }catch(Exception e){}
        }
		for(int i=49;i>-1;--i){
			productList.add(finSort[i]);
        }
	
    }

	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		getStock0326List(arg0start,arg0end,arg0data);
		return 0;
	}
}
class Gp0326 {
    private double pRes;
 
    private String sNum;
    
    public Gp0326() {
        this.pRes = 0.0;
        this.sNum=null;
    }
    public String getsNum() {
        return sNum;
    }

    public void setsNum(String num) {
    	this.sNum = num;
    }

    public double getPres() {
        return pRes;
    }

    public void setPres(double pRes) {
    	this.pRes = pRes;
    }
}


class Pres0326Comparator implements Comparator {
    public int compare(Object op1, Object op2) {
    	Gp0326 eOp1 = (Gp0326) op1;
    	Gp0326 eOp2 = (Gp0326) op2;
   
       return eOp1.getPres()>eOp2.getPres()?1:eOp1.getPres()<eOp2.getPres()?-1:0;
    }
}
