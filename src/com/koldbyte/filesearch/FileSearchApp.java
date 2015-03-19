package com.koldbyte.filesearch;

import java.awt.EventQueue;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.koldbyte.filesearch.lib.RegexValidator;
import com.koldbyte.filesearch.lib.SearchContents;

public class FileSearchApp {

	private JFrame frmFindInFiles;
	private JTextField txtKey;
	private JTextField txtExtensions;
	private JTextField txtDir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileSearchApp window = new FileSearchApp();
					window.frmFindInFiles.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FileSearchApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFindInFiles = new JFrame();
		frmFindInFiles.setType(Type.UTILITY);
		frmFindInFiles.setTitle("Find in Files");
		frmFindInFiles.setResizable(false);
		frmFindInFiles.setBounds(100, 100, 450, 140);
		frmFindInFiles.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFindInFiles.getContentPane().setLayout(null);

		JLabel lblFindWhat = new JLabel("Find what");
		lblFindWhat.setBounds(10, 11, 88, 14);
		frmFindInFiles.getContentPane().add(lblFindWhat);

		txtKey = new JTextField();
		txtKey.setBounds(108, 8, 316, 20);
		frmFindInFiles.getContentPane().add(txtKey);
		txtKey.setColumns(10);

		JLabel lblExtensionFilter = new JLabel("Extension Filter");
		lblExtensionFilter.setBounds(10, 36, 88, 14);
		frmFindInFiles.getContentPane().add(lblExtensionFilter);

		txtExtensions = new JTextField();
		txtExtensions.setBounds(108, 33, 316, 20);
		frmFindInFiles.getContentPane().add(txtExtensions);
		txtExtensions.setColumns(10);

		JLabel lblDirectory = new JLabel("Directory");
		lblDirectory.setBounds(10, 61, 88, 14);
		frmFindInFiles.getContentPane().add(lblDirectory);

		final JCheckBox isCaseSensitive = new JCheckBox("Match Case");
		isCaseSensitive.setBounds(108, 85, 97, 23);
		frmFindInFiles.getContentPane().add(isCaseSensitive);

		txtDir = new JTextField();
		txtDir.setBounds(108, 58, 260, 20);
		frmFindInFiles.getContentPane().add(txtDir);
		txtDir.setColumns(10);

		JButton btnChangeDir = new JButton("...");
		btnChangeDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentDirectoryPath = txtDir.getText();
				JFileChooser dirChooser = new JFileChooser(currentDirectoryPath);
				dirChooser.setDialogTitle("Select Folder to Search in...");
				dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = dirChooser.showOpenDialog(frmFindInFiles);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = dirChooser.getSelectedFile();
					txtDir.setText(file.getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(frmFindInFiles,
							"Open command cancelled by user.");
				}
			}
		});
		btnChangeDir.setBounds(378, 57, 46, 23);
		frmFindInFiles.getContentPane().add(btnChangeDir);

		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String msg = "";
				Boolean noErrors = true;

				// Process Search key
				String key = txtKey.getText();
				if (key == null || key.isEmpty()) {
					msg += "•" + " Invalid/Empty Search Key."
							+ System.getProperty("line.separator");
					noErrors = false;
				}

				// Process Directory selection
				String dir = txtDir.getText();
				File f = new File(dir);
				if (f == null || !f.isDirectory()) {
					msg += "•" + " Invalid Directory selection."
							+ System.getProperty("line.separator");
					noErrors = false;
				}

				String extensions = txtExtensions.getText();
				if (extensions == null || extensions.isEmpty()) {
					msg += "•" + " Empty Extension filter."
							+ System.getProperty("line.separator");
					noErrors = false;
				} else if (RegexValidator.isValid(extensions) == false) {
					msg += "•" + " Invalid Extension filter."
							+ System.getProperty("line.separator");
					noErrors = false;
				}

				if (noErrors) {
					SearchResults results = new SearchResults();
					results.initTabs(new SearchContents(dir, key, 0,
							extensions, isCaseSensitive.isSelected()).search());
					try {
						results.setVisible(true);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(frmFindInFiles, msg);
				}

			}
		});
		btnSearch.setBounds(335, 85, 89, 23);
		frmFindInFiles.getContentPane().add(btnSearch);
	}
}
