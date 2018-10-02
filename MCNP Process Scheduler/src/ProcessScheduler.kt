// Process Scheduler using Circular algorithm
open class ProcessScheduler {
    // Last 4 digits are 0871
    private val studentNumber = 871

    private val numOfCores = studentNumber % 3 + 2
    // Delay time is the time spent processing to add a job to a processor
    private val delayTime = 1
    val turnaroundTime : Int get() = (processorTime.max() ?: 0) - firstJob.arrivalTime
    var j = 0
    var processorTime : MutableList<Int> = ArrayList()
    var currentTime : Int = 0
    lateinit var firstJob: Job

    init {
        for (num in 1.. numOfCores)
        {
            processorTime.add(0)
        }
    }
    /*
        Determines the next processor for a job
     */
    open fun nextProcessor() : Int { return (j++ +1)%numOfCores}

    /*
        Processes the next job passed by assigning it to the appropriate processor
        And updating relevant statistics
        @job : Job to be processed next
     */
    fun processNextJob(job: Job) {
        if (!::firstJob.isInitialized) {
            firstJob = job
        }
        job.processor = nextProcessor()
        var waitingTime = 0
        if (job.arrivalTime > currentTime){
            waitingTime = job.arrivalTime - currentTime
        }
        processorTime[job.processor] += waitingTime + delayTime + job.processingTime
        currentTime += waitingTime
    }
}
