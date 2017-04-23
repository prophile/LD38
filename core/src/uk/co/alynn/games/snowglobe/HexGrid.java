package uk.co.alynn.games.snowglobe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HexGrid<T> implements Iterable<HexGrid.Entry<T>> {
    private static final double SIN_60_DEGREES = 0.8660254037844386;
    private static final double SIN_30_DEGREES = 0.5;

    @Override
    public Iterator<Entry<T>> iterator() {
        final Iterator<Map.Entry<Coord, T>> iter = data.entrySet().iterator();
        return new Iterator<Entry<T>>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Entry<T> next() {
                Map.Entry<Coord, T> kvp = iter.next();
                if (kvp == null)
                    return null;
                return new Entry<T>(kvp.getKey().slice, kvp.getKey().column, kvp.getValue());
            }

            @Override
            public void remove() {
                iter.remove();
            }
        };
    }

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
            return (new Integer(slice).hashCode() + column);
        }
    }

    public static class Entry<T> {
        public final int slice;
        public final int column;
        public final T value;

        public Entry(int slice, int column, T value) {
            this.slice = slice;
            this.column = column;
            this.value = value;
        }
    }

    private final Map<Coord, T> data;

    @SuppressWarnings("unchecked")
    public HexGrid() {
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
        return (double)column * SIN_60_DEGREES;
    }

    public static double hexToY(int slice, int column) {
        return (double)slice + SIN_30_DEGREES * (double)column;
    }

    public static double xyDistanceFromHex(double x, double y, int s2, int c2) {
        // return distance between xy position and slice column positions
        // in xy coordinate system
        return (double) Math.hypot(x - hexToX(s2, c2), y - hexToY(s2, c2));

    }
    public static Coord xyToHex(double x, double y) {
        // calculate Hex position from XY position
        // by finding the nearest four centres of the hex grid and
        // returning the grid entry with the smallest xy distance between centre of grid and the point
        int sliceMin = (int) Math.floor(y - x * SIN_30_DEGREES);
        int columnMin = (int) Math.floor (x);
        int slice = sliceMin;
        int column = columnMin;
        int columns[] = {columnMin, columnMin + 1};
        int slices[] = {sliceMin, sliceMin + 1};
        for (int c: columns){
            for (int s: slices){
                if (xyDistanceFromHex(x, y, s, c) < xyDistanceFromHex(x, y, slice, column)) {
                    slice = s;
                    column = c;
                }
            }
        }
        System.err.println("picking from sliceMin " + sliceMin + " column Min" + columnMin);
        return new Coord(slice, column);
    }

    public static int locToSlice(double x, double y) {
        return xyToHex(x, y).slice;
    }

    public static int locToColumn(double x, double y) {
        return xyToHex(x, y).column;
    }

}
