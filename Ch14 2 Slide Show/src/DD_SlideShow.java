
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Dolunay Dagci
 * CISS 111-360
 * Assignment: Ch14 2 Slide Show
 * Due: 3.10.2019
 * This program provides user to choose pictures via file chooser and determine delay time,
 * and performs a slide show with the pictures.
 */
public final class DD_SlideShow extends JFrame implements ActionListener {

    private int slideIndex = 0;

    //files array to hold files and their paths
    File files[];
    //timer
    Timer timer;
    private JPanel imagePanel, buttonPanel;
    private JLabel pictureLabel, timeDelayLabel;
    private JButton addPictureButton, startSlidesButton;
    private JTextField timeDelayText = new JTextField(5);
    private JTextField filesNumText;

    /**
     * constructor
     */
    DD_SlideShow() {
        setTitle("Dolunay's SlideShow");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        //build the option panel
        BuildPanel();
        setResizable(false);
        //add buttons Action Listeners
        addPictureButton.addActionListener(this);
        startSlidesButton.addActionListener(this);

        getContentPane().setBackground(Color.decode("#920000")); //background color

        setVisible(true);
    }

    public void BuildPanel() {
        imagePanel = new JPanel(); buttonPanel = new JPanel();
        pictureLabel = new JLabel();
        imagePanel.add(pictureLabel);
        addPictureButton = new JButton("Add Pictures");
        buttonPanel.add(addPictureButton);
        startSlidesButton = new JButton("Slideshow Begin!");
        timeDelayLabel = new JLabel("Time Delay:");
        filesNumText = new JTextField(3);
        filesNumText.setEditable(false);
        filesNumText.setToolTipText("Number of Files Selected"); //I've added this uneditable text field to see how many fields are chosen, and make sure that the files chosen
        //are in the array
        buttonPanel.add(startSlidesButton);
        buttonPanel.add(timeDelayLabel);
        buttonPanel.add(timeDelayText);
        buttonPanel.add(filesNumText);

        add(imagePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == addPictureButton) {
            //clear picture when add picture button is clicked
            //clears every time you add new pictures
            pictureLabel.setIcon(null);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);

            FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG, PNG, and GIF Images",
                    "jpg", "png", "gif");

            fileChooser.setFileFilter(filter); //filter for files

            while (true){ //if user chooses files less than 2, or more 10, option dialog appears until user chooses valid number of files.
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                files = fileChooser.getSelectedFiles();
                if (files.length < 2 || files.length > 10) {
                    JOptionPane.showMessageDialog(null, "You need to choose at least 2 files, or at most 10 files.");
                    fileChooser.showOpenDialog(null);
                    files = fileChooser.getSelectedFiles(); //get files selected into files array
                    filesNumText.setText(String.valueOf(files.length));
                }
                else{
                    files = fileChooser.getSelectedFiles();
                    filesNumText.setText(String.valueOf(files.length));
                    break;  }
                }
            }
            //if user clicks start slide button
        } else if (e.getSource() == startSlidesButton) {

            //start over slides
            if (files == null) {
                slideIndex = 0;
            } else if(slideIndex -1 == -1) {
                slideIndex = files.length;
            }

            try { //exception for invalid time delay input
                String delayInput = timeDelayText.getText();
                int delayInt = Integer.parseInt(delayInput);
                if (delayInt < 5 || delayInt > 30) JOptionPane.showMessageDialog(null, "Time Delay can not be more than 30 seconds, or less than 5 seconds.");
                else {timer = new Timer(((delayInt) * 1000), null); //convert seconds to milliseconds
                    timer.addActionListener(this);
                    timer.start(); }
            } catch (RuntimeException a){
                JOptionPane.showMessageDialog(null, "You did not enter the amount of time delay.", "Warning!", JOptionPane.WARNING_MESSAGE);
            }

            // if the timer is activated.
        } else if (e.getSource() == timer) {

            if (files == null) {
            } else if (slideIndex - 1 == -1) {
                slideIndex = files.length;
            } else {
                ImageIcon imageIcon = new ImageIcon(files[slideIndex - 1].getAbsolutePath());
                Image img = imageIcon.getImage();
                Image newImg = img.getScaledInstance(700, 500, java.awt.Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(newImg);
                pictureLabel.setIcon(imageIcon);

                slideIndex--;
                pack();
            }
        }
        }
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new DD_SlideShow();
    }
}