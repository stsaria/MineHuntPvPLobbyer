package si.f5.stsaria.mineHuntPvPLobbyer;

import java.util.Objects;

public class PaperDownloader extends httpGet {
    private static String getDonwloadURL(String version){
        String versionInfo = contentString("https://api.papermc.io/v2/projects/paper/versions/" + version);
        if (Objects.equals(versionInfo, "")){
            return "";
        }
        char[] versionInfoCharArray = versionInfo.toCharArray();

        StringBuilder buildNumberStringBuilder = new StringBuilder();
        boolean isDiscoveredDigit = false;
        int reversedI;
        for (int i = 0; i < versionInfoCharArray.length; i++){
            reversedI = versionInfoCharArray.length-1-i;
            boolean isDigit = versionInfoCharArray[reversedI] >= '0' && versionInfoCharArray[reversedI] <= '9';
            if (isDigit){
                if (!isDiscoveredDigit){
                    isDiscoveredDigit = true;
                }
                buildNumberStringBuilder.append(versionInfoCharArray[reversedI]);
            } else if (isDiscoveredDigit){
                break;
            }
        }
        buildNumberStringBuilder.reverse();
        return "https://api.papermc.io/v2/projects/paper/versions/"+version+"/builds/"+ buildNumberStringBuilder+"/downloads/paper-"+version+"-"+buildNumberStringBuilder+".jar";
    }
    public static void downloadLatestBuild(String version, String saveFileString){
        download(getDonwloadURL(version), saveFileString);
    }
}
