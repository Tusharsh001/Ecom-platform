package com.Ecom.platform.controller;


import com.Ecom.platform.model.Product;
import com.Ecom.platform.service.EcomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/api")
public class RESTController {

    @Autowired
    private EcomService service;



    @GetMapping("/products")
    public List<Product> hello(){
        return service.getAllProducts();
    }

    @GetMapping("/product/{id}")
    public Product getProductById(@PathVariable("id") int id){
        return service.getProductById(id);

    }


    /// adding a prodcut to the website
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile){
        Product saveProduct= null;

        try {
            saveProduct = service.addProduct(product,imageFile);
            return new ResponseEntity<>(saveProduct,HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    ///  image send on the differnt url of a perticular product
    @GetMapping("/product/{productId}/image")
    public byte[] getImageByProductId(@PathVariable int productId){
       return service.getProductById(productId).getImageData();
    }


    /// updating the product
    @PutMapping("/product/{id}")
    public String updateProduct(@PathVariable int id , @RequestPart Product product, @RequestPart MultipartFile imageFile){
        Product updatedProduct=null;
        try{
            updatedProduct=service.updateProduct(product,imageFile);
            return "Success";
        }
        catch (Exception e){
            return e.getMessage();
        }
    }


    /// deleting the product
    @DeleteMapping("/product/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        Product product = service.getProductById(id);
        if(product!=null){
            service.deleteProduct(id);
            return "Deleted";
        }

        return "No such product available";
    }


    ///
    @GetMapping("/products/search")
    public List<Product> searchProduct(@RequestParam String keyword){
        List<Product> products =service.searchProduct(keyword);
        System.out.println("Searhing with: "+keyword);
        return products;
    }



}

//    @GetMapping("/load")
//    public String load(){
//        return service.addItem();
//    }