package it.gius.pePpe.data.structures;

public class MockHashListItem {
	
	public int value;

	
	@Override
	public int hashCode() {
		return value % 3;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MockHashListItem other = (MockHashListItem) obj;
		if (value != other.value)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ""+value;
	}
	

}
