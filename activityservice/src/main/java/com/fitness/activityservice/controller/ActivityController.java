package com.fitness.activityservice.controller;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.service.Activityservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private Activityservice activityservice;

    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request, @RequestHeader("X-User-Id") String  userId){


        if(userId != null)
        {
            request.setUserId(userId);
        }

        return(ResponseEntity.ok(activityservice.trackActivity(request)));

    }

    @GetMapping
    public ResponseEntity<
    List<ActivityResponse>> getUserActivities(@RequestHeader("X-User-ID")String userId){

        return(ResponseEntity.ok(activityservice.findByUserId(userId)));

    }

    @GetMapping("/{activityId}")
    public ResponseEntity<List<ActivityResponse>> getActivitiesbyId(@PathVariable String  activityId){

        return(ResponseEntity.ok(activityservice.finById(activityId)));

    }
}
