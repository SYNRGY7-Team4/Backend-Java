package synrgy.team4.backend.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import synrgy.team4.backend.model.dto.response.UserResponse;
import synrgy.team4.backend.service.QRCodeService;
import synrgy.team4.backend.service.UserService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/qrcode")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/scan")
    @ResponseBody
    public ResponseEntity<String> scanQRCode(@RequestParam("file") MultipartFile file) {
        log.info("Scanning QR code");
        try {
            if (file.isEmpty()) {
                log.warn("Uploaded file is empty");
                return ResponseEntity.badRequest().body("File is empty");
            }
            log.info("File received: {}, size: {}", file.getOriginalFilename(), file.getSize());
            InputStream inputStream = file.getInputStream();
            String decodedText = qrCodeService.decodeQRCode(inputStream);
            return ResponseEntity.ok(decodedText);
        } catch (Exception e) {
            log.error("Error decoding QR code", e);
            return ResponseEntity.status(500).body("Failed to decode QR code");
        }
    }


    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> generateQRCode() {
        try {
            UserResponse userResponse = userService.getUserResponse();
            String text = String.format("Name: %s\nAccount Number: %s", userResponse.getName(), userResponse.getAccountNumber());
            BufferedImage qrCodeImage = qrCodeService.generateQRCodeImage(text);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrCodeImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            log.info("QR code generated successfully");
            return ResponseEntity.ok().headers(headers).body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
