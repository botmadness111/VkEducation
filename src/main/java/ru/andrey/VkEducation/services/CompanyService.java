package ru.andrey.VkEducation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrey.VkEducation.models.user.dependencies.Company;
import ru.andrey.VkEducation.repositories.CompanyRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public Optional<Company> save(Company company) {
        Optional<Company> companyOptional = companyRepository.findByName(company.getName());
        if (companyOptional.isEmpty()) {
            companyRepository.save(company);
            companyOptional = Optional.of(company);
        }


        return companyOptional;
    }
}
