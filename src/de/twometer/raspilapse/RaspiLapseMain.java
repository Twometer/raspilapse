package de.twometer.raspilapse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class RaspiLapseMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 1) {
            System.out.println("Usage: raspilapse <video_name>");
            return;
        }

        File configFile = new File("./raspilapse.properties");
        if (!configFile.exists()) {
            Logger.error("Config file raspilapse.properties not found");
            return;
        }

        Properties config = readConfigFile(configFile);

        String destFolder = config.getProperty("destination");
        String videoName = args[0];
        int timeout = Integer.parseInt(config.getProperty("timeout"));

        TimelapseRecorder recorder = new TimelapseRecorder(destFolder, videoName, timeout);
        recorder.begin();
    }

    private static Properties readConfigFile(File configFile) throws IOException {
        Properties config = new Properties();
        config.load(new FileReader(configFile.getAbsolutePath()));
        return config;
    }

}
