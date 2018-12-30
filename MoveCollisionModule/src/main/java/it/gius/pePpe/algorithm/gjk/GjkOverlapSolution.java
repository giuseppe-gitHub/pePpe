package it.gius.pePpe.algorithm.gjk;

public class GjkOverlapSolution {
	
	public Simplex simplexSolution = null;
		
	public int iterations = 0;
	
	public GjkOverlapSolution() {
		simplexSolution = new Simplex(Simplex.LARGER_2D_SIMPLEX);
	}

}
