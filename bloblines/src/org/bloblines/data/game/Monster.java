package org.bloblines.data.game;

import org.bloblines.data.battle.Character;
import org.bloblines.data.battle.Skill;

public class Monster extends Character {

	@Override
	public Skill getFirstSkill() {
		return null;
	}

	@Override
	public Skill getSecondSkill() {
		return null;
	}

	@Override
	public int getAttribute(Attributes attr) {
		return 0;
	}

}
