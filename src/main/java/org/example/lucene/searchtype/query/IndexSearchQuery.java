package org.example.lucene.searchtype.query;

import lombok.*;

/**
 * IndexSearchQuery object has been stored into json column
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IndexSearchQuery {

    /**
     * start date for index search query
     */
    private String startDate;

    /**
     * end date for index search query
     */
    private String endDate;

    /**
     * The name of the index to search.
     */
    private String index;

    /**
     * The maximum number of search results to be returned.
     */
    private int size;

    /**
     * The sorting details for the search results.
     */
    private SortDetails sort;

    /**
     * The filter details to apply to the search.
     */
    private FilterDetails filter;

    /**
     * The highlighting details for the search results.
     */
    private HighlightDetails highlight;

    /**
     * This query is to redirect request based on it's query type
     */
    private QueryDetails query;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class SortDetails {
        /**
         * The field to sort by.
         */
        private String field;

        /**
         * The sorting order, either "asc" (ascending) or "desc" (descending).
         */
        private String order;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class FilterDetails {
        /**
         * The range filter details.
         */
        private Range range;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Range {
        /**
         * The field to apply the range filter on.
         */
        private String dateField;

        /**
         * The start date for the range filter.
         */
        private String gte;

        /**
         * The end date for the range filter.
         */
        private String lte;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class HighlightDetails {
        /**
         * The fields to highlight in the search results.
         */
        private String[] fields;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class QueryDetails {

        /**
         * The type of the query.
         */
        private String type;

        /**
         * The parameters associated with the query.
         */
        private WildcardQuery queryParams;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class WildcardQuery {

        /**
         * The type of the query.
         */
        private String field;

        /**
         * The parameters associated with the query.
         */
        private String value;

    }


}
