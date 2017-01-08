


//A tuple like object to use for 2-dimensional coordinates.

//Left and right are reversed for a matrix: matrix[c.gRight()][c.gLeft()]

public class CoordPair {

    private int left;
    private int right;

    public CoordPair(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int gLeft() {
        return this.left;
    }

    public int gRight(){
        return this.right;
    }

    public void sLeft(int l){
        left = l;
    }

    public void sRight(int r){
        right = r;
    }

    public boolean equals(CoordPair other){
        return (other.gLeft() == left) && (other.gRight() == right);
    }

    public boolean equals(int l, int r){
        return (left == l) && (right == r);
    }

    public String toString() {
        return "(" + left + ", " + right + ")";
    }

}
