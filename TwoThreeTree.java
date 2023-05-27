import java.util.Objects;

public class TwoThreeTree {
    private TreeNode root;
    private int countOfIterations = 0;
    private boolean successfulInsert;
    private boolean successfulRemoval;
    private boolean split;
    private boolean underflow;
    private boolean first;
    private boolean singleNodeUnderflow;

    private enum Nodes {
        LEFT, RIGHT, DUMMY
    }

    public TwoThreeTree() {
        root = null;
        successfulInsert = false;
        successfulRemoval = false;
        underflow = false;
        singleNodeUnderflow = false;
        split = false;
        first = false;
    }

    private class Node {

    }

    private class TreeNode extends Node {

        int[] keys;
        Node[] children;
        int degree;

        TreeNode() {
            keys = new int[2];
            children = new Node[3];
            degree = 0;
        }

    }

    private class LeafNode extends Node {
        int key;

        LeafNode(int key) {
            this.key = key;
        }

    }

    private void insertKey(int key) {
        countOfIterations++;
        Node[] array;
        array = insert(key, root);

        if (array[1] == null) {
            root = (TreeNode) array[0];
        } else {
            TreeNode treeRoot = new TreeNode();
            treeRoot.children[0] = array[0];
            treeRoot.children[1] = array[1];
            updateTree(treeRoot);
            root = treeRoot;
        }
    }

    private Node[] insert(int key, Node n) {
        countOfIterations++;
        Node[] array = new Node[2];
        Node[] catchArray;
        TreeNode t = null;

        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        }

        if (root == null && !first) {
            first = true;
            t = new TreeNode();
            t.children[0] = insert(key, t.children[0])[0];
            updateTree(t);
            array[0] = t;
            array[1] = null;
        } else if (t != null && !(t.children[0] instanceof LeafNode)) {
            if (key < t.keys[0]) {
                catchArray = insert(key, t.children[0]);
                t.children[0] = catchArray[0];
                if (split) {
                    if (t.degree <= 2) {
                        split = false;
                        t.children[2] = t.children[1];
                        t.children[1] = catchArray[1];
                        updateTree(t);
                        array[0] = t;
                        array[1] = null;
                    } else {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = t.children[1];
                        newNode.children[1] = t.children[2];
                        updateTree(newNode);
                        t.children[1] = catchArray[1];
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            } else if (t.children[2] == null || key < t.keys[1]) {
                catchArray = insert(key, t.children[1]);
                t.children[1] = catchArray[0];
                if (split) {
                    if (t.degree <= 2) {
                        split = false;
                        t.children[2] = catchArray[1];
                        updateTree(t);
                        array[0] = t;
                        array[1] = null;
                    } else {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = catchArray[1];
                        newNode.children[1] = t.children[2];
                        updateTree(newNode);
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            } else {
                catchArray = insert(key, t.children[2]);
                t.children[2] = catchArray[0];
                if (split) {
                    if (t.degree > 2) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = catchArray[0];
                        newNode.children[1] = catchArray[1];
                        updateTree(newNode);
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;

                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            }
        } else if (t != null) {
            LeafNode l1, l2 = null, l3 = null;
            l1 = (LeafNode) t.children[0];
            if (t.children[1] != null && t.children[1] instanceof LeafNode) {
                l2 = (LeafNode) t.children[1];
            }
            if (t.children[2] != null && t.children[2] instanceof LeafNode) {
                l3 = (LeafNode) t.children[2];
            }
            if (t.degree <= 2) {
                if (t.degree == 1 && key > l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[1] = leaf;
                } else if (t.degree == 1 && key < l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[1] = l1;
                    t.children[0] = leaf;
                } else if (t.degree == 2 && key < l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = l2;
                    t.children[1] = l1;
                    t.children[0] = leaf;
                } else if (t.degree == 2 && key < Objects.requireNonNull(l2).key && key > l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = l2;
                    t.children[1] = leaf;
                } else if (t.degree == 2) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = leaf;
                }
                updateTree(t);
                array[0] = t;
                array[1] = null;
            } else {
                split = true;
                if (key < l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    TreeNode newNode = new TreeNode();
                    t.children[0] = leaf;
                    t.children[1] = l1;
                    t.children[2] = null;
                    updateTree(t);
                    newNode.children[0] = l2;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key < Objects.requireNonNull(l2).key) {
                    LeafNode leaf = new LeafNode(key);
                    TreeNode newNode = new TreeNode();
                    t.children[1] = leaf;
                    t.children[2] = null;
                    updateTree(t);
                    newNode.children[0] = l2;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key < Objects.requireNonNull(l3).key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = null;
                    updateTree(t);
                    TreeNode newNode = new TreeNode();
                    newNode.children[0] = leaf;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key >= l3.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = null;
                    updateTree(t);
                    TreeNode newNode = new TreeNode();
                    newNode.children[0] = l3;
                    newNode.children[1] = leaf;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                }
            }
            successfulInsert = true;
        } else if (n == null) {
            successfulInsert = true;
            array[0] = new LeafNode(key);
            array[1] = null;
            return array;
        }
        return array;
    }

    private Node remove(int key, Node n) {
        countOfIterations++;
        TreeNode t = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        }
        if (n == null) {
            return null;
        }
        if (t != null && t.children[0] instanceof TreeNode) {
            if (key < t.keys[0]) {
                t.children[0] = remove(key, t.children[0]);
                if (singleNodeUnderflow) {
                    TreeNode child = (TreeNode) t.children[0];
                    TreeNode rightChild = (TreeNode) t.children[1];
                    if (rightChild.degree == 2) {
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = child;
                        updateTree(rightChild);
                        t.children[0] = rightChild;
                        t.children[1] = t.children[2];
                        t.children[2] = null;
                        if (t.degree == 2) {
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        } else {
                            singleNodeUnderflow = false;
                        }
                    } else if (rightChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = t.children[0];
                        newNode.children[1] = rightChild.children[0];
                        t.children[0] = newNode;
                        updateTree(newNode);
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        singleNodeUnderflow = false;
                    }
                } else if (underflow) {
                    underflow = false;
                    TreeNode child = (TreeNode) t.children[0];
                    TreeNode rightChild = (TreeNode) t.children[1];
                    if (rightChild.degree == 3) {
                        Node reference = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        child.children[1] = reference;
                        updateTree(child);
                    } else if (rightChild.degree == 2) {
                        Node reference = child.children[0];
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = reference;
                        updateTree(rightChild);
                        t.children[0] = rightChild;
                        if (t.degree == 3) {
                            t.children[1] = t.children[2];
                            t.children[2] = null;
                        } else {
                            Node ref = t.children[0];
                            t = (TreeNode) ref;
                            singleNodeUnderflow = true;
                        }
                    }
                }
                updateTree(t);
            } else if (t.children[2] == null || key < t.keys[1]) {
                t.children[1] = remove(key, t.children[1]);
                if (singleNodeUnderflow) {
                    TreeNode leftChild = (TreeNode) t.children[0];
                    TreeNode child = (TreeNode) t.children[1];
                    TreeNode rightChild = (TreeNode) t.children[2];
                    if (leftChild.degree == 2) {
                        leftChild.children[2] = child;
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        updateTree(leftChild);
                        if (t.degree == 2) {
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        } else {
                            singleNodeUnderflow = false;
                        }
                    } else if (rightChild != null && rightChild.degree == 2) {
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = child;
                        updateTree(rightChild);
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        singleNodeUnderflow = false;

                    } else if (leftChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = leftChild.children[2];
                        newNode.children[1] = child;
                        t.children[1] = newNode;
                        updateTree(newNode);
                        updateTree(leftChild);
                        singleNodeUnderflow = false;
                    } else if (rightChild != null && rightChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = child;
                        newNode.children[1] = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        t.children[1] = newNode;
                        updateTree(newNode);
                        updateTree(rightChild);
                        singleNodeUnderflow = false;
                    }
                } else if (underflow) {
                    underflow = false;
                    TreeNode leftChild = (TreeNode) t.children[0];
                    TreeNode child = (TreeNode) t.children[1];
                    TreeNode rightChild = (TreeNode) t.children[2];
                    if (leftChild.degree == 3) {
                        Node reference = leftChild.children[2];
                        leftChild.children[2] = null;
                        child.children[1] = child.children[0];
                        child.children[0] = reference;
                        updateTree(leftChild);
                        updateTree(child);
                    } else if (rightChild != null && rightChild.degree == 3) {
                        Node reference = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        child.children[1] = reference;
                        updateTree(child);
                    } else if (leftChild.degree == 2) {
                        Node reference = child.children[0];
                        leftChild.children[2] = reference;
                        updateTree(leftChild);
                        t.children[1] = null;
                        if (t.degree == 3) {
                            t.children[1] = t.children[2];
                            t.children[2] = null;
                        } else {
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        }
                    } else if (rightChild != null && rightChild.degree == 2) {
                        Node reference = child.children[0];
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = reference;
                        updateTree(rightChild);
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        singleNodeUnderflow = false;
                    }
                }
                updateTree(t);
            } else {
                t.children[2] = remove(key, t.children[2]);
                if (singleNodeUnderflow) {
                    TreeNode child = (TreeNode) t.children[2];
                    TreeNode leftChild = (TreeNode) t.children[1];
                    if (leftChild.degree == 2) {
                        leftChild.children[2] = child;
                        t.children[2] = null;
                        updateTree(leftChild);
                    } else if (leftChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = leftChild.children[2];
                        newNode.children[1] = t.children[2];
                        t.children[2] = newNode;
                        updateTree(newNode);
                        updateTree(leftChild);
                    }
                    singleNodeUnderflow = false;
                } else if (underflow) {
                    underflow = false;
                    TreeNode leftChild = (TreeNode) t.children[1];
                    TreeNode child = (TreeNode) t.children[2];
                    if (leftChild.degree == 3) {
                        Node reference = leftChild.children[2];
                        leftChild.children[2] = null;
                        child.children[1] = child.children[0];
                        child.children[0] = reference;
                        updateTree(leftChild);
                        updateTree(child);
                    } else if (leftChild.degree == 2) {
                        Node reference = child.children[0];
                        leftChild.children[2] = reference;
                        updateTree(leftChild);
                        t.children[2] = null;
                    }
                }
                updateTree(t);
            }
        } else if (t != null && t.children[0] instanceof LeafNode) {
            LeafNode l1, l2 = null, l3 = null;
            l1 = (LeafNode) t.children[0];
            if (t.children[1] != null && t.children[1] instanceof LeafNode) {
                l2 = (LeafNode) t.children[1];
            }
            if (t.children[2] != null && t.children[2] instanceof LeafNode) {
                l3 = (LeafNode) t.children[2];
            }
            if (t.degree == 3) {
                if (key == l1.key) {
                    t.children[0] = l2;
                    t.children[1] = l3;
                    t.children[2] = null;
                } else if (key == Objects.requireNonNull(l2).key) {
                    t.children[1] = l3;
                    t.children[2] = null;
                } else if (key == Objects.requireNonNull(l3).key) {
                    t.children[2] = null;
                }
                updateTree(t);
            } else if (t.degree == 2) {
                underflow = true;
                if (l1.key == key) {
                    t.children[0] = l2;
                    t.children[1] = null;
                } else if (Objects.requireNonNull(l2).key == key) {
                    t.children[1] = null;
                }
            } else if (t.degree == 1) {
                if (l1.key == key) {
                    t.children[0] = null;
                }
            }

            successfulRemoval = true;
        }
        return t;
    }

    private void updateTree(TreeNode t) {
        countOfIterations++;
        if (t != null) {
            if (t.children[2] != null && t.children[1] != null && t.children[0] != null) {
                t.degree = 3;
                t.keys[0] = getValueForKey(t, Nodes.LEFT);
                t.keys[1] = getValueForKey(t, Nodes.RIGHT);
            } else if (t.children[1] != null && t.children[0] != null) {
                t.degree = 2;
                t.keys[0] = getValueForKey(t, Nodes.LEFT);
                t.keys[1] = 0;
            } else if (t.children[0] != null) {
                t.degree = 1;
                t.keys[1] = t.keys[0] = 0;
            }
        }
    }

    private int getValueForKey(Node n, Nodes nodes) {
        countOfIterations++;
        int key = -1;
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (l != null) {
            key = l.key;
        }
        if (t != null) {
            if (null != nodes) {
                switch (nodes) {
                    case LEFT -> key = getValueForKey(t.children[1], Nodes.DUMMY);
                    case RIGHT -> key = getValueForKey(t.children[2], Nodes.DUMMY);
                    case DUMMY -> key = getValueForKey(t.children[0], Nodes.DUMMY);
                    default -> {
                    }
                }
            }
        }
        return key;
    }

    private boolean search(int key, Node n) {
        countOfIterations++;
        boolean found = false;
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (t != null) {
            if (t.degree == 1) {
                found = search(key, t.children[0]);
            } else if (t.degree == 2 && key < t.keys[0]) {
                found = search(key, t.children[0]);
            } else if (t.degree == 2) {
                found = search(key, t.children[1]);
            } else if (t.degree == 3 && key < t.keys[0]) {
                found = search(key, t.children[0]);
            } else if (t.degree == 3 && key < t.keys[1]) {
                found = search(key, t.children[1]);
            } else if (t.degree == 3) {
                found = search(key, t.children[2]);
            }
        } else if (l != null && key == l.key) {
            return true;
        }
          return found;
    }

    public boolean insert(int key) {
        countOfIterations = 0;
        boolean insert = false;
        split = false;
        if (!search(key)) {
            insertKey(key);
        }
        if (successfulInsert) {
            insert = true;
            successfulInsert = false;
        }
        return insert;
    }

    public boolean search(int key) {
        countOfIterations = 0;
        return search(key, root);
    }

    public boolean remove(int key) {
        countOfIterations = 0;
        boolean delete = false;
        singleNodeUnderflow = false;
        underflow = false;
        if (search(key)) {
            root = (TreeNode) remove(key, root);
            if (Objects.requireNonNull(root).degree == 1 && root.children[0] instanceof TreeNode) {
                root = (TreeNode) root.children[0];
            }
        }
        if (successfulRemoval) {
            delete = true;
            successfulRemoval = false;
        }
        return delete;
    }

    public int getCountOfIterations() {
        return countOfIterations;
    }
}