package sys;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DBUtil.Configuration;
import DBUtil.DBOperator;
import comUtil.MD5;
import comUtil.WMSCombobox;
import dmdata.DataManager;
import util.Math_SAM;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JComboBox;

public class loginFrm extends JFrame {

	private JPanel contentPane;
	private JTextField txt_user_code;
	private JPasswordField txt_user_pwd;
	private JButton btnLogin;
	private WMSCombobox cbWarehouse;
	Configuration saveCf = new Configuration(System.getProperty("user.dir")+"/logon.properties");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginFrm frame = new loginFrm();
					Toolkit toolkit = Toolkit.getDefaultToolkit();
					int x = (int)(toolkit.getScreenSize().getWidth()-frame.getWidth())/2;
					int y = (int)(toolkit.getScreenSize().getHeight()-frame.getHeight())/2;
					frame.setLocation(x, y);
					frame.setResizable(false);
					frame.setMinimumSize(new Dimension(320,320));
					frame.setMaximumSize(new Dimension(320,320));
					frame.setUndecorated(true);
					frame.setVisible(true);
					frame.txt_user_code.setFocusable(true);
					frame.txt_user_code.requestFocus();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public loginFrm() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(loginFrm.class.getResource("/images/logo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 387, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u8D26\u6237\u540D\uFF1A");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		lblNewLabel.setBounds(64, 73, 65, 15);
		panel.add(lblNewLabel);
		
		txt_user_code = new JTextField();
		txt_user_code.setForeground(Color.MAGENTA);
		txt_user_code.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar()=='\n'){
					txt_user_pwd.requestFocus();
					txt_user_pwd.selectAll();
				}
			}
		});
		txt_user_code.setBounds(129, 70, 155, 21);
		panel.add(txt_user_code);
		txt_user_code.setColumns(10);
		
		JLabel lbll = new JLabel("\u5BC6  \u7801\uFF1A");
		lbll.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		lbll.setBounds(64, 119, 65, 15);
		panel.add(lbll);
		
		txt_user_pwd = new JPasswordField();
		txt_user_pwd.setForeground(Color.GRAY);
		txt_user_pwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyChar()=='\n'){
					btnLogin.requestFocus();
					btnLogin.doClick();
				}
			}
		});
		txt_user_pwd.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				
			}
		});
		txt_user_pwd.setColumns(12);
		txt_user_pwd.setBounds(129, 116, 155, 21);
		panel.add(txt_user_pwd);
		
	    btnLogin = new JButton("\u767B\u5F55");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userCode = txt_user_code.getText().trim();
				String userPwd = txt_user_pwd.getText().trim();
				String warehouseCode = cbWarehouse.getSelectedOID();
				String warehouseName = cbWarehouse.getSelectedItem().toString();
				if(warehouseCode.trim().equals("")){
					Message.showWarningMessage("请选择仓库！");
					cbWarehouse.requestFocus();
					return;
				}
				
		        saveCf.setValue("wms.userwarehouse", warehouseCode);
		        saveCf.saveFile(System.getProperty("user.dir")+"/logon.properties","");
		        MD5 getMD5 = new MD5();
				String password = getMD5.GetMD5Code(userPwd);
				String sql = "select '"+warehouseCode+"' CUR_WAREHOUSE_CODE,WAREHOUSE_CODE,USER_CODE,USER_NAME,ROLE_NAME,ACTIVE from sys_user "
				+"where (user_code= '"+userCode+"' or login_code = '"+userCode+"') and password='"+password+"' ";
				if(sqlValidate(sql)){
					DataManager dm = DBOperator.DoSelect2DM(sql);
					if(dm==null || dm.getCurrentCount()==0){
						Message.showWarningMessage("账户名和密码不正确");
						txt_user_code.requestFocus();
						txt_user_code.selectAll();
						return;
					}else{
						if(!dm.getString("ACTIVE", 0).toString().equals("true")){
							Message.showWarningMessage("账户已停用");
							txt_user_code.requestFocus();
							txt_user_code.selectAll();
							return;
						}
						MainFrm.setUserInfo(dm);
						MainFrm.createUI();
						setVisible(false);
					}
				}else{
					Message.showErrorMessage("登录信息输入不正确!");
					txt_user_code.requestFocus();
					txt_user_code.selectAll();
					return;
				}
			}
		});
		btnLogin.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		btnLogin.setBounds(129, 163, 71, 30);
		panel.add(btnLogin);
		
		JButton btnClose = new JButton("\u9000\u51FA");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnClose.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		btnClose.setBounds(213, 163, 71, 30);
		panel.add(btnClose);
		
		JLabel labelVersion = new JLabel("\u7248\u672C\u53F7\uFF1A");
		labelVersion.setForeground(Color.BLUE);
		labelVersion.setBounds(0, 0, 259, 15);
		panel.add(labelVersion);
		
		//登录时候判断是否要版本更新
		labelVersion.setText("当前版本号:"+MainFrm.wmsVersion);
		checkLogonFile();
		
		JLabel lbll_1 = new JLabel("\u4ED3  \u5E93\uFF1A");
		lbll_1.setBounds(62, 40, 54, 15);
		panel.add(lbll_1);
		
	    cbWarehouse = new WMSCombobox("select distinct warehouse_code,warehouse_name from bas_warehouse",true);
	    cbWarehouse.setForeground(Color.BLUE);
	    cbWarehouse.setSelectedDisplayName(saveCf.getValue("wms.userwarehouse"));
		cbWarehouse.setBounds(129, 37, 155, 21);
		panel.add(cbWarehouse);
		String curVersion = MainFrm.wmsVersion;
		String sysVersion = getSysVersion();
		if(Math_SAM.str2Double(curVersion)<Math_SAM.str2Double(sysVersion)){
			boolean update = Message.showOKorCancelMessage("WMS版本信息过低,请更新最新版本!");
			if(update){
				Process process;
				Runtime runtime = Runtime.getRuntime(); 
				try {
					runtime.exec("cmd /c java -jar "+System.getProperty("user.dir")+"/update.jar");
					System.exit(0);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}else{
				System.exit(0);
			}
			
		}
	}
	
	private void checkLogonFile(){
		File f = new File(System.getProperty("user.dir")+"/logon.properties");
		if(!f.exists()){
			new SwingWorker<String, Void>() {

	            @Override
	            protected String doInBackground() throws Exception {
	            	httpDownload("http://www.ajyaguru.com/download/logon.properties",System.getProperty("user.dir")+"/logon.properties");
	                return "";
	            }

	            @Override
	            protected void done() {
	                System.out.println("后台方法运行结束");
					System.exit(0);
	            }
	        }.execute();
		}
		
		f = new File(System.getProperty("user.dir")+"/update.jar");
		if(!f.exists()){
			new SwingWorker<String, Void>() {

	            @Override
	            protected String doInBackground() throws Exception {
	            	httpDownload("http://www.ajyaguru.com/download/update.jar",System.getProperty("user.dir")+"/update.jar");
	                return "";
	            }

	            @Override
	            protected void done() {
	                System.out.println("后台方法运行结束");
					System.exit(0);
	            }
	        }.execute();
		}
	}
	
	//效验
    protected static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }
    
    private String getSysVersion(){
    	String sql = "select SYS_VERSION_CODE,SYS_VERSION_NAME from sys_version where SYS_VERSION_CLIENT='WMS' order by SYS_VERSION_CODE desc limit 1";
    	DataManager dm = DBOperator.DoSelect2DM(sql);
    	if(dm==null || dm.getCurrentCount()==0){
    		return "";
    	}else{
    		MainFrm.setVersionInfo(dm);
    		return dm.getString("SYS_VERSION_CODE", 0);
    	}
    }
    
    /**http下载*/
	public static boolean httpDownload(String httpUrl,String saveFile){
        // 下载网络文件
        int bytesum = 0;
        int byteread = 0;

        URL url = null;
		try {
			url = new URL(httpUrl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(saveFile);

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
