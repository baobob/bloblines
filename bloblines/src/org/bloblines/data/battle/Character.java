package org.bloblines.data.battle;

import java.util.HashMap;
import java.util.Map;

import org.bloblines.data.game.Status;

public abstract class Character {

	private static int characterId = 1;

	/** Character Name */
	public String name;

	private int id = characterId++;

	protected Map<Attributes, Integer> attributes = new HashMap<>();

	public Status status = Status.OK;

	public boolean firstSkillDone = false;
	public boolean secondSkillDone = false;

	public enum Attributes {
		HP, CURRENT_HP, SPEED, CURRENT_SPEED, STRENGTH, CURRENT_STRENGTH, INTELLIGENCE, CURRENT_INTELLIGENCE, WISDOM, CURRENT_WISDOM
	}

	/**
	 * First skill.
	 * 
	 * @return Returns the skill to use in first round of a turn. This may be null if the character uses only one skill that's set in second
	 *         position
	 */
	public abstract Skill getFirstSkill();

	/**
	 * Second skill.
	 * 
	 * @return Returns the skill to use in second round of a turn. This may be null if the character uses only one skill that's set in first
	 *         position
	 */
	public abstract Skill getSecondSkill();

	public int getAttribute(Attributes attr) {
		return attributes.get(attr);
	}

	public void setAttribute(Attributes attr, int value) {
		attributes.put(attr, value);
	}

	public int changeAttribute(Attributes attr, int value) {
		attributes.put(attr, Integer.max(attributes.get(attr) + value, 0));
		return attributes.get(attr);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Character other = (Character) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
