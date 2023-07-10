package org.example.lucene.searchtype.wildcardquery;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class WildcardQueryExample {

    public static void main(String[] args) {
        String indexPath = "/home/deep/Projects/prototypes/src/main/java/org/example/lucene/indexing/index_0";

        try {
            // Create the index directory
            Directory directory = FSDirectory.open(Paths.get(indexPath));

            // Search using WildcardQuery
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            // Create a WildcardQuery to search for documents with terms matching the pattern "lu*n*" in the "content" field
            WildcardQuery wildcardQuery = new WildcardQuery(new Term("content", "lu*n*"));

            // Execute the query and get the top 10 results
            TopDocs topDocs = searcher.search(wildcardQuery, 10);

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
