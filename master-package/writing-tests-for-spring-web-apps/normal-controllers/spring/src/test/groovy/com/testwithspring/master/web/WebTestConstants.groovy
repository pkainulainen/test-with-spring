package com.testwithspring.master.web

/**
 * This constant class contains the constants that are required by both
 * unit and integration tests which use the Spring MVC Test
 * framework.
 *
 * The idea of this class is to help us to avoid typos, avoid magic strings,
 * and help us to write tests that are easy to maintain.
 */
final class WebTestConstants {

    /**
     * This class contains the view names of the "normal" views.
     */
    static class View {

        static LOGIN = 'user/login'
    }
}
