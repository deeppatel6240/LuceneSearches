package org.example.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class IndexWriterExample {

    public static void main(String[] args) {
        String indexPath = "/home/deep/Projects/prototypes/src/main/java/org/example/lucene/indexing/index_0";

        // Create an instance of StandardAnalyzer
        StandardAnalyzer analyzer = new StandardAnalyzer();

        try {
            // Create the index directory
            Directory directory = FSDirectory.open(Paths.get(indexPath));

            // Create the index writer configuration
            IndexWriterConfig config = new IndexWriterConfig(analyzer);

            // Create the index writer
            IndexWriter writer = new IndexWriter(directory, config);

            // Create a sample document
            Document doc1 = new Document();
            doc1.add(new TextField("id", "1", Field.Store.YES));
            doc1.add(new TextField("content", "Apache Lucene is a full-text search library.", Field.Store.YES));
            doc1.add(new TextField("title", "Introduction to Lucene", Field.Store.YES));

            Document doc2 = new Document();
            doc2.add(new TextField("id", "2", Field.Store.YES));
            doc2.add(new TextField("content", "It is widely used for information retrieval in Java applications.", Field.Store.YES));
            doc2.add(new TextField("title", "Lucene in Java", Field.Store.YES));

            // Add the documents to the index writer
            writer.addDocument(doc1);
            writer.addDocument(doc2);

            // Commit and close the index writer
            writer.commit();
            writer.close();

            System.out.println("Indexing complete.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
