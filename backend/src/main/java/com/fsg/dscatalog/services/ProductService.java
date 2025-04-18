package com.fsg.dscatalog.services;

import com.fsg.dscatalog.dto.CategoryDTO;
import com.fsg.dscatalog.dto.ProductDTO;
import com.fsg.dscatalog.entities.Category;
import com.fsg.dscatalog.entities.Product;
import com.fsg.dscatalog.repositories.CategoryRepository;
import com.fsg.dscatalog.repositories.ProductRepository;
import com.fsg.dscatalog.services.exceptions.DatabaseException;
import com.fsg.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> list = productRepository.findAll(pageable);
         return list.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO  dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO  dto) {
        try {
            Product entity = productRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id not found " + id);
        }

        try {
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
         entity.setName(dto.getName());
         entity.setDescription(dto.getDescription());
         entity.setPrice(dto.getPrice());
         entity.setDate(dto.getDate());
         entity.setImgUrl(dto.getImgUrl());

         entity.getCategories().clear();
         for (CategoryDTO categoryDTO : dto.getCategories()) {
             Category category = categoryRepository.getReferenceById(categoryDTO.getId());
             entity.getCategories().add(category);
         }
    }


}
