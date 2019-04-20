package finder;

import java.awt.Point;

public class NodeTuple {
	private String name;
	private int id;
	private Point coordinate;
	
	public NodeTuple(String name, int id, int x, int y) {
		setName(name);
		setId(id);
		setLocation(x, y);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return coordinate.x;
	}
	
	
	public int getY() {
		return coordinate.y;
	}
	
	public Point getCoordinate() {
		return coordinate;
	}

	public void setLocation(int x, int y) {
		this.coordinate = new Point(x, y);
	}
}
