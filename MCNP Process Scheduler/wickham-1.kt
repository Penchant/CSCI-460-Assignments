import java.util.concurrent.ThreadLocalRandom
/*
    @author Dylan Wickham
    This is a Multicore Non-preemptive Process Scheduler using both a Circular and a Greedy algorithm

    Instructions on how to run:
        After installing kotlin, using instructions here: https://www.codexpedia.com/kotlin/install-compile-and-run-kotlin-from-command-line/
        kotlinc main.kt Job.kt ProcessScheduler.kt GreedyProcessScheduler.kt -include-runtime -d wickham.jar
        java -jar wickham.jar
 */

fun main(args: Array<String>) {
    val turnaroundTimes = mutableListOf<Int>()
    val greedyTurnaroundTimes = mutableListOf<Int>()
    var jobs: MutableList<Job> = mutableListOf()

    var processScheduler = ProcessScheduler()
    var greedyProcessScheduler = GreedyProcessScheduler()

    for(j in 1.. 1000) {
        for (i in 1..100) {
            jobs.add(Job(i, ThreadLocalRandom.current().nextInt(1, 501)))
        }
        for(job in jobs)
        {
            processScheduler.processNextJob(job)
            greedyProcessScheduler.processNextJob(job)
        }
        turnaroundTimes.add(processScheduler.turnaroundTime)
        greedyTurnaroundTimes.add(greedyProcessScheduler.turnaroundTime)

        jobs = mutableListOf()
        processScheduler= ProcessScheduler()
        greedyProcessScheduler = GreedyProcessScheduler()
    }

    val min : Int = turnaroundTimes.min() ?: 0
    val max : Int = turnaroundTimes.max() ?: 0
    val average : Double = turnaroundTimes.average()
    val standardDeviation : Double = Math.pow(turnaroundTimes.parallelStream().mapToDouble { x -> Math.pow(x - average, 2.0) }.sum() / (turnaroundTimes.count() -1), .5)
    print("----------------- Circular Statistics -----------------\n")
    print("Min: $min \nMax: $max \nAverage: $average \nStandard deviation $standardDeviation \n")
    print("-------------------------------------------------------\n")

    val gmin : Int = greedyTurnaroundTimes.min() ?: 0
    val gmax : Int = greedyTurnaroundTimes.max() ?: 0
    val gaverage : Double = greedyTurnaroundTimes.average()
    val gstandardDeviation : Double = Math.pow(greedyTurnaroundTimes.parallelStream().mapToDouble { x -> Math.pow(x - average, 2.0) }.sum() / (greedyTurnaroundTimes.count() -1), .5)
    print("----------------- Greedy Statistics -----------------\n")
    print("Min: $gmin \nMax: $gmax \nAverage: $gaverage \nStandard deviation $gstandardDeviation \n")
    print("-----------------------------------------------------\n")

    jobs = mutableListOf()
    processScheduler = ProcessScheduler()
    greedyProcessScheduler = GreedyProcessScheduler()

    jobs.add(Job(4, 9))
    jobs.add(Job(15, 2))
    jobs.add(Job(18, 16))
    jobs.add(Job(20, 3))
    jobs.add(Job(26, 29))
    jobs.add(Job(29, 198))
    jobs.add(Job(35, 7))
    jobs.add(Job(45, 170))
    jobs.add(Job(57, 180))
    jobs.add(Job(83, 178))
    jobs.add(Job(88, 73))
    jobs.add(Job(95, 8))
    for(job in jobs)
    {
        processScheduler.processNextJob(job)
        greedyProcessScheduler.processNextJob(job)
    }
    turnaroundTimes.add(processScheduler.turnaroundTime)
    print("Circular turnaround time was ${processScheduler.turnaroundTime} \n")
    print("Greedy turnaround time was ${greedyProcessScheduler.turnaroundTime} \n")
}

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

// Process Scheduler using Greedy algorithm,
// selecting the processor with the current soonest finish time
class GreedyProcessScheduler : ProcessScheduler() {
    override fun nextProcessor(): Int {
        return processorTime.indexOf(processorTime.min())
    }
}

class Job(val arrivalTime : Int, val processingTime : Int) {
    var processor : Int = -1
}