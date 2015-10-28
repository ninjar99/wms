package DBUtil;

import java.sql.*;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class sqlConnection {
    private static Connection connection = null;
    private static Session session = null;

    private static void connectToServer(String dataBaseName) throws SQLException {
        connectSSH();
        connectToDataBase(dataBaseName);
    }

    private static void connectSSH() throws SQLException {
        String sshHost = "";
        String sshuser = "";
        String dbuserName = "";
        String dbpassword = "";
        String SshKeyFilepath = "/Users/XXXXXX/.ssh/id_rsa";

        int localPort = 8740; // any free port can be used
        String remoteHost = "127.0.0.1";
        int remotePort = 3306;
        String localSSHUrl = "localhost";
        /***************/
        String driverName = "com.mysql.jdbc.Driver";

        try {
            java.util.Properties config = new java.util.Properties();
            JSch jsch = new JSch();
            session = jsch.getSession(sshuser, sshHost, 22);
            jsch.addIdentity(SshKeyFilepath);
            config.put("StrictHostKeyChecking", "no");
            config.put("ConnectionAttempts", "3");
            session.setConfig(config);
            session.connect();

            System.out.println("SSH Connected");

            Class.forName(driverName).newInstance();

            int assinged_port = session.setPortForwardingL(localPort, remoteHost, remotePort);

            System.out.println("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
            System.out.println("Port Forwarded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void connectToDataBase(String dataBaseName) throws SQLException {
        String dbuserName = "sf2_showpad_biz";
        String dbpassword = "lOAWEnL3K";
        int localPort = 8740; // any free port can be used
        String localSSHUrl = "localhost";
        try {

            //mysql database connectivity
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(localSSHUrl);
            dataSource.setPortNumber(localPort);
            dataSource.setUser(dbuserName);
            dataSource.setAllowMultiQueries(true);

            dataSource.setPassword(dbpassword);
            dataSource.setDatabaseName(dataBaseName);

            connection = dataSource.getConnection();

            System.out.print("Connection to server successful!:" + connection + "\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void closeConnections() {
        CloseDataBaseConnection();
        CloseSSHConnection();
    }

    private static void CloseDataBaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Closing Database Connection");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void CloseSSHConnection() {
        if (session != null && session.isConnected()) {
            System.out.println("Closing SSH Connection");
            session.disconnect();
        }
    }


    // works ONLY FOR  single query (one SELECT or one DELETE etc)
    private static ResultSet executeMyQuery(String query, String dataBaseName) {
        ResultSet resultSet = null;

        try {
            connectToServer(dataBaseName);
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(query);
            System.out.println("Database connection success");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static void DeleteOrganisationReferencesFromDB(String organisationsLike) {
        try {
            connectToServer("ServerName");
            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("select * from DB1");

            String organisationsToDelete = "";
            List<String> organisationsIds = new ArrayList<String>();

            // create string with id`s values to delete organisations references
            while (resultSet.next()) {
                String actualValue = resultSet.getString("id");
                organisationsIds.add(actualValue);
            }

            for (int i = 0; i < organisationsIds.size(); i++) {
                organisationsToDelete = " " + organisationsToDelete + organisationsIds.get(i);
                if (i != organisationsIds.size() - 1) {
                    organisationsToDelete = organisationsToDelete + ", ";
                }
            }

            stmt.executeUpdate(" DELETE FROM `DB1`.`table1` WHERE `DB1`.`table1`.`organisation_id` in ( " + organisationsToDelete + " );");


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    public static List<String> getOrganisationsDBNamesBySubdomain(String organisationsLike) {
        List<String> organisationDbNames = new ArrayList<String>();
        ResultSet resultSet = executeMyQuery("select `DB`.organisation.dbname from `DB1`.organisation where subdomain like '" + organisationsLike + "%'", "DB1");
        try {
            while (resultSet.next()) {
                String actualValue = resultSet.getString("dbname");
                organisationDbNames.add(actualValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return organisationDbNames;
    }

     public static List<String> getAllDBNames() {
        // get all live db names incentral DB
        List<String> organisationDbNames = new ArrayList<String>();
        ResultSet resultSet = executeMyQuery("show databases", "DB1");
        try {
            while (resultSet.next()) {
                String actualValue = resultSet.getString("Database");
                organisationDbNames.add(actualValue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
        return organisationDbNames;
    }

      public static void deleteDataBasesByName(List<String> DataBasesNamesList) {
        try {
            connectSSH();
            int dataBasesAmount = DataBasesNamesList.size();
            for (int i = 0; i < dataBasesAmount; i++) {
                connectToDataBase(DataBasesNamesList.get(i));

                Statement stmt = connection.createStatement();
                stmt.executeUpdate("DROP database `" + DataBasesNamesList.get(i) + "`");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseDataBaseConnection();
            closeConnections();
        }
    }
}
