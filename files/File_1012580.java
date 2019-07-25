public class Trie3 {

    private class Node{

        public boolean isWord;
        public Node[] next;

        public Node(boolean isWord){
            this.isWord = isWord;
            next = new Node[26];
        }

        public Node(){
            this(false);
        }
    }

    private Node root;
    private int size;

    public Trie3(){
        root = new Node();
        size = 0;
    }

    // 获得Trie中存储的�?��?数�?
    public int getSize(){
        return size;
    }

    // �?�Trie中添加一个新的�?��?word
    public void add(String word){

        Node cur = root;
        for(int i = 0 ; i < word.length() ; i ++){
            char c = word.charAt(i);
            if(cur.next[c-'a'] == null)
                cur.next[c-'a'] = new Node();
            cur = cur.next[c-'a'];
        }

        if(!cur.isWord){
            cur.isWord = true;
            size ++;
        }
    }

    // 查询�?��?word是�?�在Trie中
    public boolean contains(String word){

        Node cur = root;
        for(int i = 0 ; i < word.length() ; i ++){
            char c = word.charAt(i);
            if(cur.next[c-'a'] == null)
                return false;
            cur = cur.next[c-'a'];
        }
        return cur.isWord;
    }
}
