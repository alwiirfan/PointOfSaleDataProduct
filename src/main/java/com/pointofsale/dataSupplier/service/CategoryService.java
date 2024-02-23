package com.pointofsale.dataSupplier.service;

import com.pointofsale.dataSupplier.constant.ECategory;
import com.pointofsale.dataSupplier.entity.Category;

public interface CategoryService {
    Category getOrSave(ECategory eCategory);
}
