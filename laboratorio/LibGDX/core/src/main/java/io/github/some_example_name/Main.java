package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.files.FileHandle;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private int mouseX;
    private BitmapFont font;
    private String text;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        FileHandle file = new FileHandle("prova.txt");
        text = file.readString();
        font = new BitmapFont();
    }

    @Override
    public void render() {
        // Update application
        //mouseX = Gdx.input.getX();

        // Render image
        ScreenUtils.clear(0,0,0,0);
        batch.begin();
        batch.draw(image, 100, 500);
        font.draw(batch, "schech", 100, 100);
        font.draw(batch, text, 100, 120);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
