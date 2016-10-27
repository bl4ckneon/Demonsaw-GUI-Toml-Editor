import javax.swing.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class DS extends javax.swing.JDialog {
    private javax.swing.JPanel contentPane;
    private javax.swing.JButton buttonOK;
    private javax.swing.JButton buttonCancel;
    private JTabbedPane tabbedPane1;
    private JTextField ipAdd;
    private JTextField rurl;
    private JTextField port;
    private JTextField motd;
    private JTextField RName;
    private JCheckBox transferRouter;
    private JCheckBox routerOpen;
    private JCheckBox messageRouter;
    private JTextField tIP;
    private JTextField tPort;
    private JCheckBox transferRouterEnabled;
    private JTextField tName;
    private JButton tAdd;
    private JTextField entropy;
    private JButton groupButton;
    private JCheckBox groupBox;
    private JComboBox keySize;
    private JComboBox hash;
    private JSpinner iterations;
    private JTextField entropyP;
    private JTextField salt;
    private JTextArea debug;
    private JComboBox cipher;
    private JTextPane githubHttpsGithubComTextPane;
    private PrintWriter outputFile;
    private String ipadd = "";
    private String portS = "";
    private String serverName = "";
    private String motdS = "";
    private String rurlS = "";
    private boolean transfer = false;
    private boolean message = false;
    ArrayList<String> transferR = new ArrayList<String>();
    ArrayList<String> groups = new ArrayList<String>();


    public DS() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //The save button
        buttonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onOK();
            }
        });

        //The cancel button
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCancel();
            }
        });

        //The transfer router add button
        tAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addTransfer();
            }
        });

        //The group add button
        groupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addGroup();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCancel();
            }
        }, javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    //The main function called once safe2save gives a true back
    public void fakemain() {
        try {
            //Clears the demonsaw.toml file
            saveToFile("demonsaw.toml", "");
            //Appends the basic things to the file
            appendToFile("demonsaw.toml", "[demonsaw]\n" +
                    "\tversion = 1\n" +
                    "[[router]]\n" +
                    "\tport = " + port.getText() + "\n" +
                    "\tname = \"" + RName.getText() + "\"\n" +
                    "\taddress = \"" + ipAdd.getText() + "\"");

            //Will add the transfer router(s) if there are some
            if (transferR.isEmpty() == false) {
                int i = transferR.size();
                int j = 0;

                while (i > 0) {
                    appendToFile("demonsaw.toml", transferR.get(j));
                    i--;
                    j++;
                }
            }

            //Will add router info if there is any
            appendToFile("demonsaw.toml", "\t[router.network]");

            //Appends the message of the day if there is one
            if (motd.getText().equals("")) {
                //Do Nothing
            } else {
                appendToFile("demonsaw.toml", "\t\tmotd = \"" + motd.getText() + "\"");
            }

            //If the message router check box is selected then it will append a true if not then a false
            //Fixed a bug in V 1.1
            if (routerOpen.isSelected()) {
                appendToFile("demonsaw.toml", "\t\topen = true");
            } else {
                appendToFile("demonsaw.toml", "\t\topen = false");
            }

            //If there is a redirect URL it will add it
            if (rurl.getText().equals("")) {
                //Do Nothing
            } else {
                appendToFile("demonsaw.toml", "\t\tredirect = \"" + rurl.getText() + "\"");
            }

            //If it is a message router then it will put true
            if (messageRouter.isSelected()) {
                appendToFile("demonsaw.toml", "\t\tmessage = true");
            } else {
                appendToFile("demonsaw.toml", "\t\tmessage = false");
            }

            //If it is a transfer router then it will put true
            if (transferRouter.isSelected()) {
                appendToFile("demonsaw.toml", "\t\ttransfer = true");
            } else {
                appendToFile("demonsaw.toml", "\t\ttransfer = false");
            }

            //If there are groups in the array list it loop and add them
            if (groups.isEmpty() == false) {
                int i = groups.size();
                int j = 0;

                //loops through from the number of objects in the arraylist
                while (i > 0) {
                    appendToFile("demonsaw.toml", groups.get(j));
                    i--;
                    j++;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Will add the transfer router to the arraylist
    public void addTransfer() {
        //Try baby try
        try {
            transferR.add("\t[[router.transfer]]\n" +
                    "\t\tport = " + tPort.getText() + "\n" +
                    "\t\tname = \"" + tName.getText() + "\"\n" +
                    "\t\tenabled = " + transferRouterEnabled() + "\n" +
                    "\t\taddress = \"" + tIP.getText() + "\"");
        } catch (Exception e){
            //Print those errors
            e.printStackTrace();
        }
    }

    //Makes a string and then adds the relevant info to it.
    public void addGroup() {
        try {

            //Makes a temp string that will be added to the arraylist
            String temp = "";

            //Will add the basic needed info for a group and checks to see if the enabled checkbox is enabled
            temp = "\t[[router.group]]\n" +
                    "\t\tenabled = " + groupEnabled();


            //Cipher
            if (cipher.getSelectedItem().equals("aes")) {
                //does nothing
            } else {
                temp = temp + "\n\t\tcipher = \"" + String.valueOf(cipher.getSelectedItem()) + "\"";
            }

            //Entropy %
            if (Double.valueOf(entropyP.getText()) > 100.00 || Double.valueOf(entropyP.getText()) < 0.00) {
                debugPrinter("Please enter a number between 100 and 1");
                //Will crash if there is an invalid number and give a debug statement
                throw new Exception();
            } else {
                //gets the number and turns it into a double then adds a 0
                temp = temp + "\n\t\tpercent = " + Double.valueOf(entropyP.getText()) + "0";
            }

            //entropy
            temp = temp + "\n\t\tentropy = \"" + entropy.getText() + "\"";


            //keySize
            if (keySize.getSelectedItem().equals("256")) {
                //Do nothing
            } else {
                temp = temp + "\n\t\tkey_size = " + keySize.getSelectedItem();
            }

            //interations
            if (((Integer) iterations.getValue()) < 1) {
                debugPrinter("Enter a number higher than 1");
                //Here comes dat Exception!!!
                throw new Exception();
            } else if (((Integer) iterations.getValue()) == 1) {
                //Do nothing
            } else {
                temp = temp + "\n\t\titerations = " + ((Integer) iterations.getValue());
            }

            //salt
            if (salt.getText().equals("")) {
                //Nothing
            } else {
                temp = temp + "\n\t\tsalt = \"" + salt.getText() + "\"";
            }
            //add the string temp to the arraylist groups that will be appended to the file in fakemain()
            groups.add(temp);

        } catch (Exception e) {
            e.printStackTrace();
            //More errurz
            debug.append("\nError happened when trying to add a group");
        }
    }

    //Checks to see if the transfer router check box is enabled
    public boolean transferRouterEnabled() {
        if (transferRouterEnabled.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    //Checks to see if the group check box is checked
    public boolean groupEnabled() {
        if (groupBox.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    //Function called on the save button
    private void onOK() {

        try {
            if (safe2save()) {
                //calls the "main" function
                fakemain();

            } else {
                //Prints to the debug
                debugPrinter("Please complete all of the * fields");

                //For debuging
                //System.out.println(safe2save());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //Closes the program
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    //Main call, standard
    public static void main(String[] args) {
        DS dialog = new DS();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }


    //Will save a string to a file and overwrite anything in it
    public void saveToFile(String strFileName, String strContent) throws Exception {
        try {
            //Makes a new PrintWriter
            outputFile = new PrintWriter(new BufferedWriter(new FileWriter(strFileName, false)));
            //Appends the content to the file
            outputFile.println(strContent);
            //closes the file
            outputFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Will append a String to a file
    public void appendToFile(String strFilename, String strContent) {
        try {
            //Makes a new Buffered Writer
            BufferedWriter bw = new BufferedWriter(new FileWriter(strFilename, true));
            //writes the file
            bw.write(strContent);
            //writes a new line
            bw.newLine();
            //flushes the text
            bw.flush();
            //closes the BufferedWriter
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Will print a String to the debug text area
    private void debugPrinter(String error) {
        try {
            //Erases the text from the debug text area
            debug.setText("");
            //Will print the String
            debug.append(error);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Will check to see if the fields * are filled in with something.
    private boolean safe2save() {
        try {
            //checks to see if the IP Field, Port Field, Router Name are empty. And sees if both the Message Router or Transfer Router check boxes are not checked.
            if (ipAdd.getText().equals("") || port.getText().equals("") || RName.getText().equals("") || (true != messageRouter.isSelected() && true != transferRouter.isSelected())) {
                //Ff any of them are empty then it will return a false since they HAVE to be filled in
                return false;
            } else {
                //If nothing getts triggered then it returns a true for being safe to save
                return true;
            }

            //catch all the errorz
        } catch (Exception e) {
            e.printStackTrace();
            debugPrinter("ERROR :'(");
            return false;
        }
    }


}
