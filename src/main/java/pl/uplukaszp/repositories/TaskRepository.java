package pl.uplukaszp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import pl.uplukaszp.domain.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
	@Override
	List<Task> findAll();

	@Query("SELECT t FROM TASK t WHERE t.device.id=?1 " + "ORDER BY DATEADD('MINUTE',MINUTE(TIME),"
			+ "DATEADD('HOUR',HOUR(TIME), " + "DATEADD('DAY_OF_WEEK',"
			+ "CASE WHEN (ISO_DAY_OF_WEEK(CURRENT_DATE)=(DAY_OF_WEEK+1)) AND (CURRENT_TIME<TIME) THEN 0 ELSE (CASE WHEN ISO_DAY_OF_WEEK(CURRENT_DATE)>=DAY_OF_WEEK THEN (7-ABS(ISO_DAY_OF_WEEK(CURRENT_DATE)-DAY_OF_WEEK)) ELSE ABS(ISO_DAY_OF_WEEK(CURRENT_DATE)-DAY_OF_WEEK) END )END,CURRENT_DATE)))")
	List<Task> findByDeviceId(String deviceId);

	@Query("SELECT t FROM TASK t " + "ORDER BY DATEADD('MINUTE',MINUTE(TIME)," + "DATEADD('HOUR',HOUR(TIME), "
			+ "DATEADD('DAY_OF_WEEK',"
			+ "CASE WHEN (ISO_DAY_OF_WEEK(CURRENT_DATE)=(DAY_OF_WEEK+1)) AND (CURRENT_TIME<TIME) THEN 0 ELSE (CASE WHEN ISO_DAY_OF_WEEK(CURRENT_DATE)>=DAY_OF_WEEK THEN (7-ABS(ISO_DAY_OF_WEEK(CURRENT_DATE)-DAY_OF_WEEK)) ELSE ABS(ISO_DAY_OF_WEEK(CURRENT_DATE)-DAY_OF_WEEK) END )END,CURRENT_DATE)))")
	List<Task> findNearestTasks();

}
