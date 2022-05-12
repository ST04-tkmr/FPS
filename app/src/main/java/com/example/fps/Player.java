package com.example.fps;

class Player {
    Vec2 pos,v;//位置ベクトル,速度ベクトル
    double angle;

    Player() {
        this.pos = new Vec2(0, 0);
        this.v = new Vec2(0, 0);
        this.angle = 0;
    }
}
