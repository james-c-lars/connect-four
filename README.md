# connect-four
A java implementation of a connect four AI. Never yet beaten.

Can be played through the terminal by running the makefile
Difficulty can be tweaked by editing the foresight field in the DecisionTree class

The DecisionTree class uses basic game theory to evaluate the state of many possible boards and then select which move best positions itself to get to those board states.
This method is remarkably effective given the power of modern computers.

The Board class stores the state of a connect four board. Many board states are created in order to be considered by the DecisionTree.
