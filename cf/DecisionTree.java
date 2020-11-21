package cf;

import java.util.ArrayList;
import java.util.LinkedList;

class DecisionNode
{
    Board state;
	Piece turn;		// Who placed last piece
	int move;		// Which column was last placed
	ArrayList<DecisionNode> subsequent;
	
	DecisionNode(Board state, Piece turn, int move)
	{
		this.state = state;
		this.turn = turn;
		this.move = move;
		subsequent = new ArrayList<DecisionNode>(7);
	}
}

public class DecisionTree
{
	DecisionNode head;
	int foresight = 100000;
	Piece myColor = Piece.BLACK;
	
	public DecisionTree()
	{
		head = new DecisionNode(new Board(), myColor, 0);   // myColor is "dealer"
		fillTree();
	}
	
	// User access
	public void print()
	{
		head.state.print();
	}
    public boolean validCol(int col)
    {
        return head.state.availableCols()[col-1];
    }
    public boolean checkTie()
    {
        int validCols = 0;
        boolean[] cols = head.state.availableCols();
        for(int i = 0; i < 7; ++i) {
            if(cols[i]) {
                validCols++;
            }
        }
        
        return validCols == 0;
    }
    public boolean enterUserInput(int col)
	{
		return colChosen(col);
	}
	public boolean computersTurn()
	{
		return colChosen(pickBestMove(myColor));
	}
	
	// Pruning tree
	boolean colChosen(int col)
	{
		for(int i = 0; i < head.subsequent.size(); ++i) {
			if(head.subsequent.get(i).move == col) {
				head = head.subsequent.get(i);
				fillTree();
				
				if(head.subsequent.isEmpty()) {
					System.out.println("The game has ended");
					return true;
				}
				return false;
			}
		}
		
		throw new IllegalArgumentException("Tried and failed to place a piece at: " + col);
	}
	
	// Reading tree
	int pickBestMove(Piece color)
	{
		if(head.turn == color) {
			throw new IllegalArgumentException("Bad time to pick move");
		}
		
		int bestTreeIndex = 0;
		int bestTreeValue = pickBestMove(head.subsequent.get(0), color);
		int comparedTree;
		for(int i = 1; i < head.subsequent.size(); ++i) {
			comparedTree = pickBestMove(head.subsequent.get(i), color);
			if(comparedTree > bestTreeValue) {
				bestTreeValue = comparedTree;
				bestTreeIndex = i;
			}
		}
		
		return head.subsequent.get(bestTreeIndex).move;
	}
	private int pickBestMove(DecisionNode node, Piece color)
	{
		if(node.subsequent.isEmpty()) {
			return node.state.getContrastingScore(color);
		}
		
		int bestSubTree = pickBestMove(node.subsequent.get(0), color);
		int comparedTree;
		
		// If the computer just went
		if(color == node.turn) {
			for(int i = 1; i < node.subsequent.size(); ++i) {
				comparedTree = pickBestMove(node.subsequent.get(i), color);
				if(comparedTree < bestSubTree) {
					bestSubTree = comparedTree;
				}
			}
		// If it's now the computer's turn
		} else {
			for(int i = 1; i < node.subsequent.size(); ++i) {
				comparedTree = pickBestMove(node.subsequent.get(i), color);
				if(comparedTree > bestSubTree) {
					bestSubTree = comparedTree;
				}
			}
		}
		
		return bestSubTree;
	}
	
	// Creating tree
	private void fillTree()
	{
		LinkedList<DecisionNode> nodeQueue = discoverLeaves(head);
		
		int i = 0;
		DecisionNode considered;
		boolean[] availableCols;
		DecisionNode toAdd;
		while(i < foresight && !nodeQueue.isEmpty()) {
			considered = nodeQueue.poll();
			if(considered.state.hasEnded()) {
				continue;
			}
			
			availableCols = considered.state.availableCols();
			for(int j = 0; j < Board.COL_COUNT; ++j) {
				if(availableCols[j]) {
					toAdd = new DecisionNode(new Board(considered.state), considered.turn.opposite(), j+1);
					toAdd.state.dropPiece(j+1, considered.turn.opposite());
					considered.subsequent.add(toAdd);
					nodeQueue.add(toAdd);
					i++;
				}
			}
		}
	}
	private LinkedList<DecisionNode> discoverLeaves(DecisionNode node)
	{
		LinkedList<DecisionNode> toReturn = new LinkedList<DecisionNode>();
		
		if(node.subsequent.size() == 0) {
			toReturn.add(node);
			return toReturn;
		}
		
		for(DecisionNode branch: node.subsequent) {
			toReturn.addAll(discoverLeaves(branch));
		}
		return toReturn;
	}
}
