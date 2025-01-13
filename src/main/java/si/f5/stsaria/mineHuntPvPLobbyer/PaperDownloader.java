package si.f5.stsaria.mineHuntPvPLobbyer;

public class PaperDownloader extends httpGet {
    private static String getDonwloadURL(String version){
        String versionInfo = contentString("http://api.papermc.io/v2/projects/paper/versions/" + version);
        System.out.println(versionInfo);
        if (versionInfo == ""){
            return "";
        }
        char[] versionInfoCharArray = versionInfo.toCharArray();

        StringBuilder buildNumberStringBuilder = new StringBuilder();
        boolean isDiscoveredDigit = false;
        for (int i = 0; i < versionInfoCharArray.length; i++){
            int reversedI = versionInfoCharArray.length-1-i;
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
        return "https://api.papermc.io/v2/projects/paper/versions/"+version+"/builds/"+buildNumberStringBuilder.toString()+"/downloads/paper-"+version+"-"+buildNumberStringBuilder.toString()+".jar";
    }
    public static boolean downloadLatestBuild(String version, String saveFileString){
        return download(getDonwloadURL(version), saveFileString);
    }
}
