/**
 * 
 */
package org.fabryprog.iota.mam.merkle;

import org.fabryprog.iota.iri.utils.Converter;

/**
 * @author fabryprog
 *
 */
public class Hash {
	int[] value;
	String trytes;
	
	public Hash(int[] trits) {
	    this.value = trits;
	    
	    this.trytes = this.toString();
	}
	
	public String toString() {
		return Converter.trytes(this.value); 
	}
}
