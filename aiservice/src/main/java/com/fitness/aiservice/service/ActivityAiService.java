package com.fitness.aiservice.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.http11.filters.SavedRequestInputFilter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiService {

    private final GeminiService geminiService;


    public String generateRecommendation(Activity activity){

        String prompt = createPromptForAiActivity(activity);

        String aiResponse = geminiService.getAnswer(prompt);

        log.info("Response From ai {}", aiResponse );

       processAiResposne(activity, aiResponse);

        return aiResponse;
    }

    private void processAiResposne(Activity activity, String aiResponse){
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
                    .trim();

            log.info("PARSED RESPONSE FROM AI: {}", jsonContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createPromptForAiActivity(Activity activity) {


            return String.format("""
                    Analyze the fitness activity and provide detailed recommendations in the following format
                    { 
                    "analysis":{
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
                     "description": "Detailed workout descriptio" 
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
