import java.util.*;
import java.util.concurrent.CountDownLatch;
public class BinarySearchTree<T extends Comparable<T>> implements Iterable<T>  {
    private String name;
    private ArrayList<T> elements;
    private ArrayList<T> sorted;
    private Node root;

    public BinarySearchTree(String initName){
        name = initName;
    }

    class Node {
        T data;
        Node left;
        Node right;

        Node(T key) {
            data = key;
        }
    }


    void createBST(){
        root = new Node(elements.get(0));

        for (int i = 1; i < elements.size(); i++){
            insertRec(root, elements.get(i));
        }
    }

    Node insertRec(Node root, T key)
    {

        /* If the tree is empty,
           return a new node */
        if (root == null) {
            root = new Node(key);
            return root;
        }

        /* Otherwise, recur down the tree */
        if (key.compareTo(root.data) < 0)
            root.left = insertRec(root.left, key);
        else if (key.compareTo(root.data) > 0)
            root.right = insertRec(root.right, key);

        /* return the (unchanged) node pointer */
        return root;
    }





    public void addAll(Collection<? extends T> col){
        elements = new ArrayList<T>(col);
        sorted = (ArrayList) elements.clone();
        Collections.sort(sorted);
        createBST();


    }


    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(){
            int i = 0;

            /**
             * Returns {@code true} if the iteration has more elements.
             * (In other words, returns {@code true} if {@link #next} would
             * return an element rather than throwing an exception.)
             *
             * @return {@code true} if the iteration has more elements
             */
            @Override
            public boolean hasNext() {
                if (i < elements.size()) return true;
                return false;
            }

            /**
             * Returns the next element in the iteration.
             *
             * @return the next element in the iteration
             * @throws NoSuchElementException if the iteration has no more elements
             */
            @Override
            public T next() {
                 if (!hasNext()) throw new NoSuchElementException();
                return sorted.get(i++);
            }
        };
    }

    public String toString(){
        return "[" + name + "] " + printHelper(root);
    }

    private String printHelper(Node root){
        if (root == null) return "";

        String str = root.data.toString();
        if (root.left != null){
            str += " L:(" + printHelper(root.left) + ")";
        }
        if (root.right != null){
            str += " R:(" + printHelper(root.right) + ")";
            //return str;
        }
        return str;
    }
    

    public static <T extends Comparable<T>> List<T> merge(List<BinarySearchTree<T>> bstlist){
        // wait() and notifyAll() in the same synchronized object?
        // a synchronized block indicates that only one thread can occur at one time.
        List<T> result = new ArrayList<>();
        int k = bstlist.size();
        List<T> min = new ArrayList<T>(k);
        for (int i = 0; i < k; i++){
            min.add(null);
        }
        // System.out.println(min.size());



        List<Thread> threads = new ArrayList<>();
        int index = 0;
        for (BinarySearchTree<T> bst : bstlist){
            Worker<T> worker = new Worker(min, bst.sorted, index);
            index++;
            threads.add(worker);
            worker.start();
        }
        
        Master<T> jee = new Master(threads, result, min, k);
        jee.start();
        while (jee.isAlive()){
           
        }
        return result;

    }


    public static void main(String... args) {
// each tree has a name, provided to its constructor
        BinarySearchTree<Integer> t1 = new BinarySearchTree<>("Oak");
// adds the elements to t1 in the order 5, 3, 0, and then 9
        t1.addAll(Arrays.asList(5, 3, 0, 9));
        BinarySearchTree<Integer> t2 = new BinarySearchTree<>("Maple");
// adds the elements to t2 in the order 9, 5, and then 10
        t2.addAll(Arrays.asList(9, 5, 10));
        System.out.println(t1); // see the expected output for exact format
        t1.forEach(System.out::println); // iteration in increasing order
        System.out.println(t2); // see the expected output for exact format
        t2.forEach(System.out::println); // iteration in increasing order
        BinarySearchTree<String> t3 = new BinarySearchTree<>("Cornucopia");
        t3.addAll(Arrays.asList("coconut", "apple", "banana", "plum", "durian",
                "no durians on this tree!", "tamarind"));
        System.out.println(t3); // see the expected output for exact format
        t3.forEach(System.out::println); // iteration in increasing order

        // NEW TEST CASES HERE:
        BinarySearchTree<Integer> test1 = new BinarySearchTree<>("1");
        test1.addAll(Arrays.asList(5, 3, 0, 9));
        BinarySearchTree<Integer> test2 = new BinarySearchTree<>("2");
        test2.addAll(Arrays.asList(1, 2));
        BinarySearchTree<Integer> test3 = new BinarySearchTree<>("3");
        test3.addAll(Arrays.asList(11, 3, 45, 1, 4));
        List<BinarySearchTree<Integer>> list = new ArrayList<>();
        list.add(test1);
        list.add(test2);
        list.add(test3);
        System.out.println(merge(list).toString());


    }
}
class Worker<T extends Comparable<T>> extends Thread{
    List<T> resource;
    List<T> values;
    int index;
    Worker(List<T> resource, List<T> values, int index){
        this.resource = resource;
        this.values = values;
        this.index = index;
    }

    public void run(){
        while (!values.isEmpty()){
            if (resource.get(index) == null){
                resource.set(index, values.remove(0));
            }
        }
    }
}

class Master<T extends Comparable<T>> extends Thread{
    List<Thread> threads;
    List<T> result;
    List<T> min;
    int k;
    Master(List<Thread> threads, List<T> result, List<T> min, int k){
        this.threads = threads;
        this.result = result;
        this.min = min;
        this.k = k;
    }

    public void run(){
        while (Collections.frequency(min, null) < k){
            ArrayList<T> nonNull = new ArrayList(min);
            nonNull.removeAll(Collections.singleton(null));
            if (nonNull.size() > 0) {
                T minimum = Collections.min(nonNull);
                result.add(minimum);
                int ind = min.indexOf(minimum);
                min.set(ind, null);
            }
            System.out.println(result.toString());
        }
    }
}