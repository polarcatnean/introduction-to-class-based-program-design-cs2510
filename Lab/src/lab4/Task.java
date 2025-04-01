package lab4;
import tester.Tester;

class Task {
  int id;          // unique
  ILoPrereq prereq;  // ids of the tasks that need to be completed before
  
  Task(int id, ILoPrereq prereq) {
    this.id = id;
    this.prereq = prereq;
  }
  
  // can this task be done given a list prereq done
  public boolean canBeDone(ILoPrereq prereqDone) {
    return this.prereq.subset(prereqDone);
  }
}
  

interface ILoTask {
  // produces the list of the tasks one can complete from these tasks (this ILoTask)
  ILoTask getCompletableTasks();

  ILoTask doTasks(ILoPrereq prereqDone, ILoTask taskDone, ILoTask todo);
  
  // produce true if at least one task in this list can be done
  boolean canBeDone(ILoPrereq prereqDone);
  
  // reverse the order of tasks in this list
  ILoTask reverse();
  ILoTask reverseHelper(ILoTask rsf);
}

class MtLoTask implements ILoTask {

  public ILoTask getCompletableTasks() {
    return this;
  }

  public ILoTask doTasks(ILoPrereq prereqDone, ILoTask taskDone, ILoTask todo) {
    // if there is still any task that can be done, do
    if (todo.canBeDone(prereqDone)) {
      return todo.doTasks(prereqDone, taskDone, new MtLoTask());
    }
    // terminate when todo list is empty OR when no task can be done anymore
    return taskDone.reverse();
  }

  public boolean canBeDone(ILoPrereq prereqDone) {
    return false;
  }

  public ILoTask reverse() {
    return this;
  }

  public ILoTask reverseHelper(ILoTask rsf) {
    return rsf;
  }
  
}

class ConsLoTask implements ILoTask {
  Task first;
  ILoTask rest;
  
  ConsLoTask(Task first, ILoTask rest) {
    this.first = first;
    this.rest = rest;
  }

  // fields: ...this.first... ...this.rest...
  // methods: ...this.first.canBeDone()...
  
  public ILoTask getCompletableTasks() {
    return this.doTasks(new MtPrereq(), new MtLoTask(), new MtLoTask());
  }
  
  // do tasks and put in a listof taskDone, given a listof prereqDone
  public ILoTask doTasks(ILoPrereq prereqDone, ILoTask taskDone, ILoTask todo) {
    if (this.first.canBeDone(prereqDone)) {
      // add this.first in the taskDone 
      // and update prereqDone by adding this.first.id
      return this.rest.doTasks(new ConsPrereq(this.first.id, prereqDone), 
                                new ConsLoTask(this.first, taskDone), 
                                 todo);
    }
    
    return this.rest.doTasks(prereqDone, taskDone, new ConsLoTask(this.first, todo));
  }

  public boolean canBeDone(ILoPrereq prereqDone) {
    return this.first.canBeDone(prereqDone) || this.rest.canBeDone(prereqDone);
  }

  public ILoTask reverse() {
    return this.rest.reverseHelper(new ConsLoTask(this.first, new MtLoTask()));
  }

  public ILoTask reverseHelper(ILoTask rsf) {
    return this.rest.reverseHelper(new ConsLoTask(this.first, rsf));
  }

}

interface ILoPrereq {
  // is this list of prereq a subset of the other list given 
  // (order doesn't matter, assume no duplicates)
  boolean subset(ILoPrereq other);
  // is this list contains given task_id
  boolean contains(int id);
  
}

class MtPrereq implements ILoPrereq {

  public boolean subset(ILoPrereq other) {
    return true;
  }

  public boolean contains(int id) {
    return false;
  }
  
}

class ConsPrereq implements ILoPrereq {
  int first;       // a task's id
  ILoPrereq rest;
  
  ConsPrereq(int first, ILoPrereq rest) {
    this.first = first;
    this.rest = rest;
  }

  public boolean subset(ILoPrereq other) {
    // TODO ...this.rest...
    if (other.contains(this.first)) {
      return this.rest.subset(other);
    }
    return false;
  }

  public boolean contains(int id) {
    if (this.first == id) {
      return true;
    }
    return this.rest.contains(id);
  }
}

class ExamplesTasks {
  ILoTask empty = new MtLoTask();
  ILoPrereq emptyreq = new MtPrereq();
  
  Task task1 = new Task(1, emptyreq);
  Task task2 = new Task(2, new ConsPrereq(1, emptyreq));
  
  // circular dependency (impossible)
  Task task6 = new Task(6, new ConsPrereq(7, emptyreq));
  Task task7 = new Task(7, new ConsPrereq(6, emptyreq));
  
  // some with prereqs
  Task task8 = new Task(8, new ConsPrereq(9, emptyreq));
  Task task9 = new Task(9, new ConsPrereq(3, emptyreq));  //task 3 has no prereq
  
  // mixed dependencies
  Task task10 = new Task(10, new ConsPrereq(11, new ConsPrereq(3, emptyreq)));
  Task task11 = new Task(11, new ConsPrereq(4, emptyreq));
  
  Task task12 = new Task(12, new ConsPrereq(13, emptyreq));
  Task task13 = new Task(13, new ConsPrereq(14, emptyreq));
  Task task14 = new Task(14, new ConsPrereq(12, emptyreq));
  
  // no prereqs
  Task task3 = new Task(3, emptyreq);
  Task task4 = new Task(4, emptyreq);
  Task task5 = new Task(5, emptyreq);
  
  ILoTask list1 = new ConsLoTask(task1, empty);
  ILoTask list2 = new ConsLoTask(task2, empty);
  ILoTask list3 = new ConsLoTask(task2, new ConsLoTask(task1, empty));
  ILoTask list4 = new ConsLoTask(task3, new ConsLoTask(task4, new ConsLoTask(task5, empty)));
  ILoTask list5 = new ConsLoTask(task6, new ConsLoTask(task7, empty));
  ILoTask list6 = new ConsLoTask(task3, new ConsLoTask(task4, new ConsLoTask(task11, empty)));
  ILoTask list7 = new ConsLoTask(task10,
      new ConsLoTask(task4, new ConsLoTask(task11, new ConsLoTask(task3, empty))));
  ILoTask list8 = new ConsLoTask(task13, new ConsLoTask(task12, new ConsLoTask(task14, empty)));
  ILoTask list9 = new ConsLoTask(task11,
      new ConsLoTask(task14,
          new ConsLoTask(task9, new ConsLoTask(task8, new ConsLoTask(task12, new ConsLoTask(task13,
              new ConsLoTask(task10, new ConsLoTask(task3, new ConsLoTask(task4, empty)))))))));
  ILoTask list10 = new ConsLoTask(task3,
      new ConsLoTask(task4, new ConsLoTask(task9, new ConsLoTask(task11, new ConsLoTask(task8,
          new ConsLoTask(task10, empty))))));
  
  
  ILoPrereq req1 = new ConsPrereq(1, emptyreq);
  ILoPrereq req2 = new ConsPrereq(1, new ConsPrereq(2, new ConsPrereq(3, emptyreq)));
  
  
  boolean testCompletable(Tester t) {
    return t.checkExpect(empty.getCompletableTasks(), empty)
        && t.checkExpect(list1.getCompletableTasks(), list1)  // one task, no prereq
        && t.checkExpect(list2.getCompletableTasks(), empty)  // one task with no completable prereq in list
        && t.checkExpect(list3.getCompletableTasks(), 
            new ConsLoTask(task1, new ConsLoTask(task2, empty)))  // one task with completable prereq in list
        && t.checkExpect(list4.getCompletableTasks(), list4)  // multiple tasks with no prereq
        && t.checkExpect(list5.getCompletableTasks(), empty)  // circular dependency
        && t.checkExpect(list6.getCompletableTasks(), list6)  // multiple tasks, some with prereqs
        && t.checkExpect(list7.getCompletableTasks(),
            new ConsLoTask(task4,
                new ConsLoTask(task11, new ConsLoTask(task3, new ConsLoTask(task10, empty)))))
        && t.checkExpect(list8.getCompletableTasks(), empty)  // bigger cycle (3)
        && t.checkExpect(list9.getCompletableTasks(), list10); // multiple tasks, with a cycle
  }
  
  boolean testContains(Tester t) {
    return t.checkExpect(emptyreq.contains(1), false)
        && t.checkExpect(req1.contains(0), false)
        && t.checkExpect(req1.contains(1), true)
        && t.checkExpect(req2.contains(2), true)
        && t.checkExpect(req2.contains(3), true);
  }
  
  boolean testSubset(Tester t) {
    return t.checkExpect(emptyreq.subset(req1), true)
        && t.checkExpect(req1.subset(req2), true)
        && t.checkExpect(req2.subset(req1), false)
        && t.checkExpect(req2.subset(req2), true);
  }
  
  boolean testReverse(Tester t) {
    return t.checkExpect(empty.reverse(), empty)
        && t.checkExpect(list4.reverse(), new ConsLoTask(task5, new ConsLoTask(task4, new ConsLoTask(task3, empty))));
  }
}
