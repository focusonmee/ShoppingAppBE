package com.example.shopapp.service;

import com.example.shopapp.dto.ProductDTO;
import com.example.shopapp.dto.ProductImageDTO;
import com.example.shopapp.exception.DataNotFoundException;
import com.example.shopapp.exception.InvalidParamException;
import com.example.shopapp.model.Category;
import com.example.shopapp.model.Product;
import com.example.shopapp.model.ProductImage;
import com.example.shopapp.repository.CategoryRepository;
import com.example.shopapp.repository.ProductImageRepository;
import com.example.shopapp.repository.ProductRepository;
import com.example.shopapp.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional; // Nhập khẩu annotation @Transactional

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional // Thêm annotation @Transactional cho phương thức này
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("" +
                        "can not find category with id: " + productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription()) // Không quên thêm thuộc tính description
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    @Transactional // Thêm annotation @Transactional cho phương thức này
    public Product getProductById(long productId) throws Exception {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find this product with id " + productId));
    }

    @Override
    @Transactional // Thêm annotation @Transactional cho phương thức này
    public Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) {
        //lay san pham theo page va limit
        Page<Product> productPage;
        productPage = productRepository.searchProducts(categoryId, keyword, pageRequest);
        return productPage.map(ProductResponse::fromProduct);
    }

    @Override
    @Transactional // Thêm annotation @Transactional cho phương thức này
    public Product updateProduct(
            long productId,
            ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(productId);
        if (existingProduct != null) {
            //copy cac thuoc tinh tu dto -> Product
            //co the su dung modelMapper
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("" +
                            "can not find category with id: " + productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional // Thêm annotation @Transactional cho phương thức này
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    @Transactional // Thêm annotation @Transactional cho phương thức này
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Transactional // Thêm annotation @Transactional cho phương thức này
    public ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception {
        // Tìm sản phẩm dựa trên productId từ productImageDTO
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id: " + productImageDTO.getProductId()));

        // Tạo đối tượng ProductImage mới
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        // Kiểm tra số lượng hình ảnh hiện có cho sản phẩm
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException("Number of images must be" +
                    " <= " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }

        // Lưu hình ảnh mới vào cơ sở dữ liệu
        return productImageRepository.save(newProductImage);
    }

    @Override
    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.findProductsByIds(productIds);
    }

    @Override
    public Page<Product> getProductsByKeyword(String keyword, Pageable pageable) {
        return productRepository.findByKeyword(keyword, pageable);

    }


}
