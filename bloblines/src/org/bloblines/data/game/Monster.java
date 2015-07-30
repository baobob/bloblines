package org.bloblines.data.game;

import org.bloblines.data.battle.Character;
import org.bloblines.data.battle.Skill;
import org.bloblines.data.battle.Skill.Target;

public class Monster extends Character {

	public Monster() {
		super();
		setAttribute(Attributes.HP, 10);
		setAttribute(Attributes.CURRENT_HP, 10);
		setAttribute(Attributes.INTELLIGENCE, 10);
		setAttribute(Attributes.CURRENT_INTELLIGENCE, 10);
		setAttribute(Attributes.SPEED, 10);
		setAttribute(Attributes.CURRENT_SPEED, 10);
		setAttribute(Attributes.WISDOM, 10);
		setAttribute(Attributes.CURRENT_WISDOM, 10);
		setAttribute(Attributes.STRENGTH, 10);
		setAttribute(Attributes.CURRENT_STRENGTH, 10);
	}

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
