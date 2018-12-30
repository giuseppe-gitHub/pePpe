package it.gius.pePpe.algorithm.gjk.shapes;


//import it.gius.pePpe.data.shapes.Shape;
import it.gius.pePpe.data.physic.Bind;

public abstract class GjkShapesFactory {

	
	
	 public static GjkSingleShape getNewGjkAlgorithmShape(Bind bind)
	 {
		 /*if(bind.shape instanceof Polygon)
			 return new GjkPolygon(bind);
		 
		 if(bind.shape instanceof Ellipse)
			 return new GjkEllipse(bind);*/
		 return new GjkSingleShape(bind);
		 
		// throw new RuntimeException();
	 }
	 

	 public static GjkSingleShape getNewGjkContainedShape(Bind bind)
	 {
		 /*if(bind.shape instanceof Polygon)
			 return new GjkPolygon(bind);
		 
		 if(bind.shape instanceof Ellipse)
			 return new GjkEllipse(bind);*/
		 
		 return new GjkSingleShape(bind);
		 
		// throw new RuntimeException();
	 }
	 

}
