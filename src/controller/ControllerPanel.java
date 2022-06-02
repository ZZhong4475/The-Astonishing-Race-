
package controller;

import application.Main;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
import javax.swing.SwingWorker;
import javax.swing.Timer;
import model.PropertyChangeEnabledRaceControls;
import view.Utilities;

/**
 * Comment me.
 * 
 * @author Charles Bryan
 * @author your name
 * @version Autumn 2019
 */
public class ControllerPanel extends JPanel {

    /** The serialization ID. */
    private static final long serialVersionUID = -6759410572845422202L;

    /** A reference to the backing Race Model. */
    private final PropertyChangeEnabledRaceControls myRace;

    /** Display of messages coming from the Race Model. */
    private final JTextArea myOutputArea;

    /** Panel to display CheckBoxs for each race Participant. */
    private final JPanel myParticipantPanel;

    /** A view on the race model  that displays the current race time. */
    private final JLabel myTimeLabel;

    /** A controller and view of the Race Model. */
    private final JSlider myTimeSlider;

    /** The list of javax.swing.Actions that make up the ToolBar (Controls) buttons. */
    private final List<Action> myControlActions;

    /** The timer that advances the Race Model. */
    private final Timer myTimer;

    /** Container to hold the different output areas. */
    private final JTabbedPane myTabbedPane;
        

    /**
     * Construct a ControllerPanel.
     * 
     * @param theRace the backing race model
     */
    public ControllerPanel(final PropertyChangeEnabledRaceControls theRace) {
        super();

        myOutputArea = new JTextArea(10, 50);
        myTimeLabel = new JLabel(Utilities.formatTime(0));

        myRace = theRace;
        myTimeSlider = new JSlider(0, 0, 0);
        myControlActions = new ArrayList<>();

        myTabbedPane = new JTabbedPane();
        myParticipantPanel = new JPanel();

        //TODO This component requires Event Handler(s) 
        myTimer = new Timer(0, null);

        buildActions();
        setUpComponents();
    }
    
    /**
     * Displays a simple JFrame.
     */
    private void setUpComponents() {
        setLayout(new BorderLayout());
        
        // JPanel is a useful container for organizing components inside a JFrame
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(buildSliderPanel(), BorderLayout.NORTH);

        myOutputArea.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(myOutputArea);
        scrollPane.
            setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        
        final JScrollPane participantScrollPane = new JScrollPane(myParticipantPanel);
        participantScrollPane.setPreferredSize(scrollPane.getSize());

        myTabbedPane.addTab("Data Output Stream", scrollPane);
        myTabbedPane.addTab("Race Participants", participantScrollPane);

        mainPanel.add(myTabbedPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        add(buildToolBar(), BorderLayout.SOUTH);

    }

    /**
     * Builds the panel with the time slider and time label.
     * 
     * @return the panel
     */
    private JPanel buildSliderPanel() {
        //TODO These components require Event Handlers
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 25, 5));
        
        myTimeSlider.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.add(myTimeSlider, BorderLayout.CENTER);

        myTimeLabel.setBorder(BorderFactory.
                              createCompoundBorder(BorderFactory.createEtchedBorder(),
                                              BorderFactory.createEmptyBorder(5, 5, 5, 5)));
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
       
        fileMenu.add(load);

        fileMenu.addSeparator();

        final JMenuItem exitItem = new JMenuItem("Exit");

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

        final JMenuItem infoItem = new JMenuItem("Race Info...");
        helpMenu.add(infoItem);

        final JMenuItem aboutItem = new JMenuItem("About...");
        helpMenu.add(aboutItem);
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
        myControlActions.add(new SimpleAction("Restart", "/ic_restart.png"));
        myControlActions.add(new SimpleAction("Play", "/ic_play.png"));
        myControlActions.add(new SimpleAction("Times One", "/ic_one_times.png"));
        myControlActions.add(new SimpleAction("Single Race", "/ic_repeat.png"));
        myControlActions.add(new SimpleAction("Clear", "/ic_clear.png"));
 
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

    /**
     * This is a simple implementation of an Action.
     * You will most likely not use this implementation in your final solution. Either
     * create your own Actions or alter this to suit the requirements for this assignment. 
     * 
     * @author Charles Bryan
     * @version Autumn 2019
     */
    private class SimpleAction extends AbstractAction {

        /** The serialization ID. */
        private static final long serialVersionUID = -3160383376683650991L;

        /**
         * Constructs a SimpleAction.
         * 
         * @param theText the text to display on this Action
         * @param theIcon the icon to display on this Action
         */
        SimpleAction(final String theText, final String theIcon) {
            super(theText);
            
            // small icons are usually assigned to the menu
            ImageIcon icon = (ImageIcon) new ImageIcon(getRes(theIcon));
            final Image smallImage = icon.getImage().
                            getScaledInstance(16, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);

            // Here is how to assign a larger icon to the tool bar.
            icon = (ImageIcon) new ImageIcon(getRes(theIcon));
            final Image largeImage = icon.getImage().
                            getScaledInstance(24, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);
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
            // TODO If you use this Action class, your behaviors go here. 
        
        }
    }
    
}
