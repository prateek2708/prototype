package com.zoomsystems.replenisher.poc.qr.gen;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import net.glxn.qrgen.QRCode;

public class GenerateQRCode {
    public static void main(String[] args) {
        // override image size to be 250x250
        ByteArrayOutputStream qrCode = QRCode.from(UUID.randomUUID().toString()).withSize(250, 250).stream();
        try {
            OutputStream out = new FileOutputStream("qr-code.png");
            qrCode.writeTo(out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
