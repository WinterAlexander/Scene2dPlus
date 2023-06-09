package me.winter.scene2dplus.utils.moredrawables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.badlogic.gdx.utils.Array;

import java.util.function.Function;

import static me.winter.gdx.utils.Validation.ensureNotNull;

/**
 * {@link MultiDrawable} made with a bunch of texture regions
 * <p>
 * Created on 2021-01-10.
 *
 * @author Alexander Winter
 */
public class MultiTextureRegionDrawable extends MultiTransformDrawable {
	public MultiTextureRegionDrawable(TextureRegion... regions) {
		this(new Array<>(regions));
	}

	public MultiTextureRegionDrawable(Array<TextureRegion> regions) {
		super(makeDrawables(regions, TextureRegionDrawable::new));
	}

	public MultiTextureRegionDrawable(Array<TextureRegion> regions, Array<Color> colors) {
		super(makeDrawables(regions, colors, TextureRegionDrawable::new));
	}

	public MultiTextureRegionDrawable(Array<TextureRegion> regions, Function<TextureRegion, ? extends TransformDrawable> constructor) {
		super(makeDrawables(regions, constructor));
	}

	public MultiTextureRegionDrawable(Array<TextureRegion> regions, Array<Color> colors, Function<TextureRegion, ? extends TransformDrawable> constructor) {
		super(makeDrawables(regions, colors, constructor));
	}

	private static TransformDrawable[] makeDrawables(Array<TextureRegion> regions, Function<TextureRegion, ? extends TransformDrawable> constructor) {
		TransformDrawable[] drawables = new TransformDrawable[regions.size];

		for(int i = 0; i < regions.size; i++)
			drawables[i] = constructor.apply(regions.get(i));
		return drawables;
	}

	private static TransformDrawable[] makeDrawables(Array<TextureRegion> regions, Array<Color> colors, Function<TextureRegion, ? extends TransformDrawable> constructor) {
		ensureNotNull(regions, "regions");
		ensureNotNull(colors, "colors");

		if(regions.size != colors.size)
			throw new IllegalArgumentException("Different number of regions and colors");

		TransformDrawable[] drawables = new TransformDrawable[regions.size];

		for(int i = 0; i < regions.size; i++) {
			drawables[i] = new TintedTransformDrawable(constructor.apply(regions.get(i)), colors.get(i));
		}
		return drawables;
	}
}
