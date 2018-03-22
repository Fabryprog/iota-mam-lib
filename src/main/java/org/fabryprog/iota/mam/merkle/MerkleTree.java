package org.fabryprog.iota.mam.merkle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.fabryprog.iota.iri.hash.Curl;
import org.fabryprog.iota.iri.hash.SpongeFactory;
import org.fabryprog.iota.iri.utils.Converter;

/**
 * Porting of javascript library: https://github.com/iotaledger/outdated-mam.client.js/blob/master/lib/merkle.js
 * 
 * @author fabryprog
 *
 */
public class MerkleTree {
	public static final Integer HASH_LENGTH = 243;

	private MerkleNodeInterface root;
	
	// Hash -> Hash
	// Key -> Key

	// combineHashes -> Utils.combineHashes

	// MerkleNode -> Node
	
	public MerkleNodeInterface computeMerkleTree(List<? extends MerkleNodeInterface> nodes, Curl curl) {
	    List<MerkleNodeInterface> subnodes = new LinkedList<MerkleNodeInterface>();
	    while (nodes.size() != 0) {
	    	subnodes.add(new Node(nodes.remove(0), nodes.remove(0), curl));
	    }
	    
	    if (subnodes.size() == 1) {
	        return subnodes.get(0);
	    }
	    return computeMerkleTree(subnodes, curl);
	}
	
	public MerkleTree(String seed, Integer start, Integer count, Integer security) throws Exception {
	    int[] seedTrits = null;
	    Converter.trits(seed, seedTrits, 0);
	    List<Key> keys = new LinkedList<Key>();
	    Integer end = start + count;
	    for (int i = start; i < end; i++) {
	        keys.add(new Key(seedTrits, i, security));
	    }
	    this.root = computeMerkleTree(keys, new Curl(SpongeFactory.Mode.KERL));
	};
	
	public Map get(int index) {
        List tree = new LinkedList();
        MerkleNodeInterface node = this.root;
        MerkleNodeInterface key = null;
        Integer size = node.getSize();
        if (index < size) {
            while (node != null) {
                if (node.getLeft() == null) {
                    key = node;
                    break;
                }
                size = node.getLeft().getSize();
                if (index < size) {
                    if (node.getRight() != null) {
                        tree.add(0, node.getRight());
                    }
                    node = node.getLeft();
                } else {
                    tree.add(0, node.getLeft());
                    node = node.getRight();
                    index -= size;
                }
            }
        }
        
//        tree.toString = function() {
//            return tree.reduce(function(a,b) {
//                return a.concat(b.hash.toString());
//            }, "");
		Map result = new HashMap();
		result.put("key", key);
		result.put("tree", tree);
		
		return result;
    }
	/*
	public boolean verify(input, tree, index, curl) {
	    if(curl == null) {
	        curl = new Crypto.curl();
	    }
	    var indexCopy = index;
	    var hash = input.slice();
	    tree.forEach(function(v, i) {
	        curl.initialize();
	        var h = Crypto.converter.trytes(v.hash.value)
	        var j = Crypto.converter.trytes(hash)
	        if(indexCopy & 1) {
	            curl.absorb(v.hash.value, 0, HASH_LENGTH);
	            curl.absorb(hash, 0, HASH_LENGTH);
	        } else {
	            curl.absorb(hash, 0, HASH_LENGTH);
	            curl.absorb(v.hash.value, 0, HASH_LENGTH);
	        }
	        indexCopy >>= 1;
	        curl.squeeze(hash, 0, HASH_LENGTH);
	    });
	    return hash;
	}
    */
}
