package uk.co.alynn.games.snowglobe;

import java.util.HashMap;
import java.util.Map;

public class HexGrid<T> {
    //private static final double THAT_THING = 0.8660254037844386;
    private static final double THAT_THING = 0.5;

    private static class Coord {
        public final int slice;
        public final int column;

        public Coord(int slice, int column) {
            this.slice = slice;
            this.column = column;
        }

        @Override
        public boolean equals(Object otherObject) {
            if (!(otherObject instanceof Coord)) {
                return false;
            }

            return slice == ((Coord) otherObject).slice && column == ((Coord) otherObject).column;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(slice) + column;
        }
    }

    private final Map<Coord, T> data;

    @SuppressWarnings("unchecked")
    public HexGrid(int slices, int columns) {
        data = new HashMap<Coord, T>();
    }

    public T get(int slice, int column) {
        return data.get(new Coord(slice, column));
    }

    public void set(int slice, int column, T value) {
        Coord key = new Coord(slice, column);
        if (value != null) {
            data.put(key, value);
        } else {
            data.remove(key);
        }
    }

    public static int distance(int slice1, int column1, int slice2, int column2) {
        return (
            Math.abs(column2 - column1) +
            Math.abs(slice2 - slice1) +
            Math.abs(column1 + slice1 - column2 - slice2)
        ) / 2;
    }

    public static boolean isAdjacent(int slice1, int column1, int slice2, int column2) {
        return distance(slice1, column1, slice2, column2) == 1;
    }

    public static double hexToX(int slice, int column) {
        return (double)column;
    }

    public static double hexToY(int slice, int column) {
        return (double)slice + THAT_THING*(double)column;
    }
}
