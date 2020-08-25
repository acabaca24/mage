package org.mage.test.cards.cost.modification;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.Test;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 *
 * @author LevelX2
 */
public class KaradorGhostChieftainTest extends CardTestPlayerBase {

    @Test
    public void castReducedTwo() {
        setStrictChooseMode(true);

        addCard(Zone.BATTLEFIELD, playerA, "Swamp", 1);
        addCard(Zone.BATTLEFIELD, playerA, "Forest", 1);
        addCard(Zone.BATTLEFIELD, playerA, "Plains", 1);
        addCard(Zone.BATTLEFIELD, playerA, "Island", 3);
        addCard(Zone.GRAVEYARD, playerA, "Silvercoat Lion", 2);
        // Karador, Ghost Chieftain costs {1} less to cast for each creature card in your graveyard.
        // During each of your turns, you may cast one creature card from your graveyard.
        addCard(Zone.HAND, playerA, "Karador, Ghost Chieftain");// {5}{B}{G}{W}

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Karador, Ghost Chieftain");
        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();
        assertAllCommandsUsed();
        assertPermanentCount(playerA, "Karador, Ghost Chieftain", 1);
    }

    /**
     * I had a couple problems in a commander game last night. Using Karador,
     * Ghost Chieftain as my commander. Most of the match, his casting cost was
     * correctly calculated, reducing the extra commander tax and generic mana
     * costs by the number of creature cards in my graveyard. On the 4th cast
     * though, the cost was 12 mana total. I tried casting a few times over a
     * couple turns, but it was still an incorrect cost (I had probably 15
     * creatures in my graveyard).
     */
    @Test
    public void castReducedSeven() {
        setStrictChooseMode(true);

        addCard(Zone.BATTLEFIELD, playerA, "Swamp", 1);
        addCard(Zone.BATTLEFIELD, playerA, "Forest", 1);
        addCard(Zone.BATTLEFIELD, playerA, "Plains", 1);
        addCard(Zone.GRAVEYARD, playerA, "Silvercoat Lion", 7);
        // Karador, Ghost Chieftain costs {1} less to cast for each creature card in your graveyard.
        // During each of your turns, you may cast one creature card from your graveyard.
        addCard(Zone.HAND, playerA, "Karador, Ghost Chieftain");// {5}{B}{G}{W}

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Karador, Ghost Chieftain");
        setStopAt(1, PhaseStep.BEGIN_COMBAT);
        execute();
        assertAllCommandsUsed();
        assertPermanentCount(playerA, "Karador, Ghost Chieftain", 1);
    }

    @Test
    public void castCastMultipleFromGraveyard() {
        setStrictChooseMode(true);

        addCard(Zone.BATTLEFIELD, playerA, "Swamp", 1);
        addCard(Zone.BATTLEFIELD, playerA, "Forest", 1);
        addCard(Zone.BATTLEFIELD, playerA, "Plains", 4);
        addCard(Zone.GRAVEYARD, playerA, "Silvercoat Lion", 7);

        // Exile target creature you control, then return that card to the battlefield under your control.
        addCard(Zone.HAND, playerA, "Cloudshift");// Instant {W}
        // Karador, Ghost Chieftain costs {1} less to cast for each creature card in your graveyard.
        // During each of your turns, you may cast one creature card from your graveyard.
        addCard(Zone.HAND, playerA, "Karador, Ghost Chieftain");// {5}{B}{G}{W}

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Karador, Ghost Chieftain");

        castSpell(3, PhaseStep.PRECOMBAT_MAIN, playerA, "Silvercoat Lion");

        castSpell(3, PhaseStep.PRECOMBAT_MAIN, playerA, "Cloudshift", "Karador, Ghost Chieftain");

        castSpell(3, PhaseStep.PRECOMBAT_MAIN, playerA, "Silvercoat Lion");

        setStopAt(3, PhaseStep.BEGIN_COMBAT);
        execute();

        assertAllCommandsUsed();

        assertPermanentCount(playerA, "Silvercoat Lion", 2);
        assertGraveyardCount(activePlayer, "Cloudshift", 1);

        assertPermanentCount(playerA, "Karador, Ghost Chieftain", 1);
    }

    @Test
    // @Ignore // It's not possible yet to select which ability to use to allow a asThoughtAs effect
    public void test_castFromGraveyardWithDifferentApprovers() {
        setStrictChooseMode(true);

        addCard(Zone.BATTLEFIELD, playerA, "Plains", 4);
        addCard(Zone.BATTLEFIELD, playerA, "Forest", 1);
        addCard(Zone.BATTLEFIELD, playerA, "Swamp", 2);
        addCard(Zone.BATTLEFIELD, playerA, "Island", 3);

        // {1}{B}: Target attacking Zombie gains indestructible until end of turn.
        addCard(Zone.LIBRARY, playerA, "Accursed Horde", 1); // Creature Zombie {3}{B}
        skipInitShuffling();

        addCard(Zone.GRAVEYARD, playerA, "Silvercoat Lion", 5);

        // Karador, Ghost Chieftain costs {1} less to cast for each creature card in your graveyard.
        // During each of your turns, you may cast one creature card from your graveyard.
        addCard(Zone.HAND, playerA, "Karador, Ghost Chieftain");// {5}{B}{G}{W}

        // When Gisa and Geralf enters the battlefield, put the top four cards of your library into your graveyard.
        // During each of your turns, you may cast a Zombie creature card from your graveyard.
        addCard(Zone.HAND, playerA, "Gisa and Geralf"); // CREATURE {2}{U}{B} (4/4)

        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Karador, Ghost Chieftain");
        castSpell(1, PhaseStep.PRECOMBAT_MAIN, playerA, "Gisa and Geralf");

        castSpell(3, PhaseStep.PRECOMBAT_MAIN, playerA, "Accursed Horde");
        setChoice(playerA, "During each of your turns, you may cast a Zombie creature card from your graveyard"); // Choose the permitting object

        castSpell(3, PhaseStep.PRECOMBAT_MAIN, playerA, "Silvercoat Lion");

        setStopAt(3, PhaseStep.BEGIN_COMBAT);
        execute();

        assertAllCommandsUsed();

        assertPermanentCount(playerA, "Karador, Ghost Chieftain", 1);
        assertPermanentCount(playerA, "Gisa and Geralf", 1);

        assertPermanentCount(playerA, "Silvercoat Lion", 1);
        assertPermanentCount(playerA, "Accursed Horde", 1);
    }

}
