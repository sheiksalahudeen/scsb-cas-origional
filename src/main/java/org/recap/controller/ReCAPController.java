package org.recap.controller;

import org.apache.catalina.connector.Connector;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.recap.model.User;
import org.recap.processor.UserProcessor;
import org.recap.spring.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sheiks on 12/02/17.
 */
@RestController
public class ReCAPController {

    @Autowired
    UserProcessor userProcessor;

    @RequestMapping("/register")
    public ModelAndView main(Model model) {
        return new ModelAndView("register");
    }

    @RequestMapping("/resetPassword")
    public ModelAndView resetPassword(Model model) {
        return new ModelAndView("resetPassword");
    }

    @RequestMapping(method = RequestMethod.POST, value = "/recapApi/createNewUser", produces = {"application/json"})
    @ResponseBody
    public String createNewUser(@RequestBody User user) {
        JSONObject response = new JSONObject();
        try {
            boolean created = userProcessor.createUser(user);
            if(created) {
                response.put("status" , true);
            } else{
                response.put("status" , false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status" , false);
        }
        return response.toString();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/recapApi/updateUser", produces = {"application/json"})
    @ResponseBody
    public String updateUser(@RequestBody User user) {
        JSONObject response = new JSONObject();
        try {
            boolean updated = userProcessor.updateUser(user);
            if(updated) {
                response.put("status" , true);
            } else{
                response.put("status" , false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status" , false);
        }
        return response.toString();
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainerFactory() {
        ApplicationContext applicationContext = ApplicationContextProvider.getInstance().getApplicationContext();
        TomcatEmbeddedServletContainerFactory factory = applicationContext.getBean(TomcatEmbeddedServletContainerFactory.class);
        if(null != factory) {
            factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
                @Override
                public void customize(Connector connector) {
                    connector.setSecure(true);
                }
            });
        }
        return factory;
    }

}
