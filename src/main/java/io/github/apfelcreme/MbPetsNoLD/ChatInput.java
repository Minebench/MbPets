package io.github.apfelcreme.MbPetsNoLD;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
public class ChatInput {
	
	private Operation operation;
	
	private String type;
	private String style;
	private String color;
	private String name;
	private String baby;
	private String size;
	private Integer number;
	
	private Player sender;
	private OfflinePlayer targetPlayer;

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * @return the baby
	 */
	public String getBaby() {
		return baby;
	}

	/**
	 * @param baby the baby to set
	 */
	public void setBaby(String baby) {
		this.baby = baby;
	}

	/**
	 * @return the sender
	 */
	public Player getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(Player sender) {
		this.sender = sender;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the targetPlayer
	 */
	public OfflinePlayer getTargetPlayer() {
		return targetPlayer;
	}

	/**
	 * @param targetPlayer the targetPlayer to set
	 */
	public void setTargetPlayer(OfflinePlayer targetPlayer) {
		this.targetPlayer = targetPlayer;
	}

	@Override
	public String toString() {
		return "ChatInput{" +
				"operation=" + operation +
				", type='" + type + '\'' +
				", style='" + style + '\'' +
				", color='" + color + '\'' +
				", name='" + name + '\'' +
				", baby='" + baby + '\'' +
				", size='" + size + '\'' +
				", number=" + number +
				", sender=" + sender +
				", targetPlayer=" + targetPlayer +
				'}';
	}

	public enum Operation {
		RELOAD, REGENERATE, //Admins
		DELETE, FLUSH, MONITOR, DESPAWN, //Moderators
		TYPE, STYLE, COLOR, NAME, BABY, SIZE, CALL, UNCALL, CANCEL, CONFIRM, CONVERT, HELP, INFO, LEASH, LIST, MODIFY, STATUS, SELL; //Users

		/**
		 * returns the matching operation
		 * @param key a string
         * @return an operation
         */
		public static Operation getOperation(String key) {
			for (Operation operation : Operation.values()) {
				if (operation.name().equalsIgnoreCase(key)) {
					return operation;
				}
			}
			return null;
		}

		/**
		 * checks whether a given {@link Operation} needs a value 
		 * e.g. Operation "TYPE" needs a type-value, while "CONFIRM" doesn't need anything
		 * @param operation
		 * @return
		 */
		public static boolean needsValue(Operation operation) {
			switch (operation) {
				case BABY:
				case COLOR:
				case NAME:
				case STYLE:
				case TYPE:
				case SIZE:
					return true;
				case CALL:
				case CANCEL:
				case CONFIRM:
				case CONVERT:
				case DELETE:
				case DESPAWN:
				case FLUSH:
				case HELP:
				case INFO:
				case LEASH:
				case LIST:
				case MODIFY:
				case MONITOR:
				case RELOAD:
				case SELL:
				case STATUS:
				case UNCALL:
					return false;
			}
			return false;
		}
	}
}
