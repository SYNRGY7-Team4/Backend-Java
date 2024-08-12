package synrgy.team4.backend.service;

import com.google.zxing.WriterException;

import java.awt.image.BufferedImage;
import java.io.InputStream;

public interface QRCodeService {
    BufferedImage generateQRCodeImage(String text) throws WriterException;
    String decodeQRCode(InputStream qrCodeImage) throws Exception;

}
