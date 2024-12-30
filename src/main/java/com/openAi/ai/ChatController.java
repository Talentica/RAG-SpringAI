package com.openAi.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling chat generation and document loading.
 */
@RestController
@RequestMapping("/spring")
public class ChatController {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;
    private final DataLoaderService dataLoaderService;

    /**
     * Constructor for ChatController.
     *
     * @param chatModel the chat model used for generating responses
     * @param vectorStore the vector store used for similarity search
     * @param dataLoaderService the service used for loading documents
     */
    public ChatController(ChatModel chatModel, VectorStore vectorStore, DataLoaderService dataLoaderService) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
        this.dataLoaderService = dataLoaderService;
    }

    /**
     * Endpoint to load documents into the vector store.
     *
     * @return a confirmation message after loading documents
     */
    @PostMapping("/load")
    public String load() {
        dataLoaderService.loadTika();
        return "loaded";
    }

    /**
     * Endpoint to generate a chat response based on user input.
     *
     * @param userInput the input query from the user
     * @return the generated response from the OpenAI model
     */
    @GetMapping("/ai")
    public String generation(@RequestParam String userInput) {
        SearchRequest request = SearchRequest.query(userInput).withSimilarityThreshold(0.6);
        List<Document> documents = vectorStore.similaritySearch(request);

        if (documents.isEmpty()) {
            return "Couldn't find information in specific context.";
        }
        return ChatClient.builder(chatModel)
                .build().prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults().withSimilarityThreshold(0.6)))
                .user(userInput)
                .call()
                .content();
    }
}