package org.bloblines.data.battle;

import java.util.ArrayList;
import java.util.List;

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
		// TODO battle passive effects

		// Process turns
		for (int i = 1; i <= rounds; i++) {
			// TODO turn effects

			Character character = getNextCharacter(true);
			while (character != null) {
				// for each character
				// TODO onCharacter effect

				Skill skill = character.getFirstSkill();
				// TODO use skill

				// TODO postCharacter effect
				character = getNextCharacter(true);
			}

			character = getNextCharacter(false);
			while (character != null) {
				// for each character
				// TODO onCharacter effect

				Skill skill = character.getSecondSkill();
				// TODO use skill

				// TODO postCharacter effect
				character = getNextCharacter(false);
			}
		}

		computeWinner();
	}

	private Character getNextCharacter(boolean firstSkill) {
		return null;
	}

	private void computeWinner() {

	}
}
