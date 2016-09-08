package org.jboss.ejb.client.remoting;

import org.jboss.remoting3.*;
import org.jboss.remoting3.security.UserInfo;
import org.xnio.FailedIoFuture;
import org.xnio.IoFuture;
import org.xnio.OptionMap;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

public class DummyConnection implements Connection {
	private final String ENDPOINT_NAME = "Remote endpoint name";

	@Override
	public Collection<Principal> getPrincipals() {
		return null;
	}

	@Override
	public UserInfo getUserInfo() {
		return null;
	}

	@Override
	public SSLSession getSslSession() {
		return null;
	}

	@Override
	public IoFuture<Channel> openChannel(String s, OptionMap optionMap) {
		final Channel channel = new ChannelAssociationTestCase.ChannelStub();
		return new IoFuture<Channel>() {
			@Override
			public IoFuture<Channel> cancel() {
				return new FailedIoFuture<Channel>(new IOException());
			}

			@Override
			public Status getStatus() {
				return Status.DONE;
			}

			@Override
			public Status await() {
				return Status.WAITING;
			}

			@Override
			public Status await(long l, TimeUnit timeUnit) {
				return Status.WAITING;
			}

			@Override
			public Status awaitInterruptibly() throws InterruptedException {
				return Status.WAITING;
			}

			@Override
			public Status awaitInterruptibly(long l, TimeUnit timeUnit) throws InterruptedException {
				return Status.WAITING;
			}

			@Override
			public Channel get() throws IOException, CancellationException {
				return channel;
			}

			@Override
			public Channel getInterruptibly() throws IOException, InterruptedException, CancellationException {
				return channel;
			}

			@Override
			public IOException getException() throws IllegalStateException {
				return null;
			}

			@Override
			public <A> IoFuture<Channel> addNotifier(Notifier<? super Channel, A> notifier, A a) {
				return null;
			}
		};
	}

	@Override
	public String getRemoteEndpointName() {
		return ENDPOINT_NAME;
	}

	@Override
	public Endpoint getEndpoint() {
		return null;
	}

	@Override
	public Attachments getAttachments() {
		return null;
	}

	@Override
	public void close() throws IOException {
	}

	@Override
	public void awaitClosed() throws InterruptedException {
	}

	@Override
	public void awaitClosedUninterruptibly() {

	}

	@Override
	public void closeAsync() {

	}

	@Override
	public Key addCloseHandler(CloseHandler<? super Connection> closeHandler) {
		return null;
	}
}
