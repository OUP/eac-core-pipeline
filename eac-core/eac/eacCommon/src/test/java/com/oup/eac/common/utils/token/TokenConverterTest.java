package com.oup.eac.common.utils.token;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenConverterTest {

    private static final double DOUBLE = 777;
    private static final float FLOAT = 4444;
    private static final int INT = 11;
    private static final long LONG = 8888;

    /**
     * Test encrypting a token.
     */
    @Test
    public final void testEncrypt() {
        DummyToken token = new DummyToken();
        token.setLicenseId("1234rrr6");
        token.setUrl("http://oup.com/elt/books.asp?123=ii&jjj=p");
        token.setUserId("user444555");

        String testString;
        try {
            testString = TokenConverter.encrypt(token);
            assertNotNull(testString);
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test decrypting a token.
     */
    @Test
    public final void testDecrypt() {
        DummyToken token = new DummyToken();
        token.setLicenseId("1234rrr6");
        token.setUrl("http://oup.com/elt/books.asp?123=ii&jjj=p");
        token.setUserId("user444555");
        token.setLongId(Long.valueOf(LONG));

        String testString;
        try {
            testString = TokenConverter.encrypt(token);
            assertNotNull(testString);

            DummyToken decryptToken = new DummyToken();
            decryptToken = (DummyToken) TokenConverter.decrypt(testString, decryptToken);

            assertEquals(token.getLicenseId(), decryptToken.getLicenseId());
            assertEquals(token.getUrl(), decryptToken.getUrl());
            assertEquals(token.getUserId(), decryptToken.getUserId());
            assertEquals(token.getLongId(), decryptToken.getLongId());

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test encrypting null bean value.
     */
    @Test
    public final void testEncryptNullBeanValue() {
        DummyToken token = new DummyToken();
        token.setLicenseId(null);
        token.setUrl("http://oup.com/elt/books.asp?123=ii&jjj=p");
        token.setUserId("user444555");

        String testString;
        try {
            testString = TokenConverter.encrypt(token);
            assertNotNull(testString);
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test decrypting null bean value.
     */
    @Test
    public final void testDecryptNullBeanValue() {
        DummyToken token = new DummyToken();
        token.setLicenseId(null);
        token.setUrl("http://oup.com/elt/books.asp?123=ii&jjj=p");
        token.setUserId("user444555");

        String testString;
        try {
            testString = TokenConverter.encrypt(token);
            assertNotNull(testString);

            DummyToken decryptToken = new DummyToken();
            decryptToken = (DummyToken) TokenConverter.decrypt(testString, decryptToken);

            assertEquals(token.getLicenseId(), decryptToken.getLicenseId());
            assertEquals(token.getUrl(), decryptToken.getUrl());
            assertEquals(token.getUserId(), decryptToken.getUserId());
            assertEquals(token.getLongId(), decryptToken.getLongId());

        } catch (Exception e) {
            fail(e.toString());
        }
    }

    /**
     * Test object types being set on the token.
     */
    @Test
    public final void testObjectTypes() {
        DummyToken token = new DummyToken();
        token.setLicenseId(null);
        token.setUrl("http://oup.com/elt/books.asp?123=ii&jjj=p");
        token.setUserId("user444555");

        token.setBooleanId(Boolean.FALSE);
        token.setByteId(new Byte("123"));
        token.setCharId(new Character('C'));
        token.setDoubleId(Double.valueOf(DOUBLE));
        token.setFloatId(Float.valueOf(FLOAT));
        token.setIntId(Integer.valueOf(INT));
        token.setLongId(Long.valueOf(LONG));
        token.setShortId(new Short("1"));

        String testString;
        try {
            testString = TokenConverter.encrypt(token);
            assertNotNull(testString);

            DummyToken decryptToken = new DummyToken();
            decryptToken = (DummyToken) TokenConverter.decrypt(testString, decryptToken);

            assertEquals(token.getLicenseId(), decryptToken.getLicenseId());
            assertEquals(token.getUrl(), decryptToken.getUrl());
            assertEquals(token.getUserId(), decryptToken.getUserId());
            assertEquals(token.getLongId(), decryptToken.getLongId());

        } catch (Exception e) {
            fail(e.toString());
        }

    }
}
