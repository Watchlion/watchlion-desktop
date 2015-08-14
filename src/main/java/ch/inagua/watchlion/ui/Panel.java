/**
 * 
 */
package ch.inagua.watchlion.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
import ch.inagua.watchlion.model.Environment;
import ch.inagua.watchlion.model.Watchlion;
import ch.inagua.watchlion.service.WatchlionLoader;

/**
 */
public class Panel extends JPanel /* implements ActionListener */{

	private static final long serialVersionUID = 1L;

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	
	private final Watchlion watchlion;

	private JTextField appIdTextField;

	// TODO PROPERTY 1
	private JTextField appNameTextField;

	private JTextField appBriefTextField;
	private JTextField appURLTextField;
	private JTextField appTypeTextField;
	private JTextField appCategoryTextField;
	private JTextField appUsernameTextField;
	private JTextField appSerialTextField;
	private JTextField appInstructionsTextField;
	private JTextField appInstallTextField;
	private JTextField appRequiredTextField;
	private JCheckBox appIgnoredCheckBox;

	private JTextField versionIdTextField;
	private JTextField versionNameTextField;
	private JTextField versionInstructionsTextField;
	private JTextField versionInstallTextField;
	private JCheckBox versionInstalledCheckBox;
	private JCheckBox versionIgnoredCheckBox;
	
	private JButton buttonInstalled;
	private JButton buttonIgnored;
	private JButton buttonUpdateApp;

	private JTree tree;

	private boolean isLocal = true;


	private JButton buttonUpdateVersion;


	private JButton buttonCreateVersion;


	private JTextArea infoTextArea;


	
	// TODO PROPERTY 9
	private TreeNode getRootNode() {
		final List<Application> flattenApps = isLocal ? watchlion.delta() : watchlion.getReference().getApplications();
		Map<String, List<Application>> appsByCategory = watchlion.getApplicationsByCategory(flattenApps);
		final String title = isLocal ? "[Local] Missing Versions" : "[Repository] All applications";

		Environment env = isLocal ? watchlion.getLocal() : watchlion.getReference();
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(title + " (" + flattenApps.size() + ")" + " [" + DATE_FORMAT.format(env.getUpdateDate()) + "]");
		for (String category : appsByCategory.keySet()) {
			final List<Application> apps = appsByCategory.get(category);
			
			DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category + " (" + apps.size() + ")");
			root.add(categoryNode);
			
			for (Application application : apps) {
				DefaultMutableTreeNode appNode = new DefaultMutableTreeNode(application.getLabelWithLastVersion());
				appNode.setUserObject(application);
				categoryNode.add(appNode);

				addNodeIfNotBlank(appNode, "Instructions: ", application.getInstructions());
				addNodeIfNotBlank(appNode, "Install: ", application.getInstall());
				addNodeIfNotBlank(appNode, "Username: ", application.getUsername());
				addNodeIfNotBlank(appNode, "Serial: ", application.getSerial());

				if (isLocal) {
					Application.Version version = application.getLastVersion();
					if (version != null && StringUtils.isNotBlank(version.getInstructions()) || StringUtils.isNotBlank(version.getInstall())) {
						DefaultMutableTreeNode versionsNode = new DefaultMutableTreeNode("Last Version: " + version.getName() + " [" + version.getId() + "]");
						appNode.add(versionsNode);
						
						addNodeIfNotBlank(versionsNode, "Instructions: ", version.getInstructions());
						addNodeIfNotBlank(versionsNode, "Install: ", version.getInstall());
					}
				} else {
					if (!application.getVersions().isEmpty()) {
						DefaultMutableTreeNode versionsNode = new DefaultMutableTreeNode("Versions");
						boolean displayVersions = application.getVersions().size() > 1;
						for (Application.Version version : application.getVersions()) {
							DefaultMutableTreeNode vNode = new DefaultMutableTreeNode(version.getName() + " [" + version.getId() + "]");
							versionsNode.add(vNode);
							
							displayVersions = addNodeIfNotBlank(vNode, "Instructions: ", version.getInstructions()) || displayVersions; 
							displayVersions = addNodeIfNotBlank(vNode, "Install: ", version.getInstall()) || displayVersions; 
						}
						if (displayVersions) {
							appNode.add(versionsNode);
						}
					}
				}
				
				if (!application.getPlugins().isEmpty()) {
					DefaultMutableTreeNode pluginsNode = new DefaultMutableTreeNode("Plugins");
					for (Application.Version plugin : application.getPlugins()) {
						DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(plugin.getName() /*+ " [" + plugin.getId() + "]"*/);
						pluginsNode.add(pNode);
						
						addNodeIfNotBlank(pNode, "Instructions: ", plugin.getInstructions());
						addNodeIfNotBlank(pNode, "Install: ", plugin.getInstall()); 
					}
					appNode.add(pluginsNode);
				}
			}
		}
		
		return root;
	}

	private boolean addNodeIfNotBlank(DefaultMutableTreeNode parentNode, String label, String text) {
		if (StringUtils.isNotBlank(text)) {
			parentNode.add(createNode(label, text));
			return true;
		}
		return false;
	}

	private DefaultMutableTreeNode createNode(String header, String body) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(header + "\n" + body);
		return node;
	}

	private boolean isApplicationNode(DefaultMutableTreeNode node) {
		return node != null && node.getUserObject() != null && node.getUserObject() instanceof Application;
	}

	protected Panel(final Watchlion watchlion) {
		super(new GridLayout(2, 0));
		this.watchlion = watchlion;

		JPanel topPanel = new JPanel(new GridLayout(0, 2));
		add(topPanel);

		JPanel listPanel = new JPanel(new BorderLayout());
		topPanel.add(listPanel);
		{
			{
				tree = new JTree(new DefaultMutableTreeNode("LABEL_GENERATE"));
				tree.setToolTipText("Selectionner une ligne et CTRL+C pour copier son contenu.");
				tree.setCellRenderer(new TreeCellRenderer(watchlion));
				tree.setModel(new DefaultTreeModel(getRootNode()));
				JScrollPane treeView = new JScrollPane(tree);
				listPanel.add(treeView, BorderLayout.CENTER);

				tree.addTreeSelectionListener(new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent e) {
						Application selectedApplication = getSelectedReferenceApplication();
						applicationSelected(selectedApplication);
						if (selectedApplication == null && tree.getLastSelectedPathComponent() != null) {
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
							infoTextArea.setText("" + (node.getUserObject() == null ? node : node.getUserObject()));
						} else {
							infoTextArea.setText("");
						}
					}
				});
			}
			{
				JPanel buttonsPanel = new JPanel(new FlowLayout());
				listPanel.add(buttonsPanel, BorderLayout.PAGE_END);

				final JButton buttonReference = new JButton("Repository");
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
				// buttonInstalled.setEnabled(false);
				buttonInstalled.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Application selectedApplication = getSelectedReferenceApplication();
						if (selectedApplication != null) {
							watchlion.setLastVersionInstalled(selectedApplication, true);
							reloadDataLater();
							JOptionPane.showMessageDialog(Panel.this, selectedApplication.getLabelWithLastVersion() + " installed. Need to save!", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				});

				buttonIgnored = new JButton("Ignored");
				buttonsPanel.add(buttonIgnored);
				// buttonIgnored.setEnabled(false);
				buttonIgnored.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Application selectedApplication = getSelectedReferenceApplication();
						if (selectedApplication != null) {
							watchlion.ignoreLastVersionForApplication(selectedApplication, true);
							reloadDataLater();
							JOptionPane.showMessageDialog(Panel.this, selectedApplication.getLabelWithLastVersion() + " ignored. Need to save!", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				});
			}
		}

		infoTextArea = new JTextArea();
		JScrollPane scroll = new JScrollPane(infoTextArea);
		topPanel.add(scroll);


		JPanel detailPanel = new JPanel(new BorderLayout());
		add(detailPanel);
		{
			JPanel appVersionPanel = new JPanel(new GridLayout(0, 3));
			detailPanel.add(appVersionPanel, BorderLayout.PAGE_START);
			{
				{
					JPanel applicationPanel = new JPanel(new GridLayout(0, 2));
					appVersionPanel.add(applicationPanel);
					
					applicationPanel.add(new JLabel(""));
					applicationPanel.add(new JLabel("APPLICATION:"));

					// TODO PROPERTY 2

					// formPanelRight = new JPanel(new GridLayout(0, 2));
					appIdTextField = createTextField(applicationPanel, "ID");
					appNameTextField = createTextField(applicationPanel, "Name");

					appBriefTextField = createTextField(applicationPanel, "Brief");
					// appURLTextField = createTextField(applicationPanel, "URL");
					{
						final JButton button = new JButton("URL (clic to open)");
						applicationPanel.add(button);
						appURLTextField = new JTextField("");
						applicationPanel.add(appURLTextField);
						addOpenURLListener(button, appURLTextField);
					}
					appTypeTextField = createTextField(applicationPanel, "Type");
					appCategoryTextField = createTextField(applicationPanel, "Category");
					appRequiredTextField = createTextField(applicationPanel, "Required");
				}
				{
					JPanel middlePanel = new JPanel(new GridLayout(0, 2));
					appVersionPanel.add(middlePanel);
					
					addBlankRowOn(middlePanel);
					// appUsernameTextField = createTextField(middlePanel, "Username");
					{
						final JButton button = new JButton("Username");
						middlePanel.add(button);
						appUsernameTextField = new JTextField("");
						middlePanel.add(appUsernameTextField);
						addCopyClipboardListener(button, appUsernameTextField);
					}
					// appSerialTextField = createTextField(middlePanel, "Serial");
					{
						final JButton button = new JButton("Serial");
						middlePanel.add(button);
						appSerialTextField = new JTextField("");
						middlePanel.add(appSerialTextField);
						addCopyClipboardListener(button, appSerialTextField);
					}
					appInstructionsTextField = createTextField(middlePanel, "Instructions");
					// appInstallTextField = createTextField(middlePanel, "Install");
					{
						final JButton button = new JButton("Install");
						middlePanel.add(button);
						appInstallTextField = new JTextField("");
						middlePanel.add(appInstallTextField);
						addOpenURLListener(button, appInstallTextField);
					}
					appIgnoredCheckBox = createCheckbox(middlePanel, "Ignored?");
					addBlankRowOn(middlePanel);
					addBlankRowOn(middlePanel);
				}
				{
					JPanel versionPanel = new JPanel(new GridLayout(0, 2));
					appVersionPanel.add(versionPanel);
					
					versionPanel.add(new JLabel(""));
					versionPanel.add(new JLabel("VERSION:"));
					versionIdTextField = createTextField(versionPanel, "Id");
					versionNameTextField = createTextField(versionPanel, "Name");
					versionInstructionsTextField = createTextField(versionPanel, "Instructions");
					//versionInstallTextField = createTextField(versionPanel, "Install");
					{
						final JButton button = new JButton("Install");
						versionPanel.add(button);
						versionInstallTextField = new JTextField("");
						versionPanel.add(versionInstallTextField);
						addOpenURLListener(button, versionInstallTextField);
					}
					versionInstalledCheckBox = createCheckbox(versionPanel, "Installed?");
					versionIgnoredCheckBox = createCheckbox(versionPanel, "Ignored?");

					buttonCreateVersion = new JButton("Create");
					versionPanel.add(buttonCreateVersion);
					buttonCreateVersion.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							createNewVersion();
							applicationSelected(null);
							reloadDataLater();
							JOptionPane.showMessageDialog(Panel.this, "New Version created. Need to save!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
						}
					});

					buttonUpdateVersion = new JButton("Update");
					versionPanel.add(buttonUpdateVersion);
					buttonUpdateVersion.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							updateVersion();
							applicationSelected(null);
							reloadDataLater();
							JOptionPane.showMessageDialog(Panel.this, "Version Updated. Need to save!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
						}
					});

				}
			}
			{
				JPanel buttonsPanel = new JPanel(new FlowLayout());
				detailPanel.add(buttonsPanel, BorderLayout.PAGE_END);

				JButton buttonClear = new JButton("Clear");
				buttonsPanel.add(buttonClear);
				buttonClear.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						clearFields();
					}
				});

				buttonUpdateApp = new JButton("Update App");
				buttonsPanel.add(buttonUpdateApp);
				//buttonUpdateApp.setEnabled(false);
				buttonUpdateApp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						updateSelectedApplication();
						applicationSelected(null);
						reloadDataLater();
					}
				});

				JButton buttonCreate = new JButton("Create All");
				buttonsPanel.add(buttonCreate);
				buttonCreate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						createNewApplication();
						createNewVersion();
						applicationSelected(null);
						reloadDataLater();
						JOptionPane.showMessageDialog(Panel.this, "New Application created. Need to save!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
					}
				});

				JButton buttonSave = new JButton("Save");
				buttonsPanel.add(buttonSave);
				buttonSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							new WatchlionLoader().save(watchlion);
							JOptionPane.showMessageDialog(Panel.this, "Data saved!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		}

		applicationSelected(null);
	}

	private void addCopyClipboardListener(final JButton button, final JTextField textField) {
		button.setToolTipText("Clic to copy the content in the clipboard");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection(textField.getText());
				Clipboard clipboard = Toolkit.getDefaultToolkit ().getSystemClipboard ();
				clipboard.setContents (stringSelection, null);
				// JOptionPane.showMessageDialog(Panel.this, "Not Yet Implemented!", "Information", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
	
	private void addOpenURLListener(final JButton button, final JTextField textField) {
		button.setToolTipText("Clic to open the link in the browser");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
		        	URI uri = URI.create(textField.getText());
		            try {
						desktop.browse(uri);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(Panel.this, e1.getLocalizedMessage(), "ALERT", JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					}
			    }
			}
		});
	}

	protected void toggleIsLocal() {
		isLocal = !isLocal;
		reloadData();
	}

	@SuppressWarnings("unused")
	private void addNotNetImplementedAlert(JButton buttonReference) {
		buttonReference.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notYetImplementedPopup();
			}
		});
	}

	private JTextField createTextField(JPanel panel, String label) {
		final JLabel jLabel = createLabel(label);
		panel.add(jLabel);
		final JTextField textField = new JTextField("");
		panel.add(textField);
		// addTextFieldChangeListener(textField);
		return textField;
	}

	private void addBlankRowOn(JPanel panel) {
		panel.add(new JLabel());
		panel.add(new JLabel());
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

	// TODO PROPERTY 3
	private void refreshWithApplication(Application app) {
		appIdTextField.setText(app == null ? "" : app.getId());
		appNameTextField.setText(app == null ? "" : app.getName());
		
		appBriefTextField.setText(app == null ? "" : app.getBrief());
		appURLTextField.setText(app == null ? "" : app.getURL());
		appTypeTextField.setText(app == null ? "" : app.getType());
		appCategoryTextField.setText(app == null ? "" : app.getCategory());
		appUsernameTextField.setText(app == null ? "" : app.getUsername());
		appSerialTextField.setText(app == null ? "" : app.getSerial());
		appInstructionsTextField.setText(app == null ? "" : app.getInstructions());
		appInstallTextField.setText(app == null ? "" : app.getInstall());
		appRequiredTextField.setText(app == null ? "" : app.getRequired());
		appIgnoredCheckBox.setSelected(app != null && app.isIgnored());
        
		Application.Version version = app == null ? null : app.getLastVersion();
		versionIdTextField.setText(version == null ? "" : version.getId());
		versionNameTextField.setText(version == null ? "" : version.getName());
		versionInstructionsTextField.setText(version == null ? "" : version.getInstructions());
		versionInstallTextField.setText(version == null ? "" : version.getInstall());
		
		versionInstalledCheckBox.setSelected(watchlion.isLastVersionInstalled(app));
		versionIgnoredCheckBox.setSelected(!isLocal && app != null && watchlion.isApplicationIgnored(app.getId()));
	}
	
	private void clearFields() {
		refreshWithApplication(null);
	}

	private void updateSelectedApplication() {
		Application selectedApplication = getSelectedReferenceApplication();
		if (selectedApplication != null) {
			updateApplication(selectedApplication);
			JOptionPane.showMessageDialog(Panel.this, "Current Application updated. Need to save!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(Panel.this, "Application update failed. Maybe you have to try Create for a new Application.", "WARNING", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void createNewApplication() {
		{
			Application application = new Application();
			updateApplication(application);
			watchlion.getLocal().addApplication(application);
		}
		{
			Application application = new Application();
			updateApplication(application);
			watchlion.getReference().addApplication(application);
		}
	}

	private void createNewVersion() {
		if (StringUtils.isNotEmpty(versionIdTextField.getText())) {
			Application selectedApplication = getSelectedReferenceApplication();
			if (selectedApplication != null) {
				{
					Application.Version version = new Application.Version();
					updateVersion(version);
					Application localApp = watchlion.getLocal().getApplicationForId(selectedApplication.getId());
					if (localApp == null) {
						localApp = selectedApplication.copyLocal();
						watchlion.getLocal().addApplication(localApp);
					}
					localApp.addVersion(version);
				}
				{
					Application.Version version = new Application.Version();
					updateVersion(version);
					watchlion.getReference().getApplicationForId(selectedApplication.getId()).addVersion(version);
				}
			}
		}
	}

	private void updateVersion() {
		Application selectedApplication = getSelectedReferenceApplication();
		if (selectedApplication != null) {
			Application.Version version = isLocal //
					? watchlion.getLocal().getApplicationForId(selectedApplication.getId()).getLastVersion() // 
					: watchlion.getReference().getApplicationForId(selectedApplication.getId()).getLastVersion();
			updateVersion(version);
		}
	}

	// TODO PROPERTY 7
	private void updateApplication(Application application) {
		application.setUpdated();
		
		application.setId(appIdTextField.getText());
		application.setName(appNameTextField.getText());

		application.setBrief(appBriefTextField.getText());
		application.setURL(appURLTextField.getText());
		application.setType(appTypeTextField.getText());
		application.setCategory(appCategoryTextField.getText());
		application.setUsername(appUsernameTextField.getText());
		application.setSerial(appSerialTextField.getText());
		application.setInstructions(appInstructionsTextField.getText());
		application.setInstall(appInstallTextField.getText());
		application.setRequired(appRequiredTextField.getText());
		application.setIgnored(appIgnoredCheckBox.isSelected());
	}

	private void updateVersion(Application.Version version) {
		if (!isLocal) {
			version.setId(versionIdTextField.getText());
			version.setName(versionNameTextField.getText());
			version.setInstructions(versionInstructionsTextField.getText());
			version.setInstall(versionInstallTextField.getText());
			
			Application selectedApplication = getSelectedReferenceApplication();
			watchlion.setLastVersionInstalled(selectedApplication, versionInstalledCheckBox.isSelected());
			watchlion.ignoreLastVersionForApplication(selectedApplication, versionIgnoredCheckBox.isSelected());
		}
		version.setInstall(versionInstallTextField.getText());
	}

	private void reloadData() {
		tree.setModel(null);
		tree.setModel(new DefaultTreeModel(getRootNode()));
	}

	private void reloadDataLater() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				reloadData();
			}
		});
	}

	private void applicationSelected(Application application) {
		buttonInstalled.setEnabled(false);
		buttonIgnored.setEnabled(false);
		
		buttonUpdateApp.setEnabled(application != null);
		buttonCreateVersion.setEnabled(application != null && !isLocal);
		buttonUpdateVersion.setEnabled(application != null);

		versionInstalledCheckBox.setEnabled(false);
		versionIgnoredCheckBox.setEnabled(false);
		
		versionIdTextField.setEnabled(!isLocal);
		versionNameTextField.setEnabled(!isLocal);
		versionInstructionsTextField.setEnabled(!isLocal);
		// versionInstallTextField.setEnabled(!isLocal);
		
		if (application != null) {
			refreshWithApplication(application);
			if (isLocal) {
				buttonInstalled.setEnabled(true);
				buttonIgnored.setEnabled(true);
			} else {
				versionInstalledCheckBox.setEnabled(true);
				versionIgnoredCheckBox.setEnabled(true);
			}
		} else {
			clearFields();
		}
		
	}
	
	private Application getSelectedReferenceApplication() {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (isApplicationNode(selectedNode)) {
			Application application = (Application) selectedNode.getUserObject();
			return watchlion.getReference().getApplicationForId(application.getId());
		}
		return null;
	}

	private void notYetImplementedPopup() {
		JOptionPane.showMessageDialog(this, "Not Yet Implemented!", "ALERT", JOptionPane.ERROR_MESSAGE);
	}

}