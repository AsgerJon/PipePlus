package ml.pkom.pipeplus;

import ml.pkom.pipeplus.blockentities.IPipeTeleportTileEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TeleportManager {
    public static final TeleportManager instance = new TeleportManager();
    private static final Map<UUID, IPipeTeleportTileEntity> allPipes = new LinkedHashMap<>();
    private static final Set<UUID> unloadedPipes = new HashSet<>();
    public List<IPipeTeleportTileEntity> getPipes(int frequency, @Nullable UUID owner) {
        return allPipes
                .values()
                .stream()
                .filter(pipe -> pipe.getFrequency() == frequency)
                .filter(pipe -> (owner != null && pipe.getOwnerUUID().equals(owner)) || (pipe.isPublic() && owner == null))
                .filter(pipe -> !unloadedPipes.contains(pipe.getPipeUUID()))
                .toList();
    }

    public IPipeTeleportTileEntity getPipe(UUID pipeUUID) {
        return allPipes.get(pipeUUID);
    }

    public void addPipe(IPipeTeleportTileEntity pipe) {
        allPipes.put(pipe.getPipeUUID(), pipe);
    }

    public void removePipe(IPipeTeleportTileEntity pipe) {
        allPipes.remove(pipe.getPipeUUID());
    }

    public void reset() {
        allPipes.clear();
        unloadedPipes.clear();
    }

    public static void register() {
        ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
            for (Map.Entry<BlockPos, BlockEntity> blockEntity : chunk.getBlockEntities().entrySet()) {
                if(!(blockEntity.getValue() instanceof IPipeTeleportTileEntity pipe)) {
                    continue;
                }

                unloadedPipes.remove(pipe.getPipeUUID());
            }
        });

        ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
            for (Map.Entry<BlockPos, BlockEntity> blockEntity : chunk.getBlockEntities().entrySet()) {
                if(!(blockEntity.getValue() instanceof IPipeTeleportTileEntity pipe)) {
                    continue;
                }

                unloadedPipes.add(pipe.getPipeUUID());
            }
        });
    }
}
