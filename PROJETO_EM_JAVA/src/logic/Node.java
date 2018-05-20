package logic;

public class Node {
	public int value;
	public int biggestChainMax;
	public int biggestChainMin;

	public Node(int value, int biggestChainMax, int biggestChainMin) {
		this.value = value;
		this.biggestChainMax = biggestChainMax;
		this.biggestChainMin = biggestChainMin;
	}

	@Deprecated
	public static void main(String[] args) {
		int i=0;

		outerloop: for (int x1 = 0; x1 < 3; x1++)
			for (int y1 = 0; y1 < 3; y1++) {

				innerloop: for (int x2 = 0; x2 < 3; x2++)
					for (int y2 = 0; y2 < 3; y2++) {
							
						if(x1 == x2 && y2 <= y1)
							continue;
						
						if(x2<x1)
							continue innerloop;

						i++;
						
						System.out.println("x1 = " + x1);
						System.out.println("y1 = " + y1);
						System.out.println("x2 = " + x2);
						System.out.println("y2 = " + y2);
						System.out.println();
					}
			}
		System.out.println("i = " + i);
	}

}
