package com.orange.cryptotools.manager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UIManager {
	JFrame frame;
	JComboBox<String> actionComboBox;
	JTextField filePathField;
	JFileChooser fileChooser;
	FileManager fileEncryptor;

	public UIManager(final FileManager fileEncryptor) {
		this.fileEncryptor = fileEncryptor;
		frame = new JFrame("Encrypt/Decrypt File");
		actionComboBox = new JComboBox<>(new String[] { "Encrypt", "Decrypt" });
		filePathField = new JTextField();
		fileChooser = new JFileChooser();
		createUI();
	}

	private void createUI() {
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(actionComboBox, BorderLayout.NORTH);

		filePathField.setEditable(false);
		frame.add(filePathField, BorderLayout.CENTER);

		final var chooseFileButton = new JButton("Choose File");
		final var panel = new JPanel();
		panel.add(chooseFileButton);
		frame.add(panel, BorderLayout.SOUTH);

		chooseFileButton.addActionListener(this::chooseFile);

		frame.setVisible(true);
	}

	@SneakyThrows
	private void chooseFile(final ActionEvent e) {
		final var returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			final var selectedFile = fileChooser.getSelectedFile();
			filePathField.setText(selectedFile.getAbsolutePath());

			final var action = (String) actionComboBox.getSelectedItem();
			final var fileName = selectedFile.getName();
			final var filePath = selectedFile.getParent();
			final var outputFile = new File(
					filePath + "/" + (action.equals("Encrypt") ? "encrypt." : "decrypt.") + getFileExtension(fileName));
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

	private String getFileExtension(final String fileName) {
		return Optional.ofNullable(fileName).filter(f -> f.contains("."))
				.map(f -> f.substring(fileName.lastIndexOf('.') + 1)).orElse("");
	}
}