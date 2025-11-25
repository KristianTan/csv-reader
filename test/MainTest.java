import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.internal.junit.ArrayAsserts.assertArrayEquals;

class MainTest {

    @Test
    void testParseCsv() throws Exception {
        String csvData = "customerRef,customerName,addressLine1,addressLine2,town,county,country,postcode\n" +
                "123,John Doe,123 Street,,Townsville,CountyX,CountryY,AB12CD\n" +
                "456,Jane Smith,456 Road,Flat 2,Townsville,CountyX,CountryY,XY34ZZ";

        List<String[]> rows = Main.parseCsv(new StringReader(csvData));

        assertEquals(2, rows.size());
        assertArrayEquals(new String[]{"123","John Doe","123 Street","","Townsville","CountyX","CountryY","AB12CD"}, rows.get(0));
        assertArrayEquals(new String[]{"456","Jane Smith","456 Road","Flat 2","Townsville","CountyX","CountryY","XY34ZZ"}, rows.get(1));
    }

    @Test
    void testToJson() {
        String[] row = {"123", "John Doe", "123 Street", "", "Townsville", "CountyX", "CountryY", "AB12CD"};
        String expected = "{\"customerRef\":\"123\",\"customerName\":\"John Doe\",\"addressLine1\":\"123 Street\",\"addressLine2\":\"\",\"town\":\"Townsville\",\"county\":\"CountyX\",\"country\":\"CountryY\",\"postcode\":\"AB12CD\"}";
        assertEquals(expected, Main.toJson(row));
    }
}