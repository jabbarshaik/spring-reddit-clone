package com.sample.springredditclone.controller;

import com.sample.springredditclone.dto.SubredditDto;
import com.sample.springredditclone.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
@Slf4j
public class SubredditController {


    private final SubredditService subredditService;

    @GetMapping
    public List<SubredditDto> getAllSubreddits() {
        return subredditService.getAll();
    }

    @GetMapping("/{id}")
    public SubredditDto getSubreddit(@PathVariable Long id) {
        return subredditService.getSubreddit(id);
    }

    @PostMapping
    public SubredditDto create(@RequestBody @Valid SubredditDto subredditDto) {
        return subredditService.save(subredditDto);
    }


}
