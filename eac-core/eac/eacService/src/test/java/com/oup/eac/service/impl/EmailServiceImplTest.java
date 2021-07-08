package com.oup.eac.service.impl;

import static org.easymock.EasyMock.expect;

import java.util.Arrays;
import java.util.Date;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.service.EmailService;

/**
 * Test disabled as email sending is generally disabled during 
 * tests. If EACSettings.EMAIL_DISABLED is false, this test will pass. 
 * 
 * @author Ian Packard
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(com.oup.eac.common.utils.EACSettings.class)
public class EmailServiceImplTest {

    /**
     * Create a test.
     * 
     * @throws NamingException
     *             the exception
     */
    public EmailServiceImplTest() throws NamingException {
        super();
    }

    private JavaMailSender javaMailSender;

    private EmailService service;
    
    /**
     * @throws Exception
     *             Sets up data and create mocks ready for testing.
     */
    @Before
    public final void setUp() throws Exception {
        javaMailSender = PowerMock.createMock(JavaMailSender.class);
        PowerMock.mockStatic(EACSettings.class);
        service = new EmailServiceImpl(javaMailSender);
    }

    /**
     * Test sending mail.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public final void testSendMail() throws Exception {
        JavaMailSenderImpl impl = new JavaMailSenderImpl();
        MimeMessage msg = impl.createMimeMessage();
        MailCriteria mailCriteria = new MailCriteria();
        mailCriteria.addToAddress(new InternetAddress("address@test.com", "personal"));
        mailCriteria.setFrom(new InternetAddress("from@test.com", "from"));
        mailCriteria.setSubject("Subject");
        mailCriteria.setText("Some text");
        
        MimeMessage expected = impl.createMimeMessage();        
        expected.setContent("Some text", "plain/text");
        expected.setSentDate(new Date());
        expected.setFrom(new InternetAddress("from@test.com"));        
        expected.setRecipient(RecipientType.TO, new InternetAddress("address@test.com","personal"));
        InternetAddress replyTo = new InternetAddress("eac.admin@oup.com");
        Address[] replyToAddresses = {replyTo};
        expected.setReplyTo(replyToAddresses);
        
        //set expectations
        expect(EACSettings.getBoolProperty(EACSettings.EMAIL_DISABLED)).andReturn(Boolean.FALSE);
        expect(EACSettings.getProperty(EACSettings.EMAIL_SENT_DEBUG_FORMAT)).andReturn("EEE dd/MM/yyyy hh:mm");
        expect(EACSettings.getProperty(EACSettings.EAC_EMAIL_REPLY_TO_ADDRESS)).andReturn("eac.admin@oup.com");
        expect(javaMailSender.createMimeMessage()).andReturn(msg);

        javaMailSender.send(eqMimeMessage(expected));
        EasyMock.expectLastCall();
        
        PowerMock.replay(EACSettings.class, this.javaMailSender);
        service.sendMail(mailCriteria);
        PowerMock.verify(EACSettings.class, this.javaMailSender);
        
        
    }
    
    private MimeMessage eqMimeMessage(final MimeMessage expected) {
        IArgumentMatcher matcher = new IArgumentMatcher() {
            
                @Override
                public boolean matches(Object arg) {
                    try{
                        if(arg instanceof MimeMessage == false){
                            return false;
                        }
                        MimeMessage message = (MimeMessage)arg;
                        boolean part1 = Arrays.equals(expected.getAllRecipients(), message.getAllRecipients());
                        boolean part2 = Arrays.equals(expected.getReplyTo(), message.getReplyTo());
                        boolean part3 = Arrays.equals(expected.getFrom(), message.getFrom());
                        long sentExpected = expected.getSentDate().getTime();
                        long sentActual = message.getSentDate().getTime();
                        long diff = sentActual - sentExpected;
                        boolean part4 = diff >= 0;
                        boolean part5 = expected.getContent().equals(message.getContent());
                        boolean part6 = expected.getContentType().equals(message.getContentType());
                        //boolean result = part1 && part2 && part3 && part4 && part5 && part6;
                        boolean result = part1 && part2 && part3 && part5 && part6;
                        System.out.printf("part1 %b%n",part1);
                        System.out.printf("part2 %b%n",part2);
                        System.out.printf("part3 %b%n",part3);
                        System.out.printf("part4 expected[%d] actual[%d] diff[%d] %b%n",sentExpected, sentActual, diff, part4);
                        System.out.printf("part5 %b%n",part5);
                        System.out.printf("part6 %b%n",part6);
                        System.out.flush();
                        
                        return result;
                    }catch(Exception ex){
                        ex.printStackTrace();
                        return false;
                    }
                }

            @Override
            public void appendTo(StringBuffer out) {                
            }
        };
        EasyMock.reportMatcher(matcher);
        return null;
    } 
}
