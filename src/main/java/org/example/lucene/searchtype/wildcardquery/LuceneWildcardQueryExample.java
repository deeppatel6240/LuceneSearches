package org.example.lucene.searchtype.wildcardquery;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.nio.file.Paths;

public class LuceneWildcardQueryExample {
    public static void main(String[] args) {
        try {
            // Create an index and add documents
            Directory directory = FSDirectory.open(Paths.get("/home/deep/Projects/prototypes/src/main/java/org/example/lucene/indexing/index_2"));
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            IndexWriter writer = new IndexWriter(directory, config);

            // Add some example documents
            addDocument(writer, "1", "black");
            addDocument(writer, "2", "barks");
            addDocument(writer, "3", "brown");
            addDocument(writer, "4", "babb");
            writer.close();

            // Search using wildcard query
            String queryStr = "b*";
            Query query = new WildcardQuery(new Term("title", queryStr));

            // Sort the results based on the title field in ascending order
            SortField sortField = new SortField("title", SortField.Type.DOC, false);
            Sort sort = new Sort(sortField);

            // Create an index reader
            IndexReader reader = DirectoryReader.open(directory);
            // Perform the search
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs topDocs = searcher.search(query, 10, sort);

            // Print the results
            System.out.println("Documents matching the wildcard query '" + queryStr + "':");
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                System.out.println("ID: " + doc.get("id") + ", Title: " + doc.get("title"));
            }

            searcher.getIndexReader().close();
            reader.close();
            directory.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to add a document to the index
    private static void addDocument(IndexWriter writer, String id, String title) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("id", id, Field.Store.YES));
        doc.add(new TextField("title", title, Field.Store.YES));
        writer.addDocument(doc);
    }
}
