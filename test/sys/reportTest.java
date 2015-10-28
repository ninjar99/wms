package sys;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class reportTest extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					reportTest frame = new reportTest();
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
	public reportTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JButton btnNewButton = new JButton("report test");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				try{
//				ReportClientDocument rpt =  new ReportClientDocument();
//				rpt.open(System.getProperty("user.dir")+"\\report\\basItem.rpt", 0);
//			    rpt.getDatabaseController().logon("namacity", "namacity1542");
//			    Tables tables = rpt.getDatabaseController().getDatabase().getTables();
//			    for(int i=0; i< tables.size(); i++){
//			    	System.out.print(i);
//			        ITable table = tables.getTable(i);
//
//			        IConnectionInfo connInfo = table.getConnectionInfo();
//
////			        PropertyBag innerProp = connInfo.getAttributes();
////			        innerProp.clear();
////
////			        PropertyBag propertyBag = new PropertyBag();
////			        propertyBag.put("Server Type", "JDBC (JNDI)");
////			        propertyBag.put("Database DLL", "crdb_jdbc.dll");
////			        propertyBag.put("Connection String", DBConnect.getConnectionString());
////			        propertyBag.put("Database Class Name", "com.mysql.jdbc.Driver");
////			        propertyBag.put("Use JDBC", "true");
////			        propertyBag.put("Server Name", DBConnect.getServer());
////			        propertyBag.put("Generic JDBC Driver Behavior", "No");
////			        propertyBag.put("URI", "!com.mysql.jdbc.Driver!jdbc:mysql://"+DBConnect.getServer()+":"+DBConnect.getPort()+"/"+DBConnect.getDatabase()+"!ServerType=29!QuoteChar=`");
//
////			        connInfo.setAttributes(propertyBag);
////			        connInfo.setKind(ConnectionInfoKind.SQL);
//
//			        table.setConnectionInfo(connInfo);
//			        rpt.getDatabaseController().setTableLocation(table, tables.getTable(i));
//			    }
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				// 定义报表运行环境,才能执行报表    
//		         String envPath = "D:\\FineReport\\develop\\code\\build\\package\\WebReport\\WEB-INF";    
//		         FRContext.setCurrentEnv(new LocalEnv(envPath));    
//		         try {    
//		             TemplateWorkBook workbook = TemplateWorkBookIO.readTemplateWorkBook(FRContext.getCurrentEnv(), "GettingStarted.cpt");    
//		             // 参数传值    
//		             Parameter[] parameters = workbook.getParameters();    
//		             HashMap<String, String> paraMap = new HashMap<String, String>();  
//		             paraMap.put(parameters[0].getName(), "华北");  
//		               
//		             // java中调用报表打印方法    
//		             boolean a = PrintUtils.printWorkBook("GettingStarted.cpt", paraMap, true);    
//		             if (a == false) {    
//		                 System.out.println("失败啦！返回" + a);    
//		             } else {    
//		                 System.out.println("成功！返回" + a);    
//		             }    
//		         } catch (Exception e) {    
//		             e.printStackTrace();    
//		         }   
				
//				Message.showInfomationMessage(System.getProperty("user.dir")+"\\report\\CrystalNoUI.EXE "+
//						System.getProperty("user.dir")+"\\report\\basItem.rpt 0 item_code '0094-00040' ");
//				String exec =
//						System.getProperty("user.dir")+"\\report\\CrystalNoUI.EXE "+
//				System.getProperty("user.dir")+
//				"\\report\\basItem.rpt 0  ";
//				String exec ="CrystalNoUI.EXE basItem.rpt 1  ";
//			        try {
//						Process p = Runtime.getRuntime().exec(exec);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
			}
		});
		contentPane.add(btnNewButton, BorderLayout.NORTH);
	}

}
