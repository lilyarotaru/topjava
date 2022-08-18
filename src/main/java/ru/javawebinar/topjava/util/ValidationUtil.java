package ru.javawebinar.topjava.util;


import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ValidationUtil {

    private static final Validator validator;
    private static final ReloadableResourceBundleMessageSource bundleMessageSource;
    public static final Map<String, String> validationMessages = new HashMap();

    static {
        //  From Javadoc: implementations are thread-safe and instances are typically cached and reused.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //  From Javadoc: implementations of this interface must be thread-safe
        validator = factory.getValidator();

        bundleMessageSource = new ReloadableResourceBundleMessageSource();
        bundleMessageSource.setBasename("org/hibernate/validator/ValidationMessages");
        bundleMessageSource.setDefaultEncoding("UTF-8");
        validationMessages.put("NotBlank", bundleMessageSource.getMessage("javax.validation.constraints.NotBlank.message", null, Locale.getDefault()));

        //how add min and max to msg? cause new Object[]{"2","120"} doesn't work
        //can load more messages and use it in tests
        validationMessages.put("Size", bundleMessageSource.getMessage("javax.validation.constraints.Size.message", null, Locale.getDefault()));

    }

    private ValidationUtil() {
    }

    public static <T> void validate(T bean) {
        // https://alexkosarev.name/2018/07/30/bean-validation-api/
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}