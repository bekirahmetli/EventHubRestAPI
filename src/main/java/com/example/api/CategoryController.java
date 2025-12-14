package com.example.api;

import com.example.business.abstracts.ICategoryService;
import com.example.entities.Category;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Category save(@Valid @RequestBody Category category){
        return this.categoryService.save(category);
    }

}
