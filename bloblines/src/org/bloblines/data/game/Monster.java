package org.bloblines.data.game;

import org.bloblines.data.battle.Character;
import org.bloblines.data.battle.Skill;
import org.bloblines.data.battle.Skill.Target;

public class Monster extends Character {

	public Monster() {
		super();
		initializeAttributes(10, 10, 10, 10, 10);
	}

	@Override
	public Skill getFirstSkill() {
		Skill roots = new Skill();
		roots.name = "Roots";
		roots.attribute = Attributes.STRENGTH;
		roots.target = Target.HIGH_HP;
		roots.value = 2;
		return roots;
	}

	@Override
	public Skill getSecondSkill() {
		return getFirstSkill();
	}

}
