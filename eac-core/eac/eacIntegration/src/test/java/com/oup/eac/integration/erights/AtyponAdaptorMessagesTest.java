package com.oup.eac.integration.erights;

import java.util.StringTokenizer;

import org.junit.Ignore;

import junit.framework.TestCase;

/**
 * Simple test for decoding messages sent/received between atypon adaptor and server. The
 * data being passed can be captured by setting log level to 9 on the adaptor.
 * 
 * @author packardi
 *
 */

public class AtyponAdaptorMessagesTest extends TestCase {

    public final void testDecodeString() {
        String msg = "00|00|00|B4|00|00|00|00|00|00|36|05|00|00|01|2E|BF|0C|8D|4A|00|00|00|00|00|00|00|29|72|65|64|69|72|65|63|74|5F|75|72|6C|3D|2F|65|61|63|2F|61|63|63|65|73|73|2E|68|74|6D|3F|64|65|6E|79|52|65|61|73|6F|6E|3D|2D|31|33|31|26|75|72|6C|3D|68|74|74|70|25|33|41|25|32|46|25|32|46|70|63|31|37|32|34|35|2E|75|6B|2E|6F|75|70|2E|63|6F|6D|25|32|46|65|61|63|53|61|6D|70|6C|65|53|69|74|65|25|32|46|62|6F|6F|6B|2D|64|65|74|61|69|6C|73|25|32|46|73|70|6D|2D|73|75|63|63|65|73|73|2D|61|64|64|6D|61|74|68|2E|68|74|6D|6C|7C|64|65|63|69|73|69|6F|6E|3D|72|65|64|69|72|65|63|74";
        
        System.out.println(AtyponAdaptorMessagesDecoder.decode(msg));
    }
    
    private static final class AtyponAdaptorMessagesDecoder {
        
        static final String decode(String msg) {
            StringTokenizer tokens = new StringTokenizer(msg,"|");
            StringBuilder sb = new StringBuilder();
            while (tokens.hasMoreElements()) {
                 
                 sb.append((char)Integer.parseInt(tokens.nextToken(),16));
            }
            return sb.toString();
        }
    }
}
