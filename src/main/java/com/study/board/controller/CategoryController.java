package com.study.board.controller;

import com.study.board.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<String> showCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{category}/stores")
    public List<String> showStoresByCategory(@PathVariable String category) {
        return categoryService.getStoresByCategory(category);
    }
}