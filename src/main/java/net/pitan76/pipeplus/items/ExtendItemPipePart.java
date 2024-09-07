package net.pitan76.pipeplus.items;

import alexiil.mc.mod.pipes.items.ItemPipePart;
import alexiil.mc.mod.pipes.pipe.PipeSpDef;
import net.minecraft.text.Text;
import net.pitan76.mcpitanlib.api.event.item.ItemAppendTooltipEvent;
import net.pitan76.mcpitanlib.api.item.CompatibleItemSettings;
import net.pitan76.mcpitanlib.api.item.ExtendItemProvider;

import java.util.ArrayList;
import java.util.List;

public class ExtendItemPipePart extends ItemPipePart implements ExtendItemProvider {
    public List<Text> tooltip = new ArrayList<>();

    public ExtendItemPipePart(CompatibleItemSettings settings, PipeSpDef part) {
        super(settings.build(), part);
    }

    public ExtendItemPipePart(CompatibleItemSettings settings, PipeSpDef part, List<Text> tooltip) {
        super(settings.build(), part);
        this.tooltip.addAll(tooltip);
    }

    public ExtendItemPipePart(CompatibleItemSettings settings, PipeSpDef part, Text tooltip) {
        super(settings.build(), part);
        this.tooltip.add(tooltip);
    }

    @Override
    public void appendTooltip(ItemAppendTooltipEvent e, Options options) {
        if (!this.tooltip.isEmpty())
            e.addTooltip(this.tooltip);
    }
}
