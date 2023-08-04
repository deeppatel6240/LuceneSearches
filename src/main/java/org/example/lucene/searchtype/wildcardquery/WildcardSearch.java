package org.example.lucene.searchtype.wildcardquery;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.example.lucene.searchtype.query.IndexSearchQuery;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WildcardSearch {

    // Method to perform wildcard-based search with sorting, filtering, and highlighting using Lucene
    public List<Document> performWildcardSearch(IndexSearchQuery query) throws IOException, InvalidTokenOffsetsException {
        // Create an in-memory index (you may use a different directory if needed)
        Directory directory = FSDirectory.open(Paths.get("/home/deep/Projects/prototypes/src/main/java/org/example/lucene/indexing/index_1"));

        // Create an index writer using StandardAnalyzer
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        IndexWriter writer = new IndexWriter(directory, config);

        // Sample document 1
        Document doc1 = new Document();
        doc1.add(new TextField("field1", "apple", Field.Store.YES));
        doc1.add(new TextField("dateField", "2023-01-05 apple", Field.Store.YES));
        writer.addDocument(doc1);

        // Sample document 2
        Document doc2 = new Document();
        doc2.add(new TextField("field1", "banana", Field.Store.YES));
        doc2.add(new TextField("dateField", "2023-03-15 banana", Field.Store.YES));
        writer.addDocument(doc2);

        // Sample document 3
        Document doc3 = new Document();
        doc3.add(new TextField("field1", "orange", Field.Store.YES));
        doc3.add(new TextField("dateField", "2023-08-20 orange", Field.Store.YES));
        writer.addDocument(doc3);
        writer.addDocument(doc1);
        writer.addDocument(doc2);
        writer.addDocument(doc3);

        // Close the index writer
        writer.close();

        // Create an index reader
        IndexReader reader = DirectoryReader.open(directory);

        // Create an index searcher
        IndexSearcher searcher = new IndexSearcher(reader);

        // Create a wildcard query based on the provided query details
        WildcardQuery wildcardQuery = new WildcardQuery(new Term(query.getQuery().getQueryParams().getField(), query.getQuery().getQueryParams().getValue()));

        // Create a sort field (if sort details are provided)
        Sort sort = null;
        if (query.getSort() != null && query.getSort().getField() != null && query.getSort().getOrder() != null) {
            SortField sortField = new SortField(query.getSort().getField(), SortField.Type.STRING, "asc".equalsIgnoreCase(query.getSort().getOrder()));
            sort = new Sort(sortField);
        }

        // Create a filter (if filter details are provided)
        Query filterQuery = null;
        if (query.getFilter() != null && query.getFilter().getRange() != null) {
            filterQuery = TermRangeQuery.newStringRange(
                    query.getFilter().getRange().getDateField(),
                    query.getFilter().getRange().getGte(),
                    query.getFilter().getRange().getLte(),
                    true,
                    true
            );
        }

        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        booleanQueryBuilder.add(new BooleanClause(wildcardQuery, BooleanClause.Occur.MUST)); // Must match the wildcard query
        if (filterQuery != null) {
            booleanQueryBuilder.add(new BooleanClause(filterQuery, BooleanClause.Occur.FILTER)); // Filter based on the range
        }
        Query finalQuery = booleanQueryBuilder.build();

        // Perform the search with sorting, filtering, and highlighting
        int searchSize = query.getSize();
        TopDocs topDocs;
        if (sort != null || filterQuery != null) {
            // If sorting or filtering is required, use a custom collector
            // TopFieldCollector collector = TopFieldCollector.create(sort, searchSize, true, true, true, true);
            topDocs = searcher.search(finalQuery, searchSize, sort);
        } else {
            // Otherwise, perform a simple search
            topDocs = searcher.search(finalQuery, searchSize);
        }

        // Create a highlighter (if highlight details are provided)
        Highlighter highlighter = null;
        if (query.getHighlight() != null && query.getHighlight().getFields() != null) {
            QueryScorer scorer = new QueryScorer(wildcardQuery);
            highlighter = new Highlighter(new SimpleHTMLFormatter(), scorer);
        }

        // Collect the search results with or without highlighting
        List<Document> results = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document resultDoc = searcher.doc(scoreDoc.doc);

            // Apply highlighting (if enabled)
            if (highlighter != null) {
                for (String field : query.getHighlight().getFields()) {
                    String fieldValue = resultDoc.get(field);
                    String highlightedValue = highlighter.getBestFragment(new StandardAnalyzer(), field, fieldValue);
                    resultDoc.removeFields(field);
                    resultDoc.add(new TextField(field, highlightedValue, Field.Store.YES));
                }
            }

            results.add(resultDoc);
        }

        // Close the index reader
        reader.close();

        return results;
    }

    public static void main(String[] args) {
        // Example usage of WildcardSearch with an IndexSearchQuery
        IndexSearchQuery indexSearchQuery = new IndexSearchQuery();
        IndexSearchQuery.QueryDetails queryDetails = new IndexSearchQuery.QueryDetails();
        IndexSearchQuery.WildcardQuery wildcardQuery = new IndexSearchQuery.WildcardQuery();
        wildcardQuery.setField("field1");
        wildcardQuery.setValue("a*");
        queryDetails.setType("wildcard");
        queryDetails.setQueryParams(wildcardQuery);
        indexSearchQuery.setQuery(queryDetails);
        indexSearchQuery.setSize(10);

        // Add SortDetails (sorting by field1 in ascending order)
        IndexSearchQuery.SortDetails sortDetails = new IndexSearchQuery.SortDetails();
        sortDetails.setField("field1");
        sortDetails.setOrder("asc");
        indexSearchQuery.setSort(null);

        // Add FilterDetails (filtering by dateField between '2023-01-01' and '2023-07-01')
        IndexSearchQuery.FilterDetails filterDetails = new IndexSearchQuery.FilterDetails();
        IndexSearchQuery.Range range = new IndexSearchQuery.Range();
        range.setDateField("dateField"); // Replace 'dateField' with your actual date field name
        range.setGte("2023-01-01");
        range.setLte("2023-07-01");
        filterDetails.setRange(range);
        indexSearchQuery.setFilter(null);

        // Add HighlightDetails (highlighting 'field1')
        IndexSearchQuery.HighlightDetails highlightDetails = new IndexSearchQuery.HighlightDetails();
        highlightDetails.setFields(new String[]{"field1", "dateField"});
        indexSearchQuery.setHighlight(highlightDetails);

        try {
            WildcardSearch wildcardSearch = new WildcardSearch();
            List<Document> searchResults = wildcardSearch.performWildcardSearch(indexSearchQuery);

            // Print search results (you can do whatever you want with the results)
            for (Document result : searchResults) {
                System.out.println("field1: " + result.get("field1"));
                System.out.println("dateField: " + result.get("dateField"));
                System.out.println("---------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
