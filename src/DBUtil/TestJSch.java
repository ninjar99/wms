package DBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class TestJSch {

	static int lport = 8740;//���ض˿�
	static String rhost = "10.10.119.161";//Զ��MySQL������
	static int rport = 3306;//Զ��MySQL����˿�

	public static void bindSSH() {
		String user = "root";//SSH�����û���
		String password = "540hs0qos";//SSH��������
		String host = "123.59.82.190";//SSH������
		int port = 22;//SSH���ʶ˿�
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			System.out.println(session.getServerVersion());//�����ӡSSH�������汾��Ϣ
			int assinged_port = session.setPortForwardingL(lport, rhost, rport);
			System.out.println("localhost:" + assinged_port + " -> " + rhost + ":" + rport);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sql() throws SQLException {
		Connection conn = null;
		ResultSet rs = null;
		Statement st = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//ͨ��SSH��Զ�̷������˿�ӳ�䵽�����˿ڣ���ͨ���������ӷ���Զ�����ݿ�
			conn = DriverManager.getConnection("jdbc:mysql://localhost:8740/aggwms", "root", "namacity1524");
			st = conn.createStatement();
			String sql = "select * from inb_po_header";
			rs = st.executeQuery(sql);
			while (rs.next())
				System.out.println(rs.getString(1));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			rs.close();
			st.close();
			conn.close();
		}
	}

	public static void main(String[] args) {
//		bindSSH();
		try {
			sql();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
