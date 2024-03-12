package ru.andrey.VkEducation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrey.VkEducation.models.user.dependencies.Address;
import ru.andrey.VkEducation.repositories.AddressRepository;

@Service
@Transactional(readOnly = true)
public class AddressService {
    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public void save(Address address){
        addressRepository.save(address);
    }
}
