package org.example.lucene.searchtype.booleanquery;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class BooleanQueryExample {

    public static void main(String[] args) {
        String indexPath = "/home/deep/Projects/prototypes/src/main/java/org/example/lucene/indexing/index_0";

        try {
            // Create the index directory
            Directory directory = FSDirectory.open(Paths.get(indexPath));

            // Search using BooleanQuery
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            // Create a BooleanQuery to search for documents that contain either "lucene" or "java" in the "content" field
            TermQuery luceneQuery = new TermQuery(new Term("content", "lucene"));
            TermQuery javaQuery = new TermQuery(new Term("content", "java"));

            BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
            booleanQuery.add(luceneQuery, BooleanClause.Occur.SHOULD); // Matches documents with "lucene" in the "content" field
            booleanQuery.add(javaQuery, BooleanClause.Occur.SHOULD); // Matches documents with "java" in the "content" field

            // Execute the query and get the top 10 results
            TopDocs topDocs = searcher.search(booleanQuery.build(), 10);

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
