/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.zenith.server.handler.player.incoming.movement;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import lombok.NonNull;
import com.zenith.server.PorkServerConnection;
import com.zenith.util.handler.HandlerRegistry;

import static com.zenith.util.Constants.*;

/**
 * @author DaPorkchop_
 */
public class PlayerRotationHandler implements HandlerRegistry.AsyncIncomingHandler<ClientPlayerRotationPacket, PorkServerConnection> {
    @Override
    public boolean applyAsync(@NonNull ClientPlayerRotationPacket packet, @NonNull PorkServerConnection session) {
        CACHE.getPlayerCache()
                .setYaw((float) packet.getYaw())
                .setPitch((float) packet.getPitch());
        session.getProxy().getSpectatorConnections()
                .forEach(connection -> {
                    connection.send(new ServerEntityTeleportPacket(
                            CACHE.getPlayerCache().getEntityId(),
                            CACHE.getPlayerCache().getX(),
                            CACHE.getPlayerCache().getY(),
                            CACHE.getPlayerCache().getZ(),
                            CACHE.getPlayerCache().getYaw(),
                            CACHE.getPlayerCache().getPitch(),
                            packet.isOnGround()
                    ));
                    connection.send(new ServerEntityHeadLookPacket(
                            CACHE.getPlayerCache().getEntityId(),
                            CACHE.getPlayerCache().getYaw()
                    ));
                });
        return true;
    }

    @Override
    public Class<ClientPlayerRotationPacket> getPacketClass() {
        return ClientPlayerRotationPacket.class;
    }
}