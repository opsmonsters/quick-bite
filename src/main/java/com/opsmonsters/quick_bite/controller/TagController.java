package com.opsmonsters.quick_bite.controller;

import com.opsmonsters.quick_bite.dto.TagDto;
import com.opsmonsters.quick_bite.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/product/tags")
public class TagController {

    @Autowired
    private TagService tagService;


    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) {
        TagDto response = tagService.createTag(tagDto);
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagDto> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }


    @PostMapping("/{productId}")
    public ResponseEntity<TagDto> addTagsToProduct(@PathVariable Long productId, @RequestBody Set<Long> tagIds) {
        TagDto response = tagService.addTagsToProduct(productId, tagIds);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }
}
