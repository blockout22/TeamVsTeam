package team.vs.team;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.lwjgl.nanovg.NVGColor;

import ce.core.Scene;
import ce.core.Util;
import ce.core.Window;
import ce.core.input.Key;
import ce.core.maths.Vector2f;

public class TeamVs {

	private Window window;
	private long ctx;
	private NVGColor color;
	private float scale = 25f;

	private Tile[][] tiles;

	private Vector2f playerLoc = new Vector2f();
	private Vector2f playerScale = new Vector2f(scale, scale * 2);

	public TeamVs() {
		init();
		loop();
		close();
	}

	public void init() {
		window = Window.createWindow(new Scene(1080, 850), "Team Vs Team");
		initNanoVG();
	}

	private void initNanoVG() {
		ctx = nvgCreate(NVG_STENCIL_STROKES);
		if (ctx == 0) {
			throw new RuntimeException("Could not init nanoVG");
		}

		color = NVGColor.create();
		try {
			InputStream in1 = Util.loadInternal("map.png");
			InputStream in2 = Util.loadInternal("collision.png");
			BufferedImage map = ImageIO.read(in1);
			BufferedImage collision = ImageIO.read(in2);
			tiles = new Tile[map.getWidth()][map.getHeight()];
			for (int x = 0; x < map.getWidth(); x++) {
				for (int y = 0; y < map.getHeight(); y++) {
					int rgb = map.getRGB(x, y);
					float r = (rgb >> 16) & 0xFF;
					float g = (rgb >> 8) & 0xFF;
					float b = (rgb & 0xFF);
					float a = (rgb >> 24) & 0xFF;

					if (r > 1) {
						r = r / 255f;
					}

					if (g > 1) {
						g = g / 255f;
					}

					if (b > 1) {
						b = b / 255f;
					}

					if (a > 1) {
						a = a / 255f;
					}

					float colAlpha = ((collision.getRGB(x, y) >> 24) & 0xFF);

					tiles[x][y] = new Tile(r, g, b, a, colAlpha > 0);
					System.out.println(tiles[x][y].isCanCollide());
				}
			}

			map.flush();
			collision.flush();
			in1.close();
			in2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// for (int x = 0; x < 10; x++) {
		// for (int y = 0; y < 10; y++) {
		// colors[x][y] = new Color((float) Math.random(), (float) Math.random(),
		// (float) Math.random(), (float) Math.random());
		// }
		// }
	}

	private void loop() {
		while (!window.isCloseRequested()) {

			nvgBeginFrame(ctx, window.getWidth(), window.getHeight(), 1);
			{
				// nvgBeginPath(ctx);
				// nvgRect(ctx, 10, 10, 50, 50);
				// nvgFillColor(ctx, setColor(0, 1, 0, 0.5f));
				// nvgFill(ctx);
				//
				// nvgBeginPath(ctx);
				// nvgRect(ctx, 30, 30, 50, 50);
				// nvgFillColor(ctx, setColor(1, 0, 0, 0.5f));
				// nvgFill(ctx);

				for (int x = 0; x < tiles.length; x++) {
					for (int y = 0; y < tiles[x].length; y++) {
						float xPos = x * scale;
						float yPos = y * scale;
						nvgBeginPath(ctx);
						nvgRect(ctx, xPos, yPos, scale, scale);
						nvgFillColor(ctx, setColor(tiles[x][y].getRed(), tiles[x][y].getGreen(), tiles[x][y].getBlue(), tiles[x][y].getAlpha()));
						nvgFill(ctx);
						if (tiles[x][y].isCanCollide()) {
							checkCollision(xPos, yPos, scale, scale);
						}

					}
				}

				nvgBeginPath(ctx);
				nvgRect(ctx, playerLoc.x, playerLoc.y, playerScale.x, playerScale.y);
				nvgFillColor(ctx, setColor(0.25f, 0.87f, 0.8f, 1f));
				nvgFill(ctx);

			}
			nvgEndFrame(ctx);

			if (window.input.isKeyPressed(Key.KEY_I)) {
				scale++;
				System.out.println("Current Scale: " + scale);
			}

			if (window.input.isKeyDown(Key.KEY_W)) {
				playerLoc.y -= 1.1f;
			}

			if (window.input.isKeyDown(Key.KEY_A)) {
				playerLoc.x -= 1.1f;
			}
			if (window.input.isKeyDown(Key.KEY_S)) {
				playerLoc.y += 1.1f;
			}
			if (window.input.isKeyDown(Key.KEY_D)) {
				playerLoc.x += 1.1f;
			}

			window.update();
		}
	}

	private void checkCollision(float xPos, float yPos, float scaleX, float scaleY) {
		Rectangle objRect = new Rectangle((int) xPos, (int) yPos, (int) scaleX, (int) scaleY);
		Rectangle playerRect = new Rectangle((int) playerLoc.x, (int) playerLoc.y, (int) playerScale.x, (int) playerScale.y);

		if (playerRect.intersects(objRect)) {
			System.out.println("COLLIDE");
		}
	}

	private NVGColor setColor(float r, float g, float b, float a) {
		if (r > 1) {
			r = r / 255f;
		}

		if (g > 1) {
			g = g / 255f;
		}

		if (b > 1) {
			b = b / 255f;
		}

		if (a > 1) {
			a = a / 255f;
		}
		color.r(r);
		color.g(g);
		color.b(b);
		color.a(a);
		return color;
	}

	private void close() {
		nvgEndFrame(ctx);
		nvgDelete(ctx);
		window.close();
		window.disposeGLFW();
	}

	public static void main(String[] args) {
		new TeamVs();
	}

	// private void reader()
	// {
	// try {
	// BufferedReader br = new BufferedReader(new FileReader(new
	// File("src/map.info")));
	// ArrayList<String> target = new ArrayList<String>();
	// target.add("map");
	// target.add("bounds");
	//
	// String endTargetFlag = "end";
	// boolean process = false;
	//
	// String line;
	// while ((line = br.readLine()) != null) {
	//// System.out.println(line);
	//
	// if(process) {
	// if(line.equals(endTargetFlag)) {
	// process = false;
	// continue;
	// }
	// System.err.println(line);
	// continue;
	// }
	//
	// for (String s : target) {
	// if (line.contains(s)) {
	// process = true;
	// System.out.println("Found");
	// }
	// }
	// Thread.sleep(325);
	// }
	//
	// br.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

}
