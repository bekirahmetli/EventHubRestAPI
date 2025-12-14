package com.example.api;

import com.example.business.abstracts.ICategoryService;
import com.example.dto.request.CategorySaveRequest;
import com.example.dto.response.CategoryResponse;
import com.example.entities.Category;
import com.example.mapper.IModelMapperService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    private final ICategoryService categoryService;
    private final IModelMapperService modelMapperService;

    public CategoryController(ICategoryService categoryService, IModelMapperService modelMapperService) {
        this.categoryService = categoryService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse save(@Valid @RequestBody CategorySaveRequest request){
        Category savedCategory = this.modelMapperService.forRequest().map(request,Category.class);
        this.categoryService.save(savedCategory);
        return this.modelMapperService.forResponse().map(savedCategory,CategoryResponse.class);
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable("id") Long id){
        Category category = this.categoryService.get(id);
        this.categoryService.delete(category.getId());
        return true;
    }
}
