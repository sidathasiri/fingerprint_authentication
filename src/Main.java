import com.digitalpersona.uareu.*;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String args[]) throws UareUException, SQLException {
        //get available reader
        Reader reader = Util.getAvailableReader();
        //connect with reader
        Util.connectToReader(Reader.Priority.COOPERATIVE);

        //enrollment
        Enrollment enrollment = new Enrollment(reader);
        Fmd fmd = enrollment.startEnrolling();
        DBHandler dbHandler = new DBHandler();
        dbHandler.insert(fmd.getData());
        List<DBHandler.Record> users = dbHandler.GetAllFPData();
        System.out.println("Users:"+users.size());
        dbHandler.closeConnection();

        //identification
        int falsepositive_rate = Engine.PROBABILITY_ONE / 100000;
        Identifier identifier = new Identifier(reader, fmd, falsepositive_rate);
        identifier.startIdentifying();
    }
}