package io.github.apfelcreme.MbPetsNoLD;

import io.github.apfelcreme.MbPetsNoLD.Pet.PetType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
public class MbPetsConfig {

    private static File languageConfigFile;
    private static YamlConfiguration languageConfig;

    /**
     * init
     */
    public static void init() {
        MbPets.getInstance().saveResource("config.yml", false);
        MbPets.getInstance().saveResource("lang.de.yml", false);


        languageConfigFile = new File(MbPets.getInstance().getDataFolder() + "/lang.de.yml");
        languageConfig = YamlConfiguration.loadConfiguration(languageConfigFile);
    }


    /**
     * reloads the language file
     */
    public static void reloadLanguageConfig() {
        languageConfigFile = new File(MbPets.getInstance().getDataFolder() + "/lang.de.yml");
        languageConfig = YamlConfiguration
                .loadConfiguration(languageConfigFile);
    }

    /**
     * returns the username of the mysql user
     *
     * @return the username of the mysql user
     */
    public static String getDbUser() {
        return MbPets.getInstance().getConfig().getString("database.user");
    }

    /**
     * returns the mysql password
     *
     * @return the mysql password
     */
    public static String getDbPassword() {
        return MbPets.getInstance().getConfig().getString("database.password");
    }

    /**
     * returns the database name
     *
     * @return the database name
     */
    public static String getDatabase() {
        return MbPets.getInstance().getConfig().getString("database.database");
    }

    /**
     * returns the database url
     *
     * @return the database url
     */
    public static String getDbUrl() {
        return MbPets.getInstance().getConfig().getString("database.url");
    }

    /**
     * should the hikari connection pool be used
     *
     * @return true or false
     */
    public static boolean useHikariCP() {
        return MbPets.getInstance().getConfig().getBoolean("useHikariCP");
    }

    /**
     * returns a text from the language file
     *
     * @param key
     * @return
     */
    public static String getTextNode(String key) {
        return getNode("texts." + key);
    }

    /**
     * returns a string from the language file
     *
     * @param key a path in a yml file
     * @return a text
     */
    public static String getNode(String key) {
        String ret = (String) languageConfig
                .get(key);
        if (ret != null && !ret.isEmpty()) {
            ret = ChatColor.translateAlternateColorCodes('§', ret);
            return ChatColor.translateAlternateColorCodes('&', ret);
        } else {
            return "Missing text node: " + key;
        }
    }

    /**
     * returs a DyeColor from the chat input given. e.g. /pet ... color blau
     * will return AnimalColor.BLUE
     *
     * @param color
     * @return
     */
    public static DyeColor parseColor(String color) {
        if (color == null)
            return null;
        for (DyeColor a : DyeColor.values()) {
            if (languageConfig.getStringList("DyeColors." + a.name() + ".aliases")
                    .contains(color.toUpperCase())) {
                return a;
            }
        }
        return null;
    }

    /**
     * returs an Horse.Color from the chat input given. e.g. /pet ... color
     * Kastanie will return Horse.Color.CHESTNUT
     *
     * @param color
     * @return
     */
    public static Horse.Color parseHorseColor(String color) {
        if (color == null)
            return null;
        for (Horse.Color a : Horse.Color.values()) {
            if (languageConfig.getStringList("HorseColors." + a.name() + ".aliases")
                    .contains(color.toUpperCase())) {
                return a;
            }
        }
        return null;
    }

    /**
     * returns an Horse.Style from the chat input given. e.g. /pet ... style ...
     * schwarzfleckig will return Horse.Style.BLACK_DOTS
     *
     * @param style
     * @return
     */
    public static Horse.Style parseHorseStyle(String style) {
        if (style == null)
            return null;
        for (Horse.Style a : Horse.Style.values()) {
            if (languageConfig.getStringList("HorseStyles." + a.name() + ".aliases")
                    .contains(style.toUpperCase())) {
                return a;
            }
        }
        return null;
    }

    /**
     * returns an Ocelot.Style from the chat input given. e.g. /pet ... style
     * ...
     *
     * @param type
     * @return
     */
    public static Ocelot.Type parseOcelotType(String type) {
        if (type == null)
            return null;
        for (Ocelot.Type a : Ocelot.Type.values()) {
            if (languageConfig.getStringList("OcelotTypes." + a.name() + ".aliases")
                    .contains(type.toUpperCase())) {
                return a;
            }
        }
        return null;
    }

    /**
     * returns a RabbitType from the chat input given. e.g. /pet ... style gold
     * will return RabbitType.GOLD
     *
     * @param type
     * @return
     */
    public static Rabbit.Type parseRabbitType(String type) {
        if (type == null)
            return null;
        for (Rabbit.Type a : Rabbit.Type.values()) {
            if (languageConfig.getStringList("RabbitTypes." + a.name() + ".aliases")
                    .contains(type.toUpperCase())) {
                return a;
            }
        }
        return null;
    }

    /**
     * returns a Llama.Color from the chat input given. e.g. /pet ... color creamy
     * will return Llama.Color.CREAMY
     *
     * @param color the color name
     * @return the actual color
     */
    public static Llama.Color parseLlamaColor(String color) {
        if (color == null)
            return null;
        for (Llama.Color c : Llama.Color.values()) {
            if (languageConfig.getStringList("LlamaColors." + c.name() + ".aliases")
                    .contains(color.toUpperCase())) {
                return c;
            }
        }
        return null;
    }

    /**
     * returns a Parrot.Color from the chat input given.
     *
     * @param color the color name
     * @return the actual color
     */
    public static Parrot.Variant parseParrotColor(String color) {
        if (color == null)
            return null;
        for (Parrot.Variant c : Parrot.Variant.values()) {
            if (languageConfig.getStringList("ParrotColors." + c.name() + ".aliases")
                    .contains(color.toUpperCase())) {
                return c;
            }
        }
        return null;
    }

    /**
     * returns an Integer (1-3) for a slime's size. input would be e.g. "Groß" or "klein"
     *
     * @param size
     * @return
     */
    public static Integer parseSlimeSize(String size) {
        if (size == null)
            return null;
        for (int i = 1; i < 5; i++) {
            if (languageConfig.getStringList("SlimeSizes." + i + ".aliases")
                    .contains(size.toUpperCase())) {
                return i;
            }
        }
        return null;
    }


    /**
     * returs a PetType from the chat input given. e.g. /pet ... type katze
     * will return PetType.OCELOT
     *
     * @param type the type
     * @return the matching PetType
     */
    public static PetType parseType(String type) {
        if (type == null)
            return null;
        for (PetType petType : PetType.values()) {
            if (languageConfig.getStringList("PetTypes." + petType.name() + ".aliases")
                    .contains(type.toUpperCase())) {
                return petType;
            }
        }
        return null;
    }

    /**
     * returns a list of Strings that contains the names of all available pet-types
     *
     * @return a list of Strings that contains the names of all available pet-types
     */
    public static List<String> getAvailableTypes() {
        List<String> strings = new ArrayList<String>();
        for (PetType type : PetType.values()) {
            strings.add(languageConfig.getString("PetTypes." + type.name() + ".displaytext"));
        }
        return strings;
    }

    /**
     * returns a list of Strings that contains the names of all available DyeColors
     *
     * @returna list of Strings that contains the names of all available DyeColors
     */
    public static List<String> getAvailableDyeColors() {
        List<String> strings = new ArrayList<String>();
        for (DyeColor color : DyeColor.values()) {
            String typ = languageConfig.getString("DyeColors." + color.name() + ".displaytext");
            if (typ != null) {
                strings.add(typ);
            }
        }
        return strings;
    }

    /**
     * returns a list of Strings that contains the names of all available HorseColors
     *
     * @return a list of Strings that contains the names of all available HorseColors
     */
    public static List<String> getAvailableHorseColors() {
        List<String> strings = new ArrayList<String>();
        for (Horse.Color color : Horse.Color.values()) {
            String typ = languageConfig.getString("HorseColors." + color.name() + ".displaytext");
            if (typ != null) {
                strings.add(typ);
            }
        }
        return strings;
    }

    /**
     * returns a list of Strings that contains the names of all available HorseStyles
     *
     * @return a list of Strings that contains the names of all available HorseStyles
     */
    public static List<String> getAvailableHorseStyles() {
        List<String> strings = new ArrayList<String>();
        for (Horse.Style style : Horse.Style.values()) {
            String typ = languageConfig.getString("HorseStyles." + style.name() + ".displaytext");
            if (typ != null) {
                strings.add(typ);
            }
        }
        return strings;
    }

    /**
     * returns a list of Strings that contains the names of all available OcelotStyles
     *
     * @return a list of Strings that contains the names of all available OcelotStyles
     */
    public static List<String> getAvailableOcelotStyles() {
        List<String> strings = new ArrayList<String>();
        for (Ocelot.Type style : Ocelot.Type.values()) {
            String typ = languageConfig.getString("OcelotTypes." + style.name() + ".displaytext");
            if (typ != null) {
                strings.add(typ);
            }
        }
        return strings;
    }

    /**
     * returns a list of Strings that contains the names of all available RabbitStyles
     *
     * @return a list of Strings that contains the names of all available RabbitStyles
     */
    public static List<String> getAvailableRabbitTypes() {
        List<String> strings = new ArrayList<String>();
        for (Rabbit.Type style : Rabbit.Type.values()) {
            String typ = languageConfig.getString("RabbitTypes." + style.name() + ".displaytext");
            if (typ != null) {
                strings.add(typ);
            }
        }
        return strings;
    }

    /**
     * returns a list of Strings that contains the names of all available LlamaColors
     *
     * @return a list of Strings that contains the names of all available LlamaColors
     */
    public static List<String> getAvailableLlamaColors() {
        List<String> strings = new ArrayList<>();
        for (Llama.Color color : Llama.Color.values()) {
            String c = languageConfig.getString("LlamaColors." + color.name() + ".displaytext");
            if (c != null) {
                strings.add(c);
            }
        }
        return strings;
    }

    /**
     * returns a list of Strings that contains the names of all available ParrotColors
     *
     * @return a list of Strings that contains the names of all available ParrotColors
     */
    public static List<String> getAvailableParrotColors() {
        List<String> strings = new ArrayList<>();
        for (Parrot.Variant color : Parrot.Variant.values()) {
            String c = languageConfig.getString("ParrotColors." + color.name() + ".displaytext");
            if (c != null) {
                strings.add(c);
            }
        }
        return strings;
    }

    /**
     * returns a list of Strings that contains the names of all available DroppedItems
     *
     * @return a list of Strings that contains the names of all available DroppedItems
     */
    public static List<String> getAvailableSlimeSizes() {
        List<String> strings = new ArrayList<String>();
        for (int i = 1; i < 5; i++) {
            String text = languageConfig.getString("SlimeSizes." + i + ".displaytext");
            if (text != null) {
                strings.add(text);
            }
        }
        return strings;
    }

    /**
     * returns the type-specific price
     *
     * @param type the type
     * @return the price
     */
    public static Double getPetPrice(PetType type) {
        if (type == null) {
            return 0.0;
        }
        return MbPets.getInstance().getConfig()
                .getDouble("prices." + type.name());
    }

    /**
     * returns the modification price
     *
     * @return the modification price
     */
    public static Double getModificationPrice() {
        return MbPets.getInstance().getConfig()
                .getDouble("prices.MODIFY");
    }

    /**
     * returns the type-specific attack strength
     *
     * @param type the pet type
     * @return the strength
     */
    public static float getPetAttackStrength(PetType type) {
        if (type == null) {
            return 4.0f; // Basic attack stregth of a wolf
        }
        return (float) MbPets.getInstance().getConfig()
                .getDouble("damage." + type.name());
    }

    /**
     * returns the type-specific basic movement speed
     *
     * @param type the pet type
     * @return its speed
     */
    public static Double getPetSpeed(PetType type) {
        if (type == null) {
            return 0.0;
        }
        return MbPets.getInstance().getConfig()
                .getDouble("speed." + type.name());
    }

    /**
     * returns the type-specific enhanced movement speed
     *
     * @param type the pet type
     * @return the speed
     */
    public static Double getEnhancedPetSpeed(PetType type) {
        if (type == null) {
            return 0.0;
        }
        return MbPets.getInstance().getConfig()
                .getDouble("enhancedSpeed." + type.name());
    }

    /**
     * returns the pet specific sound
     *
     * @param type the pet type
     * @return the sound
     */
    public static Sound getPetSound(PetType type) {
        if (type == null) {
            return Sound.ENTITY_SHEEP_STEP;
        }
        return Sound.valueOf(MbPets.getInstance().getConfig().getString("sound." + type.name()));
    }

    /**
     * returns the amount of exp the pet gains when killing an entity
     *
     * @param type the entity type
     * @return the amount of exp
     */
    public static int getTargetExpReward(EntityType type) {
        if (type == null) {
            return 0;
        }
        return MbPets.getInstance().getConfig().getInt("expReward." + type.name());

    }

    /**
     * returns the amount of time that must pass before a pet can be called or uncalled again
     *
     * @return an amount time in milliseconds
     */
    public static Long getCallDelay() {
        return MbPets.getInstance().getConfig().getLong("callDelay");
    }

    /**
     * returns the delay between the particle task ticks
     *
     * @return a number of ticks, 20 = 1s
     */
    public static long getParticleTaskDelay() {
        return MbPets.getInstance().getConfig().getLong("particleTaskDelay");
    }

    /**
     * returns the delay between the follow task ticks
     *
     * @return a number of ticks, 20 = 1s
     */
    public static long getFollowTaskDelay() {
        return MbPets.getInstance().getConfig().getLong("followTaskDelay");
    }

    /**
     * returns the cooldown before a player can call a pet again after it died
     *
     * @return an amount time in milliseconds
     */
    public static long getPetDeathCooldown() {
        return MbPets.getInstance().getConfig().getLong("petDeathCooldown");
    }

    /**
     * returns the language configuration object to access the lang.de.yml
     *
     * @return the language configuration object to access the lang.de.yml
     */
    public static FileConfiguration getLanguageConfiguration() {
        return languageConfig;
    }

}
