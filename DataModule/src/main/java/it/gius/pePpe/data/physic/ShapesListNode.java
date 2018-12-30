package it.gius.pePpe.data.physic;

import it.gius.data.structures.IdNode;
import it.gius.pePpe.data.aabb.AABoundaryBox;

/**
 * 
 * @author giuseppe
 * @opt all
 * @has 1 - 1 it.gius.pePpe.data.aabb.AABoundaryBox
 * @has 1 - 1 it.gius.pePpe.data.shapes.Shape
 */
@Deprecated
public class ShapesListNode extends IdNode {
	
	ShapesListNode() {
	}
	
	public AABoundaryBox box;
	public Bind bind;
	public Object otherData;
	

}
