package verifyavl;

public class VerifyAVL {

    public static boolean verifyAVL(AVLNode root) {
        return verifyAVL(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static boolean verifyAVL(AVLNode root, int min, int max) {
        if(root == null) {
            return true;
        }
        int height = -1; //fencepost for height check
        if(root.left != null) {
            if(root.left.key > root.key || (root.left.key > max || root.left.key < min)) { //bst check
                return false;
            }
            height = root.left.height;
        }
        if(root.right == null) { //verify avl property
            if(height > 0) {
                return false;
            }
        }
        if(root.right != null) {
            if(root.right.key < root.key || (root.right.key < min || root.right.key > max)) { //bst check
                return false;
            }
            if(root.right.height > height + 1) { //avl property check - right height too large
                return false;
            }
            if(root.right.height < height - 1) { //avl property check - left height too large
                return false;
            }
            if(root.right.height > height) {
                height = root.right.height;
            }
        }
        if(height+1 != root.height) { //avl property check - root = ceiling of left & right
            return false;
        }
        return verifyAVL(root.left, min, root.key) && verifyAVL(root.right, root.key, max);
    }
}