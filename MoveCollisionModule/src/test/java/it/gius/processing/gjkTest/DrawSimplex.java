package it.gius.processing.gjkTest;

import it.gius.pePpe.algorithm.gjk.Simplex;
import it.gius.processing.util.MyAbstractPApplet;

@SuppressWarnings("serial")
public class DrawSimplex extends MyAbstractPApplet {

	private Simplex simplex;

	public DrawSimplex(Simplex simplex) {
		this.simplex = simplex;

	}


	@Override
	public void setup() {

		size(300,300);

		noLoop();
	}


	public void draw() {

		background(255);

		drawAxis();


		stroke(0);
		fill(0);
		for(int i=0; i< simplex.currentDim; i++)
		{

			text(""+i,width/2 + simplex.vs[i].x,height/2 + simplex.vs[i].y);
			int i1 = i;
			int i2 = i+1 < simplex.currentDim ? i+1 : 0;

			
			line(simplex.vs[i1].x + width/2,simplex.vs[i1].y + height/2,
					simplex.vs[i2].x + width/2, simplex.vs[i2].y+ height/2);
		}	
	}

	private void drawAxis()
	{
		stroke(0,0,220);
		line(0,height/2,width,height/2);
		line(width/2,0,width/2,height);
	}
}
