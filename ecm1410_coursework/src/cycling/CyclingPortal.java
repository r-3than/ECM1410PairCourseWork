package cycling;

import java.util.Arrays;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;



/**
 * CyclingPortal (//TODO ADD STUFF LATER )
 * 
 * 
 * @author Ethan Ray & Thomas Newbold
 * @version 1.0
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
		// TODO Check inputs?
		return Stage.addSegmentToStage(stageId, location, SegmentType.SPRINT, 0.0, location);
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
			throw new DuplicatedResultException();
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
		return Result.getResult(stageId, riderId).getCheckpoints();
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
			elapsedTime.plusHours(t.getHour()).plusMinutes(t.getMinute()).plusSeconds(t.getSecond()).plusNanos(t.getNano());
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
		Arrays.fill(riderRanks, -1);
		for(Result r : results) {
			for(int i=0; i<riderRanks.length; i++) {
				if(riderRanks[i] == -1) {
					riderRanks[i] = r.getRiderId();
				} else {
					Result compare = Result.getResult(stageId, riderRanks[i]);
					if(r.getCheckpoints()[-1].isBefore(compare.getCheckpoints()[-1])) {
						int temp;
						int prev = r.getRiderId();
						for(int j=i; j<riderRanks.length; j++) {
							temp = riderRanks[j];
							riderRanks[j] = prev;
							if(j!=riderRanks.length) {
								if(riderRanks[j+1] == -1) {
									break;
								}
								prev = riderRanks[j+1];
							}
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eraseCyclingPortal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		try {
 
            FileInputStream fos = new FileInputStream(filename);
            ObjectInputStream oos = new ObjectInputStream(fos);
 
			Object obj = oos.readObject();
            CyclingPortal CycObj = (CyclingPortal) obj;
			this.riderManager = CycObj.riderManager;
 
            oos.close();
 
        }
		catch (Exception ex) {
            ex.printStackTrace();
        }

	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		// TODO Auto-generated method stub
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

}
