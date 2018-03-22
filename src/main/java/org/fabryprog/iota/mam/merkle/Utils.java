/**
 * 
 */
package org.fabryprog.iota.mam.merkle;

import java.util.ArrayList;
import java.util.List;

import org.fabryprog.iota.iri.hash.Curl;

import jota.pow.JCurl;
import jota.pow.SpongeFactory;

/**
 * @author fabryprog
 *
 */
public class Utils {
	public static Hash combineHashes(Hash first, Hash second) {
		
		JCurl curl = new JCurl(SpongeFactory.Mode.KERL);
		
	    List subroot = new ArrayList(MerkleTree.HASH_LENGTH);
	    curl.initialize();
	    curl.absorb(first.value, 0,MerkleTree.HASH_LENGTH);
	    curl.absorb(second.value, 0, MerkleTree.HASH_LENGTH);
	    curl.squeeze(subroot, 0, MerkleTree.HASH_LENGTH);
	    return new Hash(subroot);
	}
	
}
