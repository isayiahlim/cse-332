package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.worklists.WorkList;
import datastructures.worklists.ArrayStack;

import java.lang.reflect.Array;

/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 * <p>
 * 1. Create a subclass of BSTNode, perhaps named AVLNode.
 * 2. Override the insert method such that it creates AVLNode instances
 * instead of BSTNode instances.
 * 3. Do NOT "replace" the children array in BSTNode with a new
 * children array or left and right fields in AVLNode.  This will
 * instead mask the super-class fields (i.e., the resulting node
 * would actually have multiple copies of the node fields, with
 * code accessing one pair or the other depending on the type of
 * the references used to access the instance).  Such masking will
 * lead to highly perplexing and erroneous behavior. Instead,
 * continue using the existing BSTNode children array.
 * 4. Ensure that the class does not have redundant methods
 * 5. Cast a BSTNode to an AVLNode whenever necessary in your AVLTree.
 * This will result a lot of casts, so we recommend you make private methods
 * that encapsulate those casts.
 * 6. Do NOT override the toString method. It is used for grading.
 * 7. The internal structure of your AVLTree (from this.root to the leaves) must be correct
 */

public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {
    public AVLTree() {
        super();
    }

    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        if(root == null) {
            root = new AVLNode(key, value, 0);
            size = 1;
            return null;
        }
        WorkList<AVLNode> parents = new ArrayStack<>();
        V replaced = insert(key, value, (AVLNode)root, parents); //bst insert
        if(replaced == null) {
            rotateUp(parents); //updates heights + rotates when necessary
        }
        return replaced;
    }

    private void rotateUp(WorkList<AVLNode> parents) { //rotates up + updates heights
        boolean rotated = false;
        while(parents.hasWork() && !rotated) {
            AVLNode grandParent = null;
            AVLNode parent = parents.next();
            int parentIndex = parent.key.compareTo(root.key) < 0 ? 0 : 1;
            if (parents.hasWork()) {
                grandParent = parents.peek();
                parentIndex = parent.key.compareTo(grandParent.key) < 0 ? 0 : 1;
            }
            AVLNode left = (AVLNode) parent.children[0];
            AVLNode right = (AVLNode) parent.children[1];
            int leftHeight = left == null ? -1 : left.height;
            int rightHeight = right == null ? -1 : right.height;
            if (leftHeight > rightHeight + 1) {
                int leftLeftHeight = left.children[0] == null ? -1 : ((AVLNode) left.children[0]).height;
                int leftRightHeight = left.children[1] == null ? -1 : ((AVLNode) left.children[1]).height;
                //single rotate
                if (leftLeftHeight > leftRightHeight) {
                    if (left.children[0] != null && parent == root) {
                        root = rotateLeft(parent, left);
                    } else if (left.children[0] != null && grandParent != null) {
                        grandParent.children[parentIndex] = rotateLeft(parent, left);
                    }
                } else if (leftRightHeight > leftLeftHeight) {
                    //double rotate
                    if (left.children[1] != null && parent == root) {
                        root.children[0] = rotateRight(left, (AVLNode) left.children[1]);
                        root = rotateLeft((AVLNode) root, (AVLNode) root.children[0]);
                    } else if (left.children[1] != null && grandParent != null) {
                        parent.children[0] = rotateRight(left, (AVLNode) left.children[1]);
                        grandParent.children[parentIndex] = rotateLeft(parent, (AVLNode) parent.children[0]);
                    }
                }
                rotated = true;
            } else if (leftHeight + 1 < rightHeight) {
                int rightLeftHeight = right.children[0] == null ? -1 : ((AVLNode) right.children[0]).height;
                int rightRightHeight = right.children[1] == null ? -1 : ((AVLNode) right.children[1]).height;
                //single rotate
                if (rightRightHeight > rightLeftHeight) {
                    if (right.children[1] != null && parent == root) {
                        root = rotateRight(parent, right);
                    } else if (right.children[1] != null && grandParent != null) {
                        grandParent.children[parentIndex] = rotateRight(parent, right);
                    }
                }
                //double rotate
                else if (rightLeftHeight > rightRightHeight) {
                    if (right.children[0] != null && parent == root) {
                        root.children[1] = rotateLeft(right, (AVLNode) right.children[0]);
                        root = rotateRight((AVLNode) root, (AVLNode) root.children[1]);
                    } else if (right.children[0] != null && grandParent != null) {
                        parent.children[1] = rotateLeft(right, (AVLNode) right.children[0]);
                        grandParent.children[parentIndex] = rotateRight(parent, (AVLNode) parent.children[1]);
                    }
                }
                rotated = true;
            }
            adjustHeight(parent);
        }
    }

    private AVLNode rotateLeft(AVLNode parent, AVLNode left) {
        parent.children[0] = left.children[1];
        adjustHeight(parent);
        left.children[1] = parent;
        adjustHeight(left);
        return left;
    }

    private AVLNode rotateRight(AVLNode parent, AVLNode right) {
        parent.children[1] = right.children[0];
        adjustHeight(parent);
        right.children[0] = parent;
        adjustHeight(right);
        return right;
    }

    private void adjustHeight(AVLNode parent) {
        int leftHeight = parent.children[0] == null ? -1 : ((AVLNode)parent.children[0]).height;
        int rightHeight = parent.children[1] == null ? -1 : ((AVLNode)parent.children[1]).height;
        parent.height = Math.max(leftHeight, rightHeight) + 1;
    }

    private V insert(K key, V value, AVLNode root, WorkList<AVLNode> parents) { //bst insert
        int direction = key.compareTo(root.key);
        if(direction == 0) { //key = current
            V replaced = root.value;
            root.value = value;
            return replaced;
        }
        parents.add(root);
        direction = direction < 0 ? 0 : 1; //left or right child
        if(root.children[direction] == null){ //insert
            root.children[direction] = new AVLNode(key, value);
            adjustHeight(root);
            size ++;
            return null;
        }
        return insert(key, value, (AVLNode) root.children[direction], parents);
    }

    //inner class
    public class AVLNode extends BSTNode{
        public int height;
        public AVLNode(K key, V value) {
            this(key, value, 0);
        }
        public AVLNode(K key, V value, int height) {
            super(key, value);
            this.height = height;
        }
    }


// AVLNode toReplace = null;
// // BST Find
//    AVLNode prev = null;
//    AVLNode current = (AVLNode)this.root;
//    AVLNode problem = null;
//    int child = -1;
//        while (current != null) { //looks for the key trying to insert
//        int direction = Integer.signum(key.compareTo(current.key));
//        // We found the key!
//        if (direction == 0) {
//            toReplace = current;
//        }
//        else {
//            // direction + 1 = {0, 2} -> {0, 1}
//            child = Integer.signum(direction + 1);
//            if(prev != null && ((AVLNode)prev.children[child]).height >
//                    ((AVLNode)prev.children[Math.abs(child-1)]).height) {
//                prev.height ++;
//                problem = prev;
//            } else if(prev != null && ((AVLNode)prev.children[child]).height ==
//                    ((AVLNode)prev.children[Math.abs(child-1)]).height) {
//                prev.height ++;
//            }
//            prev = current;
//            current = (AVLNode) current.children[child];
//        }
//    }
//    // If the key doesn't exist, must make new node
//        if (toReplace == null) {
//        current = new AVLNode(key, value);
//        if (this.root == null) {
//            this.root = current;
//        }
//        else {
//            assert(child >= 0); // child should have been set in the loop
//            // above
//            prev.children[child] = current;
//        }
//        this.size++;
//        toReplace = current;
//        if(problem != null) { //rotations time!
//
//        }
//    }
//
//    V oldValue = toReplace.value;
//    toReplace.value = value;
//    return oldValue;
}
