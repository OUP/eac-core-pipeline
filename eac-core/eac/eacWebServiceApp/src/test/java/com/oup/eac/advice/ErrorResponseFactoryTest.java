package com.oup.eac.advice;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.oup.eac.ws.v2.binding.access.RedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;

public final class ErrorResponseFactoryTest {

    private ErrorResponseFactory factory = new ErrorResponseFactory();

    @Test
    public void testOkay1() {
        String randMsg = UUID.randomUUID().toString();
        Object response = factory.getErrorResponse(RedeemActivationCodeResponse.class, randMsg);
        Assert.assertTrue(response instanceof RedeemActivationCodeResponse);
        RedeemActivationCodeResponse resp = (RedeemActivationCodeResponse) response;
        Assert.assertEquals(StatusCode.SERVER_ERROR, resp.getErrorStatus().getStatusCode());
        Assert.assertEquals(randMsg, resp.getErrorStatus().getStatusReason());
    }

    @Test
    public void testOkay2() {
        String randMsg = UUID.randomUUID().toString();
        Object response = factory.getErrorResponse(ExampleSuitableResponse.class, randMsg);
        Assert.assertTrue(response instanceof ExampleSuitableResponse);
        ExampleSuitableResponse resp = (ExampleSuitableResponse) response;
        Assert.assertEquals(StatusCode.SERVER_ERROR, resp.getErrorStatus().getStatusCode());
        Assert.assertEquals(randMsg, resp.getErrorStatus().getStatusReason());
    }

    @Test
    public void testFailure1() {
        String randMsg = UUID.randomUUID().toString();
        Object response = factory.getErrorResponse(ExampleUnsuitableResponse1.class, randMsg);
        Assert.assertNull(response);
    }

    @Test
    public void testFailure2() {
        String randMsg = UUID.randomUUID().toString();
        Object response = factory.getErrorResponse(ExampleUnsuitableResponse2.class, randMsg);
        Assert.assertNull(response);
    }

    @Test
    public void testFailure3() {
        String randMsg = UUID.randomUUID().toString();
        Object response = factory.getErrorResponse(ExampleUnsuitableResponse3.class, randMsg);
        Assert.assertNull(response);
    }

    public static class ExampleSuitableResponse {
        private ErrorStatus ErrorStatus;

        public ErrorStatus getErrorStatus() {
            return ErrorStatus;
        }

        public void setErrorStatus(ErrorStatus ErrorStatus) {
            this.ErrorStatus = ErrorStatus;
        }
    }

    public static class ExampleUnsuitableResponse1 {
    }

    public static class ExampleUnsuitableResponse2 {
        private String ErrorStatus;

        public String getErrorStatus() {
            return ErrorStatus;
        }

        public void setErrorStatus(String ErrorStatus) {
            this.ErrorStatus = ErrorStatus;
        }
    }

    public static class ExampleUnsuitableResponse3 {
        private ErrorStatus status;

        public ErrorStatus getStatus() {
            return status;
        }

        public void setStatus(ErrorStatus status) {
            this.status = status;
        }

    }

}
