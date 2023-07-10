package org.example.lucene.searchtype.fieldbased;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class AdvancedFieldBasedSearchExample {

    public static void main(String[] args) {
        String indexPath = "/home/deep/Projects/prototypes/src/main/java/org/example/lucene/indexing/index_0";

        // Create an instance of StandardAnalyzer
        StandardAnalyzer analyzer = new StandardAnalyzer();

        try {
            // Create the index directory
            Directory directory = FSDirectory.open(Paths.get(indexPath));

            // Search based on fields with conditions and different operators
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            // Define the fields to search in
            String[] fields = {"title", "content"};

            // Create a BooleanQuery with conditions using different operators
            BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
            booleanQuery.add(new TermQuery(new Term("title", "lucene")), BooleanClause.Occur.MUST); // Matches documents with "lucene" in the "title" field
            booleanQuery.add(new TermQuery(new Term("content", "search")), BooleanClause.Occur.MUST); // Matches documents with "java" in the "content" field

            // Create the MultiFieldQueryParser with the fields and analyzer
            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);

            // Parse the query string using the MultiFieldQueryParser
            Query query = queryParser.parse("full-text AND information"); // Matches documents with "full-text" or "information" in the specified fields

            // Combine the queries using a BooleanQuery with the desired operator
            BooleanQuery.Builder combinedQuery = new BooleanQuery.Builder();
            combinedQuery.add(booleanQuery.build(), BooleanClause.Occur.MUST);
            combinedQuery.add(query, BooleanClause.Occur.SHOULD); // Matches documents that satisfy the previous conditions AND contain "full-text" or "information"

            // Execute the query and get the top 10 results
            TopDocs topDocs = searcher.search(combinedQuery.build(), 10);

            // Iterate over the search results and print the documents
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document resultDoc = searcher.doc(scoreDoc.doc);
                System.out.println("Title: " + resultDoc.get("title"));
                System.out.println("Content: " + resultDoc.get("content"));
                System.out.println();
            }

            // Close the reader and directory
            reader.close();
            directory.close();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
