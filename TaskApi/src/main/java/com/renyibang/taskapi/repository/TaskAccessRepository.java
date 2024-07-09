package com.renyibang.taskapi.repository;

import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.entity.TaskAccess;
import com.renyibang.taskapi.entity.TaskCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAccessRepository extends JpaRepository<TaskAccess, Long> {
    boolean existsByAccessor_idAndTask(long accessorId, Task task);
    TaskAccess findByTaskAndAccessor_id(Task task, long accessorId);
}
