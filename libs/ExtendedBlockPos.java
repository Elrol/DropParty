package com.github.elrol.dropparty.libs;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import net.minecraft.util.math.BlockPos;

public class ExtendedBlockPos {

	private int x, y, z;
	private String dim;
	
	public ExtendedBlockPos(int x, int y, int z, String dim) {
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
	
	public BlockPos getBlockPos(){
		return new BlockPos(x, y, z);
	}
	
	public Location<World> getLocation(){
		return new Location<World>(Sponge.getServer().getWorld(dim).get(), x, y, z);
	}
	
}
