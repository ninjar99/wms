package DBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class TestJSch {

	static int lport = 8740;//本地端口
	static String rhost = "10.10.119.161";//远程MySQL服务器
	static int rport = 3306;//远程MySQL服务端口

	public static void bindSSH() {
		String user = "root";//SSH连接用户名
		String password = "540hs0qos";//SSH连接密码
		String host = "123.59.82.190";//SSH服务器
		int port = 22;//SSH访问端口
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			System.out.println(session.getServerVersion());//这里打印SSH服务器版本信息
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
			//通过SSH把远程服务器端口映射到本机端口，再通过本机链接访问远程数据库
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
