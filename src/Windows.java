package p1;

import hebtu.myh.StockPrice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/* 图形界面写的比较乱，界面布局用的是最基本的布局方法，当时只注重功能实现，并没有对代码进行优化。
 * 图形界面都在这个类里面
 * 其余的每个类都对应一个方法
 * 
 * */
public class Windows extends JFrame{
	
	/*panel 系列*/
	JTabbedPane jtpFigures=new JTabbedPane();//左边那个（各种按钮）
	JTabbedPane jtpFigure2=new JTabbedPane();//右边那个（各种table）
	JProgressBar jproBar1=new JProgressBar();//进度条
	JPanel jpBarandF=new JPanel();
	JPanel jp1=new JPanel();//MA
	JPanel jp2=new JPanel();//总量
	JPanel jp3=new JPanel();//相似
	JPanel jp4=new JPanel();//总量比例
	JPanel jp5=new JPanel();//MA最高
	JPanel jp6=new JPanel();//MA收盘下穿
	JPanel jp7=new JPanel();//锤子-阴坡阳
	JPanel jp8=new JPanel();//最低价连续上涨
	JPanel jp9=new JPanel();//MA单边
	JPanel jPmain=new JPanel();
	JPanel jPup=new JPanel();
	JPanel jPupdown=new JPanel();
	JPanel jPfuzagongneng=new JPanel();
	JPanel jPce=new JPanel();
	StockDataPanel kDataPanel=new StockDataPanel();//股票视图
	StockBarginPanel kBarginPanel=new StockBarginPanel();//股票成交量视图
	
	/*下拉列表框*/
	JComboBox<Integer> jcbMAday=new JComboBox<Integer>(new Integer[]{8,10,15,34});
	JComboBox<Integer> jcbMAday1=new JComboBox<Integer>(new Integer[]{1,2,3,4,5,6,7,8});
	JComboBox<Integer> jcbMAday2=new JComboBox<Integer>(new Integer[]{8,10,15});
	JComboBox<Integer> jcbMAday21=new JComboBox<Integer>(new Integer[]{1,2,3,4,5,6,7,8});
	JComboBox<Integer> jcbMAday3=new JComboBox<Integer>(new Integer[]{8,10,15});
	JComboBox<Integer> jcbMAday31=new JComboBox<Integer>(new Integer[]{1,2,3,4,5,6,7,8});
	
	JComboBox<String> jcbDate=new JComboBox<String>(new String[]{"半年","一年","两年","全部"});
	
	JComboBox<String> jcbDayorWeek=new JComboBox<String>(new String[]{"日线","周线","月线"});
	
	JComboBox<String> jcbLCU=new JComboBox<String>(new String[]{"日线","周线","月线"});
	
	JComboBox<String> jcbMADB=new JComboBox<String>(new String[]{"日线","周线","月线"});
	
	
	/*JTable*/
	/*MA*/
	String[] columnNamesMA={"股票号","值","日期"};
	DefaultTableModel tableModelMA= new DefaultTableModel(null,columnNamesMA);
	JTable jTableMA=new JTable(tableModelMA);
	TableColumnModel tableColumnModelMA=jTableMA.getColumnModel();
	ListSelectionModel listSelectionModelMA=jTableMA.getSelectionModel();
	
	/*MA单边*/
	String[] columnNamesMADB={"股票号","值","日期"};
	DefaultTableModel tableModelMADB= new DefaultTableModel(null,columnNamesMADB);
	JTable jTableMADB=new JTable(tableModelMADB);
	TableColumnModel tableColumnModelMADB=jTableMADB.getColumnModel();
	ListSelectionModel listSelectionModelMADB=jTableMADB.getSelectionModel();
	
	/*总量比例*/
	String[] columnNames0326={"股票号","查询日期值"};
	DefaultTableModel tableModel0326= new DefaultTableModel(null,columnNames0326);
	JTable jTable0326=new JTable(tableModel0326);
	TableColumnModel tableColumnModel0326=jTable0326.getColumnModel();
	ListSelectionModel listSelectionModel0326=jTable0326.getSelectionModel();
	
	/*总量统计*/
	String[] columnNamesZLTJ={"股票号","查询日期值","日期"};
	DefaultTableModel tableModelZLTJ= new DefaultTableModel(null,columnNamesZLTJ);
	JTable jTableZLTJ=new JTable(tableModelZLTJ);
	TableColumnModel tableColumnModelZLTJ=jTableZLTJ.getColumnModel();
	ListSelectionModel listSelectionModelZLTJ=jTableZLTJ.getSelectionModel();
	/*锤子*/
	String[] columnNamesCOM={"股票号","值","日期","类型"};
	DefaultTableModel tableModelCOM= new DefaultTableModel(null,columnNamesCOM);
	JTable jTableCOM=new JTable(tableModelCOM);
	TableColumnModel tableColumnModelCOM=jTableCOM.getColumnModel();
	ListSelectionModel listSelectionModelCOM=jTableCOM.getSelectionModel();
	/*周锤子*/
	String[] columnNamesCOMWeek={"股票号","值","日期","类型"};
	DefaultTableModel tableModelCOMWeek= new DefaultTableModel(null,columnNamesCOMWeek);
	JTable jTableCOMWeek=new JTable(tableModelCOMWeek);
	TableColumnModel tableColumnModelCOMWeek=jTableCOMWeek.getColumnModel();
	ListSelectionModel listSelectionModelCOMWeek=jTableCOMWeek.getSelectionModel();
	
	/*阴坡阳*/
	String[] columnNamesBACK={"股票号","值","日期"};
	DefaultTableModel tableModelBACK= new DefaultTableModel(null,columnNamesBACK);
	JTable jTableBACK=new JTable(tableModelBACK);
	TableColumnModel tableColumnModelBACK=jTableBACK.getColumnModel();
	ListSelectionModel listSelectionModelBACK=jTableBACK.getSelectionModel();
	
	/*最低价连续上涨*/
	String[] columnNamesLCU={"股票号","值","日期"};
	DefaultTableModel tableModelLCU= new DefaultTableModel(null,columnNamesLCU);
	JTable jTableLCU=new JTable(tableModelLCU);
	TableColumnModel tableColumnModelLCU=jTableLCU.getColumnModel();
	ListSelectionModel listSelectionModelLCU=jTableLCU.getSelectionModel();
	
	
	/*button 系列*/
	JButton jBfind=new JButton("查寻股票");
	JButton jB1MA=new JButton("查寻MA");
	JButton jB1MADB=new JButton("查寻MA单边");
	JButton jB1TJMADB=new JButton("统计MA单边");
	JButton jB1MAmagic=new JButton("统计MA");
	JButton jB1MAmagic5_10=new JButton("统计MA5_10");
	JButton jB1MAmagic1=new JButton("显示统计结果MA");
	JButton jB1MAmagic15_10=new JButton("显示统计结果MA5_10");
	JButton jB2ZL=new JButton("查寻当日价格对应总量");
	JButton jB3XS=new JButton("查寻相似");
	JButton jB4ZLTJ=new JButton("最小总量统计");
	JButton jBchaxuncom=new JButton("锤子");
	JButton jBchaxuncomTJ=new JButton("日锤子统计");
	JButton jBchaxuncomWeek=new JButton("周锤子");
	JButton jBchaxunback=new JButton("阴坡阳");
	JButton jBLCU=new JButton("最低价连续上涨");
	
	/*JTextField 系列*/
	JTextField textNum=new JTextField();
	JTextField textStart=new JTextField();
	JTextField textEnd=new JTextField();
	JTextField text1=new JTextField();
	JTextField text12=new JTextField();
	JTextField text121=new JTextField();
	JTextField text21=new JTextField();
	JTextField text22=new JTextField();
	JTextField text23=new JTextField();
	JTextField text41=new JTextField();
	JTextField text42=new JTextField();
	JTextField text43=new JTextField();
	JTextField text44=new JTextField();
	JTextField text71=new JTextField();
	JTextField text72=new JTextField();
	JTextField text8=new JTextField();
	JTextField text81=new JTextField();
	JTextField text9=new JTextField();
	
	JTextArea jText=new JTextArea();
	
	
	
	private String getStartDateJcb(int arg0) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar cal=Calendar.getInstance();
		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH)+1;
		int day=cal.get(Calendar.DATE);
		String date=null;
		switch(arg0){
		case 0 :
				if(month-6>0){
					date=format.format(format.parse(String.valueOf(year)+"-"+String.valueOf(month-6)+"-"+String.valueOf(day)));	
				}
				else{		
					date=format.format(format.parse(String.valueOf(year-1)+"-"+String.valueOf(month+6)+"-"+String.valueOf(day)));			
				}break;
		case 1 :
				date=format.format(format.parse(String.valueOf(year-1)+"-"+String.valueOf(month)+"-"+String.valueOf(day)));	
			break;
		case 2 :
				date=format.format(format.parse(String.valueOf(year-2)+"-"+String.valueOf(month)+"-"+String.valueOf(day)));	
			break;
		case 3 :
			
				date=format.format(format.parse(String.valueOf(year-20)+"-"+String.valueOf(month)+"-"+String.valueOf(day)));
			break;
		}
		return date;
	}
	
	
	public Windows(){
		text1.setText(Function.getEndTime());
		text9.setText(Function.getEndTime());
		textStart.setText(Function.getStartTime());
		textEnd.setText(Function.getEndTime());
		text41.setText(Function.getStartTime());
		text43.setText(Function.getEndTime());
		text71.setText(Function.getEndTime());
		text81.setText(Function.getEndTime());
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*下面这些是table加的监听*/
		listSelectionModelMA.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int m=jTableMA.getSelectedRow();
				if(m!=-1){
					
					ArrayList<StockData> arg0=null;
					try {
						arg0 = Function.getStockList(tableModelMA.getValueAt(m, 0).toString(),getStartDateJcb(jcbDate.getSelectedIndex()), Function.getEndTime(),jcbDayorWeek.getSelectedIndex());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					kDataPanel.setStockData(arg0,Function.getStockListBargin(arg0),Function.getStockListJX(arg0),Integer.valueOf(jcbMAday.getSelectedItem().toString()));
					kDataPanel.ViewData();
					kBarginPanel.setStockBarginPanel(arg0);
					kBarginPanel.ViewData();
				}
			}
		});
		
		listSelectionModelLCU.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int m=jTableLCU.getSelectedRow();
				if(m!=-1){
					
					ArrayList<StockData> arg0=null;
					try {
						arg0 = Function.getStockList(tableModelLCU.getValueAt(m, 0).toString(),getStartDateJcb(jcbDate.getSelectedIndex()), text81.getText(),jcbDayorWeek.getSelectedIndex());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					kDataPanel.setStockData(arg0,Function.getStockListBargin(arg0),Function.getStockListJX(arg0),Integer.valueOf(jcbMAday.getSelectedItem().toString()));
					kDataPanel.ViewData();
					kBarginPanel.setStockBarginPanel(arg0);
					kBarginPanel.ViewData();
				}
			}
		});

		listSelectionModelMADB.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int m=jTableMADB.getSelectedRow();
				if(m!=-1){
					
					ArrayList<StockData> arg0=null;
					try {
						arg0 = Function.getStockList(tableModelMADB.getValueAt(m, 0).toString(),getStartDateJcb(jcbDate.getSelectedIndex()), Function.getEndTime(),jcbDayorWeek.getSelectedIndex());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					kDataPanel.setStockData(arg0,Function.getStockListBargin(arg0),Function.getStockListJX(arg0),Integer.valueOf(jcbMAday.getSelectedItem().toString()));
					kDataPanel.ViewData();
					kBarginPanel.setStockBarginPanel(arg0);
					kBarginPanel.ViewData();
				}
			}
		});
		
		
		listSelectionModelCOM.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int m=jTableCOM.getSelectedRow();
				if(m!=-1){
					
					ArrayList<StockData> arg0=null;
					try {
						arg0 = Function.getStockList(tableModelCOM.getValueAt(m, 0).toString(),getStartDateJcb(jcbDate.getSelectedIndex()), Function.getEndTime(),jcbDayorWeek.getSelectedIndex());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					kDataPanel.setStockData(arg0,Function.getStockListBargin(arg0),Function.getStockListJX(arg0),Integer.valueOf(jcbMAday.getSelectedItem().toString()));
					kDataPanel.ViewData();
					kBarginPanel.setStockBarginPanel(arg0);
					kBarginPanel.ViewData();
				}
			}
		});
		
		listSelectionModelCOMWeek.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int m=jTableCOMWeek.getSelectedRow();
				if(m!=-1){
					
					ArrayList<StockData> arg0=null;
					try {
						arg0 = Function.getStockList(tableModelCOMWeek.getValueAt(m, 0).toString(),getStartDateJcb(jcbDate.getSelectedIndex()), Function.getEndTime(),jcbDayorWeek.getSelectedIndex());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					kDataPanel.setStockData(arg0,Function.getStockListBargin(arg0),Function.getStockListJX(arg0),Integer.valueOf(jcbMAday.getSelectedItem().toString()));
					kDataPanel.ViewData();
					kBarginPanel.setStockBarginPanel(arg0);
					kBarginPanel.ViewData();
				}
			}
		});
		
		
		listSelectionModelBACK.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int m=jTableBACK.getSelectedRow();
				if(m!=-1){
					
					ArrayList<StockData> arg0=null;
					try {
						arg0 = Function.getStockList(tableModelBACK.getValueAt(m, 0).toString(),getStartDateJcb(jcbDate.getSelectedIndex()), Function.getEndTime(),jcbDayorWeek.getSelectedIndex());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					kDataPanel.setStockData(arg0,Function.getStockListBargin(arg0),Function.getStockListJX(arg0),Integer.valueOf(jcbMAday.getSelectedItem().toString()));
					kDataPanel.ViewData();
					kBarginPanel.setStockBarginPanel(arg0);
					kBarginPanel.ViewData();
				}
			}
		});
		
		listSelectionModelZLTJ.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int m=jTableZLTJ.getSelectedRow();
				if(m!=-1){
					ArrayList<StockData> arg0=null;
					try {
						arg0 = Function.getStockList(tableModelZLTJ.getValueAt(m, 0).toString(), getStartDateJcb(jcbDate.getSelectedIndex()), Function.getEndTime(),jcbDayorWeek.getSelectedIndex());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					kDataPanel.setStockData(arg0,Function.getStockListBargin(arg0),Function.getStockListJX(arg0),Integer.valueOf(jcbMAday.getSelectedItem().toString()));
					kDataPanel.ViewData();
					kBarginPanel.setStockBarginPanel(arg0);
					kBarginPanel.ViewData();
				}
			}
		});
		
		
		listSelectionModel0326.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int m=jTable0326.getSelectedRow();
				if(m!=-1){
					ArrayList<StockData> arg0=Function.getStockList(tableModel0326.getValueAt(m, 0).toString(), text21.getText(), text22.getText(),jcbDayorWeek.getSelectedIndex());
					kDataPanel.setStockData(arg0,Function.getStockListBargin(arg0),Function.getStockListJX(arg0),Integer.valueOf(jcbMAday.getSelectedItem().toString()));
					kDataPanel.ViewData();
					kBarginPanel.setStockBarginPanel(arg0);
					kBarginPanel.ViewData();
				}
			}
		});
		
		jproBar1.setStringPainted(true);
		jPmain.setLayout(new BorderLayout());
		jpBarandF.setLayout(new BorderLayout());
		jp1.setLayout(new GridLayout(3,3));
		jp2.setLayout(new GridLayout(2,4));
		jp4.setLayout(new GridLayout(3,3));
		jp5.setLayout(new GridLayout(3,3));
		jp6.setLayout(new GridLayout(3,3));
		jp7.setLayout(new GridLayout(3,3));
		jp8.setLayout(new GridLayout(3,2));
		jp9.setLayout(new GridLayout(3,3));
		jPup.setLayout(new GridLayout(2,1));
		jPce.setLayout(new GridLayout(1,5));
		jPupdown.setLayout(new GridLayout(2,1));
		jPfuzagongneng.setLayout(new GridLayout(1,2));
		
		jPmain.add(jPup,BorderLayout.CENTER);
		jPmain.add(jPce,BorderLayout.SOUTH);
		
		jPup.add(kDataPanel);
		jPup.add(jPupdown);
		jPupdown.add(kBarginPanel);
		jPupdown.add(jPfuzagongneng);
		jPfuzagongneng.add(jpBarandF);
		jpBarandF.add(jtpFigures,BorderLayout.CENTER);
		jpBarandF.add(jproBar1,BorderLayout.SOUTH);
		jPfuzagongneng.add(jtpFigure2);
		
		
		jtpFigures.addTab("MA", jp1);
		jtpFigures.addTab("总量", jp2);
		jtpFigures.addTab("相似", jp3);
		jtpFigures.addTab("总量比例", jp4);
		jtpFigures.addTab("MA_最高", jp5);
		jtpFigures.addTab("MA_收盘下穿", jp6);
		jtpFigures.addTab("锤子_阴坡阳", jp7);
		jtpFigures.addTab("最低价连续上涨", jp8);
		jtpFigures.addTab("MA单边", jp9);
		jtpFigure2.add("MA",new JScrollPane(jTableMA));
		jtpFigure2.add("总量",new JScrollPane(jTable0326));
		jtpFigure2.add("总量比例",new JScrollPane(jTableZLTJ));
		jtpFigure2.add("文本",new JScrollPane(jText));
		jtpFigure2.add("锤子",new JScrollPane(jTableCOM));
		jtpFigure2.add("阴坡阳",new JScrollPane(jTableBACK));
		jtpFigure2.add("周锤子",new JScrollPane(jTableCOMWeek));
		jtpFigure2.add("MA单边",new JScrollPane(jTableMADB));
		jtpFigure2.add("最低价连续上涨",new JScrollPane(jTableLCU));
		
		jp1.add(new JLabel("输入日期："));
		jp1.add(new JLabel("选择MA："));
		jp1.add(new JLabel("选择收盘价高于MA天数："));
		jp1.add(text1);
		jp1.add(jcbMAday);
		jp1.add(jcbMAday1);
		jp1.add(jB1MA);
		
		jp9.add(new JLabel("输入日期："));
		jp9.add(new JLabel("选择类型："));
		jp9.add(new JLabel());
		jp9.add(text9);
		jp9.add(jcbMADB);
		jp9.add(new JLabel());
		jp9.add(jB1MADB);
		jp9.add(jB1TJMADB);
		
		
		jp5.add(new JLabel("输入要单独查询股票(可为空)："));
		jp5.add(new JLabel("选择MA："));
		jp5.add(new JLabel("选择收盘价高于MA天数："));
		jp5.add(text12);
		jp5.add(jcbMAday2);
		jp5.add(jcbMAday21);
		jp5.add(jB1MAmagic);
		jp5.add(jB1MAmagic1);
		
		
		jp6.add(new JLabel("输入要单独查询股票(可为空)："));
		jp6.add(new JLabel("选择MA："));
		jp6.add(new JLabel("选择收盘价高于MA天数："));
		jp6.add(text121);
		jp6.add(jcbMAday3);
		jp6.add(jcbMAday31);
		jp6.add(jB1MAmagic5_10);
		jp6.add(jB1MAmagic15_10);
		
		jp7.add(new JLabel("请输入截止日期"));
		jp7.add(text71);
		jp7.add(jBchaxuncom);
		jp7.add(new JLabel("请输入天数"));
		jp7.add(text72);
		jp7.add(jBchaxuncomWeek);
		jp7.add(new JLabel());
		jp7.add(jBchaxuncomTJ);
		jp7.add(jBchaxunback);
		
		jp8.add(new JLabel("请输入要连续上涨天数"));
		jp8.add(text8);
		jp8.add(new JLabel("请输入截止日期"));
		jp8.add(text81);
		jp8.add(jcbLCU);
		jp8.add(jBLCU);
		
		jp4.add(new JLabel("输入起始日期：(默认时间为半年)"));
		jp4.add(new JLabel("输入截止日期："));
		jp4.add(new JLabel("输入天数"));
		jp4.add(text41);
		jp4.add(text43);
		jp4.add(text42);
		jp4.add(new JLabel("输入百分数%："));
		jp4.add(text44);
		jp4.add(jB4ZLTJ);
		
		jp2.add(new JLabel("输入起始日期："));
		jp2.add(text21);
		jp2.add(new JLabel("输入终止日期："));
		jp2.add(text22);
		jp2.add(new JLabel("输入查询日期："));
		jp2.add(text23);
		jp2.add(new JLabel());
		jp2.add(jB2ZL);
		
		/*中间层*/
		
		jPce.add(jBfind);
		jPce.add(textNum);
		jPce.add(textStart);
		jPce.add(textEnd);
		jPce.add(jcbDayorWeek);
		jPce.add(jcbDate);
		
		/*按钮加监听*/
		
		ActionListener xixi=new JBfindACT();
		jBfind.addActionListener(xixi);
		jB1MA.addActionListener(xixi);
		jB1MADB.addActionListener(xixi);
		jB1TJMADB.addActionListener(xixi);
		jB2ZL.addActionListener(xixi);
		jB1MAmagic.addActionListener(xixi);
		jB1MAmagic1.addActionListener(xixi);
		jB4ZLTJ.addActionListener(xixi);
		jB1MAmagic5_10.addActionListener(xixi);
		jB1MAmagic15_10.addActionListener(xixi);
		jBchaxuncom.addActionListener(xixi);
		jBchaxunback.addActionListener(xixi);
		jBchaxuncomWeek.addActionListener(xixi);
		jBLCU.addActionListener(xixi);
		jBchaxuncomTJ.addActionListener(xixi);
		//kDataPanel.addMouseMotionListener(l);
		
		
		this.setLayout(new BorderLayout());
		this.add(jPmain,BorderLayout.CENTER);
		this.setSize(1000, 600);
		this.setTitle("股票数据分析");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		jproBar1.setPreferredSize(new Dimension(jpBarandF.getWidth(), jpBarandF.getHeight()/5));
		
	}
	
	
	/*按钮监听，所有按钮监听都写在这个里面了*/
	class JBfindACT implements ActionListener{
		
		public String double2String(double arg0){
			String temp=String.valueOf(arg0);
			if(temp.length()>4){
				return temp.substring(0, 5);
			}
			else{
			return temp;
			}
		}
		
		
		public String _2String(double arg0){
			String temp=String.valueOf(arg0);
			if(temp.length()>4){
				return temp.substring(0, 6);
			}
			else{
			return temp;
			}
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if(e.getSource()==jBfind){
			ArrayList<StockData> arg0=Function.getStockList(textNum.getText(), textStart.getText(), textEnd.getText(),jcbDayorWeek.getSelectedIndex());
			kDataPanel.setStockData(arg0,Function.getStockListBargin(arg0),Function.getStockListJX(arg0),Integer.valueOf(jcbMAday.getSelectedItem().toString()));
			kDataPanel.ViewData();
			kBarginPanel.setStockBarginPanel(arg0);
			kBarginPanel.ViewData();
			}
			else if(e.getSource()==jB1MA){
				jText.setText("");
				final FunctionMA task=new FunctionMA(text1.getText(),Integer.valueOf(jcbMAday.getSelectedItem().toString()),Integer.valueOf(jcbMAday1.getSelectedItem().toString()));
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							task.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(int m=tableModelMA.getRowCount()-1;m>=0;m--){
							tableModelMA.removeRow(m);
						}
						ArrayList<GpMA> arg0=task.getMA();
						
						for(GpMA sp:arg0){
							jText.append("股票号："+sp.getsNum()+"      "+String.valueOf(sp.getPres())+"\n");
							Object[] xixi={sp.getsNum(),sp.getPres(),sp.getDate()};
							tableModelMA.addRow(xixi);
						}
						jtpFigure2.setSelectedIndex(0);
						
					}
				}).start();
				
						
				}
			
			else if(e.getSource()==jBLCU){
				jText.setText("");
				final FunctionLowCU task=new FunctionLowCU(Integer.valueOf(text8.getText()),text81.getText(),jcbLCU.getSelectedIndex());
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							task.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(int m=tableModelLCU.getRowCount()-1;m>=0;m--){
							tableModelLCU.removeRow(m);
						}
						ArrayList<GpLCU> arg0=task.getResultList();
						
						for(GpLCU sp:arg0){
							jText.append("股票号："+sp.getsNum()+"      "+String.valueOf(sp.getPres())+"\n");
							Object[] xixi={sp.getsNum(),sp.getPres(),sp.getDate()};
							tableModelLCU.addRow(xixi);
						}
						jtpFigure2.setSelectedIndex(8);
						
					}
				}).start();
				
						
				}
			else if(e.getSource()==jB1MADB){
				jText.setText("");
				final FunctionMADB task=new FunctionMADB(text9.getText(),jcbMADB.getSelectedIndex());
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							task.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(int m=tableModelMADB.getRowCount()-1;m>=0;m--){
							tableModelMADB.removeRow(m);
						}
						ArrayList<GpMA> arg0=task.getMA();
						
						for(GpMA sp:arg0){
							jText.append("股票号："+sp.getsNum()+"      "+String.valueOf(sp.getPres())+"\n");
							Object[] xixi={sp.getsNum(),sp.getPres(),sp.getDate()};
							tableModelMADB.addRow(xixi);
						}
						jtpFigure2.setSelectedIndex(7);
						
					}
				}).start();
				
						
				}
			
			else if(e.getSource()==jB1TJMADB){
				jText.setText("");
				int count=tableModelMADB.getRowCount();
				String[] arg0=new String[count];
				for(int m=0;m<count;++m){
					arg0[m]=tableModelMADB.getValueAt(m, 0).toString();
					//System.out.println(arg0[m]);
				}
				final FunctionTJMADB task=new FunctionTJMADB(arg0);
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							task.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ArrayList<tjMADBResult> arg0=task.getResult();
						
						for(tjMADBResult sp:arg0){
							jText.append("股票号："+sp.name+"   盈利概率："+sp.winCount+"%   盈利百分比："+sp.winP+"%   亏损概率："+sp.loseCount+"%   亏损百分比："+sp.loseP+"%\n");	
						}
						jtpFigure2.setSelectedIndex(3);
						
					}
				}).start();
				
						
				}
			
			else if(e.getSource()==jBchaxuncomWeek){
				//jText.setText("")
				final BotLenComWeek task=new BotLenComWeek(text71.getText(),Integer.valueOf(text72.getText()));
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							task.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(int m=tableModelCOMWeek.getRowCount()-1;m>=0;m--){
							tableModelCOMWeek.removeRow(m);
						}
						ArrayList<UnitB> arg0=task.getBotLenCom();
					//	System.out.println(arg0.size());
						Object[] onname={"请勿单击，仅显示个数",arg0.size()};
						tableModelCOMWeek.addRow(onname);
						for(UnitB sp:arg0){
							//jText.append("股票号："+sp.getsNum()+"      "+String.valueOf(sp.getPres())+"\n");
							Object[] xixi={sp.GetsNum(),sp.Getkey(),sp.Getdate(),sp.getType()};
							tableModelCOMWeek.addRow(xixi);
						//	System.out.println(sp.GetsNum());
						}
						jtpFigure2.setSelectedIndex(6);
						
					}
				}).start();		
				}
			
			
			else if(e.getSource()==jBchaxuncom){
				//jText.setText("")
				final BotLenCom task=new BotLenCom(text71.getText(),Integer.valueOf(text72.getText()));
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							task.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(int m=tableModelCOM.getRowCount()-1;m>=0;m--){
							tableModelCOM.removeRow(m);
						}
						ArrayList<UnitB> arg0=task.getBotLenCom();
					//	System.out.println(arg0.size());
						Object[] onname={"请勿单击，仅显示个数",arg0.size()};
						tableModelCOM.addRow(onname);
						for(UnitB sp:arg0){
							//jText.append("股票号："+sp.getsNum()+"      "+String.valueOf(sp.getPres())+"\n");
							Object[] xixi={sp.GetsNum(),sp.Getkey(),sp.Getdate(),sp.getType()};
							tableModelCOM.addRow(xixi);
						//	System.out.println(sp.GetsNum());
						}
						jtpFigure2.setSelectedIndex(4);
						
					}
				}).start();		
				}
			
			else if(e.getSource()==jBchaxuncomTJ){
				jText.setText("");
				int count=tableModelCOM.getRowCount();
				String[] arg0=new String[count-1];
				for(int m=1;m<count;++m){
					arg0[m-1]=tableModelCOM.getValueAt(m, 0).toString();
					//System.out.println(arg0[m]);
				}
				final BotLenComTJ task=new BotLenComTJ(arg0,25);
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							task.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						ArrayList<String> arg0=task.getResult();
						for(String sp:arg0){
							jText.append(sp+"\n");	
						}
						jtpFigure2.setSelectedIndex(3);
						
					}
				}).start();		
				}
			else if(e.getSource()==jBchaxunback){
				//jText.setText("")
				final Backward task=new Backward();
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							task.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(int m=tableModelBACK.getRowCount()-1;m>=0;m--){
							tableModelBACK.removeRow(m);
						}
						ArrayList<Unit> arg0=task.getBotLenCom();
						
						for(Unit sp:arg0){
							//jText.append("股票号："+sp.getsNum()+"      "+String.valueOf(sp.getPres())+"\n");
							Object[] xixi={sp.GetsNum(),sp.Getkey(),sp.Getdate()};
							tableModelBACK.addRow(xixi);
						}
						jtpFigure2.setSelectedIndex(5);
						
					}
				}).start();
				
						
				}
			
				else if(e.getSource()==jB4ZLTJ){
				jText.setText("");
				final FunctionZLTJ task=new FunctionZLTJ(text41.getText(),Integer.valueOf(text42.getText()),text43.getText(),Double.valueOf(text44.getText()));
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							task.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(int m=tableModelZLTJ.getRowCount()-1;m>=0;m--){
							tableModelZLTJ.removeRow(m);
						}
						ArrayList<GpZLTJ> arg0=task.getZLTJ();
						Object[] onname={"请勿单击，仅显示个数",arg0.size()};
						tableModelZLTJ.addRow(onname);
						for(GpZLTJ sp:arg0){
							jText.append("股票号："+sp.getsNum()+"      "+_2String(sp.getPres())+"\n");
							Object[] xixi={sp.getsNum(),_2String(sp.getPres()),sp.getDate()};
							tableModelZLTJ.addRow(xixi);
						}
						jtpFigure2.setSelectedIndex(2);
						
					}
				}).start();
				
						
				}

			else if(e.getSource()==jB2ZL){
				jText.setText("");
				final Function0326 task=new Function0326(text21.getText(), text22.getText(), text23.getText());
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							task.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(int m=tableModel0326.getRowCount()-1;m>=0;m--){
							tableModel0326.removeRow(m);
						}
						ArrayList<Gp0326> arg0=task.get0326();
						
						for(Gp0326 sp:arg0){
							jText.append("股票号："+sp.getsNum()+"     所占总量"+double2String(sp.getPres())+"\n");
							Object[] xixi={sp.getsNum(),String.valueOf(sp.getPres()).substring(0, 5)};
							tableModel0326.addRow(xixi);
						}
						jtpFigure2.setSelectedIndex(1);
					}
				}).start();
				
			}
			
			else if(e.getSource()==jB1MAmagic){
				
				if(!text12.getText().isEmpty()){
					String[] arg0={text12.getText()};
					FunctionTJMA task=new FunctionTJMA(arg0,Integer.valueOf(jcbMAday2.getSelectedItem().toString()),Integer.valueOf(jcbMAday21.getSelectedItem().toString()));
					task.addPropertyChangeListener(new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							// TODO Auto-generated method stub
							if("progress".equals(evt.getPropertyName())){
								jproBar1.setValue((Integer) evt.getNewValue());
							}
						}
					});
					task.execute();
					
				}
				else{
				int count=tableModelMA.getRowCount();
				String[] arg0=new String[count];
				for(int m=0;m<count;++m){
					arg0[m]=tableModelMA.getValueAt(m, 0).toString();
					//System.out.println(arg0[m]);
				}
				
				
				FunctionTJMA task=new FunctionTJMA(arg0,Integer.valueOf(jcbMAday2.getSelectedItem().toString()),Integer.valueOf(jcbMAday21.getSelectedItem().toString()));
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				}
			}
			
			else if(e.getSource()==jB1MAmagic5_10){
				
				if(!text121.getText().isEmpty()){
					String[] arg0={text121.getText()};
					FunctionTJMA5_10 task=new FunctionTJMA5_10(arg0,Integer.valueOf(jcbMAday3.getSelectedItem().toString()),Integer.valueOf(jcbMAday31.getSelectedItem().toString()));
					task.addPropertyChangeListener(new PropertyChangeListener() {
						
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							// TODO Auto-generated method stub
							if("progress".equals(evt.getPropertyName())){
								jproBar1.setValue((Integer) evt.getNewValue());
							}
						}
					});
					task.execute();
					
				}
				else{
				int count=tableModelMA.getRowCount();
				String[] arg0=new String[count];
				for(int m=0;m<count;++m){
					arg0[m]=tableModelMA.getValueAt(m, 0).toString();
					//System.out.println(arg0[m]);
				}
				
				
				FunctionTJMA5_10 task=new FunctionTJMA5_10(arg0,Integer.valueOf(jcbMAday3.getSelectedItem().toString()),Integer.valueOf(jcbMAday31.getSelectedItem().toString()));
				task.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						// TODO Auto-generated method stub
						if("progress".equals(evt.getPropertyName())){
							jproBar1.setValue((Integer) evt.getNewValue());
						}
					}
				});
				task.execute();
				}
			}
			
			else if(e.getSource()==jB1MAmagic1){
				jText.setText("");
				
				try {
					Connection connection=DriverManager.getConnection("jdbc:mysql://localhost/mystockdata", "root", "xinzhi200766");
					Statement statement=connection.createStatement();
					
					
					if(text12.getText().isEmpty()){
					int count=tableModelMA.getRowCount();
					String[] arg0=new String[count];
					for(int m=0;m<count;++m){
						arg0[m]=tableModelMA.getValueAt(m, 0).toString();
						ResultSet resultSet=statement.executeQuery("select * from ma where name="+arg0[m]);
						resultSet.next();
						jText.append("股票号："+resultSet.getString(1)+"   盈利概率："+resultSet.getString(2)+"%   盈利百分比："+resultSet.getString(3)+"%   亏损概率："+resultSet.getString(4)+"%   亏损百分比："+resultSet.getString(5)+"%\n");
						resultSet.close();
						
					}
					}
					else{
						ResultSet resultSet=statement.executeQuery("select * from ma where name="+text12.getText());
						resultSet.next();
						jText.append("股票号："+resultSet.getString(1)+"   盈利概率："+resultSet.getString(2)+"%   盈利百分比："+resultSet.getString(3)+"%   亏损概率："+resultSet.getString(4)+"%   亏损百分比："+resultSet.getString(5)+"%\n");
						resultSet.close();
					}
					
					statement.close();
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				jtpFigure2.setSelectedIndex(3);
			}
			
			else if(e.getSource()==jB1MAmagic15_10){
				jText.setText("");
				
				try {
					Connection connection=DriverManager.getConnection("jdbc:mysql://localhost/mystockdata", "root", "xinzhi200766");
					Statement statement=connection.createStatement();
					
					
					if(text121.getText().isEmpty()){
					int count=tableModelMA.getRowCount();
					String[] arg0=new String[count];
					for(int m=0;m<count;++m){
						arg0[m]=tableModelMA.getValueAt(m, 0).toString();
						ResultSet resultSet=statement.executeQuery("select * from ma1 where name="+arg0[m]);
						resultSet.next();
						jText.append("股票号："+resultSet.getString(1)+"   盈利概率："+resultSet.getString(2)+"%   盈利百分比："+resultSet.getString(3)+"%   亏损概率："+resultSet.getString(4)+"%   亏损百分比："+resultSet.getString(5)+"%\n");
						resultSet.close();
						
					}
					}
					else{
						ResultSet resultSet=statement.executeQuery("select * from ma1 where name="+text12.getText());
						resultSet.next();
						jText.append("股票号："+resultSet.getString(1)+"   盈利概率："+resultSet.getString(2)+"%   盈利百分比："+resultSet.getString(3)+"%   亏损概率："+resultSet.getString(4)+"%   亏损百分比："+resultSet.getString(5)+"%\n");
						resultSet.close();
					}
					
					statement.close();
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				jtpFigure2.setSelectedIndex(3);
			}
			
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Windows();
	}

}
/*股票交易量视图*/
class StockBarginPanel extends JPanel{
	
	private ArrayList<StockData> stockData;
	int flag;
	private int jpWidth;
	private int jpHight;
	private int dataNum;
	private double xWidth;
	double MAX=0;
	
	public StockBarginPanel(){
		flag=0;
	}
	public void ViewData(){
		repaint();
	}
	public void setStockBarginPanel(ArrayList<StockData> arg0){
		MAX=0;
		jpWidth=this.getWidth();
		jpHight=this.getHeight();
		stockData=arg0;
		dataNum=stockData.size()-1;
		xWidth=((double)jpWidth*0.8)/dataNum;
		for(int i=1;i<dataNum+1;++i){
			if(stockData.get(i).getBargin()>MAX){
				MAX=stockData.get(i).getBargin();
			}
		}
		flag++;
	}
	protected void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, jpWidth, jpHight);
		if(flag!=0){
			double open;
			double close;
			double x=0;
			for(int i=1;i<dataNum+1;++i){
				open=stockData.get(i).getOpen();
				close=stockData.get(i).getClose();
				if(open<close){
					g.setColor(Color.RED); //开盘小于收盘，盈利红色
					g.fillRect((int)x, 0, (int)(xWidth-1),(int)(stockData.get(i).getBargin()/MAX*jpHight ));
					x+=xWidth;
				}
				else{
					g.setColor(Color.GREEN);//开盘大于收盘，亏损绿色
					g.fillRect((int)x, 0, (int)(xWidth-1),(int)(stockData.get(i).getBargin()/MAX*jpHight ));
					x+=xWidth;
				}
			}
			
		}
		
	}
}



/*股票数据视图*/
class StockDataPanel extends JPanel{
	
	final double NUMBER=10;
	private double[] stockBargin;
	private double[] stockJX;
	private ArrayList<StockData> stockData;
	int flag;
	private int jpWidth;
	private int jpHight;
	private int dataNum;
	private double xWidth;
	private double yHight;
	private double stockDataHigh;
	private double stockDataLow;
	private double everyPricePoint;
	private double MAX=0;
	private double MAXJX=0;
	private int MAXJXtemp=0;
	private int maFanMADay=0;
	int mouseX=0;
	int mouseY=0;
	public StockDataPanel(){
		flag=0;
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				mouseX=e.getX();
				mouseY=e.getY();
				repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
				
				
			}
		});
	}
	public void setStockData(ArrayList<StockData> arg0,double[] arg1,double[] arg2,int arg3){
		MAX=0;
		MAXJX=0;
		MAXJXtemp=0;
		stockData=arg0;
		flag++;
		jpWidth=this.getWidth();
		jpHight=this.getHeight();
	
		dataNum=stockData.size()-1;
		stockDataHigh=stockData.get(0).getHigh();
		stockDataLow=stockData.get(0).getLow();
		
		//System.out.println(stockDataHigh);
		//System.out.println(stockDataLow);
		xWidth=((double)jpWidth*0.8)/dataNum;
		everyPricePoint=jpHight/(stockDataHigh-stockDataLow);
		
		stockBargin=arg1;
		stockJX=arg2;
		maFanMADay=arg3;
		yHight=((double)jpHight)/stockBargin.length;
		for(int i=0;i<stockBargin.length;++i){
			if(stockBargin[i]>MAX){
				MAX=stockBargin[i];
			}
		}
		
		for(int i=0;i<stockJX.length;++i){
			if(stockJX[i]>MAXJX){
				MAXJX=stockJX[i];
				MAXJXtemp=i;
			}
		}
		
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
	
	public void ViewData(){
		repaint();
	}
	private double getavargeMA(ArrayList<StockData> arrayStock,int m,int arg0){
		double temp=0;
		switch(arg0){
		case 8 :
			for(int i=0;i<8;i++){
				temp+=arrayStock.get(m-i).getClose();
			}
			break;
		case 10 :
			for(int i=0;i<10;i++){
				temp+=arrayStock.get(m-i).getClose();
			}
			break;
		
		case 15 :
			for(int i=0;i<15;i++){
				temp+=arrayStock.get(m-i).getClose();
			}
			break;
		case 34 :
			for(int i=0;i<34;i++){
				temp+=arrayStock.get(m-i).getClose();
			}
			break;
		}
		return temp/arg0;	
	}
	private void maFan(int arg0){
		
	}
	protected void paintComponent(Graphics g){
		super.paintComponents(g);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, jpWidth, jpHight);
		if(flag!=0){
		
		double open;
		double high;
		double low;
		double close;
		double x=0;
		double tempMA5=0;
		double MA5=0;
		for(int i=1;i<dataNum+1;++i){
			open=stockData.get(i).getOpen();
			high=stockData.get(i).getHigh();
			low=stockData.get(i).getLow();
			close=stockData.get(i).getClose();
			if(open<close){
				g.setColor(Color.RED); //开盘小于收盘，盈利红色
				g.fillRect((int)x,(int)((stockDataHigh-close)*everyPricePoint) ,(int) (xWidth-1), (int)((close-open)*everyPricePoint));
				g.drawLine((int)(x+xWidth/2),(int)((stockDataHigh-high)*everyPricePoint) , (int)(x+xWidth/2), (int)((stockDataHigh-low)*everyPricePoint) );
				
			}
			else{
				g.setColor(Color.GREEN);//开盘大于收盘，亏损绿色
				g.fillRect((int)x,(int)((stockDataHigh-close)*everyPricePoint) ,(int) (xWidth-1), (int)((close-open)*everyPricePoint));
				g.drawLine((int)(x+xWidth/2),(int)((stockDataHigh-high)*everyPricePoint) , (int)(x+xWidth/2), (int)((stockDataHigh-low)*everyPricePoint) );	
			}
			if(i==maFanMADay){
				tempMA5=getavargeMA(stockData,i,maFanMADay);	
			}
			if(i>maFanMADay){
				MA5=getavargeMA(stockData,i,maFanMADay);
				g.setColor(Color.BLUE); 
				g.drawLine((int)(x-xWidth+xWidth/2),(int)((stockDataHigh-tempMA5)*everyPricePoint) , (int)(x+xWidth/2), (int)((stockDataHigh-MA5)*everyPricePoint) );
				tempMA5=MA5;
			}
			x+=xWidth;
		}
		
		g.setColor(Color.BLUE); 
		/*画三角？*/
		int xPostion=(int)(jpWidth*0.8+30);
		int length=(int) (jpWidth*0.2);
		double y=0;
		for(int i=stockBargin.length-1;i>-1;i--){
			g.fillRect(xPostion,(int) y, (int) (stockBargin[i]/MAX*length), Math.max((int)yHight-1, 1));
			y+=yHight;
		}
		
		g.drawLine(0,(int)(yHight*(stockJX.length-MAXJXtemp)/10),xPostion-30,(int)(yHight*(stockJX.length-MAXJXtemp)/10));
		
		
		y=0;
		g.setColor(Color.BLACK);
		g.drawLine(0, mouseY, jpWidth, mouseY);
		g.drawLine(mouseX, 0, mouseX, jpHight);
		double everyAddY=jpHight/NUMBER;
		double everyAddPrice=(stockDataHigh-stockDataLow)/NUMBER;
		for(int i=0;i<10;++i){
			g.drawString(double2String(stockDataHigh-everyAddPrice*i), xPostion-30, (int)y);
			y+=everyAddY;
		}
		
		int xiaBiao=(int) (mouseX/xWidth)+1;
		if(xiaBiao<dataNum+1){
		g.drawString("日期："+stockData.get(xiaBiao).getMonth().toString()+"   开盘："+double2String(stockData.get(xiaBiao).getOpen())+"    最高："+double2String(stockData.get(xiaBiao).getHigh())+"    最低："+double2String(stockData.get(xiaBiao).getLow())+"   收盘："+double2String(stockData.get(xiaBiao).getClose())+"   总量："+String.valueOf(stockData.get(xiaBiao).getBargin()), 0, jpHight-10);
		
		}
		}
	}
}