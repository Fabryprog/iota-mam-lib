/**
 * 
 */
package org.fabryprog.iota.mam.merkle;

import jota.pow.ICurl;
import jota.utils.Signing;

/**
 * @author fabryprog
 *
 */
public class Key implements MerkleNodeInterface{

	private int[] key;
	private Hash hash;
	
	public Key(int[] seed, Integer index, Integer security) throws Exception {
        ICurl curl =  jota.pow.SpongeFactory.create(jota.pow.SpongeFactory.Mode.KERL);
        Signing signing = new Signing(curl);

	    int[] key = signing.key(seed, index, security);
	    int[] digests = signing.digests(key);
	    int[] address = signing.address(digests);
	    
	    this.key = key;
	    this.hash = new Hash(address);
	}
	
	public Integer getSize() {return 1;}

	public MerkleNodeInterface getLeft() {
		return null;
	}
	public MerkleNodeInterface getRight() {
		return null;
	}

	public Hash getHash() {
		return this.hash;
	}

	public int[] getKey() {
		return key;
	}
}
