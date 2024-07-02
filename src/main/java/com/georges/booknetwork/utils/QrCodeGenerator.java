package com.georges.booknetwork.utils;

import com.georges.booknetwork.domains.Scientist;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QrCodeGenerator {

    public static void generateQrCode(Scientist scientist) throws WriterException, IOException {
        String directoty = "/home/georges/Desktop/Projects/";
        String qrCodeName = directoty.concat(scientist.getName()).concat(String.valueOf(scientist.getId())).concat("-QRCODE.png");
        var qeCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qeCodeWriter.encode("ID: " + scientist.getId() + "\n" +
                              "Name: " + scientist.getName() + "\n" +
                              "Description: " + scientist.getDescription() + "\n" +
                              "CreateAt: " + scientist.getCreationDate(), BarcodeFormat.QR_CODE, 400, 400);
        Path path = FileSystems.getDefault().getPath(qrCodeName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
