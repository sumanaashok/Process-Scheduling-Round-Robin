
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class RoundRobinPriorityScheduling {

	static ArrayList<Integer> available_prog = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
	static ArrayList<Integer> available_prog_priority = new ArrayList<Integer>(Arrays.asList(2, 3, 1));
	static ArrayList<Integer> available_prog_burst_time = new ArrayList<Integer>(Arrays.asList(10, 7, 5));

	static ArrayList<Integer> burst_time = new ArrayList<Integer>(Arrays.asList(10, 7));
	static ArrayList<Integer> priority = new ArrayList<Integer>(Arrays.asList(2, 3));
	static ArrayList<Integer> process_number = new ArrayList<Integer>((Arrays.asList(1, 2)));
	static ArrayList<Integer> program_name = new ArrayList<Integer>((Arrays.asList(1, 2)));
	static ArrayList<Integer> gantt_chart = new ArrayList<Integer>();
	static ArrayList<Integer> ready_queue = new ArrayList<Integer>((Arrays.asList(1, 2)));

	static ArrayList<Integer> forking_processes = new ArrayList<Integer>((Arrays.asList(1, 2)));
	static ArrayList<Integer> forked_processes = new ArrayList<Integer>((Arrays.asList(2, 3)));

	static int forkingInterval = 3;
	static int time_quantum = 4;

	public static void main(String[] args) {

		while ((ready_queue.size()) != 0) {

			// fetch process number of highest priority process from ready queue
			int processNumIndex = priority.indexOf(Collections.min(priority));
			int processNo = ready_queue.get(priority.indexOf(Collections.min(priority)));

			// get the program number
			int programNumber = program_name.get(processNo - 1);

			int executionTime = 0;
			if (burst_time.get(processNo - 1) >= 4) {
				executionTime = 4;
			} else {
				executionTime = burst_time.get(processNo - 1);
			}

			// get the burst time for that process ID
			int burstTime = burst_time.get(processNo - 1);

			// decrease burst time of the executing process
			DecrementBurstTime(processNo);

			boolean isItForking = false;
			
			try {
				if (forking_processes.contains(programNumber) && ((float) burstTime / (float) forkingInterval) > 0.0) {
					isItForking = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (isItForking) {

				AddToProcess(programNumber);
			}

			// put the executing process back in the ready queue - if conditions met

			if (burst_time.get(processNo - 1) > 0) {
				ready_queue.add(processNo);
				priority.add(available_prog_priority.get(programNumber - 1));

			}

			// remove the executed processes from the ready queue
			ready_queue.remove(processNumIndex);
			priority.remove(processNumIndex);

			// add the process in the gantt chart
			for (int i = 0; i < executionTime; i++) {
				gantt_chart.add(processNo);
			}

		}

		System.out.println("Gantt Chart:\n");
		print_gantt_chart();

	}

	static void print_gantt_chart() {
		int count = 0;
		int masterCount = 0;

		String ganttChartOutput = "0<--";
		int whichProcess = 0;
		for (int i = 0; i < gantt_chart.size() - 1; i++) {

			if (i < gantt_chart.size() - 1 && gantt_chart.get(i) == gantt_chart.get(i + 1)) {
				count++;
			} else {
				masterCount += count + 1;
				ganttChartOutput += "P" + gantt_chart.get(i) + "-->" + masterCount + "<--";
				count = 0;
			}
			whichProcess = gantt_chart.get(i);
		}
		masterCount += count + 1;
		ganttChartOutput += "P" + whichProcess + "-->" + masterCount;
		System.out.println(ganttChartOutput);

	}

	static void DecrementBurstTime(int processNumber) {

		int val = burst_time.get(processNumber - 1);

		if (val > time_quantum) {

			burst_time.set(processNumber - 1, val - time_quantum);
		} else {
			burst_time.set(processNumber - 1, 0);

		}

	}

	static void AddToProcess(int progname) {
		int newProcessNum = process_number.size() + 1;
		process_number.add(newProcessNum);

		int forked_prog_num = forked_processes.get(forking_processes.indexOf(progname));

		program_name.add(forked_prog_num);
		ready_queue.add(newProcessNum);

		priority.add(available_prog_priority.get(forked_prog_num - 1));
		burst_time.add(available_prog_burst_time.get(forked_prog_num - 1));

	}

}
