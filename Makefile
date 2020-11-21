.SUFFIXES: .java .class

.java.class:
	javac $*.java

all: cf.jar ConnectFour.class
	java ConnectFour

cf.jar: cf/Board.class cf/DecisionTree.class cf/Piece.class
	jar cf cf.jar cf/*.class
