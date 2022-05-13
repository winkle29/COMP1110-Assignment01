package comp1110.ass1;

import static comp1110.ass1.TileType.*;

/**
 * This enumeration type represents the eight tile names.
 *
 * It encodes what type type each of them is and the symbol used
 * to represent this tile name.
 *
 * Notice that this is an enumeration type, so none of the fields
 * change once the type is created (they are all declared final).
 */
public enum TileName {
    PLUS(STRAIGHT, '+'),
    EQUALS(GREEN_CORNER, '='),
    SQUARE(GREEN_CORNER, '□'),
    TRIANGLE(BROWN_CORNER, '△'),
    CROSS(BROWN_CORNER, 'x'),
    CIRCLE(BROWN_CORNER, '○'),
    STAR(STAIRCASE, '*'),
    DIAMOND(STAIRCASE, '◇');

    final private TileType type;
    final private char symbol;

    /**
     * Constructor.
     * @param type the tile type for this tile name
     * @param symbol the symbol used to represent this tile name
     */
    TileName(TileType type, char symbol) {
        this.type = type;
        this.symbol = symbol;
    }

    /** @return the type of this tile name */
    public TileType getType() { return type; }

    /**
     * Return a string representation of this tile type.
     * @return A string consisting of the tile type's symbol
     */
    @Override
    public String toString() {
        return Character.toString(symbol);
    }
}


