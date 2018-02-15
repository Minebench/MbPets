package io.github.apfelcreme.MbPetsNoLD.Command;

import io.github.apfelcreme.MbPetsNoLD.ChatInput;
import io.github.apfelcreme.MbPetsNoLD.MbPets;
import io.github.apfelcreme.MbPetsNoLD.MbPetsConfig;
import io.github.apfelcreme.MbPetsNoLD.Pet.Pet;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetConfiguration;
import io.github.apfelcreme.MbPetsNoLD.Pet.PetManager;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.logging.Level;

/**
 * Copyright (C) 2015 Lord36 aka Apfelcreme
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
 * @author Lord36 aka Apfelcreme on 01.07.2015.
 */
public class ConfirmCommand implements SubCommand {

    /**
     * executes the command
     *
     * @param chatInput the input
     */
    public void execute(ChatInput chatInput) {

        if (!chatInput.getSender().hasPermission("MbPets.confirm")) {
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.noPermission"));
            return;
        }
        if (!MbPets.getInstance().isVaultEnabled() || MbPets.getInstance().getEconomy() == null) {
            MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.noVault"));
            return;
        }
        if (!MbPets.getInstance().getEconomy().hasAccount(chatInput.getSender())) {
            return;
        }
        PetConfiguration petConfiguration = PetManager.getInstance().getConfigurations()
                .get(chatInput.getSender().getUniqueId());
        if (petConfiguration != null) {
            if (petConfiguration.isFinished()) {
                if (MbPets.getInstance().getEconomy()
                        .getBalance(MbPets.getInstance().getServer()
                                .getPlayer(petConfiguration.getOwner())) >= petConfiguration.getPrice()) {

                    // delete an old pet that has the same number
                    Pet oldPet = PetManager.getInstance().loadPet(chatInput.getSender().getUniqueId(), petConfiguration.getNumber());
                    if (oldPet != null) {
                        oldPet.delete();
                    } else if (petConfiguration.getConfigurationType() == PetConfiguration.ConfigurationType.MODIFICATION) {
                        // can't modify non-existent pet
                        MbPets.getInstance().getLogger().log(Level.WARNING, chatInput.getSender().getName() + "/" + chatInput.getSender().getUniqueId() + " tried to modify pet that doesn't exist?");
                        return;
                    }

                    // spawn and enter into the db
                    EconomyResponse response = MbPets.getInstance().getEconomy()
                            .withdrawPlayer(MbPets.getInstance().getServer().getPlayer(petConfiguration.getOwner()), petConfiguration.getPrice());
                    if (response.transactionSuccess()) {

                        // if the to-spawn entity is a requested conversion, the "old" entity is stored.
                        // it gets despawned now.
                        // any protection it had is getting deleted
                        if (petConfiguration.getConvertedEntity() != null) {
                            if (MbPets.getInstance().getPluginAnimalProtect() != null &&
                                    MbPets.getInstance().getPluginAnimalProtect()
                                            .hasOwner(petConfiguration.getConvertedEntity().getUniqueId())) {
                                //entity a protected animal
                                MbPets.getInstance()
                                        .getPluginAnimalProtect()
                                        .unprotectAnimal(
                                                petConfiguration.getConvertedEntity()
                                                        .getUniqueId());
                                MbPets.getInstance()
                                        .getLogger()
                                        .info("Animal "
                                                + petConfiguration.getConvertedEntity()
                                                .getUniqueId()
                                                .toString()
                                                + "/ "
                                                + petConfiguration.getConvertedEntity().getType()
                                                .toString()
                                                + " has been removed. The protection was deleted!");
                            }
                            petConfiguration.getConvertedEntity().remove();
                        }

                        // despawn any currently spawned pet
                        Pet currentlySpawnedPet = PetManager.getInstance().getPets().get(chatInput.getSender().getUniqueId());
                        if (currentlySpawnedPet != null) {
                            currentlySpawnedPet.uncall();
                        }

                        // enter into db
                        petConfiguration.confirm();

                        // remove the configuration object
                        PetManager.getInstance().getConfigurations().remove(chatInput.getSender().getUniqueId());

                        // send some messages
                        MbPets.sendMessage(chatInput.getSender(),
                                MbPetsConfig.getTextNode("info.petConfirmed")
                                        .replace("{0}", petConfiguration.getNumber().toString()));

                        // give the player a leash item
                        if (MbPetsConfig.isCallItemEnabled()) {
                            chatInput.getSender().getInventory().addItem(PetManager.createCallItem(petConfiguration));
                        }
                        MbPets.getInstance().getLogger().info(petConfiguration.getPetDescription().getLogDescription());
                    }
                } else {
                    MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.notThatMuchMoney"));
                }
            } else {
                MbPets.sendMessage(chatInput.getSender(), MbPetsConfig.getTextNode("error.petNotFinished"));
            }
        }
    }
}
