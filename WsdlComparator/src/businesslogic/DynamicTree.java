package businesslogic;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class DynamicTree extends JPanel {
	protected JPanel ParentPanel;
	protected DefaultMutableTreeNode rootNode;
	protected DefaultTreeModel treeModel;
	protected JTree tree;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	private JPopupMenu menu = new JPopupMenu("Popup");
	private TreePath selectedTreeItemPopup;

	TreeProcess process;

	public DynamicTree(JPanel parentPanel) {
		super(new GridLayout(1, 0));

		ParentPanel = parentPanel;
		rootNode = new DefaultMutableTreeNode("Wsdl Comparator");
		treeModel = new DefaultTreeModel(rootNode);

		tree = new JTree(treeModel);
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);

		menu.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				// TODO Auto-generated method stub
			}
		});

		JMenuItem item = new JMenuItem("Expand all sub node");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (process == null && treeModel.getChildCount(rootNode) > 0) {
					System.out.println("Expand all sub node");
					process = new TreeProcess(TreeProcess.EXPAND, selectedTreeItemPopup, true);
					process.execute();
				}
			}
		});
		menu.add(item);

		item = new JMenuItem("Collapse all sub node");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (process == null && treeModel.getChildCount(rootNode) > 0) {
					System.out.println("Collapse all sub node");
					process = new TreeProcess(TreeProcess.COLLAPSE, selectedTreeItemPopup, true);
					process.execute();
				}
			}
		});
		menu.add(item);

		item = new JMenuItem("Expand sub node");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (process == null && treeModel.getChildCount(rootNode) > 0) {
					System.out.println("Expand all sub node");
					process = new TreeProcess(TreeProcess.EXPAND, selectedTreeItemPopup, false);
					process.execute();
				}
			}
		});
		menu.add(item);

		item = new JMenuItem("Collapse sub node");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (process == null && treeModel.getChildCount(rootNode) > 0) {
					System.out.println("Expand all sub node");
					process = new TreeProcess(TreeProcess.COLLAPSE, selectedTreeItemPopup, false);
					process.execute();
				}
			}
		});
		menu.add(item);

		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				selectedTreeItemPopup = tree.getPathForLocation(e.getX(), e.getY());
				if (SwingUtilities.isRightMouseButton(e)) {
					int selRow = tree.getRowForLocation(e.getX(), e.getY());
					if (selRow > -1) {
						tree.setSelectionPath(selectedTreeItemPopup);
						menu.show(e.getComponent(), e.getX(), e.getY());
					}
				} else {
					tree.setSelectionPath(selectedTreeItemPopup);
					selectedTreeItemPopup = null;
					menu.setVisible(false);
				}
			}
		};
		tree.addMouseListener(ml);

		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane);
	}

	public DefaultMutableTreeNode getRootNode() {
		return rootNode;
	}

	public void setCellRenderer(TreeCellRenderer render) {
		tree.setCellRenderer(render);		
	}

	public void expandAllRootTree() {
		if (process == null && treeModel.getChildCount(rootNode) > 0) {
			System.out.println("Expand all root node");
			TreePath parent = new TreePath(tree.getModel().getRoot());
			process = new TreeProcess(TreeProcess.EXPAND, parent, true);
			process.execute();
		}
	}

	public void collapseAllRootTree() {
		if (process == null && treeModel.getChildCount(rootNode) > 0) {
			System.out.println("Collapse all root node");
			TreePath parent = new TreePath(tree.getModel().getRoot());
			process = new TreeProcess(TreeProcess.COLLAPSE, parent, true);
			process.execute();
		}
	}

	private void expand(TreePath parent) {
		expand(parent, true);
	}

	@SuppressWarnings("rawtypes")
	private void expand(TreePath parent, boolean isAll) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				if (tree.isCollapsed(path) || isAll) {
					expand(path, isAll);
				}
			}
		}
		tree.expandPath(parent);
	}

	private void collapse(TreePath parent) {
		collapse(parent, true);
	}

	@SuppressWarnings("rawtypes")
	private void collapse(TreePath parent, boolean isAll) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				if (tree.isExpanded(path) || isAll) {
					collapse(path, isAll);
				}
			}
		}
		tree.collapsePath(parent);
	}

	/** Remove all nodes except the root node. */
	public void clear() {
		rootNode.removeAllChildren();
		treeModel.reload();
	}

	/** Remove the currently selected node. */
	public void removeCurrentNode() {
		TreePath currentSelection = tree.getSelectionPath();
		if (currentSelection != null) {
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
			MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
			if (parent != null) {
				treeModel.removeNodeFromParent(currentNode);
				return;
			}
		}

		// Either there was no selection, or the root was selected.
		toolkit.beep();
	}

	/** Add child to the currently selected node. */
	public DefaultMutableTreeNode addObject(Object child) {
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) {
			parentNode = rootNode;
		} else {
			parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
		}

		return addObject(parentNode, child, true);
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
		return addObject(parent, child, false);
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child, boolean shouldBeVisible) {
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

		if (parent == null) {
			parent = rootNode;
		}

		// It is key to invoke this on the TreeModel, and NOT
		// DefaultMutableTreeNode
		treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

		// Make sure the user can see the lovely new node.
		if (shouldBeVisible) {
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}

	@SuppressWarnings("unchecked")
	private class TreeProcess extends SwingWorker<Object, Void> {

		public static final int EXPAND = 0;
		public static final int COLLAPSE = 1;

		private int TypeOperation = -1;
		private TreePath TreePath;
		private boolean IsAll = false;

		public TreeProcess(int type, TreePath treePath, boolean isAll) {
			TypeOperation = type;
			TreePath = treePath;
			IsAll = isAll;
		}

		@Override
		protected void process(List chunks) {
			super.process(chunks);
		}

		protected Object doInBackground() {
			// ((Main) ParentPanel).showLoading();
			Object result = null;
			try {
				if (TypeOperation != -1) {
					switch (TypeOperation) {
					case EXPAND:
						if (IsAll) {
							expand(TreePath);
						} else {
							expand(TreePath, IsAll);
						}
						break;
					case COLLAPSE:
						if (IsAll) {
							collapse(TreePath);
						} else {
							collapse(TreePath, IsAll);
						}
						break;
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
				// ((Main) ParentPanel).hideLoading();
				// Object result = get();
				// if (result instanceof Boolean) {
				// if ((boolean) result) {
				// JOptionPane.showMessageDialog(getRootPane(), "Operation
				// performed successfully");
				// } else {
				// JOptionPane.showMessageDialog(getRootPane(), "Operation
				// failed");
				// }
				// } else if (result instanceof Exception) {
				// JOptionPane.showMessageDialog(getRootPane(), ((Exception)
				// result).getMessage());
				// }
			} catch (Exception e) {
				JOptionPane.showMessageDialog(getRootPane(), e.getMessage());
			} finally {
				process = null;
			}
		}

	}

	class MyTreeModelListener implements TreeModelListener {
		public void treeNodesChanged(TreeModelEvent e) {
			DefaultMutableTreeNode node;
			node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());

			/*
			 * If the event lists children, then the changed node is the child
			 * of the node we've already gotten. Otherwise, the changed node and
			 * the specified node are the same.
			 */

			int index = e.getChildIndices()[0];
			node = (DefaultMutableTreeNode) (node.getChildAt(index));

			System.out.println("The user has finished editing the node.");
			System.out.println("New value: " + node.getUserObject());
		}

		public void treeNodesInserted(TreeModelEvent e) {
		}

		public void treeNodesRemoved(TreeModelEvent e) {
		}

		public void treeStructureChanged(TreeModelEvent e) {
		}
	}
}
