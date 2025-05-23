package com.fitness.activityservice.service;

import com.fitness.activityservice.controller.ActivityController;
import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class Activityservice {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;


    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest request){



        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if(!isValidUser){
            throw new RuntimeException("Invalid User: " + request.getUserId());
        }
        Activity activity = Activity.builder().
                userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics()).build();

        Activity savedActivity = activityRepository.save(activity);

        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);
        } catch (Exception e) {
            log.error("Failed to publish activity to RabbitMQ: ", e);
        }

        return maptoResponse(savedActivity);

    }


    private ActivityResponse maptoResponse(Activity a){
        ActivityResponse response = new ActivityResponse();
        response.setId(a.getId());
        response.setUserId(a.getUserId());
        response.setType(a.getType());
        response.setDuration(a.getDuration());
        response.setCaloriesBurned(a.getCaloriesBurned());
        response.setStartTime(a.getStartTime());
        response.setAdditionalMetrics(a.getAdditionalMetrics());
        response.setCreatedAt(a.getCreatedAt());
        response.setUpdatedAt(a.getUpdatedAt());

        return response;
    }

    public List<ActivityResponse> findByUserId(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);

        return activities.stream().map(this::maptoResponse).collect(Collectors.toList());
    }

    public List<ActivityResponse> finById(String id) {
        Optional<Activity> activities = activityRepository.findById(id);

        return activities.stream().map(this::maptoResponse).collect(Collectors.toList());


    }
}
