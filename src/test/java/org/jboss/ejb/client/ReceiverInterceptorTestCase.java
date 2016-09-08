package org.jboss.ejb.client;

import org.jboss.ejb.client.remoting.ChannelAssociation;
import org.jboss.ejb.client.remoting.ChannelAssociationTestCase;
import org.jboss.ejb.client.remoting.DummyConnection;
import org.jboss.ejb.client.remoting.RemotingConnectionEJBReceiver;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.Connection;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

/**
 * @author <a href="mailto:rstancel@redhat.com">Radovan Stancel</a>
 */
public class ReceiverInterceptorTestCase {

    private static final String APP_NAME = "appName";
    private static final String MODULE_NAME = "moduleName";
    private static final String BEAN_NAME = "beanName";
    private static final String DISTINCT_NAME = "distinctName";

    @Test
    public void testHandleInvocation() {
        ReceiverInterceptor receiverInterceptor = new ReceiverInterceptor();

        // create mocks
        EJBReceiverInvocationContext ejbReceiverInvocationContextMock = mock(EJBReceiverInvocationContext.class);

        // create dummy variables
        Connection connection = new DummyConnection();
        Channel channel = new ChannelAssociationTestCase.ChannelStub();

        // create helped variables with some initial values
        RemotingConnectionEJBReceiver receiver = new RemotingConnectionEJBReceiver(connection);
        EJBClientContext ejbClientContext = EJBClientContext.create();
        EJBLocator locator = new StatelessEJBLocator<Serializable>(Serializable.class, APP_NAME, MODULE_NAME, BEAN_NAME,
                DISTINCT_NAME);
        EJBInvocationHandler ejbInvocationHandler = new EJBInvocationHandler(locator);
        Map<EJBReceiverContext, ChannelAssociation> channelAssociations = new IdentityHashMap<EJBReceiverContext, ChannelAssociation>();
        EJBClientInvocationContext ejbClientInvocationContext = new EJBClientInvocationContext(ejbInvocationHandler,
                ejbClientContext, null, null, null);

        Map<EJBReceiver, EJBClientContext.ReceiverAssociation> ejbReceiverAssociations = new IdentityHashMap<EJBReceiver, EJBClientContext.ReceiverAssociation>();
        EJBReceiverContext ejbReceiverContext = new EJBReceiverContext(receiver, ejbClientContext);
        ChannelAssociation channelAssociation = new ChannelAssociation(receiver, ejbReceiverContext, channel, (byte) 0x01, null,
                null);
        EJBClientContext.ReceiverAssociation receiverAssociation = new EJBClientContext.ReceiverAssociation(ejbReceiverContext);
        receiverAssociation.associated = true;
        EJBReceiver.ModuleID moduleID = new EJBReceiver.ModuleID(APP_NAME, MODULE_NAME, DISTINCT_NAME);
        Set<EJBReceiver.ModuleID> accessibleModules = Collections.synchronizedSet(new HashSet<EJBReceiver.ModuleID>());
        accessibleModules.add(moduleID);

        // setup spyies
        ChannelAssociation channelAssociationSpy = spy(channelAssociation);

        // setup variables
        channelAssociations.put(ejbReceiverContext, channelAssociationSpy);
        ejbReceiverAssociations.put(receiver, receiverAssociation);

        // overide internal states
        setInternalState(receiver, "channelAssociations", channelAssociations);
        setInternalState(ejbClientContext, "ejbReceiverAssociations", ejbReceiverAssociations);
        setInternalState(receiver, "accessibleModules", accessibleModules);

        try {
            // setup mock methods
            when(ejbReceiverInvocationContextMock.getEjbReceiverContext()).thenReturn(ejbReceiverContext);
            when(channelAssociationSpy.acquireChannelMessageOutputStream()).thenThrow(new InterruptedException());

            receiverInterceptor.handleInvocation(ejbClientInvocationContext);
            Assert.fail("Should not finish sucessfuly");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(e instanceof RequestSendFailedException);
        }
    }
}
