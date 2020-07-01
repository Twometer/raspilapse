package de.twometer.raspilapse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class RaspiLapseMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        File configFile = new File("./raspilapse.properties");
        if (!configFile.exists()) {
            System.err.println("raspilapse.properties does not exist");
            return;
        }

        System.out.println("Reading config...");
        Properties config = new Properties();
        config.load(new FileReader(configFile.getAbsolutePath()));

        int delay = Integer.parseInt(config.getProperty("timeout"));
        String dst = config.getProperty("destination");
        String slash = dst.endsWith("/") ? "" : "/";

        System.out.println("Initializing camera...");
        Camera camera = new Camera();
        camera.start();

        System.out.println("Timelapse active");
        int imageNum = 0;
        while (true) {
            byte[] image = camera.captureImage();
            String filename = String.format("%s%s%d.jpg", dst, slash, imageNum);

            FileOutputStream outputStream = new FileOutputStream(filename);
            outputStream.write(image);
            outputStream.close();

            Thread.sleep(delay);
            imageNum++;
        }
    }

}
