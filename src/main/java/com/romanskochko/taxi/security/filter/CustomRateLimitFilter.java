package com.romanskochko.taxi.security.filter;

import com.romanskochko.taxi.security.config.SecurityConfiguration;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomRateLimitFilter extends OncePerRequestFilter {
    private static final List<String> SHOULD_FILTER_LIST_URL = Arrays.stream(SecurityConfiguration.WHITE_LIST_URL)
            .map(url -> url.replace("**", ""))
            .toList();
    private static final Logger LOG = LoggerFactory.getLogger(CustomRateLimitFilter.class);

    Supplier<BucketConfiguration> bucketConfiguration;
    ProxyManager<String> proxyManager;

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
        String path = request.getRequestURI();
        return SHOULD_FILTER_LIST_URL.stream().noneMatch(path::startsWith);
    }
}
