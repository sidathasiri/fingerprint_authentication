import com.digitalpersona.uareu.*;

/**
 * Fingerprint enrolment for new users
 **/
public class Enrollment{

    private Reader m_reader;
    private Engine engine;
    public Enrollment(Reader reader){
        this.m_reader = reader;
        //acquire engine
        engine = UareUGlobal.GetEngine();
    }

    /**
     * Callback method on every scanning iteration in enrolling process
     **/
    private Engine.PreEnrollmentFmd GetFmd(Fmd.Format format){
        Engine.PreEnrollmentFmd prefmd = null;
        Reader.CaptureResult cr = null;
        System.out.println("Place finger");
        //capture data from reader
        cr = Util.captureData(m_reader, Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT, m_reader.GetCapabilities().resolutions[0], -1);

        //convert to FMD
        Fmd fmd = Util.convertToFMD(cr.image, Fmd.Format.ANSI_378_2004);
        System.out.println("Scan quality:"+cr.quality);
        System.out.println(fmd.getData());
        prefmd = new Engine.PreEnrollmentFmd();
        prefmd.fmd = fmd;
        prefmd.view_index = 0;
        return prefmd;
    }

    /**
     * Start the enrolling process
     **/
    public Fmd startEnrolling(){
        Fmd fmd = null;
        try{
            System.out.println("Device name:"+m_reader.GetDescription().name);
            System.out.println("start enrolling");

            //start enrolling
            fmd = engine.CreateEnrollmentFmd(Fmd.Format.ANSI_378_2004, this::GetFmd);
            System.out.println("Enroll Complete");
            return fmd;
        } catch (UareUException e) {
            e.printStackTrace();
        }

        return fmd;
    }
}
