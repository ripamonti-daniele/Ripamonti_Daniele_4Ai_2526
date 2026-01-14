package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private final Random r = new Random();
    private SpriteBatch batch;
    private Texture image;
    private int mouseX;
    private int mouseY;
    private BitmapFont font;
    private String text;
    private Texture lampadina_spenta;
    private Texture lampadina_accesa;
    private Texture lampadina_rotta;
    private List<Lampadina> archivio = new ArrayList<>();
    private float timer = 0f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        lampadina_spenta = new Texture("Lampadina_spenta.png");
        lampadina_accesa = new Texture("Lampadina_accesa.png");
        lampadina_rotta = new Texture("Lampadina_rotta.png");
        FileHandle file = new FileHandle("prova.txt");
        text = file.readString();
        font = new BitmapFont();
        for (int i = 0; i < 10; i++) {
            Lampadina l = new Lampadina();
            l.posizione(r.nextInt(0, 500), r.nextInt(0, 500));
            archivio.add(l);
        }
    }

    @Override
    public void render() {
        // Update application
//        mouseX = Gdx.input.getX();
//        mouseY = Gdx.input.getY();

        // Render image
        ScreenUtils.clear(0,0,0,0);
        batch.begin();
//        batch.draw(image, 100, 500);
//        font.draw(batch, "schech", 100, 100);
//        font.draw(batch, text, 100, 120);
//        batch.draw(lampadina_spenta, 100, 100);
//        batch.draw(lampadina_accesa,500, 500);

        float delta = Gdx.graphics.getDeltaTime(); // tempo passato dall'ultimo frame
        timer += delta;

        if (timer >= 0.04f) { // 0.5 secondi = 2 volte al secondo
            aggiorna(archivio);
            timer = 0f;
        }

        disegnaLampadine(archivio);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }

    public void aggiorna(List<Lampadina> archivio) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            for (Lampadina l : archivio) {
                if (l.getStato() == StatoLamp.SPENTA) {
                    l.accendi();
                } else if (l.getStato() == StatoLamp.ACCESA) {
                    l.spegni();
                }
            }
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.C)) {
            Lampadina l = new Lampadina();
            l.posizione(r.nextInt(0, 500), r.nextInt(0, 500));
            archivio.add(l);
        }

        else if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            List<Integer> indici = new ArrayList<>();

            for (int i = 0; i < archivio.size(); i++) {
                if (archivio.get(i).getStato() == StatoLamp.ROTTA) indici.add(i);
            }

            for (int i = indici.size() -1; i >= 0; i--) archivio.remove(i);
        }
    }

    public void disegnaLampadine(List<Lampadina> archivio) {
        for (Lampadina l : archivio)
            switch (l.getStato()) {
                case ACCESA: batch.draw(lampadina_accesa, l.getX(), l.getY()); break;
                case SPENTA: batch.draw(lampadina_spenta, l.getX(), l.getY()); break;
                case ROTTA: batch.draw(lampadina_rotta, l.getX(), l.getY()); break;
        }
    }
}
