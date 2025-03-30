package com.fsg.dscatalog.services;

import com.fsg.dscatalog.dto.CategoryDTO;
import com.fsg.dscatalog.entities.Category;
import com.fsg.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = categoryRepository.findAll();

         return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
    }
}
