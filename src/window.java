import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

@SuppressWarnings("serial")
public class window extends JFrame {
	private JTextField tfFilename = new JTextField("...");
	private JTextField tfFileDir = new JTextField("...");
	private JTextField tfStatus = new JTextField("...");
	private JTextField tfFileStatus = new JTextField();
	private JTextField tfKey = new JTextField("");
	private JLabel lName = new JLabel("  File Name: ");
	private JLabel lPatch = new JLabel("  Directory: ");
	private JLabel lStatus = new JLabel("  Stauts: ");
	private JLabel lKey = new JLabel("  16-length Key: ");

	private JLabel lnFileName = new JLabel("");

	private JButton encrypt = new JButton("Encrypt");
	private JButton decrypt = new JButton("Decrypt");

	private JLabel lPublicKey = new JLabel("");
	private JTextField tfPublicKey = new JTextField();

	private String fileName;
	private Container cP;

	private boolean s = false;

	public window() {
		super("XML encryptor-decrypter");
		initMenu();
		cP = getContentPane();

		JPanel pList = new JPanel();
		GridLayout gr = new GridLayout(0, 1, 0, 0);
		JPanel mainPane = new JPanel();
		mainPane.setPreferredSize(new Dimension(495, 240));
		mainPane.setLayout(gr);

		// buttony
		encrypt.addActionListener(new OpenFileE());
		pList.add(encrypt);

		decrypt.addActionListener(new OpenFileD());
		pList.add(decrypt);

		cP.add(pList, BorderLayout.SOUTH);

		// pola
		tfFileDir.setEditable(false);
		tfFileDir.setBorder(null);
		tfFilename.setEditable(false);
		tfFilename.setBorder(null);
		tfStatus.setEditable(false);
		tfFileStatus.setEditable(false);
		tfFileStatus.setBorder(null);
		tfStatus.setBorder(null);
		tfPublicKey.setEditable(true);
		JPanel pList2 = new JPanel(new GridLayout(8, 2, -159, 0));

		pList2.add(lName);
		pList2.add(tfFilename);
		pList2.add(lPatch);
		pList2.add(tfFileDir);
		pList2.add(lStatus);
		pList2.add(tfStatus);

		pList2.add(lnFileName);
		pList2.add(tfFileStatus);

		pList2.add(lKey);
		tfKey.setFont(new Font("Serif", Font.BOLD, 14));
		pList2.add(tfKey);
		pList2.add(lPublicKey);
		pList2.add(tfPublicKey);
		tfPublicKey.setText("if decrypting, insert the public key");

		mainPane.add(pList2);
		cP.add(mainPane, BorderLayout.CENTER);
	}

	private class OpenFileE implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (tfKey.getText().isEmpty() || (tfKey.getText().length() != 16)) {
				tfKey.setBorder(BorderFactory.createLineBorder(Color.RED));
			} else {
				tfKey.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				JFileChooser fC = new JFileChooser();
				fC.setFileFilter(new javax.swing.filechooser.FileFilter() {
					public boolean accept(File file) {
						return file.getName().toLowerCase().endsWith(".xml")
								|| file.isDirectory();
					}

					public String getDescription() {
						return "XML FILES";
					}
				});

				int option = fC.showOpenDialog(window.this);

				if (option == JFileChooser.APPROVE_OPTION) {

					tfFilename.setText(fC.getSelectedFile().getName());
					tfFileDir.setText(fC.getCurrentDirectory().toString());
					fileName = fC.getSelectedFile().getAbsolutePath();
					// System.out.println(tfKey.getText());
					try {

						// filePatch= filePatch.substring(2,
						// filePatch.length()).replace('\\', '/');
						String newFileName = fileName.replaceFirst(".xml",
								"Encrypted.xml");
						XmlOpenerEncrypter ed = new XmlOpenerEncrypter(
								tfKey.getText(), true);
						boolean state = ed.go(fileName, newFileName);

						if (state) {
							tfStatus.setText("Succesfully Encrypted   ");

							tfStatus.setFont(new Font("Arial", Font.BOLD, 15));
							lnFileName.setText("   New file name: ");
							tfFileStatus.setText(newFileName);
							lPublicKey.setText("   new Public Key: ");
							tfPublicKey.setText(ed.getPublicKey());

						} else {
							tfStatus.setText("Unsuccesful");
							tfStatus.setFont(new Font("Arial", Font.BOLD, 15));
							lnFileName.setText("");
							tfFileStatus.setText("");
							lPublicKey.setText("");
						}

					} catch (IOException e1) {
						tfStatus.setText("Unsuccesful" + e1);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						tfStatus.setText("Unsuccesful " + e1);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (TransformerException e1) {
						tfStatus.setText("Unsuccesful " + e1);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SAXParseException e1) {
						tfStatus.setText("Unsuccesful wrong file ");
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SAXException e1) {
						tfStatus.setText("Unsuccesful " + e1);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				if (option == JFileChooser.CANCEL_OPTION) {
					tfFilename.setText("");
					tfFileDir.setText("");
				}
			}
		}
	}

	private class OpenFileD implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (tfKey.getText().isEmpty() || (tfKey.getText().length() != 16)) {
				tfKey.setBorder(BorderFactory.createLineBorder(Color.RED));
			} else {
				tfKey.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				JFileChooser fC = new JFileChooser();
				fC.setFileFilter(new javax.swing.filechooser.FileFilter() {
					public boolean accept(File file) {
						return file.getName().toLowerCase().endsWith(".xml")
								|| file.isDirectory();
					}

					public String getDescription() {
						return "XML FILES";
					}
				});

				int option = fC.showOpenDialog(window.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					tfFilename.setText(fC.getSelectedFile().getName());
					tfFileDir.setText(fC.getCurrentDirectory().toString());

					fileName = fC.getSelectedFile().getAbsolutePath();
					// fileName = fC.getSelectedFile().getName();
					try {
						// filePatch= filePatch.substring(2,
						// filePatch.length()).replace('\\', '/');
						String newFileName = fileName.replaceFirst(
								"Encrypted.xml", "Decrypted.xml");
						XmlOpenerEncrypter ed = new XmlOpenerEncrypter(
								tfKey.getText(), false, tfPublicKey.getText());
						boolean state = ed.go(fileName, newFileName);

						if (state) {
							tfStatus.setText("Succesfully Decrypted   ");
							tfStatus.setFont(new Font("Arial", Font.BOLD, 15));
							lnFileName.setText("   New file name: ");
							tfFileStatus.setText(newFileName);

						}

						else {
							tfStatus.setText("Unsuccesfull probably wrong key ");
							tfStatus.setFont(new Font("Arial", Font.BOLD, 15));
							lnFileName.setText("");
							tfFileStatus.setText("");
						}

					} catch (IOException e1) {
						tfStatus.setText("Unsuccesful" + e1);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						tfStatus.setText("Unsuccesful " + e1);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (TransformerException e1) {
						tfStatus.setText("Unsuccesful " + e1);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SAXParseException e1) {
						tfStatus.setText("Unsuccesful wrong file or invalid data inside file");
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SAXException e1) {
						tfStatus.setText("Unsuccesful " + e1);
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				if (option == JFileChooser.CANCEL_OPTION) {
					tfFilename.setText("");
					tfFileDir.setText("");
				}
			}
		}

	}

	public void initMenu() {

		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("FILE");
		JMenuItem menuItem1 = new JMenuItem("New");
		JMenuItem menuItem2 = new JMenuItem("Exit");

		menuItem1.setToolTipText("Open new Instance, kinde of");
		menuItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				tfFilename.setText("...");
				tfFileDir.setText("...");
				tfStatus.setText("...");
				tfStatus.setFont(new Font("Arial", Font.PLAIN, 15));
				tfFileStatus.setText("");
				tfKey.setText("");

			}

		});
		menuItem2.setToolTipText("Exit application");
		menuItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}

		});
		file.add(menuItem1);
		file.add(menuItem2);
		menuBar.add(file);
		setJMenuBar(menuBar);
	}

}