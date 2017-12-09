package team.vs.team;

public class Tile {

	private float red;
	private float green;
	private float blue;
	private float alpha;
	private boolean canCollide = false;

	public Tile(float red, float green, float blue, float alpha, boolean canCollide) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
		this.canCollide = canCollide;
	}

	public float getRed() {
		return red;
	}

	public void setRed(float red) {
		this.red = red;
	}

	public float getGreen() {
		return green;
	}

	public void setGreen(float green) {
		this.green = green;
	}

	public float getBlue() {
		return blue;
	}

	public void setBlue(float blue) {
		this.blue = blue;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public boolean isCanCollide() {
		return canCollide;
	}

	public void setCanCollide(boolean canCollide) {
		this.canCollide = canCollide;
	}
}
