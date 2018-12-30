package it.gius.pePpe.mains;

import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.processing.gjkTest.DrawSimplex;
import it.gius.processing.util.PAppletManager;
import it.gius.processing.util.ProcessingGraphicException;

public class DrawSimplexMain {

	/**
	 * @param args
	 */
	private static Simplex simplex;

	public static void main(String[] args) {

		initSimplex();

		DrawSimplex applet = new DrawSimplex(simplex);

		PAppletManager manager = new PAppletManager(true,true);
		try {
			manager.startAndAddApplet(new String[]{""}, applet, PAppletManager.DISPOSE_ALL);
		} catch (ProcessingGraphicException e) {
			e.printStackTrace();
		}

	}


	private static void initSimplex()
	{

		simplex = new Simplex(Simplex.LARGER_2D_SIMPLEX);
		simplex.vs[0].set(-14.8f,-14);
		simplex.vs[1].set(45.1f,26);
		simplex.vs[2].set(15.1f,16);
		simplex.currentDim = Simplex.IS_2_SIMPLEX;
	}

}
