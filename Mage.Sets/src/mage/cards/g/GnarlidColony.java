package mage.cards.g;

import mage.MageInt;
import mage.abilities.common.EntersBattlefieldAbility;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.condition.common.KickedCondition;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.continuous.GainAbilityAllEffect;
import mage.abilities.effects.common.counter.AddCountersSourceEffect;
import mage.abilities.keyword.KickerAbility;
import mage.abilities.keyword.TrampleAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.SubType;
import mage.counters.CounterType;
import mage.filter.FilterPermanent;
import mage.filter.common.FilterControlledCreaturePermanent;
import mage.filter.predicate.permanent.CounterPredicate;

import java.util.UUID;

/**
 * @author TheElk801
 */
public final class GnarlidColony extends CardImpl {

    private static final FilterPermanent filter = new FilterControlledCreaturePermanent();

    static {
        filter.add(new CounterPredicate(CounterType.P1P1));
    }

    public GnarlidColony(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{1}{G}");

        this.subtype.add(SubType.BEAST);
        this.power = new MageInt(2);
        this.toughness = new MageInt(2);

        // Kicker {2}{G}
        this.addAbility(new KickerAbility(new ManaCostsImpl("{2}{G}")));

        // If Gnarlid Colony was kicked, it enters the battlefield with two +1/+1 counters on it.
        this.addAbility(new EntersBattlefieldAbility(
                new AddCountersSourceEffect(CounterType.P1P1.createInstance(2)), KickedCondition.instance,
                "If {this} was kicked, it enters the battlefield with two +1/+1 counters on it.", ""
        ));

        // Each creature you control with a +1/+1 counter on it has trample.
        this.addAbility(new SimpleStaticAbility(new GainAbilityAllEffect(
                TrampleAbility.getInstance(), Duration.WhileOnBattlefield, filter,
                "Each creature you control with a +1/+1 counter on it has trample"
        )));
    }

    private GnarlidColony(final GnarlidColony card) {
        super(card);
    }

    @Override
    public GnarlidColony copy() {
        return new GnarlidColony(this);
    }
}
