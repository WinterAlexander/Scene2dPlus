package me.winter.scene2dplus.utils.moredrawables;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.badlogic.gdx.math.MathUtils.cosDeg;
import static com.badlogic.gdx.math.MathUtils.sinDeg;
import static java.lang.Math.abs;

/**
 * Drawable that draws a specified TextureRegion rotated by specified angle
 * <p>
 * Created on 2018-04-22.
 *
 * @author Alexander Winter
 */
public class RotatedTextureRegionDrawable extends TextureRegionDrawable {
	public float angle;

	public RotatedTextureRegionDrawable() {}

	public RotatedTextureRegionDrawable(TextureRegion region, float angle) {
		super(region);
		this.angle = angle;
	}

	public RotatedTextureRegionDrawable(RotatedTextureRegionDrawable drawable) {
		super(drawable);
		this.angle = drawable.angle;
	}

	@Override
	public void draw(Batch batch, float x, float y, float width, float height) {
		float w = width * abs(cosDeg(angle)) + height * abs(sinDeg(angle));
		float h = height * abs(cosDeg(angle)) + width * abs(sinDeg(angle));

		super.draw(batch,
				x + width / 2 - w / 2, y + height / 2 - h / 2,
				w / 2, h / 2,
				w, h,
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
		super.draw(batch, x, y, originX, originY, width, height, scaleX, scaleY, rotation + this.angle);
	}
}
