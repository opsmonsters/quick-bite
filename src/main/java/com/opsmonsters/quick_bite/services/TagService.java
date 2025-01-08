package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.TagDto;
import com.opsmonsters.quick_bite.models.Product;
import com.opsmonsters.quick_bite.models.Tag;
import com.opsmonsters.quick_bite.repositories.ProductRepo;
import com.opsmonsters.quick_bite.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagRepo tagRepo;

    @Autowired
    private ProductRepo productRepo;

    public TagDto createTag(TagDto dto) {
        Tag tag = new Tag();
        tag.setName(dto.getName());

        Tag savedTag = tagRepo.save(tag);

        dto.setTagId(savedTag.getTagId());
        return dto;
    }

    public List<TagDto> getAllTags() {
        return tagRepo.findAll()
                .stream()
                .map(tag -> {
                    TagDto dto = new TagDto();
                    dto.setTagId(tag.getTagId());
                    dto.setName(tag.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public TagDto addTagsToProduct(Long productId, Set<Long> tagIds) {
        Optional<Product> productOptional = productRepo.findById(productId);

        if (!productOptional.isPresent()) {
            throw new RuntimeException("Product not found");
        }

        Product product = productOptional.get();
        Set<Tag> tags = new HashSet<>();

        for (Long tagId : tagIds) {
            Optional<Tag> tagOptional = tagRepo.findById(tagId);

            if (tagOptional.isPresent()) {
                tags.add(tagOptional.get());
            } else {
                throw new RuntimeException("Tag with ID " + tagId + " not found");
            }
        }

        product.setTags(tags);
        productRepo.save(product);

        TagDto tagDto = new TagDto();
        tagDto.setTagId(null);
        return tagDto;
    }

    public void deleteTag(Long tagId) {
        tagRepo.deleteById(tagId);
    }
}
