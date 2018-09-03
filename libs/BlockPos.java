package com.github.elrol.dropparty.libs;

public class BlockPos {

	private int x, y, z;
	private String dim;
	
	public BlockPos(int x, int y, int z, String dim) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dim = dim;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public String getDim() {
		return this.dim;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setZ(int z) {
		this.z = z;
	}
	
	public void setDim(String dim) {
		this.dim = dim;
	}
	
	public String toString() {
		return "[" + dim + "] x:" + x + ", y:" + y + ", z:" + z;
	}
	
}
