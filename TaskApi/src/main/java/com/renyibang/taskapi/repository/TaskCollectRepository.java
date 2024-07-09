package com.renyibang.taskapi.repository;

import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.entity.TaskCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCollectRepository extends JpaRepository<TaskCollect, Long> {
    boolean existsByCollector_idAndAndTask(long collectorId, Task task);

    TaskCollect findByTaskAndCollector_id(Task task, long collectorId);
}
