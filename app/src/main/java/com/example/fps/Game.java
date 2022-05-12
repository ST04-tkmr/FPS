package com.example.fps;

class Game {
    Player player;
    Level level;

    Game() {
        this.player = new Player();
        this.level = new Level();
    }

    protected void reset() {
        this.player.pos = new Vec2(100, 200);
        this.player.v = new Vec2(2, 0);
        this.player.angle = -Math.PI/2;
    }
}
