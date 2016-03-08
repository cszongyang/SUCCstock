package p1;

public class StockXS {
	private String name;
	private String startDate;
	private String endDate;
	private double xiangSi;
	
	public StockXS(){
		name=null;
		startDate=null;
		endDate=null;
		xiangSi=0;
	}
	
	public void setName(String arg0){
		name=arg0;
	}
	public String getName(){
		return name;
	}
	
	public void setStart(String arg0){
		startDate=arg0;
	}
	public String getStart(){
		return startDate;
	}
	
	public void setEnd(String arg0){
		endDate=arg0;
	}
	
	public String getEnd(){
		return endDate;
	}
	
	public void setXS(double arg0){
		xiangSi=arg0;
	}
	
	public double getXS(){
		return xiangSi;
	}
}
