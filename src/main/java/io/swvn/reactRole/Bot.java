package io.swvn.reactRole;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Bot {

    public static void main(String args[]){
        try {

            new JDABuilder(AccountType.BOT)
                    .setToken(Config.getToken())
                    .addEventListener(new Listener())
                    .buildAsync();

        } catch (LoginException | IOException e) {
            e.printStackTrace();
        }
    }

}
