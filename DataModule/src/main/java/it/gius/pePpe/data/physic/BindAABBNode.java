package it.gius.pePpe.data.physic;

import it.gius.data.structures.IdGetSet;
import it.gius.pePpe.MathUtils;
import it.gius.pePpe.data.aabb.AABoundaryBox;

public class BindAABBNode  implements IdGetSet{


	public short id;

	@Override
	public short getId() {
		return id;
	}

	@Override
	public void setId(short id) {
		this.id = id;
		this.bind.globalId = id;
	}

	public AABoundaryBox box;
	public Bind bind;
	public Object otherData;




	@Override
	public int hashCode() {
		return MathUtils.cuttableHashCode((int)id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BindAABBNode other = (BindAABBNode) obj;
		if (bind == null) {
			if (other.bind != null)
				return false;
		} else if (!bind.equals(other.bind))
			return false;
		return true;
	}

	/*public void updateAABB()
	{
		if(box == null)
			throw new RuntimeException("AABB null during update");

		if(bind == null)
			throw new RuntimeException("Bind null during AABB update");

		bind.getAABoundaryBox(box);
	}

	public void updateEnlargeAABB()
	{
		if(box == null)
			throw new RuntimeException("AABB null during update");

		if(bind == null)
			throw new RuntimeException("Bind null during AABB update");

		bind.getAABoundaryBox(box);

		box.enlarge(SystemCostants.DEFAULT_AABB_ENLARGE);
	}*/

}
