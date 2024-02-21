package net.pitan76.pipeplus.blockentities;

import alexiil.mc.lib.attributes.Simulation;
import alexiil.mc.lib.attributes.item.ItemExtractable;
import alexiil.mc.lib.attributes.item.impl.EmptyItemExtractable;
import alexiil.mc.mod.pipes.blocks.PipeFlowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import net.pitan76.mcpitanlib.api.event.block.TileCreateEvent;
import net.pitan76.pipeplus.blocks.Blocks;
import net.pitan76.pipeplus.config.PipePlusConfig;
import net.pitan76.pipeplus.pipeflow.TinPipeFlow;

public class TinPipeEntity extends ExtendTilePipeSided {
    private int needCooldown = 10;
    private int cooldown = needCooldown;

    public TinPipeEntity(TileCreateEvent event) {
        super(BlockEntities.TIN_PIPE_ENTITY, event, Blocks.TIN_PIPE, TinPipeFlow::new);
        needCooldown = PipePlusConfig.getConfig().tinTransportExtractDelay;
    }

    @Override
    public void tick() {
        super.tick();
        cooldown--;
        if (cooldown <= 0) {
            cooldown = needCooldown;
            if (!world.isClient) {
                Direction dir = currentDirection();
                if (dir != null) {
                    tryExtract(dir, 1);
                }
            }
        }
    }

    @Override
    protected boolean canFaceDirection(Direction dir) {
        if (this.getNeighbourPipe(dir) != null) {
            return false;
        } else {
            return this.getItemExtractable(dir) != EmptyItemExtractable.NULL;
        }
    }

    public void tryExtract(Direction dir, int pulses) {
        ItemExtractable extractable = this.getItemExtractable(dir);
        ItemStack stack = extractable.attemptAnyExtraction(pulses, Simulation.ACTION);
        if (!stack.isEmpty()) {
            ((PipeFlowItem)this.flow).insertItemsForce(stack, dir, (DyeColor)null, 0.08D);
        }

    }
}
