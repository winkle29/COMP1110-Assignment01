package comp1110.ass1;



/**
 * This class represents a movable tile in the TempleTrap game.   The
 * class encodes which sort of tile it is (tileName), its orientation,
 * its position on the board, and whether it currently holds the peg
 * (AKA the 'adventurer').
 *
 * This class contains four tasks of varying difficulty that you need
 * to complete.
 */
public class Tile {

    private final TileName tileName;      // The TileName ('PLUS', 'SQUARE' ...) (this never changes)
    private final Direction orientation;  // The tile's orientation (this does not change after the board has been initialised)
    public int position;                  // The current position of the tile on the board.

    private static Tile[] list = new Tile[TileName.values().length];

    /**
     * Static method to return the tile with give tild ID
     * @param id A tile ID corresponding to the tileName ordinal
     * @return The corresponding tile
     */
    public static Tile getTileFromID(int id) {
        assert id >= 0 && id <= TileName.values().length;
        return list[id];
    }

    /**
     * Constructor for a Tile
     *
     * @param type The TileType of the new tile
     * @param placement the String placement of the tile eg: "N0"
     */
    public Tile(TileName type, String placement) {
        this.tileName = type;
        this.orientation = placementToOrientation(placement);
        this.position = placementToPosition(placement);
        list[tileName.ordinal()] = this;
    }

    /**
     * Given a two-character tile placement string, decode the tile's position.
     *
     * You will need to read the description of the encoding in README.md.
     *
     * Hint: you will probably want to use the charAt() method on the placemen
     * string.
     *
     * @param placement A string representing the placement of a tile on the game board
     * @return An int corresponding to the tile's position on the board.
     */
    public static int placementToPosition(String placement) {
        char a= placement.charAt(1);
        String b=Character.toString(a);
        int c =Integer.parseInt(b);

        return c;  // Converted positional character to string and then to integer
    }

    /**
     * Given a two-character tile placement string, decode the tile's orientation.
     *
     * You will need to read the description of the encoding in README.md
     *
     * @param placement A string representing the placement of a tile on the game board
     * @return A value of type `Direction` corresponding to the tile's orientation on board
     */
    public static Direction placementToOrientation(String placement) {
        char a = placement.charAt(0);
        Direction b = null;
        if (a == 'N')
            b = Direction.NORTH;
        else if (a == 'S')
            b = Direction.SOUTH;
        else if (a == 'W')
            b = Direction.WEST;
        else if (a == 'E')
            b = Direction.EAST;


        return b;
    }

    /**
     * Determine whether a given tile is adjacent to this tile instance,
     * and if so, the direction in which it is adjacent.
     *
     * For example: Tile tile1 is in position 0, tile other is in position 1.
     * other is adjacent to tile 1 in the Direction East, so the method would
     * return EAST.
     *
     * @param other the other tile
     * @return The direction of adjacency if the tiles are adjacent, or null if
     * they are not adjacent.
     */
    public Direction adjacencyDirection(Tile other) {
        //Initialisation
        Direction check = null;
        int one=this.position;
        int two=other.position;

        //Condition to check adjacent tile
        if((one==2 || one==5 )&&(two==3 || two==6))
            check=null;
        else if((one==3 || one==6)&&(two==2|| two==5))
            check=null;
        else if(this.position==other.position-1)
            check=Direction.EAST;
        else if(this.position==other.position+1)
            check=Direction.WEST;
        else if(this.position==other.position+3)
            check=Direction.NORTH;
        else if(this.position==other.position-3)
            check=Direction.SOUTH;


        return check;  // Done 
    }

    /**
     * Determine whether a peg can move between this tile and another (other).
     *
     * Conditions for this to be true:
     * - The tiles must be adjacent.
     * - The tiles must have compatible tile types:
     *   - The tiles must both have exits that are on the abutting side.
     *   - The tiles must be at the same level on their abutting sides (both high or both low).
     *
     * NOTE: This test does not consider whether a peg can end its movement on these
     * tiles, only whether it can move between them (i.e. whether this pair of tiles can
     * form part of a path for a peg to transit along).
     *
     * @param other The other tile being considered.
     * @return true if a peg can legally transit from this tile to the other.
     */
    public boolean canTransit(Tile other) {

        //Initialisation
        int one_pos=this.position;
        int two_pos=other.position;
        boolean check = false;

        //Renaming
        Direction one_o=this.orientation;
        Direction two_o=other.orientation;
        TileName one_n=this.tileName;
        TileName two_n=other.tileName;

        Direction north=Direction.NORTH;
        Direction south=Direction.SOUTH;
        Direction west=Direction.WEST;
        Direction east=Direction.EAST;

        TileName equals=TileName.EQUALS;
        TileName plus=TileName.PLUS;
        TileName diamond=TileName.DIAMOND;
        TileName square=TileName.SQUARE;
        TileName star=TileName.STAR;
        TileName triangle=TileName.TRIANGLE;
        TileName cross=TileName.CROSS;
        TileName circle=TileName.CIRCLE;


       // GREEN RIGHT
       if((one_n==equals && (one_o==west || one_o==south)) || (one_n==square && (one_o==south || one_o==west)) || (one_n==plus && (one_o==north || one_o==south))|| ((one_n==star || one_n==diamond) && one_o==north)){
               if(two_pos==one_pos+1 && one_pos!=2 && one_pos!=5 && one_pos!=8){
                   if((two_n==plus && (two_o==north || two_o==south)) || ((two_n==equals || two_n==square) && (two_o==north || two_o==east)) || (two_n==star && two_o==south) || (two_n==diamond && two_o==south)){
                       check=true;
                   }
               }

       }

       // GREEN LEFT
        if((one_n==equals && (one_o==north || one_o==east)) || (one_n==square && (one_o==north || one_o==east)) || (one_n==plus && (one_o==north || one_o==south))|| ((one_n==star || one_n==diamond) && one_o==south)){
            if(two_pos==one_pos-1 && one_pos!=0 && one_pos!=3 && one_pos!=6){
                if((two_n==plus && (two_o==north || two_o==south)) || ((two_n==equals || two_n==square) && (two_o==south || two_o==west)) || (two_n==star && two_o==north) || (two_n==diamond && two_o==north)){
                    check=true;
                }
            }

        }

        // GREEN UP
        if(((one_n==equals || one_n==square) && (one_o==east|| one_o==south)) || (one_n==plus && (one_o==east || one_o==west)) || ((one_n==star || one_n==diamond) && one_o==west)){
            if(two_pos==one_pos-3 && one_pos!=1 && one_pos!=0 && one_pos!=2){
                if((two_n==plus && (two_o==east || two_o==west)) || ((two_n==equals || two_n==square) && (two_o==north || two_o==west)) || ((two_n==star || two_n==diamond) && two_o==east)){
                    check=true;
                }
            }

        }

        //GREEN DOWN
        if(((one_n==equals || one_n==square) && (one_o==north|| one_o==west)) || (one_n==plus && (one_o==east || one_o==west))|| ((one_n==star || one_n==diamond) && one_o==east)){
            if(two_pos==one_pos+3 && one_pos!=7 && one_pos!=6 && one_pos!=8){
                if((two_n==plus && (two_o==east || two_o==west)) || ((two_n==equals || two_n==square) && (two_o==east || two_o==south)) || ((two_n==star || two_n==diamond) && two_o==west)){
                    check=true;
                }
            }

        }

        //BROWN RIGHT
        if(((one_n==triangle|| one_n==cross|| one_n==circle) && (one_o==south || one_o==west)) ||((one_n==star || one_n== diamond) && one_o==south)){
            if(two_pos==one_pos+1 && one_pos!=2 && one_pos!=5 && one_pos!=8){
                if( ((two_n==triangle || two_n==cross || two_n==circle) && (two_o==north || two_o==east)) || ((two_n==star || two_n==diamond) && two_o==north)){
                    check=true;
                }
            }

        }

        //BROWN LEFT
        if(((one_n==triangle|| one_n==cross|| one_n==circle) && (one_o==north || one_o==east)) ||((one_n==star || one_n== diamond) && one_o==north)){
            if(two_pos==one_pos-1 && one_pos!=0 && one_pos!=3 && one_pos!=6){
                if( ((two_n==triangle || two_n==cross || two_n==circle) && (two_o==south || two_o==west)) || ((two_n==star || two_n==diamond) && two_o==south)){
                    check=true;
                }
            }

        }

        //BROWN UP
        if(((one_n==triangle|| one_n==cross|| one_n==circle) && (one_o==south || one_o==east)) ||((one_n==star || one_n== diamond) && one_o==east)){
            if(two_pos==one_pos-3 && one_pos!=0 && one_pos!=1 && one_pos!=2){
                if( ((two_n==triangle || two_n==cross || two_n==circle) && (two_o==north || two_o==west)) || ((two_n==star || two_n==diamond) && two_o==west)){
                    check=true;
                }
            }

        }

        //BROWN DOWN
        if(((one_n==triangle|| one_n==cross|| one_n==circle) && (one_o==north || one_o==west)) ||((one_n==star || one_n== diamond) && one_o==west)){
            if(two_pos==one_pos+3 && one_pos!=6 && one_pos!=7 && one_pos!=8){
                if( ((two_n==triangle || two_n==cross || two_n==circle) && (two_o==east || two_o==south)) || ((two_n==star || two_n==diamond) && two_o==east)){
                    check=true;
                }
            }

        }

        return check;  // Done Task 10
    }

    /** @return the orientation of this tile */
    public Direction getOrientation() { return orientation; }

    /** @return the TileName of this tile */
    public TileName getTileName() { return tileName; }

    /** @return the TileType of this tile */
    public TileType getTileType() { return tileName.getType(); }

    /**
     * Set the position of this tile
     * @param position the new position of this tile.
     */
    public void setPosition(int position) { this.position = position; }

    /** @return the position of this tile */
    public int getPosition() { return position; }

    /** @return true if this tile able to hold the peg */
    public boolean canTakePeg() { return getTileType().canTakePeg(); }

    /**
     * @param viaA if true, we're referring to exiting via A, otherwise via B
     * @return true if the given exit is Green.
     */
    public boolean exitIsGreen(boolean viaA) {
        return viaA ? getTileType().exitAisGreen() : getTileType().exitBisGreen();
    }

    /**
     * @return The direction of exit a faces (in its current orientation).
     */
    public Direction exitAFaces() {
        return getTileType().exitAFaces(orientation);
    }

    /**
     * @return The direction of exit b faces (in its current orientation).
     */
    public Direction exitBFaces() {
        return getTileType().exitBFaces(orientation);
    }

    /**
     * Return a string representation of this tile.
     * @return A string consisting of the tile name's symbol followed by the symbol
     * for the tile's orientation.
     */
    @Override
    public String toString() {
        return tileName+orientation.getSymbol();
    }
}