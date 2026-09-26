//Tarun Pokra
//3381418
//July 4th, 2025
//Assignment 1, part 2
// this program basically lets you browse a file like midi (for audio), , mpeg-1, etc, and lets you play.
package A1Part2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.media.*;
import java.net.URL;

public class A1Part2App extends JFrame
{ 
    private Player mediaPlayer;
    private Component videoComponent;
    private Component controlComponent;
    private Timer positionTimer;
    private JSlider positionSlider;

    public A1Part2App()
    {
        setTitle("JMF Media Player for Part 2! ");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu bar here
        JMenuBar menuBar = new JMenuBar ();
        JMenu fileMenu = new JMenu("File ") ;
        JMenuItem openItem = new JMenuItem("open");
        JMenuItem closeItem = new JMenuItem("Close ");
        JMenuItem exitItem = new JMenuItem ("Exit!");

        openItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openMedia();
            }
        });

        closeItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                closeMedia();
            }
        });

        exitItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });

        fileMenu.add(openItem);
        fileMenu.add(closeItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Buttons panel
        JPanel controlPanel = new JPanel();
        JButton playBtn = new JButton("Play"); //play display to user button
        JButton pauseBtn = new JButton("Pause");//same thing
        JButton stopBtn = new JButton("Stop");//same thing, etc.

        playBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (mediaPlayer != null)
                {
                    mediaPlayer.start();
                } // show another box pop open if file cant be read.
            }
        });

        pauseBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (mediaPlayer !=null )
                {
                    mediaPlayer.stop() ;
                } // same thing for description as above.
            }
        });

        stopBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e )
            {
                if ( mediaPlayer != null)
                {
                    mediaPlayer.stop () ; // stop.
                    mediaPlayer.setMediaTime(new Time(0) ) ;
                }
            }
        });
        controlPanel.add (playBtn); // control buttons, etc.
        controlPanel.add( pauseBtn );
        controlPanel.add(stopBtn);
        add(controlPanel, BorderLayout.SOUTH ) ;
        // Position slider
        positionSlider =new JSlider();
        positionSlider.setEnabled(false);
        add (positionSlider, BorderLayout.NORTH );

        // this is  for a timer position slider update
        positionTimer= new Timer(500, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                updatePosition();
            }
        });
    }

    private void openMedia() // pick a file from your computer.
    {
        JFileChooser chooser = new JFileChooser ();
        int result= chooser.showOpenDialog(this ) ;
        if (result ==JFileChooser.APPROVE_OPTION) {
            File file =chooser.getSelectedFile();
            playFile(file);
        }
    }
    private void playFile(File file)
    {
        try
        {
            closeMedia(); // Close previous file if any
            URL mediaURL = file.toURI().toURL();
            mediaPlayer = Manager.createRealizedPlayer(mediaURL);

            if ((videoComponent = mediaPlayer.getVisualComponent()) != null)
            {
                add(videoComponent, BorderLayout.CENTER);
            }

            if ((controlComponent = mediaPlayer.getControlPanelComponent()) !=null)
            {
                add(controlComponent, BorderLayout.EAST);
            }

            mediaPlayer.addControllerListener(new ControllerListener()
            {
                public void controllerUpdate(ControllerEvent e)
                {
                    if (e instanceof EndOfMediaEvent)
                    {
                        mediaPlayer.stop();
                        mediaPlayer.setMediaTime(new Time(0));
                    }
                }
            });
            positionSlider.setEnabled(true);
            positionSlider.setMinimum(0);
            positionSlider.setMaximum((int ) mediaPlayer.getDuration().getSeconds() ) ;
            mediaPlayer.start();
            positionTimer.start();
            validate();
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "failed to open media file!: " + ex.getMessage(), "Error! ", JOptionPane.ERROR_MESSAGE);
        }
    } //display if the file does not open for whatever reason. 

    private void updatePosition()
    {
        if (mediaPlayer != null && mediaPlayer.getDuration().getSeconds() > 0)
        {
            int currentTime = (int) mediaPlayer.getMediaTime().getSeconds();
            positionSlider.setValue(currentTime);
        }
    }
    private void closeMedia()
    {
        if (mediaPlayer != null)
        {
            positionTimer.stop();
            mediaPlayer.stop();
            mediaPlayer.deallocate();
            mediaPlayer.close () ;
            if (videoComponent != null)
            {
                remove(videoComponent);
            }
            if (controlComponent != null)
            {
                remove(controlComponent);
            }

            videoComponent = null;
            controlComponent = null;
            mediaPlayer = null;

            positionSlider.setValue(0);
            positionSlider.setEnabled(false);
            validate(); // i changed this to validate from revalidate everywhere for the 2 errors
            // i kept getting. 
            repaint();
        }
    }
    public static void main(String[] args)
    {
        A1Part2App app = new A1Part2App();
        app.setVisible(true);
    }
}
// done. 