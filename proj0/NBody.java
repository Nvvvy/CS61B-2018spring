public class NBody {
    /**
     * Simulate a universe specified in the date files.
     */


    /*
     * Read the Radius from the given file.
     */
    public static double readRadius(String filename) {
        In in = new In(filename);

        int planets_num = in.readInt();
		double universe_radius = in.readDouble();

        return universe_radius;
    }

    /*
     * Read the planet data from the file
     * and instantiate given numbers of planet
     * then Return an array of planets
     */
    public static Planet[] readPlanets(String filename) {
        In in = new In(filename);

        int size = in.readInt();
		double universe_radius = in.readDouble();

        Planet planets_arr[] = new Planet[size];
        int index = 0;
        while (index < size) {
            Double xx_pos = in.readDouble();
            Double yy_pos = in.readDouble();
            Double xx_vel = in.readDouble();
            Double yy_vel = in.readDouble();
            Double mass = in.readDouble();
            String img_name = in.readString();

            planets_arr[index] = new Planet(xx_pos, yy_pos, xx_vel, yy_vel, mass, img_name);
            index += 1;
        }
        return planets_arr;
    }
    
    /*
     * Drawing the Initial Universe State (main)
     */
    public static void main(String[] args) {

        /*
         * Collecting All Needed Input
         * read radius and planets from the args in the command line;
         */
        Double T = Double.parseDouble(args[0]), dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planet_array = readPlanets(filename);


        StdDraw.enableDoubleBuffering(); // prevent flickering in the animation 
        /*
         * Drawing the Background with StdDraw library
         * check the StdDrawDemo.java for reference
         */

        StdDraw.setScale(-radius, radius);//set the scale of canvas
        StdDraw.clear();
        StdDraw.picture(0, 0, "images/starfield.jpg");
        StdDraw.show();

        /*Drawing Every Planet listed in the planet array*/
        for (Planet p : planet_array) {
            p.draw();
        }

        /* Creating Animation */
        for (double time = 0; time < T; time += dt) {
            /*calculate the NetForce(in x and y) for every planet */
            double[] xx_forces = new double[planet_array.length];
            double[] yy_forces = new double[planet_array.length];
            for (int i = 0; i < planet_array.length; i += 1) {
                xx_forces[i] = planet_array[i].calcNetForceExertedByX(planet_array);
                yy_forces[i] = planet_array[i].calcNetForceExertedByY(planet_array);
            }

            /* update the position, velocity for every planet */
            for (int i = 0; i < planet_array.length; i += 1) {
                planet_array[i].update(dt, xx_forces[i], yy_forces[i]);
            }
            
            /* draw a static starfield canvas without planets */
            StdDraw.setScale(-radius, radius);//set the scale of canvas
            StdDraw.clear();
            StdDraw.picture(0, 0, "images/starfield.jpg");

            /* draw a static picture containing all planets */
            for (Planet p : planet_array) {
                p.draw();
            }

            /* display the static picture for 10 milliseconds */
            StdDraw.show();
            StdDraw.pause(10);
        }

        /*Print out the the final state of the universe */
        StdOut.printf("%d\n", planet_array.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planet_array.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                          planet_array[i].xxPos, planet_array[i].yyPos, planet_array[i].xxVel,
                          planet_array[i].yyVel, planet_array[i].mass, planet_array[i].imgFileName);   
        }



    }
}
