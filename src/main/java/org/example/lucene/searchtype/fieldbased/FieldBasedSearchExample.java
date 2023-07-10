package org.example.lucene.searchtype.fieldbased;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class FieldBasedSearchExample {

    public static void main(String[] args) {
        String indexPath = "/home/deep/Projects/prototypes/src/main/java/org/example/lucene/indexing/index_0";

        String field = "title";
        String text = "introduction";

        try {
            // Create the index directory
            Directory directory = FSDirectory.open(Paths.get(indexPath));

            // Search based on fields
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            // Create a term query to search for documents with the term "lucene" in the "title" field
            Query query = new TermQuery(new Term(field, text));

            // Execute the query and get the top 10 results
            ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;

            // Iterate over the search results and print the documents
            for (ScoreDoc hit : hits) {
                Document resultDoc = searcher.doc(hit.doc);
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
