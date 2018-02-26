package io.swvn.reactRole;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Listener extends ListenerAdapter{


    private static final Logger logger = LogManager.getLogger(Listener.class);

    @Override
    public void onReady(ReadyEvent event){
        try {
            Config.loadRoles(event.getJDA());
            for(TextChannel tc : Config.mappedRoles.keySet()){
                for(Message m : Config.mappedRoles.get(tc).keySet()){
                    for(Emote e : Config.mappedRoles.get(tc).get(m).keySet())
                        m.addReaction(e).queue();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Emotes loaded and ready to go!");
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        TextChannel channel = event.getTextChannel();
        Message message = null;
        Emote emote = event.getReactionEmote().getEmote();

        if(event.getUser().isBot()) return;
        if(!Config.mappedRoles.keySet().contains(channel)) return;

        boolean hasID = false;
        for(Message m : Config.mappedRoles.get(channel).keySet()){
            if(m.getId().equals(event.getMessageId())){
                hasID = true;
                message = m;
            }
        }

        if(!hasID) return;

        event.getGuild().getController().addSingleRoleToMember(
                event.getMember(),
                Config.mappedRoles.get(channel).get(message).get(emote)
        ).reason("Self-Add Role").queue();
        logger.info(event.getUser()+" Self-Added role "+Config.mappedRoles.get(channel).get(message).get(emote));

    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event){
        TextChannel channel = event.getTextChannel();
        Message message = null;
        Emote emote = event.getReactionEmote().getEmote();

        if(event.getUser().isBot()) return;
        if(!Config.mappedRoles.keySet().contains(channel)) return;

        boolean hasID = false;
        for(Message m : Config.mappedRoles.get(channel).keySet()){
            if(m.getId().equals(event.getMessageId())){
                hasID = true;
                message = m;
            }
        }
        if(!hasID) return;

        event.getGuild().getController().removeSingleRoleFromMember(
                event.getMember(),
                Config.mappedRoles.get(channel).get(message).get(emote)
        ).reason("Self-Remove Role").queue();
        logger.info(event.getUser()+" Self-Removed role "+Config.mappedRoles.get(channel).get(message).get(emote));
    }
}
