package me.winter.scene2dplus.utils.moredrawables;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.function.BooleanSupplier;

/**
 * Drawable which only draws if a specified condition is met
 * <p>
 * Created on 2021-11-01.
 *
 * @author Alexander Winter
 */
public class ConditionDrawable extends WrapperDrawable {
	public BooleanSupplier enabled;

	public ConditionDrawable(Drawable drawable, BooleanSupplier enabled) {
		super(drawable);

		this.enabled = enabled;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height) {
		if(enabled.getAsBoolean())
			super.draw(batch, x, y, width, height);
	}
}
