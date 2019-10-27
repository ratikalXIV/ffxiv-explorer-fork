package com.fragmenterworks.ffxivextract.models;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.fragmenterworks.ffxivextract.Constants;
import com.fragmenterworks.ffxivextract.helpers.Utils;

public class LoDSubModel {

	final public short meshOffset;
	final public short numMeshes;
    final public int vertTableOffset;
    final public int indexTableOffset;
    final public int vertTableSize;
    final public int indexTableSize;
    
    public Mesh[] meshList;
	
	private LoDSubModel(short meshOffset, short numMeshes, int vertBuffSize, int indexBuffSize, int vertOffset, int indexOffset){
		this.meshOffset = meshOffset;
		this.numMeshes = numMeshes;
		this.vertTableOffset = vertOffset;
		this.indexTableOffset = indexOffset;
		this.vertTableSize = vertBuffSize;
		this.indexTableSize = indexBuffSize;
	}
	
	public static LoDSubModel loadInfo(ByteBuffer bb){
		
		short meshOffset = bb.getShort();
		short numMeshes = bb.getShort();    
		
        bb.position(bb.position()+0x28);
        
        int vertBuffSize = bb.getInt();
        int indexBuffSize = bb.getInt();
        int vertOffset = bb.getInt();
        int indexOffset = bb.getInt();

		Utils.getGlobalLogger().debug("Num meshes: {}", numMeshes);
		Utils.getGlobalLogger().debug("\tVert table size: {}\n\tIndex table size: {}\n\tVert table offset: {}\n\tIndex table offset: {}", vertBuffSize, indexBuffSize, vertOffset, indexOffset);
        
		return new LoDSubModel(meshOffset, numMeshes, vertBuffSize, indexBuffSize, vertOffset, indexOffset);
	}
	
	public void setMeshList(Mesh[] list)
	{
		meshList = list;
	}

	public void loadMeshes(ByteBuffer bb)  throws BufferOverflowException, BufferUnderflowException{
		if (meshList == null)
			return;
		
		for (Mesh m : meshList)
			m.loadMeshes(bb, vertTableOffset, indexTableOffset);
	}
}
