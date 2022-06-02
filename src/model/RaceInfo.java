// Class stores race information.
package model;

import java.util.List;

/**Objects stores race information.
 * @author Zheng Zhong
 * @version Winter 2020
 *
 */
public class RaceInfo implements RaceInformation {
    /**
     * List stores racers.
     */
    private final List<RaceParticipant> myRacers;
    /**
     * Varirable for race total time.
     */
    private final int myTotalTime;
    /**
     * 
     */
    private final int myTrackDistance;
    /**
     * 
     */
    private final String myRaceName;
    /**
     * 
     */
    private final String myTrackType;
    /**
     * 
     */
    private final int myTrackHeigh;
    /**
     * 
     */
    private final int myTrackWidth;
    
    /** Constructs raceinformation objects.
     * @param theList List of racers
     * @param theTotalTime the total time.
     * @param theTrackDistance the track distance.
     * @param theRaceName the race type name.
     * @param theTrackType the race type.
     * @param theTrackHeigh the race height.
     * @param theTrackWidth the race width.
     */
    public RaceInfo(final List<RaceParticipant> theList, final int theTotalTime,
                    final int theTrackDistance, final String theRaceName,
                    final String theTrackType,
                    final int theTrackHeigh, final int theTrackWidth) {
        myRacers = theList;
        myTotalTime = theTotalTime;
        myTrackDistance = theTrackDistance;
        myRaceName = theRaceName;
        myTrackType = theTrackType;
        myTrackHeigh = theTrackHeigh;
        myTrackWidth = theTrackWidth;
                        
       
        
    }
    

    @Override
    public List<RaceParticipant> getParticipants() {
        // TODO Auto-generated method stub
        return myRacers;
    }

    @Override
    public int getTotalTime() {
        // TODO Auto-generated method stub
        return myTotalTime;
    }

    @Override
    public int getTrackDistance() {
        // TODO Auto-generated method stub
        return myTrackDistance;
    }

    @Override
    public String getRaceName() {
        // TODO Auto-generated method stub
        return myRaceName;
    }

    @Override
    public String getTrackType() {
        // TODO Auto-generated method stub
        return myTrackType;
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return myTrackHeigh;
    }

    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        return myTrackWidth;
    }

}
