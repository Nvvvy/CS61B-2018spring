public class Planet {

    /*Planet Instance Variables */
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    /*Constant G to calculate Force*/
    private static final double G = 6.67e-11;

    /* First Planet Constructor:
     * given postion, velocity, mass and img
     */
    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP; yyPos = yP; xxVel = xV; yyVel = yV; mass = m; imgFileName = img;
    }

    /* Second Planet Constructor:
     * make a copy of given planet p
     */
    public Planet(Planet p) {
        this.xxPos = p.xxPos; this.yyPos = p.yyPos;
        this.xxVel = p.xxVel; this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    /* Clacualte the distance between <this> and given planet p */
    public double calcDistance(Planet p) {
        double dx = p.xxPos - this.xxPos , dy = p.yyPos - this.yyPos;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }

    /*Calculate the force exerted by the given planet p */
    public double calcForceExertedBy(Planet p) {
        double distance = this.calcDistance(p);
        double force = (G * this.mass * p.mass)/(Math.pow(distance, 2));
        return force;
    }

    public double calcForceExertedByX(Planet p) {
        double dx = p.xxPos - this.xxPos;
        double xx_force = this.calcForceExertedBy(p) * dx / this.calcDistance(p);
        return xx_force;
    }

    public double calcForceExertedByY(Planet p) {
        double dy = p.yyPos - this.yyPos;
        double yy_force = this.calcForceExertedBy(p) * dy / this.calcDistance(p);
        return yy_force;
    }

    public double calcNetForceExertedByX(Planet[] planets_array) {
        double xx_net_force = 0;
        for (Planet p : planets_array) {
            if (this.equals(p)) {
                continue;
            }
            else xx_net_force += this.calcForceExertedByX(p);
        }
        return xx_net_force;
    }

    public double calcNetForceExertedByY(Planet[] planets_array) {
        double yy_net_force = 0;
        for (Planet p : planets_array) {
            if (this.equals(p)) {
                continue;
            }
            else yy_net_force += this.calcForceExertedByY(p);
        }
        return yy_net_force;
    }

    /**
     * Determin how much the forces exerted on the planet will cause that planet to accelerate
     * @param dt time that force exerted on <this> planet
     * @param xx_force x-force exerted on <this> planet
     * @param yy_force y-force exerted on <this> planet
     */
    public void update(double dt, double xx_force, double yy_force) {
        double xx_accel = xx_force / this.mass, yy_accel = yy_force / this.mass;
        this.xxVel += dt * xx_accel;
        this.yyVel += dt * yy_accel;
        this.xxPos += dt * xxVel;
        this.yyPos += dt * yyVel;
    }

    /*Draw a planet corresponding to <this> position and img */
    public void draw() {
        String imgRoutine = "images/" + this.imgFileName;
        StdDraw.picture(this.xxPos, this.yyPos, imgRoutine);
    }

}