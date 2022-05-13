package comp1110.ass1;

import static comp1110.ass1.Direction.*;

/**
 * This enumeration represents a the four different types of tile.
 *
 * The type encodes the directions that each of the exits face,
 * and what colors they are.
 *
 * Notice that this is an enumeration type, so none of the fields
 * change once the type is created (they are all declared final).
 */
public enum TileType {
    STRAIGHT(EAST, WEST),      // The straight tile (+)
    GREEN_CORNER(SOUTH, WEST), // The green corners (= and □)
    BROWN_CORNER(SOUTH, WEST), // The brown corners (△, ○, x)
    STAIRCASE(EAST, WEST);     // The staircases    (* and ◇)

    final private Direction aFacing;   // The direction exit A faces when tile is oriented North
    final private Direction bFacing;   // The direction exit B faces when tile is oriented North

    TileType(Direction aFacing, Direction bFacing) {
        this.aFacing = aFacing;
        this.bFacing = bFacing;
    }

    /**
     * Return the direction exit A faces when the tile is oriented orientation.
     *
     * For example a STRAIGHT tile oriented North has exit A in the East,
     * but a STRAIGHT tile oriented South has exit A in the West.
     *
     * Note A is the lower exit of staircases.
     *
     * @param orientation
     * @return the direction of exit B when the tile is oriented orientation.
     */
    Direction exitAFaces(Direction orientation) {
        return Direction.values()[(aFacing.ordinal()+orientation.ordinal())%4];
    }

    /**
     * Return the direction that exit B faces when the tile is oriented orientation.
     *
     * For example a STRAIGHT tile oriented North has exit B facing West,
     * but a STRAIGHT tile oriented South has exit B facing East.
     *
     * Note B is the upper exit of staircases.
     *
     * @param orientation
     * @return the direction of exit B when the tile is oriented orientation.
     */
    Direction exitBFaces(Direction orientation) {
        return Direction.values()[(bFacing.ordinal()+orientation.ordinal())%4];
    }

    /**
     * @return true if exit A of this tile type is green
     */
    public boolean exitAisGreen() {
        return this == STRAIGHT || this == GREEN_CORNER || this == STAIRCASE;
    }

    /**
     * @return true if exit A of this tile type is green
     */
    public boolean exitBisGreen() {
        return this == STRAIGHT || this == GREEN_CORNER;
    }

    /**
     * @return true if this tile type can accept a peg
     */
    public boolean canTakePeg() {
        return this == BROWN_CORNER || this == STAIRCASE;
    }
}
