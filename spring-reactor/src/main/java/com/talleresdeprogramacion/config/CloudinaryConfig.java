package com.talleresdeprogramacion.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name","drpgnjesv",
                "api_key", "792179128635764",
                "api_secret", "Lrxz9JQ14ONVY7WAMBJFTxYJADE"));
    }
}
