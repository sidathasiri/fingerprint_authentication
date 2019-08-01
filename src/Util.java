import com.digitalpersona.uareu.*;

/**
 * This class contains utility methods to interact with device
 **/
public class Util {
    private static Reader reader = null;

    /**
     * Get the available reader
     **/
    public static Reader getAvailableReader(){
        ReaderCollection m_collection = null;
        if(reader == null){
            try {
                m_collection = UareUGlobal.GetReaderCollection();
                m_collection.GetReaders();
                if(m_collection.size()>0) {
                    reader = m_collection.get(0);
                } else {
                    throw new Error("No available readers");
                }
            } catch (UareUException e) {
                e.printStackTrace();
            }
        }
        return reader;
    }

    /**
     * Connect with the available reader
     **/
    public static void connectToReader(Reader.Priority priority){
        if(reader == null){
            getAvailableReader();
        }
        try {
            reader.Open(priority);
        } catch (UareUException e) {
            e.printStackTrace();
        }
    }

    /**
     * Capture the fingerprint from the device
     **/
    public static Reader.CaptureResult captureData(Reader m_reader, Fid.Format m_format, Reader.ImageProcessing m_proc, int resolution, int timeout){
        Reader.CaptureResult result = null;
        try {
            result = m_reader.Capture(m_format, m_proc, resolution, timeout);
        } catch (UareUException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Convert the FID to FMD format
     **/
    public static Fmd convertToFMD(Fid image, Fmd.Format format){
        Engine engine = UareUGlobal.GetEngine();
        Fmd fmd = null;
        try {
            fmd = engine.CreateFmd(image, format);
        } catch (UareUException e) {
            e.printStackTrace();
        }

        return fmd;
    }
}
