// Information shell which holds boolean values to represent its state.

public class Cell {
    public Cell(int L, int W) {
        // Information of visiting / opened walls
        eastOpen = false;
        northOpen = false;
        westOpen = false;
        southOpen = false;
        isVisited = false;

        // Information on location of cell for backtracking purposes
        this.L = L;
        this.W = W;
    }

    public int L;
    public int W;

    public boolean eastOpen;
    public boolean northOpen;
    public boolean westOpen;
    public boolean southOpen;
    public boolean isVisited;
}
