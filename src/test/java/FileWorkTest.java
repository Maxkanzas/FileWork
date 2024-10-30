import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.pdftest.PDF.containsText;
import static org.apache.poi.xssf.usermodel.XSSFWorkbookType.XLSX;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileWorkTest {

    private ClassLoader cl = FileWorkTest.class.getClassLoader();

    @Test
    void zipFileParsingPdfTest() throws Exception{
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
    @Test
    void zipFileParsingXlsxTest() throws Exception{
        try (ZipInputStream zis = new ZipInputStream(cl.getResourceAsStream("Archive.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
                if (entry.getName().contains(".xlsx")) {
                    XLS xls = new XLS(zis);
                    Assertions.assertEquals(xls.excel.getSheetAt(0).getRow(0).getCell(1).getStringCellValue(), "Семейное положение");
                    break;
                }
            }
        }
    }
    @Test
    void zipFileParsingCsvTest() throws Exception{
        try (ZipInputStream zis = new ZipInputStream(cl.getResourceAsStream("Archive.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
                if (entry.getName().contains(".csv")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> data = csvReader.readAll();
                    Assertions.assertArrayEquals(new String[] {"Russia; Moscow"}, data.get(0));
                    Assertions.assertArrayEquals(new String[] {"USA; Washington"}, data.get(1));
                    break;
                    }
                }
            }
        }
    }
