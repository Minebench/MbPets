package io.github.apfelcreme.MbPetsNoLD.Listener;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Copyright (C) 2016 Lord36 aka Apfelcreme
 * <p>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme
 */
public class PlayerLeashItemClickListener implements Listener {

    Map<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLeashClick(final PlayerInteractEvent event) {
        MbPets.getInstance().getServer().getScheduler().runTaskAsynchronously(MbPets.getInstance(), () -> {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                if (event.getMaterial() == Material.CARROT_ON_A_STICK) {
                    if (cooldowns.get(event.getPlayer().getUniqueId()) == null
                            || ((cooldowns.get(event.getPlayer().getUniqueId()) + MbPetsConfig.getCallDelay()) < System.currentTimeMillis())) {
                        String regex = ChatColor.stripColor(MbPetsConfig.getTextNode("info.leashTitle")
                                .replace("{0}", ".*.").replace("[", "\\[").replace("]", "\\]"));
                        if (Pattern.matches(regex, ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName()))) {
                            if (PetManager.getInstance().getPets().get(event.getPlayer().getUniqueId()) != null) {
                                PetManager.getInstance().getPets().get(event.getPlayer().getUniqueId()).uncall();
                            } else {
                                Integer number = Integer.parseInt(ChatColor.stripColor(event.getItem().getItemMeta().getLore().get(1)).replace("#", ""));
                                Pet pet = PetManager.getInstance().loadPet(event.getPlayer().getUniqueId(), number);
                                if (pet != null) {
                                    pet.call();
                                }
                            }
                            cooldowns.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
                        }
                    } else {
                        MbPets.sendMessage(event.getPlayer(), MbPetsConfig.getTextNode("error.cooldown"));
                    }
                }
            }
        });
    }

}
