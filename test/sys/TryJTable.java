package sys;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
//����
@SuppressWarnings("unchecked")
public class TryJTable extends JFrame 
{
	private static final long serialVersionUID = 1L;
	//�����Զ�����ģ�Ͷ���
	MyTableModel dtm=new MyTableModel();
	//����JTable����
	JTable jt=new JTable(dtm);
	//��JTable��װ����������
	JScrollPane jsp=new JScrollPane(jt);	
	//�Զ���ı��ģ��
	private class MyTableModel extends AbstractTableModel
	{
		
		private static final long serialVersionUID = 1L;
		//������ʾ���������͵���������
		Class[] typeArray=
		      {Object.class,Double.class,Boolean.class,Icon.class,Color.class};
		//�����б����ַ�������
		String[] head={"Object","Number","Boolean","Icon","Color"};
		//������ʼ�������
		Object[][] data={{"�������ַ���1",new Integer(20),new Boolean(true),
							new ImageIcon("D:/p1.gif "),Color.black},
						{"�������ַ���2",new Double(12.34),new Boolean(false),
						new ImageIcon("D:/p2.gif "),Color.darkGray},
						{"�������ַ���3",new Integer(125),new Boolean(true),
						new ImageIcon("D:/p3.gif"),Color.white}};
		//��дgetColumnCount����
		public int getColumnCount()
		{
			return head.length;
		}
		//��дgetRowCount����	
		public int getRowCount()
		{
			return data.length;
		}
		//��дgetColumnName����
		public String getColumnName(int col)
		{
			return head[col];
		}
		//��дgetValueAt����
		public Object getValueAt(int r,int c)
		{
			return data[r][c];
		}
		//��дgetColumnClass����
		public Class getColumnClass(int c)
		{
			return typeArray[c];
		}
		//��дisCellEditable����
		public boolean isCellEditable(int r,int c)
		{
			return true;
		}
		//��дsetValueAt����
		public void setValueAt(Object value,int r,int c)
		{
			data[r][c]=value;
			//
			this.fireTableCellUpdated(r,c);
		}
	}
	//�Զ���Ļ�����
	private class MyCellRendererForColorClass 
					extends JLabel implements TableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		//���幹����
		public MyCellRendererForColorClass ()
		{
			//���ñ�ǩΪ��͸��״̬
			this.setOpaque(true);
			//���ñ�ǩ���ı����뷽ʽΪ����
			this.setHorizontalAlignment(JLabel.CENTER);
		}
		//ʵ�ֻ�ȡ���ֿؼ���getTableCellRendererComponent����
		public Component getTableCellRendererComponent(JTable table,Object value,
	           			boolean isSelected,boolean hasFocus,int row,int column)
		{			
			//��ȡҪ���ֵ���ɫ
			Color c=(Color)value;
			//���ݲ���value���ñ���ɫ
			this.setBackground(c);
			//����ǰ��ɫΪ����ɫ��ɫ
			this.setForeground(new Color(255-c.getRed()
			                    ,255-c.getGreen(),255-c.getBlue()));
			//���ñ�ǩ����ʾRGB��ֵ
			this.setText("["+c.getRed()+","+c.getGreen()+","+c.getBlue()+"]");
			//���Լ�����
			return this;	
		}
	}	
	//����Color���͵��Զ���༭��
	class MyEditorForColorClass extends AbstractCellEditor 
			implements TableCellEditor,ActionListener
	{
		private static final long serialVersionUID = 1L;
		//����Color����
		Color c;
		//����Ի������
		JDialog jd;
		//����һ����Ť
		JButton jb=new JButton();
		//����JColorChooser����
		JColorChooser jcc=new JColorChooser();
		//����һ������
		public static final String EDIT="edit";
		//���幹����
		public MyEditorForColorClass()
		{
			//Ϊ��Ťע�������
			jb.addActionListener(this);
			//���ô˰�Ť�Ķ�������
			jb.setActionCommand(EDIT);
			//��ȡ��ɫѡ����
			jd=JColorChooser.createDialog(jb,"ѡ����ɫ",true,jcc,this,null);
		}
		//ʵ��actionPerformed����
		public void actionPerformed(ActionEvent e)
		{
			//���Ի�õĶ��������Ƿ����EDIT����
			if(e.getActionCommand().equals(EDIT))
			{
				//���ð�Ť�ı�����ɫ
				jb.setBackground(c);
				//����ǰ��ɫΪ����ɫ��ɫ
				jb.setForeground(new Color(255-c.getRed()
			                    ,255-c.getGreen(),255-c.getBlue()));
				//���ð�ť����ʾRGB��ֵ
				jb.setText("["+c.getRed()+","+c.getGreen()+","+c.getBlue()+"]");				
				
				//������ɫѡ��������ɫ
				jcc.setColor(c);
				//������ɫѡ�����ɼ�
				jd.setVisible(true);
				//֪ͨ���м����������ӳٷ�ʽ�����¼�����
				this.fireEditingStopped();			
			}
			else
			{
				//��ȡ��ɫ
				c=jcc.getColor();
			}
		}
		//����getCellEditorValue����������ɫֵ
		public Object getCellEditorValue()
		{
			return c; 
		}
		//��дgetTableCellEditorComponent����
		public Component getTableCellEditorComponent(JTable table,
	        Object value,boolean isSelected,int row,int column)
	    {
	    	c=(Color)value;
	    	return jb;  	
	    }
	}
	//����Icon���͵��Զ���༭��
	class MyEditorForIconClass extends AbstractCellEditor 
			implements TableCellEditor,ActionListener
	{
		private static final long serialVersionUID = 1L;
		//����Icon����
		Icon icon;
		//����һ����Ť
		JButton jb=new JButton();
		//����JColorChooser����
		JFileChooser jfc=new JFileChooser();
		//����һ������
		public static final String EDIT="edit";
		//���幹����
		public MyEditorForIconClass()
		{
			//Ϊ��Ťע�������
			jb.addActionListener(this);
			//���ô˰�Ť�Ķ�������
			jb.setActionCommand(EDIT);
		}
		//ʵ��actionPerformed����
		public void actionPerformed(ActionEvent e)
		{
			//���Ի�õĶ��������Ƿ����EDIT����
			if(e.getActionCommand().equals(EDIT))
			{
				//���ð�Ť��ͼ��
				jb.setIcon(icon);
				//��ʾ�ļ�ѡ�����Ի���
				jfc.showOpenDialog(jb);
				//��ȡ��ͼƬ
				if(jfc.getSelectedFile()!=null)
				{
					icon=new ImageIcon(jfc.getSelectedFile().getAbsolutePath());
				}				
				//֪ͨ���м����������ӳٷ�ʽ�����¼�����
				this.fireEditingStopped();			
			}
		}
		//����getCellEditorValue��������ͼ��
		public Object getCellEditorValue()
		{
			return icon; 
		}
		//��дgetTableCellEditorComponent����
		public Component getTableCellEditorComponent(JTable table,
	        Object value,boolean isSelected,int row,int column)
	    {
	    	icon=(Icon)value;
	    	return jb;  	
	    }
	}	
	//������
	public TryJTable()
	{
		//���ñ��ÿ�еĸ߶�Ϊ30������
		jt.setRowHeight(30);
		//����JTable�Ĺ���������ӽ�������м�
		this.add(jsp,BorderLayout.CENTER);		
		//�����Զ���ı�������
		MyCellRendererForColorClass mcr=new MyCellRendererForColorClass();
		//����ע��ָ���������ݵĻ�����
		jt.setDefaultRenderer(Color.class,mcr);		
		//�����Զ���ı��༭��
		MyEditorForColorClass mefcc=new MyEditorForColorClass();
		MyEditorForIconClass mefic=new MyEditorForIconClass();
		//����ע��ָ���������ݵı༭��
		jt.setDefaultEditor(Color.class,mefcc);
		jt.setDefaultEditor(Icon.class,mefic);		
		//���ô���Ĺرն��������⡢��Сλ���Լ��ɼ���
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("�Զ������������༭������");
		this.setBounds(100,100,500,200);
		this.setVisible(true);
	}
	//������
	public static void main(String[] args)
	{
		//����TryJTable�������
		new TryJTable ();
	}
}
