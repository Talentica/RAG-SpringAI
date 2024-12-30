package com.openAi.ai;

import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Service class for loading documents into the vector store.
 */
@Service
public class DataLoaderService {

    @Value("classpath:/data/Orion Customer Portal User Guide.pdf")
    private Resource pdfResource;

    @Value("classpath:/guides/*.docx")
    private Resource[] documents;

    @Autowired
    private VectorStore vectorStore;

    /**
     * Loads data from a PDF document into the vector store.
     */
    public void load() {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(this.pdfResource,
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfBottomTextLinesToDelete(3)
                                .withNumberOfTopPagesToSkipBeforeDelete(1)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        var tokenTextSplitter = new TokenTextSplitter();
        this.vectorStore.accept(tokenTextSplitter.apply(pdfReader.get()));
    }

    /**
     * Loads data from documents using Tika document reader into the vector store.
     */
    public void loadTika() {
        Arrays.asList(this.documents).forEach(document -> {
            TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(document);
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter(1000, 400, 10, 5000, true);
            var documentList = tikaDocumentReader.get();
            documentList.forEach(document1 ->
                    document1.getMetadata().put("filename", document.getFilename())
            );
            this.vectorStore.accept(tokenTextSplitter.apply(documentList));
        });
    }
}