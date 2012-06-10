//
//  TaskState.m
//  ccdream
//
//  Created by wei zhang on 12-6-6.
//  Copyright 2012年 Clican. All rights reserved.
//

#import "TaskState.h"
#import "Listener.h"
#import "Event.h"
#import "Session.h"
#import "State.h"
#import "Variable.h"
#import "WorkflowConstants.h"
#import "EngineContext.h"

@implementation TaskState

@synthesize taskListeners = _taskListeners;
@synthesize taskName = _taskName;
@synthesize assignees = _assignees;
@synthesize taskType= _taskType;

-(id) init {
    self = [super init];
    if(self){
        self.taskListeners = [[[NSMutableArray alloc] init] autorelease];
    }
    return self;
}

-(void) handle:(Event*) event{
    @try{
        [super propagateVariables:event];
        if([event.eventType isEqualToString:EVENT_TYPE_JOB]){
            
        }else if([event.eventType isEqualToString:EVENT_TYPE_TASK]){
            NSString* taskIdStr = [self getVariableValueForEvent:event variableName:PARAM_TASK_ID nested:NO];
            NSString* taskAssigneeStr = [self getVariableValueForEvent:event variableName:PARAM_TASK_ASSIGNEE nested:NO];
            Task* task = nil;
            
            for (Task* t in event.state.tasks) {
                if(taskIdStr!=nil&&[taskIdStr length]>0){
                    if(t.taskId == [taskIdStr longLongValue]){
                        task = t;
                        break;
                    } 
                }else if(taskAssigneeStr!=nil&&[taskAssigneeStr length]>0){
                    if([t.assignee isEqualToString:taskAssigneeStr]){
                        task = t;
                        break;
                    } 
                }
            }
            if(task ==nil){
                [NSException raise:@"Task not found" format:@"taskId=%@",taskIdStr];
            }
            [self setTaskVariable:task variables:event.variables];
            @try {
                for (id<TaskListener> taskListener in self.taskListeners) {
                    [taskListener beforeHandleTask:task];
                }
                [self handleTask:task];
            }
            @finally {
                for (id<TaskListener> taskListener in self.taskListeners) {
                    [taskListener afterHandleTask:task];
                }
            }
        }else{
            [NSException raise:@"Handle event error for state" format:@"state name=%@",self.name];
        }
    }@catch (NSException* e) {
        CCLOGERROR(@"Exception occured:%@",e);
        @throw e;
    }
    BOOL allCompleted = YES;
    for (Task* task in event.state.tasks) {
        if(task.completeTime==nil){
            allCompleted =NO;
        }
    }
    if(allCompleted){
        [super onEnd:event.state event:event];
    }
}

-(void) handleTask:(Task*) task {
    task.completeTime = [NSDate date];
}

-(void) setTaskVariable:(Task*) task variables:(NSArray*) variables{
    for (Variable* var in variables) {
        [task.variables addObject:[Variable copyFromVariable:var]];
    }
}
-(State*) onStart:(Session*) session istate:(IState*) previousState event:(Event*) event{
    State* state = [super onStart:session istate:previousState event:event];
    if (![state.status isEqualToString:STATUS_ACTIVE]) {
        return state;
    }
    [self assignTasks:state event:event];
    return state;
}

-(NSMutableArray*) assignTasks:(State*) state event:(Event*) event{
    NSMutableArray* tasks = [[[NSMutableArray alloc] init] autorelease];
    NSArray* assigneeArray = [self.assignees componentsSeparatedByString:@","];
    for (NSString* assignee in assigneeArray) {
        Task* task = [[[Task alloc] init] autorelease];
        task.taskId = [[EngineContext sharedEngineContext] getAndAddAtomicLong];
        task.assignee = assignee;
        task.assignTime =[NSDate date];
        task.name = self.taskName;
        task.type = self.taskType;
        task.state = state;
        task.endTime = [NSDate date];
        for (id<TaskListener> taskListener in self.timeoutListeners) {
            [taskListener beforeAssignTask:task];
        }
        [state.tasks addObject:task];
        [tasks addObject:task];
        for (id<TaskListener> taskListener in self.timeoutListeners) {
            [taskListener afterAssignTask:task];
        }
    }
    return tasks;
}

- (void)dealloc {
    [_taskListeners release];
    _taskListeners = nil;
    [_taskName release];
    _taskName = nil;
    [_assignees release];
    _assignees = nil;
    [_taskType release];
    _taskType = nil;
    [super dealloc];
}

@end
