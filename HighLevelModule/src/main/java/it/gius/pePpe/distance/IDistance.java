package it.gius.pePpe.distance;

import org.jbox2d.common.Vec2;

import it.gius.data.structures.IdList;
import it.gius.pePpe.data.distance.DistanceSolution;
import it.gius.pePpe.data.distance.OverlapSolution;
import it.gius.pePpe.data.physic.BindAABBNode;

/**
 * 
 * @author giuseppe
 *
 * @opt all
 */
public interface IDistance {

	public void init(IdList<BindAABBNode> globalShapes);
	
	public void distance(short idA,short idB, DistanceSolution sol);
	
	public void distance(short id, Vec2 q, DistanceSolution sol);
	
	public boolean overlap(short idA,short idB, OverlapSolution sol);
	
	/**
	 * remove from cache
	 * @param id
	 */
	public void remove(short id);
	
	
}
