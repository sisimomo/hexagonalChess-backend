package com.github.sisimomo.hexagonalchess.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class CoordinateTest {

  private static final List<Coordinate> RADIUS_1 = List.of(new Coordinate(-1, 1, 0), new Coordinate(0, 1, -1),
      new Coordinate(1, 0, -1), new Coordinate(1, -1, 0), new Coordinate(0, -1, 1), new Coordinate(-1, 0, 1));

  private static final List<Coordinate> RADIUS_2 =
      List.of(new Coordinate(-2, 2, 0), new Coordinate(-1, 2, -1), new Coordinate(0, 2, -2), new Coordinate(1, 1, -2),
          new Coordinate(2, 0, -2), new Coordinate(2, -1, -1), new Coordinate(2, -2, 0), new Coordinate(1, -2, 1),
          new Coordinate(0, -2, 2), new Coordinate(-1, -1, 2), new Coordinate(-2, 0, 2), new Coordinate(-2, 1, 1));

  private static final List<Coordinate> RADIUS_3 = List.of(new Coordinate(-3, 3, 0), new Coordinate(-2, 3, -1),
      new Coordinate(-1, 3, -2), new Coordinate(0, 3, -3), new Coordinate(1, 2, -3), new Coordinate(2, 1, -3),
      new Coordinate(3, 0, -3), new Coordinate(3, -1, -2), new Coordinate(3, -2, -1), new Coordinate(3, -3, 0),
      new Coordinate(2, -3, 1), new Coordinate(1, -3, 2), new Coordinate(0, -3, 3), new Coordinate(-1, -2, 3),
      new Coordinate(-2, -1, 3), new Coordinate(-3, 0, 3), new Coordinate(-3, 1, 2), new Coordinate(-3, 2, 1));

  @Test
  void coordinateEqualsTest() {
    // Given
    Coordinate coordinate1 = new Coordinate(-4, 5, -1);
    Coordinate coordinate2 = new Coordinate(-4, 5, -1);
    Coordinate coordinate3 = new Coordinate(-4, 6, -1);
    // When/Then
    assertEquals(coordinate1, coordinate2);
    assertNotEquals(coordinate1, coordinate3);
  }

  @Test
  void coordinatesEqualsTest() {
    // Given
    List<Coordinate> modified = RADIUS_3.stream().map(c -> c.equals(RADIUS_3.get(0)) ? c.multiply(2) : c).toList();
    // When/Then
    assertTrue(Coordinate.equals(RADIUS_3, RADIUS_3));
    assertFalse(Coordinate.equals(RADIUS_3, modified));
  }

  @Test
  void ringTest() {
    // When/Then
    assertTrue(Coordinate.equals(RADIUS_1, Coordinate.ring(Coordinate.ORIGIN, 1)));
    assertTrue(Coordinate.equals(RADIUS_2, Coordinate.ring(Coordinate.ORIGIN, 2)));
    assertTrue(Coordinate.equals(RADIUS_3, Coordinate.ring(Coordinate.ORIGIN, 3)));
  }

  @Test
  void spiralTest() {
    // Given
    List<Coordinate> coordinates = new ArrayList<>(RADIUS_1);
    coordinates.addAll(RADIUS_2);
    coordinates.addAll(RADIUS_3);
    coordinates.add(Coordinate.ORIGIN);
    // When/Then
    assertTrue(Coordinate.equals(coordinates, Coordinate.spiral(Coordinate.ORIGIN, 3)));
  }

  @Test
  void addTest() {
    // When/Then
    assertEquals(new Coordinate(1, 2, 4), new Coordinate(1, 2, 3).add(new Coordinate(0, 0, 1)));
    assertEquals(new Coordinate(1, 3, 3), new Coordinate(1, 2, 3).add(new Coordinate(0, 1, 0)));
    assertEquals(new Coordinate(2, 2, 3), new Coordinate(1, 2, 3).add(new Coordinate(1, 0, 0)));
    assertEquals(new Coordinate(1, 2, 3), new Coordinate(1, 2, 3).add(new Coordinate(0, 0, 0)));
    assertEquals(new Coordinate(0, 2, 3), new Coordinate(1, 2, 3).add(new Coordinate(-1, 0, 0)));
    assertEquals(new Coordinate(1, 1, 3), new Coordinate(1, 2, 3).add(new Coordinate(0, -1, 0)));
    assertEquals(new Coordinate(1, 2, 2), new Coordinate(1, 2, 3).add(new Coordinate(0, 0, -1)));
  }

  @Test
  void subtractTest() {
    // When/Then
    assertEquals(new Coordinate(1, 2, 2), new Coordinate(1, 2, 3).subtract(new Coordinate(0, 0, 1)));
    assertEquals(new Coordinate(1, 1, 3), new Coordinate(1, 2, 3).subtract(new Coordinate(0, 1, 0)));
    assertEquals(new Coordinate(0, 2, 3), new Coordinate(1, 2, 3).subtract(new Coordinate(1, 0, 0)));
    assertEquals(new Coordinate(1, 2, 3), new Coordinate(1, 2, 3).subtract(new Coordinate(0, 0, 0)));
    assertEquals(new Coordinate(2, 2, 3), new Coordinate(1, 2, 3).subtract(new Coordinate(-1, 0, 0)));
    assertEquals(new Coordinate(1, 3, 3), new Coordinate(1, 2, 3).subtract(new Coordinate(0, -1, 0)));
    assertEquals(new Coordinate(1, 2, 4), new Coordinate(1, 2, 3).subtract(new Coordinate(0, 0, -1)));
  }

  @Test
  void multiplyTest() {
    // When/Then
    assertEquals(new Coordinate(0, 0, 0), new Coordinate(1, 2, 3).multiply(0));
    assertEquals(new Coordinate(1, 2, 3), new Coordinate(1, 2, 3).multiply(1));
    assertEquals(new Coordinate(2, 4, 6), new Coordinate(1, 2, 3).multiply(2));
    assertEquals(new Coordinate(3, 6, 9), new Coordinate(1, 2, 3).multiply(3));
  }

  @Test
  void horizontalReflectionTest() {
    // When/Then
    assertEquals(new Coordinate(1, 2, 3), new Coordinate(1, 3, 2).horizontalReflection());
    assertEquals(new Coordinate(2, 4, 6), new Coordinate(2, 6, 4).horizontalReflection());
    assertEquals(new Coordinate(3, 6, 9), new Coordinate(3, 9, 6).horizontalReflection());
  }

  @Test
  void verticalReflectionTest() {
    // When/Then
    assertEquals(new Coordinate(-1, -2, -3), new Coordinate(1, 3, 2).verticalReflection());
    assertEquals(new Coordinate(-2, -4, -6), new Coordinate(2, 6, 4).verticalReflection());
    assertEquals(new Coordinate(-3, -6, -9), new Coordinate(3, 9, 6).verticalReflection());
  }

  @Test
  void distanceTest() {
    // When/Then
    assertEquals(4, new Coordinate(1, 2, 3).distance(new Coordinate(4, 5, 6)));
    assertEquals(10, new Coordinate(1, 2, 3).distance(new Coordinate(-4, -5, -6)));
    assertEquals(7, new Coordinate(-1, -2, 3).distance(new Coordinate(-4, -5, -6)));
  }

}
