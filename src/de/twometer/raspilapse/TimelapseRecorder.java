package de.twometer.raspilapse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TimelapseRecorder {

    private String destinationFolder;

    private String timelapseName;

    private int timeout;

    private volatile boolean running;

    public TimelapseRecorder(String destinationFolder, String videoName, int timeout) {
        this.destinationFolder = destinationFolder;
        this.timelapseName = videoName;
        this.timeout = timeout;
    }

    public void begin() throws InterruptedException, IOException {
        running = true;

        Logger.info("Initializing camera...");
        Camera camera = new Camera();
        camera.start();

        String folderPath = buildFolderPath();
        ensureExists(folderPath);

        Logger.info("Taking pictures...");
        int imageNumber = 1;
        while (running) {
            byte[] imageData = camera.captureImage();
            String imagePath = buildImagePath(folderPath, imageNumber);

            FileOutputStream outputStream = new FileOutputStream(imagePath);
            outputStream.write(imageData);
            outputStream.close();

            imageNumber++;
            Thread.sleep(timeout);
        }
    }

    public void stop() {
        this.running = false;
    }

    private String buildImagePath(String folderPath, int num) {
        return String.format("%s/%d.jpg", folderPath, num);
    }

    private String buildFolderPath() {
        if (!destinationFolder.endsWith("/"))
            destinationFolder += "/";
        return destinationFolder + timelapseName;
    }

    private void ensureExists(String folder) throws IOException {
        File file = new File(folder);
        if (!file.exists())
            if (!file.mkdir())
                throw new IOException("Failed to create directory!");
    }

}
