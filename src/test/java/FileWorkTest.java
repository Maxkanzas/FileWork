import com.codeborne.pdftest.PDF;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.pdftest.PDF.containsText;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileWorkTest {

    private ClassLoader cl = FileWorkTest.class.getClassLoader();

    @Test
    void zipFileParsingTest() throws Exception{
        try (ZipInputStream zis = new ZipInputStream(cl.getResourceAsStream("Archive.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
                if (entry.getName().contains(".pdf")) {
                    PDF pdf = new PDF(zis);
                    assertThat(pdf, containsText("ИНН 6324016308 КПП 632101001"));
                    break;
                }
            }
        }
    }
}
