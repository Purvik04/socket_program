public class Test {
    public static void main(String[] args) {
        // Adding a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown Hook: Cleaning up resources...");
        }));

        System.out.println("Application is running...");

        try {
            Thread.sleep(5000); // Simulating some work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Exiting application...");
    }
}
