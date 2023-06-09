package me.winter.scene2dplus.ui;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Pool.Poolable;
import me.winter.scene2dplus.Actor;

import static me.winter.gdx.utils.Validation.ensureNotNull;
import static me.winter.gdx.utils.Validation.ensurePositive;

/**
 * A cell for a {@link Table}.
 *
 * @author Nathan Sweet
 */
@SuppressWarnings("unchecked")
public class Cell<T extends Actor> implements Poolable {
	private static final Float zerof = 0f, onef = 1f;
	private static final Integer zeroi = 0, onei = 1;
	private static final Integer centeri = onei, topi = Align.top, bottomi = Align.bottom, lefti = Align.left,
			righti = Align.right;

	private static Files files;
	private static Cell<?> defaults;

	Value minWidth, minHeight;
	Value prefWidth, prefHeight;
	Value maxWidth, maxHeight;
	Value spaceTop, spaceLeft, spaceBottom, spaceRight;
	Value padTop, padLeft, padBottom, padRight;
	Float fillX, fillY;
	Integer align;
	Integer expandX, expandY;
	Integer colspan;
	Boolean uniformX, uniformY;

	@Null
	Actor actor;
	float actorX, actorY;
	float actorWidth, actorHeight;

	private Table table;
	boolean endRow;
	int column, row;
	int cellAboveIndex;
	float computedPadTop, computedPadLeft, computedPadBottom, computedPadRight;

	public Cell() {
		cellAboveIndex = -1;
		Cell<?> defaults = defaults();
		if(defaults != null)
			set(defaults);
	}

	public void setTable(Table table) {
		this.table = table;
	}

	/**
	 * Sets the actor in this cell and adds the actor to the cell's table. If null, removes any current actor.
	 */
	public <A extends Actor> Cell<A> setActor(@Null A newActor) {
		if(actor != newActor) {
			if(actor != null && actor.getParent() == table)
				actor.remove();
			actor = newActor;
			if(newActor != null)
				table.addActor(newActor);
		}
		return (Cell<A>)this;
	}

	/**
	 * Removes the current actor for the cell, if any.
	 */
	public Cell<T> clearActor() {
		setActor(null);
		return this;
	}

	/**
	 * Returns the actor for this cell, or null.
	 */
	public @Null T getActor() {
		return (T)actor;
	}

	/**
	 * Returns true if the cell's actor is not null.
	 */
	public boolean hasActor() {
		return actor != null;
	}

	/**
	 * Sets the minWidth, prefWidth, maxWidth, minHeight, prefHeight, and maxHeight to the specified value.
	 */
	public Cell<T> size(Value size) {
		ensureNotNull(size, "size");
		minWidth = size;
		minHeight = size;
		prefWidth = size;
		prefHeight = size;
		maxWidth = size;
		maxHeight = size;
		return this;
	}

	/**
	 * Sets the minWidth, prefWidth, maxWidth, minHeight, prefHeight, and maxHeight to the specified values.
	 */
	public Cell<T> size(Value width, Value height) {
		ensureNotNull(width, "width");
		ensureNotNull(height, "height");
		minWidth = width;
		minHeight = height;
		prefWidth = width;
		prefHeight = height;
		maxWidth = width;
		maxHeight = height;
		return this;
	}

	/**
	 * Sets the minWidth, prefWidth, maxWidth, minHeight, prefHeight, and maxHeight to the specified value.
	 */
	public Cell<T> size(float size) {
		size(Value.Fixed.valueOf(size));
		return this;
	}

	/**
	 * Sets the minWidth, prefWidth, maxWidth, minHeight, prefHeight, and maxHeight to the specified values.
	 */
	public Cell<T> size(float width, float height) {
		size(Value.Fixed.valueOf(width), Value.Fixed.valueOf(height));
		return this;
	}

	/**
	 * Sets the minWidth, prefWidth, and maxWidth to the specified value.
	 */
	public Cell<T> width(Value width) {
		ensureNotNull(width, "width");
		minWidth = width;
		prefWidth = width;
		maxWidth = width;
		return this;
	}

	/**
	 * Sets the minWidth, prefWidth, and maxWidth to the specified value.
	 */
	public Cell<T> width(float width) {
		width(Value.Fixed.valueOf(width));
		return this;
	}

	/**
	 * Sets the minHeight, prefHeight, and maxHeight to the specified value.
	 */
	public Cell<T> height(Value height) {
		ensureNotNull(height, "height");
		minHeight = height;
		prefHeight = height;
		maxHeight = height;
		return this;
	}

	/**
	 * Sets the minHeight, prefHeight, and maxHeight to the specified value.
	 */
	public Cell<T> height(float height) {
		height(Value.Fixed.valueOf(height));
		return this;
	}

	/**
	 * Sets the minWidth and minHeight to the specified value.
	 */
	public Cell<T> minSize(Value size) {
		ensureNotNull(size, "size");
		minWidth = size;
		minHeight = size;
		return this;
	}

	/**
	 * Sets the minWidth and minHeight to the specified values.
	 */
	public Cell<T> minSize(Value width, Value height) {
		ensureNotNull(width, "width");
		ensureNotNull(height, "height");
		minWidth = width;
		minHeight = height;
		return this;
	}

	public Cell<T> minWidth(Value minWidth) {
		ensureNotNull(minWidth, "minWidth");
		this.minWidth = minWidth;
		return this;
	}

	public Cell<T> minHeight(Value minHeight) {
		ensureNotNull(minHeight, "minHeight");
		this.minHeight = minHeight;
		return this;
	}

	/**
	 * Sets the minWidth and minHeight to the specified value.
	 */
	public Cell<T> minSize(float size) {
		minSize(Value.Fixed.valueOf(size));
		return this;
	}

	/**
	 * Sets the minWidth and minHeight to the specified values.
	 */
	public Cell<T> minSize(float width, float height) {
		minSize(Value.Fixed.valueOf(width), Value.Fixed.valueOf(height));
		return this;
	}

	public Cell<T> minWidth(float minWidth) {
		this.minWidth = Value.Fixed.valueOf(minWidth);
		return this;
	}

	public Cell<T> minHeight(float minHeight) {
		this.minHeight = Value.Fixed.valueOf(minHeight);
		return this;
	}

	/**
	 * Sets the prefWidth and prefHeight to the specified value.
	 */
	public Cell<T> prefSize(Value size) {
		ensureNotNull(size, "size");
		prefWidth = size;
		prefHeight = size;
		return this;
	}

	/**
	 * Sets the prefWidth and prefHeight to the specified values.
	 */
	public Cell<T> prefSize(Value width, Value height) {
		ensureNotNull(width, "width");
		ensureNotNull(height, "height");
		prefWidth = width;
		prefHeight = height;
		return this;
	}

	public Cell<T> prefWidth(Value prefWidth) {
		ensureNotNull(prefWidth, "prefWidth");
		this.prefWidth = prefWidth;
		return this;
	}

	public Cell<T> prefHeight(Value prefHeight) {
		ensureNotNull(prefHeight, "prefHeight");
		this.prefHeight = prefHeight;
		return this;
	}

	/**
	 * Sets the prefWidth and prefHeight to the specified value.
	 */
	public Cell<T> prefSize(float width, float height) {
		prefSize(Value.Fixed.valueOf(width), Value.Fixed.valueOf(height));
		return this;
	}

	/**
	 * Sets the prefWidth and prefHeight to the specified values.
	 */
	public Cell<T> prefSize(float size) {
		prefSize(Value.Fixed.valueOf(size));
		return this;
	}

	public Cell<T> prefWidth(float prefWidth) {
		this.prefWidth = Value.Fixed.valueOf(prefWidth);
		return this;
	}

	public Cell<T> prefHeight(float prefHeight) {
		this.prefHeight = Value.Fixed.valueOf(prefHeight);
		return this;
	}

	/**
	 * Sets the maxWidth and maxHeight to the specified value. If the max size is 0, no maximum size is used.
	 */
	public Cell<T> maxSize(Value size) {
		ensureNotNull(size, "size");
		maxWidth = size;
		maxHeight = size;
		return this;
	}

	/**
	 * Sets the maxWidth and maxHeight to the specified values. If the max size is 0, no maximum size is used.
	 */
	public Cell<T> maxSize(Value width, Value height) {
		ensureNotNull(width, "width");
		ensureNotNull(height, "height");
		maxWidth = width;
		maxHeight = height;
		return this;
	}

	/**
	 * If the maxWidth is 0, no maximum width is used.
	 */
	public Cell<T> maxWidth(Value maxWidth) {
		ensureNotNull(maxWidth, "maxWidth");
		this.maxWidth = maxWidth;
		return this;
	}

	/**
	 * If the maxHeight is 0, no maximum height is used.
	 */
	public Cell<T> maxHeight(Value maxHeight) {
		ensureNotNull(maxHeight, "maxHeight");
		this.maxHeight = maxHeight;
		return this;
	}

	/**
	 * Sets the maxWidth and maxHeight to the specified value. If the max size is 0, no maximum size is used.
	 */
	public Cell<T> maxSize(float size) {
		maxSize(Value.Fixed.valueOf(size));
		return this;
	}

	/**
	 * Sets the maxWidth and maxHeight to the specified values. If the max size is 0, no maximum size is used.
	 */
	public Cell<T> maxSize(float width, float height) {
		maxSize(Value.Fixed.valueOf(width), Value.Fixed.valueOf(height));
		return this;
	}

	/**
	 * If the maxWidth is 0, no maximum width is used.
	 */
	public Cell<T> maxWidth(float maxWidth) {
		this.maxWidth = Value.Fixed.valueOf(maxWidth);
		return this;
	}

	/**
	 * If the maxHeight is 0, no maximum height is used.
	 */
	public Cell<T> maxHeight(float maxHeight) {
		this.maxHeight = Value.Fixed.valueOf(maxHeight);
		return this;
	}

	/**
	 * Sets the spaceTop, spaceLeft, spaceBottom, and spaceRight to the specified value.
	 */
	public Cell<T> space(Value space) {
		ensureNotNull(space, "space");
		spaceTop = space;
		spaceLeft = space;
		spaceBottom = space;
		spaceRight = space;
		return this;
	}

	public Cell<T> space(Value top, Value left, Value bottom, Value right) {
		ensureNotNull(top, "top");
		ensureNotNull(left, "left");
		ensureNotNull(bottom, "bottom");
		ensureNotNull(right, "right");
		spaceTop = top;
		spaceLeft = left;
		spaceBottom = bottom;
		spaceRight = right;
		return this;
	}

	public Cell<T> spaceTop(Value spaceTop) {
		ensureNotNull(spaceTop, "spaceTop");
		this.spaceTop = spaceTop;
		return this;
	}

	public Cell<T> spaceLeft(Value spaceLeft) {
		ensureNotNull(spaceLeft, "spaceLeft");
		this.spaceLeft = spaceLeft;
		return this;
	}

	public Cell<T> spaceBottom(Value spaceBottom) {
		ensureNotNull(spaceBottom, "spaceBottom");
		this.spaceBottom = spaceBottom;
		return this;
	}

	public Cell<T> spaceRight(Value spaceRight) {
		ensureNotNull(spaceRight, "spaceRight");
		this.spaceRight = spaceRight;
		return this;
	}

	/**
	 * Sets the spaceTop, spaceLeft, spaceBottom, and spaceRight to the specified value. The space cannot be less than 0.
	 */
	public Cell<T> space(float space) {
		ensurePositive(space, "space");
		space(Value.Fixed.valueOf(space));
		return this;
	}

	/**
	 * The space cannot be less than 0.
	 */
	public Cell<T> space(float top, float left, float bottom, float right) {
		ensurePositive(top, "top");
		ensurePositive(left, "left");
		ensurePositive(bottom, "bottom");
		ensurePositive(right, "right");
		space(Value.Fixed.valueOf(top), Value.Fixed.valueOf(left), Value.Fixed.valueOf(bottom), Value.Fixed.valueOf(right));
		return this;
	}

	/**
	 * The space cannot be less than 0.
	 */
	public Cell<T> spaceTop(float spaceTop) {
		ensurePositive(spaceTop, "spaceTop");
		this.spaceTop = Value.Fixed.valueOf(spaceTop);
		return this;
	}

	/**
	 * The space cannot be less than 0.
	 */
	public Cell<T> spaceLeft(float spaceLeft) {
		ensurePositive(spaceLeft, "spaceLeft");
		this.spaceLeft = Value.Fixed.valueOf(spaceLeft);
		return this;
	}

	/**
	 * The space cannot be less than 0.
	 */
	public Cell<T> spaceBottom(float spaceBottom) {
		ensurePositive(spaceBottom, "spaceBottom");
		this.spaceBottom = Value.Fixed.valueOf(spaceBottom);
		return this;
	}

	/**
	 * The space cannot be less than 0.
	 */
	public Cell<T> spaceRight(float spaceRight) {
		ensurePositive(spaceRight, "spaceRight");
		this.spaceRight = Value.Fixed.valueOf(spaceRight);
		return this;
	}

	/**
	 * Sets the padTop, padLeft, padBottom, and padRight to the specified value.
	 */
	public Cell<T> pad(Value pad) {
		ensureNotNull(pad, "pad");
		padTop = pad;
		padLeft = pad;
		padBottom = pad;
		padRight = pad;
		return this;
	}

	public Cell<T> pad(Value top, Value left, Value bottom, Value right) {
		ensureNotNull(top, "top");
		ensureNotNull(left, "left");
		ensureNotNull(bottom, "bottom");
		ensureNotNull(right, "right");
		padTop = top;
		padLeft = left;
		padBottom = bottom;
		padRight = right;
		return this;
	}

	public Cell<T> padTop(Value padTop) {
		ensureNotNull(padTop, "padTop");
		this.padTop = padTop;
		return this;
	}

	public Cell<T> padLeft(Value padLeft) {
		ensureNotNull(padLeft, "padLeft");
		this.padLeft = padLeft;
		return this;
	}

	public Cell<T> padBottom(Value padBottom) {
		ensureNotNull(padBottom, "padBottom");
		this.padBottom = padBottom;
		return this;
	}

	public Cell<T> padRight(Value padRight) {
		ensureNotNull(padRight, "padRight");
		this.padRight = padRight;
		return this;
	}

	/**
	 * Sets the padTop, padLeft, padBottom, and padRight to the specified value.
	 */
	public Cell<T> pad(float pad) {
		pad(Value.Fixed.valueOf(pad));
		return this;
	}

	public Cell<T> pad(float top, float left, float bottom, float right) {
		pad(Value.Fixed.valueOf(top), Value.Fixed.valueOf(left), Value.Fixed.valueOf(bottom), Value.Fixed.valueOf(right));
		return this;
	}

	public Cell<T> padTop(float padTop) {
		this.padTop = Value.Fixed.valueOf(padTop);
		return this;
	}

	public Cell<T> padLeft(float padLeft) {
		this.padLeft = Value.Fixed.valueOf(padLeft);
		return this;
	}

	public Cell<T> padBottom(float padBottom) {
		this.padBottom = Value.Fixed.valueOf(padBottom);
		return this;
	}

	public Cell<T> padRight(float padRight) {
		this.padRight = Value.Fixed.valueOf(padRight);
		return this;
	}

	/**
	 * Sets fillX and fillY to 1.
	 */
	public Cell<T> fill() {
		fillX = onef;
		fillY = onef;
		return this;
	}

	/**
	 * Sets fillX to 1.
	 */
	public Cell<T> fillX() {
		fillX = onef;
		return this;
	}

	/**
	 * Sets fillY to 1.
	 */
	public Cell<T> fillY() {
		fillY = onef;
		return this;
	}

	public Cell<T> fill(float x, float y) {
		fillX = x;
		fillY = y;
		return this;
	}

	/**
	 * Sets fillX and fillY to 1 if true, 0 if false.
	 */
	public Cell<T> fill(boolean x, boolean y) {
		fillX = x ? onef : zerof;
		fillY = y ? onef : zerof;
		return this;
	}

	/**
	 * Sets fillX and fillY to 1 if true, 0 if false.
	 */
	public Cell<T> fill(boolean fill) {
		fillX = fill ? onef : zerof;
		fillY = fill ? onef : zerof;
		return this;
	}

	/**
	 * Sets the alignment of the actor within the cell. Set to {@link Align#center}, {@link Align#top}, {@link Align#bottom},
	 * {@link Align#left}, {@link Align#right}, or any combination of those.
	 */
	public Cell<T> align(int align) {
		this.align = align;
		return this;
	}

	/**
	 * Sets the alignment of the actor within the cell to {@link Align#center}. This clears any other alignment.
	 */
	public Cell<T> center() {
		align = centeri;
		return this;
	}

	/**
	 * Adds {@link Align#top} and clears {@link Align#bottom} for the alignment of the actor within the cell.
	 */
	public Cell<T> top() {
		if(align == null)
			align = topi;
		else
			align = (align | Align.top) & ~Align.bottom;
		return this;
	}

	/**
	 * Adds {@link Align#left} and clears {@link Align#right} for the alignment of the actor within the cell.
	 */
	public Cell<T> left() {
		if(align == null)
			align = lefti;
		else
			align = (align | Align.left) & ~Align.right;
		return this;
	}

	/**
	 * Adds {@link Align#bottom} and clears {@link Align#top} for the alignment of the actor within the cell.
	 */
	public Cell<T> bottom() {
		if(align == null)
			align = bottomi;
		else
			align = (align | Align.bottom) & ~Align.top;
		return this;
	}

	/**
	 * Adds {@link Align#right} and clears {@link Align#left} for the alignment of the actor within the cell.
	 */
	public Cell<T> right() {
		if(align == null)
			align = righti;
		else
			align = (align | Align.right) & ~Align.left;
		return this;
	}

	/**
	 * Sets expandX, expandY, fillX, and fillY to 1.
	 */
	public Cell<T> grow() {
		expandX = onei;
		expandY = onei;
		fillX = onef;
		fillY = onef;
		return this;
	}

	/**
	 * Sets expandX and fillX to 1.
	 */
	public Cell<T> growX() {
		expandX = onei;
		fillX = onef;
		return this;
	}

	/**
	 * Sets expandY and fillY to 1.
	 */
	public Cell<T> growY() {
		expandY = onei;
		fillY = onef;
		return this;
	}

	/**
	 * Sets expandX and expandY to 1.
	 */
	public Cell<T> expand() {
		expandX = onei;
		expandY = onei;
		return this;
	}

	/**
	 * Sets expandX to 1.
	 */
	public Cell<T> expandX() {
		expandX = onei;
		return this;
	}

	/**
	 * Sets expandY to 1.
	 */
	public Cell<T> expandY() {
		expandY = onei;
		return this;
	}

	public Cell<T> expand(int x, int y) {
		expandX = x;
		expandY = y;
		return this;
	}

	/**
	 * Sets expandX and expandY to 1 if true, 0 if false.
	 */
	public Cell<T> expand(boolean x, boolean y) {
		expandX = x ? onei : zeroi;
		expandY = y ? onei : zeroi;
		return this;
	}

	public Cell<T> colspan(int colspan) {
		this.colspan = colspan;
		return this;
	}

	/**
	 * Sets uniformX and uniformY to true.
	 */
	public Cell<T> uniform() {
		uniformX = Boolean.TRUE;
		uniformY = Boolean.TRUE;
		return this;
	}

	/**
	 * Sets uniformX to true.
	 */
	public Cell<T> uniformX() {
		uniformX = Boolean.TRUE;
		return this;
	}

	/**
	 * Sets uniformY to true.
	 */
	public Cell<T> uniformY() {
		uniformY = Boolean.TRUE;
		return this;
	}

	public Cell<T> uniform(boolean uniform) {
		uniformX = uniform;
		uniformY = uniform;
		return this;
	}

	public Cell<T> uniform(boolean x, boolean y) {
		uniformX = x;
		uniformY = y;
		return this;
	}

	public void setActorBounds(float x, float y, float width, float height) {
		actorX = x;
		actorY = y;
		actorWidth = width;
		actorHeight = height;
	}

	public float getActorX() {
		return actorX;
	}

	public void setActorX(float actorX) {
		this.actorX = actorX;
	}

	public float getActorY() {
		return actorY;
	}

	public void setActorY(float actorY) {
		this.actorY = actorY;
	}

	public float getActorWidth() {
		return actorWidth;
	}

	public void setActorWidth(float actorWidth) {
		this.actorWidth = actorWidth;
	}

	public float getActorHeight() {
		return actorHeight;
	}

	public void setActorHeight(float actorHeight) {
		this.actorHeight = actorHeight;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	/**
	 * @return May be null if this cell is row defaults.
	 */
	public @Null Value getMinWidthValue() {
		return minWidth;
	}

	public float getMinWidth() {
		return minWidth.get(actor);
	}

	/**
	 * @return May be null if this cell is row defaults.
	 */
	public @Null Value getMinHeightValue() {
		return minHeight;
	}

	public float getMinHeight() {
		return minHeight.get(actor);
	}

	/**
	 * @return May be null if this cell is row defaults.
	 */
	public @Null Value getPrefWidthValue() {
		return prefWidth;
	}

	public float getPrefWidth() {
		return prefWidth.get(actor);
	}

	/**
	 * @return May be null if this cell is row defaults.
	 */
	public @Null Value getPrefHeightValue() {
		return prefHeight;
	}

	public float getPrefHeight() {
		return prefHeight.get(actor);
	}

	/**
	 * @return May be null if this cell is row defaults.
	 */
	public @Null Value getMaxWidthValue() {
		return maxWidth;
	}

	public float getMaxWidth() {
		return maxWidth.get(actor);
	}

	/**
	 * @return May be null if this cell is row defaults.
	 */
	public @Null Value getMaxHeightValue() {
		return maxHeight;
	}

	public float getMaxHeight() {
		return maxHeight.get(actor);
	}

	/**
	 * @return May be null if this value is not set.
	 */
	public @Null Value getSpaceTopValue() {
		return spaceTop;
	}

	public float getSpaceTop() {
		return spaceTop.get(actor);
	}

	/**
	 * @return May be null if this value is not set.
	 */
	public @Null Value getSpaceLeftValue() {
		return spaceLeft;
	}

	public float getSpaceLeft() {
		return spaceLeft.get(actor);
	}

	/**
	 * @return May be null if this value is not set.
	 */
	public @Null Value getSpaceBottomValue() {
		return spaceBottom;
	}

	public float getSpaceBottom() {
		return spaceBottom.get(actor);
	}

	/**
	 * @return May be null if this value is not set.
	 */
	public @Null Value getSpaceRightValue() {
		return spaceRight;
	}

	public float getSpaceRight() {
		return spaceRight.get(actor);
	}

	/**
	 * @return May be null if this value is not set.
	 */
	public @Null Value getPadTopValue() {
		return padTop;
	}

	public float getPadTop() {
		return padTop.get(actor);
	}

	/**
	 * @return May be null if this value is not set.
	 */
	public @Null Value getPadLeftValue() {
		return padLeft;
	}

	public float getPadLeft() {
		return padLeft.get(actor);
	}

	/**
	 * @return May be null if this value is not set.
	 */
	public @Null Value getPadBottomValue() {
		return padBottom;
	}

	public float getPadBottom() {
		return padBottom.get(actor);
	}

	/**
	 * @return May be null if this value is not set.
	 */
	public @Null Value getPadRightValue() {
		return padRight;
	}

	public float getPadRight() {
		return padRight.get(actor);
	}

	/**
	 * Returns {@link #getPadLeft()} plus {@link #getPadRight()}.
	 */
	public float getPadX() {
		return padLeft.get(actor) + padRight.get(actor);
	}

	/**
	 * Returns {@link #getPadTop()} plus {@link #getPadBottom()}.
	 */
	public float getPadY() {
		return padTop.get(actor) + padBottom.get(actor);
	}

	public float getFillX() {
		return fillX;
	}

	public float getFillY() {
		return fillY;
	}

	public int getAlign() {
		return align;
	}

	public int getExpandX() {
		return expandX;
	}

	public int getExpandY() {
		return expandY;
	}

	public int getColspan() {
		return colspan;
	}

	public boolean getUniformX() {
		return uniformX;
	}

	public boolean getUniformY() {
		return uniformY;
	}

	/**
	 * Returns true if this cell is the last cell in the row.
	 */
	public boolean isEndRow() {
		return endRow;
	}

	/**
	 * The actual amount of combined padding and spacing from the last layout.
	 */
	public float getComputedPadTop() {
		return computedPadTop;
	}

	/**
	 * The actual amount of combined padding and spacing from the last layout.
	 */
	public float getComputedPadLeft() {
		return computedPadLeft;
	}

	/**
	 * The actual amount of combined padding and spacing from the last layout.
	 */
	public float getComputedPadBottom() {
		return computedPadBottom;
	}

	/**
	 * The actual amount of combined padding and spacing from the last layout.
	 */
	public float getComputedPadRight() {
		return computedPadRight;
	}

	public void row() {
		table.row();
	}

	public Table getTable() {
		return table;
	}

	/**
	 * Sets all constraint fields to null.
	 */
	void clear() {
		minWidth = null;
		minHeight = null;
		prefWidth = null;
		prefHeight = null;
		maxWidth = null;
		maxHeight = null;
		spaceTop = null;
		spaceLeft = null;
		spaceBottom = null;
		spaceRight = null;
		padTop = null;
		padLeft = null;
		padBottom = null;
		padRight = null;
		fillX = null;
		fillY = null;
		align = null;
		expandX = null;
		expandY = null;
		colspan = null;
		uniformX = null;
		uniformY = null;
	}

	/**
	 * Reset state so the cell can be reused, setting all constraints to their {@link #defaults() default} values.
	 */
	public void reset() {
		actor = null;
		table = null;
		endRow = false;
		cellAboveIndex = -1;
		set(defaults());
	}

	void set(Cell<?> cell) {
		minWidth = cell.minWidth;
		minHeight = cell.minHeight;
		prefWidth = cell.prefWidth;
		prefHeight = cell.prefHeight;
		maxWidth = cell.maxWidth;
		maxHeight = cell.maxHeight;
		spaceTop = cell.spaceTop;
		spaceLeft = cell.spaceLeft;
		spaceBottom = cell.spaceBottom;
		spaceRight = cell.spaceRight;
		padTop = cell.padTop;
		padLeft = cell.padLeft;
		padBottom = cell.padBottom;
		padRight = cell.padRight;
		fillX = cell.fillX;
		fillY = cell.fillY;
		align = cell.align;
		expandX = cell.expandX;
		expandY = cell.expandY;
		colspan = cell.colspan;
		uniformX = cell.uniformX;
		uniformY = cell.uniformY;
	}

	void merge(@Null Cell<?> cell) {
		if(cell == null)
			return;
		if(cell.minWidth != null)
			minWidth = cell.minWidth;
		if(cell.minHeight != null)
			minHeight = cell.minHeight;
		if(cell.prefWidth != null)
			prefWidth = cell.prefWidth;
		if(cell.prefHeight != null)
			prefHeight = cell.prefHeight;
		if(cell.maxWidth != null)
			maxWidth = cell.maxWidth;
		if(cell.maxHeight != null)
			maxHeight = cell.maxHeight;
		if(cell.spaceTop != null)
			spaceTop = cell.spaceTop;
		if(cell.spaceLeft != null)
			spaceLeft = cell.spaceLeft;
		if(cell.spaceBottom != null)
			spaceBottom = cell.spaceBottom;
		if(cell.spaceRight != null)
			spaceRight = cell.spaceRight;
		if(cell.padTop != null)
			padTop = cell.padTop;
		if(cell.padLeft != null)
			padLeft = cell.padLeft;
		if(cell.padBottom != null)
			padBottom = cell.padBottom;
		if(cell.padRight != null)
			padRight = cell.padRight;
		if(cell.fillX != null)
			fillX = cell.fillX;
		if(cell.fillY != null)
			fillY = cell.fillY;
		if(cell.align != null)
			align = cell.align;
		if(cell.expandX != null)
			expandX = cell.expandX;
		if(cell.expandY != null)
			expandY = cell.expandY;
		if(cell.colspan != null)
			colspan = cell.colspan;
		if(cell.uniformX != null)
			uniformX = cell.uniformX;
		if(cell.uniformY != null)
			uniformY = cell.uniformY;
	}

	public String toString() {
		return actor != null ? actor.toString() : super.toString();
	}

	/**
	 * Returns the defaults to use for all cells. This can be used to avoid needing to set the same defaults for every table (eg,
	 * for spacing).
	 */
	public static Cell<?> defaults() {
		if(files == null || files != Gdx.files) {
			files = Gdx.files;
			defaults = new Cell<>();
			defaults.minWidth = Value.minWidth;
			defaults.minHeight = Value.minHeight;
			defaults.prefWidth = Value.prefWidth;
			defaults.prefHeight = Value.prefHeight;
			defaults.maxWidth = Value.maxWidth;
			defaults.maxHeight = Value.maxHeight;
			defaults.spaceTop = Value.zero;
			defaults.spaceLeft = Value.zero;
			defaults.spaceBottom = Value.zero;
			defaults.spaceRight = Value.zero;
			defaults.padTop = Value.zero;
			defaults.padLeft = Value.zero;
			defaults.padBottom = Value.zero;
			defaults.padRight = Value.zero;
			defaults.fillX = zerof;
			defaults.fillY = zerof;
			defaults.align = centeri;
			defaults.expandX = zeroi;
			defaults.expandY = zeroi;
			defaults.colspan = onei;
			defaults.uniformX = null;
			defaults.uniformY = null;
		}
		return defaults;
	}
}
