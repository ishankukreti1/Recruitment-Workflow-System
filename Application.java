class Application {
    int appId;
    String candidateName;
    Job job;
    ApplicationStatus status;

    public Application(int appId, String candidateName, Job job) {
        this.appId = appId;
        this.candidateName = candidateName;
        this.job = job;
        this.status = ApplicationStatus.APPLIED;
    }
}
