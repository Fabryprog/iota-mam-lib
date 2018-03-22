/**
 * 
 */
package org.fabryprog.iota.mam.merkle;

import org.fabryprog.iota.iri.hash.Curl;
import org.fabryprog.iota.mam.utils.CurlUtils;

/**
 * @author fabryprog
 *
 */
public class Node implements MerkleNodeInterface {
	private MerkleNodeInterface left;
	private MerkleNodeInterface right;
	private Hash hash;
	
	public Node(MerkleNodeInterface left, MerkleNodeInterface right, Curl curl) {
	    this.left = left;
	    this.right = right == null ? left : right;
	    this.hash = CurlUtils.combineHashes(this.left.getHash(), this.right.getHash());
	};
	
	public Integer getSize() {
	    return this.left.getSize() + (this.right == this.left ? 0 : this.right.getSize());
	}

	public MerkleNodeInterface getLeft() {
		return left;
	}

	public MerkleNodeInterface getRight() {
		return right;
	}

	public Hash getHash() {
		return hash;
	}
}

