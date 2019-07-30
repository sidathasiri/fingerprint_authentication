import com.digitalpersona.uareu.*;

/**
 * Identify a user by comparing with registered fingerprints
 **/
public class Identifier {
    private Reader m_reader;
    private Engine engine = null;
    private Fmd[] storedData = new Fmd[1];
    private int falsepositive_rate;

    public Identifier(Reader reader, Fmd sample, int falsepositive_rate){
        this.m_reader = reader;
        storedData[0] = sample;
        engine = UareUGlobal.GetEngine();
        this.falsepositive_rate = falsepositive_rate;
    }

    /**
     * Continuously identify inputs from the scanner
     * */
    public void startIdentifying(){
        System.out.println("Place fingerprint to identify");
        while (true){
            //capture input from scanner
            Reader.CaptureResult cr = Util.captureData(m_reader, Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT, m_reader.GetCapabilities().resolutions[0], -1);
            //convert input FID to FMD
            Fmd fmdToIdentify = Util.convertToFMD(cr.image, Fmd.Format.ANSI_378_2004);

            try {
                //identify the matching candidates
                Engine.Candidate[] vCandidates = engine.Identify(fmdToIdentify, 0, storedData, falsepositive_rate, 1);
                if(vCandidates.length > 0){
                    //get false match rate
                    int falsematch_rate = engine.Compare(fmdToIdentify, 0, storedData[vCandidates[0].fmd_index], vCandidates[0].view_index);

                    String str = String.format("\nFingerprint identified");
                    System.out.println(str);
                    str = String.format("dissimilarity score: 0x%x.", falsematch_rate);
                    System.out.println(str);
                    str = String.format("false match rate: %e.\n----------------------------------", (double)(falsematch_rate / Engine.PROBABILITY_ONE));
                    System.out.println(str);
                }
                else{
                    System.out.println("Fingerprint was not identified.\n");
                }
            } catch (UareUException e) {
                e.printStackTrace();
            }
        }
    }
}
