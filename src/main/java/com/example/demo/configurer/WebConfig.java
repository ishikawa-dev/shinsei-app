package com.example.demo.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //「このクラスは設定クラスですよ」という意味 
//WebMvcConfigurer を実装するとSpring MVC の細かい設定を上書きできる
public class WebConfig implements WebMvcConfigurer {
	
    @Override
    //addResourceHandlers は静的リソース（画像・CSS・JSなど）へのURLパスと物理パスを関連付けるためのメソッド
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	//「ブラウザがアクセスするURLのパターン」 /uploads/abc.jpg にアクセスしたら…
        registry.addResourceHandler("/uploads/**")
        //サーバーのファイルシステム上の実際の場所を指定。"file:" を付けることで、ファイルシステムのパスとして扱う。
                .addResourceLocations("file:uploads/");
    }
}