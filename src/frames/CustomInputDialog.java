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
    private String typedText = null;
    private JTextField textField;

    private JOptionPane optionPane;

    private final String btnString1 = "Enter";

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

    /** Creates the reusable dialog.
     * @param aFrame - owner frame
     * @param title - title for dialog
     * @param prompt - prompt for dialog
     * @param len - length of text input field */
    public CustomInputDialog(Frame aFrame, String prompt, String title, int len) {
        //Creates a dialog with the specified title, owner Frame and modality.
        super(aFrame, title, true);//modality will default to true for this class
        
        textField = new JTextField(len);

        //Create an array of the text and components to be displayed.

        Object[] array = {prompt, textField};

        //Create an array specifying the number of dialog buttons
        //and their text.
        //Object[] options = {btnString1, btnString2};

        //Create the JOptionPane.
        optionPane = new JOptionPane(array, JOptionPane.QUESTION_MESSAGE);

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
    }

    /** This method handles events for the text field.
     * @param e */
    @Override
    public void actionPerformed(ActionEvent e) {
        optionPane.setValue("OK");
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

            if ("OK".equals(value)) {
                    typedText = textField.getText();
                String ucText = typedText.toUpperCase();
                if (isAlpha(ucText)) {
                    //we're done; clear and dismiss the dialog
                    clearAndHide();
                } else {
                    //text was invalid
                    textField.selectAll();
                    JOptionPane.showMessageDialog(CustomInputDialog.this,
                                    "Sorry, \"" + typedText + "\" "
                                    + "isn't a valid response.\n"
                                    + "Please enter "
                                    + "your name.",
                                    "Try again",
                                    JOptionPane.ERROR_MESSAGE);
                    typedText = null;
                    textField.requestFocusInWindow();
                }
            } else { //user closed dialog or clicked cancel
                typedText = null;
                clearAndHide();
            }
        }
    }

    /** This method clears the dialog and hides it. */
    public void clearAndHide() {
        textField.setText(null);
        setVisible(false);
    }    
}