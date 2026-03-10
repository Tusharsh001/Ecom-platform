package com.Ecom.platform.service;

import com.Ecom.platform.model.Product;
import com.Ecom.platform.repo.EcomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

@Service
public class EcomService {

    @Autowired
    private EcomRepo repo;



    /// get all product
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(new Product());
    }


    /// adding the product
    public Product addProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());
        return repo.save(product);

    }

    ///  updading the product
    public Product updateProduct(Product product, MultipartFile imageFile) throws IOException {
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        return repo.save(product);
    }

    /// deleting a product

    public void deleteProduct(int id){
        repo.deleteById(id);
    }

    public List<Product>searchProduct(String keyword) {
        return repo.searchProduct(keyword);
    }
}

///  unused code

//    public String addItem(){
//        repo.saveAll(list);
//        return  "accepted";
//    }

//List<Product> list=new ArrayList<>(List.of(
//        new Product(
//                0, "iPhone 14", "Apple smartphone with A15 Bionic chip",
//                "Apple",new BigDecimal("69999.00"),
//                "Electronics", new Date(123, 8, 15), true,
//                50
//        ),
//        new Product(
//                0, "Galaxy S23", "Samsung flagship smartphone with AMOLED display", "Samsung", new BigDecimal("64999.00"),
//                "Electronics",
//                new Date(123, 1, 1),    // 2023-02-01
//                true,
//                40
//        ),
//        new Product(
//                0,
//                "ThinkPad X1",
//                "Business laptop with Intel i7 processor",
//                "Lenovo",
//                new BigDecimal("129999.00"),
//                "Laptops",
//                new Date(122, 10, 10),  // 2022-11-10
//                true,
//                25
//        ),
//        new Product(
//                0,
//                "Sony WH-1000XM5",
//                "Noise cancelling wireless headphones",
//                "Sony",
//                new BigDecimal("29999.00"),
//                "Accessories",
//                new Date(122, 4, 20),   // 2022-05-20
//                true,
//                60
//        ),
//        new Product(
//                0,
//                "Canon EOS R10",
//                "Mirrorless camera with 24MP sensor",
//                "Canon",
//                new BigDecimal("89999.00"),
//                "Cameras",
//                new Date(123, 2, 18),   // 2023-03-18
//                false,
//                0
//        )
//));

