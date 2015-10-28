package outbound;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DBUtil.DBOperator;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import sys.JTableUtil;
import util.Math_SAM;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;
import java.awt.Font;

public class PickListPrintFrm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private PBSUIBaseGrid table;
	private PickListPrintFrm printFrm;
	private String sql = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PickListPrintFrm dialog = new PickListPrintFrm();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public PickListPrintFrm(String strsql) {
		this();
		this.sql = strsql;
		this.printFrm = this;
		DataManager dm = DBOperator.DoSelect2DM(sql);
		if (table.getColumnCount() == 0) {
			table.setColumn(dm.getCols());
		}
		table.removeRowAll();
		table.setData(dm.getDataStrings());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setColumnEditableAll(false);
		Dimension size = table.getTableHeader().getPreferredSize();
		size.height = 30;//�����µı�ͷ�߶�40
		table.getTableHeader().setPreferredSize(size);
		table.getTableHeader().setFont(new Font("Dialog", 0, 28));
		table.setRowHeight(50);
		JTableUtil.fitTableColumns(table);
//		table.setSortEnable();
		setTableSum();
	}
	
	public void setTableSum(){
		double qty = 0.0;
		for(int i=0;i<table.getRowCount();i++){
			qty = qty + Math_SAM.str2Double(table.getValueAt(i, table.getColumnModel().getColumnIndex("����")).toString());
			table.setValueAt(String.valueOf(i+1), i, 0);
		}
		//����һ�п���
		Object [] addRowValues = new Object[table.getColumnCount()];
		addRowValues[table.getColumnCount()-2] = String.valueOf(qty);
		table.addRow(addRowValues);
		
	}

	/**
	 * Create the dialog.
	 */
	public PickListPrintFrm() {
		setBounds(100, 100, 831, 493);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				table = new PBSUIBaseGrid();
				table.setFont(new Font("����", Font.PLAIN, 28));
				table.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount()>=2){
							table.setColumnSelectionAllowed(true);
						}
					}
				});
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.NORTH);
			{
				JButton btnPrint = new JButton("\u6253\u5370");
				btnPrint.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							  //����Ҫ��ӡ�� ��˵��
				              MessageFormat headerFormat = new MessageFormat("�ֿ�����");
				              //ҳ�����
				              MessageFormat footerFormat = new MessageFormat("- {0} -");
				              table.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
				              
				              Book book = new Book(); 

				           // ���ó����� 
				           PageFormat pf = new PageFormat(); 
				           pf.setOrientation(PageFormat.PORTRAIT); 

				           // ͨ��Paper����ҳ��Ŀհױ߾�Ϳɴ�ӡ���򡣱�����ʵ�ʴ�ӡֽ�Ŵ�С����� 
				           Paper p = new Paper(); 
				           p.setSize(590,840);//ֽ�Ŵ�С 
				           p.setImageableArea(45,45,594,702);//A4(595 X 842)���ô�ӡ������ʵ0��0Ӧ����72��72����ΪA4ֽ��Ĭ��X,Y�߾���72 
				           pf.setPaper(p); 

//				           //��ȡ��ӡ������� 
//				           PrinterJob job = PrinterJob.getPrinterJob(); 
//				           // ���ô�ӡ�� 
//				           job.setPageable(book); 
				           
				            } catch (Exception pe) {
				              System.err.println("Error printing: " + pe.getMessage());
				            }
					}
				});
				btnPrint.setActionCommand("OK");
				buttonPane.add(btnPrint);
				getRootPane().setDefaultButton(btnPrint);
			}
			{
				JButton cancelButton = new JButton("\u5173\u95ED");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						printFrm.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
