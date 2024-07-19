package com.renyibang.taskapi.repository;

import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.entity.TaskAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAccessRepository extends JpaRepository<TaskAccess, Long> {
    boolean existsByAccessorIdAndTask(long accessorId, Task task);
    TaskAccess findByTaskAndAccessorId(Task task, long accessorId);
    Object countByTask(Task task);

    Page<TaskAccess> findByTask(Task task, Pageable pageable);
}
