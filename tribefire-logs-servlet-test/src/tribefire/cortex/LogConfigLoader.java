package tribefire.cortex;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

public class LogConfigLoader {
    public static void load() {
        try (FileInputStream fis = new FileInputStream("res/logging.properties")) {
            LogManager logManager = LogManager.getLogManager();
            logManager.readConfiguration(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
