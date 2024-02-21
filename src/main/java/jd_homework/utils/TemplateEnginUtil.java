package jd_homework.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

public class TemplateEnginUtil {
    private static final TemplateEngine engine;

    // static initialization for singleton class
    static {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        // path ./webapp/templates/ doesn't work in my case
        // because tomcat try to get templates from ./tomcat/bit/webapp/template/
        resolver.setPrefix("/home/mightyloot/IdeaProjects/JD_Homework_11/src/main/webapp/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }
    private TemplateEnginUtil(){
    }

    public static TemplateEngine getInstance() {
        return engine;
    }
}
