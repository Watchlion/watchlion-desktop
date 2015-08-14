/**
 * 
 */
package ch.inagua.watchlion.ui;


/**
 */
public class SVNPanel /* extends JPanel implements ActionListener */ {

//	private static final String LABEL_LOADING = "Loading...";
//
//	private static final String LABEL_GENERATE = "Generate!";
//
//	private static final String TAG_REVISIONS = "[Revisions]";
//
//	private static final String TAG_PATHS = "[Paths]";
//
//	private static final String TAG_POSTPONE = "[Postpone]";
//
//	private static final long serialVersionUID = 1L;
//
//	private SVNReport svnReport;
//
//	// private final JTextField repoURLtextField;
//	private final JComboBox repoURLComboBox;
//
//	private final JTextField usernameTextField;
//
//	private final JTextField passwordTextField;
//
//	private final JTextField startRevisionTextField;
//
//	private final JTextField startDateTextField;
//
//	private final JTextField qcNumberTextField;
//
//	private final JTextField qcOwnerTextField;
//
//	private final JTextField fromReleaseTextField;
//
//	private final JTextField toReleaseTextField;
//
//	private final JTextField pathFilterTextField;
//
//	private final JPanel formPanel_;
//
//	private final JPanel formPanelLeft;
//
//	private final JPanel formPanelRight;
//
//	private final JTextArea reportTextArea;
//
//	private final JCheckBox revisionsWithFilesCheckBox;
//
//	private final JCheckBox revisionsWithAuthorCheckBox;
//
//	private final JTextField revisionsNumbersToFilterTextField;
//
//	private final JRadioButton revisionsDetailedRadioButton;
//
//	private final JRadioButton revisionsAllRadioButton;
//
//	private final JRadioButton revisionsLastRadioButton;
//
//	private final JRadioButton revisionsNoneRadioButton;
//
//	private final JCheckBox postponeWithFileCheckBox;
//
//	private final JTree tree;
//
//	private final JButton generateButton;
//
//	private final SVNProperties svnProperties;
//
//	private boolean updatingFieldsWithPreferences = false;
//
//	protected SVNPanel(SVNProperties properties) {
//		super(new BorderLayout());
//		this.svnProperties = properties;
//
//		formPanel_ = new JPanel(new GridLayout(0, 2));
//		add(formPanel_, BorderLayout.PAGE_START);
//		formPanelLeft = new JPanel(new GridLayout(0, 2));
//		formPanel_.add(formPanelLeft);
//		formPanelRight = new JPanel(new GridLayout(0, 2));
//		formPanel_.add(formPanelRight);
//
//		// repoURLtextField = createField(formPanelLeft, "Repo URL");
//		// repoURLtextField.setToolTipText("WARNING: The shorter URL is, the closer to the root you are, the more the search last long");
//		// //
//		// repoURLtextField.setText("http://svn1/nova/facturation/trunk/facturation/");
//		repoURLComboBox = createComboBox(formPanelLeft, "Repo URL", properties.getSubversionRepositories().toArray());
//		repoURLComboBox.setToolTipText("WARNING: The shorter URL is, the closer to the root you are, the more the search last long");
//
//		usernameTextField = createTextField(formPanelLeft, "Username");
//		passwordTextField = createPasswordField(formPanelLeft, "Password");
//
//		// Scope to begin to search:
//		startRevisionTextField = createTextField(formPanelLeft, "Start SVN Revision ");
//		startRevisionTextField.setToolTipText("To reduce search scope");
//		startRevisionTextField.setText("");
//
//		// Date startDate = createDate(1, Calendar.FEBRUARY, 2014);
//		startDateTextField = createTextField(formPanelLeft, "Start Date as jj/mm/aaaa ");
//		startDateTextField.setToolTipText("To reduce search scope. Overwrite startRevision");
//		// startDateTextField.setText("01/02/2014");
//
//		generateButton = new JButton(LABEL_GENERATE);
//		formPanelLeft.add(new JLabel());
//		formPanelLeft.add(generateButton);
//		generateButton.addActionListener(createGenerateActionListener());
//
//		{
//			qcOwnerTextField = createTextField(formPanelLeft, "QC Owner");
//			qcOwnerTextField.setToolTipText("SVN account who made commit (part of the name accepted, not case sensitive)");
//			// addTextFieldChangeListener(qcOwnerTextField);
//
//			qcNumberTextField = createTextField(formPanelLeft, "QC Number");
//			qcNumberTextField.setToolTipText("Or some text contained in the commit message (not case sensitive)");
//			// addTextFieldChangeListener(qcNumberTextField);
//		}
//
//		{
//			revisionsWithAuthorCheckBox = createCheckBox(formPanelRight, "Display Author? " + TAG_REVISIONS);
//			revisionsWithAuthorCheckBox.setSelected(true);
//			revisionsWithFilesCheckBox = createCheckBox(formPanelRight, "Display Paths? " + TAG_REVISIONS);
//			revisionsNumbersToFilterTextField = createTextField(formPanelRight, "Numbers to display " + TAG_REVISIONS);
//			revisionsNumbersToFilterTextField.setToolTipText("Revisions numbers to display, separated by Space ' ' or Comma ','");
//			// addTextFieldChangeListener(revisionsNumbersToFilterTextField);
//		}
//
//		{
//			formPanelRight.add(createLabel("Display Changes? " + TAG_PATHS));
//
//			revisionsDetailedRadioButton = new JRadioButton("Detail");
//			// revisionsDetailedRadioButton.setActionCommand(PATH_DETAILED_REVISIONS);
//			revisionsDetailedRadioButton.addActionListener(this);
//			revisionsAllRadioButton = new JRadioButton("All");
//			revisionsAllRadioButton.setSelected(true);
//			revisionsAllRadioButton.addActionListener(this);
//			revisionsLastRadioButton = new JRadioButton("Last");
//			revisionsLastRadioButton.addActionListener(this);
//			revisionsNoneRadioButton = new JRadioButton("None");
//			revisionsNoneRadioButton.addActionListener(this);
//
//			ButtonGroup group = new ButtonGroup();
//			group.add(revisionsDetailedRadioButton);
//			group.add(revisionsAllRadioButton);
//			group.add(revisionsLastRadioButton);
//			group.add(revisionsNoneRadioButton);
//
//			JPanel radioPanel = new JPanel(new GridLayout(1, 0));
//			radioPanel.add(revisionsDetailedRadioButton);
//			radioPanel.add(revisionsAllRadioButton);
//			radioPanel.add(revisionsLastRadioButton);
//			radioPanel.add(revisionsNoneRadioButton);
//			formPanelRight.add(radioPanel);
//		}
//
//		{
//			pathFilterTextField = createTextField(formPanelRight, "Pattern filter " + TAG_PATHS);
//			pathFilterTextField.setToolTipText("Display only Paths containing this text (NOT case sensitive, use '"
//					+ BooleanPatternMatching.AND + "' XOR '" + BooleanPatternMatching.OR + "' for 'and' XOR 'or' operator)");
//			// addTextFieldChangeListener(pathFilterTextField);
//		}
//
//		{
//			// Postpone
//			fromReleaseTextField = createTextField(formPanelRight, "FROM Release " + TAG_POSTPONE);
//			fromReleaseTextField.setToolTipText("When to move QC from this 'Origin' Release to another. Ex: 2.3.1.0");
//			// addTextFieldChangeListener(fromReleaseTextField);
//
//			toReleaseTextField = createTextField(formPanelRight, "TO Release " + TAG_POSTPONE);
//			toReleaseTextField.setToolTipText("When to move a QC from a Release to this 'Destination' Release. Ex: 2.3.0.5");
//			// addTextFieldChangeListener(toReleaseTextField);
//
//			postponeWithFileCheckBox = createCheckBox(formPanelRight, "Display a file? " + TAG_POSTPONE);
//			postponeWithFileCheckBox
//					.setToolTipText("Display one file for each Revision you want to change comment (to find it with anoter tool)");
//		}
//
//		{
//			reportTextArea = new JTextArea();
//			final JScrollPane scrollPane = new JScrollPane(reportTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
//					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//
//			final JTabbedPane tabbedPane = new JTabbedPane();
//			final ImageIcon icon = null; // createImageIcon("images/middle.gif");
//			tabbedPane.addTab("Text", icon, scrollPane, "Visualize as raw text");
//
//			tree = new JTree(new DefaultMutableTreeNode(LABEL_GENERATE));
//			tree.setToolTipText("Sélectionner une ligne et CTRL+C pour copier son contenu.");
//			tree.setCellRenderer(new SVNTreeCellRenderer());
//			JScrollPane treeView = new JScrollPane(tree);
//			tabbedPane.addTab("Tree", icon, treeView, "Visualize as tree");
//
//			add(tabbedPane, BorderLayout.CENTER);
//		}
//
//		updateFieldsWith(properties);
//	}
//
//	private ActionListener createGenerateActionListener() {
//		return new ActionListener() {
//
//			public void actionPerformed(ActionEvent arg) {
//				reportTextArea.setText(LABEL_LOADING);
//				tree.setModel(null);
//				tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(LABEL_LOADING)));
//				generateButton.setText(LABEL_LOADING);
//				generateButton.setEnabled(false);
//
//				final SVNRepo selectedRepo = getSelectedRepoAndStoreIfNew();
//
//				new Thread() {
//					@Override
//					public void run() {
//						svnReport = new SVNReport();
//						try {
//							svnReport.setRepoUrl(selectedRepo.getUrl());
//							svnReport.setUsername(selectedRepo.getUsername());
//							svnReport.setPassword(selectedRepo.getPassword());
//
//							svnReport.setStartRevision(startRevisionTextField.getText());
//							svnReport.setStartDate(startDateTextField.getText());
//							// svnReport.setQcNumber(qcNumberTextField.getText());
//							// svnReport.setQcOwner(qcOwnerTextField.getText());
//							svnReport.generate();
//							SwingUtilities.invokeLater(new Runnable() {
//								public void run() {
//									storeFieldsInProperties();
//									refreshTextArea();
//									generateButton.setText(LABEL_GENERATE);
//									generateButton.setEnabled(true);
//								}
//							});
//						} catch (final Throwable e) {
//							SwingUtilities.invokeLater(new Runnable() {
//								public void run() {
//									reportTextArea.setText(ExceptionUtils.getFullStackTrace(e));
//									generateButton.setText(LABEL_GENERATE);
//									generateButton.setEnabled(true);
//								}
//							});
//						}
//					}
//				}.start();
//			}
//		};
//	}
//
//	protected SVNRepo getSelectedRepoAndStoreIfNew() {
//		SVNRepo repo = null;
//		final Object selectedItem = repoURLComboBox.getSelectedItem();
//		if (selectedItem instanceof String) {
//			final String url = (String) selectedItem;
//			repo = SVNRepo.getSVNRepoWithURL(svnProperties.getSubversionRepositories(), url);
//			if (repo != null) {
//				svnProperties.removeSubversionRepository(repo);
//			}
//			repo = new SVNRepo(url, usernameTextField.getText(), passwordTextField.getText());
//			svnProperties.addSubversionRepository(repo);
//
//			svnProperties.sortSubversionRepositories();
//
//			setRepos(svnProperties.getSubversionRepositories());
//			repoURLComboBox.setSelectedItem(repo);
//
//		} else if (selectedItem instanceof SVNRepo) {
//			repo = (SVNRepo) selectedItem;
//		}
//		return repo;
//	}
//
//	private JComboBox createComboBox(JPanel container, String label, Object[] models) {
//		final JComboBox comboBox = new JComboBox(models);
//		comboBox.setEditable(true);
//		comboBox.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (usernameTextField != null && passwordTextField != null) {
//					final JComboBox comboBox = (JComboBox) e.getSource();
//					final Object selectedItem = comboBox.getSelectedItem();
//					if (selectedItem instanceof SVNRepo) {
//						final SVNRepo repo = (SVNRepo) selectedItem;
//						usernameTextField.setText(repo.getUsername());
//						passwordTextField.setText(repo.getPassword());
//						// } else {
//						// usernameTextField.setText("");
//						// passwordTextField.setText("");
//					}
//				}
//			}
//		});
//		// if (models.length > 0) {
//		// comboBox.setSelectedIndex(0);
//		// }
//		container.add(createLabel(label));
//		container.add(comboBox);
//		return comboBox;
//	}
//
//	private void addTextFieldChangeListener(JTextField textField) {
//		textField.getDocument().addDocumentListener(new DocumentListener() {
//			public void removeUpdate(DocumentEvent e) {
//				storeFieldsInProperties();
//				refreshTextArea();
//			}
//
//			public void insertUpdate(DocumentEvent e) {
//				storeFieldsInProperties();
//				refreshTextArea();
//			}
//
//			public void changedUpdate(DocumentEvent e) {
//				System.err.println(">>>>> CHANGED!");
//			}
//		});
//	}
//
//	private JCheckBox createCheckBox(JPanel panel, String label) {
//		panel.add(createLabel(label));
//		final JCheckBox checkBox = new JCheckBox();
//		checkBox.addActionListener(this);
//		panel.add(checkBox);
//		return checkBox;
//	}
//
//	protected SVNReportType getReportType() {
//		if (revisionsDetailedRadioButton.isSelected())
//			return SVNReportType.DETAILED;
//		if (revisionsAllRadioButton.isSelected())
//			return SVNReportType.ALL;
//		if (revisionsLastRadioButton.isSelected())
//			return SVNReportType.LAST;
//		if (revisionsNoneRadioButton.isSelected())
//			return SVNReportType.NONE;
//		return null;
//	}
//
//	private JTextField createTextField(JPanel panel, String label) {
//		final JLabel jLabel = createLabel(label);
//		panel.add(jLabel);
//		final JTextField textField = new JTextField("");
//		panel.add(textField);
//		addTextFieldChangeListener(textField);
//		return textField;
//	}
//
//	private JPasswordField createPasswordField(JPanel panel, String label) {
//		final JLabel jLabel = createLabel(label);
//		panel.add(jLabel);
//		final JPasswordField textField = new JPasswordField("");
//		panel.add(textField);
//		addTextFieldChangeListener(textField);
//		return textField;
//	}
//
//	private JLabel createLabel(String label) {
//		final JLabel jLabel = new JLabel(label + " : ", SwingConstants.RIGHT);
//		if (label.endsWith(TAG_REVISIONS)) {
//			jLabel.setForeground(Color.red);
//			jLabel.setOpaque(true);
//		}
//		if (label.contains(TAG_PATHS)) {
//			jLabel.setForeground(Color.blue);
//			jLabel.setOpaque(true);
//		}
//		return jLabel;
//	}
//
//	public void actionPerformed(ActionEvent e) {
//		refreshTextArea();
//		// http://www.java2s.com/Code/Java/Swing-Components/IconNodeTreeExample.htm
//		// javax.swing.plaf.metal.MetalIconFactory
//	}
//
//	private void refreshTextArea() {
//		storeFieldsInProperties();
//		if (svnReport != null) {
//			svnReport.setQcNumber(qcNumberTextField.getText());
//			svnReport.setQcOwner(qcOwnerTextField.getText());
//			svnReport.setFromRelease(fromReleaseTextField.getText());
//			svnReport.setToRelease(toReleaseTextField.getText());
//			svnReport.setDisplayAFilePerRelease(postponeWithFileCheckBox.isSelected());
//			svnReport.setAuthorOnRevisions(revisionsWithAuthorCheckBox.isSelected());
//			svnReport.setPathsOnRevisions(revisionsWithFilesCheckBox.isSelected());
//			svnReport.setType(getReportType());
//			svnReport.setPathFilterPattern(new BooleanPatternMatching(pathFilterTextField.getText(), !CASE_SENSITIVE));
//			svnReport.setRevisionNumbersToDisplay(CollectionUtils.splitToSet(revisionsNumbersToFilterTextField.getText()));
//			reportTextArea.setText(svnReport.toString());
//
//			tree.setModel(null);
//			tree.setModel(new DefaultTreeModel(svnReport.getReport().getRootNode()));
//		}
//	}
//
//	private void storeFieldsInProperties() {
//		if (updatingFieldsWithPreferences) {
//			return;
//		}
//
//		svnProperties.setSubversionStartRevision(startRevisionTextField.getText());
//		svnProperties.setSubversionStartDate(startDateTextField.getText());
//
//		svnProperties.setQcOwner(qcOwnerTextField.getText());
//		svnProperties.setQcNumber(qcNumberTextField.getText());
//
//		svnProperties.setReportRevisionsWithAuthor(revisionsWithAuthorCheckBox.isSelected());
//		svnProperties.setReportRevisionsWithPaths(revisionsWithFilesCheckBox.isSelected());
//		svnProperties.setReportRevisionsNumbers(revisionsNumbersToFilterTextField.getText());
//
//		svnProperties.setReportPathsLevelDetail(revisionsDetailedRadioButton.isSelected());
//		svnProperties.setReportPathsLevelAll(revisionsAllRadioButton.isSelected());
//		svnProperties.setReportPathsLevelLast(revisionsLastRadioButton.isSelected());
//		svnProperties.setReportPathsLevelNone(revisionsNoneRadioButton.isSelected());
//		svnProperties.setReportPathsPatternFilter(pathFilterTextField.getText());
//
//		svnProperties.setReportPostponeFromRelease(fromReleaseTextField.getText());
//		svnProperties.setReportPostponeToRelease(toReleaseTextField.getText());
//		svnProperties.setReportPostponeWithFile(postponeWithFileCheckBox.isSelected());
//
//		svnProperties.saveToPreferences();
//	}
//
//	private void updateFieldsWith(SVNProperties properties) {
//		updatingFieldsWithPreferences = true;
//
//		setRepos(properties.getSubversionRepositories());
//		startRevisionTextField.setText(properties.getSubversionStartRevision());
//		startDateTextField.setText(properties.getSubversionStartDate());
//
//		qcOwnerTextField.setText(properties.getQcOwner());
//		qcNumberTextField.setText(properties.getQcNumber());
//
//		revisionsWithAuthorCheckBox.setSelected(properties.isReportRevisionsWithAuthor());
//		revisionsWithFilesCheckBox.setSelected(properties.isReportRevisionsWithPaths());
//		revisionsNumbersToFilterTextField.setText(properties.getReportRevisionsNumbers());
//
//		revisionsDetailedRadioButton.setSelected(properties.isReportPathsLevelDetail());
//		revisionsAllRadioButton.setSelected(properties.isReportPathsLevelAll());
//		revisionsLastRadioButton.setSelected(properties.isReportPathsLevelLast());
//		revisionsNoneRadioButton.setSelected(properties.isReportPathsLevelNone());
//		pathFilterTextField.setText(properties.getReportPathsPatternFilter());
//
//		fromReleaseTextField.setText(properties.getReportPostponeFromRelease());
//		toReleaseTextField.setText(properties.getReportPostponeToRelease());
//		postponeWithFileCheckBox.setSelected(properties.isReportPostponeWithFile());
//
//		updatingFieldsWithPreferences = false;
//	}
//
//	private void setRepos(List<SVNRepo> newRepos) {
//		if (newRepos != null && newRepos.size() > 0) {
//			if (newRepos != svnProperties.getSubversionRepositories()) {
//				svnProperties.setSubversionRepositories(newRepos);
//			}
//			repoURLComboBox.setModel(new DefaultComboBoxModel(newRepos.toArray()));
//			repoURLComboBox.setSelectedIndex(0);
//		}
//	}

}
