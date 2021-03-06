package com.uladzislau.dairy_run.game_state;

import com.badlogic.gdx.graphics.Color;
import com.uladzislau.dairy_run.DairyRun;
import com.uladzislau.dairy_run.colorxv.ColorXv;
import com.uladzislau.dairy_run.entity.Background;
import com.uladzislau.dairy_run.entity.GroundBlock;
import com.uladzislau.dairy_run.gui.ClickableText;
import com.uladzislau.dairy_run.gui.Slider;
import com.uladzislau.dairy_run.gui.StaticGUI;
import com.uladzislau.dairy_run.information.ScreenUtil;
import com.uladzislau.dairy_run.manager.AudioManager;
import com.uladzislau.dairy_run.manager.FontManager;
import com.uladzislau.dairy_run.manager.InputManager;
import com.uladzislau.dairy_run.manager.ResourceManager;
import com.uladzislau.dairy_run.manager.TextureManager;
import com.uladzislau.dairy_run.math.geometry.Rectanglei;
import com.uladzislau.dairy_run.world.Map;

public class Options extends GameState {

	private Slider musicSlider;
	private ClickableText musicPercentage;

	private Slider soundSlider;
	private ClickableText soundPercentage;

	private ClickableText audio;
	private ClickableText controls;

	private ClickableText layout_one;
	private ClickableText layout_two;
	private ClickableText layout_three;
	private ClickableText layout_four;

	private GroundBlock[] ground_blocks;

	public static byte current_layout;

	private boolean audio_state = true;

	public Options(DairyRun dairy_run, GameStateManager.STATE state) {
		super(dairy_run, state);
	}

	@Override
	public void initialize() {

		this.musicSlider = new Slider(Map.size / 2, (int) (Map.size * 4.0f), ScreenUtil.screen_width - Map.size * 5, Map.size, ColorXv.TEAL, ColorXv.DARK_BLUE);

		this.musicPercentage = new ClickableText("100%", new Rectanglei(ScreenUtil.screen_width - Map.size * 4, Map.size * 4.0f, Map.size * 4, Map.size), //$NON-NLS-1$
				ColorXv.DARK_BLUE, ColorXv.PURPLE, 800);

		this.soundSlider = new Slider(Map.size / 2, Map.size * 2, ScreenUtil.screen_width - Map.size * 5, Map.size, ColorXv.TEAL, ColorXv.DARK_BLUE);

		this.soundPercentage = new ClickableText("100%", new Rectanglei(ScreenUtil.screen_width - Map.size * 4, Map.size * 2, Map.size * 4, Map.size), //$NON-NLS-1$
				ColorXv.DARK_BLUE, ColorXv.PURPLE, 800);

		this.audio = new ClickableText("Audio", new Rectanglei(Map.size / 2, ScreenUtil.screen_height - Map.size - Map.size / 4, Map.size * "Audio".length(), //$NON-NLS-1$ //$NON-NLS-2$
				Map.size), ColorXv.DARK_BLUE, ColorXv.PURPLE, 800);

		this.controls = new ClickableText("Controls", new Rectanglei(Map.size / 2 + Map.size * "Audio".length() + Map.size, ScreenUtil.screen_height - Map.size //$NON-NLS-1$ //$NON-NLS-2$
				- Map.size / 4, Map.size * "Controls".length(), Map.size), ColorXv.DARK_BLUE, ColorXv.PURPLE, 800); //$NON-NLS-1$

		String layout_one_title = "left"; //$NON-NLS-1$
		String layout_two_title = "right"; //$NON-NLS-1$
		String layout_three_title = "split"; //$NON-NLS-1$
		String layout_four_title = "dupl."; //$NON-NLS-1$

		this.layout_one = new ClickableText(layout_one_title, new Rectanglei(Map.size / 2, ScreenUtil.screen_height - Map.size * 3.5f, layout_one_title.length() * Map.size, Map.size),
				ColorXv.BLACK, ColorXv.DARK_BLUE, 800);

		this.layout_two = new ClickableText(layout_two_title, new Rectanglei(Map.size * 6.5f, ScreenUtil.screen_height - Map.size * 3.5f, layout_two_title.length() * Map.size, Map.size),
				ColorXv.BLACK, ColorXv.DARK_BLUE, 800);

		this.layout_three = new ClickableText(layout_three_title, new Rectanglei(Map.size / 2, Map.size * 3, layout_three_title.length() * Map.size, Map.size), ColorXv.BLACK,
				ColorXv.DARK_BLUE, 800);

		this.layout_four = new ClickableText(layout_four_title, new Rectanglei(Map.size * 6.5f, Map.size * 3, layout_four_title.length() * Map.size, Map.size), ColorXv.BLACK,
				ColorXv.DARK_BLUE, 800);

		this.ground_blocks = new GroundBlock[(ScreenUtil.screen_width / Map.size) + 2];
		// this.ground_blocks = new GroundBlock[1];
		for (int i = 0; i < this.ground_blocks.length; i++) {
			this.ground_blocks[i] = new GroundBlock(i * Map.size, Map.size * 1.5f, Map.size, Map.size, this.ground_blocks.length, false, GroundBlock.Theme.SNOW);
		}

		Options.current_layout = 3;
	}

	@Override
	public void update(float delta) {

		Map.setCurrentScroll(0);

		this.musicSlider.update((int) (delta * 1000.0f));
		this.musicPercentage.update(delta);
		this.soundSlider.update((int) (delta * 1000.0f));
		this.soundPercentage.update(delta);

		this.audio.update(delta);
		this.controls.update(delta);

		if (this.audio.isMouseDownOnMe() && !InputManager.pointersDragging[0]) {
			this.audio_state = true;
		} else if (this.controls.isMouseDownOnMe() && !InputManager.pointersDragging[0]) {
			this.audio_state = false;
		}

		if (this.audio_state) {
			AudioManager.setSoundLevel(this.soundSlider.percentageFull());
			this.soundPercentage.setTitle("" + (int) (AudioManager.getSoundLevel() * 100) + "%"); //$NON-NLS-1$ //$NON-NLS-2$

			AudioManager.setMusicLevel(this.musicSlider.percentageFull());
			this.musicPercentage.setTitle("" + (int) (AudioManager.getMusicLevel() * 100) + "%"); //$NON-NLS-1$ //$NON-NLS-2$
		} else {

			this.layout_one.update(delta);
			this.layout_two.update(delta);
			this.layout_three.update(delta);
			this.layout_four.update(delta);

			if (this.layout_one.isMouseDownOnMe() && !InputManager.pointersDragging[0]) {
				Options.current_layout = 0;
			} else if (this.layout_two.isMouseDownOnMe() && !InputManager.pointersDragging[0]) {
				Options.current_layout = 1;
			} else if (this.layout_three.isMouseDownOnMe() && !InputManager.pointersDragging[0]) {
				Options.current_layout = 2;
			} else if (this.layout_four.isMouseDownOnMe() && !InputManager.pointersDragging[0]) {
				Options.current_layout = 3;
			}
		}

		for (GroundBlock gb : this.ground_blocks) {
			gb.update(delta);
		}

		StaticGUI.music_button.update(delta);
		StaticGUI.back_button.update(delta);
	}

	@Override
	public void render() {
		Background.render(ResourceManager.getSpriteBatch(), Background.BLUE);

		for (GroundBlock gb : this.ground_blocks) {
			gb.render();
		}

		if (this.audio_state) {
			FontManager.Font.PIXEL_REGULAR.render("Sound", Color.BLACK, Map.size / 2, Map.size * 3, Map.size, false, -1); //$NON-NLS-1$
			this.soundSlider.render(ResourceManager.getSpriteBatch());
			this.soundPercentage.render(ResourceManager.getSpriteBatch(), false);

			FontManager.Font.PIXEL_REGULAR.render("Music", Color.BLACK, Map.size / 2, (int) (Map.size * 5.0f), Map.size, //$NON-NLS-1$
					false, -1);
			this.musicSlider.render(ResourceManager.getSpriteBatch());
			this.musicPercentage.render(ResourceManager.getSpriteBatch(), false);

			// Underline the audio text.
			ResourceManager.getSpriteBatch().draw(TextureManager.Spritesheet.PIXEL_SPRITESHEET.getFrame(TextureManager.BLACK), this.audio.getRectanglei().getX() - Map.size / 2,
					this.audio.getRectanglei().getY() - Map.size / 4, this.audio.getTitle().length() * Map.size + Map.size, Map.size / 4);
		} else {

			if (Options.current_layout == 0) {
				this.layout_one.setColor(0.0f, 0.0f, 0.0f, 1.0f);
				this.layout_two.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				this.layout_three.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				this.layout_four.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), 31 * 6 + 19, Map.size, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.REGULAR, Map.size * 2, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.CHOCOLATE, Map.size * 3, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.STRAWBERRY, Map.size * 4, 0);

			} else if (Options.current_layout == 1) {
				this.layout_one.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				this.layout_two.setColor(0.0f, 0.0f, 0.0f, 1.0f);
				this.layout_three.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				this.layout_four.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), 31 * 6 + 19, ScreenUtil.screen_width - Map.size * 2, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.REGULAR, ScreenUtil.screen_width - Map.size * 3, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.CHOCOLATE, ScreenUtil.screen_width - Map.size * 4, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.STRAWBERRY, ScreenUtil.screen_width - Map.size * 5, 0);
			} else if (Options.current_layout == 2) {
				this.layout_one.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				this.layout_two.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				this.layout_three.setColor(0.0f, 0.0f, 0.0f, 1.0f);
				this.layout_four.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), 31 * 6 + 19, Map.size, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.REGULAR, Map.size * 2, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.CHOCOLATE, ScreenUtil.screen_width - Map.size * 2, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.STRAWBERRY, ScreenUtil.screen_width - Map.size * 3, 0);
			} else if (Options.current_layout == 3) {
				this.layout_one.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				this.layout_two.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				this.layout_three.setColor(0.0f, 0.0f, 0.0f, 0.5f);
				this.layout_four.setColor(0.0f, 0.0f, 0.0f, 1.0f);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), 31 * 6 + 19, Map.size, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.REGULAR, Map.size * 2, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.CHOCOLATE, Map.size * 3, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.STRAWBERRY, Map.size * 4, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), 31 * 6 + 19, ScreenUtil.screen_width - Map.size * 2, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.REGULAR, ScreenUtil.screen_width - Map.size * 3, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.CHOCOLATE, ScreenUtil.screen_width - Map.size * 4, 0);
				TextureManager.Spritesheet.PIXEL_SPRITESHEET.render(ResourceManager.getSpriteBatch(), TextureManager.STRAWBERRY, ScreenUtil.screen_width - Map.size * 5, 0);
			}

			// Render the powerup buttons in the center.
			ResourceManager.getSpriteBatch().draw(TextureManager.Spritesheet.PIXEL_SPRITESHEET.getFrame(TextureManager.POWER_UP_ONE), ScreenUtil.screen_width / 2 - Map.size / 2 - Map.size,
					0, Map.size, Map.size);
			ResourceManager.getSpriteBatch().draw(TextureManager.Spritesheet.PIXEL_SPRITESHEET.getFrame(TextureManager.POWER_UP_TWO), ScreenUtil.screen_width / 2 - Map.size / 2, 0,
					Map.size, Map.size);
			ResourceManager.getSpriteBatch().draw(TextureManager.Spritesheet.PIXEL_SPRITESHEET.getFrame(TextureManager.POWER_UP_THREE),
					ScreenUtil.screen_width / 2 - Map.size / 2 + Map.size, 0, Map.size, Map.size);

			this.layout_one.render(FontManager.Font.PIXEL_REGULAR.getFont());
			this.layout_two.render(FontManager.Font.PIXEL_REGULAR.getFont());
			this.layout_three.render(FontManager.Font.PIXEL_REGULAR.getFont());
			this.layout_four.render(FontManager.Font.PIXEL_REGULAR.getFont());

			// Underline the controls text.
			ResourceManager.getSpriteBatch().draw(TextureManager.Spritesheet.PIXEL_SPRITESHEET.getFrame(TextureManager.BLACK), this.controls.getRectanglei().getX() - Map.size / 2,
					this.controls.getRectanglei().getY() - Map.size / 4, this.controls.getTitle().length() * Map.size + Map.size, Map.size / 4);
		}

		this.audio.render(FontManager.Font.PIXEL_REGULAR.getFont());
		this.controls.render(FontManager.Font.PIXEL_REGULAR.getFont());

		StaticGUI.music_button.render(ColorXv.DARK_TEAL);
		StaticGUI.back_button.render(ColorXv.DARK_TEAL);
	}

	@Override
	public void stateChangedToThis() {
		// Intentionally left blank.
	}

	@Override
	public void stateFinishedFadingInToExit() {
		// Intentionally left blank.
	}

	@Override
	public void stateFinishedFadingInToEntrance() {
		// Intentionally left blank.
	}

	@Override
	public void stateFinishedFadingOut() {
		// Intentionally left blank.
	}

	@Override
	public void pause() {
		// Intentionally left blank.
	}

	@Override
	public void resume() {
		// Intentionally left blank.
	}

	@Override
	public void dispose() {
		// Intentionally left blank.
	}

}