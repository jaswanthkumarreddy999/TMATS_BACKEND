package com.TMATS_BACKEND.Controller;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class ChatbotController {

    // Simple predefined responses for farming-related questions
    private final Map<String, List<String>> responses = new HashMap<>();
    private final List<String> fallbackResponses = new ArrayList<>();
    private final Random random = new Random();

    public ChatbotController() {
        // Initialize predefined responses
        initializeResponses();
    }

    @PostMapping("/chatbot")
    public Map<String, String> handleChatbotMessage(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message").toLowerCase();
        String response = generateResponse(userMessage);
        
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("response", response);
        return responseMap;
    }

    private String generateResponse(String userMessage) {
        // Check for predefined responses
        for (Map.Entry<String, List<String>> entry : responses.entrySet()) {
            if (userMessage.contains(entry.getKey())) {
                List<String> possibleResponses = entry.getValue();
                return possibleResponses.get(random.nextInt(possibleResponses.size()));
            }
        }

        // If no matching response is found, return a fallback response
        return fallbackResponses.get(random.nextInt(fallbackResponses.size()));
    }

    private void initializeResponses() {
        // Greeting responses
        List<String> greetingResponses = new ArrayList<>();
        greetingResponses.add("Hello! How can I help with your farming today?");
        greetingResponses.add("Hi there! What farming questions do you have?");
        greetingResponses.add("Greetings! I'm here to assist with your urban farming needs.");
        responses.put("hello", greetingResponses);
        responses.put("hi", greetingResponses);
        responses.put("hey", greetingResponses);
        
        // Crop-related responses
        List<String> cropResponses = new ArrayList<>();
        cropResponses.add("For crop management, I recommend regular monitoring of soil moisture and nutrients. Would you like specific advice for a particular crop?");
        cropResponses.add("Successful crop growing starts with good soil preparation. Have you tested your soil recently?");
        cropResponses.add("To manage your crops effectively, consider implementing a crop rotation system to prevent soil depletion and reduce pests.");
        responses.put("crop", cropResponses);
        responses.put("grow", cropResponses);
        responses.put("plant", cropResponses);
        
        // Pest control responses
        List<String> pestResponses = new ArrayList<>();
        pestResponses.add("For organic pest control, consider neem oil or introducing beneficial insects like ladybugs.");
        pestResponses.add("Regular crop rotation and companion planting can help reduce pest problems naturally.");
        pestResponses.add("If you're seeing pests, first identify them correctly before treatment. Would you like to describe what you're seeing?");
        responses.put("pest", pestResponses);
        responses.put("bug", pestResponses);
        responses.put("insect", pestResponses);
        
        // Water/irrigation responses
        List<String> waterResponses = new ArrayList<>();
        waterResponses.add("For efficient irrigation, consider drip systems that deliver water directly to plant roots.");
        waterResponses.add("Most plants prefer deep, infrequent watering rather than frequent light watering.");
        waterResponses.add("Watering in the early morning reduces evaporation and fungal disease risks.");
        responses.put("water", waterResponses);
        responses.put("irrigation", waterResponses);
        responses.put("dry", waterResponses);
        
        // Soil responses
        List<String> soilResponses = new ArrayList<>();
        soilResponses.add("Healthy soil is the foundation of successful farming. Regular addition of compost helps maintain soil fertility.");
        soilResponses.add("Consider getting a soil test to understand exactly what nutrients your soil needs.");
        soilResponses.add("Cover crops like clover or rye can help improve soil structure and fertility between growing seasons.");
        responses.put("soil", soilResponses);
        responses.put("dirt", soilResponses);
        responses.put("compost", soilResponses);
        
        // Help responses
        List<String> helpResponses = new ArrayList<>();
        helpResponses.add("I can help with questions about crops, pests, watering, soil management, and general farming advice. What would you like to know?");
        helpResponses.add("Need assistance? I can provide information on crop management, pest control, irrigation, and soil health. Just ask!");
        helpResponses.add("I'm here to help with your farming questions. You can ask about specific crops, pest management, watering techniques, or soil improvement.");
        responses.put("help", helpResponses);
        responses.put("assist", helpResponses);
        responses.put("support", helpResponses);
        
        // Thank you responses
        List<String> thankResponses = new ArrayList<>();
        thankResponses.add("You're welcome! Feel free to ask if you need more help with your farming.");
        thankResponses.add("Happy to help! Let me know if you have other questions about your crops.");
        thankResponses.add("Anytime! Successful farming is all about continuous learning and adaptation.");
        responses.put("thank", thankResponses);
        responses.put("thanks", thankResponses);
        
        // Fallback responses
        fallbackResponses.add("I'm still learning about farming topics. Could you rephrase your question?");
        fallbackResponses.add("That's an interesting point. Would you like information about crop management, pest control, or watering techniques?");
        fallbackResponses.add("I don't have specific information on that yet. Would you like to know about soil health, crop rotation, or organic pest control instead?");
        fallbackResponses.add("I'm not sure about that. As your farming assistant, I can help with basic crop management, watering advice, and pest control tips.");
    }
} 