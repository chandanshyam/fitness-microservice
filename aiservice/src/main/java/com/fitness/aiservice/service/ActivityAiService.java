package com.fitness.aiservice.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {

    private final GeminiService geminiService;


    public Recommendation generateRecommendation(Activity activity){

        String prompt = createPromptForAiActivity(activity);

        String aiResponse = geminiService.getAnswer(prompt);

        log.info("Response From ai {}", aiResponse );


        return   processAiResposne(activity, aiResponse);
    }


    private Recommendation processAiResposne(Activity activity, String aiResponse){
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(aiResponse);

            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent = textNode.asText()
                    .replaceAll("json\\n", "")
                    .replaceAll("\\n","")
                    .replaceAll("`","")
                    .trim();

//            log.info("PARSED RESPONSE FROM AI: {}", jsonContent);

            JsonNode analysisJson= mapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace");
            addAnalysisSection(fullAnalysis, analysisNode, "hearRate", "Heart Rate");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories Burned ");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuideLines(analysisJson.path("safety"));


            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  createDefaultRecommendation(activity);
    }

    private Recommendation createDefaultRecommendation(Activity activity)
    {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your current workout"))
                .suggestions(Collections.singletonList("Consult a fitness trainer"))
                .safety(Arrays.asList(
                        "Always warmup before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                )).createdAt(LocalDateTime.now())
                .build();

    }

    private List<String> extractSafetyGuideLines(JsonNode safetyNode) {

        List<String> safety = new ArrayList<>();

        if( safetyNode.isArray()){
            safetyNode.forEach(item -> safety.add(item.asText()));
        }

        return safety.isEmpty() ?
                Collections.singletonList(("No specific safetyGuideLines provided"))
                :safety;
    }

private List<String> extractSuggestions(JsonNode suggestionsNode) {
    List<String> suggesstions = new ArrayList<>();

    if( suggestionsNode.isArray()){
        suggestionsNode.forEach(suggestion -> {
            String workout = suggestion.path("workout").asText();
            String detail = suggestion.path("description").asText();
            suggesstions.add(String.format("%s: %s", workout, detail));

        });
    }

    return suggesstions.isEmpty() ?
            Collections.singletonList(("No specific suggestions provided"))
            :suggesstions;
}

private List<String> extractImprovements(JsonNode improvementsNode) {
    List<String> improvements = new ArrayList<>();

    if( improvementsNode.isArray()){
        improvementsNode.forEach(improvement -> {
            String area = improvement.path("area").asText();
            String detail = improvement.path("recommendation").asText();
            improvements.add(String.format("%s: %s", area, detail));

        });
    }

    return improvements.isEmpty()
            ? Collections.singletonList(("No specific Improvements provided"))
            : improvements;
}

private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix ) {
    if(!analysisNode.path(key).isMissingNode())
    {
        fullAnalysis.append("%s: ".formatted(prefix))
                .append(analysisNode.path(key).asText())
                .append("\n\n");
    }
}

private String createPromptForAiActivity(Activity activity) {


    return String.format("""
                    Analyze the fitness activity and provide detailed recommendations in the following format
                    {"analysis":{
                     "overall": "Overall analysis here",
                     "pace": "pace analysis here",
                     "hearRate": "Heart rate analysis here",
                     "caloriesBurned": "Calories analysis here"
                     },
                     "improvements": [{
                     "area": "Area Name",
                     "recommendation": "Detailed recommendation" 
                     }
                     ],
                      "suggestions": [{
                     "workout ": "Workout Name",
                     "description": "Detailed workout description" 
                     }
                     ],
                      "safety": [
                     "Safety Point 1",
                     "Safety Point 2" 
                     ]
                     }
                     Analyze this activity:
                     Activity Type: %s
                     Duration: %d minutes
                     Calories Burned: %d
                     Additional Metrics: %s
                     
                     Provide detailed analysis focusing on performance, improvements, next workout suggestions and safety Guidelines
                     Ensure the response follows the Exact JSON Format shown above.
                    """,
            activity.getType(),
            activity.getDuration(),
            activity.getCaloriesBurned(),
            activity.getAdditionalMetrics());

}

}
