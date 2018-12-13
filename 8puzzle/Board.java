import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Corner cases.  You may assume that the constructor receives an n-by-n array containing the n2
 * integers between 0 and n2 − 1, where 0 represents the blank square.
 * <p>
 * Performance requirements.  Your implementation should support all Board methods in time
 * proportional to n2 (or better) in the worst case.
 */
public class Board {

    /**
     * Optimization: array elements are shorts, this uses half of the memory than ints, and by
     * definition, the max board size is 127 * 127 = 16129, so the max element is only 16128 which
     * fits into a short
     */
    private final short[][] blocks;
    private final short dimension;
    private short emptyCellX;
    private short emptyCellY;

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
     * @param blocks the matrix to use to make a new board
     */
    public Board(int[][] blocks) {
        CopyResult copyResult = copyOf(blocks);
        this.blocks = copyResult.blocks;
        this.emptyCellX = copyResult.emptyCellPosition.x;
        this.emptyCellY = copyResult.emptyCellPosition.y;
        this.dimension = (short) this.blocks.length;
    }

    /**
     * construct a board from an n-by-n array of blocks
     * <p>
     * (where blocks[i][j] = block in y i, xumn * j)
     *
     * @param blocks the matrix to use to make a new board
     */
    private Board(short[][] blocks) {
        CopyResult copyResult = copyOf(blocks);
        this.blocks = copyResult.blocks;
        this.dimension = (byte) this.blocks.length;
        this.emptyCellX = copyResult.emptyCellPosition.x;
        this.emptyCellY = copyResult.emptyCellPosition.y;
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

    /**
     * Returns the copy of the <code>blocksToCopy</code>
     *
     * @param blocksToCopy the blocksToCopy to copy
     * @return the copy of the <code>blocksToCopy</code>
     */
    private CopyResult copyOf(short[][] blocksToCopy) {
        return new CopyResult(blocksToCopy);
    }

    public static void main(String[] args) {
        //
    }

    /**
     * number of blocks out of place
     *
     * @return the hamming distance
     */
    public int hamming() {

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

        return hamming;

    }

    /**
     * sum of Manhattan distances between blocks and goal
     *
     * @return sum of Manhattan distances between blocks and goal
     */
    public int manhattan() {

        int manhattan = 0;
        short requiredNumber = 1;
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                int number = blocks[y][x];
                if (number != requiredNumber && number != 0) {
                    int dx = Math.abs(x - ((number - 1) % dimension));
                    int dy = Math.abs(y - ((number - 1) / dimension));
                    manhattan += dx + dy;
                }
                requiredNumber++;
            }
        }

        return manhattan;
    }

    /**
     * is this board the goal board?
     *
     * @return true if the board is the goal board
     */
    public boolean isGoal() {
        return manhattan() == 0;
    }

    /**
     * does this board equal obj?
     *
     * @param obj the other Object
     * @return true if this object equals to the obj
     */
    public boolean equals(Object obj) {

        if (obj == null || !(this.getClass() == obj.getClass())) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        Board other = (Board) obj;
        if (other.dimension != this.dimension) {
            return false;
        }

        for (int y = 0; y < dimension; y++) {
            if (!Arrays.equals(this.blocks[y], other.blocks[y])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns all neighboring boards
     *
     * @return the {@link List} of all valid neighboring boards
     */
    public Iterable<Board> neighbors() {

        List<Board> boards = new ArrayList<>();
        Position emptyCellPosition = new Position(emptyCellX, emptyCellY);
        List<Direction> emptyCellNeighbours = emptyCellPosition.getNeighbourDirections();
        for (Direction d : emptyCellNeighbours) {
            Board neighbour = new Board(this);
            neighbour.moveEmptyCell(d);
            boards.add(neighbour);
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

        Position emptyCellPosition = new Position(emptyCellX, emptyCellY);
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                if (!emptyCellPosition.equals(x, y)) {
                    positions.add(new Position(x, y));
                    if (positions.size() == 2) {
                        break;
                    }
                }
            }
        }


        Board twin = new Board(this);
        twin.swap(positions.get(0), positions.get(1));
        return twin;
    }

    /**
     * Moves the empy cell in the given direction
     *
     * @param direction the {@link Direction}
     */
    private void moveEmptyCell(Direction direction) {
        switch (direction) {
            case NORTH:
                swapEmptyCell(emptyCellX, (short) (emptyCellY - 1));
                break;
            case SOUTH:
                swapEmptyCell(emptyCellX, (short) (emptyCellY + 1));
                break;
            case EAST:
                swapEmptyCell((short) (emptyCellX + 1), emptyCellY);
                break;
            case WEST:
                swapEmptyCell((short) (emptyCellX - 1), emptyCellY);
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
        short tmp = blocks[p1.y][p1.x];
        blocks[p1.y][p1.x] = blocks[p2.y][p2.x];
        blocks[p2.y][p2.x] = tmp;
    }

    /**
     * Swaps the empty cell with the new coordinates
     *
     * @param newX new x
     * @param newY new y
     */
    private void swapEmptyCell(short newX, short newY) {
        swap(emptyCellX, emptyCellY, newX, newY);
        emptyCellX = newX;
        emptyCellY = newY;
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
        short tmp = blocks[y1][x1];
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
        private final short x;
        private final short y;

        public Position() {
            x = 0;
            y = 0;
        }

        public Position(int x, int y) {
            this.x = (short) x;
            this.y = (short) y;
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
         *
         * @param positionX column
         * @param positionY row
         * @return true if the position is valid
         */
        public boolean isValid(int positionX, int positionY) {
            boolean valid = positionX >= 0 && positionX < dimension && positionY >= 0
                    && positionY < dimension;
            return valid;
        }
    }


    /**
     * Value objects for storing the result of a blocks copy operation
     */
    private class CopyResult {
        private final short[][] blocks;
        private final Position emptyCellPosition;

        public CopyResult(int[][] blocksToCopy) {
            Position position = null;
            this.blocks = new short[blocksToCopy.length][blocksToCopy.length];
            for (int y = 0; y < blocksToCopy.length; y++) {
                for (int x = 0; x < blocksToCopy.length; x++) {
                    this.blocks[y][x] = (short) blocksToCopy[y][x];
                    if (blocksToCopy[y][x] == 0) {
                        position = new Position(x, y);
                    }
                }
            }

            this.emptyCellPosition = position;
        }

        public CopyResult(short[][] blocksToCopy) {
            Position position = null;
            this.blocks = new short[blocksToCopy.length][blocksToCopy.length];
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
