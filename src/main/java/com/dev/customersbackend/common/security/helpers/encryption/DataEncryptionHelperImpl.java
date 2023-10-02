package com.dev.customersbackend.common.security.helpers.encryption;

import com.dev.customersbackend.common.security.config.SecurityConstants;
import com.dev.customersbackend.common.security.exceptions.UnableToDecryptDataException;
import com.dev.customersbackend.common.security.exceptions.UnableToEncryptDataException;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class DataEncryptionHelperImpl implements DataEncryptionHelper {
    private final SecurityConstants constants;
    private static final String ENCRYPT_METHOD = "AES";
    private static final String ENCRYPT_TRANSFORMATION = "AES/CBC/PKCS5PADDING";

    public DataEncryptionHelperImpl(SecurityConstants constants) {
        this.constants = constants;
    }

    @Override
    public String encrypt(String property) {
        try {
            IvParameterSpec param = new IvParameterSpec(constants.getAesKeyOrThrowsException().getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secret = new SecretKeySpec(constants.getAesKeyOrThrowsException().getBytes(StandardCharsets.UTF_8), ENCRYPT_METHOD);
            Cipher cipher = Cipher.getInstance(ENCRYPT_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secret, param);
            byte[] bytes = cipher.doFinal(property.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new UnableToEncryptDataException("Não foi possível criptografar os dados.");
        }
    }

    @Override
    public String decrypt(String property) {
        try {
            IvParameterSpec param = new IvParameterSpec(constants.getAesKeyOrThrowsException().getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secret = new SecretKeySpec(constants.getAesKeyOrThrowsException().getBytes(StandardCharsets.UTF_8), ENCRYPT_METHOD);
            Cipher cipher = Cipher.getInstance(ENCRYPT_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secret, param);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(property));
            return new String(bytes);
        } catch (Exception exception) {
            throw new UnableToDecryptDataException("Não foi possível descriptografar os dados.");
        }
    }
}
