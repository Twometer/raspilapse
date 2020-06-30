package de.twometer.raspilapse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Camera {
    private static byte[] buffer = new byte[131072];

    private Process process;

    public void start() throws IOException {
        process = Runtime.getRuntime().exec("raspistill -o - -t 0 -n -s");
    }

    public byte[] captureImage() throws IOException {
        if (!process.isAlive()) throw new IllegalStateException("Raspistill process died or could not be started");

        Runtime.getRuntime().exec("pkill -USR1 -x raspistill");
        InputStream stream = process.getInputStream();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4194304);

        int len;
        while (true) {
            if(stream.available() == 0) continue;
            len = stream.read(buffer);
            byteArrayOutputStream.write(buffer, 0, len);
            if (buffer[len - 1] == (byte) 0xD9 && buffer[len - 2] == (byte) 0xFF)
                break; // End of JPEG stream reached
        }

        return byteArrayOutputStream.toByteArray();
    }

    public void shutdown() {
        if (process == null) return;
        if (process.isAlive())
            process.destroy();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            System.err.println("Failed to wait for process");
        }
    }
}
