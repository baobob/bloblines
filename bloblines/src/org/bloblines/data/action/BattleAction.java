package org.bloblines.data.action;

import org.bloblines.data.battle.Battle;
import org.bloblines.data.battle.Environment;
import org.bloblines.data.battle.Party;
import org.bloblines.data.game.Monster;
import org.bloblines.data.game.Player;
import org.bloblines.data.map.ActionType;
import org.bloblines.data.map.Biome;

public class BattleAction extends Action {

	public BattleAction(String description, Biome biome) {
		super(ActionType.FIGHT, description, biome);
	}

	/**
	 * Return a new unprocessed Battle for Player.
	 * 
	 * @param player
	 * @param action
	 * @return
	 */
	public Battle getBattle(Player player) {
		Party p1 = new Party(player.name);
		p1.characters.addAll(player.blobs);

		Party p2 = new Party("Enemies");
		p2.characters.add(getRandomMonster(biome));
		p2.characters.add(getRandomMonster(biome));

		Environment env = new Environment();
		return new Battle(p1, p2, env);
	}

	private Monster getRandomMonster(Biome biome) {
		Monster monster = new Monster();
		switch (biome) {
		case BEACH:
			monster.name = "Shell";
			break;
		case GRASSLAND:
			monster.name = "Cucumber";
			break;
		case HILL:
			monster.name = "Cabbage";
			break;
		case MOUNTAIN:
			monster.name = "Radish";
			break;
		case OCEAN:
			monster.name = "Coral";
			break;
		default:
			monster.name = "Potato";
			break;
		}
		return monster;
	}

}
