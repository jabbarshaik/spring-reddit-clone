package com.sample.springredditclone.service;


import com.sample.springredditclone.dto.SubredditDto;
import com.sample.springredditclone.exception.SpringRedditException;
import com.sample.springredditclone.mapper.SubredditMapper;
import com.sample.springredditclone.model.Subreddit;
import com.sample.springredditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.Instant.now;

@Service
@AllArgsConstructor
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final SubredditMapper subredditMapper;

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {

        return subredditRepository
                .findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) {

        Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));

        return subredditMapper.mapSubredditToDto(subreddit);


    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit1 = subredditMapper.mapDtoToSubreddit(subredditDto);
        subreddit1.setUser(authService.getCurrentUser());
        Subreddit subreddit = subredditRepository.save(subreddit1);
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }


}
