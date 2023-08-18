package com.github.sisimomo.hexagonalchess.engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class Coordinate {

  public static final Coordinate ORIGIN = new Coordinate(0, 0, 0);

  public static final List<Coordinate> DIRECTION_VECTORS = List.of(new Coordinate(1, 0, -1), new Coordinate(1, -1, 0),
      new Coordinate(0, -1, 1), new Coordinate(-1, 0, 1), new Coordinate(-1, 1, 0), new Coordinate(0, 1, -1));

  public static final List<Coordinate> DIAGONAL_VECTORS = List.of(new Coordinate(2, -1, -1), new Coordinate(1, -2, 1),
      new Coordinate(-1, -1, 2), new Coordinate(-2, 1, 1), new Coordinate(-1, 2, -1), new Coordinate(1, 1, -2));

  @NotNull
  @Max(Constant.BOARD_SIDE_LENGTH - 1)
  @Min((Constant.BOARD_SIDE_LENGTH - 1) * -1)
  private final int q;
  @NotNull
  @Max(Constant.BOARD_SIDE_LENGTH - 1)
  @Min((Constant.BOARD_SIDE_LENGTH - 1) * -1)
  private final int r;
  @NotNull
  @Max(Constant.BOARD_SIDE_LENGTH - 1)
  @Min((Constant.BOARD_SIDE_LENGTH - 1) * -1)
  private final int s;

  /**
   * Checks if two lists of {@link Coordinate}s are equal.
   *
   * @param coordinatesA A list of {@link Coordinate} objects.
   * @param coordinatesB A second list of {@link Coordinate} objects.
   * @return A boolean value.
   */
  public static boolean equals(List<Coordinate> coordinatesA, List<Coordinate> coordinatesB) {
    if (coordinatesA.size() != coordinatesB.size()) {
      return false;
    }
    Comparator<Coordinate> coordinateComparator =
        Comparator.comparing(Coordinate::getQ).thenComparing(Coordinate::getR).thenComparing(Coordinate::getS);
    List<Coordinate> sortedCoordinatesA = coordinatesA.stream().sorted(coordinateComparator).toList();
    List<Coordinate> sortedCoordinatesB = coordinatesB.stream().sorted(coordinateComparator).toList();

    for (int i = 0; i < sortedCoordinatesA.size(); i++) {
      if (!Objects.equals(sortedCoordinatesA.get(i), sortedCoordinatesB.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Generates a list of {@link Coordinate}s in a hexagonal ring shape around a given center
   * coordinate. <a href="https://www.redblobgames.com/grids/hexagons/#rings-single">Source</a>
   *
   * @param center The center of the ring.
   * @param radius The distance from the center coordinate to the outermost coordinates in the ring.
   * @return A List of {@link Coordinate} objects.
   */
  public static List<Coordinate> ring(Coordinate center, @Min(1) int radius) {
    List<Coordinate> results = new ArrayList<>();
    Coordinate hex = center.add(DIRECTION_VECTORS.get(4).multiply(radius));
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < radius; j++) {
        results.add(hex);
        hex = hex.add(DIRECTION_VECTORS.get(i));
      }
    }
    return results;
  }

  /**
   * A list of {@link Coordinate}s in a spiral pattern, starting from a given center coordinate and
   * expanding outwards up to a specified radius.
   * <a href="https://www.redblobgames.com/grids/hexagons/#rings-spiral">Source</a>
   *
   * @param center The center of the ring.
   * @param radius The distance from the center coordinate to the outermost coordinates in the spiral.
   * @return The method is returning a List of Coordinate objects.
   */
  public static List<Coordinate> spiral(Coordinate center, @Min(0) int radius) {
    List<Coordinate> results = new ArrayList<>();
    results.add(center);
    for (int i = 1; i <= radius; i++) {
      results.addAll(ring(center, i));
    }
    return results;
  }

  /**
   * Adding the current {@link Coordinate} with the provided {@link Coordinate} object. We are
   * achieving this by adding the q, r, and s values of the current Coordinate object with the q, r,
   * and s of the provided {@link Coordinate} object.
   *
   * @param coordinate The provided {@link Coordinate} object.
   * @return A new {@link Coordinate} object.
   */
  public Coordinate add(Coordinate coordinate) {
    return new Coordinate(this.q + coordinate.q, this.r + coordinate.r, this.s + coordinate.s);
  }

  /**
   * Subtracting the current {@link Coordinate} with provided {@link Coordinate} object. We are
   * achieving this by subtracting the q, r, and s values of the current Coordinate object with the q,
   * r, and s of the provided {@link Coordinate} object.
   *
   * @param coordinate The provided {@link Coordinate} object.
   * @return A new {@link Coordinate} object.
   */
  public Coordinate subtract(Coordinate coordinate) {
    return new Coordinate(this.q - coordinate.q, this.r - coordinate.r, this.s - coordinate.s);
  }

  /**
   * Multiplying the current {@link Coordinate} with provided multiplier. We are achieving this by
   * multiplying the q, r, and s values of the current Coordinate object with the provided multiplier.
   *
   * @param multiplier The provided multiplier.
   * @return A new {@link Coordinate} object.
   */
  public Coordinate multiply(int multiplier) {
    return new Coordinate(this.q * multiplier, this.r * multiplier, this.s * multiplier);
  }

  public Coordinate horizontalReflection() {
    return new Coordinate(this.q, this.s, this.r);
  }

  public Coordinate verticalReflection() {
    return new Coordinate(this.q * -1, this.s * -1, this.r * -1);
  }

  public int distance(Coordinate coordinate) {
    Coordinate vec = subtract(coordinate);
    return (Math.abs(vec.q) + Math.abs(vec.r) + Math.abs(vec.s)) / 2;
  }

  @Override
  public int hashCode() {
    return Objects.hash(q, r, s);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Coordinate other = (Coordinate) obj;
    return Objects.equals(q, other.q) && Objects.equals(r, other.r) && Objects.equals(s, other.s);
  }

}
