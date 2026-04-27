package com.Ecom.platform.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChatBotSeervice {



    @Autowired
    VectorStore vectorStore;

    @Autowired
    ChatClient chatClient;


    public String getBotResponse(String userQuery) {
     String message= """
             You are a helpful and professional customer service assistant.
             
             A professional, friendly, and efficient e-commerce customer service chatbot.
             
             You assist customers by:
             
             - Searching and managing customer orders if an order number is provided.
             - Asnwering general e-commerce questions (shipping times, returns, refunds, product availability, payment status etc)
             - Providing clear, helpful, and polite responses to all queries.
             - Offering tracking links, order cancellation, and return help when relevent.
             
             If not enough information is given, politely ask for more details.
             
             Use the context provided below to answer the user's question:
             
             {context}
             
             User's Query: {userQuery}
             - Format all responses cleanly and professionally.
             - Do not use any formatting symbols (such as asterisks *, underscores _, or HTML tags like <b>).
             - Use plain text only.
             - When listing multiple items, use dashes (-) or numbers (1., 2., etc.)..
             - Keep each section on its own line.
             - Keep responses short, clear, and polite.
             - If the context is not sufficient, politely ask the user to rephrase more information.
             """;

        String context=fetchSemanticContext(userQuery);
        PromptTemplate template=new PromptTemplate(message);
        Prompt prompt=template.create(Map.of("userQuery",userQuery,"context",context));

      return   chatClient.prompt(prompt)
                .call().content();
    }

    private String fetchSemanticContext(String userQuery) {

        List<Document> documents=vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userQuery)
                        .topK(5)
                        .similarityThreshold(.07f)
                        .build()
        );

        StringBuilder context=new StringBuilder();
        for(Document document:documents){
            context.append(document.getFormattedContent()).append("\n");
        }
        return context.toString();
    }
}
