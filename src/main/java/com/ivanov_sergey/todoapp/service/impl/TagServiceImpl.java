package com.ivanov_sergey.todoapp.service.impl;

import com.ivanov_sergey.todoapp.dto.TaskDTO;
import com.ivanov_sergey.todoapp.model.Tag;
import com.ivanov_sergey.todoapp.repository.TagRepository;
import com.ivanov_sergey.todoapp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Set<Tag> getTagsFromTask(TaskDTO taskDTO) {
        Set<String> strTags = taskDTO.getTags().stream().map(Tag::getName).collect(Collectors.toSet());
        Set<Tag> tags = new HashSet<>();

        strTags.forEach(tag -> {

            Optional<Tag> optionalTag = tagRepository.findByName(tag).stream().findFirst();
            if (optionalTag.isPresent()) {
                tags.add(optionalTag.get());
            } else {
                throw new RuntimeException("Error: Tag is not found.");
            }
        });
        System.out.println("tags from TagRepository = " + tags); // todo remove
        return tags;
    }
}
