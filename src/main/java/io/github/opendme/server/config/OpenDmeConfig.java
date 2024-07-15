package io.github.opendme.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration("opendme")
public class OpenDmeConfig {

    private User instanceOwner;

    public User instanceOwner() {
        return instanceOwner;
    }

    @Component
    public class User {
        private String name;
        private String password;

        public String name() {
            return name;
        }

        public String password() {
            return password;
        }
    }
}
