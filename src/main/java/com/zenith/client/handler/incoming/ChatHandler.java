package com.zenith.client.handler.incoming;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.zenith.client.ClientSession;
import com.zenith.event.proxy.DeathMessageEvent;
import com.zenith.event.proxy.SelfDeathMessageEvent;
import com.zenith.event.proxy.ServerChatReceivedEvent;
import com.zenith.event.proxy.ServerRestartingEvent;
import com.zenith.util.handler.HandlerRegistry;
import lombok.NonNull;
import net.daporkchop.lib.minecraft.text.component.MCTextRoot;
import net.daporkchop.lib.minecraft.text.parser.AutoMCFormatParser;

import java.awt.*;

import static com.zenith.util.Constants.*;
import static java.util.Objects.nonNull;

public class ChatHandler implements HandlerRegistry.AsyncIncomingHandler<ServerChatPacket, ClientSession> {
    @Override
    public boolean applyAsync(@NonNull ServerChatPacket packet, @NonNull ClientSession session) {
        try {
            CHAT_LOG.info(packet.getMessage());
            final MCTextRoot mcTextRoot = AutoMCFormatParser.DEFAULT.parse(packet.getMessage());
            final String messageString = mcTextRoot.toRawString();
            /*
             * example death message:
             * {"extra":[{"text":""},{"color":"dark_aqua","text":""},
             * {"color":"dark_aqua","clickEvent":{"action":"suggest_command","value":"/w DCI5135 "},
             * "hoverEvent":{"action":"show_text","value":[{"text":""},
             * {"color":"gold","text":"Message "},{"color":"dark_aqua","text":""},
             * {"color":"dark_aqua","text":"DCI5135"},{"color":"dark_aqua","text":""}]},"text":"DCI5135"},
             * {"color":"dark_aqua","text":" "},
             * {"color":"dark_red","text":"died inside lava somehow."}],"text":""}
             */
            if (!messageString.startsWith("<")) { // normal chat msg
                // death message color on 2b
                if (mcTextRoot.getChildren().stream().anyMatch(child -> nonNull(child.getColor()) && child.getColor().equals(new Color(170, 0, 0)))) {
                    EVENT_BUS.dispatch(new DeathMessageEvent(messageString, mcTextRoot));
                    if (mcTextRoot.getChildren().stream().anyMatch(child -> nonNull(child.getText()) && child.getText().equals(CONFIG.authentication.username))) {
                        // todo: known oversight: also detects when we kill someone else
                        //  need to use death message parsing helper class
                        EVENT_BUS.dispatch(new SelfDeathMessageEvent(messageString));
                    }
                } else if (messageString.startsWith(("[SERVER]"))) { // server message
                    if (messageString.startsWith("[SERVER] Server restarting in")) { // todo: include time till restart in event
                        EVENT_BUS.dispatch(new ServerRestartingEvent(messageString));
                    }
                }
            }

            EVENT_BUS.dispatch(new ServerChatReceivedEvent(messageString));
        } catch (final Exception e) {
            CLIENT_LOG.error("Caught exception in ChatHandler. Packet: " + packet, e);
        }
        return true;
    }

    @Override
    public Class<ServerChatPacket> getPacketClass() {
        return ServerChatPacket.class;
    }
}
