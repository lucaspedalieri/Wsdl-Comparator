package businesslogic;

import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import model.ChieldDifference;
import model.ParentDifference;

public class DifferenceTreeCellRenderer implements TreeCellRenderer {
	private JLabel label;

	public DifferenceTreeCellRenderer() {
		label = new JLabel();
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		Object o = ((DefaultMutableTreeNode) value).getUserObject();
		if (o instanceof ParentDifference) {
			ParentDifference parentDifference = (ParentDifference) o;
			URL imageUrl = this.getClass().getResource(parentDifference.getFlagIcon());
			if (imageUrl != null) {
				label.setIcon(new ImageIcon(imageUrl));
			}
			label.setText(parentDifference.getName());
		} else if (o instanceof ChieldDifference) {
			ChieldDifference chieldDifference = (ChieldDifference) o;
			URL imageUrl = this.getClass().getResource(chieldDifference.getFlagIcon());
			if (imageUrl != null) {
				label.setIcon(new ImageIcon(imageUrl));
			}
			label.setText(chieldDifference.getName());
		}else {
            label.setIcon(null);
            label.setText("" + value);
        }
		return label;
	}
}
