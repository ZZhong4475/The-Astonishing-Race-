//Object stores individual Participant information.
package model;

public class ParticipantInfo implements RaceParticipant {
    /**
     * The maximum ID value.
     */
    private static final int MAX_ID_VALUE = 100;
    /**
     * Variables of participant's name.
     */
    private final String myName;
    /**
     * Variables of RacerID.
     */
    private final int myRacerID;
    /**
     * Varirable of starting distance.
     */
    private final int myDistance;
    
    /**Construct an Partcipant object.
     * @param theName particpant's name.
     * @param theRacerID participant's id.
     * @param theDistance participant's distance.
     */
    public ParticipantInfo(final String theName, final int theRacerID, final int theDistance) {
        if (theRacerID < 0 | theRacerID > MAX_ID_VALUE) {
            throw new IllegalArgumentException("Vaild ID should be an nonnegative"
                            + "and less than 100");
        }
        myName = theName;
        myRacerID = theRacerID;
        myDistance = theDistance;
        
        
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return myName;
    }

    @Override
    public int getNumber() {
        // TODO Auto-generated method stub
        return myRacerID;
    }

    @Override
    public int getStartingDistance() {
        // TODO Auto-generated method stub
        return myDistance;
    }

}
