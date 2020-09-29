package mage.cards.a;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.dynamicvalue.DynamicValue;
import mage.abilities.dynamicvalue.common.PermanentsOnBattlefieldCount;
import mage.abilities.effects.ContinuousEffectImpl;
import mage.abilities.effects.common.continuous.SetPowerToughnessSourceEffect;
import mage.abilities.mana.GreenManaAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.*;
import mage.filter.FilterPermanent;
import mage.filter.StaticFilters;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.predicate.Predicates;
import mage.filter.predicate.permanent.TokenPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;

import java.util.UUID;

/**
 * @author jmharmon
 */
public final class AshayaSoulOfTheWild extends CardImpl {

    private static final DynamicValue xValue
            = new PermanentsOnBattlefieldCount(StaticFilters.FILTER_CONTROLLED_PERMANENT_LANDS);

    public AshayaSoulOfTheWild(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{3}{G}{G}");

        this.addSuperType(SuperType.LEGENDARY);
        this.subtype.add(SubType.ELEMENTAL);
        this.power = new MageInt(0);
        this.toughness = new MageInt(0);

        // Ashaya, Soul of the Wild’s power and toughness are each equal to the number of lands you control.
        this.addAbility(new SimpleStaticAbility(
                Zone.ALL, new SetPowerToughnessSourceEffect(
                xValue, Duration.EndOfGame
        )));

        // Nontoken creatures you control are Forest lands in addition to their other types. (They’re still affected by summoning sickness.)
        this.addAbility(new SimpleStaticAbility(new AshayaSoulOfTheWildEffect()));
    }

    public AshayaSoulOfTheWild(final AshayaSoulOfTheWild card) {
        super(card);
    }

    @Override
    public AshayaSoulOfTheWild copy() {
        return new AshayaSoulOfTheWild(this);
    }
}

class AshayaSoulOfTheWildEffect extends ContinuousEffectImpl {

    private static final FilterPermanent filter
            = new FilterControlledCreaturePermanent();

    static {
        filter.add(Predicates.not(TokenPredicate.instance));
    }

    public AshayaSoulOfTheWildEffect() {
        super(Duration.WhileOnBattlefield, Layer.TypeChangingEffects_4, SubLayer.NA, Outcome.Neutral);
        staticText = "Nontoken creatures you control are Forest lands in addition to their other types";
        this.dependencyTypes.add(DependencyType.BecomeForest);
    }

    public AshayaSoulOfTheWildEffect(final AshayaSoulOfTheWildEffect effect) {
        super(effect);
    }

    @Override
    public AshayaSoulOfTheWildEffect copy() {
        return new AshayaSoulOfTheWildEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        for (Permanent permanent : game.getBattlefield().getActivePermanents(
                filter, source.getControllerId(), source.getSourceId(), game
        )) {
            if (!permanent.isLand()) {
                permanent.addCardType(CardType.LAND);
            }
            if (!permanent.hasSubtype(SubType.FOREST, game)) {
                permanent.getSubtype(game).add(SubType.FOREST);
                if (!permanent.getAbilities(game).containsClass(GreenManaAbility.class)) {
                    permanent.addAbility(new GreenManaAbility(), source.getSourceId(), game);
                }
            }
        }
        return true;
    }
}
