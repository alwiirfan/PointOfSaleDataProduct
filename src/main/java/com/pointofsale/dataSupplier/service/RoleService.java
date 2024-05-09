package com.pointofsale.dataSupplier.service;

import com.pointofsale.dataSupplier.constant.ERole;
import com.pointofsale.dataSupplier.entity.Role;

public interface RoleService {
    Role getRole(ERole role);
}
