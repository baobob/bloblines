package org.bloblines.data.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bloblines.data.battle.Character.Attributes;
import org.bloblines.data.battle.Log.Type;
import org.bloblines.data.game.Status;

/**
 * A battle is a fight between 2 parties. Usually, the player party and a group of enemy. Battle system should work with 2 Non playable
 * parties (sort of battle cut scene ?).
 * 
 * There are no player interaction during battle at the moment although we may need this at some point. (start a fury attack once a day ?
 * revive a character by consuming a one-time-only item, something like that).
 * 
 * Battle is turn-based.
 * 
 * Each character in the battle has 2 active skill slots and 1 passive skill slot.
 * 
 * All passive skills of all characters apply to the battle at all time, except when skill owner is killed, stunned, asleep, etc.
 * 
 * Environment / Battleground may add special passive effect (forest environment may heal mushrooms at each turn, underground environment
 * may lower lightning skills power, etc.)
 * 
 */
public class Battle {

	public Party party1;
	public Party party2;
	public Environment environment;
	public List<Character> characters = new ArrayList<>();

	public List<Effect> effects = new ArrayList<>();

	public Party winner = null;

	public List<Log> logs = new ArrayList<>();

	public enum OUTCOME {
		WIN, // Party 1 wins, party 2 loses
		LOSE, // Party 1 loses, party 2 wins
		DRAW // No one wins
	}

	public Battle(Party p1, Party p2, Environment e) {
		party1 = p1;
		party2 = p2;
		environment = e;
		prepare();
	}

	private void prepare() {
		// Get all characters (players AND monsters)
		characters.addAll(party1.characters);
		characters.addAll(party2.characters);

		// TODO : Collect effects from blobs passive skills / environment

	}

	/**
	 * A classic battle lasts 1 round only. But special battles may extend to more than 1 round. During a round, each character will use 2
	 * active skills. Each character has a "SPEED" attribute. Turn order is based on this attribute.<br/>
	 * <br/>
	 * Example : Team 1<br/>
	 * <br/>
	 * Bob - SPD 12. Skills : Fire attack / Ice attack<br/>
	 * John - SPD 9. Skills : Regen Party (heal party each time it's a party's member turn) / Revive Ally<br/>
	 * <br/>
	 * Team 2 :<br/>
	 * Carrot - SPD 7. Skills : Bite / Bite<br/>
	 * Radish - SPD 14. Skills : Radish powder (cancels skill of next enemy) / Low kick<br/>
	 * <br/>
	 * 
	 * What would happen :
	 * 
	 * <pre>
	 * Radish launches "Radish powder". 
	 * Bob Fire attack is cancelled. 
	 * Bob does nothing (his attack has been cancelled) 
	 * John launches Regen Party. 
	 * Carrot bites Bob, does 5 damage. 
	 * Radish low kicks Bob. Does 7 damage. 
	 * Regen party Bob and John heal 2 HP. 
	 * Bob lauch Ice attack. Carrot takes 10 damage. Carrot is dead. 
	 * Regen party Bob and John heal 2 HP. 
	 * John launches Revive Ally. Nothing happened.
	 * Carrot is dead, it doesn't play anymore.
	 * </pre>
	 * 
	 * As this is a classic fight, it ends and winner is determined with very complex rules (compare (sum initial HP - sum final HP) of each
	 * party).
	 * 
	 * A special fight may lasts for more than one round, or lasts until death of all members of a party.
	 * 
	 * @param rounds
	 */
	public void process(int rounds) {
		logs.add(new Log(Type.BATTLE_INFO, "Start of Battle"));
		// TODO battle passive effects

		// Process turns
		for (int i = 1; i <= rounds; i++) {
			// TODO turn effects
			logs.add(new Log(Type.BATTLE_INFO, "Round " + i));

			Character character = getNextCharacter(true);
			while (character != null) {
				logs.add(new Log(Type.BATTLE_INFO, "Next character: " + character.name));
				// for each character
				// TODO onCharacter effect

				Skill skill = character.getFirstSkill();
				doSomething(character, skill);
				logs.add(new Log(Type.BATTLE_INFO, character.name + " uses skill " + skill.name));
				character.firstSkillDone = true;
				// TODO postCharacter effect
				character = getNextCharacter(true);
			}

			character = getNextCharacter(false);
			while (character != null) {
				// for each character
				// TODO onCharacter effect

				Skill skill = character.getSecondSkill();
				// TODO use skill
				logs.add(new Log(Type.BATTLE_INFO, character.name + " uses skill " + skill.name));
				character.secondSkillDone = true;

				// TODO postCharacter effect
				character = getNextCharacter(false);
			}
		}

		logs.add(new Log(Type.BATTLE_INFO, "End of Battle"));
		computeWinner();
	}

	private void doSomething(Character currentCharacter, Skill skill) {
		Party allies = party1;
		Party enemies = party2;
		if (!party1.characters.contains(currentCharacter)) {
			allies = party2;
			enemies = party1;
		}

		List<Character> targets = new ArrayList<>();
		switch (skill.target) {
		case ALL:
			targets = characters;
			break;
		case HIGH_HP:
			sortCharacters(enemies.characters, Attributes.HP, true);
			targets.add(enemies.characters.get(0));
			break;
		case HIGH_SPEED:
			sortCharacters(enemies.characters, Attributes.SPEED, true);
			targets.add(enemies.characters.get(0));
			break;
		case LOW_HP:
			sortCharacters(enemies.characters, Attributes.HP, false);
			targets.add(enemies.characters.get(0));
			break;
		case LOW_SPEED:
			sortCharacters(enemies.characters, Attributes.SPEED, false);
			targets.add(enemies.characters.get(0));
			break;
		default:
			break;
		}

		int damage = skill.value * currentCharacter.getAttributeCurrent(skill.attribute);
		for (Character c : targets) {
			logs.add(new Log(Type.DAMAGE, "Character " + c.name + " took " + damage + " damage."));
			if (c.changeAttributeCurrent(Attributes.HP, -damage) == 0) {
				logs.add(new Log(Type.BATTLE_INFO, "Character " + c.name + " is now dead. "));
				c.status = Status.DEAD;
			}
		}
	}

	private void sortCharacters(List<Character> chars, final Attributes attr, boolean descending) {
		Collections.sort(chars, new Comparator<Character>() {
			@Override
			public int compare(Character c0, Character c1) {
				return c1.getAttributeCurrent(attr) - c0.getAttributeCurrent(attr);
			}
		});
		if (descending) {
			Collections.reverse(chars);
		}
	}

	/**
	 * Gets next character to play
	 * 
	 * @param firstSkill
	 *            <code>true</code> if we want next character to play its first skill, <code>false</code> otherwise
	 * @return Next character to play, null if there is none
	 */
	private Character getNextCharacter(boolean firstSkill) {
		List<Character> remainingChars = new ArrayList<>();
		for (Character c : characters) {
			if (c.status == Status.DEAD || c.status == Status.SLEEP || c.status == Status.STUN) {
				continue;
			}
			if (firstSkill && !c.firstSkillDone || !firstSkill && !c.secondSkillDone) {
				remainingChars.add(c);
			}
		}
		if (remainingChars.size() == 0) {
			return null;
		}
		sortCharacters(remainingChars, Attributes.SPEED, false);
		return remainingChars.get(0);
	}

	private void computeWinner() {

	}
}
