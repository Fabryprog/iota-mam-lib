/**
 * 
 */
package org.fabryprog.iota.mam.impl;

import org.fabryprog.iota.iri.utils.Converter;
import org.fabryprog.iota.mam.MAMInterface;
import org.fabryprog.iota.mam.merkle.MerkleTree;
import org.fabryprog.iota.mam.utils.CurlUtils;

import jota.IotaAPI;
import jota.model.Bundle;
import jota.utils.Signing;

/**
 * @author fabryprog
 *
 */
public class MAMImpl implements MAMInterface {

	public static final Integer HASH_LENGTH = 243;
	public static final String[] NULL_HASH_TRYTES = new String[81];

	private final static Integer DEPTH = 4;
	private final static Integer MIN_WEIGHT_MAGNITURE = 13;
	
	public MAMImpl() {
		for(int i=0; i < NULL_HASH_TRYTES.length; i++) {
			NULL_HASH_TRYTES[i] = "9";
		}
		
	}
	
	private buffer(array, value, size) {
	    int remaining = array.length % size;
	    const copy = array.slice();
	    for(int i = 0; i < remaining; i++) {
	        copy.concat(value);
	    }
	    return copy;
	}
	
	private int bufferLength(int length) {
	    return (int)(2187 - ( length - Math.floor(length/2187)*2187 ));
	}
	
	private Object sign (String message, int[] key, Bundle bundle) {
	    if(bundle == null) {
	        bundle = new Bundle();
	    }
	    int[] normalizedHash = bundle.normalizedBundle(messageHash(message)).slice(0,27);
        Signing signing = new Signing(null);
        return signing.signatureFragment(normalizedHash, key);
	}

	private Object channelKey(int[] seed, int index, int subchannel) {
	    int[] key = seed.clone();
	    int i = index;
	    CurlUtils curl = new CurlUtils(null);
	    curl.initialize(null);
	    int sub = subchannel;
	    while(i-- > 0) {
	        Encryption.increment(key, sub);
	        Encryption.hash(key, curl);
	        sub = 1;
	    }
	    return key;
	}

	
	create = function (options) {

	    const treeSize = options.merkleTree.root.size();
	    const keyIndex = options.index % treeSize;
	    const key = options.merkleTree.get(keyIndex);

	    let tree = key.tree.reduce((a, b) => {
	        var h = b.hash.value;
	        var c = a.concat(h);
	        return c;
	    }, []);

	    tree = Crypto.converter.trytes(tree).concat(Array(81).fill('9').join(''));

	    let messageTrytes = options.nextRoot.concat(options.message);
	    const treeTrytes = key.tree.toString().concat(NULL_HASH_TRYTES);
	    //var treelen = treeTrytes.length;
	    messageTrytes = messageTrytes.concat(new Array(bufferLength(messageTrytes.length + tree.length)).fill('9').join(''));


	    const transfers = [];

	    const hash = messageHash(messageTrytes);
	    const bundle = new Crypto.bundle();
	    const signature = sign(messageTrytes, key.key.key, bundle);

	    const messageOut = Crypto.converter.trytes(signature).concat(tree).concat(messageTrytes);
	    const encryptionkey = Encryption.key(options.channelKey, messageOut.length*3);
	    const cipher = Encryption.encrypt(messageOut, encryptionkey);

	    const signatureFragments = cipher.match(/.{1,2187}/g);
	    const address = messageID(options.channelKey, options.channelKeyIndex, options.channelKeyIndex);//Crypto.converter.trytes(channel);
	    const value = 0;
	    const indextrits = Crypto.converter.fromValue(options.index);
	    const indexTrytes = Crypto.converter.trytes(indextrits.concat(new Array(Math.ceil(indextrits.length/3)*3 - indextrits.length).fill(0)));
	    const tag = indexTrytes.concat('999999999999999999999999999').substring(0, 27);
	    const timestamp = Math.floor(Date.now() / 1000);

	    bundle.addEntry(signatureFragments.length, address, value, tag, timestamp);
	    bundle.finalize();
	    bundle.addTrytes(signatureFragments);
	    
	    const trytes = bundle.bundle.map((tx) => Crypto.transactionTrytes(tx));
	    const l = trytes[0].length;

	    return trytes.reverse();
	}

	const messageHash = function (message) {
	    const curl = new Crypto.curl();
	    const hash = new Int32Array(HASH_LENGTH);
	    const messageTrits = Crypto.converter.trits(message);
	    curl.initialize();
	    curl.absorb(messageTrits, 0, messageTrits.length);
	    curl.squeeze(hash, 0, hash.length);
	    return Crypto.converter.trytes(hash);
	}

	const validationHashes = function(message) {

	}

	const messageID = function (channelKey, keyIndex) {
	    const curl = new Crypto.curl();
	    const channel = new Int32Array(Crypto.converter.trits(channelKey));
	    Encryption.hash(channel);
	    Encryption.hash(channel);
	    return Crypto.converter.trytes(channel);
	}

	const reverse = s => s.split("").reverse().join("");

	const parse = function(options) {
	    const cipher = options.message.join('');
	    const key = Encryption.key(options.key, cipher.length*3);
	    const messages = Encryption.decrypt(cipher, key).match(/.{1,2187}/g);
	    const signature = messages.shift();
	    const afterSignature = messages.join('');
	    let hashes = afterSignature.match(/^(?:(?![9]{81}).{81})*(?=[9]{81})/).map( m => m.match(/.{81}/g)).shift();
	    const signedPortion = reverse(afterSignature).match(/.*(?=[9]{81})/).map(reverse).shift();
	    const nextRoot = signedPortion.substring(0,81);
	    var bundle = new Crypto.bundle();
	    const normalizedHash = bundle.normalizedBundle(messageHash(signedPortion)).slice(0, 27);
	    const signingKey = Crypto.signing.address(Crypto.signing.digest(normalizedHash, Crypto.converter.trits(signature)))
	    const root = Crypto.converter.trytes(MerkleTree.verify(signingKey, hashes.map(h => Crypto.converter.trits(h)), options.index));
	    const message = signedPortion.substring(81).match(/^(?:(?![9]{81}).)*(?=[9]*)/).shift();
	    return { root, signingKey, nextRoot, message }
	}

	
	
	public void send(IotaAPI client, String seed, String message) {
			// fixed Channel Key (TODO generation) 
			final Integer channelKeyIndex = 3;
			//TODO
//			String channelKey = Converter.trytes(Encryption.hash(Encryption.increment(Crypto.converter.trits(seed.slice()))));
			String channelKey = Converter.asciiToTrytes(seed);
			
			//  Merkle Tree attributes (explained later)
			final Integer start = 3;
			final Integer count = 4;    // Merkle Treesize
			final Integer security = 1; //TODO

			//  Merkle Tree generation
			MerkleTree tree0 = new MerkleTree(seed, start, count, security);
			MerkleTree tree1 = new MerkleTree(seed, start + count, count, security);
			int index = 0;

			//  create bundle necessary for publishing your message to the Tangle.
//			const mam = new MAM.create({
//			    message: iota.utils.toTrytes(message),  // convert you raw message to trytes here.
//			    merkleTree: tree0,
//			    index: index,
//			    nextRoot: tree1.root.hash.toString(),
//			    channelKey: channelKey,
//			});

//			console.log("Next Key: " + mam.nextKey);

			// Send trytes（attach your Bundle to Tangle）
			client.sendTrytes(arg0, arg1, arg2)
			iota.api.sendTrytes(mam.trytes, DEPTH, MIN_WEIGHT_MAGNITURE, (err, tx) => {
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
