//Object stores leaderboard information.
package model;

/**
 * @author Zheng Zhong
 * @version winter 2020
 *
 */
public class LeaderBoardMessage extends AbstractMessage implements LeaderBoard {
    
    /**
     * Maximum ID value.
     */
    private static final int MAX_ID_VALUE = 100;
   
    /**
     * All RacerID.
     */
    private final int[] myRacerIDs;
    
    
    /**Constructs a leaderboard object.
     * @param theTime the time.
     * @param theRacerIDs all racer ID following it.
     */
    public LeaderBoardMessage(final int theTime, final int[] theRacerIDs) {
        super(theTime);
        for (int i = 0; i < theRacerIDs.length; i++) {
            if (theRacerIDs[i] < 0 | theRacerIDs[i] > MAX_ID_VALUE) {
                throw new IllegalArgumentException("Vaild ID should be an nonnegative"
                                + "and less than 100");
            }
        }
        myRacerIDs = theRacerIDs;
        
        
    }

    @Override
    public int[] getLeaderBoard() {
//        printLeader();
        return myRacerIDs;
        
    
    }
    @Override
    public String getMessage() {
        
  
        final StringBuilder sb = new StringBuilder();
        sb.append("$L:");
        sb.append(super.getMessage());
        for (int i = 0; i < myRacerIDs.length; i++) {
            sb.append(":" + myRacerIDs[i]);
                
        }

        return sb.toString();
        
    }

}



