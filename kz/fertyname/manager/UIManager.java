package kz.fertyname.manager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UIManager {
    private JFrame frame;
    private JComboBox<String> actionComboBox;
    private JTextField filePathField;
    private JFileChooser fileChooser;
    private FileManager fileEncryptor;

    public UIManager(FileManager fileEncryptor) {
        this.fileEncryptor = fileEncryptor;
        createUI();
    }

    private void createUI() {
        frame = new JFrame("Encrypt/Decrypt File");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        String[] options = {"Encrypt", "Decrypt"};
        actionComboBox = new JComboBox<>(options);
        frame.add(actionComboBox, BorderLayout.NORTH);

        filePathField = new JTextField();
        filePathField.setEditable(false);
        frame.add(filePathField, BorderLayout.CENTER);

        JButton chooseFileButton = new JButton("Choose File");
        JPanel panel = new JPanel();
        panel.add(chooseFileButton);
        frame.add(panel, BorderLayout.SOUTH);

        fileChooser = new JFileChooser();
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });

        frame.setVisible(true);
    }

    private void chooseFile() {
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());

            String action = (String) actionComboBox.getSelectedItem();
            String fileName = selectedFile.getName();
            String filePath = selectedFile.getParent();
            File outputFile = new File(filePath + "/" + (action.equals("Encrypt") ? "encrypt." : "decrypt.") + getFileExtension(fileName));

            try {
                if (action.equals("Encrypt")) {
                    fileEncryptor.encryptFile(selectedFile, outputFile);
                    JOptionPane.showMessageDialog(null, "File encrypted: " + outputFile.getPath());
                } else {
                    fileEncryptor.decryptFile(selectedFile, outputFile);
                    JOptionPane.showMessageDialog(null, "File decrypted: " + outputFile.getPath());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        return (lastIndexOf == -1) ? "" : fileName.substring(lastIndexOf + 1);
    }
}