//Assignment 4b, controller pane of Observer Design Pattern. TCSS 305 Winter 2020
package controller;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_STRING_MESSAGE;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_RACE_STATUS;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_RACE_INFORMATION;

import application.Main;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.PropertyChangeEnabledRaceControls;
import model.RaceInfo;
import model.RaceModel;
import model.RaceParticipant;
import view.Utilities;


/**
 * Controller class for control component.
 * 
 * @author Charles Bryan
 * @author Zheng Zhong
 * @version Winter 2020
 */

/**
 * @author zhong
 * @version 03/17/2020
 */
public class ControllerPanel extends JPanel implements PropertyChangeListener {

    /** The serialization ID. */
    private static final long serialVersionUID = -6759410572845422202L;
    
    /**
     * The Time Frequency variable for timer.
     */
    private static final int TIMER_FREQUENCY = 31;
     
    /**
     * The regular speed variable for speeding action.
     */
    private static final int REGULAR_SPEED = 1;
    
    /**
     * The fast speed variable for speeding action.
     */
    private static final int FAST_SPEED = 4;
    
    
    /**
     * Default cursor varaible.
     */
    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    
    /**
     * The wait cursor variable.
     */
    private static final Cursor WAIT_CURSOR = new Cursor(Cursor.WAIT_CURSOR);
    /**
     * The parameter 10 of J Components.
     */
    private static final int JVAL_TEN = 10;
    /**
     * The parameter 5 of J Components.
     */
    private static final int JVAL_FIVE = 5;
    
    /**
     * The ten second in millisec variavle for time slider tick spacing.
     */
    private static final int TENSECS_IN_MILLSECOND = 10000;
    
    /**
     * The sixty second in millisec variavle for time slider tick spacing.
     */
    private static final int MIN_IN_MILLSECOND = 60000;
    
    /**
     * The Space variable for spacing string.
     */
    private static final String SPACE = "\n";
    
    /**
     * The game status determine either end or start.
     */
    private boolean myGameStatus;
    
    
    
    /**
     * A variable stores race information.
     */
    private RaceInfo myRaceInformation;
    
    /** A reference to the backing Race Model. */
    private final PropertyChangeEnabledRaceControls myRace;
    
    /**
     * infoItem stores race information.
     */
    private final JMenuItem myInfoItem = new JMenuItem("Race Info...");

    /** Display of messages coming from the Race Model. */
    private final JTextArea myOutputArea;
    
    
    /** Display of messages coming from the Race Model. */
    private final JTextArea myPartcipant = new JTextArea(JVAL_TEN, JVAL_TEN * JVAL_FIVE);


    /** Panel to display CheckBoxs for each race Participant. */
    private final JPanel myParticipantPanel;

    /** A view on the race model  that displays the current race time. */
    private final JLabel myTimeLabel;

    /** A controller and view of the Race Model. */
    private final JSlider myTimeSlider;

    /** The list of javax.swing.Actions that make up the ToolBar (Controls) buttons. */
    private final List<Action> myControlActions = new ArrayList<>();

    /** The timer that advances the Race Model. */
    private final Timer myTimer;
    /**
     * The multiple of time speeding.
     */
    private int myMultipler = REGULAR_SPEED;
    
    /**
     * The variable refer to loop status.
     */
    private boolean myRepeat;
    /** Container to hold the different output areas. */
    private final JTabbedPane myTabbedPane;
    
    /** Jfile chooser for choose a file.
     * 
     */
    private final JFileChooser myFileChooser = new JFileChooser(".");
        
    /**
     * The race model.
     */
    private final RaceModel myRaceModel = new RaceModel();
    
    
    /**
     * Construct a ControllerPanel.
     * 
     * @param theRace the backing race model
     */
    
    public ControllerPanel(final PropertyChangeEnabledRaceControls theRace) {
        super();
        myOutputArea = new JTextArea(JVAL_TEN, JVAL_TEN * JVAL_FIVE);
        myTimeLabel = new JLabel(Utilities.formatTime(0));
        myRace = theRace;
        myTimeSlider = new JSlider(0, 0, 0);
        myTabbedPane = new JTabbedPane();
        myParticipantPanel = new JPanel();
        myTimer = new Timer(TIMER_FREQUENCY * myMultipler, this::handlerTimer);
        addListeners();
        setUpComponents();
        
    }
    
    /**
     * Displays a simple JFrame.
     */
    private void setUpComponents() {
        setLayout(new BorderLayout());
        
        // JPanel is a useful container for organizing components inside a JFrame
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(JVAL_TEN , JVAL_TEN, 
                                                            JVAL_TEN, JVAL_TEN));

        mainPanel.add(buildSliderPanel(), BorderLayout.NORTH);

        myOutputArea.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(myOutputArea);
        scrollPane.
        
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        myParticipantPanel.add(myPartcipant);
        myPartcipant.setEditable(false);
        myPartcipant.setBackground(getBackground());
        final JScrollPane participantScrollPane = new JScrollPane(myParticipantPanel);
        participantScrollPane.setPreferredSize(scrollPane.getSize());
       

        myTabbedPane.addTab("Data Output Stream", scrollPane);
        myTabbedPane.addTab("Race Participants", participantScrollPane);

        mainPanel.add(myTabbedPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        add(buildToolBar(), BorderLayout.SOUTH);
        myRaceModel.addPropertyChangeListener(this);
        myMultipler = REGULAR_SPEED;


        
        

    }
    
    
    private void handlerTimer(final ActionEvent theEvent) {
        myRaceModel.advance(TIMER_FREQUENCY * myMultipler);
 
    }

    /**
     * Builds the panel with the time slider and time label.
     * 
     * @return the panel
     */
    private JPanel buildSliderPanel() {
        //TODO These components require Event Handlers
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(JVAL_FIVE, JVAL_FIVE,
                                                        JVAL_FIVE * JVAL_FIVE, JVAL_FIVE));
        
        myTimeSlider.setBorder(BorderFactory.createEmptyBorder(JVAL_FIVE, JVAL_FIVE,
                                                               JVAL_FIVE, JVAL_FIVE));
        myTimeSlider.addChangeListener(new TimeSliderListener());

        panel.add(myTimeSlider, BorderLayout.CENTER);
        myTimeSlider.setEnabled(false);
        myTimeSlider.setValue(0);

        myTimeLabel.setBorder(BorderFactory.
                              createCompoundBorder(BorderFactory.createEtchedBorder(),
                                              BorderFactory.createEmptyBorder(JVAL_FIVE,
                                                                              JVAL_FIVE,
                                                                              JVAL_FIVE, 
                                                                              JVAL_FIVE)));
        final JPanel padding = new JPanel();
        padding.add(myTimeLabel);
        panel.add(padding, BorderLayout.EAST);
       

        return panel;
        
    }
    
    /**
     * Constructs a JMenuBar for the Frame.
     * @return the Menu Bar
     */
    private JMenuBar buildMenuBar() {
        final JMenuBar bar = new JMenuBar();
        bar.add(buildFileMenu());
        bar.add(buildControlsMenu(myControlActions));
        bar.add(buildHelpMenu());
        return bar;
        
    }
    
    /**
     * Builds the file menu for the menu bar.
     * 
     * @return the File menu
     */
    private JMenu buildFileMenu() {
        //TODO These components require Event Handlers

        final JMenu fileMenu = new JMenu("File");

        final JMenuItem load = new JMenuItem("Load Race...");
        
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                if (myTimer.isRunning()) {
                    myTimer.stop();
                    myOutputArea.setText("");
                    JOptionPane.showMessageDialog(null, 
                                                  "Please stop the current game first, "
                                                  + "then reload the new rce file");
                } else {
                    final int choose = myFileChooser.showOpenDialog(null);
                    if (choose == JFileChooser.APPROVE_OPTION) {
                        setCursor(WAIT_CURSOR);
                        final File selectedFile = myFileChooser.getSelectedFile();
                        try {
                            myRaceModel.loadRace(selectedFile);
                            myRaceModel.moveTo(0);
                            
                        } catch (final IOException e1) {
                            JOptionPane.showMessageDialog(null, "Wrong File,"
                                            + "Please select rce file");
                            
                        }
                    }
                }
            
            }
        });
        fileMenu.add(load);

        fileMenu.addSeparator();

        final JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(event -> { 
            System.exit(0); });

        fileMenu.add(exitItem);
        return fileMenu;
    }
    
    /**
     * Build the Controls JMenu.
     * 
     * @param theActions the Actions needed to add/create the items in this menu
     * @return the Controls JMenu
     */
    private JMenu buildControlsMenu(final List<Action> theActions) {
        final JMenu controlsMenu = new JMenu("Controls");

        for (final Action a : theActions) {
            controlsMenu.add(a);
        }

        return controlsMenu;
    }
    
    /**
     * Build the Help JMenu.
     * 
     * @return the Help JMenu
     */
    private JMenu buildHelpMenu() {
        //TODO These components require Event Handlers
        final JMenu helpMenu = new JMenu("Help");

        
        helpMenu.add(myInfoItem);
        myInfoItem.setEnabled(false);
        myInfoItem.addActionListener(event -> {
            JOptionPane.showMessageDialog(null, showMessage(myRaceInformation)); });

        final JMenuItem aboutItem = new JMenuItem("About...");
        
        helpMenu.add(aboutItem);
        aboutItem.addActionListener(event -> {
            JOptionPane.showMessageDialog(null, "TCSS 305 Winter 2020 by Zheng Zhong"); });
        return helpMenu;
    }

    /**
     * Build the toolbar from the Actions list.
     * 
     * @return the toolbar with buttons for all of the Actions in the list
     */
    private JToolBar buildToolBar() {
        final JToolBar toolBar = new JToolBar();
        for (final Action a : myControlActions) {
            final JButton b = new JButton(a);
            b.setHideActionText(true);
            toolBar.add(b);
        }
        return toolBar;
    }
    
    /**
     * Add actionListeners to the buttons. 
     */
    private void addListeners() {
       
      
        buildActions();

        
    }
    
    
    /**
     * Instantiate and add the Actions.
     */
    private void buildActions() {
      //TODO These components require Event Handlers
        myControlActions.add(new RestartAction("Restart", "/ic_restart.png"));
        
        final PlayAction play = new PlayAction("Play", 
                                               "/ic_play.png",
                                               "Pause",
                                               "/ic_pause.png");
        
        myControlActions.add(play);
        
        myControlActions.add(new SpeedAction("Times One", 
                                             "/ic_one_times.png",
                                             "Time Four",
                                             "/ic_four_times.png"));
        
        final RepeatAction repeat = new RepeatAction("Single Race", 
                                                     "/ic_repeat.png",
                                                     "Repeat Race",
                                                     "/ic_repeat_color.png");
        myControlActions.add(repeat);
        myControlActions.add(new ClearAction("Clear", "/ic_clear.png"));
        disableAction();
        
      
        myRaceModel.addPropertyChangeListener(PROPERTY_TIME, play);
        myRaceModel.addPropertyChangeListener(PROPERTY_TIME, repeat);
        
        
    }
    /**Helper method to disable all control actions.
     * 
     */
    private void disableAction() {
      
        for (int i = 0; i < myControlActions.size(); i++) {
            myControlActions.get(i).setEnabled(false);
        }
        
    }
   
        
    

    /**
     *Constructs property change listener for the controller.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        
        if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
            myTimeLabel.setText(Utilities.formatTime((Integer) theEvent.getNewValue()));
            myTimeSlider.setValue((int) theEvent.getNewValue());
            if ((int) theEvent.getNewValue() == myRaceInformation.getTotalTime() - 1) {
                myControlActions.get(1).setEnabled(false);
            } else {
                myControlActions.get(1).setEnabled(true);
            }
     
        } else if (PROPERTY_STRING_MESSAGE.equals(theEvent.getPropertyName())) {
                
            myOutputArea.append((String) theEvent.getNewValue() + SPACE);
             
        } else if (PROPERTY_RACE_STATUS.equals(theEvent.getPropertyName())) {
            myGameStatus = (boolean) theEvent.getNewValue();
            if (myGameStatus) {
                for (final Action action:myControlActions) {
                    action.setEnabled(true);
                }
                setCursor(DEFAULT_CURSOR);
                
                
            }
              
        } else if (PROPERTY_RACE_INFORMATION.equals(theEvent.getPropertyName())) {
            myRaceInformation = (RaceInfo) theEvent.getNewValue();
            
            myPartcipant.append(showPartiMessage(myRaceInformation.getParticipants()));
            myTimeSlider.setMaximum(myRaceInformation.getTotalTime() - 1);
            myTimeSlider.setMinimum(0);
            myTimeSlider.setEnabled(true);
            myTimeSlider.setMajorTickSpacing(MIN_IN_MILLSECOND);
            myTimeSlider.setMinorTickSpacing(TENSECS_IN_MILLSECOND);
            myTimeSlider.setPaintTicks(true);
            myTimeSlider.setPaintTrack(true);
            myInfoItem.setEnabled(true);
            
        }
            
    }
    
  
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        final JFrame frame = new JFrame("Astonishing Race!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //TODO instantiate your model here. 
        final PropertyChangeEnabledRaceControls race = null;
        
        //Create and set up the content pane.
        final ControllerPanel pane = new ControllerPanel(race);
        
        //Add the JMenuBar to the frame:
        frame.setJMenuBar(pane.buildMenuBar());
        
        pane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(pane);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    /**Helper method wrap up the race information.
     * @param theRaceInformation
     * @return Race Information.
     */
    private String showMessage(final RaceInfo theRaceInformation) {
        final RaceInfo raceinformation = theRaceInformation;
        final StringBuilder sb = new StringBuilder(1000);
        sb.append("RaceTime:");
        sb.append((String) raceinformation.getRaceName());
        sb.append(SPACE);
        sb.append("Race Type:");
        sb.append((String) raceinformation.getTrackType());
        sb.append(SPACE);
        sb.append("Total Time:");
        sb.append((String) Utilities.formatTime(raceinformation.getTotalTime()));
        sb.append(SPACE);
        sb.append("Total Distance:");
        sb.append(raceinformation.getTotalTime());
        sb.append(SPACE);
        return sb.toString();
    }
    
    /**Helps method print all participants.
     * @param thePartcipant
     * @return all participant messages.
     */
    private String showPartiMessage(final List<RaceParticipant> thePartcipant) {
        List<RaceParticipant> partcipants = new ArrayList<>();
        partcipants = thePartcipant;
        final StringBuilder sb = new StringBuilder(1000);
        for (int i = 0; i < partcipants.size(); i++) {
            System.out.println(partcipants.get(i).getName());
            sb.append(thePartcipant.get(i).getName());
            sb.append(SPACE);
        }
     
        return sb.toString();
    }

    /**
     * This is a simple implementation of an Action.
     * You will most likely not use this implementation in your final solution. Either
     * create your own Actions or alter this to suit the requirements for this assignment. 
     * 
     * @author Charles Bryan
     * @version Autumn 2019
     */
    protected class SimpleAction extends AbstractAction  {

        /** The serialization ID. */
        private static final long serialVersionUID = -3160383376683650991L;

        /**
         * Constructs a SimpleAction.
         * 
         * @param theText the text to display on this Action
         * @param theIcon the icon to display on this Action
         */
        
        private final String myText;
        /**
         * 
         */
        private final String myIcon;
        
        SimpleAction(final String theText, final String theIcon) {
            super(theText);
            myText = theText;
            myIcon = theIcon;
            
        }
        
        /**
         * Wrapper method to get a system resource.
         * 
         * @param theResource the name of the resource to retrieve
         * @return the resource
         */
        private URL getRes(final String theResource) {
            return Main.class.getResource(theResource);
        }
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
        
        }
        protected void setIcon(final String theIcon) {
            final ImageIcon icon = (ImageIcon) new ImageIcon(getRes(theIcon));
            final Image smallImage = icon.getImage().
                            getScaledInstance(16, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);

           
            
            final Image largeImage = icon.getImage().
                            getScaledInstance(24, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);
        }
        protected String getString() {
            return myText;
        }
        
        protected String getIconString() {
            return myIcon;
        }

      
    }
    
    /**
     * @author zhong
     * Implementation of restart botton behavior.
     *
     */
    private class RestartAction extends SimpleAction {
        
        

        /**
         * The serialization ID.
         */
        private static final long serialVersionUID = 4142052896050702562L;


        RestartAction(final String thePlayText, final String thePlayIcon) {
            super(thePlayText, thePlayIcon);
            setIcon(thePlayIcon);
          
            
        }
        
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            
            myRaceModel.moveTo(0);
            myRaceModel.advance();
            
            
        
        }
    }
        
    
    
    /**
     * Implementation of play botton behavior.
     *
     */
    private class PlayAction extends SimpleAction implements PropertyChangeListener {
        
        /**
         * The serialization ID.
         */
        private static final long serialVersionUID = 2242484695482904602L;
        /**
         * Variable stores pause text.
         */
        private final String myPauseText;
        /**
         * Variable store image object.
         */
        private final String myPauseIcon;
        PlayAction(final String thePlayText, final String thePlayIcon 
                   , final String thePauseText, final String thePauseIcon) {
            super(thePlayText, thePlayIcon);
            myPauseText = thePauseText;
            myPauseIcon = thePauseIcon;
            setIcon(thePlayIcon);
          
            
        }
        
      
       
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (myTimer.isRunning()) {
                myTimer.stop();
                putValue(Action.NAME, super.getString());
                setIcon(super.getIconString());
                myTimeSlider.setEnabled(true);
               
                
            } else {
                myTimer.start();
                putValue(Action.NAME, myPauseText);
                setIcon(myPauseIcon);
                myTimeSlider.setEnabled(false);
                
                
            }
        
            
            
            
        
        }


        @Override
        public void propertyChange(final PropertyChangeEvent theEvent) {
            if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                if ((int) theEvent.getNewValue() == myRaceInformation.getTotalTime() - 1) {
                    
                  
                        
                    putValue(Action.NAME, super.getString());
                    setIcon(super.getIconString());
                    myTimeSlider.setEnabled(true);
                    if (!myRepeat) {
                        myTimer.stop();
                    
                    }
                    
                } else if (myTimer.isRunning())  {
                       
                    putValue(Action.NAME, myPauseText);
                    setIcon(myPauseIcon);
                }
                    
                      
                
            }
            
            
        }
        
    }
    /**
     * @author zhong
     * Implementation of repeat bottom behaviors.
     *
     */
    private class RepeatAction extends SimpleAction implements PropertyChangeListener {
        
       /**
         * The serialization ID.
         */
        private static final long serialVersionUID = 5670766569176055213L;
    /**
     * Variable stores repeat text.
     */
        private final String myRepeatText;
       /**
     * Varibale stores repeat Icon.
     */
        private final String myRepeatIcon;
  
        
        RepeatAction(final String theRepeatToggle, 
                     final String theRepeatToggleIcon,
                     final String theRepeat,
                     final String theRepeatIcon) {
            super(theRepeatToggle, theRepeatToggleIcon);
            setIcon(theRepeatToggleIcon);
            myRepeatText = theRepeat;
            myRepeatIcon = theRepeatIcon;
            myRepeat = false;
          
            
        }
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
           
            if (myRepeat) {
                setIcon(super.getIconString());
                putValue(Action.NAME, super.getString());
                myRepeat = false;
            } else {
           
                setIcon(myRepeatIcon);
                putValue(Action.NAME, myRepeatText);
                myRepeat = true; 
            }
            
        
        }

        @Override
        public void propertyChange(final PropertyChangeEvent theEvent) {
            
            if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                
                final int newValue = (int) theEvent.getNewValue();
                
                if (newValue >= myRaceInformation.getTotalTime() - 1 && myRepeat) {
                    
                     
                    myOutputArea.setText("");
                    myRaceModel.moveTo(0);
                    myRaceModel.advance();
                    myTimeSlider.setEnabled(false);
                       
                    
                }
                
            }
            
        }
        
    }
    
    /**
     * @author zhong
     *Implementation of speed bottom action.
     */
    private class SpeedAction extends SimpleAction {
        
       /**
         * The serialization ID.
         */
        private static final long serialVersionUID = 3497125639377182819L;
    /**
        * Variable stores 4 time speed text.
        */
        private final String myFourTime;
       /**
       * varaible stores 4 time speed icon.
       */
        private final String myFourTimeIcon;

        SpeedAction(final String theOneTimeText, 
                    final String theOneTimeIcon,
                    final String theFourTimeText,
                    final String theFourTimeIcon) {
            super(theOneTimeText, theOneTimeIcon);
            setIcon(theOneTimeIcon);
            myFourTime = theFourTimeText;
            myFourTimeIcon = theFourTimeIcon;
          
            
        }
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            
            if (myMultipler == REGULAR_SPEED) {
                myMultipler = FAST_SPEED;
                putValue(Action.NAME, myFourTime);
                setIcon(myFourTimeIcon);
               
            } else {
                myMultipler = REGULAR_SPEED;
                putValue(Action.NAME, super.getString());
                setIcon(super.getIconString());
            }
            
            
            
        
        }
        
    }
    
   
    /**
     * @author Zheng Zhong
     * Implementation of clear bottom behaviors.
     *
     */
    private class ClearAction extends SimpleAction {
        
        

        /**
         * The serialization ID.
         */
        private static final long serialVersionUID = 6748162821575830738L;

        ClearAction(final String theClearText, final String theClearIcon) {
            super(theClearText, theClearIcon);
            setIcon(theClearIcon);
          
            
        }
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myOutputArea.setText("");
            
            
            
        
        }
        
    }
  

  
  
  /**
 * @author zhong
 * Time slider change Listener.
 *
 */
    private class TimeSliderListener implements ChangeListener {

        @Override
      public void stateChanged(final ChangeEvent theEvent) {
            if (myTimeSlider.isEnabled()) {
                final  int temp = myTimeSlider.getValue();
                myRaceModel.moveTo(temp);
            }
              
        } }



 
}
  
    

