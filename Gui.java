import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//The class which will handle all of the GUI
public class Gui {
    final int SCREEN_WIDTH = 300;
    final int SCREEN_HEIGHT = 500;

    private JLabel[] display;
    private static final Insets insets = new Insets(0, 0, 0, 0);
    private Backend backend;

    Gui(Backend backend){
        //JFrame is the main window. JPanels are divs inside the main window in which stuff is organised.
        //We need one JFrame and as many JPanels as we want, one JPanel can have many componants, one
        //JFrame can have many JPanels.
        this.backend = backend;

        JFrame frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        JPanel outer = new JPanel();
        outer.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1D, 1D, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
        outer.add(initiateScreen(), gbc);
        gbc = new GridBagConstraints(0, 1, 1, 1, 1D, 2D, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
        outer.add(initiateButtons(), gbc);
        frame.add(outer);
        frame.setVisible(true);
    }

    private JPanel initiateScreen(){
        //Adding the display
        JPanel displayPanel = new JPanel();
        GridLayout l = new GridLayout(2, 1);
        l.setHgap(0);
        displayPanel.setLayout(l);
        display = new JLabel[2];
        for (int x = 1; x >= 0; x--){
            display[x] = new JLabel("", SwingConstants.RIGHT);
            display[x].setBorder(new EmptyBorder(0, 20, 0, 10));
            display[x].setPreferredSize(new Dimension(SCREEN_WIDTH, 20));
            display[x].setFont(new Font("SansSerif", Font.PLAIN, 20));
            displayPanel.add(display[x]);
        }
        display[0].setFont(new Font("SansSerif", Font.BOLD, 35));
        return displayPanel;
    }

    private JPanel initiateButtons(){
        //We need a layout manager to help arrange stuff in our window.
        //Each JFrame and JPanel has a layout manager. 
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(6, 4));    
        JButton allButtons[] = new JButton[24];
        String[] buttonText = {"%", "\u221A", "x\u00B2", "1/x", "CE", "C", "<-", "/", "7", "8", "9", "x", "4", "5", "6", "-", "1", "2", "3", "+", "+-", "0", ".", "="};
        Font font = new Font("Comic Sans", Font.BOLD, 18);
        for (int x = 0; x < 24; x++){
            JButton tempbtn = new JButton(buttonText[x]);
            tempbtn.setFont(font);

            tempbtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    backend.handle(tempbtn.getText());
                    updateDisplay();
                }
            });
            allButtons[x] = tempbtn;
            buttons.add(allButtons[x]);
        }

        return buttons;
    }

    private void updateDisplay(){
        String[] data = backend.getDisplayInfo();
        for(int x = 0; x < 2; x++)
            display[x].setText(data[x]);
    }

    public static void main(String args[]){
        Gui cal = new Gui(new Backend());
        cal.updateDisplay();
     }   
}