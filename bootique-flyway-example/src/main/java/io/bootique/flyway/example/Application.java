package io.bootique.flyway.example;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.bootique.Bootique;

public class Application implements Module {
    public static void main(String[] args) {
        Bootique.app("--config=classpath:config.yml", "--migrate")
                .autoLoadModules()
                .module(Application.class)
                .run();
    }

    @Override
    public void configure(Binder binder) {
    }
}
