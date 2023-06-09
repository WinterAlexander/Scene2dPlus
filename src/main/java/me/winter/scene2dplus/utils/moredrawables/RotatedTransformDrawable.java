package me.winter.scene2dplus.utils.moredrawables;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;

/**
 * Drawable that draws a specified {@link TransformDrawable} rotated at a
 * specific angle
 * <p>
 * Created on 2021-01-23.
 *
 * @author Alexander Winter
 */
public class RotatedTransformDrawable extends WrapperDrawable implements TransformDrawable {
	private final float angle;

	public RotatedTransformDrawable(TransformDrawable drawable, float angle) {
		super(drawable);
		this.angle = angle;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height) {
		((TransformDrawable)drawable).draw(batch,
				x, y,
				width / 2f, height / 2f,
				width, height,
				1f, 1f,
				angle);
	}

	@Override
	public void draw(Batch batch,
	                 float x, float y,
	                 float originX, float originY,
	                 float width, float height,
	                 float scaleX, float scaleY,
	                 float rotation) {
		((TransformDrawable)drawable).draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation + this.angle);
	}
}
