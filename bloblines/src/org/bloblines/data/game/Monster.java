package org.bloblines.data.game;

import org.bloblines.data.battle.Character;
import org.bloblines.data.battle.Skill;
import org.bloblines.data.battle.Skill.Target;

public class Monster extends Character {

	@Override
	public Skill getFirstSkill() {
		Skill roots = new Skill();
		roots.name = "Roots";
		roots.target = Target.HIGH_HP;
		roots.value = 2;
		return roots;
	}

	@Override
	public Skill getSecondSkill() {
		return getFirstSkill();
	}

	@Override
	public int getAttribute(Attributes attr) {
		return 5;
	}

}
