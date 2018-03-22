package org.fabryprog.iota.mam.merkle;

public interface MerkleNodeInterface {
	public Integer getSize();
	public MerkleNodeInterface getLeft();
	public MerkleNodeInterface getRight();
	public Hash getHash();
}
