package fr.inria.controlflow;

/**
 * Branching for a node
 */
public enum BranchKind {
    BRANCH,      // Represents a branch
    STATEMENT,   // Represents an statement
    BLOCK_BEGIN, // Represents the begining of a block
    BLOCK_END,   // Represents the end of a block
    CONVERGE,    // The exit node of all branches. Depending on the analysis it may be convenient to leave them
    EXIT ,        // EXIT node is where all return statements points to
    BEGIN         // BEGIN node is where all begins
}