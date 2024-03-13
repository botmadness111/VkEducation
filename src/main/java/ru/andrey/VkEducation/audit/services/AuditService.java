package ru.andrey.VkEducation.audit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrey.VkEducation.audit.models.Audit;
import ru.andrey.VkEducation.audit.repositories.AuditRepository;

@Service
@Transactional(readOnly = true)
public class AuditService {
    private final AuditRepository auditRepository;

    @Autowired
    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Transactional
    public void save(Audit audit){
        try {
            auditRepository.save(audit);
        } catch (Error error){
            return;
        }

    }
}
