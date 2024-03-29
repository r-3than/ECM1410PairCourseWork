package cycling;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

/**
 * CyclingPortal implements CyclingPortalInterface; contains methods for
 * handling the following classes: Race, Stage, Segment, RiderManager (and in
 * turn Rider and Team), and Result.
 * These classes are used manage races and their subdivisions, teams and their
 * riders, and to calculate and assign points from riders' results.
 * Also contains methods for saving and loading (Mini)CyclingPortalInterface to
 * and from a file.
 * 
 * @author Ethan Ray & Thomas Newbold
 * @version 1.1
 *
 */
public class CyclingPortal implements CyclingPortalInterface {
	public RiderManager riderManager = new RiderManager();

	@Override
	public int[] getRaceIds() {
		return Race.getAllRaceIds();
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		Race r = new Race(name, description);
		return r.getRaceId();
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		double sum = 0.0;
		for(int id : Race.getStages(raceId)) {
			sum += Stage.getStageLength(id);
		}
		// calculates total stage length to append to string
		return Race.toString(raceId)+Double.toString(sum)+";";
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		Race.removeRace(raceId);
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		int[] stageIds = Race.getStages(raceId);
		return stageIds.length;
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		return Race.addStageToRace(raceId, stageName, description, length, startTime, type);
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		return Race.getStages(raceId);
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		return Stage.getStageLength(stageId);
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		Race.removeStage(stageId);
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		return Stage.addSegmentToStage(stageId, location, type, averageGradient, length);
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		// adds stage with type SPRINT, and length 0.0
		return Stage.addSegmentToStage(stageId, location, SegmentType.SPRINT, 0.0, 0.0);
	}

	@Override
	public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {
		Stage.removeSegment(segmentId);
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		Stage.updateStageState(stageId);
	}

	@Override
	public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
		return Stage.getSegments(stageId);
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		return riderManager.createTeam(name, description);
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		riderManager.removeTeam(teamId);
	}

	@Override
	public int[] getTeams() {
		return riderManager.getTeams();
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		return riderManager.getTeamRiders(teamId);
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException, IllegalArgumentException {
		return riderManager.createRider(teamID, name, yearOfBirth);
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		riderManager.removeRider(riderId);
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
			InvalidStageStateException {
		if(Stage.getStageState(stageId).equals(StageState.BUILDING)) {
			throw new InvalidStageStateException("stage is not waiting for results");
		} else if(Stage.getSegments(stageId).length+2 != checkpoints.length) {
			throw new InvalidCheckpointsException("checkpoint count mismatch");
		}
		try {
			Result.getResult(stageId, riderId);
			throw new DuplicatedResultException("result already exists for rider in stage");
		} catch(IDNotRecognisedException ex) {
			Stage.getStage(stageId);
			riderManager.getRider(riderId);
			// above should throw exceptions if IDs are not in system
			new Result(stageId, riderId, checkpoints);
		}
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Stage.getStage(stageId);
		riderManager.getRider(riderId);
		// above should throw exceptions if IDs are not in system
		Result result = Result.getResult(stageId, riderId);
		LocalTime[] checkpointTimes = result.getCheckpoints();
		LocalTime[] out = new LocalTime[checkpointTimes.length+1];
		for(int i=0; i<checkpointTimes.length; i++) {
			out[i] = checkpointTimes[i];
		}
		out[checkpointTimes.length] = result.getTotalElasped();
		// adds total elapsed time to end of split times list
		return out;
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Stage.getStage(stageId);
		riderManager.getRider(riderId);
		// above should throw exceptions if IDs are not in system
		LocalTime[] adjustedTimes = Result.getResult(stageId, riderId).adjustedCheckpoints();
		LocalTime elapsedTime = adjustedTimes[0];
		for(int i=1; i<adjustedTimes.length; i++) {
			LocalTime t = adjustedTimes[i];
			elapsedTime = elapsedTime.plusHours(t.getHour());
			elapsedTime = elapsedTime.plusMinutes(t.getMinute());
			elapsedTime = elapsedTime.plusSeconds(t.getSecond());
			elapsedTime = elapsedTime.plusNanos(t.getNano());
			// sums adjusted split times
		}
		return elapsedTime;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Stage.getStage(stageId);
		riderManager.getRider(riderId);
		// above should throw exceptions if IDs are not in system
		Result.removeResult(stageId, riderId);
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		Result[] results = Result.getResultsInStage(stageId);
		int[] riderRanks = new int[results.length];
		Arrays.fill(riderRanks, -1); // 0 may be a rider id
		for(Result r : results) {
			for(int i=0; i<riderRanks.length; i++) {
				if(riderRanks[i] == -1) {
					riderRanks[i] = r.getRiderId();
					break;
				} else if(r.getTotalElasped().isBefore(Result.getResult(stageId, riderRanks[i]).getTotalElasped())) {
					// add id at position i, move other ids down
					int temp;
					int prev = r.getRiderId();
					for(int j=i; j<riderRanks.length; j++) {
						temp = riderRanks[j];
						riderRanks[j] = prev;
						prev = temp;
						if(prev == -1) {
							break;
						}
					}
					break;
				}
			}
		}
		return riderRanks;
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		int[] riderRanks = this.getRidersRankInStage(stageId);
		LocalTime[] out = new LocalTime[riderRanks.length];
		for(int i=0; i<out.length; i++) {
			Result r = Result.getResult(stageId, riderRanks[i]);
			LocalTime[] checkpoints = r.getCheckpoints();
			LocalTime[] adjustedTimes = r.adjustedCheckpoints();
			out[i] = adjustedTimes[0];
			// adjusted splits measured from adjusted start time
			LocalTime adjustedSplit;
			for(int j=0; j<adjustedTimes.length; j++) {
				adjustedSplit = Result.getElapsed(adjustedTimes[j], checkpoints[j]);
				// adjusted per segment
				out[i] = out[i].plusHours(adjustedSplit.getHour());
				out[i] = out[i].plusMinutes(adjustedSplit.getMinute());
				out[i] = out[i].plusSeconds(adjustedSplit.getSecond());
				out[i] = out[i].plusNanos(adjustedSplit.getNano());
			}
		}
		return out;
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		StageType type = Stage.getStageType(stageId);
		int[] points = new int[Result.getResultsInStage(stageId).length];
		int[] distribution = new int[15];
		// distributions from https://en.wikipedia.org/wiki/Points_classification_in_the_Tour_de_France
		// The points to be awarded in order for the stage
		switch(type) {
			case FLAT:
				distribution = new int[]{50,30,20,18,16,14,12,10,8,7,6,5,4,3,2};
				break;
			case MEDIUM_MOUNTAIN:
				distribution = new int[]{30,25,22,19,17,15,13,11,9,7,6,5,4,3,2};
				break;
			case HIGH_MOUNTAIN:
				distribution = new int[]{20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};
				break;
			case TT:
				distribution = new int[]{20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};
				break;
		}
		for(int i=0; i<Math.min(points.length, distribution.length); i++) {
			points[i] = distribution[i];
		}
		// check for SPRINT checkpoints
		distribution = new int[]{20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};
		ArrayList<Integer> ridersArray = new ArrayList<Integer>();
		for(int r : getRidersRankInStage(stageId)) { ridersArray.add(r); }
		// converts RRIS from int[] to ArrayList
		int[] segments = Stage.getSegments(stageId);
		Result[] results;
		for(int s=0; s<segments.length; s++) {
			if(Segment.getSegmentType(segments[s]).equals(SegmentType.SPRINT)) {
				// get ranks for segment
				results = Result.getResultsInStage(stageId);
				int[] riderRanks = new int[results.length];
				Arrays.fill(riderRanks, -1); // 0 may be a rider id
				for(Result r : results) {
					for(int i=0; i<riderRanks.length; i++) {
						if(riderRanks[i] == -1) {
							riderRanks[i] = r.getRiderId();
							break;
						} else if(r.getCheckpoints()[s].isBefore(Result.getResult(stageId, riderRanks[i]).getCheckpoints()[s])) {
							int temp;
							int prev = r.getRiderId();
							for(int j=i; j<riderRanks.length; j++) {
								temp = riderRanks[j];
								riderRanks[j] = prev;
								prev = temp;
								if(prev == -1) {
									break;
								}
							}
							break;
						}
					}
				}
				// adds points to position of rider in overall ranking
				for(int i=0; i<Math.min(points.length, distribution.length); i++) {
					int overallPos = ridersArray.indexOf(riderRanks[i]);
					if(overallPos<points.length && overallPos!=-1) {
						points[overallPos] += distribution[i];
					}
				}
			}
		}
		return points;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		Result[] results = Result.getResultsInStage(stageId);
		// All results refering to the stage with id *stageId*
		int[] riders = getRidersRankInStage(stageId);
		// An int array of rider ids, from first to last
		int[] segments = Stage.getSegments(stageId);
		// An int array of the segment ids in the stage
		int[] points = new int[riders.length];
		// The int in position i is the number of points to be awarded to the rider with id riders[i]
		for(int s=0; s<segments.length; s++) {
			SegmentType type = Segment.getSegmentType(segments[s]);
			int[] distribution = new int[1];
			// The points to be awarded in order for the segment
			switch(type) {
				case C4:
					distribution = new int[]{1};
					break;
				case C3:
					distribution = new int[]{2,1};
					break;
				case C2:
					distribution = new int[]{5,3,2,1};
					break;
				case C1:
					distribution = new int[]{10,8,6,4,2,1};
					break;
				case HC:
					distribution = new int[]{20,15,12,10,8,6,4,2};
					break;
				case SPRINT:
			}
			// get ranks for segment
			int[] riderRanks = new int[results.length];
			Arrays.fill(riderRanks, -1); // 0 may be a rider id
			for(Result r : results) {
				for(int i=0; i<riderRanks.length; i++) {
					if(riderRanks[i] == -1) {
						riderRanks[i] = r.getRiderId();
						break;
					} else if(r.getCheckpoints()[s].isBefore(Result.getResult(stageId, riderRanks[i]).getCheckpoints()[s])) {
						int temp;
						int prev = r.getRiderId();
						for(int j=i; j<riderRanks.length; j++) {
							temp = riderRanks[j];
							riderRanks[j] = prev;
							prev = temp;
							if(prev == -1) {
								break;
							}
						}
						break;
					}
				}
			}
			// adds points to position of rider in overall ranking
			ArrayList<Integer> ridersArray = new ArrayList<Integer>();
			for(int r : riders) { ridersArray.add(r); }
			for(int i=0; i<Math.min(points.length, distribution.length); i++) {
				int overallPos = ridersArray.indexOf(riderRanks[i]);
				if(overallPos<points.length && overallPos!=-1) {
					points[overallPos] += distribution[i];
				}
			}
		}
		return points;
	}

	@Override
	public void eraseCyclingPortal() {
		Team.teamNames.clear();
		Team.teamTopId = 0;
		Rider.ridersTopId = 0;

		RiderManager.allRiders.clear();
		RiderManager.allTeams.clear();


		Race.allRaces.clear();
		Race.removedIds.clear();
		Race.loadId();

		Segment.allSegments.clear();
		Segment.removedIds.clear();
		Segment.loadId();

		Stage.allStages.clear();
		Stage.removedIds.clear();
		Stage.loadId();

		Result.allResults.clear();
	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			ArrayList<ArrayList> allObj = new ArrayList<>();
			allObj.add(RiderManager.allTeams);
			allObj.add(RiderManager.allRiders);
			allObj.add(Stage.allStages);
			allObj.add(Stage.removedIds);
			allObj.add(Race.allRaces);
			allObj.add(Race.removedIds);
			allObj.add(Result.allResults);
			allObj.add(Segment.allSegments);
			allObj.add(Segment.removedIds);

			oos.writeObject(allObj);

			oos.flush();
			oos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	/**
     * @param raceId filename String - A vaild race Id to get the the riders rank in order.
     * @throws IOException name String - Has to be non-null or IllegalArgumentException is thrown.
	 * @throws ClassNotFoundException if in the save file their is a non specified Class.
     */
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		try {
 
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
			ArrayList<Object> allObjects = new ArrayList<>();
			ArrayList<Team> allTeams = new ArrayList<>();
			ArrayList<Rider> allRiders = new ArrayList<>();
			ArrayList<Result> allResults = new ArrayList<Result>();
			ArrayList<Race> allRaces = new ArrayList<Race>();
			ArrayList<Stage> allStages = new ArrayList<Stage>();
			ArrayList<Segment> allSegments = new ArrayList<Segment>();
			ArrayList<Integer> removedIds = new ArrayList<>();
			
			Class<?> classFlag = null;

			allObjects = (ArrayList) ois.readObject(); //Get all objects in the filename
			for (Object tempObj : allObjects){
				ArrayList Objects = (ArrayList) tempObj; //Convert all the objects to an arrayList
			for (Object obj : Objects){
				if (classFlag != null){
					if (obj.getClass() != classFlag && obj.getClass() != Integer.class){ //Get our defiend classs and add all the removeds ids back from save
						if (classFlag == Race.class){
							Race.removedIds = removedIds;
						}
						if (classFlag == Segment.class){
							Segment.removedIds = removedIds;
						}
						if (classFlag == Stage.class){
							Stage.removedIds = removedIds;
						}
						classFlag = null;
						removedIds.clear();


					}
					else{
						Integer removedId = (Integer) obj; //Fix removedIds
						removedIds.add(removedId);

					}
				}
				String objClass = obj.getClass().getName();
				System.out.println(objClass);					// Do all the magic of added each class we need
				if (obj.getClass() == Rider.class){ 
					Rider newRider = (Rider) obj;
					allRiders.add(newRider);
					System.out.println("NEW RIDER");
				}
				if (obj.getClass() == Team.class){
					Team newTeam = (Team) obj;
					allTeams.add(newTeam);
					System.out.println("NEW TEAM");
				}
				if (obj.getClass() == Result.class){
					Result newResult = (Result) obj;
					allResults.add(newResult);
					System.out.println("NEW RESULT");
				}
				if (obj.getClass() == Stage.class){
					Stage newStage = (Stage) obj;
					allStages.add(newStage);
					System.out.println("NEW STAGE");
					classFlag = Stage.class;
				}
				if (obj.getClass() == Race.class){
					Race newRace = (Race) obj;
					allRaces.add(newRace);
					System.out.println("NEW Race");
					classFlag = Race.class;
				}
				if (obj.getClass() == Segment.class){
					Segment newSeg = (Segment) obj;
					allSegments.add(newSeg);
					System.out.println("NEW SEGMENT");
					classFlag = Segment.class;
				}
			
				
				System.out.println(obj.getClass());
			}
		}
		if (classFlag == Race.class){ // Add last removed IDs 
			Race.removedIds = removedIds;
		}
		if (classFlag == Segment.class){
			Segment.removedIds = removedIds;
		}
		if (classFlag == Stage.class){
			Stage.removedIds = removedIds;
		}

			this.riderManager.setAllTeams(allTeams); //Load all team reiders ids etc etc.
			this.riderManager.setAllRiders(allRiders);
			Race.allRaces = allRaces;
			Race.loadId();
			Stage.allStages = allStages;
			Stage.loadId();
			Segment.allSegments = allSegments;
			Segment.loadId();
			Result.allResults = allResults;
            ois.close();
 
        }
		catch (Exception ex) {
            ex.printStackTrace();
        }

	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		boolean found = false;
		for (int raceId : Race.getAllRaceIds()){
			try {
				if (name == Race.getRaceName(raceId)){
					Race.removeRace(raceId);
				}
			}
			catch(Exception c){
				assert(false); // Exception will not throw by for each condition
				// This try catch is easier than moving exceptions to CyclingPortal level
			}

		}
		if (!found){ throw new NameNotRecognisedException("Name not in System.");}
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		Race currentRace = Race.getRace(raceId); //get race
		int[] stageIds = currentRace.getStages(); // get all stages in the race
		int[] riderIds = this.riderManager.getRiderIds(); // get all rider Ids in the system
		HashMap<Integer,Long> riderElaspedTime = new HashMap<Integer,Long>(); //Rider Id : totalTime (long)
		for (int riderId : riderIds){
			riderElaspedTime.put(riderId,0L); // (Map all RiderId's to a long of total time starting of 0) (Total time is in nano seconds hence the long)
		}
		for (int stageId : stageIds){ 
			Result[] temp = Result.getResultsInStage(stageId); //Get all the resuts in the each stage
			for(Result result: temp){ //get the rider id of each stage and their timeTaken.
				int riderId = result.getRiderId();
				LocalTime getTotalElasped = result.getTotalElasped();
				long timeTaken = getTotalElasped.toNanoOfDay();
				Long newTime = (Long)riderElaspedTime.get(riderId)+timeTaken;
				riderElaspedTime.put(riderId,newTime); // update the hash map
			}
			
		}
		long[][] riderTimePos = new long[riderIds.length][2]; //2d arr to hold riderId and timetaken
		int count = 0;
		for (int riderId : riderIds){
			Long finalRiderTime = riderElaspedTime.get(riderId);// ## -> [[time,riderId],....] sort by time!
			riderTimePos[count][0] = riderId;
			riderTimePos[count][1] = finalRiderTime;
			count++;
		}
		Arrays.sort(riderTimePos, Comparator.comparingDouble(o -> o[1]));
		LocalTime[] finalTimes = new LocalTime[riderIds.length];
		count = 0;
		for (long[] items : riderTimePos){
			finalTimes[count]= LocalTime.ofNanoOfDay(items[1]);
			count++;
		}



		return finalTimes;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		ArrayList<Integer> order = new ArrayList<Integer>();
		for(int riderId : getRidersGeneralClassificationRank(raceId)) {
			order.add(riderId); // converts GCR from int[] to ArrayList
		}
		int[] out = new int[order.size()];
		int[] stageRank, stagePoints;
		for(int stageId : Race.getStages(raceId)) {
			stageRank = getRidersRankInStage(stageId);
			stagePoints = getRidersPointsInStage(stageId);
			for(int i=0; i<stageRank.length; i++) {
				out[order.indexOf(stageRank[i])] += stagePoints[i];
				// orders points from stagePoints by order using stageRank
			}
		}
		return out;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		ArrayList<Integer> order = new ArrayList<Integer>();
		for(int riderId : getRidersGeneralClassificationRank(raceId)) {
			order.add(riderId); // converts GCR from int[] to ArrayList
		}
		int[] out = new int[order.size()];
		int[] stageRank, stagePoints;
		for(int stageId : Race.getStages(raceId)) {
			stageRank = getRidersRankInStage(stageId);
			stagePoints = getRidersMountainPointsInStage(stageId);
			for(int i=0; i<stageRank.length; i++) {
				out[order.indexOf(stageRank[i])] += stagePoints[i];
				// orders points from stagePoints by order using stageRank
			}
		}
		return out;
	}
	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		Race currentRace = Race.getRace(raceId); //get race
		int[] stageIds = currentRace.getStages(); // get all stages in the race
		int[] riderIds = this.riderManager.getRiderIds(); // get all rider Ids in the system
		HashMap<Integer,Long> riderElaspedTime = new HashMap<Integer,Long>(); //Rider Id : totalTime (long)
		for (int riderId : riderIds){
			riderElaspedTime.put(riderId,0L); // (Map all RiderId's to a long of total time starting of 0) (Total time is in nano seconds hence the long)
		}
		for (int stageId : stageIds){ 
			Result[] temp = Result.getResultsInStage(stageId); //Get all the resuts in the each stage
			for(Result result: temp){ //get the rider id of each stage and their timeTaken.
				int riderId = result.getRiderId();
				LocalTime getTotalElasped = result.getTotalElasped();
				long timeTaken = getTotalElasped.toNanoOfDay();
				Long newTime = (Long)riderElaspedTime.get(riderId)+timeTaken;
				riderElaspedTime.put(riderId,newTime); // update the hash map
			}
			
		}
		long[][] riderTimePos = new long[riderIds.length][2]; //2d arr to hold riderId and timetaken
		int count = 0;
		for (int riderId : riderIds){
			Long finalRiderTime = riderElaspedTime.get(riderId);// ## -> [[time,riderId],....] sort by time!
			riderTimePos[count][0] = riderId;
			riderTimePos[count][1] = finalRiderTime;
			count++;
		}
		Arrays.sort(riderTimePos, Comparator.comparingDouble(o -> o[1])); // Sort by time.
		int[] finalPos = new int[riderIds.length];
		count = 0;
		for (long[] items : riderTimePos){
			finalPos[count]= (int)items[0];
			count++;
		}

		return finalPos;
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		ArrayList<Integer> order = new ArrayList<Integer>();
		for(int riderId : getRidersGeneralClassificationRank(raceId)) {
			order.add(riderId); // converts GCR from int[] to ArrayList
		}
		int[] points = getRidersPointsInRace(raceId);
		int[] out = new int[order.size()];
		for(int i=0; i<out.length; i++) {
			int maxPoints = -1;
			int nextId = -1;
			for(int j=0; j<order.size(); j++) {
				int id = order.get(j);
				if(id<0) { continue; }
				if(points[id] > maxPoints) {
					maxPoints = points[j];
					nextId = id;
				}
				// fetches highest points
			}
			if(maxPoints < 0) {
				break;
			} else {
				out[i] = nextId;
				order.set(order.indexOf(nextId), -1);
				// adds id to out, removes from check (order)
			}
		}
		return out;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		ArrayList<Integer> order = new ArrayList<Integer>();
		for(int riderId : getRidersGeneralClassificationRank(raceId)) {
			order.add(riderId); // converts GCR from int[] to ArrayList
		}
		int[] points = getRidersMountainPointsInRace(raceId);
		int[] out = new int[order.size()];
		for(int i=0; i<out.length; i++) {
			int maxPoints = -1;
			int nextId = -1;
			for(int j=0; j<order.size(); j++) {
				int id = order.get(j);
				if(id<0) { continue; }
				if(points[id] > maxPoints) {
					maxPoints = points[j];
					nextId = id;
				}
				// fetches highest points
			}
			if(maxPoints < 0) {
				break;
			} else {
				out[i] = nextId;
				order.set(order.indexOf(nextId), -1);
				// adds id to out, removes from check (order)
			}
		}
		return out;
	}
}
