public class Main {
    // Interface
    public interface Workout {
        String getWorkoutType();
        double calculateCaloriesBurned(double duration);
    }

    // Class 1
    public static class Running implements Workout {
        private double weight;

        public Running(double weight) {
            this.weight = weight;
        }

        @Override
        public String getWorkoutType() {
            return "Running";
        }

        @Override
        public double calculateCaloriesBurned(double duration) {
            double MET = 9.8;
            return 0.0175 * MET * weight * duration;
        }
    }

    // Class 2
    public static class Walking implements Workout {
        private double weight;

        public Walking(double weight) {
            this.weight = weight;
        }

        @Override
        public String getWorkoutType() {
            return "Walking";
        }

        @Override
        public double calculateCaloriesBurned(double duration) {
            double MET = 3.8;
            return 0.0175 * MET * weight * duration;
        }
    }

    public static void main(String[] args) {
        Workout running = new Running(70);
        Workout walking = new Walking(70);

        System.out.println("Workout: " + running.getWorkoutType());
        System.out.println("Calories burned (30 mins): " + running.calculateCaloriesBurned(30) + " kcal");

        System.out.println("Workout: " + walking.getWorkoutType());
        System.out.println("Calories burned (30 mins): " + walking.calculateCaloriesBurned(30) + " kcal");
    }
}
