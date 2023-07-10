package org.example.lucene.searchtype.prefixquery;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class PrefixQueryExample {

    public static void main(String[] args) {
        String indexPath = "/home/deep/Projects/prototypes/src/main/java/org/example/lucene/indexing/index_0";

        try {
            // Create the index directory
            Directory directory = FSDirectory.open(Paths.get(indexPath));

            // Search using PrefixQuery
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            // Create a PrefixQuery to search for documents with terms starting with "luc" in the "content" field
            PrefixQuery prefixQuery = new PrefixQuery(new Term("content", "infor"));

            // Execute the query and get the top 10 results
            TopDocs topDocs = searcher.search(prefixQuery, 10);

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
