package DeveloperCity.OpenFlashChart;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JsonParameter {
    public String renameTo() default "";
}
