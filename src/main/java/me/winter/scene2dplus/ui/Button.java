/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package me.winter.scene2dplus.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Pools;
import me.winter.scene2dplus.Actor;
import me.winter.scene2dplus.InputEvent;
import me.winter.scene2dplus.Stage;
import me.winter.scene2dplus.Touchable;
import me.winter.scene2dplus.utils.ChangeListener;
import me.winter.scene2dplus.utils.ClickListener;
import me.winter.scene2dplus.utils.Disableable;
import me.winter.scene2dplus.utils.Drawable;

/**
 * A button is a {@link Table} with a checked state and additional {@link ButtonStyle style} fields for pressed, unpressed, and
 * checked. Each time a button is clicked, the checked state is toggled. Being a table, a button can contain any other actors.<br>
 * <br>
 * The button's padding is set to the background drawable's padding when the background changes, overwriting any padding set
 * manually. Padding can still be set on the button's table cells.
 * <p>
 * {@link ChangeListener.ChangeEvent} is fired when the button is clicked. Cancelling the event will restore the checked button state to what is
 * was previously.
 * <p>
 * The preferred size of the button is determined by the background and the button contents.
 *
 * @author Nathan Sweet
 */
public class Button extends Table implements Disableable {
	private ButtonStyle style;
	boolean isChecked, isDisabled;
	ButtonGroup<? extends Button> buttonGroup;
	private ClickListener clickListener;
	private boolean programmaticChangeEvents = true;

	public Button(Skin skin) {
		super(skin);
		initialize();
		setStyle(skin.get(ButtonStyle.class));
		setSize(getPrefWidth(), getPrefHeight());
	}

	public Button(Skin skin, String styleName) {
		super(skin);
		initialize();
		setStyle(skin.get(styleName, ButtonStyle.class));
		setSize(getPrefWidth(), getPrefHeight());
	}

	public Button(Actor child, Skin skin, String styleName) {
		this(child, skin.get(styleName, ButtonStyle.class));
		setSkin(skin);
	}

	public Button(Actor child, ButtonStyle style) {
		initialize();
		add(child);
		setStyle(style);
		setSize(getPrefWidth(), getPrefHeight());
	}

	public Button(ButtonStyle style) {
		initialize();
		setStyle(style);
		setSize(getPrefWidth(), getPrefHeight());
	}

	/**
	 * Creates a button without setting the style or size. At least a style must be set before using this button.
	 */
	public Button() {
		initialize();
	}

	private void initialize() {
		setTouchable(Touchable.enabled);
		addListener(clickListener = new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if(isDisabled())
					return;
				setChecked(!isChecked, true);
			}
		});
	}

	public Button(@Null Drawable up) {
		this(new ButtonStyle(up, null, null));
	}

	public Button(@Null Drawable up, @Null Drawable down) {
		this(new ButtonStyle(up, down, null));
	}

	public Button(@Null Drawable up, @Null Drawable down, @Null Drawable checked) {
		this(new ButtonStyle(up, down, checked));
	}

	public Button(Actor child, Skin skin) {
		this(child, skin.get(ButtonStyle.class));
	}

	public void setChecked(boolean isChecked) {
		setChecked(isChecked, programmaticChangeEvents);
	}

	@SuppressWarnings("unchecked")
	void setChecked(boolean isChecked, boolean fireEvent) {
		if(this.isChecked == isChecked)
			return;
		if(buttonGroup != null && !((ButtonGroup<Button>)buttonGroup).canCheck(this, isChecked))
			return;
		this.isChecked = isChecked;

		if(fireEvent) {
			ChangeListener.ChangeEvent changeEvent = Pools.obtain(ChangeListener.ChangeEvent.class);
			if(fire(changeEvent))
				this.isChecked = !isChecked;
			Pools.free(changeEvent);
		}
	}

	/**
	 * Toggles the checked state. This method changes the checked state, which fires a {@link ChangeListener.ChangeEvent} (if programmatic change
	 * events are enabled), so can be used to simulate a button click.
	 */
	public void toggle() {
		setChecked(!isChecked);
	}

	public boolean isChecked() {
		return isChecked;
	}

	public boolean isPressed() {
		return clickListener.isVisualPressed();
	}

	public boolean isOver() {
		return clickListener.isOver();
	}

	public ClickListener getClickListener() {
		return clickListener;
	}

	public boolean isDisabled() {
		return isDisabled;
	}

	/**
	 * When true, the button will not toggle {@link #isChecked()} when clicked and will not fire a {@link ChangeListener.ChangeEvent}.
	 */
	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	/**
	 * If false, {@link #setChecked(boolean)} and {@link #toggle()} will not fire {@link ChangeListener.ChangeEvent}. The event will only be
	 * fired only when the user clicks the button
	 */
	public void setProgrammaticChangeEvents(boolean programmaticChangeEvents) {
		this.programmaticChangeEvents = programmaticChangeEvents;
	}

	public void setStyle(ButtonStyle style) {
		if(style == null)
			throw new IllegalArgumentException("style cannot be null.");
		this.style = style;

		setBackground(getBackgroundDrawable());
	}

	/**
	 * Returns the button's style. Modifying the returned style may not have an effect until {@link #setStyle(ButtonStyle)} is
	 * called.
	 */
	public ButtonStyle getStyle() {
		return style;
	}

	/**
	 * @return May be null.
	 */
	public @Null ButtonGroup<? extends Button> getButtonGroup() {
		return buttonGroup;
	}

	/**
	 * Returns appropriate background drawable from the style based on the current button state.
	 */
	protected @Null Drawable getBackgroundDrawable() {
		if(isDisabled() && style.disabled != null)
			return style.disabled;
		if(isPressed()) {
			if(isChecked() && style.checkedDown != null)
				return style.checkedDown;
			if(style.down != null)
				return style.down;
		}
		if(isOver()) {
			if(isChecked()) {
				if(style.checkedOver != null)
					return style.checkedOver;
			} else {
				if(style.over != null)
					return style.over;
			}
		}
		boolean focused = hasKeyboardFocus();
		if(isChecked()) {
			if(focused && style.checkedFocused != null)
				return style.checkedFocused;
			if(style.checked != null)
				return style.checked;
			if(isOver() && style.over != null)
				return style.over;
		}
		if(focused && style.focused != null)
			return style.focused;
		return style.up;
	}

	public void draw(Batch batch, float parentAlpha) {
		validate();

		setBackground(getBackgroundDrawable());

		float offsetX, offsetY;
		if(isPressed() && !isDisabled()) {
			offsetX = style.pressedOffsetX;
			offsetY = style.pressedOffsetY;
		} else if(isChecked() && !isDisabled()) {
			offsetX = style.checkedOffsetX;
			offsetY = style.checkedOffsetY;
		} else {
			offsetX = style.unpressedOffsetX;
			offsetY = style.unpressedOffsetY;
		}
		boolean offset = offsetX != 0 || offsetY != 0;

		Array<Actor> children = getChildren();
		if(offset) {
			for(int i = 0; i < children.size; i++)
				children.get(i).moveBy(offsetX, offsetY);
		}
		super.draw(batch, parentAlpha);
		if(offset) {
			for(int i = 0; i < children.size; i++)
				children.get(i).moveBy(-offsetX, -offsetY);
		}

		Stage stage = getStage();
		if(stage != null && stage.getActionsRequestRendering() && isPressed() != clickListener.isPressed())
			Gdx.graphics.requestRendering();
	}

	public float getPrefWidth() {
		float width = super.getPrefWidth();
		if(style.up != null)
			width = Math.max(width, style.up.getMinWidth());
		if(style.down != null)
			width = Math.max(width, style.down.getMinWidth());
		if(style.checked != null)
			width = Math.max(width, style.checked.getMinWidth());
		return width;
	}

	public float getPrefHeight() {
		float height = super.getPrefHeight();
		if(style.up != null)
			height = Math.max(height, style.up.getMinHeight());
		if(style.down != null)
			height = Math.max(height, style.down.getMinHeight());
		if(style.checked != null)
			height = Math.max(height, style.checked.getMinHeight());
		return height;
	}

	public float getMinWidth() {
		return getPrefWidth();
	}

	public float getMinHeight() {
		return getPrefHeight();
	}

	/**
	 * The style for a button, see {@link Button}.
	 *
	 * @author mzechner
	 */
	public static class ButtonStyle {
		public @Null Drawable up, down, over, focused, disabled;
		public @Null Drawable checked, checkedOver, checkedDown, checkedFocused;
		public float pressedOffsetX, pressedOffsetY, unpressedOffsetX, unpressedOffsetY, checkedOffsetX, checkedOffsetY;

		public ButtonStyle() {}

		public ButtonStyle(@Null Drawable up, @Null Drawable down, @Null Drawable checked) {
			this.up = up;
			this.down = down;
			this.checked = checked;
		}

		public ButtonStyle(ButtonStyle style) {
			up = style.up;
			down = style.down;
			over = style.over;
			focused = style.focused;
			disabled = style.disabled;

			checked = style.checked;
			checkedOver = style.checkedOver;
			checkedDown = style.checkedDown;
			checkedFocused = style.checkedFocused;

			pressedOffsetX = style.pressedOffsetX;
			pressedOffsetY = style.pressedOffsetY;
			unpressedOffsetX = style.unpressedOffsetX;
			unpressedOffsetY = style.unpressedOffsetY;
			checkedOffsetX = style.checkedOffsetX;
			checkedOffsetY = style.checkedOffsetY;
		}
	}
}
