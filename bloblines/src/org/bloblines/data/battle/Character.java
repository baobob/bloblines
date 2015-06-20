package org.bloblines.data.battle;

import org.bloblines.data.game.Status;

public abstract class Character {

	public String name;
	public int lifeMax;
	public int lifeCurrent;

	public Status status = Status.OK;

	public boolean firstSkillDone = false;
	public boolean secondSkillDone = false;

	public enum Attributes {
		SPEED, STRENGTH, INTELLIGENCE, WISDOM
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

	public abstract int getAttribute(Attributes attr);
}
