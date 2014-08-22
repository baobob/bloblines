package org.bloblines.data.event;

import java.util.Random;

import org.bloblines.data.game.Player;
import org.bloblines.data.world.Area;

/**
 * Event with a random chance of happening
 * @author mrflibble
 * 
 */
public class RandomEvent extends Event {

	public int frequency;

	public static final Random R = new Random();

	/**
	 * This RandomEvent has one chance out of "frequency" to happen at each test
	 * @param a Area where this event may occur
	 * @param frequency integer between 1 and as many as you want
	 */
	public RandomEvent(Area a, int frequency) {
		super(a);
		this.frequency = frequency;
	}

	public boolean happens(Player player) {
		return R.nextInt(frequency) == 0;
	}

}
