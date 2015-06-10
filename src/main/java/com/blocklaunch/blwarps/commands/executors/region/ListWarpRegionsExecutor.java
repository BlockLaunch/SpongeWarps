package com.blocklaunch.blwarps.commands.executors.region;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import com.blocklaunch.blwarps.BLWarps;
import com.blocklaunch.blwarps.Constants;
import com.blocklaunch.blwarps.Util;
import com.blocklaunch.blwarps.Warp;
import com.blocklaunch.blwarps.region.WarpRegion;
import com.google.common.base.Optional;

public class ListWarpRegionsExecutor implements CommandExecutor {

    private BLWarps plugin;

    public ListWarpRegionsExecutor(BLWarps plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the currently loaded warps, paginates them into pages of size WARPS_PER_PAGE, and sends
     * the warp names in a message to the player
     * 
     * @param source
     * @param args
     * @return
     * @throws CommandException
     */
    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

        if (plugin.getWarpRegionManager().getPayload().isEmpty()) {
            source.sendMessage(Constants.NO_WARPS_MSG);
            return CommandResult.success();
        }

        List<Text> warpRegionNames = new ArrayList<Text>();

        for (WarpRegion region : plugin.getWarpRegionManager().getPayload()) {
            Optional<Warp> linkedWarpOpt = plugin.getWarpManager().getOne(region.getLinkedWarpName());
            if (linkedWarpOpt.isPresent()) {
                if (plugin.getUtil().hasPermission(source, linkedWarpOpt.get()) == false) {
                    continue;
                }
            }
            warpRegionNames.add(Util.generateWarpRegionInfoText(region));
        }

        PaginationService paginationService = plugin.getGame().getServiceManager().provide(PaginationService.class).get();
        paginationService.builder().contents(warpRegionNames).title(Texts.of(TextColors.BLUE, "WarpRegions")).paddingString("-").sendTo(source);

        return CommandResult.success();
    }

}
