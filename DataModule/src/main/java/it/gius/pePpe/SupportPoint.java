package it.gius.pePpe;

import it.gius.pePpe.data.shapes.witness.VertexIndexWitness;

import org.jbox2d.common.Vec2;

public interface SupportPoint {
	public void supportPoint(Vec2 d,Vec2 supportPoint, VertexIndexWitness witnessOut);
}
