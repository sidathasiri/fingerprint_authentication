import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler class to interact with DB
 **/
public class DBHandler {
    private Connection conn = null;
    private String tableName;
    private String col1;

    /**
     * Model to store the fingerprint data
     **/
    public class Record {
        byte[] fmdBinary;

        Record(byte[] fmd) {
            fmdBinary=fmd;
        }
    }

    public DBHandler(){
        tableName = "users";
        col1 = "data";
    }

    /**
     * Connect with the DB
     **/
    public void connect(){
        if(conn == null){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/biometic_prototype","root","root");
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }

    /**
     * Insert a new fingerprint data to DB
     **/
    public void insert(byte[] data) throws SQLException {
        connect();
        String preppedStmtInsert= "INSERT INTO " + tableName + "(" + col1 + ") VALUES(?)";
        PreparedStatement pst= conn.prepareStatement(preppedStmtInsert);
        pst.setBytes(1, data);
        pst.execute();
        System.out.println("Added to database");
    }

    /**
     * Fetch all the saved fingerprints from the DB
     **/
    public List<Record> GetAllFPData() throws SQLException {
        List<Record> listUsers=new ArrayList<Record>();

        String sqlStmt="Select * from "+ tableName;
        Statement st=conn.createStatement();
        ResultSet rs = st.executeQuery(sqlStmt);
        while(rs.next())
        {
            if(rs.getBytes(col1)!=null)
                listUsers.add(new Record(rs.getBytes(col1)));
        }
        closeConnection();
        return listUsers;
    }

    /**
     * Terminate the DB connection
     * */
    public void closeConnection() throws SQLException {
        conn.close();
    }

}
