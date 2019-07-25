package com.chenenyu.router.chain;


import androidx.annotation.NonNull;

import com.chenenyu.router.AptHub;
import com.chenenyu.router.MatcherRegistry;
import com.chenenyu.router.RouteInterceptor;
import com.chenenyu.router.RouteResponse;
import com.chenenyu.router.RouteStatus;
import com.chenenyu.router.matcher.AbsExplicitMatcher;

import java.util.List;

/**
 * Created by chenenyu on 2018/6/15.
 */
public class FragmentValidator implements RouteInterceptor {
    @NonNull
    @Override
    public RouteResponse intercept(Chain chain) {
        // Fragment�?�能匹�?显�?Matcher
        List<AbsExplicitMatcher> matcherList = MatcherRegistry.getExplicitMatcher();
        if (matcherList.isEmpty()) {
            return RouteResponse.assemble(RouteStatus.FAILED, "The MatcherRegistry contains no explicit matcher.");
        }
        if (AptHub.routeTable.isEmpty()) {
            return RouteResponse.assemble(RouteStatus.FAILED, "The RouteTable is empty.");
        }
        return chain.process();
    }
}
