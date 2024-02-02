package io.github.apfelcreme.MbPetsNoLD.Dependencies;
/*
 * MbPets
 * Copyright (c) 2024 Max Lee aka Phoenix616 (max@themoep.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import org.bukkit.entity.Player;

import java.util.Set;

public class WorldGuardAdapter {
    public boolean isSpawningBlocked(Player player) {
        LocalPlayer lp = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionManager rm = WorldGuard.getInstance().getPlatform().getRegionContainer().get(lp.getWorld());
        if (rm != null) {
            BlockVector3 vector = lp.getLocation().toVector().toBlockPoint();
            boolean mobSpawning = rm.getApplicableRegions(vector).queryState(lp, Flags.MOB_SPAWNING) != StateFlag.State.DENY;
            if (!mobSpawning) {
                MbPets.sendMessage(player, MbPetsConfig.getTextNode("error.inFlaggedRegion"));
                return true;
            }

            Set<String> blockedCommands = rm.getApplicableRegions(vector).queryValue(lp, Flags.BLOCKED_CMDS);
            if (blockedCommands != null && blockedCommands.contains("/pet")) {
                MbPets.sendMessage(player, MbPetsConfig.getTextNode("error.inFlaggedRegion"));
                return true;
            }

            Set<String> allowedCommands = rm.getApplicableRegions(vector).queryValue(lp, Flags.ALLOWED_CMDS);
            if (allowedCommands != null && !allowedCommands.contains("/pet")) {
                MbPets.sendMessage(player, MbPetsConfig.getTextNode("error.inFlaggedRegion"));
                return true;
            }
        }
        return false;
    }
}
