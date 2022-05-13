package comp1110.ass1;

import java.util.*;

/**
 * This class represents a TempleTrap game, including the state of the
 * game and supporting methods that allow the game to be solved.
 *
 * The class cont
 * Temple Trap is children
 */
public class TempleTrap {
    static final int OFF_BOARD = -2;       // constant representing an off-board position
    static final int FINISH_POSITION = -1; // constant representing the finish position for the game

    /**
     * The objective represents the problem to be solved in this instance of the game.
     */
    Objective objective;

    /**
     * board is an instance field that is an array of length nine
     * with one position for each spot on the board.   Once initialized
     * the array will store the tile that is currently at each of the
     * nine board positions, with a null representing the position which
     * has no tile.
     *
     * If a Tile is in position 0, then board[0] holds a reference to that
     * Tile instance.  If a Tile is in position 3, then board[3] holds a
     * reference to that Tile instance. If the position is empty, that
     * element of the board is 'null'.
     */
    public Tile[] board = new Tile[9];

    /** The board position holding the peg */
    private int pegPosition;

    /**
     * Construct a game with a specific objective
     *
     * @param objective The objective of this game.
     */
    public TempleTrap(Objective objective) {
        this.objective = objective;
        initializeBoardState();
    }

    /**
     * Construct a game for a given level of difficulty.
     * This chooses a new objective and creates a new instance of
     * the game at the given level of difficulty.
     *
     * @param difficulty The difficulty of the game.
     */
    public TempleTrap(int difficulty) {
        this(Objective.newObjective(difficulty));
    }

    /** @return The Objective for the current TempleTrap instance. */
    public Objective getObjective() {  return objective; }

    /** @return the current peg position for this game */
    public int getPegPosition() { return pegPosition; }

    /** @param position The new peg position  */
    public void setPegPosition(int position) {  pegPosition = position; }
    /**
     * Initialise the board state according to the objective.
     */
    public void initializeBoardState() {
        String boardState = objective.getInitialState();
        for (int i = 0; i < boardState.length() / 2; i++) {
            String placement = boardState.substring(i * 2, ((i + 1) * 2));
            addTileToBoard(TileName.values()[i], placement);
        }
        pegPosition = Character.getNumericValue(boardState.charAt(boardState.length() - 1));
    }

    /**
     * Add a new tile placement to the board state, updating relevant
     * data structures accordingly.
     *
     * Note: this method is only used when initialising the board since the
     * tiles are never removed from the board.
     *
     * @param placement The placement to add.
     */
    private void addTileToBoard(TileName type, String placement) {
        Tile tile = new Tile(type, placement);
        int pos = tile.getPosition();
        board[pos] = tile;
    }

    /**
     * Print out tile state.  May be useful for debugging.
     */
    public void printTileState() {
        for (int i = 0; i < 9; i++) {
            System.out.print(board[i] == null ? "__" : board[i].toString() + "");
            if (i % 3 == 2) System.out.println();
        }
        System.out.println(getBoardState());
    }

    /**
     * This method may be useful for debugging.
     * @return A String representation of the board state of the current TempleTrap instance
     */
    public String getBoardState() {
        String rtn = "";
        for (int i = 0; i < 8; i++) {
            Tile t = Tile.getTileFromID(i);
            rtn += t == null ? "--" : t.getOrientation().toString() + t.position;
        }
        rtn += pegPosition;
        return rtn;
    }

    /**
     * Takes a solution string and returns the number of directions
     * characters in the solution (note that a solution string is made
     * up of tile symbols, the letter 'p' (for peg) and direction
     * letters.
     *
     * The number returned will match the number recorded in the upper
     * right of each game objective.  For example, objective 1 has
     * the number 11 in the upper right, and a correct solution string
     * for problem 1 will return the number 11 if passed to this
     * function.
     *
     * You may find this useful for the final (advanced) part of the
     * assignment, where you have to solve the game.
     *
     * @param solution a solution string (partial or complete)
     * @return the number of direction characters in the (partial solution).
     */
    public static int directionsInSolution(String solution) {
        int steps = 0;
        for (int i = 0; i < solution.length(); i++) {
            char c = solution.charAt(i);
            if (c >= 'E' && c <= 'W')
                steps++;
        }
        return steps;
    }

    /**
     * Takes a solution string and works out how many game steps
     * it contains (a game step may be a single tile move or a
     * single peg step -- note that a peg step is from one valid
     * position to the next, so may contain multiple directions if it
     * goes over green areas).
     *
     * You may find this useful for the final (advanced) part of the
     * assignment, where you have to solve the game.
     *
     * This method works both for complete solutions and partial solutions.
     *
     * @param solution a solution string
     * @return the number of game steps in the path
     */
    public static int stepsInSolution(String solution) {
        return solution.length() - directionsInSolution(solution);
    }

    /**
     * Assuming that the movement is valid, update the tile data structure
     * with a new tile and update the location field of that tile. The previous
     * location of the tile in the tile data structure should be set to null.
     *
     * Each entry in the data structure corresponds to a location, and
     * each location contains either a tile or is null.
     *
     * locations that are covered by a tile will have their data structure
     * entry point to the covering tile.
     *
     * locations that are not covered by a tile will point to null.
     *
     * @param tile  The Tile to be moved.
     * @param direction The direction in which to move the tile.
     */
    public void moveTile(Tile tile, Direction direction) {
        int pos = tile.getPosition();
        int newPos = getNextPosition(pos, direction);
        tile.position = newPos;
        board[newPos] = board[pos];
        board[pos] = null;
    }

    /**
     * Update the peg's position in the current game.
     * @param next the position the peg should be located in after running this method.
     */
    public void updatePeg(int next) {
        pegPosition = next;
    }

    /* START OF ASSIGNMENT TASKS */

    /**
     * Given a boardState, determine whether it is valid.
     * A boardState is valid under the following conditions:
     * - The string is exactly 17 characters long.
     * - Each orientation is a direction character (N, E, S, W)
     * - Each position is a number from 0 to 8
     * - No two tiles share a position.
     * - The peg is *not* placed on the vacant position.
     * - The peg is *not* located on an GREEN tile.
     *
     * @param boardState a string representing a boardState.
     * @return true if the boardState is valid, false if it is invalid.
     */
    public static boolean isBoardStateValid(String boardState) {
        boolean result ;
        char[] b;
        int count=0;
        char[] positions=new char[boardState.length()];
        int check_peg=0;

        // creating array
        b=boardState.toCharArray();
        if(boardState.length()!=17)
            count++;
       // System.out.println(count);

        //checking direction character
        for(int i=0;i<boardState.length()-1;i=i+2){
            // System.out.println(b[i]);
            if(b[i]!='N' && b[i] !='W' && b[i] !='S' && b[i]!='E' )
                count++;}
       // System.out.println(count);

        // checking position number
        for(int k=1;k<=boardState.length()-1;k=k+2){
            if(b[k]!='0' && b[k]!='1' && b[k]!='2' && b[k]!='3' && b[k]!='4' && b[k]!='5' && b[k]!='6' && b[k]!='7' && b[k]!='8')
                count++;}
       // System.out.println(count);

        // creating array for only positions
        for(int p=1;p<=boardState.length()-2;p=p+2){
            positions[p]=b[p];
        }
        Arrays.sort(positions);

        //checking no tile shares same position
        for(int l=9;l<=boardState.length()-2;l++){
            if(positions[l+1]==positions[l]){
                //System.out.println(positions[l+1]);
                count++;}
        }
      //  System.out.println(count);

        // checking peg is not placed on vacant position
        for(int c=8;c<=boardState.length()-1;c++){
            if(positions[c]==b[boardState.length()-1])
                check_peg++;
        }if(check_peg!=1)
            count++;
       // System.out.println(count);

        // checking peg is not located on green tile
        for(int m=1;m<=5;m=m+2){
            if(b[m]==b[boardState.length()-1])
                count++;
        }
        //System.out.println(count);

        if(count==0)
            result=true;
        else
            result=false;



        return result;  //
    }

    /**
     * Given a position and a direction to move in, determine the next position.
     *
     * @param pos the current position
     * @param dir the Direction to move in.
     * @return the next position if it is on the board, FINISH_POSITION if it is at the
     * finish position, or OFF_BOARD if it is off the board.
     */
    public static int getNextPosition(int pos, Direction dir) {
       int a = 0;
        if (dir==Direction.WEST && (pos==0))
           a=FINISH_POSITION;
        else if(dir==Direction.WEST &&(pos==3 || pos==6))
            a=OFF_BOARD;
        else if(dir==Direction.EAST &&(pos==2 || pos==5 || pos==8))
            a=OFF_BOARD;
        else if(dir==Direction.NORTH &&(pos==0 || pos==1 || pos==2))
            a=OFF_BOARD;
        else if(dir==Direction.SOUTH &&(pos==6 || pos==7 || pos==8))
            a=OFF_BOARD;
        else if(dir==Direction.EAST)
            a=pos+1;
        else if(dir==Direction.WEST)
            a=pos-1;
        else if(dir==Direction.NORTH)
            a=pos-3;
        else if(dir==Direction.SOUTH)
            a=pos+3;



        return a; // Done
    }


    /**
     * Determine whether a given tile can be moved.
     *
     * A tile can only be moved if the following conditions are met:
     * - The Tile does not contain the Peg.
     * - The Tile is adjacent to an empty (null) space.
     *
     * @param tile the tile to be moved.
     * @return true if the tile can be moved, false otherwise
     */
    public boolean canMoveTile(Tile tile) {
        //Initialisation
        String a=getBoardState();
        int[] index={1,3,5,7,9,11,13,15,16};
        char[] pos={'0','1','2','3','4','5','6','7','8'};
        char[] BoardS_pos=new char[8];
        int tile_pos=tile.position;
        int miss = 0;
        boolean check;
        int count;

        // Creating array for board state
        char[] bs=a.toCharArray();

        //Creating array for only positions of tiles
        for(int j=0;j<=7;j++) {
            int f=index[j];
            BoardS_pos[j] = bs[f];}


        //Finding null space on board
        for(int i=0;i<9;i++){
            count=0;
            for(int k=0;k<8;k++){
                if(pos[i]!=BoardS_pos[k])
                    count++;
            } if(count==8) {
                miss=i;
            }}

        //Peg position
        char p=bs[16];
        int pos_peg=Character.getNumericValue(p);


        //Conditions to move tile
        if(tile_pos==pos_peg)
            check=false;
        else if(tile_pos != pos_peg && (tile_pos+1==miss && tile_pos!=2 && tile_pos!=5) || miss ==tile_pos-1 || miss==tile_pos+3 || miss==tile_pos-3)
            check=true;
        else
            check=false;



        return check;  // Done Task 8
    }

    /**
     * Determine whether a Tile movement is valid.
     * A tile movement is valid under the following conditions:
     * - The Tile does not move off the board
     * - The Tile moves into an empty (null) space.
     * - The Tile being moved does not contain the Peg.
     *
     * @param tile The Tile to be moved.
     * @param direction the direction the tile is to be moved in.
     * @return true if the movement is valid, false if it is invalid.
     */
    public boolean isTileMovementValid(Tile tile, Direction direction) {
        //Initialisation
        String a=getBoardState();
        int tile_pos=tile.position;
        boolean check = false;
        char[] num_bs=new char[8];
        int[] ind={1,3,5,7,9,11,13,15,16};
        char[] pos={'0','1','2','3','4','5','6','7','8'};
        int count;
        int miss = 0;

        // Creating array for board state and getting position of peg
        char[] bs=a.toCharArray();
        int pos_peg=Character.getNumericValue(bs[16]);


        //Creating array for tile positions only
        for(int j=0;j<=7;j++) {
            int f=ind[j];
            num_bs[j] = bs[f];}


        //Finding null space on board
        for(int i=0;i<9;i++){
            count=0;
            for(int k=0;k<8;k++){
                if(pos[i]!=num_bs[k])
                    count++;
            } if(count==8) {
                miss =i ;
            }}


        // Checking conditions for valid tile movement
        if(direction==Direction.NORTH &&(tile_pos==0 || tile_pos==1 || tile_pos==2))
            check=false;
        else if(direction==Direction.SOUTH && (tile_pos==6 || tile_pos==7 || tile_pos==8))
            check=false;
        else if(direction==Direction.WEST && (tile_pos==0 || tile_pos==3 || tile_pos==6))
            check=false;
        else if(direction==Direction.EAST && (tile_pos==2 || tile_pos==5 || tile_pos==8))
            check=false;
        else if(tile_pos==pos_peg)
            check=false;
        else if((miss==tile_pos+3 && direction==Direction.SOUTH)||(miss==tile_pos-3 && direction==Direction.NORTH))
            check=true;
        else if((miss==tile_pos-1 && direction==Direction.WEST)||(miss==tile_pos+1 && direction==Direction.EAST && tile_pos!=2 && tile_pos!=5))
            check=true;

            
        

        return check;  // Task 9 done
    }

    public Direction getDir(String a){
        Direction temp=null;
        switch(a){
            case "N":
                temp=Direction.NORTH;
                break;
            case "S":
                temp=Direction.SOUTH;
                break;
            case " W":
                temp=Direction.WEST;
                break;
            case "E":
                temp=Direction.EAST;
                break;

        }
        return temp;
    }



    /**
     * This method returns a path for one step of the peg from the starting tile to the
     * first valid stopping position from a specified exit.  The finishing point is considered
     * a valid stopping position.   The method starts the path by exiting the starting tile via
     * exit A if viaA is true, otherwise exiting via exit B.
     *
     * Note that this is a search problem, so the implementation of this method is most
     * likely recursive, which means that getPegPathToDestination() (where the destination is the
     * finish position) is probably best solved by calling
     * getPegPathStep() on viable neighbours.
     *
     * The format of the path is a series of direction characters 'N', 'E', 'S', 'W' followed
     * by a character representing the finishing position (either "F" to indicate the finish
     * was reached, or single digit (eg '0') representing position).  The direction characters
     * reflect the path to the next step (ie the path to the first tile on which the peg
     * may stop, including the finish).
     *
     * If there is no path to a valid position from this tile using the specified exit, return null.
     *
     * @param start The starting position for the traversal.
     * @param viaA  If true, leave the start tile via exit A, otherwise use exit B
     * @return a string representing the path to the next step for the peg (as a series of
     * directions and the endpoint), or null if there is no such path.
     *
     */

    public String getPegPathStep(Tile start, boolean viaA) {
        //Initialisation
        int peg_pos=start.position;
        System.out.println("peg pos "+peg_pos);
        viaA=true;

        String a=getBoardState();
        char[] bs;
        char temp;
        int temp2;
        int one_pos;
        int two_pos ;
        String peg_path="";


        ArrayList<String> ori_arr=new ArrayList<>();
        ArrayList<Integer> pos_arr=new ArrayList<>();
        ArrayList<Integer> pos_check=new ArrayList<>();

        TileName [] type_ar={TileName.PLUS,TileName.EQUALS,TileName.SQUARE,TileName.TRIANGLE,TileName.EQUALS,TileName.CIRCLE,TileName.STAR,TileName.DIAMOND};

        TileName equals=TileName.EQUALS;
        TileName plus=TileName.PLUS;
        TileName diamond=TileName.DIAMOND;
        TileName square=TileName.SQUARE;
        TileName star=TileName.STAR;
        TileName triangle=TileName.TRIANGLE;
        TileName cross=TileName.CROSS;
        TileName circle=TileName.CIRCLE;

        Direction north=Direction.NORTH;
        Direction south=Direction.SOUTH;
        Direction west=Direction.WEST;
        Direction east=Direction.EAST;

        bs=a.toCharArray();

        System.out.println(bs);

        String[] bs_st = new String[17];

        for(int j=0;j<17;j++){
            temp=bs[j];
            bs_st[j]=String.valueOf(temp);
        }

        for(int i=0;i<16;i=i+2){
            ori_arr.add(bs_st[i]);
        }

        for(int k=1;k<16;k=k+2){
            temp2=Integer.parseInt(bs_st[k]);
            pos_arr.add(temp2);
        }

        switch(peg_pos) {
            case 0:
                pos_check.add(1);
                pos_check.add(3);
                break;
            case 1:
                pos_check.add(0);
                pos_check.add(4);
                pos_check.add(2);
                break;
            case 2:
                pos_check.add(1);
                pos_check.add(5);
                break;
            case 3:
                pos_check.add(0);
                pos_check.add(4);
                pos_check.add(6);
                break;
            case 4:
                pos_check.add(1);
                pos_check.add(3);
                pos_check.add(7);
                pos_check.add(5);
                break;
            case 5:
                pos_check.add(2);
                pos_check.add(4);
                pos_check.add(8);
                break;
            case 6:
                pos_check.add(3);
                pos_check.add(7);
                break;
            case 7:
                pos_check.add(6);
                pos_check.add(4);
                pos_check.add(8);
                break;
            case 8:
                pos_check.add(7);
                pos_check.add(5);
                break;
        }

        System.out.println("pos_check "+pos_check);
        int ind=pos_arr.indexOf(peg_pos);


        String one_o=ori_arr.get(ind);
        Direction one_d=getDir(one_o);
        TileName one_t=type_ar[ind];
        one_pos=peg_pos;




        for(int h=0;h<pos_check.size();h++) {
            two_pos = pos_check.get(h);// getting value of adjacent tile

            System.out.println("Position of adjacent tile " + two_pos);
            boolean check_pos = pos_arr.contains(two_pos);
            if (check_pos == false)
                continue;
            System.out.println("pos_check " + pos_check);
            System.out.println(two_pos);

            int ind2 = pos_arr.indexOf(two_pos);// getting index of position 2
            String two_o = ori_arr.get(ind2);
            Direction two_d = getDir(two_o);
            TileName two_t = type_ar[ind2];


            System.out.println(pos_arr);
            System.out.println(ori_arr);

            System.out.println("one_t " + one_t);
            System.out.println("one_o " + one_o);
            System.out.println("one_pos " + one_pos);

            System.out.println("two_t " + two_t);
            System.out.println("two_o " + two_o);
            System.out.println("two_pos " + two_pos);

            // GREEN RIGHT
            if ((one_t == equals && (one_d == north || one_d == south)) || (one_t == square && (one_d == south || one_d == west)) || (one_t == plus && (one_d == north || one_d == south)) || ((one_t == star || one_t == diamond) && one_d == north)) {
                if (two_pos == one_pos + 1 && one_pos != 2 && one_pos != 5 && one_pos != 8) {
                    if ((two_t == plus && (two_d == north || two_d == south)) || ((two_t == equals || two_t == square) && (two_d == north || two_d == east)) || (two_t == star && two_d == south) || (two_t == diamond && two_d == south)) {
                        if (one_d == south)
                            viaA = false;
                        if (two_t != plus || two_t != equals || two_t != square) {
                            System.out.println("reached");
                            peg_path = peg_path + "E";
                            peg_pos = peg_pos + 1;
                            System.out.println("Position of peg after going right green " + peg_pos);
                            if (one_d==north)
                                viaA=false;

                        }

                    }

                }
            }



            // GREEN UP
            if (((one_t == equals || one_t == square) && (one_d == east || one_d == south)) || (one_t == plus && (one_d == east || one_d == west)) || ((one_t == star || one_t == diamond) && one_d == west)) {
                if (two_pos == one_pos - 3 && one_pos != 1 && one_pos != 0 && one_pos != 2) {
                    System.out.println("Inside green up");
                    if ((two_t == plus && (two_d == east || two_d == west)) || ((two_t == equals || two_t == square) && (two_d == north || two_d == west)) || ((two_t == star || two_t == diamond) && two_d == east)) {
                        if (one_d == east)
                            viaA = false;
                        if (two_t != plus || two_t != equals || two_t != square) {
                            System.out.println("reached");
                            peg_path = peg_path + "N";
                            peg_pos = peg_pos - 3;
                            System.out.println("Position of peg after going up green " + peg_pos);
                            if (one_d==north)
                                viaA=false;

                        }

                    }
                }

            }





            // GREEN LEFT
            if ((one_t == equals && (one_d == north || one_d == east)) || (one_t == square && (one_d == north || one_d == east)) || (one_t == plus && (one_d == north || one_d == south)) || ((one_t == star || one_t == diamond) && one_d == south)) {
                System.out.println("In green left loop 1");
                if (two_pos == one_pos - 1 && one_pos != 0 && one_pos != 3 && one_pos != 6) {
                    System.out.println("In green left loop 2");
                    if ((two_t == plus && (two_d == north || two_d == south)) || ((two_t == equals || two_t == square) && (two_d == south || two_d == west)) || (two_t == star && two_d == north) || (two_t == diamond && two_d == north)) {
                        System.out.println(" In green left loop 3");
                        if (two_t != plus || two_t != equals || two_t != square) {
                            System.out.println("reached");
                            peg_path = peg_path + "W";
                            peg_pos = peg_pos - 1;
                            one_pos=two_pos;
                            two_pos=one_pos-1;
                            System.out.println("Position of peg after going left " + peg_pos);
                            if (one_d==north)
                                viaA=false;

                        }



                    }
                }
            }
            //GREEN DOWN
            if(((one_t==equals || one_t==square) && (one_d==north|| one_d==west)) || (one_t==plus && (one_d==east || one_d==west))|| ((one_t==star || one_t==diamond) && one_d==east)){
                if(two_pos==one_pos+3 && one_pos!=7 && one_pos!=6 && one_pos!=8){
                    if((two_t==plus && (two_d==east || two_d==west)) || ((two_t==equals || two_t==square) && (two_d==east || two_d==south)) || ((two_t==star || two_t==diamond) && two_d==west)){
                        if (two_t != plus || two_t != equals || two_t != square) {
                            System.out.println("reached");
                            peg_path = peg_path + "S";
                            peg_pos = peg_pos + 3;
                            one_pos=two_pos;
                            System.out.println("Position of peg after going left " + peg_pos);
                            if (one_d==north)
                                viaA=false;

                        }

                    }
                }

            }

            //BROWN RIGHT
            if(((one_t==triangle|| one_t==cross|| one_t==circle) && (one_d==south || one_d==west)) ||((one_t==star || one_t== diamond) && one_d==south)){
                if(two_pos==one_pos+1 && one_pos!=2 && one_pos!=5 && one_pos!=8){
                    if( ((two_t==triangle || two_t==cross || two_t==circle) && (two_d==north || two_d==east)) || ((two_t==star || two_t==diamond) && two_d==north)){
                        if (two_t != plus || two_t != equals || two_t != square) {
                            System.out.println("reached");
                            peg_path = peg_path + "E";
                            peg_pos = peg_pos + 1;
                            System.out.println("Position of peg after going right brown " + peg_pos);
                            if (one_d==south)
                                viaA=false;

                        }

                    }
                }

            }

            //BROWN LEFT
            if(((one_t==triangle|| one_t==cross|| one_t==circle) && (one_d==north || one_d==east)) ||((one_t==star || one_t== diamond) && one_d==north)){
                if(two_pos==one_pos-1 && one_pos!=0 && one_pos!=3 && one_pos!=6){
                    if( ((two_t==triangle || two_t==cross || two_t==circle) && (two_d==south || two_d==west)) || ((two_t==star || two_t==diamond) && two_d==south)){
                        if (two_t != plus || two_t != equals || two_t != square) {
                            System.out.println("reached");
                            peg_path = peg_path + "W";
                            peg_pos = peg_pos - 1;
                            System.out.println("Position of peg after going left brown " + peg_pos);
                            if (one_d==north)
                                viaA=false;

                        }

                    }
                }

            }

            //BROWN UP
            if(((one_t==triangle|| one_t==cross|| one_t==circle) && (one_d==south || one_d==east)) ||((one_t==star || one_t== diamond) && one_d==east)){
                if(two_pos==one_pos-3 && one_pos!=0 && one_pos!=1 && one_pos!=2){
                    if( ((two_t==triangle || two_t==cross || two_t==circle) && (two_d==north || two_d==west)) || ((two_t==star || two_t==diamond) && two_d==west)){
                        if (two_t != plus || two_t != equals || two_t != square) {
                            System.out.println("reached");
                            peg_path = peg_path + "N";
                            peg_pos = peg_pos + 3;
                            System.out.println("Position of peg after going up brown " + peg_pos);
                            if (one_d==east)
                                viaA=false;

                        }

                    }
                }

            }
            //BROWN DOWN
            if(((one_t==triangle|| one_t==cross|| one_t==circle) && (one_d==north || one_d==west)) ||((one_t==star || one_t== diamond) && one_d==west)){
                if(two_pos==one_pos+3 && one_pos!=6 && one_pos!=7 && one_pos!=8){
                    if( ((two_t==triangle || two_t==cross || two_t==circle) && (two_d==east || two_d==south)) || ((two_t==star || two_t==diamond) && two_d==east)){
                        if (two_t != plus || two_t != equals || two_t != square) {
                            System.out.println("reached");
                            peg_path = peg_path + "S";
                            peg_pos = peg_pos + 3;
                            System.out.println("Position of peg after going down brown " + peg_pos);
                            if (one_d==west)
                                viaA=false;

                        }

                    }
                }

            }

        }

        System.out.println("Peg path " + peg_path);
        System.out.println("Peg pos " + peg_pos);
        String temp_path;
        if (peg_path=="")
            temp_path=null;
        else
            temp_path=peg_path+peg_pos;
        System.out.println(peg_path);

        return temp_path;  // FIXME Task 11 (D)
    }



    /**
     * Return a path for one step of the peg from its current position.
     *
     * @param viaA if true, attempt the path via the tile's exit A, otherwise exit B
     * @return a string representing the path to the next step for the peg, or null if
     * there is no such path.
     */
    public String getPegPathStep(boolean viaA) {
        return getPegPathStep(board[pegPosition], viaA);
    }

    /**
     * Return the path to the finish position if one exists.
     *
     * Starting at position start, exit via exit A if viaA is true, otherwise
     * via exit B, and keep moving the peg for more steps as long as there is a valid way forward.
     * If there is a path for the peg that goes all the way to the
     * finish position, then return a string representing that path.  The
     * string should contain a series of direction characters 'N', 'E', 'S', 'W',
     * or should be null if there is no path to the finish.
     *
     * @param start The tile from which to start
     * @param dest The destination tile as a character '0' ... '8', or 'F' for the finish point.
     * @param viaA If true, start the search from exit A of the start tiles, otherwise use exit B.
     * @return A string representing the series of directions taken to reach the
     * finish position, or null if there is no path.
     */
    public String getPegPathToDestination(Tile start, char dest, boolean viaA) {
        return null;  // FIXME Task 12 (HD)
    }

    /**
     * Find the solutions to the game (the current TempleTrap object).
     *
     * Notice that this question is an advanced question and is entirely
     * optional.   You will need to use advanced data types and will
     * need to understand how to perform a search, most likely using
     * recursion, which is not covered until lecture unit C1.
     *
     * @return A set of strings, each representing a placement of all tiles,
     * which satisfies all of the game objectives.
     * eg: "hSaNeEpWWNW" translates to:
     * move tile `h` South, then tile `a` North, tile `e` East, then move peg `p` WEST, WEST, NORTH, WEST.
     */
    public Set<String> getSolutions() {
        return null; // FIXME Task 13 (HD)
    }
}
