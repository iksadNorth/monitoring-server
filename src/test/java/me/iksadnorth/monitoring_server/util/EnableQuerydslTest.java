package me.iksadnorth.monitoring_server.util;

import me.iksadnorth.monitoring_server.config.QuerydslConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({QuerydslConfig.class})
public @interface EnableQuerydslTest {
}
