package com.pointofsale.dataSupplier.service.impl;

import org.springframework.stereotype.Service;

import com.pointofsale.dataSupplier.constant.ERole;
import com.pointofsale.dataSupplier.entity.Role;
import com.pointofsale.dataSupplier.repository.RoleRepository;
import com.pointofsale.dataSupplier.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRole(ERole role) {
        return roleRepository.findByRole(role).orElseGet(() -> 
                roleRepository.save(Role.builder().role(role).build()));
    }
    
}
