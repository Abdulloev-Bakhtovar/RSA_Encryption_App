package com.example.rsaapp.controller;

import com.example.rsaapp.service.EncryptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

@Controller
public class FileController {

    private final EncryptionService encryptionService;

    public FileController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();

            KeyPair keys = encryptionService.generateKeyPair();
            byte[] encryptedData = encryptionService.encrypt(bytes, keys.getPublic());

            String encryptedDigits = encryptionService.bytesToDigits(encryptedData);
            Files.write(Paths.get("encrypted_file.txt"), encryptedDigits.getBytes());

            byte[] decryptedData = encryptionService.decrypt(encryptedData, keys.getPrivate());
            Files.write(Paths.get("decrypted_file.txt"), decryptedData);

            return ResponseEntity.ok("Файл успешно загружен и обработан!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Ошибка при обработке файла: " + e.getMessage());
        }
    }

    @GetMapping("/upload")
    public String uploadForm() {
        return "uploadForm";
    }

    @GetMapping("/keyDetails")
    public String getKeyDetails(Model model) throws NoSuchAlgorithmException {
        KeyPair keys = encryptionService.generateKeyPair();

        PublicKey publicKey = keys.getPublic();
        PrivateKey privateKey = keys.getPrivate();

        model.addAttribute("publicKey", publicKey.toString());
        model.addAttribute("privateKey", privateKey.toString());

        return "keyDetails";  // это будет имя вашего HTML-шаблона, который будет отображать детали ключей
    }
}
