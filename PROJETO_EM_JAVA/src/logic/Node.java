package logic;

/**
 * The Class Node.
 */
public class Node {
	
	/** The value of the node. */
	public int value;
	
	/** The maximizer biggest chain. */
	public int biggestChainMax;
	
	/** The minimizer biggest chain. */
	public int biggestChainMin;

	/**
	 * Instantiates a new node.
	 *
	 * @param value the value of the node
	 * @param biggestChainMax the maximizer biggest chain
	 * @param biggestChainMin the minimizer biggest chain
	 */
	public Node(int value, int biggestChainMax, int biggestChainMin) {
		this.value = value;
		this.biggestChainMax = biggestChainMax;
		this.biggestChainMin = biggestChainMin;
	}

}
