package com.example.fps;

import static com.example.fps.MainActivity.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class CustomView extends View {
    Paint paintWhiteWall = new Paint();
    Paint paintYellow = new Paint();
    Paint paintOrange = new Paint();
    Paint paintWhiteBeam = new Paint();
    Paint paintBlack = new Paint();
    Paint paintBlue = new Paint();
    Paint paintRed = new Paint();
    Ray2 viewRect,beam;
    double fov,centerAngle,leftAngle,rightAngle;
    double wallDist,wallPerpDist,lineHeight;
    double tileSize,pillarSize;
    int beamTotal,beamIndex;
    ArrayList<Vec2> allHitBeamWays;
    Vec2 hitBeam,hitPos,lineBegin,a;
    boolean pillarFlagX,pillarFlagY;
    RectF bounds,pillar;
    Float playerRadius;

    public CustomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paintWhiteWall.setColor(Color.WHITE);
        paintWhiteWall.setStrokeWidth(3f);
        paintWhiteWall.setStyle(Paint.Style.FILL);
        paintYellow.setColor(Color.YELLOW);
        paintYellow.setAlpha(160);
        paintYellow.setStrokeWidth(0.5f);
        paintOrange.setColor(Color.argb(255, 255, 168, 0));
        paintOrange.setStyle(Paint.Style.FILL);
        paintOrange.setStrokeWidth(0.5f);
        paintBlack.setColor(Color.BLACK);
        paintBlack.setAlpha(200);
        paintWhiteBeam.setColor(Color.WHITE);
        paintWhiteBeam.setStrokeWidth(0.5f);
        paintBlue.setColor(Color.argb(255, 66, 200, 251));
        paintBlue.setStyle(Paint.Style.STROKE);
        paintBlue.setStrokeWidth(6f);
        paintRed.setColor(Color.RED);
        paintRed.setStyle(Paint.Style.FILL);

        tileSize = game.level.tileSize;
        viewRect = new Ray2(
                new Vec2(3, tileSize*game.level.mapHeight + 10),
                new Vec2(0, 0)
        );
        beam = new Ray2(new Vec2(0, 0), new Vec2(0, 0));
        fov = Math.PI/2;
        beamTotal = 80;
        allHitBeamWays = new ArrayList<>();
        a = new Vec2(0, 0);
        bounds = new RectF();
        bounds.left = 0;
        bounds.top = 0;
        pillar = new RectF();
        centerAngle = game.player.angle;
        playerRadius = 5f;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        //背景
        bounds.right = getWidth();
        bounds.bottom = getHeight()*3f/4f;
        paintBlack.setStyle(Paint.Style.FILL);
        canvas.drawRect(bounds, paintBlack);
        paintBlack.setStyle(Paint.Style.STROKE);

        canvas.translate(getWidth()/256f, getWidth()/256f);

        //壁を描画
        for (Ray2 wall: game.level.walls) {
            canvas.drawLine(
                    (float)wall.pos.x, (float)wall.pos.y,
                    (float)wall.end().x, (float)wall.end().y,
                    paintWhiteWall
            );
        }

        //プレイヤー描画
        canvas.drawCircle(
                (float)game.player.pos.x,
                (float)game.player.pos.y,
                playerRadius*2,
                paintRed
        );

        centerAngle = game.player.angle;
        leftAngle = centerAngle - fov/2;
        rightAngle = centerAngle + fov/2;
        beamIndex = -1;
        for (double angle = leftAngle;angle<(rightAngle-0.01);angle+=fov/beamTotal) {
            beamIndex++;
            beam.pos = game.player.pos.copy();
            beam.way.x = Math.cos(angle);
            beam.way.y = Math.sin(angle);
            beam.way = beam.way.mul(125);
            allHitBeamWays.clear();

            //接点を計算
            for (int i=0;i<game.level.walls.size();i++) {
                Vec2 b = beam.intersection(game.level.walls.get(i));
                if (b != null) {
                    allHitBeamWays.add(b.sub(beam.pos));
                }
            }

            //接点がなければ黄色の光線
            if (allHitBeamWays.size() == 0) {
                float x1 = (float)beam.pos.x;
                float y1 = (float)beam.pos.y;
                float x2 = (float)beam.end().x;
                float y2 = (float)beam.end().y;
                canvas.drawLine(x1, y1, x2, y2, paintYellow);
            } else {
                hitBeam = allHitBeamWays.get(0);
                //接点が2個以上の時は一番近い接点を選ぶ
                for (int i=0;i<allHitBeamWays.size();i++) {
                    if (allHitBeamWays.get(i).mag() < hitBeam.mag()) {
                        hitBeam = allHitBeamWays.get(i);
                    }
                }
                hitPos = hitBeam.add(beam.pos);
                wallDist = hitBeam.mag();
                wallPerpDist = wallDist*Math.cos(angle - centerAngle);
                if (8000/wallPerpDist < 0) {
                    lineHeight = 0;
                } else lineHeight = Math.min(8000/wallPerpDist, viewRect.way.y);
                lineHeight -= lineHeight%8;
                a.x = (viewRect.way.x/beamTotal)*beamIndex;
                a.y = viewRect.way.y/2 - lineHeight/2;
                lineBegin = viewRect.pos.add(a);
                pillarSize = 2;
                pillarFlagX = hitPos.x%tileSize < pillarSize || hitPos.x%tileSize > tileSize - pillarSize;
                pillarFlagY = hitPos.y%tileSize < pillarSize || hitPos.y%tileSize > tileSize - pillarSize;
                pillar.set(
                        (float)lineBegin.x, (float)lineBegin.y,
                        getWidth()/96f + (float)lineBegin.x, (float)(lineHeight + lineBegin.y)
                );
                if (pillarFlagX && pillarFlagY) {
                    //3Dビュー
                    canvas.drawRect(pillar, paintOrange);
                    //俯瞰図
                    canvas.drawLine(
                            (float)game.player.pos.x, (float)game.player.pos.y,
                            (float)game.player.pos.add(hitBeam).x, (float)game.player.pos.add(hitBeam).y,
                            paintOrange
                    );
                } else {
                    //3Dビュー
                    canvas.drawRect(pillar, paintWhiteWall);
                    //俯瞰図
                    canvas.drawLine(
                            (float)game.player.pos.x, (float)game.player.pos.y,
                            (float)game.player.pos.add(hitBeam).x, (float)game.player.pos.add(hitBeam).y,
                            paintWhiteBeam
                    );
                }
            }
        }

        canvas.restore();

        //3Dビューの枠
        viewRect.way.x = getWidth() - 6;
        viewRect.way.y = getHeight()*3f/4f - tileSize*game.level.mapHeight - 10;
        canvas.drawRect(
                (float)viewRect.pos.x, (float)viewRect.pos.y,
                (float)viewRect.end().x, (float)viewRect.end().y,
                paintBlue
        );
    }

    protected void action() {
        //forward
        if (CustomButton.buttonStates[0] == 1) {
            Vec2 vForward = game.player.v.rotate(centerAngle);
            game.player.pos.x += vForward.x;
            game.player.pos.y += vForward.y;
        }
        //backwards
        if (CustomButton.buttonStates[1] == 1) {
            Vec2 vBack = game.player.v.rotate(centerAngle + fov*2);
            game.player.pos.x += vBack.x;
            game.player.pos.y += vBack.y;
        }
        //left
        if (CustomButton.buttonStates[2] == 1) {
            Vec2 vLeft = game.player.v.rotate(centerAngle - fov);
            game.player.pos.x += vLeft.x;
            game.player.pos.y += vLeft.y;
        }
        //right
        if (CustomButton.buttonStates[3] == 1) {
            Vec2 vRight = game.player.v.rotate(centerAngle + fov);
            game.player.pos.x += vRight.x;
            game.player.pos.y += vRight.y;
        }

        if (CustomButton.buttonStates[4] == 1) {
            game.player.angle -= Math.PI/45;
        } else if (CustomButton.buttonStates[5] == 1) {
            game.player.angle += Math.PI/45;
        }

        //当たり判定
        for (int i=0;i<game.level.walls.size();i++) {
            Player p = game.player;
            Ray2 w = game.level.walls.get(i);
            //上方向
            Ray2 up = new Ray2(
                    new Vec2(p.pos.x, p.pos.y), new Vec2(0, -playerRadius)
            );
            Vec2 u = up.intersection(w);
            if (u != null) {
                p.pos.y = u.y + playerRadius;
            }
            //下方向
            Ray2 down = new Ray2(
                    new Vec2(p.pos.x, p.pos.y), new Vec2(0, playerRadius)
            );
            Vec2 d = down.intersection(w);
            if (d != null) {
                p.pos.y = d.y - playerRadius;
            }
            //左方向
            Ray2 left = new Ray2(
                    new Vec2(p.pos.x, p.pos.y), new Vec2(-playerRadius, 0)
            );
            Vec2 l = left.intersection(w);
            if (l != null) {
                p.pos.x = l.x + playerRadius;
            }
            //右方向
            Ray2 right = new Ray2(new Vec2(p.pos.x, p.pos.y), new Vec2(playerRadius, 0));
            Vec2 r = right.intersection(w);
            if (r != null) {
                p.pos.x = r.x - playerRadius;
            }
        }
    }
}

