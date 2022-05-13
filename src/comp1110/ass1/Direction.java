package comp1110.ass1;



/**
 * This enumeration type represents the four cardinal directions
 *
 * Notice that this is an enumeration type, so none of the fields
 * change once the type is created (they are all declared final).
 *
 * This class contains three tasks that you need to complete.
 */
public enum Direction {
    NORTH('↑'), EAST('→'), SOUTH('↓'), WEST('←');

    final private char symbol;

    /**
     * Constructor
     *
     * @param symbol This direction's symbol
     */
    Direction(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Given an upper case character ('N', 'E', 'S', 'W'),
     * return the Direction associated with this character.
     *
     * @param direction a char value representing the `Direction` enum
     * @return the `Direction` associated with the char.
     */
    public static Direction fromChar(char direction) {
       // char a=direction;
        Direction d = null;

        if(direction=='N')
            d= NORTH;
        else if(direction=='S')
            d= SOUTH;
        else if(direction=='W')
            d= WEST;
        else if(direction=='E')
            d= EAST;


        return d; // Done
    }

    /**
     * Return the single character associated with a `Direction`, which is
     * the first character of the direction name, as an upper case character
     * ('N', 'E', 'S', 'W')
     *
     * @return The first character of the name of the direction
     */
    public char toChar() {
        char a=' ';
        if(this==NORTH)
            a='N';
        else if(this==SOUTH)
            a='S';
        else if(this==WEST)
            a='W';
        else if(this==EAST)
            a='E';

        return a;  // Fixed
    }

    /**
     * Given a Direction instance, return the opposite Direction
     * @return the opposite Direction to the instance.
     * for example: the opposite Direction of 'NORTH' would be 'SOUTH'.
     */
    public Direction getOpposite() {
        Direction a = null;

        if(this==NORTH)
            a=SOUTH;
        else if(this==SOUTH)
            a=NORTH;
        else if(this==WEST)
            a=EAST;
        else if(this==EAST)
            a=WEST;
       return a; // Done
    }

    /** @return this direction's symbol as a string */
    public String getSymbol() {
        return Character.toString(symbol);
    }

    /**
     * Return a string representation of the direction
     * @return A string consisting of the character returned by the toChar() method
     */
    @Override
    public String toString() {
        return Character.toString(toChar());
    }
}
