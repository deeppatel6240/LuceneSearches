package org.example.lucene.searchtype.morelikethisquery;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class MoreLikeThisQueryExample {

    public static void main(String[] args) {
        String indexPath = "/home/deep/Projects/prototypes/src/main/java/org/example/lucene/indexing/index_3";

        // Create an instance of StandardAnalyzer
        StandardAnalyzer analyzer = new StandardAnalyzer();

        try {
            // Create the index directory
            Directory directory = FSDirectory.open(Paths.get(indexPath));

            // Create the index writer configuration
            IndexWriterConfig config = new IndexWriterConfig(analyzer);

            // Set the Similarity to BM25
            config.setSimilarity(new BM25Similarity());

            // Create the index writer
            IndexWriter writer = new IndexWriter(directory, config);

            // Create a sample document
            Document doc1 = new Document();
            doc1.add(new TextField("id", "1", TextField.Store.YES));
            doc1.add(new TextField("content", "Apache Lucene is a full-text search library.", TextField.Store.YES));
            doc1.add(new TextField("title", "Introduction to Lucene", TextField.Store.YES));

            Document doc2 = new Document();
            doc2.add(new TextField("id", "2", TextField.Store.YES));
            doc2.add(new TextField("content", "It is widely used for information retrieval in Java applications.", TextField.Store.YES));
            doc2.add(new TextField("title", "Lucene in Java", TextField.Store.YES));

            // Add the documents to the index writer
            writer.addDocument(doc1);
            writer.addDocument(doc2);

            // Commit and close the index writer
            writer.commit();
            writer.close();

            System.out.println("Indexing complete.");

            // Search using MoreLikeThisQuery
            DirectoryReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            // Create an array of fields to consider for similarity calculation
            String[] fields = {"content", "title"};

            // Define the text to find similar documents for
            String likeText = "Apache Lucene is a search library";

            // Create a MoreLikeThisQuery with the specified fields and analyzer
            MoreLikeThisQuery mltQuery = new MoreLikeThisQuery(likeText, fields, analyzer, "title");

            // Set the minimum term frequency required to be considered for similarity calculation
            mltQuery.setMinTermFrequency(1);

            // Set the minimum document frequency required to be considered for similarity calculation
            mltQuery.setMinDocFreq(1);

            // Execute the query and get the top 10 results
            TopDocs topDocs = searcher.search(mltQuery, 10);

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


