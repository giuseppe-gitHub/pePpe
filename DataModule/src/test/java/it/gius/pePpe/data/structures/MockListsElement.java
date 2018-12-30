package it.gius.pePpe.data.structures;

import it.gius.data.structures.IdGetSet;
import it.gius.data.structures.IdNode;


public class MockListsElement extends IdNode implements IdGetSet {


	public String text;

	public MockListsElement(String text) {
		this.text = text;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MockListsElement other = (MockListsElement) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return text;
	}

	@Override
	public void setId(short id) {
		this.id = id;
	}

	@Override
	public short getId() {

		return id;
	}

}
