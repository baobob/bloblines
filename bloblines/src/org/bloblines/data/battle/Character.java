package org.bloblines.data.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bloblines.data.game.Atom;
import org.bloblines.data.game.Status;

public abstract class Character {

	private static int characterId = 1;

	/** Character Name */
	public String name;

	private int id = characterId++;

	protected Map<Attributes, Integer> attributesBase = new HashMap<>();

	protected Map<Attributes, Integer> attributesCurrent = new HashMap<>();

	public Status status = Status.OK;

	public List<Atom> atoms = new ArrayList<>();

	public boolean firstSkillDone = false;
	public boolean secondSkillDone = false;

	public enum Attributes {
		HP, SPEED, STRENGTH, INTELLIGENCE, WISDOM
	}

	/**
	 * Initializes attributes and set current attributes with base values
	 * 
	 * @param hp
	 *            HP base value
	 * @param intelligence
	 *            INTELLIGENCE base value
	 * @param speed
	 *            SPEED base value
	 * @param wisdom
	 *            WISDOM base value
	 * @param strength
	 *            STRENGTH base value
	 */
	public void initializeAttributes(Integer hp, Integer intelligence, Integer speed, Integer wisdom, Integer strength) {
		setAttributeBase(Attributes.HP, hp);
		setAttributeBase(Attributes.INTELLIGENCE, intelligence);
		setAttributeBase(Attributes.SPEED, speed);
		setAttributeBase(Attributes.WISDOM, wisdom);
		setAttributeBase(Attributes.STRENGTH, strength);
		initializeCurrentAttributes();
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

	public int getAttributeBase(Attributes attr) {
		return attributesBase.get(attr);
	}

	public void setAttributeBase(Attributes attr, int value) {
		attributesBase.put(attr, value);
	}

	/**
	 * Initializes current attributes with base values
	 */
	public void initializeCurrentAttributes() {
		for (Entry<Attributes, Integer> attrEntry : attributesBase.entrySet()) {
			attributesCurrent.put(attrEntry.getKey(), attrEntry.getValue());
		}
	}

	public int getAttributeCurrent(Attributes attr) {
		return attributesCurrent.get(attr);
	}

	public int changeAttributeCurrent(Attributes attr, int value) {
		attributesCurrent.put(attr, Integer.max(attributesCurrent.get(attr) + value, 0));
		return attributesCurrent.get(attr);
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
