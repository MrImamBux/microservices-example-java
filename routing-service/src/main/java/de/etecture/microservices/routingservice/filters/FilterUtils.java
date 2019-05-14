package de.etecture.microservices.routingservice.filters;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

@Component
public class FilterUtils {

    // HTTP headers
    static final String ETECTURE_CORRELATION_ID = "etecture-correlation-id";
    static final String AUTH_TOKEN = "Authorization";

    // Filter types
    static final String PRE_FILTER_TYPE = "pre";
    static final String POST_FILTER_TYPE = "post";
    static final String ROUTE_FILTER_TYPE = "route";

    public String getCorrelationId() {
        RequestContext ctx = RequestContext.getCurrentContext();

        if (ctx.getRequest().getHeader(ETECTURE_CORRELATION_ID) != null) {
            return ctx.getRequest().getHeader(ETECTURE_CORRELATION_ID);
        } else {
            // Zuul does not put headers into the HTTP request headers while it is in the filters
            return ctx.getZuulRequestHeaders().get(ETECTURE_CORRELATION_ID);
        }
    }

    public void setCorrelationId(String correlationId) {
        RequestContext ctx = RequestContext.getCurrentContext();

        // Set headers to the Zuul Headers, not the HTTP headers. Zuul headers are then merged with HTTP request headers by itself.
        ctx.addZuulRequestHeader(ETECTURE_CORRELATION_ID, correlationId);
    }

    public final String getAuthToken() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getRequest().getHeader(AUTH_TOKEN);
    }

    public void setAuthToken(String authToken) {
        RequestContext ctx = RequestContext.getCurrentContext();

        // Set headers to the Zuul Headers, not the HTTP headers. Zuul headers are then merged with HTTP request headers by itself.
        ctx.addZuulRequestHeader(AUTH_TOKEN, authToken);
    }
}
