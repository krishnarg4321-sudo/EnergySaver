package com.energysaver.controller;

import com.energysaver.dto.SuggestionDTO;
import com.energysaver.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
@CrossOrigin
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @GetMapping("/")
    public ResponseEntity<List<SuggestionDTO>> getSuggestions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<SuggestionDTO> suggestions = suggestionService.getSuggestions(username);
        return ResponseEntity.ok(suggestions);
    }
}
