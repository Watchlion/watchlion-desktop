/**
 * 
 */
package ch.inagua.watchlion.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import ch.inagua.watchlion.model.Application;
import ch.inagua.watchlion.model.Watchlion;

/**
 */
public class TreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

	final Font FONT_PLAIN = new Font("SansSerif", Font.PLAIN, 12);

//	final Font FONT_BOLD = new Font("SansSerif", Font.BOLD, 12);
	
//	private final Map<String, ImageIcon> imageIconsByType;

	private Watchlion watchlion;

	public TreeCellRenderer(Watchlion watchlion) {
		this.watchlion = watchlion;
		// imageIconsByType = new HashMap<String, ImageIcon>();
		// addImageIcon(ChangeType.A, "added0.png");
		// addImageIcon(ChangeType.D, "deleted0.png");
		// addImageIcon(ChangeType.M, "modified0.png");
	}

//	private void addImageIcon(final String type, String iconName) {
//		final URL iconURL = this.getClass().getClassLoader()
//				.getResource("icons/" + iconName);
//		imageIconsByType.put(type, new ImageIcon(iconURL));
//	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean a2, boolean a3, boolean a4, int a5, boolean a6) {

		final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		String label;
		Application app = null;
		if (node.getUserObject() != null && node.getUserObject() instanceof Application) {
			app = (Application) node.getUserObject();
			label = app.getLabelWithLastVersion() + " [" + getUpdatedDate(app) + "]";
		} else {
			label = "" + node;
		}
		
		super.getTreeCellRendererComponent(tree, label, a2, a3, a4, a5, a6);
		setFont(FONT_PLAIN);

		// final String type = getChangeType(node);
		// setIconForType(type);
		if (app != null) {
			boolean isNewApplication = false;
			try {
				isNewApplication = watchlion.isNewApplication(app.getId());
			} catch (ParseException e) {
			} catch (NullPointerException e) {
			}
			if (isNewApplication) {
		        setFont(getFont().deriveFont(Font.BOLD));
				//setFont(FONT_BOLD);
//			} else {
//				int style = getFont().getStyle();
//				style &= ~Font.BOLD;
				// setFont(getFont().deriveFont(style));
		        // setFont(getFont().deriveFont(Font.PLAIN));
//				setFont(FONT_PLAIN);
			}

			if (app.isIgnored()) {
				setForeground(Color.LIGHT_GRAY);
			} else {
				setForeground(Color.BLACK);
			}
		}
		return this;
	}

	private String getUpdatedDate(Application app) {
		Application refApp = watchlion.getReference().getApplicationForId(app.getId());
		if (refApp.getUpdateDate() == null) {
			Application.Version v = refApp.getLastVersion();
			if (v != null && v.getUpdateDate() != null) {
				return " (v:" + DATE_FORMAT.format(v.getUpdateDate()) + ") ";
			}
			return "-";
		}
		return DATE_FORMAT.format(refApp.getUpdateDate());
	}

	// private void setIconForType(String type) {
	// if (!StringUtils.isBlank(type)) {
	// final ImageIcon imageIcon = imageIconsByType.get(type);
	// setIcon(imageIcon);
	// }
	// }

	// private String getChangeType(DefaultMutableTreeNode node) {
	// final Object userObject = node.getUserObject();
	// if (userObject != null && userObject instanceof ChangeType) {
	// final ChangeType changeType = (ChangeType) userObject;
	// return changeType.getType();
	// }
	// return null;
	// }

}
