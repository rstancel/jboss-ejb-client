package org.jboss.ejb.client.test.interceptor;

import org.jboss.ejb.client.EJBClientInvocationContext;
import org.jboss.ejb.client.EJBLocator;
import org.jboss.ejb.client.ReceiverInterceptor;
import org.jboss.ejb.client.StatelessEJBLocator;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.Serializable;

import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:rstancel@redhat.com">Radovan Stancel</a>
 */
public class ReceiverInterceptorTestCase {
    @Test
    public void testHandleInvocation() {
        ReceiverInterceptor receiverInterceptor = new ReceiverInterceptor();

        EJBClientInvocationContext ejbClientInvocationContextMock = Mockito.mock(EJBClientInvocationContext.class);

        EJBLocator locator = new StatelessEJBLocator<Serializable>(Serializable.class, "appName", "moduleName", "beanName", "distinctName");

        when(ejbClientInvocationContextMock.getLocator()).thenReturn(locator);
        when(ejbClientInvocationContextMock.getInvocationHandler)

        try {
            receiverInterceptor.handleInvocation(ejbClientInvocationContextMock);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexpected exception: " + e);
        }

    }
}
