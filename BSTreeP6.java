import java.io.*;
import java.util.*;

///////////////////////////////////////////////////////////////////////////////
class BSTNode<T>
{	T key;
	BSTNode<T> left,right;
	BSTNode( T key, BSTNode<T> left, BSTNode<T> right )
	{	this.key = key;
		this.left = left;
		this.right = right;
	}
}
///////////////////////////////////////////////////////////////////////////////////////
class Queue<T>
{	LinkedList<BSTNode<T>> queue;
	Queue() { queue =  new LinkedList<BSTNode<T>>(); }
	boolean empty() { return queue.size() == 0; }
	void enqueue( BSTNode<T>  node ) { queue.addLast( node ); }
	BSTNode<T>  dequeue() { return queue.removeFirst(); }
	// THROWS NO SUCH ELEMENT EXCEPTION IF Q EMPTY
}
////////////////////////////////////////////////////////////////////////////////
class BSTreeP6<T>
{
	private BSTNode<T> root;
	private int nodeCount;
	private boolean addAttemptWasDupe=false;

	public BSTreeP6()
	{
		nodeCount = 0;
		root=null;
	}

	@SuppressWarnings("unchecked")
	public BSTreeP6( String infileName ) throws Exception
	{
		nodeCount = 0;
		root=null;
		Scanner infile = new Scanner( new File( infileName ) );
		while ( infile.hasNext() )
			add( (T) infile.next() ); // THIS CAST RPODUCES THE WARNING
		infile.close();
	}

	// DUPES BOUNCE OFF & RETURN FALSE ELSE INCR COUNT & RETURN TRUE
	@SuppressWarnings("unchecked")
	public boolean add( T key )
	{	addAttemptWasDupe=false;
		root = addHelper( this.root, key );
		return !addAttemptWasDupe;
	}
	@SuppressWarnings("unchecked")
	private BSTNode<T> addHelper( BSTNode<T> root, T key )
	{
		if (root == null) return new BSTNode<T>(key,null,null);
		int comp = ((Comparable)key).compareTo( root.key );
		if ( comp == 0 )
			{ addAttemptWasDupe=true; return root; }
		else if (comp < 0)
			root.left = addHelper( root.left, key );
		else
			root.right = addHelper( root.right, key );

		return root;
  } // END addHelper

	public int size()
	{
		return nodeCount; // LOCAL VAR KEEPING COUNT
	}

	public int countNodes() // DYNAMIC COUNT ON THE FLY TRAVERSES TREE
	{
		return countNodes( this.root );
	}
	private int countNodes( BSTNode<T> root )
	{
		if (root==null) return 0;
		return 1 + countNodes( root.left ) + countNodes( root.right );
	}

	// INORDER TRAVERSAL REQUIRES RECURSION
	public void printInOrder()
	{
		printInOrder( this.root );
		System.out.println();
	}
	private void printInOrder( BSTNode<T> root )
	{
		if (root == null) return;
		printInOrder( root.left );
		System.out.print( root.key + " " );
		printInOrder( root.right );
	}

	public void printLevelOrder()
	{
		if (this.root == null) return;
		Queue<T> q = new Queue<T>();
		q.enqueue( this.root ); // this. just for emphasis/clarity
		while ( !q.empty() )
		{	BSTNode<T> n = q.dequeue();
			System.out.print( n.key + " " );
			if ( n.left  != null ) q.enqueue( n.left );
			if ( n.right != null ) q.enqueue( n.right );
		}
	}

  public int countLevels()
  {
    return countLevels( root );
  }
  private int countLevels( BSTNode root)
  {
    if (root==null) return 0;
    return 1 + Math.max( countLevels(root.left), countLevels(root.right) );
  }

  public int[] calcLevelCounts()
  {
    int levelCounts[] = new int[countLevels()];
    calcLevelCounts( root, levelCounts, 0 );
    return levelCounts;
  }
  private void calcLevelCounts( BSTNode root, int levelCounts[], int level )
  {
    if (root==null)return;
    ++levelCounts[level];
    calcLevelCounts( root.left, levelCounts, level+1 );
    calcLevelCounts( root.right, levelCounts, level+1 );
  }

	//////////////////////////////////////////////////////////////////////////////////////
	// # # # #   WRITE THE REMOVE METHOD AND ALL HELPERS / SUPPORTING METHODS   # # # # #

	// return true only if it finds/removes the node
	public boolean remove( T key2remove )
	{
		ArrayList<T> keys = new ArrayList<T>();
		arrayAddInOrder(root, keys);
		if (keys.size() == 0)
			return false;
        if (!contains(root, key2remove))
			return false;
		return remove(keys, key2remove);
	}
    
     boolean remove(ArrayList<T> keys, T key){
		this.root = null;
		keys.remove(key);
		addKeysInBalancedOrder(keys, 0, keys.size() - 1, this);
		return true;
	}
	void addKeysInBalancedOrder(ArrayList<T> keys, int lo, int hi, BSTreeP6<T> balancedBST){
		if (keys.size() == 0)
			return;
        int mid = (hi + lo)/2;
        balancedBST.add(keys.get(mid));
        if (lo > hi)
            return;
        addKeysInBalancedOrder(keys, lo, mid - 1, balancedBST);
        addKeysInBalancedOrder(keys, mid + 1, hi, balancedBST);        
    }
    
	void arrayAddInOrder(BSTNode<T> root, ArrayList<T> keys){
        if (root == null) 
            return;
        arrayAddInOrder( root.left, keys);
        keys.add(root.key);
        arrayAddInOrder(root.right, keys);
    }

    
	@SuppressWarnings("unchecked")
	private boolean contains(BSTNode<T> root, T key){
		int comp = ((Comparable)key).compareTo( root.key );
		if (root.key.equals(key))
			return true;
		if (comp < 0)
			return (root.left == null) ? false : contains(root.left, key);
		if (comp > 0)
			return (root.right == null) ? false : contains(root.right, key);
		return false;
	}


} // END BSTREEP6 CLASS
