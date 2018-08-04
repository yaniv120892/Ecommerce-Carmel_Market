package Backend.Addons.TreeNode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeNodeTest {


    TreeNode<String> root;
    @Before
    public void setUp() throws Exception {
        root = new TreeNode<>("root");
        root.addChild("child");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void isRoot() {
        assertTrue(root.isRoot());
    }

    @Test
    public void isLeaf() {
        assertTrue(root.findTreeNode("child").isLeaf());
    }

    @Test
    public void addChild() {
        root.addChild("child2");
        assertNotNull(root.findTreeNode("child2"));
    }

    @Test
    public void getLevel() {
        assertTrue(root.getLevel() == 0);
        assertTrue(root.findTreeNode("child").getLevel() == 1);
    }

    @Test
    public void findTreeNode() {
        assertNotNull(root.findTreeNode("child"));
        assertNull(root.findTreeNode("child255"));
    }

    @Test
    public void getParent() {
        assertTrue(root.findTreeNode("child").getParent().equals(root));
    }
}