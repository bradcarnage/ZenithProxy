package com.zenith.module;

import com.collarmc.pounce.Subscribe;
import com.github.steveice10.mc.protocol.data.game.entity.player.Hand;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Iterators;
import com.zenith.Proxy;
import com.zenith.event.module.AntiAfkStuckEvent;
import com.zenith.event.module.ClientTickEvent;
import com.zenith.pathing.BlockPos;
import com.zenith.pathing.Position;
import com.zenith.util.TickTimer;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.zenith.util.Constants.*;
import static java.util.Arrays.asList;
import static java.util.Objects.isNull;

public class AntiAFK extends Module {
    private final TickTimer swingTickTimer = new TickTimer();
    private final TickTimer startWalkTickTimer = new TickTimer();
    private final TickTimer rotateTimer = new TickTimer();
    private static final long positionCacheTTLMins = 20;
    private final TickTimer distanceDeltaCheckTimer = new TickTimer();
    private boolean shouldWalk = false;
    private final Cache<Position, Position> positionCache;
    private final List<WalkDirection> walkDirections = asList(
            new WalkDirection(1, 0), new WalkDirection(-1, 0),
            new WalkDirection(1, 1), new WalkDirection(-1, -1),
            new WalkDirection(0, -1), new WalkDirection(0, 1),
            new WalkDirection(-1, 1), new WalkDirection(1, -1),
            new WalkDirection(-1, 0), new WalkDirection(1, 0),
            new WalkDirection(1, -1), new WalkDirection(-1, 1),
            new WalkDirection(0, 1), new WalkDirection(0, -1));
    private Instant lastDistanceDeltaWarningTime = Instant.EPOCH;
    private boolean stuck = false;
    private final Iterator<WalkDirection> walkDirectionIterator = Iterators.cycle(walkDirections);
    private BlockPos currentPathingGoal;
    // tick time since we started falling
    // can be negative, indicates pathing should wait until it reaches 0 to fall
    private int gravityT = 0;

    public AntiAFK() {
        super();
        this.positionCache = CacheBuilder.newBuilder()
                .expireAfterWrite(positionCacheTTLMins, TimeUnit.MINUTES)
                .build();
    }

    @Subscribe
    public void handleClientTickEvent(final ClientTickEvent event) {
        if (CONFIG.client.extra.antiafk.enabled
                && Proxy.getInstance().isConnected()
                && isNull(Proxy.getInstance().getCurrentPlayer().get())
                && !Proxy.getInstance().isInQueue()
                && CACHE.getPlayerCache().getThePlayer().getHealth() > 0
                && MODULE_MANAGER.getModule(KillAura.class).map(ka -> !ka.active()).orElse(true)) {
            if (CONFIG.client.extra.antiafk.actions.swingHand) {
                swingTick();
            }
            if (CONFIG.client.extra.antiafk.actions.gravity) {
                gravityTick();
            }
            if (CONFIG.client.extra.antiafk.actions.walk && (!CONFIG.client.extra.antiafk.actions.gravity || gravityT <= 0)) {
                walkTick();
                // check distance delta every 9 mins. Stuck kick should happen at 20 mins
                if (distanceDeltaCheckTimer.tick(10800L, true) && CONFIG.client.server.address.toLowerCase().contains("2b2t.org") && CONFIG.client.extra.antiafk.actions.stuckWarning) {
                    final double distanceMovedDelta = getDistanceMovedDelta();
                    if (distanceMovedDelta < 6) {
                        MODULE_LOG.warn("AntiAFK appears to be stuck. Distance moved: {}", distanceMovedDelta);
                        stuck = true;
                        if (Instant.now().minus(Duration.ofMinutes(20)).isAfter(lastDistanceDeltaWarningTime)) {
                            // only send discord warning once every 20 mins so we don't spam too hard
                            EVENT_BUS.dispatch(new AntiAfkStuckEvent(distanceMovedDelta));
                            lastDistanceDeltaWarningTime = Instant.now();
                        }
                    } else {
                        stuck = false;
                    }
                }
            }
            if (CONFIG.client.extra.antiafk.actions.rotate && (!CONFIG.client.extra.spook.enabled || !spookHasTarget())) {
                rotateTick();
            }
        }
    }

    @Override
    public void clientTickStarting() {
        reset();
    }

    private void reset() {
        swingTickTimer.reset();
        startWalkTickTimer.reset();
        rotateTimer.reset();
        distanceDeltaCheckTimer.reset();
        shouldWalk = false;
        positionCache.invalidateAll();
        lastDistanceDeltaWarningTime = Instant.EPOCH;
        currentPathingGoal = null;
        gravityT = 0;
        stuck = false;
    }

    private boolean spookHasTarget() {
        return MODULE_MANAGER.getModule(Spook.class)
                .map(m -> ((Spook) m).hasTarget.get())
                .orElse(false);
    }

    private double getDistanceMovedDelta() {
        final Collection<Position> positions = this.positionCache.asMap().values();
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minZ = Double.MAX_VALUE;
        double maxZ = -Double.MAX_VALUE;
        for (Position pos : positions) {
            minX = Math.min(pos.getX(), minX);
            maxX = Math.max(pos.getX(), maxX);
            minZ = Math.min(pos.getZ(), minZ);
            maxZ = Math.max(pos.getZ(), maxZ);
        }
        return Math.max(Math.abs(maxX - minX), Math.abs(maxZ - minZ));
    }

    private void rotateTick() {
        if (rotateTimer.tick(1500L, true)) {
            Proxy.getInstance().getClient().send(new ClientPlayerRotationPacket(
                    true,
                    -90 + (180 * ThreadLocalRandom.current().nextFloat()),
                    -90 + (180 * ThreadLocalRandom.current().nextFloat())
            ));
        }
    }

    public void handlePlayerPosRotate() {
        synchronized (this) {
            this.gravityT = -2;
        }
    }

    private void walkTick() {
        if (startWalkTickTimer.tick(400L, true)) {
            shouldWalk = true;
            final WalkDirection directions = walkDirectionIterator.next();
            currentPathingGoal = PATHING.getCurrentPlayerPos()
                    .addX(CONFIG.client.extra.antiafk.actions.walkDistance * directions.from)
                    .addZ(CONFIG.client.extra.antiafk.actions.walkDistance * directions.to)
                    .toBlockPos();

        }
        if (shouldWalk) {
            if (reachedPathingGoal()) {
                shouldWalk = false;
            } else {
                Position nextMovePos = PATHING.calculateNextMove(currentPathingGoal);
                if (nextMovePos.equals(PATHING.getCurrentPlayerPos())) {
                    shouldWalk = false;
                }
                Proxy.getInstance().getClient().send(nextMovePos.toPlayerPositionPacket());
                this.positionCache.put(nextMovePos, nextMovePos);
            }
        }
    }

    private boolean reachedPathingGoal() {
        return Objects.equals(PATHING.getCurrentPlayerPos().toBlockPos(), currentPathingGoal);
    }

    private void gravityTick() {
        synchronized (this) {
            final Optional<Position> nextGravityMove = PATHING.calculateNextGravityMove(gravityT);
            if (nextGravityMove.isPresent()) {
                if (!nextGravityMove.get().equals(PATHING.getCurrentPlayerPos())) {
                    Proxy.getInstance().getClient().send(nextGravityMove.get().toPlayerPositionPacket());
                }
                gravityT++;
            } else {
                gravityT = 0;
            }
        }
    }

    private void swingTick() {
        if (swingTickTimer.tick(3000L, true)) {
            Proxy.getInstance().getClient().send(new ClientPlayerSwingArmPacket(Hand.MAIN_HAND));
        }
    }

    public boolean isStuck() {
        return this.stuck;
    }


    record WalkDirection(int from, int to) {
    }
}
