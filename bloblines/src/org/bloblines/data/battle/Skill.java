package org.bloblines.data.battle;

import org.bloblines.data.battle.Character.Attributes;

public class Skill {

	public String name;

	public enum Target {
		ALL, LOW_HP, HIGH_HP, LOW_SPEED, HIGH_SPEED
	}

	public Target target;
	public int value;
	public Attributes attribute;
}
