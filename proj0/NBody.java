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








    }
}
