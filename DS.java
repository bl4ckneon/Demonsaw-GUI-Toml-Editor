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

        buttonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onCancel();
            }
        });

        tAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addTransfer();
            }
        });
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

    public void fakemain() {
        try {
            saveToFile("demonsaw.toml", "");
            appendToFile("demonsaw.toml", "[demonsaw]\n" +
                    "\tversion = 1\n" +
                    "[[router]]\n" +
                    "\tport = " + port.getText() + "\n" +
                    "\tname = \"" + RName.getText() + "\"\n" +
                    "\taddress = \"" + ipAdd.getText() + "\"");

            if (transferR.isEmpty() == false) {
                int i = transferR.size();
                int j = 0;

                while (i > 0) {
                    appendToFile("demonsaw.toml", transferR.get(j));
                    i--;
                    j++;
                }
            }
            appendToFile("demonsaw.toml", "\t[router.network]");
            if (motd.getText().equals("")) {
                //Do Nothing
            } else {
                appendToFile("demonsaw.toml", "\t\tmotd = \"" + motd.getText() + "\"");
            }

            if (messageRouter.isSelected()) {
                appendToFile("demonsaw.toml", "\t\topen = true");
            } else {
                appendToFile("demonsaw.toml", "\t\topen = false");
            }

            if (rurl.getText().equals("")) {
                //Do Nothing
            } else {
                appendToFile("demonsaw.toml", "\t\tredirect = \"" + rurl.getText() + "\"");
            }

            if (messageRouter.isSelected()) {
                appendToFile("demonsaw.toml", "\t\tmessage = true");
            } else {
                appendToFile("demonsaw.toml", "\t\tmessage = false");
            }

            if (transferRouter.isSelected()) {
                appendToFile("demonsaw.toml", "\t\ttransfer = true");
            } else {
                appendToFile("demonsaw.toml", "\t\ttransfer = false");
            }

            if (groups.isEmpty() == false) {
                int i = groups.size();
                int j = 0;

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

    public void addTransfer() {
        transferR.add("\t[[router.transfer]]\n" +
                "\t\tport = " + tPort.getText() + "\n" +
                "\t\tname = \"" + tName.getText() + "\"\n" +
                "\t\tenabled = " + transferRouterEnabled() + "\n" +
                "\t\taddress = \"" + tIP.getText() + "\"");
    }

    public void addGroup() {
        try {


            String temp = "";

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
                throw new Exception();
            } else {
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
                throw new Exception();
            } else if (((Integer) iterations.getValue()) == 1) {
                //Do nothing
            } else {
                temp = temp + "\n\t\titerations = " + ((Integer) iterations.getValue());
            }

            if (salt.getText().equals("")) {
                //Nothing
            } else {
                temp = temp + "\n\t\tsalt = \"" + salt.getText() + "\"";
            }
            groups.add(temp);

        } catch (Exception e) {
            e.printStackTrace();
            debug.append("\nError happened when trying to add a group");
        }
    }


    public boolean transferRouterEnabled() {
        if (transferRouterEnabled.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean groupEnabled() {
        if (groupBox.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    private void onOK() {

        if (safe2save()) {
            fakemain();

        } else {

            debugPrinter("Please complete all of the * fields");
            System.out.println(safe2save());
        }

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        DS dialog = new DS();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }


    public void saveToFile(String strFileName, String strContent) throws Exception {
        try {
            outputFile = new PrintWriter(new BufferedWriter(new FileWriter(strFileName, false)));
            //Appends the content to the file
            outputFile.println(strContent);
            outputFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void appendToFile(String strFilename, String strContent) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(strFilename, true));
            bw.write(strContent);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void makeTOML() {


    }

    private void debugPrinter(String error) {
        debug.setText("");
        debug.append(error);
    }

    private boolean safe2save() {
        try {
            if (ipAdd.getText().equals("") || port.getText().equals("") || RName.getText().equals("") || (true!=messageRouter.isSelected() && true!=transferRouter.isSelected())) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            debugPrinter("ERROR :'(");
            return false;
        }
    }


}
