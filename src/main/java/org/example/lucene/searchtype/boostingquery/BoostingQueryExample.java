//package org.example.lucene.searchtype.boostingquery;
//
//import org.apache.lucene.document.Document;
//import org.apache.lucene.index.DirectoryReader;
//import org.apache.lucene.index.Term;
//import org.apache.lucene.queries.BoostingQuery;
//import org.apache.lucene.search.*;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.FSDirectory;
//
//import java.io.IOException;
//import java.nio.file.Paths;
//
//public class BoostingQueryExample {
//
//    public static void main(String[] args) {
//        String indexPath = "/home/deep/Projects/prototypes/src/main/java/org/example/lucene/indexing/index_1";
//
//        try {
//            // Create the index directory
//            Directory directory = FSDirectory.open(Paths.get(indexPath));
//
//            // Search using BoostingQuery
//            DirectoryReader reader = DirectoryReader.open(directory);
//            IndexSearcher searcher = new IndexSearcher(reader);
//
//            // Create a TermQuery to search for documents containing "lucene" in the "content" field
//            Query termQuery = new TermQuery(new Term("content", "lucene"));
//
//            // Create a TermQuery to search for documents containing "java" in the "content" field
//            Query termQuery2 = new TermQuery(new Term("content", "java"));
//
//            // Create a BoostingQuery where termQuery is the main query and termQuery2 is the boosting query
//            BoostingQuery boostingQuery = new BoostingQuery(termQuery, termQuery2, 0.5f);
//
//            // Execute the query and get the top 10 results
//            TopDocs topDocs = searcher.search(boostingQuery, 10);
//
//            // Iterate over the search results and print the documents
//            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//                Document resultDoc = searcher.doc(scoreDoc.doc);
//                System.out.println("Title: " + resultDoc.get("title"));
//                System.out.println("Content: " + resultDoc.get("content"));
//                System.out.println();
//            }
//
//            // Close the reader and directory
//            reader.close();
//            directory.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
