import com.digitalpersona.uareu.*;

public class Main {
    public static void main(String args[]) throws UareUException {
        //get available reader
        Reader reader = Util.getAvailableReader();
        //connect with reader
        Util.connectToReader(Reader.Priority.COOPERATIVE);

        //enrollment
        Enrollment enrollment = new Enrollment(reader);
        Fmd fmd = enrollment.startEnrolling();

        //identification
        int falsepositive_rate = Engine.PROBABILITY_ONE / 100000;
        Identifier identifier = new Identifier(reader, fmd, falsepositive_rate);
        identifier.startIdentifying();
    }
}