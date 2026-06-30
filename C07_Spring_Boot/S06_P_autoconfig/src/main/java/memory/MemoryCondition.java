package memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Slf4j
public class MemoryCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 자바 시스템 속성 -Dmemory=on
        String memory = context.getEnvironment().getProperty("memory");
        log.info("memory={}", memory);
        return "on".equals(memory);
    }
}
