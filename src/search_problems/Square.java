package search_problems;

public record Square(int row, int column) {

    @Override
    public String toString() {

        return "Row: " + row + " Col: " + column;
    }
}
