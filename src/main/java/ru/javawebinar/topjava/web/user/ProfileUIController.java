package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.Valid;
import java.util.Locale;

import static ru.javawebinar.topjava.web.ExceptionInfoHandler.DUPLICATE_EMAIL;

@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "profile";
        } else {
            try {
                super.update(userTo, SecurityUtil.authUserId());
                SecurityUtil.get().setTo(userTo);
                status.setComplete();
                return "redirect:/meals";
            } catch (DataIntegrityViolationException exception) {
                return conflictEmail(exception, result);
            }
        }
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        } else {
            try {
                super.create(userTo);
                status.setComplete();
                return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
            } catch (DataIntegrityViolationException exception) {
                return conflictEmail(exception, result);
            }
        }
    }

    private String conflictEmail(DataIntegrityViolationException e, BindingResult result) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (rootCause.getMessage().contains(DUPLICATE_EMAIL)) {
            result.rejectValue("email", "error.user", messageSource.getMessage(DUPLICATE_EMAIL, null, Locale.getDefault()));
        }
        return "profile";
    }
}