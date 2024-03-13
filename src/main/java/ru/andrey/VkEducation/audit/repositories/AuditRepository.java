package ru.andrey.VkEducation.audit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.andrey.VkEducation.audit.models.Audit;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Integer> {
}
