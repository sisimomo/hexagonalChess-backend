package com.github.sisimomo.hexagonalchess.backend.commons.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenericTypeResolverUtils {

  /**
   * Get the generic type of the superclass of the subClass provided using the genericTypeIndex.
   *
   * @param superClass The subClass that you want to get the generic type from.
   * @param genericTypeIndex The index of the generic type you want to get.
   * @return The generic type of the superclass at the specified index.
   */
  public static Class<?> getGenericType(Class<?> superClass, int genericTypeIndex) {
    Type[] types = ((ParameterizedType) superClass.getGenericSuperclass()).getActualTypeArguments();
    return ((Class<?>) types[genericTypeIndex]);
  }

}
