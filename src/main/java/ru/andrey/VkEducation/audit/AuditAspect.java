package ru.andrey.VkEducation.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.andrey.VkEducation.audit.models.Audit;
import ru.andrey.VkEducation.audit.services.AuditService;
import ru.andrey.VkEducation.security.models.UserVk;
import ru.andrey.VkEducation.security.util.UserVkDetails;
import ru.andrey.VkEducation.services.AddressService;

import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;

    @Autowired
    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }

    @Before("execution(* ru.andrey.VkEducation.controllers.*.*(..)) && args(..)")
    public void logBefore(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserVkDetails userVkDetails = (UserVkDetails) authentication.getPrincipal();
        UserVk userVk = userVkDetails.getUserVk();

        Audit audit = new Audit();
        audit.setDate(new Date());
        audit.setUserVk(userVk);
        audit.setParams(Arrays.toString(joinPoint.getArgs()));
        audit.setMethod(joinPoint.getSignature().getName());

        auditService.save(audit);

    }
}
