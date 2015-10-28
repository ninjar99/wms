package sys;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import DBUtil.DBConnectionManager;
import dmdata.DataManager;
import main.PBSUIBaseGrid;
import util.JTNumEdit;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class testTab extends JFrame {

	private PBSUIBaseGrid contentPane;
	private PBSUIBaseGrid tblMain;
	private boolean trigTable = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testTab frame = new testTab();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public testTab() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new PBSUIBaseGrid();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		tblMain = new PBSUIBaseGrid();
		scrollPane.setViewportView(tblMain);
		tblMain.getModel().addTableModelListener(new TableModelListener()
	    {
	      public void tableChanged(TableModelEvent e)
	      {
	        tblMain_tableChanged(e);
	      }
	    });
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("add");
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("stop");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tblMain.isEditing())
			    {
			      tblMain.getCellEditor().stopCellEditing();
			    }
				tblMain.setColumnEditableAll(false);
			}
			
		});
		panel_1.add(btnNewButton_1);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DataManager dm = new DataManager();
				String[] SOColumnNames = {"货主编码","货主名称","",""};
			    tblMain.setColumn(SOColumnNames);
			    tblMain.setWidth(60, 0);
			    tblMain.setWidth(140, 1);
			    tblMain.setColumnEditable(true,0);
			    tblMain.setColumnEditable(false,1);
//			    tblMain.setCellColor(Color.red);
			    tblMain.setComponent((new JTNumEdit(15, "#####",true)), 0);
//			    JTNumEdit nEditQty = new JTNumEdit(10,"#,##0.00");
//			    nEditQty.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
//			    tblMain.setCellAlignment(javax.swing.SwingConstants.RIGHT, 0);
			    
			    java.sql.Connection con = DBConnectionManager.getInstance().getConnection("wms");
				java.sql.Statement stmt;
				try {
					stmt = con.createStatement();
					String sql = "select storer_code,storer_name,'','' from bas_storer where 1=1 ";
					ResultSet rs = stmt.executeQuery(sql);
					java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			        String[] colnames = new String[rsmd.getColumnCount()];
			        for (int i = 1 ; i <= rsmd.getColumnCount() ; i++){
			          colnames[i-1] = rsmd.getColumnName(i);
			        }
			        System.out.println(colnames);
			        java.util.Vector alldata = new java.util.Vector();
			        while(rs.next()){
			          Object[] rowdata = new Object[rsmd.getColumnCount()];
			          for ( int i = 1;i <= rsmd.getColumnCount() ;i++) {
			            rowdata[i-1] = (rs.getObject(i));
			          }
			          alldata.addElement(rowdata);
			        }
			        tblMain.setData(alldata);
			        tblMain.requestFocus();
			        tblMain.changeSelection(tblMain.getRowCount()-1,5,false,false);
			        Vector rowID = new Vector();
			        for(int i=0;i<tblMain.getRowCount();i++){
			          rowID.addElement(new Integer(i));
			        }
			        tblMain.setRowColor(rowID,Color.LIGHT_GRAY);
			        Vector rowID2 = new Vector();
			        rowID2.addElement(new Integer("0"));
			        tblMain.setColColor(rowID2, Color.red);
			        
			        if (tblMain.isEditing())
			            tblMain.getCellEditor().stopCellEditing();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			    
			}
		});
	}
	
	private void tblMain_tableChanged(TableModelEvent e)
	  {
	    int selectRow = e.getFirstRow();
	    int column = e.getColumn();
	    int type = e.getType();

	    if (!trigTable) return;
	    if (selectRow<0) return;
	    switch(type)
	    {
	      case TableModelEvent.DELETE:
	        break;
	      case TableModelEvent.INSERT:
	        break;
	      case TableModelEvent.UPDATE:
	        trigTable = false;
	        if (column==0){
	          if (tblMain.getValueAt(selectRow,0)!=null &&
	              !tblMain.getValueAt(selectRow,0).equals(""))
	            tblMain.setValueAt("test",selectRow,1);
	        }
	        trigTable = true;
	        break;
	    }
	  }
	
	private Object ChangeTimeStamp2Date(Object obj) {
		if (obj == null)
			return "";
		if (obj != null) {
			if (obj.getClass().getName().compareToIgnoreCase("java.sql.Timestamp") == 0) {
				obj = Timestamp2Date((Timestamp) obj);
			}
		}
		return obj;
	}

	private java.sql.Timestamp Date2Timestamp(java.sql.Date date) {
		return date == null ? null : Timestamp.valueOf(date.toString() + " 00:00:00");
	}

	private java.sql.Date Timestamp2Date(java.sql.Timestamp timestamp) {
		return timestamp == null ? null : new java.sql.Date(timestamp.getTime());
	}

}
