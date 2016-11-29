package view;
/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

//package components;
/*
 * This code is based on an example provided by Richard Stanford, 
 * a tutorial reader.
 */

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;
import javax.swing.tree.DefaultMutableTreeNode;

import com.predic8.soamodel.Difference;

import businesslogic.Caller;
import businesslogic.DifferenceTreeCellRenderer;
import businesslogic.DynamicTree;
import businesslogic.FileManager;
import businesslogic.Preference;
import businesslogic.WsdlOperations;
import model.ChieldDifference;
import model.ParentDifference;

@SuppressWarnings("serial")
public class Main extends JPanel implements ActionListener {
	// command
	private static String CHOOSE_FILE_WSDL_1 = "choose_file_wsdl_1";
	private static String CHOOSE_FILE_WSDL_2 = "choose_file_wsdl_2";
	private static String COMPARE_WSDL = "compare_wsdl";
	private static String SAVE_FILE = "save_file";
	private static String STOP_COMPARE = "stop_compare";
	private static String CLEAR_TREE = "clear_tree";
	private static String LINKEDIN = "linkedin";
	private static String GITHUB = "github";
	private static String WEBSITE = "website";

	// key
	private static String FILE_WSDL_1 = "file_wsdl_1";
	private static String FILE_WSDL_2 = "file_wsdl_2";
	private static String URL_WSDL_1 = "url_wsdl_1";
	private static String URL_WSDL_2 = "url_wsdl_2";

	private List<String> dumpDifference = new ArrayList<String>();
	private Preference preference;
	private LaunchProcess mProcess;

	private DynamicTree treePanel;

	private JProgressBar jProgressBar;
	private JFileChooser FileChooser;

	private JPanel urlPanel;

	private JPanel radioPanel;
	private JRadioButton rbFile;
	private JRadioButton rbUrl;

	private JButton btnPathWsdl1;
	private JTextField txtPathWsdl1;
	private JButton btnPathWsdl2;
	private JTextField txtPathWsdl2;

	private JLabel lblUrlWsdl1;
	private JTextField txtUrlWsdl1;
	private JLabel lblUrlWsdl2;
	private JTextField txtUrlWsdl2;

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread. @throws
	 */
	private static void createAndShowGUI() {
		try {
			// Create and set up the window.
			JFrame frame = new JFrame("WSDL COMPARATOR");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			// Create and set up the content pane.
			Main newContentPane = new Main();
			newContentPane.setOpaque(true); // content panes must be opaque
			frame.setContentPane(newContentPane);
					
			InputStream imgStream = Main.class.getResourceAsStream("logo.png");
			BufferedImage myImg = ImageIO.read(imgStream);			
			frame.setIconImage(myImg);
			
			// Display the window.
			frame.pack();
			frame.setVisible(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} catch (Exception e) {
			e.printStackTrace();
			// File file = new File("test.txt");
			// PrintStream ps = null;
			// try {
			// ps = new PrintStream(file);
			// } catch (FileNotFoundException e1) {
			// e1.printStackTrace();
			// }
			// e.printStackTrace(ps);
			// ps.close();
		}
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public Main() {
		super(new BorderLayout());

		preference = new Preference();

		// Create the components.
		treePanel = new DynamicTree(this);
		treePanel.setCellRenderer(new DifferenceTreeCellRenderer());
		FileChooser = new JFileChooser();

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		GridBagLayout gbl1 = new GridBagLayout();
		gbl1.rowWeights = new double[] { 1.0 };
		JPanel firstPanel = new JPanel(gbl1);
		GridBagConstraints constraints1 = new GridBagConstraints();
		constraints1.insets = new Insets(0, 0, 0, 5);
		constraints1.fill = GridBagConstraints.BOTH;
		constraints1.anchor = GridBagConstraints.NORTH;
		constraints1.gridy = 0;
		constraints1.weightx = 1.5;
		constraints1.gridx = 0;

		firstPanel.add(treePanel, constraints1);
		container.add(firstPanel);

		JSeparator separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 0, 5);
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 0;
		firstPanel.add(separator, gbc_separator);

		urlPanel = new JPanel(new GridLayout(18, 0));
		GridBagConstraints constraints2 = new GridBagConstraints();
		constraints2.gridy = 0;
		constraints2.weightx = 1.0;
		constraints2.fill = GridBagConstraints.BOTH;
		constraints2.anchor = GridBagConstraints.NORTHEAST;
		constraints2.gridx = 2;
		firstPanel.add(urlPanel, constraints2);

		radioPanel = new JPanel(new GridLayout(1, 2));
		urlPanel.add(radioPanel);

		rbFile = new JRadioButton("File");
		rbFile.setHorizontalAlignment(SwingConstants.CENTER);
		rbFile.setSelected(false);
		rbFile.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					rbFile.setSelected(true);
					rbUrl.setSelected(false);
					selectionFile();
				} else {
					if (!rbUrl.isSelected()) {
						rbFile.setSelected(true);
					}

				}
			}
		});
		radioPanel.add(rbFile);

		rbUrl = new JRadioButton("Url");
		rbUrl.setHorizontalAlignment(SwingConstants.CENTER);
		rbUrl.setSelected(true);
		rbUrl.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					rbFile.setSelected(false);
					rbUrl.setSelected(true);
					selectionUrl();
				} else {
					if (!rbFile.isSelected()) {
						rbUrl.setSelected(true);
					}
				}
			}
		});
		radioPanel.add(rbUrl);

		btnPathWsdl1 = new JButton("Select Wsdl 1 (Old Version)");
		urlPanel.add(btnPathWsdl1);
		btnPathWsdl1.setActionCommand(CHOOSE_FILE_WSDL_1);
		btnPathWsdl1.addActionListener(this);

		txtPathWsdl1 = new JTextField();
		urlPanel.add(txtPathWsdl1);
		txtPathWsdl1.setColumns(10);

		btnPathWsdl2 = new JButton("Select Wsdl 2 (New Version)");
		urlPanel.add(btnPathWsdl2);
		btnPathWsdl2.setActionCommand(CHOOSE_FILE_WSDL_2);
		btnPathWsdl2.addActionListener(this);

		txtPathWsdl2 = new JTextField();
		urlPanel.add(txtPathWsdl2);
		txtPathWsdl2.setColumns(10);

		if (preference != null) {
			String path1 = preference.getPreference(FILE_WSDL_1);
			if (path1 != null && !(path1).isEmpty()) {
				txtPathWsdl1.setText(path1);
			}

			String path2 = preference.getPreference(FILE_WSDL_2);
			if (path2 != null && !(path2).isEmpty()) {
				txtPathWsdl2.setText(path2);
			}
		}

		lblUrlWsdl1 = new JLabel("Url Wsdl 1 (Old Version)");
		urlPanel.add(lblUrlWsdl1);

		txtUrlWsdl1 = new JTextField();
		txtUrlWsdl1.setHorizontalAlignment(SwingConstants.LEFT);
		urlPanel.add(txtUrlWsdl1);
		txtUrlWsdl1.setColumns(10);

		lblUrlWsdl2 = new JLabel("Url Wsdl 2 (New Version)");
		urlPanel.add(lblUrlWsdl2);

		txtUrlWsdl2 = new JTextField();
		txtUrlWsdl2.setHorizontalAlignment(SwingConstants.LEFT);
		txtUrlWsdl2.setColumns(10);
		urlPanel.add(txtUrlWsdl2);

		if (preference != null) {
			String url1 = preference.getPreference(URL_WSDL_1);
			if (url1 != null && !(url1).isEmpty()) {
				txtUrlWsdl1.setText(url1);
			}

			String url2 = preference.getPreference(URL_WSDL_2);
			if (url2 != null && !(url2).isEmpty()) {
				txtUrlWsdl2.setText(url2);
			}
		}

		JButton btnCompareWsdl = new JButton("Compare Wsdl");
		urlPanel.add(btnCompareWsdl);
		btnCompareWsdl.setActionCommand(COMPARE_WSDL);
		btnCompareWsdl.addActionListener(this);

		actionTreePanel = new JPanel();
		actionTreePanel.setLayout(new GridLayout(1, 2));
		urlPanel.add(actionTreePanel);

		JButton btnClearTree = new JButton("Clear Tree");
		btnClearTree.setActionCommand(CLEAR_TREE);
		btnClearTree.addActionListener(this);
		actionTreePanel.add(btnClearTree);

		btnSaveDifference = new JButton("Save Difference");
		btnSaveDifference.setActionCommand(SAVE_FILE);
		btnSaveDifference.addActionListener(this);
		actionTreePanel.add(btnSaveDifference);

		lblInfoTree = new JLabel("* use right click on the node to manage the tree");
		lblInfoTree.setFont(new Font("Tahoma", Font.ITALIC, 9));
		urlPanel.add(lblInfoTree);

		jProgressBar = new JProgressBar(0, 100);
		jProgressBar.setIndeterminate(true);
		jProgressBar.setVisible(false);
		urlPanel.add(jProgressBar);

		btnStopCompare = new JButton("Stop Compare");
		btnStopCompare.setActionCommand(STOP_COMPARE);
		btnStopCompare.addActionListener(this);
		btnStopCompare.setVisible(false);
		urlPanel.add(btnStopCompare);

		infoPanel = new JPanel();
		container.add(infoPanel);
		infoPanel.setLayout(new GridLayout(3, 0));

		lblInfo = new JLabel("Powered by Luca Spedalieri");
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lblInfo);

		lblInfoLib = new JLabel("Libraries Provided By: http://www.membrane-soa.org ");
		lblInfoLib.setHorizontalAlignment(SwingConstants.CENTER);
		infoPanel.add(lblInfoLib);

		infoBtnPanel = new JPanel();
		infoPanel.add(infoBtnPanel);
		infoBtnPanel.setLayout(new GridLayout(1, 3));

		btnLinkedin = new JButton("LinkedIn");
		URL imageUrlLinkedin = this.getClass().getResource("linkedin.png");
		btnLinkedin.setIcon(new ImageIcon(imageUrlLinkedin));

		btnLinkedin.setOpaque(false);
		btnLinkedin.setContentAreaFilled(false);
		btnLinkedin.setBorderPainted(false);
		btnLinkedin.setActionCommand(LINKEDIN);
		btnLinkedin.addActionListener(this);
		infoBtnPanel.add(btnLinkedin);

		btnGitHub = new JButton("GitHub");
		URL imageUrlGitHub = this.getClass().getResource("github.png");
		btnGitHub.setIcon(new ImageIcon(imageUrlGitHub));
		btnGitHub.setOpaque(false);
		btnGitHub.setContentAreaFilled(false);
		btnGitHub.setBorderPainted(false);
		btnGitHub.setActionCommand(GITHUB);
		btnGitHub.addActionListener(this);
		infoBtnPanel.add(btnGitHub);

		btnWebSite = new JButton("WebSite");
		URL imageUrlWebSite = this.getClass().getResource("website.png");
		btnWebSite.setIcon(new ImageIcon(imageUrlWebSite));
		btnWebSite.setOpaque(false);
		btnWebSite.setContentAreaFilled(false);
		btnWebSite.setBorderPainted(false);
		btnWebSite.setActionCommand(WEBSITE);
		btnWebSite.addActionListener(this);
		infoBtnPanel.add(btnWebSite);

		add(container);
		selectionUrl();
	}

	int widthUrlPanel;
	int heightUrlPanel;
	int widthTreePanel;
	int heightTreePanel;
	private JPanel infoPanel;
	private JLabel lblInfo;
	private JPanel infoBtnPanel;
	private JButton btnLinkedin;
	private JButton btnGitHub;
	private JButton btnWebSite;
	private JPanel actionTreePanel;
	private JButton btnSaveDifference;
	private JLabel lblInfoLib;
	private JLabel lblInfoTree;
	private JButton btnStopCompare;

	@SuppressWarnings("static-access")
	public void showLoading() {
		widthUrlPanel = urlPanel.WIDTH;
		heightUrlPanel = urlPanel.HEIGHT;
		widthTreePanel = treePanel.WIDTH;
		heightTreePanel = treePanel.HEIGHT;
		jProgressBar.setVisible(true);
	}

	public void hideLoading() {
		jProgressBar.setVisible(false);
		urlPanel.setPreferredSize(new Dimension(widthUrlPanel, heightUrlPanel));
		treePanel.setPreferredSize(new Dimension(widthTreePanel, heightTreePanel));
	}

	public void actionPerformed(ActionEvent e) {
		try {
			String command = e.getActionCommand();

			if (CHOOSE_FILE_WSDL_1.equals(command)) {
				if (mProcess == null || (mProcess.getState() == StateValue.DONE)) {
					int returnVal = FileChooser.showOpenDialog(Main.this);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = FileChooser.getSelectedFile();
						String filePath = file.getPath();
						if (filePath != null && !filePath.isEmpty()) {
							if (filePath.contains(".wsdl")) {
								txtPathWsdl1.setText(filePath);
								preference.setPreference(FILE_WSDL_1, file.getPath());
							} else {
								JOptionPane.showMessageDialog(getRootPane(), "Select valid wsdl");
							}							
						}
					}
				}
			} else if (CHOOSE_FILE_WSDL_2.equals(command)) {
				if (mProcess == null || (mProcess.getState() == StateValue.DONE)) {
					int returnVal = FileChooser.showOpenDialog(Main.this);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = FileChooser.getSelectedFile();
						String filePath = file.getPath();
						if (filePath != null && !filePath.isEmpty()) {
							if (filePath.contains(".wsdl")) {
								txtPathWsdl1.setText(filePath);
								preference.setPreference(FILE_WSDL_2, file.getPath());
							} else {
								JOptionPane.showMessageDialog(getRootPane(), "Select valid wsdl");
							}
						}
					}
				}
			} else if (COMPARE_WSDL.equals(command)) {
				if (mProcess == null || (mProcess.getState() == StateValue.DONE)) {
					if (rbFile.isSelected()) {
						mProcess = new LaunchProcess(LaunchProcess.TYPE_FILE, txtPathWsdl1.getText(),
								txtPathWsdl2.getText());
					} else if (rbUrl.isSelected()) {
						preference.setPreference(URL_WSDL_1, txtUrlWsdl1.getText());
						preference.setPreference(URL_WSDL_2, txtUrlWsdl2.getText());
						mProcess = new LaunchProcess(LaunchProcess.TYPE_URL, txtUrlWsdl1.getText(),
								txtUrlWsdl2.getText());
					}
					mProcess.execute();
					System.out.println("execute COMPARE_WSDL");
				}
			} else if (SAVE_FILE.equals(command)) {
				if (mProcess == null || (mProcess.getState() == StateValue.DONE)) {
					if (dumpDifference != null && !dumpDifference.isEmpty()) {
						int returnVal = FileChooser.showSaveDialog(Main.this);

						if (returnVal == JFileChooser.APPROVE_OPTION) {
							String filePath = FileChooser.getSelectedFile().getPath();
							if (filePath != null && !filePath.isEmpty()) {
								if (!filePath.contains(".txt")) {
									if (filePath.contains(".")) {
										String[] splitResult = filePath.split("\\.");
										filePath = splitResult[0] + ".txt";
									} else {
										filePath += ".txt";
									}
								}
								FileManager fileManager = new FileManager();
								fileManager.writeFile(filePath, dumpDifference);
							}
						}
					} else {
						JOptionPane.showMessageDialog(getRootPane(), "Elements not found");
					}
				}
			} else if (STOP_COMPARE.equals(command)) {
				if (mProcess != null) {
					mProcess.stopProcess();
					mProcess = null;
				}
			} else if (CLEAR_TREE.equals(command)) {
				treePanel.clear();
				dumpDifference.clear();
			} else if (LINKEDIN.equals(command)) {
				openWebpage(new URL("https://it.linkedin.com/in/lucaspedalieri"));
			} else if (GITHUB.equals(command)) {
				openWebpage(new URL("https://github.com/lucaspedalieri"));
			} else if (WEBSITE.equals(command)) {
				openWebpage(new URL("https://lucaspedalieri.com"));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(getRootPane(), ex.getMessage());
		}
	}

	public void openWebpage(URL url) throws MalformedURLException, IOException, URISyntaxException {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			desktop.browse(url.toURI());
		}
	}

	private void selectionFile() {
		btnPathWsdl1.setEnabled(true);
		txtPathWsdl1.setEnabled(true);
		btnPathWsdl2.setEnabled(true);
		txtPathWsdl2.setEnabled(true);

		lblUrlWsdl1.setEnabled(false);
		lblUrlWsdl2.setEnabled(false);
		txtUrlWsdl1.setEnabled(false);
		txtUrlWsdl2.setEnabled(false);
	}

	private void selectionUrl() {
		btnPathWsdl1.setEnabled(false);
		txtPathWsdl1.setEnabled(false);
		btnPathWsdl2.setEnabled(false);
		txtPathWsdl2.setEnabled(false);

		lblUrlWsdl1.setEnabled(true);
		lblUrlWsdl2.setEnabled(true);
		txtUrlWsdl1.setEnabled(true);
		txtUrlWsdl2.setEnabled(true);
	}

	public void createTree(DefaultMutableTreeNode parent, List<Difference> lstDifferences) {
		for (Difference diff : lstDifferences) {
			if (parent == null) {
				parent = treePanel.getRootNode();
			}
			if (diff.getDiffs() != null && !diff.getDiffs().isEmpty()) {
				// There are children. Is Parent!
				ParentDifference parentDifference = new ParentDifference(diff.getDescription());
				DefaultMutableTreeNode node = treePanel.addObject(parent, parentDifference);
				createTree(node, diff.getDiffs());
			} else {
				// Is chield!
				ChieldDifference chieldDifference = new ChieldDifference(diff.getDescription());
				treePanel.addObject(parent, chieldDifference);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private class LaunchProcess extends SwingWorker<Object, Void> {

		private static final int TYPE_FILE = 0;
		private static final int TYPE_URL = 1;

		private final int TypeOperation;
		private String Wsdl1;
		private String Wsdl2;
		private WsdlOperations WsdlOperations;

		public LaunchProcess(int typeOperation, String wsdl1, String wsdl2) {
			TypeOperation = typeOperation;
			Wsdl1 = wsdl1;
			Wsdl2 = wsdl2;
			WsdlOperations = new WsdlOperations();
		}

		public void stopProcess() {
			WsdlOperations.stopThread();
			WsdlOperations = null;
			cancel(true);
		}

		@SuppressWarnings("rawtypes")
		@Override
		protected void process(List chunks) {
			super.process(chunks);
		}

		protected Object doInBackground() {
			Object result = null;
			try {
				showLoading();
				rbFile.setEnabled(false);
				rbUrl.setEnabled(false);
				btnStopCompare.setVisible(true);
				treePanel.clear();
				dumpDifference.clear();

				List<Difference> differences = null;
				switch (TypeOperation) {
				case TYPE_FILE:
					differences = WsdlOperations.compareWsdl(Wsdl1, Wsdl2);
					break;
				case TYPE_URL:
					InputStream streamWsdl1 = Caller.readGetRequest(Wsdl1);
					System.out.println("Downalod first wsdl");
					InputStream streamWsdl2 = Caller.readGetRequest(Wsdl2);
					System.out.println("Downalod second wsdl");
					differences = WsdlOperations.compareWsdl(streamWsdl1, streamWsdl2);
					break;
				}

				if (differences != null && !differences.isEmpty()) {
					for (Difference diff : differences) {						
						dumpDifference.add(diff.dump());
						DefaultMutableTreeNode node = treePanel.addObject(null, diff.getDescription());
						createTree(node, diff.getDiffs());
						btnStopCompare.setVisible(false);
					}
					result = true;
				} else {
					result = false;
				}
			} catch (Exception e) {
				result = e;
			}
			return result;
		}

		@Override
		protected void done() {
			super.done();
			try {
				hideLoading();
				treePanel.expandAllRootTree();
				btnStopCompare.setVisible(false);
				rbFile.setEnabled(true);
				rbUrl.setEnabled(true);
				Object result = get();
				if (result instanceof Boolean) {
					if ((boolean) result) {
						JOptionPane.showMessageDialog(getRootPane(), "Operation performed successfully");
					} else {
						JOptionPane.showMessageDialog(getRootPane(), "Differences not found");
					}
				} else if (result instanceof Exception) {
					System.out.println(((Exception) result).getMessage());
					JOptionPane.showMessageDialog(getRootPane(), ((Exception) result).getMessage());
				}
			} catch (Exception e) {
				if (!(e instanceof CancellationException)) {
					JOptionPane.showMessageDialog(getRootPane(), e.getMessage());
				}
			}
		}
	}
}