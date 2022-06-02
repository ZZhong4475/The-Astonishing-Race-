//Object stores LineCrossing Object.
package model;

public class LineCrossingMessage extends AbstractMessage implements LineCrossing {
    
    /**
     * Seperator for String.
     */
    private static final String SEPERATOR = ":";
    /**
     * Maximum ID value.
     */
    private static final int MAX_ID_VALUE = 100;
    /**
     * Variable of Lap Number.
     */
    private final int myLapNum;
    /**
     * Variable of crossline.
     */
    private final boolean myFinish;
    /**
     * Varaible of RaceID.
     */
    private final int myRacerID;
    /**
     * Construct LineCrossingMessage Object.
     * @param theTime the time stamp.
     * @param theRacerID the RaceID.
     * @param theLapNum the Lap Number.
     * @param theFinish finish line varaiable.
     */
    public LineCrossingMessage(final int theTime, final int theRacerID,
                                  final int theLapNum , final boolean theFinish) {
        
        super(theTime);
        if (theRacerID < 0 | theRacerID > MAX_ID_VALUE) {
            throw new IllegalArgumentException("Vaild ID should be an nonnegative"
                            + "and less than 100");
        }
        myLapNum = theLapNum;
        myFinish = theFinish;
        myRacerID = theRacerID;
        // TODO Auto-generated constructor stub
    }

    /**
     *Return the crossline racer.
     */
    @Override
    public int getNumber() { 
        if (myFinish) {
            return myRacerID;  
        } 
        return 0;
    }

    /**
     *Return lapNum.
     */
    @Override
    public int getLap() {
        // TODO Auto-generated method stub
        return myLapNum;
    }

    /**
     *Return true if the racer is crossing the line.
     */
    @Override
    public boolean isFinished() {
        
        
        // TODO Auto-generated method stub
        return myFinish;
    }
    public String getMessage() {
        final StringBuilder sb = new StringBuilder();
        sb.append("$C:");
        sb.append(super.getMessage() + SEPERATOR);
        sb.append(myRacerID +  SEPERATOR);
        sb.append(myLapNum + SEPERATOR);
        sb.append(myFinish);
        
        
        return sb.toString();
    }
    

}
