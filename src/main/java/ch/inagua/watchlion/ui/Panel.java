/**
 * 
 */
package ch.inagua.watchlion.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.lang.StringUtils;

import ch.inagua.watchlion.model.Application;
import ch.inagua.watchlion.model.Application.Version;
import ch.inagua.watchlion.model.Watchlion;
import ch.inagua.watchlion.service.WatchlionLoader;

/**
 */
public class Panel extends JPanel /* implements ActionListener */{

	private final Watchlion watchlion;


	private boolean updatingFieldsWithPreferences = false;

	private JTextField appIdTextField;

	// TODO PROPERTY 1
	private JTextField appNameTextField;

	private JTextField appBriefTextField;
	private JTextField appURLTextField;
	private JTextField appTypeTextField;
	private JTextField appCategoryTextField;
	private JTextField appUsernameTextField;
	private JTextField appInstructionsTextField;
	private JTextField appInstallTextField;
	private JTextField appRequiredTextField;

	private JTextField versionIdTextField;
	private JTextField versionNameTextField;
	private JTextField versionInstructionsTextField;
	private JTextField versionInstallTextField;
	private JCheckBox versionInstalledCheckBox;
	
	private JButton buttonInstalled;

	private JTree tree;

	private boolean isLocal = true;
	
	private TreeNode getRootNode() {
		final List<Application> apps = isLocal ? watchlion.delta() : watchlion.getReference().getApplications();
		final String title = isLocal ? "[Local] Missing Versions" : "[Repository] All applications";

		DefaultMutableTreeNode root = new DefaultMutableTreeNode(title + " (" + apps.size() + ")");
		for (Application application : apps) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(application.getLabelWithLastVersion());
			node.setUserObject(application);
			root.add(node);

			if (StringUtils.isNotBlank(application.getInstructions())) {
				node.add(new DefaultMutableTreeNode("Instructions: " + application.getInstructions()));
			}
		}
		return root;
	}

	private boolean isApplicationNode(DefaultMutableTreeNode node) {
		return node != null && node.getUserObject() != null && node.getUserObject() instanceof Application;
	}

	protected Panel(final Watchlion watchlion) {
		super(new GridLayout(2, 0));
		this.watchlion = watchlion;

		JPanel listPanel = new JPanel(new BorderLayout());
		add(listPanel);
		{
			{
				tree = new JTree(new DefaultMutableTreeNode("LABEL_GENERATE"));
				tree.setToolTipText("Selectionner une ligne et CTRL+C pour copier son contenu.");
				tree.setCellRenderer(new TreeCellRenderer());
				tree.setModel(new DefaultTreeModel(getRootNode()));
				JScrollPane treeView = new JScrollPane(tree);
				listPanel.add(treeView, BorderLayout.CENTER);

				tree.addTreeSelectionListener(new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent e) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

						buttonInstalled.setEnabled(false);
						if (isApplicationNode(node)) {
							Application app = (Application) node.getUserObject();
							updateWithApplication(app);
						}
						if (isLocal) {
							buttonInstalled.setEnabled(true);
						}
					}
				});
			}
			{
				JPanel buttonsPanel = new JPanel(new FlowLayout());
				listPanel.add(buttonsPanel, BorderLayout.PAGE_END);

				final JButton buttonReference = new JButton("Repository");
				// addNotNetImplementedAlert(buttonReference);
				buttonsPanel.add(buttonReference);
				buttonReference.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						toggleIsLocal();
						buttonReference.setText(isLocal ? "Repository" : "Local");
						buttonInstalled.setEnabled(false);
					}
				});

				buttonInstalled = new JButton("Installed");
				buttonsPanel.add(buttonInstalled);
				buttonInstalled.setEnabled(false);
				buttonInstalled.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
						if (isApplicationNode(selectedNode)) {
							Application app = (Application) selectedNode.getUserObject();
							// app.getLastVersion().setInstalled(true);
							watchlion.setApplicationInstalled(app);
							
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									reloadData();
								}
							});
							JOptionPane.showMessageDialog(Panel.this, app.getLabelWithLastVersion() + " installed.", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
							// JOptionPane.showMessageDialog(Panel.this, e1.getLocalizedMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					}
				});

				JButton buttonIgnored = new JButton("Ignored");
				buttonsPanel.add(buttonIgnored);
				addNotNetImplementedAlert(buttonIgnored);
				buttonIgnored.setEnabled(false);
			}
		}

		JPanel detailPanel = new JPanel(new BorderLayout());
		add(detailPanel);
		{
			JPanel appVersionPanel = new JPanel(new GridLayout(0, 2));
			detailPanel.add(appVersionPanel, BorderLayout.PAGE_START);
			{
				{
					JPanel applicationPanel = new JPanel(new GridLayout(0, 2));
					appVersionPanel.add(applicationPanel);
					
					applicationPanel.add(new JLabel("Application:"));
					applicationPanel.add(new JLabel(""));

					// TODO PROPERTY 2

					// formPanelRight = new JPanel(new GridLayout(0, 2));
					appIdTextField = createTextField(applicationPanel, "ID");
					appNameTextField = createTextField(applicationPanel, "Name");

					appBriefTextField = createTextField(applicationPanel, "Brief");
					appURLTextField = createTextField(applicationPanel, "URL");
					appTypeTextField = createTextField(applicationPanel, "Type");
					appCategoryTextField = createTextField(applicationPanel, "Category");
					appUsernameTextField = createTextField(applicationPanel, "Username");
					appInstructionsTextField = createTextField(applicationPanel, "Instructions");
					appInstallTextField = createTextField(applicationPanel, "Install");
					appRequiredTextField = createTextField(applicationPanel, "Required");
				}
				{
					JPanel versionPanel = new JPanel(new GridLayout(0, 2));
					appVersionPanel.add(versionPanel);
					
					versionPanel.add(new JLabel("New Version:"));
					versionPanel.add(new JLabel(""));
					versionIdTextField = createTextField(versionPanel, "Id");
					versionNameTextField = createTextField(versionPanel, "Name");
					versionInstructionsTextField = createTextField(versionPanel, "Instructions");
					versionInstallTextField = createTextField(versionPanel, "Install");
					versionInstalledCheckBox = createCheckbox(versionPanel, "Installed?");
					versionInstalledCheckBox.setEnabled(false);
				}
			}
			{
				JPanel buttonsPanel = new JPanel(new FlowLayout());
				detailPanel.add(buttonsPanel, BorderLayout.PAGE_END);

				JButton buttonUpdate = new JButton("Update");
				buttonsPanel.add(buttonUpdate);
				addNotNetImplementedAlert(buttonUpdate);
				buttonUpdate.setEnabled(false);

				JButton buttonCreate = new JButton("Create");
				buttonsPanel.add(buttonCreate);
				addNotNetImplementedAlert(buttonCreate);
				buttonCreate.setEnabled(false);

				JButton buttonSave = new JButton("Save");
				buttonsPanel.add(buttonSave);
				buttonSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							new WatchlionLoader().save(watchlion);
							JOptionPane.showMessageDialog(Panel.this, "Data saved!", "CONFIRMATION", JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		}

		// formPanel_ = new JPanel(new GridLayout(0, 2));
		// add(formPanel_, BorderLayout.PAGE_START);
		// formPanelLeft = new JPanel(new GridLayout(0, 2));
		// formPanel_.add(formPanelLeft);
		// formPanelRight = new JPanel(new GridLayout(0, 2));
		// formPanel_.add(formPanelRight);

		// // repoURLtextField = createField(formPanelLeft, "Repo URL");
		// // repoURLtextField.setToolTipText("WARNING: The shorter URL is, the closer to the root you are, the more the search last long");
		// // //
		// // repoURLtextField.setText("http://svn1/nova/facturation/trunk/facturation/");
		// repoURLComboBox = createComboBox(formPanelLeft, "Repo URL", properties.getSubversionRepositories().toArray());
		// repoURLComboBox.setToolTipText("WARNING: The shorter URL is, the closer to the root you are, the more the search last long");
		//
		// usernameTextField = createTextField(formPanelLeft, "Username");
		// passwordTextField = createPasswordField(formPanelLeft, "Password");
		//
		// // Scope to begin to search:
		// startRevisionTextField = createTextField(formPanelLeft, "Start SVN Revision ");
		// startRevisionTextField.setToolTipText("To reduce search scope");
		// startRevisionTextField.setText("");
		//
		// // Date startDate = createDate(1, Calendar.FEBRUARY, 2014);
		// startDateTextField = createTextField(formPanelLeft, "Start Date as jj/mm/aaaa ");
		// startDateTextField.setToolTipText("To reduce search scope. Overwrite startRevision");
		// // startDateTextField.setText("01/02/2014");
		//
		// generateButton = new JButton(LABEL_GENERATE);
		// formPanelLeft.add(new JLabel());
		// formPanelLeft.add(generateButton);
		// generateButton.addActionListener(createGenerateActionListener());
		//
		// {
		// qcOwnerTextField = createTextField(formPanelLeft, "QC Owner");
		// qcOwnerTextField.setToolTipText("SVN account who made commit (part of the name accepted, not case sensitive)");
		// // addTextFieldChangeListener(qcOwnerTextField);
		//
		// qcNumberTextField = createTextField(formPanelLeft, "QC Number");
		// qcNumberTextField.setToolTipText("Or some text contained in the commit message (not case sensitive)");
		// // addTextFieldChangeListener(qcNumberTextField);
		// }
		//
		// {
		// revisionsWithAuthorCheckBox = createCheckBox(formPanelRight, "Display Author? " + TAG_REVISIONS);
		// revisionsWithAuthorCheckBox.setSelected(true);
		// revisionsWithFilesCheckBox = createCheckBox(formPanelRight, "Display Paths? " + TAG_REVISIONS);
		// revisionsNumbersToFilterTextField = createTextField(formPanelRight, "Numbers to display " + TAG_REVISIONS);
		// revisionsNumbersToFilterTextField.setToolTipText("Revisions numbers to display, separated by Space ' ' or Comma ','");
		// // addTextFieldChangeListener(revisionsNumbersToFilterTextField);
		// }
		//
		// {
		// formPanelRight.add(createLabel("Display Changes? " + TAG_PATHS));
		//
		// revisionsDetailedRadioButton = new JRadioButton("Detail");
		// // revisionsDetailedRadioButton.setActionCommand(PATH_DETAILED_REVISIONS);
		// revisionsDetailedRadioButton.addActionListener(this);
		// revisionsAllRadioButton = new JRadioButton("All");
		// revisionsAllRadioButton.setSelected(true);
		// revisionsAllRadioButton.addActionListener(this);
		// revisionsLastRadioButton = new JRadioButton("Last");
		// revisionsLastRadioButton.addActionListener(this);
		// revisionsNoneRadioButton = new JRadioButton("None");
		// revisionsNoneRadioButton.addActionListener(this);
		//
		// ButtonGroup group = new ButtonGroup();
		// group.add(revisionsDetailedRadioButton);
		// group.add(revisionsAllRadioButton);
		// group.add(revisionsLastRadioButton);
		// group.add(revisionsNoneRadioButton);
		//
		// JPanel radioPanel = new JPanel(new GridLayout(1, 0));
		// radioPanel.add(revisionsDetailedRadioButton);
		// radioPanel.add(revisionsAllRadioButton);
		// radioPanel.add(revisionsLastRadioButton);
		// radioPanel.add(revisionsNoneRadioButton);
		// formPanelRight.add(radioPanel);
		// }
		//
		// {
		// pathFilterTextField = createTextField(formPanelRight, "Pattern filter " + TAG_PATHS);
		// pathFilterTextField.setToolTipText("Display only Paths containing this text (NOT case sensitive, use '"
		// + BooleanPatternMatching.AND + "' XOR '" + BooleanPatternMatching.OR + "' for 'and' XOR 'or' operator)");
		// // addTextFieldChangeListener(pathFilterTextField);
		// }
		//
		// {
		// // Postpone
		// fromReleaseTextField = createTextField(formPanelRight, "FROM Release " + TAG_POSTPONE);
		// fromReleaseTextField.setToolTipText("When to move QC from this 'Origin' Release to another. Ex: 2.3.1.0");
		// // addTextFieldChangeListener(fromReleaseTextField);
		//
		// toReleaseTextField = createTextField(formPanelRight, "TO Release " + TAG_POSTPONE);
		// toReleaseTextField.setToolTipText("When to move a QC from a Release to this 'Destination' Release. Ex: 2.3.0.5");
		// // addTextFieldChangeListener(toReleaseTextField);
		//
		// postponeWithFileCheckBox = createCheckBox(formPanelRight, "Display a file? " + TAG_POSTPONE);
		// postponeWithFileCheckBox
		// .setToolTipText("Display one file for each Revision you want to change comment (to find it with anoter tool)");
		// }
		//
		// {
		// reportTextArea = new JTextArea();
		// final JScrollPane scrollPane = new JScrollPane(reportTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		// JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//
		// final JTabbedPane tabbedPane = new JTabbedPane();
		// final ImageIcon icon = null; // createImageIcon("images/middle.gif");
		// tabbedPane.addTab("Text", icon, scrollPane, "Visualize as raw text");
		//
		// tree = new JTree(new DefaultMutableTreeNode(LABEL_GENERATE));
		// tree.setToolTipText("S�lectionner une ligne et CTRL+C pour copier son contenu.");
		// tree.setCellRenderer(new SVNTreeCellRenderer());
		// JScrollPane treeView = new JScrollPane(tree);
		// tabbedPane.addTab("Tree", icon, treeView, "Visualize as tree");
		//
		// add(tabbedPane, BorderLayout.CENTER);
		// }
		//
		// updateFieldsWith(properties);
	}

	protected void toggleIsLocal() {
		isLocal = !isLocal;
		reloadData();
	}

	private void addNotNetImplementedAlert(JButton buttonReference) {
		buttonReference.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notYetImplementedPopup();
			}
		});
	}

	// private ActionListener createGenerateActionListener() {
	// return new ActionListener() {
	//
	// public void actionPerformed(ActionEvent arg) {
	// reportTextArea.setText(LABEL_LOADING);
	// tree.setModel(null);
	// tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(LABEL_LOADING)));
	// generateButton.setText(LABEL_LOADING);
	// generateButton.setEnabled(false);
	//
	// final SVNRepo selectedRepo = getSelectedRepoAndStoreIfNew();
	//
	// new Thread() {
	// @Override
	// public void run() {
	// svnReport = new SVNReport();
	// try {
	// svnReport.setRepoUrl(selectedRepo.getUrl());
	// svnReport.setUsername(selectedRepo.getUsername());
	// svnReport.setPassword(selectedRepo.getPassword());
	//
	// svnReport.setStartRevision(startRevisionTextField.getText());
	// svnReport.setStartDate(startDateTextField.getText());
	// // svnReport.setQcNumber(qcNumberTextField.getText());
	// // svnReport.setQcOwner(qcOwnerTextField.getText());
	// svnReport.generate();
	// SwingUtilities.invokeLater(new Runnable() {
	// public void run() {
	// storeFieldsInProperties();
	// refreshTextArea();
	// generateButton.setText(LABEL_GENERATE);
	// generateButton.setEnabled(true);
	// }
	// });
	// } catch (final Throwable e) {
	// SwingUtilities.invokeLater(new Runnable() {
	// public void run() {
	// reportTextArea.setText(ExceptionUtils.getFullStackTrace(e));
	// generateButton.setText(LABEL_GENERATE);
	// generateButton.setEnabled(true);
	// }
	// });
	// }
	// }
	// }.start();
	// }
	// };
	// }
	//
	// protected SVNRepo getSelectedRepoAndStoreIfNew() {
	// SVNRepo repo = null;
	// final Object selectedItem = repoURLComboBox.getSelectedItem();
	// if (selectedItem instanceof String) {
	// final String url = (String) selectedItem;
	// repo = SVNRepo.getSVNRepoWithURL(svnProperties.getSubversionRepositories(), url);
	// if (repo != null) {
	// svnProperties.removeSubversionRepository(repo);
	// }
	// repo = new SVNRepo(url, usernameTextField.getText(), passwordTextField.getText());
	// svnProperties.addSubversionRepository(repo);
	//
	// svnProperties.sortSubversionRepositories();
	//
	// setRepos(svnProperties.getSubversionRepositories());
	// repoURLComboBox.setSelectedItem(repo);
	//
	// } else if (selectedItem instanceof SVNRepo) {
	// repo = (SVNRepo) selectedItem;
	// }
	// return repo;
	// }
	//
	// private JComboBox createComboBox(JPanel container, String label, Object[] models) {
	// final JComboBox comboBox = new JComboBox(models);
	// comboBox.setEditable(true);
	// comboBox.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	// if (usernameTextField != null && passwordTextField != null) {
	// final JComboBox comboBox = (JComboBox) e.getSource();
	// final Object selectedItem = comboBox.getSelectedItem();
	// if (selectedItem instanceof SVNRepo) {
	// final SVNRepo repo = (SVNRepo) selectedItem;
	// usernameTextField.setText(repo.getUsername());
	// passwordTextField.setText(repo.getPassword());
	// // } else {
	// // usernameTextField.setText("");
	// // passwordTextField.setText("");
	// }
	// }
	// }
	// });
	// // if (models.length > 0) {
	// // comboBox.setSelectedIndex(0);
	// // }
	// container.add(createLabel(label));
	// container.add(comboBox);
	// return comboBox;
	// }
	//
	// private void addTextFieldChangeListener(JTextField textField) {
	// textField.getDocument().addDocumentListener(new DocumentListener() {
	// public void removeUpdate(DocumentEvent e) {
	// storeFieldsInProperties();
	// refreshTextArea();
	// }
	//
	// public void insertUpdate(DocumentEvent e) {
	// storeFieldsInProperties();
	// refreshTextArea();
	// }
	//
	// public void changedUpdate(DocumentEvent e) {
	// System.err.println(">>>>> CHANGED!");
	// }
	// });
	// }
	//
	// private JCheckBox createCheckBox(JPanel panel, String label) {
	// panel.add(createLabel(label));
	// final JCheckBox checkBox = new JCheckBox();
	// checkBox.addActionListener(this);
	// panel.add(checkBox);
	// return checkBox;
	// }
	//
	// protected SVNReportType getReportType() {
	// if (revisionsDetailedRadioButton.isSelected())
	// return SVNReportType.DETAILED;
	// if (revisionsAllRadioButton.isSelected())
	// return SVNReportType.ALL;
	// if (revisionsLastRadioButton.isSelected())
	// return SVNReportType.LAST;
	// if (revisionsNoneRadioButton.isSelected())
	// return SVNReportType.NONE;
	// return null;
	// }
	//
	private JTextField createTextField(JPanel panel, String label) {
		final JLabel jLabel = createLabel(label);
		panel.add(jLabel);
		final JTextField textField = new JTextField("");
		panel.add(textField);
		// addTextFieldChangeListener(textField);
		return textField;
	}

	private JCheckBox createCheckbox(JPanel panel, String label) {
		final JLabel jLabel = createLabel(label);
		panel.add(jLabel);
		final JCheckBox checkBox = new JCheckBox();
		panel.add(checkBox);
		// addTextFieldChangeListener(textField);
		return checkBox;
	}

	// private JPasswordField createPasswordField(JPanel panel, String label) {
	// final JLabel jLabel = createLabel(label);
	// panel.add(jLabel);
	// final JPasswordField textField = new JPasswordField("");
	// panel.add(textField);
	// addTextFieldChangeListener(textField);
	// return textField;
	// }

	private JLabel createLabel(String label) {
		final JLabel jLabel = new JLabel(label + " : ", SwingConstants.RIGHT);
		// if (label.endsWith(TAG_REVISIONS)) {
		// jLabel.setForeground(Color.red);
		// jLabel.setOpaque(true);
		// }
		// if (label.contains(TAG_PATHS)) {
		// jLabel.setForeground(Color.blue);
		// jLabel.setOpaque(true);
		// }
		return jLabel;
	}

	// public void actionPerformed(ActionEvent e) {
	// refreshTextArea();
	// // http://www.java2s.com/Code/Java/Swing-Components/IconNodeTreeExample.htm
	// // javax.swing.plaf.metal.MetalIconFactory
	// }
	//
	// private void refreshTextArea() {
	// storeFieldsInProperties();
	// if (svnReport != null) {
	// svnReport.setQcNumber(qcNumberTextField.getText());
	// svnReport.setQcOwner(qcOwnerTextField.getText());
	// svnReport.setFromRelease(fromReleaseTextField.getText());
	// svnReport.setToRelease(toReleaseTextField.getText());
	// svnReport.setDisplayAFilePerRelease(postponeWithFileCheckBox.isSelected());
	// svnReport.setAuthorOnRevisions(revisionsWithAuthorCheckBox.isSelected());
	// svnReport.setPathsOnRevisions(revisionsWithFilesCheckBox.isSelected());
	// svnReport.setType(getReportType());
	// svnReport.setPathFilterPattern(new BooleanPatternMatching(pathFilterTextField.getText(), !CASE_SENSITIVE));
	// svnReport.setRevisionNumbersToDisplay(CollectionUtils.splitToSet(revisionsNumbersToFilterTextField.getText()));
	// reportTextArea.setText(svnReport.toString());
	//
	// tree.setModel(null);
	// tree.setModel(new DefaultTreeModel(svnReport.getReport().getRootNode()));
	// }
	// }
	//
	// private void storeFieldsInProperties() {
	// if (updatingFieldsWithPreferences) {
	// return;
	// }
	//
	// svnProperties.setSubversionStartRevision(startRevisionTextField.getText());
	// svnProperties.setSubversionStartDate(startDateTextField.getText());
	//
	// svnProperties.setQcOwner(qcOwnerTextField.getText());
	// svnProperties.setQcNumber(qcNumberTextField.getText());
	//
	// svnProperties.setReportRevisionsWithAuthor(revisionsWithAuthorCheckBox.isSelected());
	// svnProperties.setReportRevisionsWithPaths(revisionsWithFilesCheckBox.isSelected());
	// svnProperties.setReportRevisionsNumbers(revisionsNumbersToFilterTextField.getText());
	//
	// svnProperties.setReportPathsLevelDetail(revisionsDetailedRadioButton.isSelected());
	// svnProperties.setReportPathsLevelAll(revisionsAllRadioButton.isSelected());
	// svnProperties.setReportPathsLevelLast(revisionsLastRadioButton.isSelected());
	// svnProperties.setReportPathsLevelNone(revisionsNoneRadioButton.isSelected());
	// svnProperties.setReportPathsPatternFilter(pathFilterTextField.getText());
	//
	// svnProperties.setReportPostponeFromRelease(fromReleaseTextField.getText());
	// svnProperties.setReportPostponeToRelease(toReleaseTextField.getText());
	// svnProperties.setReportPostponeWithFile(postponeWithFileCheckBox.isSelected());
	//
	// svnProperties.saveToPreferences();
	// }
	//
	// private void updateFieldsWith(SVNProperties properties) {
	// updatingFieldsWithPreferences = true;
	//
	// setRepos(properties.getSubversionRepositories());
	// startRevisionTextField.setText(properties.getSubversionStartRevision());
	// startDateTextField.setText(properties.getSubversionStartDate());
	//
	// qcOwnerTextField.setText(properties.getQcOwner());
	// qcNumberTextField.setText(properties.getQcNumber());
	//
	// revisionsWithAuthorCheckBox.setSelected(properties.isReportRevisionsWithAuthor());
	// revisionsWithFilesCheckBox.setSelected(properties.isReportRevisionsWithPaths());
	// revisionsNumbersToFilterTextField.setText(properties.getReportRevisionsNumbers());
	//
	// revisionsDetailedRadioButton.setSelected(properties.isReportPathsLevelDetail());
	// revisionsAllRadioButton.setSelected(properties.isReportPathsLevelAll());
	// revisionsLastRadioButton.setSelected(properties.isReportPathsLevelLast());
	// revisionsNoneRadioButton.setSelected(properties.isReportPathsLevelNone());
	// pathFilterTextField.setText(properties.getReportPathsPatternFilter());
	//
	// fromReleaseTextField.setText(properties.getReportPostponeFromRelease());
	// toReleaseTextField.setText(properties.getReportPostponeToRelease());
	// postponeWithFileCheckBox.setSelected(properties.isReportPostponeWithFile());
	//
	// updatingFieldsWithPreferences = false;
	// }
	//
	// private void setRepos(List<SVNRepo> newRepos) {
	// if (newRepos != null && newRepos.size() > 0) {
	// if (newRepos != svnProperties.getSubversionRepositories()) {
	// svnProperties.setSubversionRepositories(newRepos);
	// }
	// repoURLComboBox.setModel(new DefaultComboBoxModel(newRepos.toArray()));
	// repoURLComboBox.setSelectedIndex(0);
	// }
	// }

	// TODO PROPERTY 3
	private void updateWithApplication(Application app) {
		appIdTextField.setText(app.getId());
		appNameTextField.setText(app.getName());
		
		appBriefTextField.setText(app.getBrief());
		appURLTextField.setText(app.getURL());
		appTypeTextField.setText(app.getType());
		appCategoryTextField.setText(app.getCategory());
		appUsernameTextField.setText(app.getUsername());
		appInstructionsTextField.setText(app.getInstructions());
		appInstallTextField.setText(app.getInstall());
		appRequiredTextField.setText(app.getRequired());
        
		Application.Version version = app.getLastVersion();
		versionIdTextField.setText(version.getId());
		versionNameTextField.setText(version.getName());
		versionInstructionsTextField.setText(app.getInstructions());
		versionInstallTextField.setText(version.getInstall());
		// versionInstalledCheckBox.setSelected();
	}

	private void reloadData() {
		tree.setModel(null);
		tree.setModel(new DefaultTreeModel(getRootNode()));
	}

	private void notYetImplementedPopup() {
		JOptionPane.showMessageDialog(this, "Not Yet Implemented!", "ALERT", JOptionPane.ERROR_MESSAGE);
	}
}
