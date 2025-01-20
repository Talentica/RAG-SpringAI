package com.openAi.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

public class OpenAiApi {
    @Data public static class EmbeddingList<T> {
        @JsonProperty("data")
        private List<T> data;

        // Getters and setters
    }

    @Data public static class Embedding {
        @JsonProperty("embedding")
        private List<Double> embedding;

        // Getters and setters
    }
}