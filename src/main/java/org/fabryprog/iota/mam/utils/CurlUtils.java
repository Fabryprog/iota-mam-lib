/**
 * 
 */
package org.fabryprog.iota.mam.utils;

import org.fabryprog.iota.mam.impl.MAMImpl;
import org.fabryprog.iota.mam.merkle.Hash;

/**
 * Cryptographic related functions to IOTA's Curl (sponge function)
 * 
 * @author fabryprog
 *
 */
public class CurlUtils {
	//TODO manual combine?
	
	private Integer NUMBER_OF_ROUNDS = 81;
	private Integer HASH_LENGTH = 243;
	private Integer STATE_LENGTH = 3 * HASH_LENGTH;

	private Integer rounds;
	private int[] truthTable;
	private int[] state;
	
	public CurlUtils(Integer rounds) {
		this.rounds = (rounds != null) ? rounds : NUMBER_OF_ROUNDS;
	    // truth table
	    this.truthTable = new int[] {1, 0, -1, 2, 1, -1, 0, 2, -1, 1, 0};
	}

	/**
	*   Initializes the state with STATE_LENGTH trits
	*
	*   @method initialize
	**/
	public void initialize(int[] state) {
	    if (state != null) {
	        this.state = state;
	    } else {
	        this.state = new int[STATE_LENGTH];
	        for (int i = 0; i < STATE_LENGTH; i++) {
	            this.state[i] = 0;
	        }
	    }
	}

	public void reset() {
	  this.initialize(null);
	}

	/**
	*   Sponge absorb function
	*
	*   @method absorb
	**/
	public void absorb(int[] trits, Integer offset, Integer length) {
	    do {
	        int i = 0;
	        int limit = (length < HASH_LENGTH ? length : HASH_LENGTH);

	        while (i < limit) {
	            this.state[i++] = trits[offset++];
	        }

	        this.transform();
	    } while (( length -= HASH_LENGTH ) > 0);
	}

	/**
	*   Sponge squeeze function
	*
	*   @method squeeze
	**/
	public void squeeze(int[] trits, Integer offset, Integer length) {
	    do {
	        int i = 0;
	        int limit = (length < HASH_LENGTH ? length : HASH_LENGTH);
	        while (i < limit) {
	            trits[offset++] = this.state[i++];
	        }
	        this.transform();
	    } while (( length -= HASH_LENGTH ) > 0);
	}

	/**
	*   Sponge transform function
	*
	*   @method transform
	**/
	private void transform() {
	    int[] stateCopy = new int[STATE_LENGTH];
	    int index = 0;

	    for (int round = 0; round < this.rounds; round++) {
	        stateCopy = this.state.clone();
	        
	        for (int i = 0; i < STATE_LENGTH; i++) {
	            this.state[i] = this.truthTable[stateCopy[index] + (stateCopy[index += (index < 365 ? 364 : -365)] << 2) + 5];
	        }
	    }
	}
	
	public static Hash combineHashes(Hash first, Hash second) {
		CurlUtils curlUtils = new CurlUtils(null);
		
	    int[] subroot = new int[MAMImpl.HASH_LENGTH];
	    
	    curlUtils.initialize(null);
	    curlUtils.absorb(first.value, 0,MAMImpl.HASH_LENGTH);
	    curlUtils.absorb(second.value, 0, MAMImpl.HASH_LENGTH);
	    curlUtils.squeeze(subroot, 0, MAMImpl.HASH_LENGTH);
	    
	    return new Hash(subroot);
	}
	
}
