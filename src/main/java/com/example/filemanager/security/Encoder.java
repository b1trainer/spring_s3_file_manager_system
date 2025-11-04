package com.example.filemanager.security;

import com.example.filemanager.config.ApplicationConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class Encoder implements PasswordEncoder {

    private static final String SECRET_KEY_INSTANCE = "PBKDF2WithHmacSHA256";

    private final ApplicationConfig applicationConfig;

    public Encoder(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            return Base64.getEncoder().encodeToString(
                    SecretKeyFactory
                            .getInstance(SECRET_KEY_INSTANCE)
                            .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(),
                                    applicationConfig.getEncoderSecret().getBytes(),
                                    applicationConfig.getIterations(),
                                    applicationConfig.getKeyLength()))
                            .getEncoded()
            );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
