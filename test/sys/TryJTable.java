package sys;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
//主类
@SuppressWarnings("unchecked")
public class TryJTable extends JFrame 
{
	private static final long serialVersionUID = 1L;
	//创建自定义表格模型对象
	MyTableModel dtm=new MyTableModel();
	//创建JTable对象
	JTable jt=new JTable(dtm);
	//将JTable封装进滚动窗格
	JScrollPane jsp=new JScrollPane(jt);	
	//自定义的表格模型
	private class MyTableModel extends AbstractTableModel
	{
		
		private static final long serialVersionUID = 1L;
		//创建表示各个列类型的类型数组
		Class[] typeArray=
		      {Object.class,Double.class,Boolean.class,Icon.class,Color.class};
		//创建列标题字符串数组
		String[] head={"Object","Number","Boolean","Icon","Color"};
		//创建初始表格数据
		Object[][] data={{"这里是字符串1",new Integer(20),new Boolean(true),
							new ImageIcon("D:/p1.gif "),Color.black},
						{"这里是字符串2",new Double(12.34),new Boolean(false),
						new ImageIcon("D:/p2.gif "),Color.darkGray},
						{"这里是字符串3",new Integer(125),new Boolean(true),
						new ImageIcon("D:/p3.gif"),Color.white}};
		//重写getColumnCount方法
		public int getColumnCount()
		{
			return head.length;
		}
		//重写getRowCount方法	
		public int getRowCount()
		{
			return data.length;
		}
		//重写getColumnName方法
		public String getColumnName(int col)
		{
			return head[col];
		}
		//重写getValueAt方法
		public Object getValueAt(int r,int c)
		{
			return data[r][c];
		}
		//重写getColumnClass方法
		public Class getColumnClass(int c)
		{
			return typeArray[c];
		}
		//重写isCellEditable方法
		public boolean isCellEditable(int r,int c)
		{
			return true;
		}
		//重写setValueAt方法
		public void setValueAt(Object value,int r,int c)
		{
			data[r][c]=value;
			//
			this.fireTableCellUpdated(r,c);
		}
	}
	//自定义的绘制器
	private class MyCellRendererForColorClass 
					extends JLabel implements TableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		//定义构造器
		public MyCellRendererForColorClass ()
		{
			//设置标签为不透明状态
			this.setOpaque(true);
			//设置标签的文本对齐方式为居中
			this.setHorizontalAlignment(JLabel.CENTER);
		}
		//实现获取呈现控件的getTableCellRendererComponent方法
		public Component getTableCellRendererComponent(JTable table,Object value,
	           			boolean isSelected,boolean hasFocus,int row,int column)
		{			
			//获取要呈现的颜色
			Color c=(Color)value;
			//根据参数value设置背景色
			this.setBackground(c);
			//设置前景色为背景色反色
			this.setForeground(new Color(255-c.getRed()
			                    ,255-c.getGreen(),255-c.getBlue()));
			//设置标签中显示RGB的值
			this.setText("["+c.getRed()+","+c.getGreen()+","+c.getBlue()+"]");
			//将自己返回
			return this;	
		}
	}	
	//声明Color类型的自定义编辑器
	class MyEditorForColorClass extends AbstractCellEditor 
			implements TableCellEditor,ActionListener
	{
		private static final long serialVersionUID = 1L;
		//定义Color变量
		Color c;
		//定义对话框变量
		JDialog jd;
		//创建一个按扭
		JButton jb=new JButton();
		//创建JColorChooser对象
		JColorChooser jcc=new JColorChooser();
		//声明一个常量
		public static final String EDIT="edit";
		//定义构造器
		public MyEditorForColorClass()
		{
			//为按扭注册监听器
			jb.addActionListener(this);
			//设置此按扭的动作命令
			jb.setActionCommand(EDIT);
			//获取颜色选择器
			jd=JColorChooser.createDialog(jb,"选择颜色",true,jcc,this,null);
		}
		//实现actionPerformed方法
		public void actionPerformed(ActionEvent e)
		{
			//测试获得的动作命令是否等于EDIT常量
			if(e.getActionCommand().equals(EDIT))
			{
				//设置按扭的背景颜色
				jb.setBackground(c);
				//设置前景色为背景色反色
				jb.setForeground(new Color(255-c.getRed()
			                    ,255-c.getGreen(),255-c.getBlue()));
				//设置按钮中显示RGB的值
				jb.setText("["+c.getRed()+","+c.getGreen()+","+c.getBlue()+"]");				
				
				//设置颜色选择器的颜色
				jcc.setColor(c);
				//设置颜色选择器可见
				jd.setVisible(true);
				//通知所有监听器，以延迟方式创建事件对象
				this.fireEditingStopped();			
			}
			else
			{
				//获取颜色
				c=jcc.getColor();
			}
		}
		//定义getCellEditorValue方法返回颜色值
		public Object getCellEditorValue()
		{
			return c; 
		}
		//重写getTableCellEditorComponent方法
		public Component getTableCellEditorComponent(JTable table,
	        Object value,boolean isSelected,int row,int column)
	    {
	    	c=(Color)value;
	    	return jb;  	
	    }
	}
	//声明Icon类型的自定义编辑器
	class MyEditorForIconClass extends AbstractCellEditor 
			implements TableCellEditor,ActionListener
	{
		private static final long serialVersionUID = 1L;
		//定义Icon变量
		Icon icon;
		//创建一个按扭
		JButton jb=new JButton();
		//创建JColorChooser对象
		JFileChooser jfc=new JFileChooser();
		//声明一个常量
		public static final String EDIT="edit";
		//定义构造器
		public MyEditorForIconClass()
		{
			//为按扭注册监听器
			jb.addActionListener(this);
			//设置此按扭的动作命令
			jb.setActionCommand(EDIT);
		}
		//实现actionPerformed方法
		public void actionPerformed(ActionEvent e)
		{
			//测试获得的动作命令是否等于EDIT常量
			if(e.getActionCommand().equals(EDIT))
			{
				//设置按扭的图标
				jb.setIcon(icon);
				//显示文件选择器对话框
				jfc.showOpenDialog(jb);
				//获取新图片
				if(jfc.getSelectedFile()!=null)
				{
					icon=new ImageIcon(jfc.getSelectedFile().getAbsolutePath());
				}				
				//通知所有监听器，以延迟方式创建事件对象
				this.fireEditingStopped();			
			}
		}
		//定义getCellEditorValue方法返回图标
		public Object getCellEditorValue()
		{
			return icon; 
		}
		//重写getTableCellEditorComponent方法
		public Component getTableCellEditorComponent(JTable table,
	        Object value,boolean isSelected,int row,int column)
	    {
	    	icon=(Icon)value;
	    	return jb;  	
	    }
	}	
	//构造器
	public TryJTable()
	{
		//设置表格每行的高度为30个像素
		jt.setRowHeight(30);
		//将含JTable的滚动窗格添加进窗体的中间
		this.add(jsp,BorderLayout.CENTER);		
		//创建自定义的表格绘制器
		MyCellRendererForColorClass mcr=new MyCellRendererForColorClass();
		//向表格注册指定类型数据的绘制器
		jt.setDefaultRenderer(Color.class,mcr);		
		//创建自定义的表格编辑器
		MyEditorForColorClass mefcc=new MyEditorForColorClass();
		MyEditorForIconClass mefic=new MyEditorForIconClass();
		//向表格注册指定类型数据的编辑器
		jt.setDefaultEditor(Color.class,mefcc);
		jt.setDefaultEditor(Icon.class,mefic);		
		//设置窗体的关闭动作、标题、大小位置以及可见性
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("自定义表格绘制器与编辑器案例");
		this.setBounds(100,100,500,200);
		this.setVisible(true);
	}
	//主方法
	public static void main(String[] args)
	{
		//创建TryJTable窗体对象
		new TryJTable ();
	}
}
