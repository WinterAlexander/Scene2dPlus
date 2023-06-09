package me.winter.scene2dplus.utils.moredrawables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;

/**
 * Tints a {@link TransformDrawable} with a specified color
 * <p>
 * Created on 2021-01-23.
 *
 * @author Alexander Winter
 */
public class TintedTransformDrawable extends TintedDrawable implements TransformDrawable {
	public TintedTransformDrawable(TransformDrawable drawable, Color color) {
		super(drawable, color);
	}

	@Override
	public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
		float prevColor = batch.getPackedColor();
		batch.setColor(batch.getColor().mul(color));

		((TransformDrawable)drawable).draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation);

		batch.setPackedColor(prevColor);
	}
}
