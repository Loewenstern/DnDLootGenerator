import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;
import java.text.ParseException;

public class CustomNumberFormatter extends NumberFormatter {


    public CustomNumberFormatter(NumberFormat format){
        super(format);
    }

    public Object stringToValue(String text) throws ParseException {
        if (text == null || text.trim().isEmpty()) {
            return null;   // allow empty field
        }
        return super.stringToValue(text);
    }
}
