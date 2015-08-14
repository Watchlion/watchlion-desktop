/**
 * 
 */
package ch.inagua.watchlion.ui;

import java.awt.Component;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import ch.inagua.watchlion.model.Application;

/**
 */
public class TreeCellRenderer extends DefaultTreeCellRenderer {

  private static final long serialVersionUID = 1L;

  private final Map<String, ImageIcon> imageIconsByType;

  public TreeCellRenderer() {
    imageIconsByType = new HashMap<String, ImageIcon>();
//    addImageIcon(ChangeType.A, "added0.png");
//    addImageIcon(ChangeType.D, "deleted0.png");
//    addImageIcon(ChangeType.M, "modified0.png");
  }

  private void addImageIcon(final String type, String iconName) {
    final URL iconURL = this.getClass().getClassLoader().getResource("icons/" + iconName);
    imageIconsByType.put(type, new ImageIcon(iconURL));
  }

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean a2, boolean a3, boolean a4, int a5,
    boolean a6) {

	final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
	
	String label;
	if (node.getUserObject() != null && node.getUserObject() instanceof Application) {
		Application app = (Application) node.getUserObject();
		label = app.getLabelWithLastVersion();
	} else {
		label = "" + node;
	}
	
    final Component c = super.getTreeCellRendererComponent(tree, label, a2, a3, a4, a5, a6);
    // final String type = getChangeType(node);
    // setIconForType(type);
    return c;
  }

//  private void setIconForType(String type) {
//    if (!StringUtils.isBlank(type)) {
//      final ImageIcon imageIcon = imageIconsByType.get(type);
//      setIcon(imageIcon);
//    }
//  }

//  private String getChangeType(DefaultMutableTreeNode node) {
//    final Object userObject = node.getUserObject();
//    if (userObject != null && userObject instanceof ChangeType) {
//      final ChangeType changeType = (ChangeType) userObject;
//      return changeType.getType();
//    }
//    return null;
//  }

}
