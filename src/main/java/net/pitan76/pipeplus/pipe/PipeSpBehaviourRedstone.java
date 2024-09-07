package net.pitan76.pipeplus.pipe;

import alexiil.mc.lib.multipart.api.property.MultipartProperties;
import alexiil.mc.lib.multipart.api.property.MultipartPropertyContainer;
import alexiil.mc.mod.pipes.client.model.part.PipeSpPartKey;
import alexiil.mc.mod.pipes.pipe.PartSpPipe;
import alexiil.mc.mod.pipes.pipe.PipeSpBehaviour;
import alexiil.mc.mod.pipes.pipe.PipeSpFlowItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.pitan76.mcpitanlib.api.util.NbtUtil;
import net.pitan76.pipeplus.client.model.part.PipeSpPartKeyMutable;

public class PipeSpBehaviourRedstone extends PipeSpBehaviour {

    public boolean isEmpty = true;

    public PipeSpBehaviourRedstone(PartSpPipe pipe) {
        super(pipe);
    }

    @Override
    public void tick() {
        super.tick();
        World world = pipe.getPipeWorld();
        if (world == null) return;
        if (!world.isClient) {
            MultipartPropertyContainer properties = pipe.container.getProperties();

            isEmpty = ((PipeSpFlowItem) pipe.getFlow()).getAllItemsForRender().isEmpty();
            if (isEmpty) {
                properties.clearValue(pipe, MultipartProperties.getStrongRedstonePower(Direction.UP));
                for (Direction dir : Direction.values()) {
                    properties.clearValue(pipe, MultipartProperties.getWeakRedstonePower(dir));
                }
            } else {
                properties.setValue(pipe, MultipartProperties.getStrongRedstonePower(Direction.UP), 15);
                for (Direction dir : Direction.values()) {
                    properties.setValue(pipe, MultipartProperties.getWeakRedstonePower(dir), 15);
                }
            }
        }
    }

    @Override
    public void fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.fromNbt(nbt, lookup);
        isEmpty = NbtUtil.getBoolean(nbt, "isEmpty");
    }

    @Override
    public NbtCompound toNbt(RegistryWrapper.WrapperLookup lookup) {
        NbtCompound nbt = super.toNbt(lookup);
        NbtUtil.putBoolean(nbt, "isEmpty", isEmpty);
        return nbt;
    }

    @Override
    public PipeSpPartKey createModelState() {
        return new PipeSpPartKeyMutable(pipe.definition, pipe.connections, isEmpty ? 0 : 1);
    }
}