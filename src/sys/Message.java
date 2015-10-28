package sys;

import javax.swing.JOptionPane;

public class Message {
	public static void showErrorMessage(String message){
		JOptionPane.showMessageDialog(null,message,"错误",JOptionPane.ERROR_MESSAGE);
	}
	public static void showInfomationMessage(String message){
		JOptionPane.showMessageDialog(null,message,"提示",JOptionPane.INFORMATION_MESSAGE);
	}
	public static void showWarningMessage(String message){
		JOptionPane.showMessageDialog(null,message,"警告",JOptionPane.WARNING_MESSAGE);
	}
	public static boolean showOKorCancelMessage(String message){
		if(JOptionPane.showConfirmDialog(null,message,"确认",JOptionPane.OK_CANCEL_OPTION)==0){
			return true;
		}
		else{
			return false;
		}
		
	}
}
