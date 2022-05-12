package com.example.fps;

class Ray2 {
    //始点の位置ベクトル, 位置ベクトルから伸びる方向ベクトル, 終点の位置ベクトル
    Vec2 pos,way;

    Ray2(Vec2 pos, Vec2 way) {
            this.pos = pos;
            this.way = way;
    }

    static Ray2 withPoints(Vec2 begin, Vec2 end) {
        return new Ray2(begin, end.sub(begin));
    }

    protected Vec2 end() {
        return pos.add(way);
    }

    protected Vec2 intersection(Ray2 r2) {
        Ray2 r1 = this;
        //Y軸平行の時は微妙にずらす
        if (Math.abs(r1.way.x) < 0.01) r1.way.x = 0.01;
        if (Math.abs(r2.way.x) < 0.01) r2.way.x = 0.01;
        //r1とr2を直線としてみて交点を求める
        double t1 = r1.way.y/r1.way.x;
        double t2 = r2.way.y/r2.way.x;
        double x1 = r1.pos.x;
        double x2 = r2.pos.x;
        double y1 = r1.pos.y;
        double y2 = r2.pos.y;
        double sx = (t1*x1 - t2*x2 - y1 + y2)/(t1 - t2);
        double sy = t1*(sx - x1) + y1;
        //交点が線分上にない時はnullを返す
        boolean xInRange =
                sx > Math.min(r1.pos.x, r1.end().x) && sx < Math.max(r1.pos.x, r1.end().x)
                        && sx > Math.min(r2.pos.x, r2.end().x) && sx < Math.max(r2.pos.x, r2.end().x);
        if (xInRange) {
            return new Vec2(sx, sy);
        } else {
            return null;
        }
    }
}
