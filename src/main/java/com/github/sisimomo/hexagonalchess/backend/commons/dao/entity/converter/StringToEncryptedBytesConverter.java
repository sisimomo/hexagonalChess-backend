package com.github.sisimomo.hexagonalchess.backend.commons.dao.entity.converter;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.github.sisimomo.hexagonalchess.backend.commons.utils.AesGcmUtils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Component
@Converter
public class StringToEncryptedBytesConverter implements AttributeConverter<String, byte[]> {

  private SecretKey key;

  @Autowired
  public void setKey(@Value("${db.encryption.key}") String encodedKey) {
    key = AesGcmUtils.stringToSecretKey(encodedKey);
  }

  @Override
  public byte[] convertToDatabaseColumn(String attribute) {
    if (attribute == null) {
      return null;
    }
    try {
      return AesGcmUtils.encrypt(attribute, key);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public String convertToEntityAttribute(byte[] dbData) {
    if (dbData == null) {
      return null;
    }
    try {
      return AesGcmUtils.decrypt(dbData, key);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

}
