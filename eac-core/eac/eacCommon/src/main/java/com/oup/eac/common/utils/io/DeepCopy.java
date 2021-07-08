package com.oup.eac.common.utils.io;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * @author harlandd
 *
 * Optimzied deep copy util
 */
public class DeepCopy {
	
	private static final Logger LOG = Logger.getLogger(DeepCopy.class);
	
	private DeepCopy() {
		
	}
	
    public static <T> T copy(T orig) {
        T obj = null;
        try {
            // Write the object out to a byte array
            FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
            in.close();
            obj = (T)in.readObject();
        } catch(Exception e) {
        	LOG.error("Problem copying object", e);
        }
        return obj;
    }
}
