package jd_homework.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jd_homework.utils.TemplateEnginUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    // To remove duplicate code TemplateEngine configuration moved to TemplateEnginUtil.class
    private final TemplateEngine engine = TemplateEnginUtil.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // Creating context to fill template
        IContext context = new Context(req.getLocale(), Map.of("timezone", extracted(req, resp)));

        engine.process("time", context, resp.getWriter());
        resp.getWriter().close();

    }

    private String extracted(HttpServletRequest req, HttpServletResponse resp) {
        ZonedDateTime dateTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of(timeZoneValue(req, resp)));

        // Define the desired format pattern
        String pattern = "yyyy-MM-dd HH:mm:ss z";

        // Create a DateTimeFormatter with the specified pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Format the LocalDateTime using the formatter
        return dateTime.format(formatter);
    }

    private String timeZoneValue(HttpServletRequest req, HttpServletResponse resp) {
        String timezone = req.getParameter("timezone");

        //If query parameter is empty that can produce NullPointerException
        try {
            if (timezone != null && !timezone.isEmpty()) {
                //converting query parameter from "UTC 2" if query ?timezone=UTC+2 to "UTC+2)
                timezone = URLEncoder.encode(timezone, StandardCharsets.UTF_8);

                //creating new cookie for timezone
                Cookie cookie = new Cookie("lastTimezone", timezone);
                //setting cookie age
                cookie.setMaxAge(10);
                //add cookie to response
                resp.addCookie(cookie);

                //returning timezone
                return timezone;
            } else {
                Cookie[] cookies = req.getCookies();
                //Check cookies for null, because can produce java.lang.NullPointerException:
                //"Cannot read the array length because "<local5>" is null"
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().contains("lastTimezone")) {
                            return cookie.getValue();
                        }
                    }
                }
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }

        return "UTC";
    }
}