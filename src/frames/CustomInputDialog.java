/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author delmarw
 */
public class CustomInputDialog extends JDialog implements ActionListener, PropertyChangeListener {
    private static final String BTN_STRING1 = "Enter";
    private static final String BTN_STRING2 = "Cancel";
    private String typedText = null;
    private JTextField textField;
    private JOptionPane optionPane;

    /**
     * Returns null if the typed string was invalid;
     * otherwise, returns the string as the user entered it.
     * @return 
     */
    public String getValidatedText() {
        return typedText;
    }
    
    /**
     * Returns true if all characters of the typed string are
     * alphabetical; otherwise, returns false.
     * @param text
     * @return 
     */
    private boolean isAlpha(String text){
        return text.matches("[a-zA-Z]+");
    }
    
    /**
     * Prepares and displays the dialog
     */
    private void prepare(){
        //Pack the dialog components
        pack();
        //Set dialog properties
        setResizable(false);
        //setMinimumSize(new Dimension(300,200));
        setLocationRelativeTo(getParent());
        //create the dialogue
        setVisible(true);
    }

    /** Creates the reusable dialog.
     * @param aFrame - owner frame
     * @param title - title for dialog
     * @param prompt - prompt for dialog
     * @param len - length of text input field */
    public CustomInputDialog(Frame aFrame, String prompt, String title, int len) {
        //Creates a dialog with the specified title, owner Frame and modality.
        super(aFrame, title, true);//modality will default to true for this class
        
        //Sets length of text field for user input
        textField = new JTextField(len);

        //Create an array of the text and components to be displayed.
        Object[] array = {prompt, textField};

        Object[] options = {BTN_STRING1, BTN_STRING2};
        
        //Create the JOptionPane.
        optionPane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options);

        //Make this dialog display it.
        setContentPane(optionPane);
        
        //Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
            /*
             * Instead of directly closing the window,
             * we're going to change the JOptionPane's
             * value property.
             */
                optionPane.setValue(JOptionPane.CLOSED_OPTION);
            }
        });

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent ce) {
                textField.requestFocusInWindow();
            }
        });

        //Register an event handler that puts the text into the option pane.
        textField.addActionListener(this);

        //Register an event handler that reacts to option pane state changes.
        optionPane.addPropertyChangeListener(this);
        
        //see prepare
        prepare();
    }

    /** This method handles events for the text field.
     * @param e */
    @Override
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue(optionPane.getValue());
    }

    /** This method reacts to state changes in the option pane.
     * @param e */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible()
         && (e.getSource() == optionPane)
         && (JOptionPane.VALUE_PROPERTY.equals(prop) ||
             JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object value = optionPane.getValue();

            if (value == JOptionPane.UNINITIALIZED_VALUE) {
                //ignore reset
                return;
            }

            //Reset the JOptionPane's value.
            //If you don't do this, then if the user
            //presses the same button next time, no
            //property change event will be fired.
            optionPane.setValue(
                    JOptionPane.UNINITIALIZED_VALUE);
            //Checks if button pressed
            if (BTN_STRING1.equals(value)) {
                typedText = textField.getText();
                String ucText = typedText.toUpperCase();
                if (isAlpha(ucText)) {
                    //we're done; clear and dismiss the dialog
                    dispose();
                } 
                else if (ucText.isEmpty()){
                    //textfield was empty
                    textField.selectAll();
                    JOptionPane.showMessageDialog(CustomInputDialog.this,
                                    "Looks like you didn't enter anything.\n"
                                    + "Please enter your name.",
                                    "Try again",
                                    JOptionPane.ERROR_MESSAGE);
                    typedText = null;
                    textField.requestFocusInWindow();
                }
                else {
                    //text was invalid
                    textField.selectAll();
                    JOptionPane.showMessageDialog(CustomInputDialog.this,
                                    "Sorry, \"" + typedText + "\" "
                                    + "isn't a valid response.\n"
                                    + "Please enter your name.",
                                    "Try again",
                                    JOptionPane.ERROR_MESSAGE);
                    typedText = null;
                    textField.requestFocusInWindow();
                }
            }
            else {
                typedText = null;
                this.dispose();
            }
        }
    }

    /** This method clears the dialog and disposes of it. */
    /*public void clearAndHide() {
        textField.setText(null);
        setVisible(false);
    }*/    
}
