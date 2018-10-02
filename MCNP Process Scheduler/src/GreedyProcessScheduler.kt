// Process Scheduler using Greedy algorithm,
// selecting the processor with the current soonest finish time
class GreedyProcessScheduler : ProcessScheduler() {
    override fun nextProcessor(): Int {
        return processorTime.indexOf(processorTime.min())
    }
}