package ru.andrey.VkEducation.audit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.andrey.VkEducation.audit.models.Audit;
import ru.andrey.VkEducation.audit.repositories.AuditRepository;
import ru.andrey.VkEducation.security.models.UserVk;
import ru.andrey.VkEducation.security.services.UserVkService;

@Service
@Transactional(readOnly = true)
public class AuditService {
    private final AuditRepository auditRepository;
    private final UserVkService userVkService;

    @Autowired
    public AuditService(AuditRepository auditRepository, UserVkService userVkService) {
        this.auditRepository = auditRepository;
        this.userVkService = userVkService;
    }

    @Transactional
    public void save(Audit audit) {
        try {
            UserVk user = audit.getUserVk();
            userVkService.save(user);
            auditRepository.save(audit);
        } catch (Error error) {
        } finally {
            audit.setUserVk(null);
            auditRepository.save(audit);
        }

    }
}
