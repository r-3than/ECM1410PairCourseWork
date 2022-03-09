package cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
		new Result(stageId, riderId, checkpoints);
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		return Result.getResult(stageId, riderId).getCheckpoints();
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
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
		Result.removeResult(stageId, riderId);
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
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
			ArrayList<ArrayList> allObj = new ArrayList<>();
			allObj.add(RiderManager.allTeams);
			allObj.add(RiderManager.allRiders);
			oos.writeObject(allObj);

			oos.flush();
			oos.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		try {
 
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
			ArrayList<Object> allObjects = new ArrayList<>();
			ArrayList<Team> allTeams = new ArrayList<>();
			ArrayList<Rider> allRiders = new ArrayList<>();
			allObjects = (ArrayList) ois.readObject();
			for (Object tempObj : allObjects){
				ArrayList Objects = (ArrayList) tempObj;
			for (Object obj : Objects){
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
				System.out.println(obj.getClass());
			}
		}
			this.riderManager.setAllTeams(allTeams);
			this.riderManager.setAllRiders(allRiders);
            ois.close();
 
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
