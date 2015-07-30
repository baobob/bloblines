package org.bloblines.data.game;

import org.bloblines.data.battle.Character;
import org.bloblines.data.battle.Skill;
import org.bloblines.data.battle.Skill.Target;

public class Blob extends Character {

	public int age;

	@Override
	public Skill getFirstSkill() {
		Skill attack = new Skill();
		attack.name = "Attack skill based on Strength";
		attack.target = Target.HIGH_HP;
		attack.value = 3;
		attack.attribute = Attributes.STRENGTH;
		return attack;
	}

	@Override
	public Skill getSecondSkill() {
		Skill fire = new Skill();
		fire.name = "Fire skill based on Intelligence";
		fire.target = Target.LOW_HP;
		fire.value = 5;
		fire.attribute = Attributes.INTELLIGENCE;
		return fire;
	}

	@Override
	public int getAttribute(Attributes attr) {
		return 10;
	}

}
