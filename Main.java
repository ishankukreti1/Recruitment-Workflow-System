import java.util.*;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static List<User> users = new ArrayList<>();
    static List<Job> jobs = new ArrayList<>();
    static List<Application> applications = new ArrayList<>();

    static int jobCounter = 1;
    static int appCounter = 1;

    static {
        users.add(new User("ishan", "123", Role.CANDIDATE));
        users.add(new User("rahul", "123", Role.RECRUITER));
    }

    public static void line() {
        System.out.println("--------------------------------------------------");
    }

    public static void title(String text) {
        line();
        System.out.println(" " + text);
        line();
    }

    public static void space() {
        System.out.println();
    }

    public static User login() {
        title("LOGIN");

        System.out.print("Username: ");
        String u = sc.next();

        System.out.print("Password: ");
        String p = sc.next();

        for (User user : users) {
            if (user.username.equals(u) && user.password.equals(p)) {
                System.out.println("Login successful as " + user.role + "\n");
                return user;
            }
        }

        System.out.println("Invalid credentials!\n");
        return null;
    }

    public static void register() {
        title("USER REGISTRATION");

        System.out.print("Enter username: ");
        String username = sc.next();

        for (User u : users) {
            if (u.username.equals(username)) {
                System.out.println("Username already exists!\n");
                return;
            }
        }

        System.out.print("Enter password: ");
        String password = sc.next();

        System.out.println("1. Candidate\n2. Recruiter");
        System.out.print("Enter role: ");
        int choice = sc.nextInt();

        Role role = (choice == 1) ? Role.CANDIDATE : Role.RECRUITER;

        users.add(new User(username, password, role));
        System.out.println("Registration successful!\n");
    }

    public static void postJob() {
        title("POST JOB");

        System.out.print("Enter job title: ");
        String title = sc.next();

        jobs.add(new Job(jobCounter++, title));
        System.out.println("Job posted successfully!\n");
    }

    public static void viewJobs() {
        title("AVAILABLE JOBS");

        for (Job j : jobs) {
            System.out.println("[" + j.jobId + "] " + j.title);
        }
        space();
    }

    public static void applyJob(User user) {
        viewJobs();

        if (jobs.isEmpty()) return;

        System.out.print("Enter Job ID: ");
        int id = sc.nextInt();

        for (Job j : jobs) {
            if (j.jobId == id) {
                applications.add(new Application(appCounter++, user.username, j));
                System.out.println("Applied successfully! Status = APPLIED\n");
                return;
            }
        }

        System.out.println("Invalid Job ID!\n");
    }

    public static void viewApplications() {
        title("APPLICATION DASHBOARD");

        for (Application app : applications) {
            System.out.println("AppID: " + app.appId +
                    " | Candidate: " + app.candidateName +
                    " | Job: " + app.job.title +
                    " | Status: " + app.status);
        }
        space();
    }

    public static boolean validTransition(ApplicationStatus current, ApplicationStatus next) {
        switch (current) {
            case APPLIED:
                return next == ApplicationStatus.SHORTLISTED;
            case SHORTLISTED:
                return next == ApplicationStatus.INTERVIEW;
            case INTERVIEW:
                return next == ApplicationStatus.SELECTED || next == ApplicationStatus.REJECTED;
            default:
                return false;
        }
    }

    public static void updateStatus() {
        title("UPDATE STATUS");

        System.out.print("Enter Application ID: ");
        int id = sc.nextInt();

        for (Application app : applications) {
            if (app.appId == id) {

                System.out.println("Current Status: " + app.status);
                System.out.println("1. SHORTLISTED");
                System.out.println("2. INTERVIEW");
                System.out.println("3. SELECTED");
                System.out.println("4. REJECTED");

                System.out.print("Enter choice: ");
                int ch = sc.nextInt();

                ApplicationStatus newStatus = null;

                switch (ch) {
                    case 1: newStatus = ApplicationStatus.SHORTLISTED; break;
                    case 2: newStatus = ApplicationStatus.INTERVIEW; break;
                    case 3: newStatus = ApplicationStatus.SELECTED; break;
                    case 4: newStatus = ApplicationStatus.REJECTED; break;
                }

                if (validTransition(app.status, newStatus)) {
                    app.status = newStatus;
                    System.out.println("Status updated!\n");
                } else {
                    System.out.println("Invalid transition!\n");
                }
                return;
            }
        }

        System.out.println("Application not found!\n");
    }

    public static void main(String[] args) {

        while (true) {
            title("RECRUITMENT WORKFLOW SYSTEM");

            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            System.out.print("\nEnter your choice: ");
            int choice = sc.nextInt();

            if (choice == 3) break;

            if (choice == 2) {
                register();
                continue;
            }

            User user = login();
            if (user == null) continue;

            if (user.role == Role.CANDIDATE) {
                while (true) {
                    title("CANDIDATE DASHBOARD");

                    System.out.println("1. View Jobs");
                    System.out.println("2. Apply Job");
                    System.out.println("3. Logout");

                    System.out.print("\nEnter your choice: ");
                    int ch = sc.nextInt();

                    if (ch == 1) viewJobs();
                    else if (ch == 2) applyJob(user);
                    else break;
                }
            } else {
                while (true) {
                    title("RECRUITER DASHBOARD");

                    System.out.println("1. Post Job");
                    System.out.println("2. View Applications");
                    System.out.println("3. Update Status");
                    System.out.println("4. Logout");

                    System.out.print("\nEnter your choice: ");
                    int ch = sc.nextInt();

                    if (ch == 1) postJob();
                    else if (ch == 2) viewApplications();
                    else if (ch == 3) updateStatus();
                    else break;
                }
            }
        }
    }
}
