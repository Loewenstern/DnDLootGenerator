import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class I18nProperties {
    private final Properties defaultProperties;
    private Properties localizedProperties;
    private final String baseName;
    private final Locale locale;

    public I18nProperties(String baseName, Locale locale){
        this.baseName = baseName;
        this.locale = locale;

        defaultProperties = new Properties();
        String fileName = baseName + ".properties";
        try(FileReader fileReader = new FileReader(fileName)){
            defaultProperties.load(fileReader);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        localizedProperties = new Properties();
        fileName = baseName + (locale.getLanguage().isEmpty() ? "" : "_" + locale.getLanguage()) + ".properties";
        try (FileReader fileReader = new FileReader(fileName)){
            localizedProperties.load(fileReader);
        }
        catch (Exception e){
            localizedProperties = defaultProperties;
        }
    }

    public String getProperty(String key){
        String s = localizedProperties.getProperty(key);
        if (s != null){
            return s;
        }
        else {
            return defaultProperties.getProperty(key);
        }
    }

    public void setProperty(String key, String defaultValue){
        defaultProperties.setProperty(key, defaultValue);
        if (locale.getLanguage().equals("en") || defaultProperties.getProperty(key).isEmpty()){
            defaultProperties.setProperty(key, defaultValue);
        }
        localizedProperties.setProperty(key, defaultValue);
    }

    public void save(){
        String fileName = baseName + ".properties";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            defaultProperties.store(fileWriter, null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        fileName = baseName + (locale.getLanguage().isEmpty() ? "" : "_" + locale.getLanguage()) + ".properties";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            localizedProperties.store(fileWriter, null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeProperty(String key){
        defaultProperties.remove(key);
        localizedProperties.remove(key);
        Properties tempProperties = new Properties();

        for (Locale locale : Main.getSupportedLocales()){
            if (locale != this.locale){
                String fileName = baseName + (locale.getLanguage().isEmpty() ? "" : "_" + locale.getLanguage()) + ".properties";
                try (FileReader fileReader = new FileReader(fileName)){
                    tempProperties.load(fileReader);
                    tempProperties.remove(key);
                    try(FileWriter fileWriter = new FileWriter(fileName)){
                        tempProperties.store(fileWriter, null);
                    }
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

}
