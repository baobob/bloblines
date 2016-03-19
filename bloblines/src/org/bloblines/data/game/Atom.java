package org.bloblines.data.game;

/**
 * Atoms give powers to characters. Atoms are cool.
 */
public class Atom {

	public enum Type {
		ANTIMONY, /**/
		ARSENIC, /**/
		BISMUTH, /**/
		CADMIUM, /**/
		CHROMIUM, /**/
		COBALT, /**/
		INDIUM, /**/
		IRON, /**/
		MANGANESE, /**/
		MOLYBDENUM, /**/
		NICKEL, /**/
		RHENIUM, /**/
		SELENIUM, /**/
		TANTALUM, /**/
		TELLURIUM, /**/
		TIN, /**/
		TITANIUM, /**/
		TUNGSTEN, /**/
		ZINC, /**/

		/** Gold group */
		GLOD, /**/
		COPPER, /**/
		LEAD, /**/
		ALUMINIUM, /**/
		MERCURY, /**/
		SILVER, /**/

		/** Platinum group */
		PLATINUM, /**/
		IRIDIUM, /**/
		OSMIUM, /**/
		PALLADIUM, /**/
		RHODIUM, /**/
		RUTHENIUM /**/
	}

	public Type type;
	public int level;

}
