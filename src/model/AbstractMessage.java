//Abstract message type for all message.
package model;

/**Abstract class of a Message Object type.
 * @author ZhengZhong
 * @version 02/27
 */
public abstract class AbstractMessage implements Message {
    
 /**
     * Variable Stores timestamp.
     */
    private final int myTime;
    /**
     * Variable Stores raceID.
     */
    
   
    /**Constrauct an message object type.
     * @param theTime the current time stamp;
     */
    protected AbstractMessage(final int theTime) {
      
        this.myTime = theTime;
        
    }

    /**
     *Compare to the current input object is message type.
     */
    @Override
    public int compareTo(final Message theMessage) {
        
        
        return myTime;

    }
    

    /**
     * Return the time stamp.
     */
    @Override
    public int getTimeStamp() {
        // TODO Auto-generated method stub
        return myTime;
    }

    /**
     *Return the message of this object.
     */
    @Override
    public String getMessage() {
        final StringBuilder sb = new StringBuilder();
        sb.append(myTime);
        return sb.toString();
    }

}
