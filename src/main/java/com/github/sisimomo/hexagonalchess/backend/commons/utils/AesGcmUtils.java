package com.github.sisimomo.hexagonalchess.backend.commons.utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * See <a href="https://www.baeldung.com/java-cipher-class">Baeldung Guide to the Cipher Class</a>
 * for more details.
 * <a href="https://gist.github.com/patrickfav/7e28d4eb4bf500f7ee8012c4a0cf7bbf">Source</a>.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AesGcmUtils {

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private static final int GCM_SALT_LENGTH = 12;

  /**
   * The transformation AES/GCM/NoPadding tells the getInstance method to instantiate the Cipher
   * object as an <a href="https://en.wikipedia.org/wiki/Advanced_Encryption_Standard">AES</a> cipher
   * with GCM <a href="https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation">mode of
   * operation</a> and NoPadding
   * <a href="https://en.wikipedia.org/wiki/Padding_(cryptography)">padding scheme</a>.
   */
  private static final String AES = "AES/GCM/NoPadding";

  /**
   * Encrypt a plaintext with given key.
   *
   * @param plaintext to encrypt (utf-8 encoding will be used).
   * @param secretKey to encrypt, must be AES type, see {@link SecretKeySpec}.
   * @return encrypted message.
   */
  public static byte[] encrypt(String plaintext, SecretKey secretKey)
      throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException,
      NoSuchPaddingException, InvalidAlgorithmParameterException {

    byte[] salt = new byte[GCM_SALT_LENGTH]; // NEVER REUSE THIS SALT WITH SAME KEY
    SECURE_RANDOM.nextBytes(salt);
    final Cipher cipher = Cipher.getInstance(AES);
    GCMParameterSpec parameterSpec = new GCMParameterSpec(128, salt); // 128 bit auth tag length
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

    byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

    ByteBuffer byteBuffer = ByteBuffer.allocate(salt.length + cipherText.length);
    byteBuffer.put(salt);
    byteBuffer.put(cipherText);
    return byteBuffer.array();
  }

  /**
   * Decrypts encrypted message (see {@link #encrypt(String, SecretKey)}).
   *
   * @param cipherMessage salt with ciphertext.
   * @param secretKey used to decrypt.
   * @return original plaintext.
   */
  public static String decrypt(byte[] cipherMessage, SecretKey secretKey)
      throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException,
      NoSuchPaddingException, InvalidAlgorithmParameterException {
    final Cipher cipher = Cipher.getInstance(AES);
    // use the first 12 bytes for salt
    AlgorithmParameterSpec gcmSalt = new GCMParameterSpec(128, cipherMessage, 0, GCM_SALT_LENGTH);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSalt);

    // use everything from 12 bytes on as ciphertext
    byte[] plainText = cipher.doFinal(cipherMessage, GCM_SALT_LENGTH, cipherMessage.length - GCM_SALT_LENGTH);

    return new String(plainText, StandardCharsets.UTF_8);
  }

  /**
   * It converts a string to a secret key.
   *
   * @param encodedKey The Base64 encoded key that you want to convert to a SecretKey object.
   * @return A SecretKey object.
   */
  public static SecretKey stringToSecretKey(String encodedKey) {
    // decode the base64 encoded string
    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
    // rebuild key using SecretKeySpec
    return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
  }

}
