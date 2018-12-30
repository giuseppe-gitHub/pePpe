package it.gius.pePpe.data.shapes;

public enum ShapeType {
	
	POLYGON("POLYGON",false),
	CIRCLE("CIRCLE",true),
	EDGE("EDGE",false);
	
	private String string;
	private boolean curve;
	
	private ShapeType(String string, boolean curve) {
		this.string = string;
		this.curve = curve;
	}
	
	public String toString() {
		return string;
	};
	
	public boolean constainsCurves()
	{
		return curve;
	}

}
