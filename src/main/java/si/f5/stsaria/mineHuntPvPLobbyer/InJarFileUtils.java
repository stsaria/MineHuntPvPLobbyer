package si.f5.stsaria.mineHuntPvPLobbyer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class InJarFileUtils {
    public static void copyResourcesFileToLocalFile(String fromFileName, String toFileName){
        InputStream fromFileStream = InJarFileUtils.class.getResourceAsStream("/"+fromFileName);
        if (fromFileStream == null) return;
        try{
            OutputStream toFileStream = new FileOutputStream(toFileName);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fromFileStream.read(buffer)) != -1) {
                toFileStream.write(buffer, 0, bytesRead);
            }
            toFileStream.close();
        } catch (Exception ignored) {}
        try{
            fromFileStream.close();
        } catch (Exception ignored){}
    }
}
