/**
 * 
 */
package org.fabryprog.iota.mam.impl;

import org.fabryprog.iota.mam.MAMInterface;

/**
 * @author fabryprog
 *
 */
public class MAMImpl implements MAMInterface {

	private final static Integer DEPTH = 4;
	private final static Integer MIN_WEIGHT_MAGNITURE = 13;
	
	public MAMImpl() {}
		/*    
	public void send(IotaAPI client, String seed, String message) {
			// fixed Channel Key (TODO generation) 
			final Integer channelKeyIndex = 3;
			String channelKey = Converter.trytes(Encryption.hash(Encryption.increment(Crypto.converter.trits(seed.slice()))));

			//  Merkle Tree attributes (explained later)
			final Integer start = 3;
			final Integer count = 4;    // Merkle Treesize
			final Integer security = 1; //TODO

			//  Merkle Tree generation
			const tree0 = new MerkleTree(seed);
			const tree1 = new MerkleTree(seed, start + count, count, security);
			let index = 0;

			//  create bundle necessary for publishing your message to the Tangle.
			const mam = new MAM.create({
			    message: iota.utils.toTrytes(message),  // convert you raw message to trytes here.
			    merkleTree: tree0,
			    index: index,
			    nextRoot: tree1.root.hash.toString(),
			    channelKey: channelKey,
			});

			console.log("Next Key: " + mam.nextKey);

			// Send trytes（attach your Bundle to Tangle）
			iota.api.sendTrytes(mam.trytes, depth, minWeightMagnitude, (err, tx) => {
			  if (err)
			    console.log(err);
			  else
			    console.log(tx);
			});
	}
	
	
	/*
	
	
	
	
	public void getMAMRoot(String seed, String message, String sideKey, Channel channel) {
		 if (sideKey == null) {
			 sideKey = org.fabryprog.iota.mam.Constants.SIDE_KEY;
		 }   
	    
		 // MAM settings
	    int[] seedTrits = Converter.tritsString(seed);
	    int[] messageTrits = Converter.tritsString(message);
	    int[] sideKeyTrits = Converter.tritsString(sideKey);

	    final Integer nextStart = channel.getStart() + channel.getCount();
//	    const NEXT_COUNT = channel
//	    const INDEX = CHANNEL.index

	    // set up merkle tree
	    String root = iota_merkle_create(seedTrits, channel.getStart(), channel.getCount(), channel.getSecurity());
	    String nextRoot = iota_merkle_create(seedTrits, nextStart, channel.getNextCount(), channel.getSecurity());
	    String rootBranch = iota_merkle_branch(root, channel.getIndex());
	    String rootSiblings = iota_merkle_siblings(rootBranch);

	    let next_root_branch = iota_merkle_branch(next_root_merkle, INDEX)

	    let root = iota_merkle_slice(root_merkle)
	    let next_root = iota_merkle_slice(next_root_merkle)

	    let masked_payload = iota_mam_create(
	        SEED_trits,
	        MESSAGE_trits,
	        SIDE_KEY_trits,
	        root,
	        root_siblings,
	        next_root,
	        START,
	        INDEX,
	        SECURITY
	    )

	    let response = {
	        payload: ctrits_trits_to_string(masked_payload),
	        root: ctrits_trits_to_string(root),
	        next_root: ctrits_trits_to_string(next_root),
	        side_key: SIDE_KEY
	    }

	    return response

	}
	
		 */	
}
