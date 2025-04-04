package com.fsg.dscatalog.dto;

import com.fsg.dscatalog.entities.Category;

import java.io.Serializable;

public class CategoryDTO implements Serializable {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public CategoryDTO() {}

    public CategoryDTO(Long id, String name) {}
}
