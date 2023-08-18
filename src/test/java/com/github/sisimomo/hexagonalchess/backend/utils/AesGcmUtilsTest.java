package com.github.sisimomo.hexagonalchess.backend.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.sisimomo.hexagonalchess.backend.commons.utils.AesGcmUtils;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.IntStream;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;

class AesGcmUtilsTest {
  private final SecureRandom secureRandom = new SecureRandom();

  @Test
  void testEncryptionRandomKey() throws Exception {
    SecretKey secretKey = randomSecretKey();

    String message = "the secret message";

    byte[] cipherText = AesGcmUtils.encrypt(message, secretKey);

    String secretKeyBase64Encoded = secretKeyToString(secretKey);
    System.out.println("Secret key base64 encoded: " + secretKeyBase64Encoded);
    String decrypted = AesGcmUtils.decrypt(cipherText, secretKey);

    assertEquals(message, decrypted);
  }

  private SecretKey randomSecretKey() {
    byte[] key = new byte[16];
    secureRandom.nextBytes(key);
    return new SecretKeySpec(key, "AES");
  }

  private String secretKeyToString(SecretKey secretKey) {
    // get base64 encoded version of the key
    return Base64.getEncoder().encodeToString(secretKey.getEncoded());
  }

  @Test
  void testEncryptionBase64Key() throws Exception {
    String secretKeyBase64Encoded = "6weHaAAhSZ0qqc1O0a82lw==";
    SecretKey secretKey = AesGcmUtils.stringToSecretKey(secretKeyBase64Encoded);

    String message = "the secret message";

    byte[] cipherText = AesGcmUtils.encrypt(message, secretKey);
    String decrypted = AesGcmUtils.decrypt(cipherText, secretKey);

    assertEquals(message, decrypted);
  }

  @Test
  void performanceTest() throws Exception {
    SecretKey secretKey = randomSecretKey();

    List<String> randomStrings = IntStream.range(0, 1000000).mapToObj(i -> randomString(15)).toList();

    ArrayList<byte[]> encriptedStrings = new ArrayList<>();
    long startTime = System.currentTimeMillis();
    for (String randomString : randomStrings) {
      encriptedStrings.add(AesGcmUtils.encrypt(randomString, secretKey));
    }
    long stopTime = System.currentTimeMillis();
    long elapsedTime = stopTime - startTime;
    System.out.println("elapsedTime to encrypt 1 000 000 string: " + elapsedTime + "ms");


    startTime = System.currentTimeMillis();
    for (byte[] encriptedString : encriptedStrings) {
      AesGcmUtils.decrypt(encriptedString, secretKey);
    }
    stopTime = System.currentTimeMillis();
    elapsedTime = stopTime - startTime;
    System.out.println("elapsedTime to decrypt 1 000 000 string: " + elapsedTime + "ms");
  }

  public String randomString(int size) {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'

    return secureRandom.ints(leftLimit, rightLimit + 1).limit(size)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
  }

}
