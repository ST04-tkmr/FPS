package com.example.fps;

import java.util.ArrayList;

class Level {
    ArrayList<Ray2> walls;
    int[][] tileMap;
    double tileSize;
    double mapWidth;
    double mapHeight;

    Level() {
        walls = new ArrayList<>();
        this.tileMap = new int[][]{};
        this.tileSize = 0;
        this.mapWidth = 0;
        this.mapHeight = 0;
    }

    protected void addWorldEdge(double size, double width, double height) {
        this.tileSize = size;
        this.mapWidth = width;
        this.mapHeight = height;
        this.walls.add(new Ray2(
                new Vec2(0, 0), new Vec2(size*width, 0)
        ));
        this.walls.add(new Ray2(
                new Vec2(0, 0), new Vec2(0, size*height)
        ));
        this.walls.add(new Ray2(
                new Vec2(size*width, size*height), new Vec2(-size*width, 0)
        ));
        this.walls.add(new Ray2(
                new Vec2(size*width, size*height), new Vec2(0, -size*height)
        ));
    }

    protected void addTileMap(int[][] tileMap) {
        double s = this.tileSize;
        double w = this.mapWidth;
        double h = this.mapHeight;
        for (int y=0;y<h;y++) {
            for (int x=0;x<w;x++) {
                int tile = tileMap[y][x];
                if (tile == 1) {
                    this.walls.add(new Ray2(
                            new Vec2(s*x, s*y), new Vec2(s, 0)));
                    this.walls.add(new Ray2(
                            new Vec2(s*x, s*y), new Vec2(0, s)));
                    if (y < (h - 1)) {
                        if (tileMap[y + 1][x] == 0) {
                            this.walls.add(new Ray2(
                                    new Vec2(s * x + s, s * y + s), new Vec2(-s, 0)));
                        }
                    }
                    if (x < (w - 1)) {
                        if (tileMap[y][x + 1] == 0) {
                            this.walls.add(new Ray2(
                                    new Vec2(s * x + s, s * y + s), new Vec2(0, -s)));
                        }
                    }
                }
            }
        }
    }
}

