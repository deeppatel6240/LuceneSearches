package org.example.lucene.searchtype.wildcardquery;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class LuceneWildcardSearchHighlighting {
    public static void main(String[] args) {
        String indexPath = "/home/deep/Projects/prototypes/src/main/java/org/example/lucene/indexing/index_0";

        // Create an instance of StandardAnalyzer
        StandardAnalyzer analyzer = new StandardAnalyzer();

        try {
            // Create the index directory
            Directory directory = FSDirectory.open(Paths.get(indexPath));

            // Create the index reader
            IndexReader reader = DirectoryReader.open(directory);

            // Create the index searcher
            IndexSearcher searcher = new IndexSearcher(reader);

            // Specify the search query
            String queryString = "content:lu*n*";
            QueryParser parser = new QueryParser("content", analyzer);
            Query query = parser.parse(queryString);

            // Set up the highlighter
            QueryScorer scorer = new QueryScorer(query, "content");
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style=\"font-weight: bold;\">", "</span>");
            Highlighter highlighter = new Highlighter(formatter, scorer);
            highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer, 100)); // Set the fragment size

            // Perform the search
            TopDocs topDocs = searcher.search(query, 10);

            // Process and display the search results
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                int docID = scoreDoc.doc;
                Document doc = searcher.doc(docID);

                // Get the "content" field text
                String content = doc.get("content");

                // Get the highlighted text
                TokenStream tokenStream = TokenSources.getTokenStream("content", reader.getTermVectors(docID), content, analyzer, -1);
                String highlightedText = highlighter.getBestFragments(tokenStream, content, 3, "...");

                System.out.println("ID: " + doc.get("id"));
                System.out.println("Title: " + doc.get("title"));
                System.out.println("Highlighted Content: " + highlightedText);
                System.out.println("---------------------------------------------");
            }

            // Close the reader
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
