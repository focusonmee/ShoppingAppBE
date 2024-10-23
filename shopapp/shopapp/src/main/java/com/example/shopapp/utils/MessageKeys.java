package com.example.shopapp.utils;

public class MessageKeys {
    // Login messages
    public static final String LOGIN_SUCCESSFULLY = "user.login.login_successfully";
    public static final String LOGIN_FAILED = "user.login.login_failed";
    public static final String REGISTER_SUCCESSFULLY = "user.register.register_successfully";
    public static final String WRONG_PHONE_PASSWORD = "user.login.wrong_phone_or_password";
    // Register messages
    public static final String PASSWORD_NOT_MATCH = "user.register.password_not_match";
    public static final String USER_IS_LOCKED = "user.login.account.locked";

    // Category messages
    public static final String CATEGORY_CREATE_SUCCESSFULLY = "category.create_category.create_successfully";
    public static final String CATEGORY_DELETE_SUCCESSFULLY = "category.delete_category.delete_successfully";
    public static final String CATEGORY_UPDATE_SUCCESSFULLY = "category.update_category.update_successfully";
    public static final String CATEGORY_CREATE_FAILED = "category.create_category.create_failed";

    // Order messages
    public static final String ORDER_DELETE_SUCCESSFULLY = "order.delete_order.delete_successfully";
    public static final String ORDER_DELETE_DETAIL_SUCCESSFULLY = "order.delete_order_detail.delete_successfully";

    // Product messages
    public static final String PRODUCT_ERROR_MAX_5_IMAGES = "product.upload_images.error_max_5_images";
    public static final String PRODUCT_FILE_LARGE = "product.upload_images.file_large";
    public static final String PRODUCT_FILE_MUST_BE_IMAGE = "product.upload_images.file_must_be_image";

    public static final String ROLE_DOES_NOT_EXIST = "user.login.role_failed";

}
