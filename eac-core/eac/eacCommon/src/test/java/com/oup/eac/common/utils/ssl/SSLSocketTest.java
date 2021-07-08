package com.oup.eac.common.utils.ssl;

/**
 * Tests SSL configuration and outputs default cipher suites. Can
 * be used to help diagnose SSL issues with 3rd parties.
 *  
 * Copyright (c) 2005 by Dr. Herong Yang
 */
import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.*;
import javax.net.ssl.*;
public class SSLSocketTest {
   public static void main(String[] args) {
      PrintStream out = System.out;
      out.println("\nDefault SSL socket factory:");
      try {
         // Generating the default SSLServerSocketFactory
         SSLServerSocketFactory ssf = (SSLServerSocketFactory)
            SSLServerSocketFactory.getDefault();
         System.out.println("\nSSLServerSocketFactory class: "
            +ssf.getClass());
         String[] dcsList = ssf.getDefaultCipherSuites();
         out.println("   Default cipher suites:");
         for (int i=0; i<dcsList.length; i++) {
            out.println("      "+dcsList[i]);
         }

         // Genearting SSLServerSocket
         SSLServerSocket ss
            = (SSLServerSocket) ssf.createServerSocket();
         System.out.println("\nSSLServerSocket class: "
            +ss.getClass());
         System.out.println("   String: "+ss.toString());

         // Generating the default SSLSocketFactory
         SSLSocketFactory sf = 
            (SSLSocketFactory) SSLSocketFactory.getDefault();
         out.println("\nSSLSocketFactory class: "
            +sf.getClass());
         dcsList = sf.getDefaultCipherSuites();
         out.println("   Default cipher suites:");
         for (int i=0; i<dcsList.length; i++) {
            out.println("      "+dcsList[i]);
         }

         // Genearting SSLSocket
         SSLSocket s
            = (SSLSocket) sf.createSocket();
         System.out.println("\nSSLSocket class: "+s.getClass());
         System.out.println("   String: "+s.toString());
      } catch (Exception e) {
         System.err.println(e.toString());
      }
   }
}
