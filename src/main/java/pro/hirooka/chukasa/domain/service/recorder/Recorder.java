//package pro.hirooka.chukasa.domain.service.recorder;
//
//import lombok.Getter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
//import org.springframework.stereotype.Component;
//import pro.hirooka.chukasa.domain.configuration.SystemConfiguration;
//import pro.hirooka.chukasa.domain.model.recorder.ReservedProgram;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//
//@Slf4j
//@Component
//public class Recorder {
//
//    @Autowired
//    SystemConfiguration systemConfiguration;
//
//    @Getter
//    Map<Integer, ScheduledFuture> scheduledFutureMap = new HashMap<>();
//
//    @Async
//    public void reserve(ReservedProgram reservedProgram){
//        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        TaskScheduler taskScheduler = new ConcurrentTaskScheduler(scheduledExecutorService);
//        Date date = new Date(reservedProgram.getStart());
//        Runnable runnable = new RecorderRunner(systemConfiguration, reservedProgram);
//        ScheduledFuture scheduledFuture = taskScheduler.schedule(runnable, date);
//        scheduledFutureMap.put(reservedProgram.getId(), scheduledFuture);
//        log.info("scheduler: {}", date.toString());
//    }
//
//    @Async
//    public void reserve(List<ReservedProgram> reservedProgramList){
//        reservedProgramList.forEach(reservedProgram -> {
//            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//            TaskScheduler taskScheduler = new ConcurrentTaskScheduler(scheduledExecutorService);
//            Date date = new Date(reservedProgram.getStart());
//            Runnable runnable = new RecorderRunner(systemConfiguration, reservedProgram);
//            ScheduledFuture scheduledFuture = taskScheduler.schedule(runnable, date);
//            scheduledFutureMap.put(reservedProgram.getId(), scheduledFuture);
//        });
//    }
//
//    public void cancel(int id){
//        if(scheduledFutureMap.containsKey(id)){
//            scheduledFutureMap.get(id).cancel(true);
//        }
//    }
//
//    public void cancelAll(){
//        scheduledFutureMap.values().stream().forEach(scheduledFuture -> {
//            scheduledFuture.cancel(true);
//        });
//    }
//
//}
