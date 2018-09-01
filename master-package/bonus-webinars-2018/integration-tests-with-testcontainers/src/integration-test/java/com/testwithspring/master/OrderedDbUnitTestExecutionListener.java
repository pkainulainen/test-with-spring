package com.testwithspring.master;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

/**
 * This test execution listener ensures that the {@link DbUnitTestExecutionListener}
 * is invoked before the {@link org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener}.
 * This ensures that we can insert the required user account data into our database
 * before the security context is created by the {@link org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener}.
 */
public class OrderedDbUnitTestExecutionListener extends DbUnitTestExecutionListener {

    @Override
    public int getOrder() {
        return 9999;
    }
}
