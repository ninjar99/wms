package sys;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DBUtil.DBOperator;
import comUtil.MD5;
import dmdata.DataManager;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChangePWD extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1037031694376810144L;
	private final JPanel contentPanel = new JPanel();
	private JPasswordField txt_OldPwd;
	private JPasswordField txt_NewPwd1;
	private JPasswordField txt_NewPwd2;
	private JTextField txt_UserCode;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ChangePWD dialog = new ChangePWD();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setSize(300,300);
			dialog.setLocation(400,400);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ChangePWD() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("\u539F\u5BC6\u7801\uFF1A");
			lblNewLabel.setFont(new Font("宋体", Font.BOLD, 12));
			lblNewLabel.setBounds(10, 55, 86, 15);
			contentPanel.add(lblNewLabel);
		}
		
		txt_OldPwd = new JPasswordField();
		txt_OldPwd.setBounds(95, 52, 143, 21);
		contentPanel.add(txt_OldPwd);
		
		JLabel label = new JLabel("\u65B0\u5BC6\u7801\uFF1A");
		label.setFont(new Font("宋体", Font.BOLD, 12));
		label.setBounds(10, 88, 86, 15);
		contentPanel.add(label);
		
		txt_NewPwd1 = new JPasswordField();
		txt_NewPwd1.setBounds(95, 85, 143, 21);
		contentPanel.add(txt_NewPwd1);
		
		JLabel label_1 = new JLabel("\u65B0\u5BC6\u7801\u786E\u8BA4\uFF1A");
		label_1.setFont(new Font("宋体", Font.BOLD, 12));
		label_1.setBounds(10, 123, 86, 15);
		contentPanel.add(label_1);
		
		txt_NewPwd2 = new JPasswordField();
		txt_NewPwd2.setBounds(95, 120, 143, 21);
		contentPanel.add(txt_NewPwd2);
		
		JLabel label_2 = new JLabel("\u8D26\u6237\u540D\uFF1A");
		label_2.setFont(new Font("宋体", Font.BOLD, 12));
		label_2.setBounds(10, 24, 86, 15);
		contentPanel.add(label_2);
		
		txt_UserCode = new JTextField();
		txt_UserCode.setBounds(95, 24, 143, 21);
		contentPanel.add(txt_UserCode);
		txt_UserCode.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("\u786E\u8BA4");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String usercode = txt_UserCode.getText().trim();
						String oldpwd = txt_OldPwd.getText().trim();
						String newpwd1 = txt_NewPwd1.getText().trim();
						String newpwd2 = txt_NewPwd2.getText().trim();
						if(usercode.equals("")){
							Message.showWarningMessage("请输入登录账户");
							txt_UserCode.requestFocus();
							return;
						}
						if(oldpwd.equals("")){
							Message.showWarningMessage("请输入原密码");
							txt_OldPwd.requestFocus();
							return;
						}
						if(newpwd1.equals("")){
							Message.showWarningMessage("请输入新密码");
							txt_NewPwd1.requestDefaultFocus();
							return;
						}
						if(!newpwd1.equals(newpwd2)){
							Message.showWarningMessage("新密码两次输入不匹配");
							txt_NewPwd1.setText("");
							txt_NewPwd2.setText("");
							txt_NewPwd1.requestFocus();
							return;
						}
						String sql = "select USER_CODE from sys_user where USER_CODE='"+usercode+"' "
								+ "and PASSWORD='"+MD5.GetMD5Code(oldpwd)+"' ";
						DataManager dm = DBOperator.DoSelect2DM(sql);
						if(dm.getString("USER_CODE", 0).equals("")){
							Message.showErrorMessage("原密码输入不正确");
							txt_OldPwd.requestFocus();
							return;
						}else if(dm.getString("USER_CODE", 0).equals(usercode)){
							sql = "update sys_user set PASSWORD='"+MD5.GetMD5Code(newpwd1)+"' "
								+ "where USER_CODE='"+usercode+"' and "
								+ "PASSWORD='"+MD5.GetMD5Code(oldpwd)+"' ";
							int t = DBOperator.DoUpdate(sql);
							if(t==1){
								Message.showInfomationMessage("密码修改成功，请重新登录");
								return;
							}else{
								Message.showErrorMessage("密码更新失败，请重试");
								return;
							}
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}
