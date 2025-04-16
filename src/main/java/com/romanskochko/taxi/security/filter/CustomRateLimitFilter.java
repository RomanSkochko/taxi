package com.romanskochko.taxi.security.filter;

import com.romanskochko.taxi.security.config.SecurityConfiguration;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomRateLimitFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(CustomRateLimitFilter.class);

    private final Supplier<BucketConfiguration> bucketConfiguration;
    private final ProxyManager<String> proxyManager;

    private RequestMatcher whiteListMatcher;

    @PostConstruct
    public void init() {
        List<RequestMatcher> matchers = Arrays.stream(SecurityConfiguration.OPEN_API_LIST_URL)
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());
        this.whiteListMatcher = new OrRequestMatcher(matchers);
        LOG.info("CustomRateLimitFilter initialized. whiteListMatcher created.");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String key = request.getRemoteAddr();
        Bucket bucket = proxyManager.builder().build(key, bucketConfiguration);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        LOG.debug(">>>>>>>>remainingTokens: {}", probe.getRemainingTokens());
        if (probe.isConsumed()) {
            filterChain.doFilter(request, response);
        } else {
            response.setContentType("text/plain");
            response.setHeader("Retry-After", "" + TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()));
            response.setStatus(429);
            response.getWriter().append("Too many requests");
        }

    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return whiteListMatcher.matches(request);
    }
}
