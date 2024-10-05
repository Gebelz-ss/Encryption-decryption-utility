package kz.fertyname;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import kz.fertyname.util.FuncUtil;
import kz.fertyname.util.Rsa;
import kz.fertyname.util.SymmetricCipher;


public class Main {
	    
	    public static void main(String[] args) throws Exception {
	 	    Rsa rsa = new Rsa();
		    FuncUtil funcutil = new FuncUtil();
		    SymmetricCipher suSymmetricCipher = new SymmetricCipher();
	        JFrame frame = new JFrame("Encrypt/Decrypt File");
	        frame.setSize(400, 200);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setLayout(new BorderLayout());

	        String[] options = {"Encrypt", "Decrypt"};
	        JComboBox<String> actionComboBox = new JComboBox<>(options);
	        frame.add(actionComboBox, BorderLayout.NORTH);

	        JButton chooseFileButton = new JButton("Choose File");
	        JTextField filePathField = new JTextField();
	        filePathField.setEditable(false);
	        frame.add(filePathField, BorderLayout.CENTER);
	        
	        JPanel panel = new JPanel();
	        panel.add(chooseFileButton);
	        frame.add(panel, BorderLayout.SOUTH);

	        JFileChooser fileChooser = new JFileChooser();
	        chooseFileButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                int returnValue = fileChooser.showOpenDialog(null);
	                if (returnValue == JFileChooser.APPROVE_OPTION) {
	                    File selectedFile = fileChooser.getSelectedFile();
	                    filePathField.setText(selectedFile.getAbsolutePath());
	                    
	                    String action = (String) actionComboBox.getSelectedItem();
	                    String fileName = selectedFile.getName();
	                    String filePath = selectedFile.getParent();
	                    File encryptedFile = new File(filePath + "/encrypt." + funcutil.getFileExtension(fileName));
	                    File decryptedFile = new File(filePath + "/decrypt." + funcutil.getFileExtension(fileName));

	                    try {
	                        if (action.equals("Encrypt")) {
	                            funcutil.encryptFile(selectedFile, encryptedFile, suSymmetricCipher, rsa);
	                            JOptionPane.showMessageDialog(null, "File encrypted: " + encryptedFile.getPath());
	                        } else {
	                            funcutil.decryptFile(selectedFile, decryptedFile, suSymmetricCipher, rsa);
	                            JOptionPane.showMessageDialog(null, "File decrypted: " + decryptedFile.getPath());
	                        }
	                    } catch (Exception ex) {
	                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
	                    }
	                }
	            }
	        });

	        frame.setVisible(true);
	    }
}

