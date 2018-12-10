/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.List;

/**
 * Corner cases.  You may assume that the constructor receives an n-by-n array containing the n2
 * integers between 0 and n2 − 1, where 0 represents the blank square.
 * <p>
 * Performance requirements.  Your implementation should support all Board methods in time
 * proportional to n2 (or better) in the worst case.
 */
public class Board {

    private int[][] blocks;
    private int dimension;
    private Position emptyCellPosition;
    private int hammingDistance = -1;
    private int manhattanDistance = -1;

    /**
     * Copy constructor
     *
     * @param other the {@link Board} to copy
     */
    private Board(Board other) {
        this(other.blocks);
    }

    /**
     * construct a board from an n-by-n array of blocks
     * <p>
     * (where blocks[i][j] = block in y i, xumn * j)
     *
     * @param blocks
     */
    public Board(int[][] blocks) {
        CopyResult copyResult = copyOf(blocks);
        this.blocks = copyResult.blocks;
        this.emptyCellPosition = copyResult.emptyCellPosition;
        this.dimension = this.blocks.length;
    }

    /**
     * Returns the copy of the <code>blocksToCopy</code>
     *
     * @param blocksToCopy the blocksToCopy to copy
     * @return the copy of the <code>blocksToCopy</code>
     */
    private CopyResult copyOf(int[][] blocksToCopy) {
        return new CopyResult(blocksToCopy);
    }

    public static void main(String[] args) {

        Board b = new Board(new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
        });
        System.out.println(b);
    }

    /**
     * number of blocks out of place
     *
     * @return
     */
    public int hamming() {

        if (hammingDistance < 0) {
            int hamming = 0;
            int required = 1;
            for (int y = 0; y < dimension; y++) {
                for (int x = 0; x < dimension; x++) {
                    int number = blocks[y][x];
                    if (number != 0 && number != required) {
                        hamming++;
                    }
                    required++;
                    if (required == dimension * dimension) {
                        required = 0;
                    }
                }
            }
            this.hammingDistance = hamming;
        }
        return this.hammingDistance;
    }

    private Position getPositionFromNumber(int number, int dimensionOfBoard) {
        if (number == 0) {
            return new Position(dimensionOfBoard - 1, dimensionOfBoard - 1);
        }
        return new Position((number - 1) % dimensionOfBoard, (number - 1) / dimensionOfBoard);
    }

    /**
     * sum of Manhattan distances between blocks and goal
     *
     * @return
     */
    public int manhattan() {
        if (manhattanDistance < 0) {
            int manhattan = 0;

            for (int y = 0; y < dimension; y++) {
                for (int x = 0; x < dimension; x++) {
                    int number = blocks[y][x];
                    if (number != 0) {
                        Position p = getPositionFromNumber(number, dimension);

                        int dx = Math.abs(x - p.x);
                        int dy = Math.abs(y - p.y);
                        manhattan += dx + dy;
                    }
                }

            }
            this.manhattanDistance = manhattan;
        }
        return this.manhattanDistance;
    }

    /**
     * is this board the goal board?
     *
     * @return
     */
    public boolean isGoal() {
        int number = 1;
        int zeroNumber = dimension * dimension;
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                if (number == zeroNumber) {
                    if (blocks[y][x] != 0) {
                        return false;
                    }
                }
                else if (blocks[y][x] != number) {
                    return false;
                }
                number++;
            }
        }
        return true;
    }

    /**
     * does this board equal obj?
     *
     * @param obj the other Object
     * @return true if this object equals to the obj
     */
    public boolean equals(Object obj) {
        if (obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }
        Board other = (Board) obj;
        if (other.dimension != this.dimension) {
            return false;
        }

        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                if (this.blocks[y][x] != other.blocks[y][x]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * all neighboring boards
     *
     * @return the {@link List} of all valid neighboring boards
     */
    public Iterable<Board> neighbors() {

        List<Board> boards = new ArrayList<>();
        List<Direction> emptyCellNeighbours = this.emptyCellPosition.getNeighbourDirections();
        for (Direction d : emptyCellNeighbours) {
            Board b = new Board(this);
            b.moveEmptyCell(d);
            boards.add(b);
        }

        return boards;
    }

    /**
     * a board that is obtained by exchanging any pair of blocks
     *
     * @return a board that is obtained by exchanging any pair of blocks
     */
    public Board twin() {
        List<Position> positions = new ArrayList<>();

        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                if (!emptyCellPosition.equals(x, y)) {
                    positions.add(new Position(x, y));
                }
            }
        }

        Board b = new Board(this);
        b.swap(positions.get(0), positions.get(1));
        return b;
    }

    /**
     * Moves the empy cell in the given direction
     *
     * @param direction the {@link Direction}
     */
    private void moveEmptyCell(Direction direction) {
        switch (direction) {
            case NORTH:
                swapEmptyCell(emptyCellPosition.x, emptyCellPosition.y - 1);
                break;
            case SOUTH:
                swapEmptyCell(emptyCellPosition.x, emptyCellPosition.y + 1);
                break;
            case EAST:
                swapEmptyCell(emptyCellPosition.x + 1, emptyCellPosition.y);
                break;
            case WEST:
                swapEmptyCell(emptyCellPosition.x - 1, emptyCellPosition.y);
                break;
        }
    }

    /**
     * Swaps two blocks
     *
     * @param p1 {@link Position} of the first block
     * @param p2 {@link Position} of the second block
     */
    private void swap(Position p1, Position p2) {
        int tmp = blocks[p1.y][p1.x];
        blocks[p1.y][p1.x] = blocks[p2.y][p2.x];
        blocks[p2.y][p2.x] = tmp;
    }

    /**
     * Swaps the empty cell with the new coordinates
     *
     * @param newX new x
     * @param newY new y
     */
    private void swapEmptyCell(int newX, int newY) {
        swap(emptyCellPosition.x, emptyCellPosition.y, newX, newY);
        emptyCellPosition.x = newX;
        emptyCellPosition.y = newY;
    }

    /**
     * Swaps two blocks
     *
     * @param x1 x coordinate of the first block
     * @param y1 y coordinate of the first block
     * @param x2 x coordinate of the second block
     * @param y2 y coordinate of the second block
     */
    private void swap(int x1, int y1, int x2, int y2) {
        int tmp = blocks[y1][x1];
        blocks[y1][x1] = blocks[y2][x2];
        blocks[y2][x2] = tmp;
    }

    /**
     * string representation of this board (in the output format specified below)
     *
     * @return string representation of this board (in the output format specified below)
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(dimension + "\r\n");
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                sb.append(String.format("%1$6s", blocks[y][x]));
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    /**
     * board dimension n
     *
     * @return board dimension
     */
    public int dimension() {
        return this.dimension;
    }

    /**
     * Directions of movement of the blocks
     */
    private enum Direction {
        NORTH, EAST, WEST, SOUTH
    }

    /**
     * Position of a block
     */
    private class Position {
        private int x;
        private int y;

        public Position() {
            x = 0;
            y = 0;
        }

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }

        public boolean equals(int positionX, int positionY) {
            return this.x == positionX && this.y == positionY;
        }

        /**
         * Returns the {@link List} of all valid neighboring directions
         *
         * @return the {@link List} of all valid neighboring directions
         */
        public List<Direction> getNeighbourDirections() {
            List<Direction> result = new ArrayList<>();

            if (isValid(x, y)) {

                // north
                if (isValid(x, y - 1)) {
                    result.add(Direction.NORTH);
                }
                // south
                if (isValid(x, y + 1)) {
                    result.add(Direction.SOUTH);
                }
                // west
                if (isValid(x - 1, y)) {
                    result.add(Direction.WEST);
                }
                // east
                if (isValid(x + 1, y)) {
                    result.add(Direction.EAST);
                }
            }
            return result;
        }

        /**
         * Returns true if the position is valid
         * @param positionX column
         * @param positionY row
         * @return true if the position is valid
         */
        public boolean isValid(int positionX, int positionY) {
            boolean valid = positionX >= 0 && positionX < dimension && positionY >= 0 && positionY < dimension;
            return valid;
        }
    }

    /**
     * Value objects for storing the result of a blocks copy operation
     */
    private class CopyResult {
        private final int[][] blocks;
        private final Position emptyCellPosition;

        public CopyResult(int[][] blocksToCopy) {
            Position position = null;
            this.blocks = new int[blocksToCopy.length][blocksToCopy.length];
            for (int y = 0; y < blocksToCopy.length; y++) {
                for (int x = 0; x < blocksToCopy.length; x++) {
                    this.blocks[y][x] = blocksToCopy[y][x];
                    if (blocksToCopy[y][x] == 0) {
                        position = new Position(x, y);
                    }
                }
            }

            this.emptyCellPosition = position;
        }
    }
}
