package ru.andrey.VkEducation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andrey.VkEducation.models.user.dependencies.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
