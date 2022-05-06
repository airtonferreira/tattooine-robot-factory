package tatooine_robot_factory;

import robocode.*;
import robocode.util.Utils;
import java.awt.*;

/**
 * R2D2 - a robot by Airton Ferreira
 * This robot was inspired on the robots of Robocode examples and Robocode documentation.
 */

public class R2D2 extends AdvancedRobot {
    boolean movingRetrieve;

    public void run() {
        setBodyColor(Color.white);
        setGunColor(new Color(0,35,109));
        setRadarColor(new Color(84,104,177));
        setBulletColor(Color.yellow);
        setScanColor(Color.white);

        while (true) {
            this.setAhead(10000);
            this.movingRetrieve = true;
            this.setTurnLeft(180);
            this.waitFor(new TurnCompleteCondition(this));
            this.setTurnRight(360);
            this.waitFor(new TurnCompleteCondition(this));
            this.setTurnLeft(360);
            this.waitFor(new TurnCompleteCondition(this));
        }
    }

    public void onScannedRobot(ScannedRobotEvent event) {
        double absoluteBearing = event.getBearing() + getHeading();
        double bearingFromGun = Utils.normalRelativeAngleDegrees(absoluteBearing - this.getGunHeading());
        if (Math.abs(bearingFromGun) <= 3.0) {
            this.turnGunRight(bearingFromGun);
            if (this.getGunHeat() == 0.0) {
                this.fire(Math.min(3.0 - Math.abs(bearingFromGun), this.getEnergy() - 0.1));
            }
        }

        this.turnGunRight(bearingFromGun);

        if (event.getDistance() <= 100) {
            if (event.getHeading() == getHeading()) {
                setBack(10000);
            }
            this.reverseDirection();
        }

        if (bearingFromGun == 0.0) {
            this.scan();
        }
    }

    public void onHitWall(HitWallEvent event) {
        this.reverseDirection();
    }

    public void onHitRobot(HitRobotEvent event) {
        setFireBullet(3);
        if (event.isMyFault()) {
            this.reverseDirection();
        }
    }

    public void onHitByBullet(HitByBulletEvent event) {
        this.reverseDirection();
    }

    public void reverseDirection() {
        if (movingRetrieve) {
            setAhead(10000);
            movingRetrieve = false;
        }
        setBack(10000);
        movingRetrieve = true;
    }
}
