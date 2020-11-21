package cf;

public enum Piece
{
	RED, BLACK, OPEN;
	
	public Piece opposite()
	{
		if (this == Piece.RED)
			return Piece.BLACK;
		if (this == Piece.BLACK)
			return Piece.RED;
		
		throw new IllegalArgumentException("Tried to find the opposite of: " + this);
	}
	
	public String toString()
	{
		if (this == Piece.RED)
			return "( RED )";
		if (this == Piece.BLACK)
			return "(BLACK)";
		if (this == Piece.OPEN)
			return "(     )";
		
		return "ERROR";
	}
}
