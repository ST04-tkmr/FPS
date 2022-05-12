package com.example.fps;

class Vec2 {
    double x,y;

    Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    protected Vec2 add(Vec2 b) {
        Vec2 a = this;
        return new Vec2(a.x + b.x, a.y + b.y);
    }

    protected Vec2 sub(Vec2 b) {
        Vec2 a = this;
        return new Vec2(a.x - b.x, a.y - b.y);
    }

    protected Vec2 copy() {
        return new Vec2(this.x, this.y);
    }

    protected Vec2 mul(double s) {
        return new Vec2(s*this.x, s*this.y);
    }

    protected double mag() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    protected Vec2 rotate(double angle) {
        Vec2 a  = this;
        return new Vec2(
                Math.cos(angle)*a.x - Math.sin(angle)*a.y,
                Math.sin(angle)*a.x + Math.cos(angle)*a.y
        );
    }
}
