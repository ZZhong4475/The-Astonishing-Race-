//RaceModel for Observer pattern.

package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * @author Zheng Zhong
 * @version 03/01/2020
 *
 */
public class RaceModel implements PropertyChangeEnabledRaceControls {
    
    /**
     * Colon sign for separate String.
     */
    private static final String SEPARATOR = ":";
    /**
     * TelemeteryMessage header.
     */
    private static final String T_PREFIX = "$T";
    /**
     * LineCrossingMessage header.
     */
    private static final String C_PREFIX = "$C";
    /**
     * LeaderBoarMessage header.
     */
    private static final String L_PREFIX = "$L";
    
    /**
     * Loading race information message.
     */
    private static final String RACEINFORLOAD = "Loading Race Information";
    /**
     * loading message information.
     */
    private static final String MESSAGELOAD = "Loading Message Information";
    /**
     * Message shows loading is done.
     */
    private static final String FINISHLOAD = "Done";
    /**
     * Variable of Property change support.
     */
    private final PropertyChangeSupport myPcs;
    /**
     * List stores MessageList.
     */
    private final List<List<Message>> myAllMessages;
    /**
     * variable of current time.
     */
    private int myCurrentTime;
    /**
     * List stores current racer's ID.
     */
    private final List<RaceParticipant> myPartcipantList;
    
    /**
     * Variable stores current leaderBoard Message.
     */
    private LeaderBoardMessage myCurrentLeader;
    /**
     * A node empty list for MessageList.
     */
    private final List<Message> myNodeList;
    /**
     * Variable stores racer information.
     */
    private RaceInfo myRaceInformation;
    /**
     * Array stores racers.
     */
    private int[] myRacers;
    /**
     * List of boardcast message.
     */
    private List<Message> myBoardCastMessage;
    
    /**
     * Constructs a RaceModel.
     */
    
    public RaceModel() {
        myPcs = new PropertyChangeSupport(this);
        myAllMessages = new ArrayList<>();
        myNodeList = new ArrayList<>();
        myAllMessages.add(myNodeList);
        myPartcipantList = new ArrayList<>();
        myCurrentTime = 0;
   

    }
    /**
     * Advance call when the message from current time to its +1 poisiton.
     */
    @Override
    public void advance() {
        advance(1);
        // TODO Auto-generated method stub
        
    }

    /**
     *Advance to a specific location.
     */
    @Override
    public void advance(final int theMillisecond) {
        boolean isEnd = false;
        final int oldTime = myCurrentTime;
        final int newTime = myCurrentTime + theMillisecond;
        myCurrentTime = newTime;
        if (theMillisecond < 0) {
            throw new IllegalArgumentException(" Time cannot be negative.");
        } else if (newTime  >= myRaceInformation.getTotalTime()) {
            myPcs.firePropertyChange(PROPERTY_RACE_STATUS, null, false);
            myCurrentTime = myRaceInformation.getTotalTime() - 1;
            isEnd = true;
            
            
        } 
        
        
        myPcs.firePropertyChange(PROPERTY_TIME, oldTime, myCurrentTime);
        if (isEnd) {
            for (int i = oldTime; i < myCurrentTime + 1; i++) {
                for (final Message message:myAllMessages.get(i)) {
                    myPcs.firePropertyChange(PROPERTY_MESSAGE, null, message);
                    myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null, 
                                             message.getMessage());
                    
                }
            
            }
                
        } else {
      
            for (int i = oldTime; i < myCurrentTime; i++) {
                for (final Message message:myAllMessages.get(i)) {
                    myPcs.firePropertyChange(PROPERTY_MESSAGE, null, message);
                    myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, 
                                             null, message.getMessage());
                   
                }
            }
        }
    }
    
    /**
     *Move to an assigned position.
     */
    @Override
    public void moveTo(final int theMillisecond) {
        
        if (theMillisecond < 0) {
            throw new IllegalArgumentException("Time cannot be negative.");
        } 
        
        final int oldTime = myCurrentTime;
        myCurrentTime = theMillisecond;
        if (theMillisecond >= myRaceInformation.getTotalTime()) {
            myCurrentTime = myRaceInformation.getTotalTime() - 1;
            throw new IllegalArgumentException("The game is end.");
            
        }
        if (theMillisecond == 0) {
            myPcs.firePropertyChange(PROPERTY_RACE_STATUS, null, true);
        }
       
        myPcs.firePropertyChange(PROPERTY_TIME, oldTime, myCurrentTime);
        if (myCurrentTime > oldTime) {
            forwardMove(oldTime, myCurrentTime);
        } else if (myCurrentTime < oldTime) {
            backWardMove(oldTime, myCurrentTime);
        }
    }
    
    
    /**Forward moving.
     * @param start the current time poistion.
     * @param end   the end posistion.
     */
    private void forwardMove(final int start,final int end) {
       
        final Map map = new HashMap<Integer , Message>();
        
        boolean foundLeader = false;
        boolean foundAllRacers = false;
        
        while (map.isEmpty() && !foundLeader) {
            int racerNum = 0;
            for (int i = end; i >= start; i--) {
                for (final Message message:myAllMessages.get(i)) {
                    if (message instanceof TelemetryMessage && !foundAllRacers) {
                        final Telemetry telemessage = (TelemetryMessage) message;
                       
                        if (!map.containsKey(telemessage.getNumber())) {
                            map.put((Integer) telemessage.getNumber(), telemessage);
                            racerNum++;
                          
                            if (racerNum == myRacers.length) {
                                foundAllRacers = true;
                            }
                     
                         
                            
                        }
                     
                    }
                    if (message instanceof LeaderBoardMessage) {
                        myCurrentLeader = (LeaderBoardMessage) message;
                        myPcs.firePropertyChange(PROPERTY_MESSAGE, null, myCurrentLeader);
                        foundLeader = true;
                    }
                   
                }
            
                
               
            }
            if (!foundLeader) {
                for (int a = end; a >= 0; a--) {
                    for (final Message message:myAllMessages.get(a)) {
                        if (message instanceof LeaderBoardMessage) {
                            myCurrentLeader = (LeaderBoardMessage) message;
                            myPcs.firePropertyChange(PROPERTY_MESSAGE, null, myCurrentLeader);
                            foundLeader = true;
                        }
                    }
                   
                }
            }

        }
   
        final Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final TelemetryMessage mess = (TelemetryMessage) entry.getValue();
            myPcs.firePropertyChange(PROPERTY_MESSAGE, null, mess);
            myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null, mess.getMessage());
        }
      
     
    }
    /**BackWard method.
     * @param theOld old time
     * @param theNew current time
     */
    private void backWardMove(final int theOld,final int theNew){
        
        final Map map = new HashMap<Integer,Message>();
        
        boolean foundLeader = false;
        boolean foundAllRacers=false;
        
        int oldtime = theOld;
        final int newtime = theNew;
        
        if (oldtime >= myRaceInformation.getTotalTime()) {
            oldtime = myRaceInformation.getTotalTime() - 1;
            myPcs.firePropertyChange(PROPERTY_MESSAGE, null,
                                     myAllMessages.get(oldtime).get(oldtime).getMessage());
        }
        
        while (map.isEmpty() && !foundLeader) {
            int racerNum = 0;
            for (int i = newtime; i < oldtime; i++) {
                for (final Message message:myAllMessages.get(i)) {
                    if (message instanceof TelemetryMessage && !foundAllRacers) {
                        final Telemetry telemessage = (TelemetryMessage) message;
                       
                        if (!map.containsKey(telemessage.getNumber())) {
                            map.put((Integer) telemessage.getNumber(), telemessage);
                            racerNum++;
                          
                            if (racerNum == myRacers.length) {
                                foundAllRacers = true;
                            }
                  
                         
                            
                        }
                     
                    }
                    if (message instanceof LeaderBoardMessage) {
                        myCurrentLeader = (LeaderBoardMessage) message;
                        myPcs.firePropertyChange(PROPERTY_MESSAGE, null, myCurrentLeader);
                        foundLeader = true;
                    }
                   
                }
            
                
               
            }
            if (!foundLeader) {
                for (int a = 0; a < oldtime; a++) {
                    for (final Message message:myAllMessages.get(a)) {
                        if (message instanceof LeaderBoardMessage) {
                            myCurrentLeader = (LeaderBoardMessage) message;
                            myPcs.firePropertyChange(PROPERTY_MESSAGE, null, myCurrentLeader);
                            foundLeader = true;
                        }
                    }
                   
                }
            }

        }
   
        final Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry entry = (Map.Entry) iter.next();
            final TelemetryMessage mess = (TelemetryMessage) entry.getValue();
            myPcs.firePropertyChange(PROPERTY_MESSAGE, null, mess);
            myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null, mess.getMessage());
        }
      
     
    }

    /**
     *Reading the file and store in array.
     */
    @Override
    public void loadRace(final File theRaceFile) throws IOException {
        final String extension = theRaceFile.getName().
                                        substring(theRaceFile.getName().lastIndexOf(".")+1,
                                                  theRaceFile.getName().length());
        if ("rce".equals(extension)) {
            myAllMessages.clear();
            int width = 0;
            int height = 0;
            int distance = 0;
            int lastTime = 0;
            String raceName = null;
            String trackType = null;
            final File currentFile = theRaceFile;
            final Scanner file = new Scanner(currentFile);
            int participantNum = 0;
            while (file.hasNext()) {
                final String[] reader = file.nextLine().split(SEPARATOR);
                final int currentTime;
                if (reader[0].equals("#RACE")) {
                    raceName = reader[1];
                } else if (reader[0].equals("#TRACK")) {
                    trackType = reader[1];
                } else if (reader[0].equals("#WIDTH")) {
                    width = Integer.parseInt(reader[1]);
                } else if (reader[0].equals("#HEIGHT")) {
                    height = Integer.parseInt(reader[1]);
                } else if (reader[0].equals("#DISTANCE")) {
                    distance = Integer.parseInt(reader[1]);
                } else if (reader[0].equals("#TIME")) {
                    lastTime = Integer.parseInt(reader[1]);
                    for (int b = 0; b < lastTime; b++) {
                        myAllMessages.add(new ArrayList<>());
                    }
               
                } else if (reader[0].equals("#PARTICIPANTS")) {
                    participantNum = Integer.parseInt(reader[1]);
                    myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null, RACEINFORLOAD);
                } else if (reader[0].equals(T_PREFIX)) {
                    currentTime = Integer.parseInt(reader[1]);
                    final TelemetryMessage currentMessage = new TelemetryMessage(currentTime, 
                                                        Integer.parseInt(reader[2]),
                                                        Double.parseDouble(reader[2 + 1]),
                                                        Integer.parseInt(reader[2 + 2]));
                    myAllMessages.get(currentTime).add(currentMessage);
                } else if (reader[0].equals(L_PREFIX)) {
                    currentTime = Integer.parseInt(reader[1]);
                    myRacers = new int[reader.length - 2 ];
                    for (int a = 2; a < reader.length; a++) {
                        myRacers[a - 2] = Integer.parseInt(reader[a]);
                    }
                    final LeaderBoardMessage currentMessage = new LeaderBoardMessage(
                                                                        currentTime,
                                                                         myRacers);
                    myAllMessages.get(currentTime).add(currentMessage);
                
                } else if (reader[0].equals(C_PREFIX)) {
                    currentTime = Integer.parseInt(reader[1]);
                    final boolean isPass = true;           
                    final LineCrossingMessage currentMessage = new LineCrossingMessage(
                                                                   Integer.parseInt(reader[1]),
                                                                   Integer.parseInt(reader[2]),
                                                     Integer.parseInt(reader[2 + 1]), isPass);
                    myAllMessages.get(currentTime).add(currentMessage);
                } else {
                    myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null, MESSAGELOAD);

                    final int racerID = Integer.parseInt(reader[0].substring(1, 3));
                    final String racerName = reader[1];
                    final int racerDistance = (int) Double.parseDouble(reader[2]);
                    final ParticipantInfo partcipantmessage = new ParticipantInfo(racerName,
                                                                              racerID,
                                                                              racerDistance);
                    myPartcipantList.add(partcipantmessage);
                }
                      
            }
            myRaceInformation = new RaceInfo(myPartcipantList, lastTime,
                                       distance, raceName,
                                       trackType, height, width);
            myPcs.firePropertyChange(PROPERTY_RACE_INFORMATION, null, myRaceInformation);
            myPcs.firePropertyChange(PROPERTY_STRING_MESSAGE, null, FINISHLOAD);
            myPcs.firePropertyChange(PROPERTY_RACE_STATUS, null, true);
//        myCurrentTime=myRaceInformation.getTotalTime();
//        moveTo(0);
        }
        else {
            throw new IOException();
        }

    }
     
 
    
    
   
    

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(theListener);
        
    }

    @Override
    public void addPropertyChangeListener(final String thePropertyName,
                                          final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(thePropertyName, theListener);
        
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(theListener);
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removePropertyChangeListener(final String thePropertyName,
                                             final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(thePropertyName, theListener);
        // TODO Auto-generated method stub
        
    }

}



