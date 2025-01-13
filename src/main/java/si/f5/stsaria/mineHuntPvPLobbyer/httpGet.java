package si.f5.stsaria.mineHuntPvPLobbyer;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class httpGet {
    public static String contentString(String url){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            String newUrl;
            while (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                newUrl = connection.getHeaderField("Location");
                connection = (HttpURLConnection) new URL(newUrl).openConnection();
                connection.setInstanceFollowRedirects(false);
                responseCode = connection.getResponseCode();
            }
            return IOUtils.toString(connection.getInputStream(), Charset.defaultCharset());
        } catch (Exception ignore) {
            return "";
        }
    }
    public static boolean download(String url, String saveFileString){
        try{
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            String newUrl;
            while (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                newUrl = connection.getHeaderField("Location");
                connection = (HttpURLConnection) new URL(newUrl).openConnection();
                connection.setInstanceFollowRedirects(false);
                responseCode = connection.getResponseCode();
            }
            Files.copy(connection.getInputStream(), Paths.get(saveFileString));
        } catch (Exception ignore) {
            return false;
        }
        return true;
    }
}
