package p1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.SwingWorker;
/*相似，皮尔森算法，这个还没有添加到图形界面*/
public class FunctionXS extends SwingWorker<Integer, Integer>{
	
	private double[] arg0FunctionXS;
	private ArrayList<StockXS> productListnb = new ArrayList<StockXS>();
	
	public ArrayList<StockXS> getStockXS(){
		return productListnb;
	}
	
	public FunctionXS(double[] arg0){
		arg0FunctionXS=arg0;
	}
	
	@SuppressWarnings("unchecked")
	public  void getStockXS(double[] s){
		int _count=0;
		double sumx1=0.0;
		double sumx2=0.0;
		int datalength=s.length;
		int datalength6=s.length*6;
		double[] datax=new double[datalength]; 
		for(int i=0;i<datalength;i++){
			datax[i]=s[i];
		}
		sumx1=FunctionXS.pccSumX1(datax);
		sumx2=FunctionXS.pccSumX2(datax);
		File f = new File("e:\\gData.txt");
		
		
		int y=1;
		double res;
		StockXS[] finSort=new StockXS[10];
		for(int i=0;i<10;++i){
			finSort[i]=new StockXS();
		}
		try {
            BufferedReader dis = new BufferedReader(new FileReader(f),1024*1024*10);
            String line = null;               
            while ((line = dis.readLine()) != null){ 
            	int x=2;
            	String[] arr = line.split(",");
            	int slength=(arr.length)/6;
            	while(datalength+x<=slength){
            		res=FunctionXS.pccsF(datax, arr, sumx1, sumx2,x);
            		if(res>finSort[0].getXS()&&res<1.2f){
            			finSort[0].setXS(res);
            			finSort[0].setName(arr[0]);
            			finSort[0].setStart(arr[x-1]);
            			finSort[0].setEnd(arr[x-7+datalength6]);
               			Arrays.sort(finSort,new PresComparator());
            		}
            		x+=6;
            		y++;
            	}
            	
            	_count++;
        		setProgress(_count*100/2704);
            }
            for(int i=9;i>-1;--i){
            	productListnb.add(finSort[i]);
            }

            dis.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	public static double pccSumX1(double[] datax){
		int l=datax.length;
		double sumx1 =0.0;
		for(int a=0;a<l;a++){
			sumx1+=datax[a];
		}
		return sumx1;
	}
	public static double pccSumX2(double[] x){
		int l=x.length;
		double sumx2 =0.0;
		for(int a=0;a<l;a++){
			sumx2+=x[a]*x[a];
		}
		return sumx2;
	}
	
	public static double pccsF(double[] x,String[] s,double sumx1,double sumx2,int gDataX){
		int l=x.length;
		double sumy1=0.0;
		double sumy2=0.0;
		double sumxy=0.0;
		for(int a=0;a<l;a++){
			sumy1+=Double.valueOf(s[gDataX+6*a]);
			sumy2+=Double.valueOf(s[gDataX+6*a])*Double.valueOf(s[gDataX+6*a]);
			sumxy+=x[a]*Double.valueOf(s[gDataX+6*a]);
		}
		return ((sumxy-sumx1*sumy1/l)/Math.sqrt((sumx2-sumx1*sumx1/l)*(sumy2-sumy1*sumy1/l)));
	}

	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		getStockXS(arg0FunctionXS);
		return 0;
	}
	
}


class PresComparator implements Comparator {
    public int compare(Object op1, Object op2) {
    	StockXS eOp1 = (StockXS) op1;
    	StockXS eOp2 = (StockXS) op2;
        
        return eOp1.getXS()>eOp2.getXS()?1:eOp1.getXS()<eOp2.getXS()?-1:0;
    }
}
