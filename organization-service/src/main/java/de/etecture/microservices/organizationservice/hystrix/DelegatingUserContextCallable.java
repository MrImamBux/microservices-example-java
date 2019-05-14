package de.etecture.microservices.organizationservice.hystrix;

import de.etecture.microservices.organizationservice.utils.UserContext;
import de.etecture.microservices.organizationservice.utils.UserContextHolder;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;

public final class DelegatingUserContextCallable<V> implements Callable<V> {

    private final Callable<V> delegate;
    private UserContext originalUserContext;

    public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext) {
        Assert.notNull(delegate, "delegate cannot be null");
        Assert.notNull(userContext, "userContext cannot be null");
        this.delegate = delegate;
        this.originalUserContext = userContext;
    }

    public DelegatingUserContextCallable(Callable<V> delegate) {
        this(delegate, UserContextHolder.getContext());
    }

    /**
     * This method is invoked before the method protected by @HytrixCommand annotation.
     * @return
     * @throws Exception
     */
    @Override
    public V call() throws Exception {
        UserContextHolder.setContext(originalUserContext);

        try {
            // once the UserContext is set, invoke the call() method on the Hystrix protected method; for instance, your getLicenseByOrganization() method.
            return delegate.call();
        } finally {
            this.originalUserContext = null;
        }
    }

    public static <V> Callable<V> create(Callable<V> delegate, UserContext userContext) {
        return new DelegatingUserContextCallable<V>(delegate, userContext);
    }
}
