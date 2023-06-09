package me.winter.scene2dplus.utils.moredrawables;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;

/**
 * {@link MultiDrawable} made only with {@link TransformDrawable}
 * <p>
 * Created on 2021-01-23.
 *
 * @author Alexander Winter
 */
public class MultiTransformDrawable extends MultiDrawable implements TransformDrawable {
	public MultiTransformDrawable(TransformDrawable... drawables) {
		super(drawables);
	}

	@Override
	public void draw(Batch batch, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {
		for(Drawable drawable : getDrawables())
			((TransformDrawable)drawable).draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
	}
}
