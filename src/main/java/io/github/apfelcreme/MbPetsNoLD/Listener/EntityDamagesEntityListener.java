package io.github.apfelcreme.MbPetsNoLD.Listener;

import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;

import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.projectiles.ProjectileSource;

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
public class EntityDamagesEntityListener implements Listener {

    /**
     * target navigation
     *
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        // We only care about LivingEntities getting damaged
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        
        // Check if player attacks Pet
        LivingEntity damager = null;
        if (event.getDamager() instanceof LivingEntity) {
            damager = (LivingEntity) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) event.getDamager()).getShooter();
            if (shooter instanceof LivingEntity) {
                damager = (LivingEntity) shooter;
            }
        }
        
        // We don't care about stuff damaging itself
        if (event.getEntity() == damager) {
            return;
        }
        
        // Player attacked pet
        if (damager instanceof Player) {
            Pet damaged = PetManager.getInstance().getPetByEntity(event.getEntity());
            // Don't allow attacking pets when PVP is disabled
            if (damaged != null && (!event.getEntity().getWorld().getPVP() || damaged.getOwner().equals(damager.getUniqueId()))) {
                event.setCancelled(true);
                return;
            }
        }
        
        // Player got attacked
        if (event.getEntity() instanceof Player) {
            Pet damagerPet = PetManager.getInstance().getPetByEntity(damager);
            if (damagerPet != null) {
                // Don't allow pets attacking when PVP is disabled
                if (!event.getEntity().getWorld().getPVP() || damagerPet.getOwner().equals(event.getEntity().getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }
                target((Player) event.getEntity(), MbPets.getInstance().getServer().getPlayer(damagerPet.getOwner()), EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER, event);
            } else {
                target((Player) event.getEntity(), damager, EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER, event);
            }
            
        }
        
        // Player attacked entity -> target it
        if (damager instanceof Player) {
            //Player attacks entity
            target((Player)damager, (LivingEntity) event.getEntity(), EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, event);
        }
    }
    
    private void target(Player petOwner, LivingEntity target, EntityTargetEvent.TargetReason reason, Event event) {
        Pet pet = PetManager.getInstance().getPets().get(petOwner.getUniqueId());
        if (pet != null) {
            pet.onSpecifyTarget(target, reason, event);
        }
    }
}

