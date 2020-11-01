package com.testwithspring.master.dbunitdatatypes;

public final class Products {

    /**
     * Prevents instantiation.
     */
    private Products() {}

    public static final Long UNKNOWN_ID = 99L;

    public static class Product {

        public static final Long ID = 1L;
        public static final String DESCRIPTION = "Product description";
        public static final String NAME = "Product name";

        public static class Review {

            public static final Long ID = 1L;
            public static final String AUTHOR = "Petri Kainulainen";
            public static final int SCORE = 5;
            public static final String TEXT = "Review";
        }

        public static class Tags {

            public static final String TAG_ONE = "example";
            public static final String TAG_TWO = "product";
        }
    }
}
