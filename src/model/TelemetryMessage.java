//Class stotes TelemetryMessage.
package model;

public class TelemetryMessage extends AbstractMessage implements Telemetry {
    /**
     * Seperator for String.
     */
    private static final String SEPERATOR = ":";
    /**
     * Maximum ID value.
     */
    private static final int MAX_ID_VALUE = 100;
    
    /**
     * Variable of RacerID.
     */
    private final int myRacerID;
    
    /**
     * Variable of distance.
     */
    private final double myDistance;
    
    /**
     * Variable of lap number.
     */
    private final int myLapNum;
    
   
    /**Constructs TelemetryMessage.
     * @param theTime Current time stamp.
     * @param theRacerID Current RacerID.
     * @param theDistance Distance to the line.
     * @param theLap the number of lap.
     */
    public TelemetryMessage(final int theTime, final int theRacerID,
                               final double theDistance , final int theLap) {
        super(theTime);
        if (theRacerID < 0 | theRacerID > MAX_ID_VALUE) {
            throw new IllegalArgumentException("Vaild ID should be an nonnegative"
                            + "and less than 100");
        }
        myRacerID = theRacerID;
        myDistance = theDistance;
        myLapNum = theLap;
        
        // TODO Auto-generated constructor stub
    }

    /**
     *Return the raceID.
     */
    @Override
    public int getNumber() {
        // TODO Auto-generated method stub
        return myRacerID;
    }

    /**
     *Return the distance.
     */
    @Override
    public double getDistance() {
        // TODO Auto-generated method stub
        return myDistance;
    }

    /**
     *Return the lapNum.
     */
    @Override
    public int getLap() {
        // TODO Auto-generated method stub
        return myLapNum;
    }
    @Override
    public String getMessage() {
        final StringBuilder sb = new StringBuilder();
        sb.append("$T:");
        sb.append(super.getMessage() + SEPERATOR);
        sb.append(myRacerID +  SEPERATOR);
        sb.append(myDistance + SEPERATOR);
        sb.append(myLapNum);
        
        
        return sb.toString();
    }
}
