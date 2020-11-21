package cf;

public class Board
{
	public static final int ROW_COUNT = 6;
	public static final int COL_COUNT = 7;
	
	private Piece[][] boardState; //[0][5] is bottom left
	private int redScore;
	private int blackScore;
	
	// Constructors
	public Board()
	{
		boardState = new Piece[COL_COUNT][ROW_COUNT];
		for(int i = 0; i < COL_COUNT; ++i) {
			for(int j = 0; j < ROW_COUNT; ++j) {
				boardState[i][j] = Piece.OPEN;
			}
		}
		
		redScore=0;
		blackScore=0;
	}
	public Board(Board otherBoard)
	{
		boardState = otherBoard.toPieceArray();
		redScore = otherBoard.getScore(Piece.RED);
		blackScore = otherBoard.getScore(Piece.BLACK);
	}
	
	// Accessors and mutators
	public void dropPiece(int colNum, Piece pieceColor)
	{
		if(colNum > COL_COUNT || colNum < 1) {
			throw new IllegalArgumentException("Invalid column drop at: " + colNum);
		}
		
		colNum -= 1;
		int highestPieceInCol = 5;
		while(highestPieceInCol >= 0 && boardState[colNum][highestPieceInCol] != Piece.OPEN) {
			highestPieceInCol--;
		}
		
		if(highestPieceInCol == -1) {
			throw new IllegalArgumentException("Column overflow at: " + colNum);
		} else {
			boardState[colNum][highestPieceInCol] = pieceColor;
		}
		
		scoreBoard();
	}
	public Piece[][] toPieceArray()
	{
		Piece[][] toReturn = new Piece[COL_COUNT][ROW_COUNT];
		for(int i = 0; i < COL_COUNT; ++i) {
			for(int j = 0; j < ROW_COUNT; ++j) {
				toReturn[i][j] = boardState[i][j];
			}
		}
		return toReturn;
	}
	public int getScore(Piece color)
	{
		if(color == Piece.RED) {
			return redScore;
		} else if(color == Piece.BLACK) {
			return blackScore;
		}
		throw new IllegalArgumentException("Tried to access the score for color: " + color);
	}
	public int getContrastingScore(Piece color)
	{
		if(color == Piece.RED) {
			return redScore - blackScore;
		} else if(color == Piece.BLACK) {
			return blackScore - redScore;
		}
		throw new IllegalArgumentException("Tried to access the score for color: " + color);
	}
	public boolean hasEnded() {
		return redScore == Integer.MAX_VALUE || blackScore == Integer.MAX_VALUE;
	}
	
	// Evaluators
	public boolean[] availableCols()
	{
		boolean[] available = new boolean[COL_COUNT];
		for(int i = 0; i < COL_COUNT; ++i) {
			if(boardState[i][0] == Piece.OPEN) {
				available[i] = true;
			}
		}
		
		return available;
	}
	
	// Helpers
	private void scoreBoard()
	{
		redScore = 0;
		blackScore = 0;
		
		horizontalConsecutive();
		if(redScore == Integer.MAX_VALUE || blackScore == Integer.MAX_VALUE) {
			return;
		}
		verticalConsecutive();
		if(redScore == Integer.MAX_VALUE || blackScore == Integer.MAX_VALUE) {
			return;
		}
		diagUpConsecutive();
		if(redScore == Integer.MAX_VALUE || blackScore == Integer.MAX_VALUE) {
			return;
		}
		diagDownConsecutive();
	}
		private void score(int redCount, int blackCount)
		{
			// If one of the colors has no opposition in the considered, it is worth points
			if(blackCount == 0 && redCount != 0) {
				if(redCount == 4) {
					redScore = Integer.MAX_VALUE;
					return;
				}
				redScore += Math.pow(10, redCount-1);
			} else if(blackCount != 0 && redCount == 0) {
				if(blackCount == 4) {
					blackScore = Integer.MAX_VALUE;
					return;
				}
				blackScore += Math.pow(10, blackCount-1);
			}
		}
		private void horizontalConsecutive()
		{
			int blackCount;
			int redCount;
			
			// The board is divided up into horizontal strips of 4 pieces and considered strip by strip
			for(int y = 0; y < ROW_COUNT; ++y) {
				for(int x = 0; x < COL_COUNT-3; ++x) {
					blackCount=0;
					redCount=0;
					for(int i = 0; i < 4; ++i) {
						if(boardState[x+i][y] == Piece.RED) {
							redCount++;
						} else if(boardState[x+i][y] == Piece.BLACK) {
							blackCount++;
						}
					}
					
					score(redCount, blackCount);
				}
			}
		}
		private void verticalConsecutive()
		{
			int blackCount;
			int redCount;
			
			// The board is divided up into vertical strips of 4 pieces and considered strip by strip
			for(int x = 0; x < COL_COUNT; ++x) {
				for(int y = 0; y < ROW_COUNT-3; ++y) {
					blackCount=0;
					redCount=0;
					for(int i = 0; i < 4; ++i) {
						if(boardState[x][y+i] == Piece.RED) {
							redCount++;
						} else if(boardState[x][y+i] == Piece.BLACK) {
							blackCount++;
						}
					}
					
					score(redCount, blackCount);
				}
			}
		}
		private void diagUpConsecutive()
		{
			int blackCount;
			int redCount;
			
			// The board is divided up into vertical strips of 4 pieces and considered strip by strip
			for(int x = 0; x < COL_COUNT-3; ++x) {
				for(int y = ROW_COUNT-1; y > 2; --y) {
					blackCount=0;
					redCount=0;
					for(int i = 0; i < 4; ++i) {
						if(boardState[x+i][y-i] == Piece.RED) {
							redCount++;
						} else if(boardState[x+i][y-i] == Piece.BLACK) {
							blackCount++;
						}
					}
					
					score(redCount, blackCount);
				}
			}
		}
		private void diagDownConsecutive()
		{
			int blackCount;
			int redCount;
			
			// The board is divided up into vertical strips of 4 pieces and considered strip by strip
			for(int x = 0; x < COL_COUNT-3; ++x) {
				for(int y = 0; y < ROW_COUNT-3; ++y) {
					blackCount=0;
					redCount=0;
					for(int i = 0; i < 4; ++i) {
						if(boardState[x+i][y+i] == Piece.RED) {
							redCount++;
						} else if(boardState[x+i][y+i] == Piece.BLACK) {
							blackCount++;
						}
					}
					
					score(redCount, blackCount);
				}
			}
		}
	
	// Misc.
	public void print()
	{
		for(int y = 0; y < ROW_COUNT; ++y) {
			for(int x = 0; x < COL_COUNT; ++x) {
				System.out.print(boardState[x][y] + "\t");
			}
			System.out.println("\n");
		}
	}
}