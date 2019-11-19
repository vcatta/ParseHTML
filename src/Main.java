import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.*;
import java.util.ArrayList;

public class Main {

    private static final String PATTERN_URL = "(.+)\\/([0-9A-Za-z]+\\_[0-9A-Za-z]+)(\\.[A-Za-z]+)";

    public static void main(String[] args) throws IOException{
        ArrayList<String> url = new ArrayList<>();
        String target = "D:/test";

        try {
            Document doc = Jsoup.connect("https://lenta.ru/").get();
            Elements elements = doc.select("img");

            elements.forEach(element -> {
                if (element.attr("abs:src").matches(PATTERN_URL)) {
                    url.add(element.attr("abs:src"));
                }
            });

            url.forEach(S -> {
                try {
                    downloadImages(S, target);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Не удалось подключиться: " + ex.getMessage() + "\nПроверьте: \n1) соединение с интернетом, \n2) корректность введенного адреса");
        }
    }

    private static void downloadImages(String url, String targetFolder) throws Exception {

        String prefix = url.replaceAll(PATTERN_URL, "$2");
        String suffix = url.replaceAll(PATTERN_URL, "$3");
        URL link = new URL(url);

        InputStream in = link.openStream();
        StandardCopyOption[] opts = {StandardCopyOption.REPLACE_EXISTING};
        Path path = Files.createTempFile(Paths.get(targetFolder), prefix, suffix);
        Files.copy(in, path, opts);
    }

}
