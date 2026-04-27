package com.Ecom.platform.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ChatClientConfig {



    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder.build();
    }


//    @Bean
//    public VectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel){
//        return  PgVectorStore.builder(jdbcTemplate,embeddingModel).build();
//    }
@Bean
@Primary
public VectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
    return PgVectorStore.builder(jdbcTemplate, embeddingModel)
            .dimensions(3072)
            .indexType(PgVectorStore.PgIndexType.NONE)
            .initializeSchema(false)
            .build();
}
}
