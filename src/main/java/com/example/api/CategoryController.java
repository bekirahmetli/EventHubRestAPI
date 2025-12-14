package com.example.api;

import com.example.business.abstracts.ICategoryService;
import com.example.entities.Category;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Category get(@PathVariable("id") Long id){
        return this.categoryService.get(id);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public Category update(@Valid @RequestBody Category category){
        categoryService.get(category.getId());
        return this.categoryService.update(category);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<Category> cursor(
            @RequestParam(name = "page",required = false,defaultValue = "0") int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "5") int pageSize
    ){
        Page<Category> categoryPage = this.categoryService.cursor(page,pageSize);
        return categoryPage;
    }
}
