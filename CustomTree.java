import java.io.Serializable;
import java.util.*;

/**
Tree
*/
public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    Entry<String> root;

    public CustomTree() {
        this.root = new Entry<String>("0");
    }

    static class Entry<T> implements Serializable{
        String elementName;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }
        void checkChildren(){
            if (leftChild != null) availableToAddLeftChildren = false;
            if (rightChild != null) availableToAddRightChildren = false;
        }
        boolean isAvailableToAddChildren(){
            return availableToAddLeftChildren || availableToAddRightChildren;
        }
    }

    @Override
    public boolean add(String s){
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Entry<String> leaf = queue.poll();

            if (leaf == null) break;

            leaf.checkChildren();
            if (leaf.isAvailableToAddChildren()) {
                if (leaf.availableToAddLeftChildren && leaf.availableToAddRightChildren) {
                    leaf.leftChild = new Entry<String>(s);
                    leaf.leftChild.parent = leaf;
                    return true;
                }
                if (leaf.availableToAddRightChildren) {
                    leaf.rightChild = new Entry<String>(s);
                    leaf.rightChild.parent = leaf;
                    return true;
                }
            } else {
                if (leaf.leftChild != null)
                    queue.offer(leaf.leftChild);
                if (leaf.rightChild != null)
                    queue.offer(leaf.rightChild);
            }
        }
        return false;
    }
    @Override
    public int size() {
        int result = 0;
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Entry<String> leaf = queue.poll();

            assert leaf != null;
            if (leaf.leftChild != null) {
                result++;
                queue.offer(leaf.leftChild);
            }
            if (leaf.rightChild != null) {
                result++;
                queue.offer(leaf.rightChild);
            }
        }
        return result;
    }

    @Override
    public boolean remove(Object o) {
        String res;
        try {
            res = (String) o;
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Entry<String> leaf = queue.poll();

            assert leaf != null;
            if (leaf.leftChild != null) {
                if (leaf.leftChild.elementName.contains(res)){
                    leaf.leftChild = null;
                    leaf.availableToAddLeftChildren = true;
                    return true;
                }
                queue.offer(leaf.leftChild);
            }
            if (leaf.rightChild != null) {
                if (leaf.rightChild.elementName.contains(res)){
                    leaf.rightChild = null;
                    leaf.availableToAddRightChildren = true;
                    return true;
                }
                queue.offer(leaf.rightChild);
            }
        }
        return false;
    }

    public String getParent(String s){
        Queue<Entry<String>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Entry<String> leaf = queue.poll();

            assert leaf != null;
            if (leaf.leftChild != null) {
                if (leaf.leftChild.elementName.contains(s)){
                    return leaf.elementName;
                }
                queue.offer(leaf.leftChild);
            }
            if (leaf.rightChild != null) {
                if (leaf.rightChild.elementName.contains(s)){
                    return leaf.elementName;
                }
                queue.offer(leaf.rightChild);
            }
        }
        return null;
    }


    @Override
    public String get(int index) {throw new UnsupportedOperationException(); }
    @Override
    public String set(int index, String element) {throw new UnsupportedOperationException(); }
    @Override
    public void add(int index, String element) {throw new UnsupportedOperationException();}
    @Override
    public String remove(int index){throw new UnsupportedOperationException();}
    @Override
    public List<String> subList(int fromIndex, int toIndex){throw new UnsupportedOperationException();}
    @Override
    protected void removeRange(int fromIndex, int toIndex) {throw new UnsupportedOperationException(); }
    @Override
    public boolean addAll(int index, Collection<? extends String> c) {throw new UnsupportedOperationException(); }
}