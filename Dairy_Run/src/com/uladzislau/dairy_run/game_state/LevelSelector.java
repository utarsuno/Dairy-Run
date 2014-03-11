package com.uladzislau.dairy_run.game_state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.uladzislau.dairy_run.DairyRun;
import com.uladzislau.dairy_run.entity.Map;
import com.uladzislau.dairy_run.information.ScreenUtil;
import com.uladzislau.dairy_run.manager.FontManager;
import com.uladzislau.dairy_run.manager.InputManager;
import com.uladzislau.dairy_run.manager.TextureManager;

public class LevelSelector extends GameState {

	private Level levels[];

	private int x_scroll;

	public LevelSelector(DairyRun dairy_run, byte id) {
		super(dairy_run, id);
	}

	@Override
	public void initialize(ShapeRenderer shapeRenderer, SpriteBatch batch) {
		this.shape_renderer = shapeRenderer;
		this.sprite_batch = batch;
		this.levels = new Level[30];
		for (int i = 0; i < this.levels.length; i++) {
			this.levels[i] = new Level(false);
		}
		this.x_scroll = 0;
		createLevels();
	}

	private void createLevels() {
		// Level One.
		this.levels[0].setVelocityMatters(true);
		this.levels[0].setVelocityNeeded(10.0f);
		this.levels[0].setUnlocked(true);
	}

	private int previous_x;

	@Override
	public void update(float delta) {
		if (InputManager.pointersDown[0]) {
			if (!InputManager.pointersDragging[0]) {
				for (int i = 0; i < this.levels.length; i++) {
					if (InputManager.pointers[0].x > Map.size + this.x_scroll + Map.size * 3 * i + this.spacing * i
							&& InputManager.pointers[0].x < (Map.size + this.x_scroll + Map.size * 3 * i + this.spacing * i) + Map.size * 3
							&& InputManager.pointers[0].y > Map.size && InputManager.pointers[0].y < Map.size * 4) {
						// TODO: Have error sound playing if the level is locked.
						if (this.levels[i].isUnlocked()) {
							this.dairy_run.getGameStateManager().changeState(GameStateManager.PLAY);
						}
					}
				}
			}
			this.x_scroll += InputManager.pointers[0].x - this.previous_x;
			if (this.x_scroll > 0) {
				this.x_scroll = 0;
			}
		}
		this.previous_x = InputManager.pointers[0].x;
	}

	private int spacing = (int) ((Map.size * 3) * 0.2f);

	@Override
	public void render() {
		this.sprite_batch.begin();

		for (int i = 0; i < this.levels.length; i++) {
			if (Map.size + this.x_scroll + Map.size * 3 * i + this.spacing * i < ScreenUtil.screen_width) {
				if (this.levels[i].isUnlocked()) {
					this.sprite_batch.draw(TextureManager.SPRITESHEET.PIXEL_SPRITESHEET.getFrame(31 * 4 + 10), Map.size + this.x_scroll
							+ Map.size * 3 * i + this.spacing * i, Map.size, Map.size * 3, Map.size * 3);
				} else {
					this.sprite_batch.draw(TextureManager.SPRITESHEET.PIXEL_SPRITESHEET.getFrame(31 * 6 + 11), Map.size + this.x_scroll
							+ Map.size * 3 * i + this.spacing * i, Map.size, Map.size * 3, Map.size * 3);
				}
				FontManager.FONT.PIXEL_REGULAR.render(this.sprite_batch, "" + i, Color.WHITE, Map.size + this.x_scroll + Map.size * 3 * i
						+ this.spacing * i, Map.size + this.x_scroll + Map.size * 3 * i + this.spacing * i + Map.size * 3, Map.size * 4,
						Map.size * 6);
			}
		}

		this.sprite_batch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
