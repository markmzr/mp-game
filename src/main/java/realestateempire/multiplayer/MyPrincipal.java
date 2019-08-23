package realestateempire.multiplayer;

import java.security.Principal;

public class MyPrincipal implements Principal {

    private final String name;

    MyPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
