package com.github.xericore.easycarts;

import org.bukkit.util.Vector;

public class SpeedAndYaw {

	private Double speed;
	private Vector direction;

	public SpeedAndYaw(double _speed, Vector _direction) {
		this.speed = _speed;
		this.direction = _direction;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Vector getDirection() {
		return direction;
	}

	public void setDirection(Vector direction) {
		this.direction = direction;
	}

}
