package org.mage.card.arcane;

import mage.cards.ArtRect;
import mage.view.CardView;

/**
 * Created by StravantUser on 2017-03-30.
 */
public class CardRendererFactory {

    public CardRendererFactory() {
    }

    public CardRenderer create(CardView card) {
        if (card.isSplitCard() && card.getArtRect() != ArtRect.SPLIT_FUSED) {
            // Split fused cards still render with the normal frame, showing all abilities
            // from both halves in one frame.
            return new ModernSplitCardRenderer(card);
        } else {
            return new ModernCardRenderer(card);
        }
    }
}
