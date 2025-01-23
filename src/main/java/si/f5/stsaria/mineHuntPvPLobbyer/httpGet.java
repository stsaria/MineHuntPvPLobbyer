package si.f5.stsaria.mineHuntPvPLobbyer;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class httpGet {
    private static InputStream getIS(String url){
        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(url).toURL().openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            String newUrl;
            URI tempUrl;
            while (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                newUrl = connection.getHeaderField("Location");
                tempUrl = URI.create(newUrl);
                if (!tempUrl.isAbsolute()){
                    tempUrl = URI.create(url).resolve(newUrl);
                }
                connection = (HttpURLConnection) tempUrl.toURL().openConnection();
                connection.setInstanceFollowRedirects(false);
                responseCode = connection.getResponseCode();
            }
            return connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String contentString(String url){
        InputStream is = getIS(url);
        if (is == null){
            return "";
        }
        try{
            return IOUtils.toString(is, Charset.defaultCharset());
        } catch (Exception ignore) {
            return "";
        }
    }
    public static void download(String url, String saveFileString){
        InputStream is = getIS(url);
        if (is == null){
            return;
        }
        try{
            Files.copy(is, Paths.get(saveFileString));
        } catch (Exception ignore) {}
    }
}
